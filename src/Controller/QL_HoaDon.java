package Controller;

import Model.HoaDon;
import Model.thongke;
import Pojos.OrderApp;
import Pojos.OrderAppDetail;
import Pojos.OrderWeb;
import Pojos.OrderWebDetail;
import Pojos.Product;
import Pojos.ProductSize;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;

public class QL_HoaDon {

    DataTransacter data = new DataTransacter();

    public List<OrderApp> layHoaDon(String txt) {
        List<OrderApp> re = new ArrayList();
        try {
            re = data.select(txt);
        } catch (Exception e) {
            System.out.println("loi hoa don - tra cuu");
        }
        return re;
    }

    public List<OrderAppDetail> layChiTietHoaDonApp(String id) {
        List<OrderAppDetail> re = new ArrayList();
        try {
            String txt = "from OrderAppDetail where orderApp=" + id;
            re = data.select(txt);
            for (OrderAppDetail e : re) {
                List<ProductSize> lps = data.select("from ProductSize where id=" + e.getProductSize().getId());
                if (lps.size() > 0) {
                    ProductSize ps = lps.get(0);
                    List<Product> p = data.select("from Product where id=" + ps.getProduct().getId());
                    if (p.size() > 0) {
                        ps.setProduct(p.get(0));
                        e.setProductSize(ps);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("loi layChiTietHoaDon " + e);
        }
        return re;
    }

    public List<OrderWebDetail> layChiTietHoaDonWeb(String id) {
        List<OrderWebDetail> re = new ArrayList();
        try {
            String txt = "from OrderWebDetail where orderWeb=" + id;
            re = data.select(txt);
            for (OrderWebDetail e : re) {
                List<ProductSize> lps = data.select("from ProductSize where id=" + e.getProductSize().getId());
                if (lps.size() > 0) {
                    ProductSize ps = lps.get(0);
                    List<Product> p = data.select("from Product where id=" + ps.getProduct().getId());
                    if (p.size() > 0) {
                        ps.setProduct(p.get(0));
                        e.setProductSize(ps);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("loi layChiTietHoaDon " + e);
        }
        return re;
    }

    public boolean banGiaoTien(String txt) {
        boolean re = false;
        try {
            List<OrderApp> oa = data.select(txt);
            if (oa.size() > 0) {
                oa.stream().map((e) -> {
                    e.setUpdatedAt(new Date());
                    return e;
                }).map((e) -> {
                    e.setStatus("Đã giao tiền");
                    return e;
                }).forEach((e) -> {
                    data.update(e);
                });
                re = true;
            }
        } catch (Exception e) {
            System.out.println("loi hoa don - ban giao tien " + e);
        }
        return re;
    }

    public void inHoaDon(List<OrderAppDetail> load, OrderApp o) {
        try {
            List<HoaDon> lhd = new ArrayList();
            load.stream().map((e) -> {
                HoaDon hd = new HoaDon();
                hd.setId(e.getProductSize().getProduct().getId());
                hd.setTen(e.getProductSize().getProduct().getName());
                hd.setSoluong(e.getQuantity());
                hd.setSize(e.getProductSize().getSize());
                hd.setGia(e.getPrice());
                hd.setTt(e.getTotalAmount());
                return hd;
            }).forEach((hd) -> {
                lhd.add(hd);
            });
            JRBeanCollectionDataSource list = new JRBeanCollectionDataSource(lhd);
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("list", list);
            parameters.put("mahoadon", o.getId() + "");
            parameters.put("manhanvien", o.getUser().getId() + "");
            parameters.put("tgian", o.getCreatedAt() + "");
            parameters.put("diachi", "169 Mỹ Đinh - Nam Từ Liêm - Hà Nội");
            parameters.put("fanpage", "facebook/top_shoe_shop");
            parameters.put("tongtien", o.getTotalAmount() + " đ");
            parameters.put("sodienthoai", "0109213127");
            JasperPrint jasperPrint = JasperFillManager.fillReport("src/View/report.jasper", parameters, new JREmptyDataSource());
            JasperViewer.viewReport(jasperPrint,false);
        } catch (Exception e) {
            System.out.println("loi in hoa don " + e);
        }
    }

    public List<OrderApp> locHoadonApp(thongke t) {
        List<OrderApp> re = new ArrayList();
        try {
            String txtApp = "from OrderApp";
            DataTransacter dt = new DataTransacter();
            int tongsp = 0;
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            if (t.getBangiao() == 1) {
                if ("from OrderApp".equals(txtApp)) {
                    txtApp += " where status='Đã giao tiền'";
                } else {
                    txtApp += " and status='Đã giao tiền'";
                }
            }
            if (t.getBangiao() == 2) {
                if ("from OrderApp".equals(txtApp)) {
                    txtApp += " where status='Chưa giao tiền'";
                } else {
                    txtApp += " and status='Chưa giao tiền'";
                }
            }
            if (t.isTimtheotg()) {
                if (t.isKhoangThoigian()) {
                    String strTg1 = dateFormat.format(t.getTg1());
                    String strTg2 = dateFormat.format(t.getTg2());
                    if ("from OrderApp".equals(txtApp)) {
                        txtApp += " where createdAt between '" + strTg1 + "' and '" + strTg2 + "'";
                    } else {
                        txtApp += " and createdAt between '" + strTg1 + "' and '" + strTg2 + "'";
                    }

                }
                if (t.isMocThoigian()) {
                    String strTg3 = dateFormat.format(t.getTg3());
                    if (t.getNgaythangnam() == 2) {
                        strTg3 = strTg3.substring(0, strTg3.lastIndexOf("-"));
                    }
                    if (t.getNgaythangnam() == 3) {
                        strTg3 = strTg3.substring(0, strTg3.indexOf("-"));
                    }
                    if ("from OrderApp".equals(txtApp)) {
                        txtApp += " where createdAt like '%" + strTg3 + "%'";
                    } else {
                        txtApp += " and createdAt like '%" + strTg3 + "%'";
                    }
                }
            }
            if (t.isTimtheoNhanvien()) {
                if ("from OrderApp".equals(txtApp)) {
                    txtApp += " where user=" + t.getManhanvien();
                } else {
                    txtApp += " and user=" + t.getManhanvien();
                }
            }
            System.out.println("thong ke app: "+txtApp);
            List<OrderApp> loa = dt.select(txtApp);
            for (OrderApp e : loa) {
                String txtdetail = "from OrderAppDetail where orderApp=" + e.getId();
                List<OrderAppDetail> load = dt.select(txtdetail);
                if (t.isTimtheoSanpham()) {
                    int checkid = checkIdApp(load, t.getMasanpham());
                    if (checkid > 0) {
                        re.add(e);
                    }
                    tongsp += checkid;
                } else {
                    re.add(e);
                    tongsp = load.stream().map((o) -> o.getQuantity()).reduce(tongsp, Integer::sum);
                }
            }
            t.setTongsp(tongsp);
        } catch (Exception e) {
            System.out.println("loi hoa don APP thong ke " + e);
        }
        return re;
    }

    private int checkIdApp(List<OrderAppDetail> load, String masp) {
        int re = 0;
        try {
            for (OrderAppDetail o : load) {
                String txtps = "from ProductSize where id=" + o.getProductSize().getId();
                DataTransacter dt = new DataTransacter();
                List<ProductSize> lps = dt.select(txtps);
                if (lps.size() > 0) {
                    ProductSize ps = lps.get(0);
                    if (ps.getProduct().getId().toString().equals(masp)) {
                        re += o.getQuantity();
                        break;
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("loi checkId " + e);
        }
        return re;
    }

    public List<OrderWeb> locHoadonWeb(thongke t) {
        List<OrderWeb> re = new ArrayList();
        try {
            String txtWeb = "from OrderWeb where deliveryStatus='Giao hàng thành công'";
            DataTransacter dt = new DataTransacter();
            int tongsp = 0;
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            if (t.isTimtheotg()) {
                if (t.isKhoangThoigian()) {
                    String strTg1 = dateFormat.format(t.getTg1());
                    String strTg2 = dateFormat.format(t.getTg2());
                    txtWeb += " and createdAt between '" + strTg1 + "' and '" + strTg2 + "'";
                }
                if (t.isMocThoigian()) {
                    String strTg3 = dateFormat.format(t.getTg3());
                    if (t.getNgaythangnam() == 2) {
                        strTg3 = strTg3.substring(0, strTg3.lastIndexOf("-"));
                    }
                    if (t.getNgaythangnam() == 3) {
                        strTg3 = strTg3.substring(0, strTg3.indexOf("-"));
                    }
                    txtWeb += " and createdAt like '%" + strTg3 + "%'";
                }
            }
            if (t.isTimtheoNhanvien()) {
                txtWeb += " and user=" + t.getManhanvien();
            }
            List<OrderWeb> loa = dt.select(txtWeb);
            for (OrderWeb e : loa) {
                String txtdetail = "from OrderWebDetail where orderWeb=" + e.getId();
                List<OrderWebDetail> load = dt.select(txtdetail);
                if (t.isTimtheoSanpham()) {
                    int checkid = checkIdWeb(load, t.getMasanpham());
                    if (checkid > 0) {
                        re.add(e);
                    }
                    tongsp += checkid;
                } else {
                    re.add(e);
                    tongsp = load.stream().map((o) -> o.getQuantity()).reduce(tongsp, Integer::sum);
                }
            }
            t.setTongsp(tongsp);
        } catch (Exception e) {
            System.out.println("loi hoa don web " + e);
        }
        return re;
    }

    private int checkIdWeb(List<OrderWebDetail> load, String masp) {
        int re = 0;
        try {
            for (OrderWebDetail o : load) {
                String txtps = "from ProductSize where id=" + o.getProductSize().getId();
                DataTransacter dt = new DataTransacter();
                List<ProductSize> lps = dt.select(txtps);
                if (lps.size() > 0) {
                    ProductSize ps = lps.get(0);
                    if (ps.getProduct().getId().toString().equals(masp)) {
                        re += o.getQuantity();
                        break;
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("loi checkId " + e);
        }
        return re;
    }
}
