package Controller;

import Pojos.OrderApp;
import Pojos.OrderWeb;
import Pojos.User;
import java.util.List;

public class QL_ThanhVien {

    public User layThanhVien(String manv) {
        User re = new User();
        try {
            DataTransacter dt = new DataTransacter();
            List<User> list = dt.select("from User where id=" + manv);
            if (list.size() > 0) {
                re = list.get(0);
            }
        } catch (Exception e) {
            System.out.println("loi layNhanVien " + e);
        }
        return re;
    }

    public boolean CapnhatTV(User u) {
        boolean re = true;
        try {
            DataTransacter dt = new DataTransacter();
            if (u.getRole().equals("ROLE_ADMIN")) {
                String txt = "from User where Role='ROLE_ADMIN'";
                List<User> lu = dt.select(txt);
                if (lu.size() == 1) {
                    re = false;
                } else {
                    boolean re2 = dt.update(u);
                    if (re2 == false) {
                        System.out.println("lôi cập nhật thành viên qltv");
                    }
                }
            } else {
                boolean re2 = dt.update(u);
                if (re2 == false) {
                    System.out.println("lôi cập nhật thành viên qltv");
                }
            }

        } catch (Exception e) {
            System.out.println("loi caop nat tv qltv " + e);
        }
        return re;
    }
    public boolean xoaThanhvien(User u){
        boolean re=true;
        try {
            boolean isHave=false;
            DataTransacter dt= new DataTransacter();
            String txt1="from OrderApp where user="+u.getId();
            List<OrderApp> loa=dt.select(txt1);
            if(loa.size()>0){
                isHave=true;
            }
            String txt2="from OrderWeb where user="+u.getId();
            List<OrderWeb> low=dt.select(txt2);
            if(low.size()>0){
                isHave=true;
            }
            if(isHave==false){
               re= dt.delete(u);
            }else{
                u.setIsDelete(true);
                re=dt.update(u);
            }
        } catch (Exception e) {
            System.out.println("xoa thanh vien "+e);
        }
        return re;
    }
}
