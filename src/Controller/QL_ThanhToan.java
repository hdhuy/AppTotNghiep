/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Pojos.OrderApp;
import Pojos.OrderAppDetail;
import Pojos.Product;
import Pojos.ProductSize;
import Pojos.User;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Admin
 */
public class QL_ThanhToan {

    public Product timKiem(String id) {
        Product re = null;
        try {
            DataTransacter data = new DataTransacter();
            String txt = "from Product where isDelete=0 and status='Đang bán' and id=" + id;
            List<Product> lp = data.select(txt);
            if (lp.size() > 0) {
                Product p = lp.get(0);
                String txt2 = "from ProductSize where product=" + p.getId();
                List<ProductSize> lps = data.select(txt2);
                Converter cv= new Converter();
                Set<ProductSize> sps = cv.ListToSet(lps);
                if (lps.size() > 0) {
                    p.setProductSizes(sps);
                }
                re = p;
            }
        } catch (Exception e) {
            System.out.println("loi timKiem " + e);
        }
        return re;
    }


    public List<OrderAppDetail> themChiTiet_Thanhtoan(OrderAppDetail oad, List<OrderAppDetail> load) {
        List<OrderAppDetail> re = null;
        try {
            boolean isHave = false;
            if (load.isEmpty()) {
                load.add(oad);
            } else {
                for (OrderAppDetail e : load) {
                    if (e.getProductSize().getId().equals(oad.getProductSize().getId())) {
                        e.setQuantity(oad.getQuantity());
                        e.setTotalAmount(oad.getTotalAmount());
                        isHave = true;
                        break;
                    }
                }
                if (isHave == false) {
                    load.add(oad);
                }
            }
            re=load;
        } catch (Exception e) {
            System.out.println("loi them chi tiet " + e);
        }
        return re;
    }

    public void thanhToan(User user,List<OrderAppDetail> load, boolean in) {
        try {
            if (load.size() > 0) {
                DataTransacter data = new DataTransacter();
                OrderApp oa = new OrderApp();
                oa.setUser(user);
                oa.setCreatedAt(new Date());
                oa.setStatus("Chưa giao tiền");
                data.insert(oa);
                //LẤY HÓA ĐƠN VỪA THÊM
                String txt = "from OrderApp";
                List<OrderApp> loa = data.select(txt);
                OrderApp oa2 = null;
                if (loa.size() > 0) {
                    oa2 = loa.get(loa.size() - 1);
                }
                //UPDATE THÔNG TIN CHI TIẾT ĐƠN HÀNG
                int tongtien=0;
                for (OrderAppDetail e : load) {
                    e.setOrderApp(oa2);
                    data.insert(e);
                    tongtien+=e.getTotalAmount();
                }
                //UPDATE TỔNG TIỀN CHO HÓA ĐƠN
                oa2.setTotalAmount(tongtien);
                data.update(oa2);
                //GIẢM SỐ LƯỢNG SP CÒN LẠI
                giamSoLuongSP(load);
                if (in) {
                    QL_HoaDon qlhd = new QL_HoaDon();
                    qlhd.inHoaDon(load, oa2);
                }
                System.out.println("ok, thanh toan xong !");
            }
        } catch (Exception e) {
            System.out.println("loi thanh toan " + e);
        }
    }
    private void giamSoLuongSP(List<OrderAppDetail> load) {
        try {
            DataTransacter data = new DataTransacter();
            for (OrderAppDetail e : load) {
                int soluong = e.getQuantity();
                String txt = "from ProductSize where id=" + e.getProductSize().getId();
                List<ProductSize> lps = data.select(txt);
                if (lps.size() > 0) {
                    ProductSize ps = lps.get(0);
                    ps.setQuantity(ps.getQuantity() - soluong);
                    data.update(ps);
                }
            }
        } catch (Exception e) {
            System.out.println("loi giamSoLuongSP " + e);
        }
    }
    //-----------------------------------------------------------------------------------

}
