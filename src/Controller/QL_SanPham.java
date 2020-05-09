package Controller;

import Pojos.Brand;
import Pojos.Category;
import Pojos.Color;
import Pojos.OrderAppDetail;
import Pojos.OrderWebDetail;
import Pojos.Product;
import Pojos.ProductColor;
import Pojos.ProductImage;
import Pojos.ProductSize;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

public class QL_SanPham {

    List<Brand> listBrand = new ArrayList();
    List<Category> listCategory = new ArrayList();
    List<Color> listColor = new ArrayList();

    List<Product> listProduct = new ArrayList();

    public void layNhanhieu_Loai_Mau() {
        try {
            DataTransacter data = new DataTransacter();
            String txtlb = "from Brand";
            listBrand = data.select(txtlb);
            String txtlc = "from Category";
            listCategory = data.select(txtlc);
            String txtco = "from Color";
            listColor = data.select(txtco);
        } catch (Exception e) {
            System.out.println("loi layNhanhieuLoai " + e);
        }
    }
//<editor-fold defaultstate="collapsed" desc="TRA CỨU SẢN PHẨM">

    //<editor-fold defaultstate="collapsed" desc="HÀM TRA CỨU CŨ">
//    public void timSPtraCuu(int iBrand, int iCate, int iColor, String tt, String tk) {
//        String txt = "from Product";
//        try {
//            DataTransacter data = new DataTransacter();
//            //loc nhan hieu
//            if (iBrand >= 0) {
//                txt += " where brand=" + listBrand.get(iBrand).getId();
//            }
//            //loc loai
//            if (iCate >= 0) {
//                if (!"from Product".equals(txt)) {
//                    txt += " and category=" + listCategory.get(iCate).getId();
//                } else {
//                    txt += " where category=" + listCategory.get(iCate).getId();
//                }
//            }
//            //trang thai san pham
//            if (!tt.equals("Tất cả")) {
//
//                if (!"from Product".equals(txt)) {
//                    txt += " and status="+tt;
//                } else {
//                    txt += " where status="+tt;
//                }
//
//            }
//            if (tk != null && tk.length() > 0) {
//                if (!"from Product".equals(txt)) {
//                    txt += " and name like '%" + tk + "%'"
//                            + " or id like '%" + tk + "%'";
//                } else {
//                    txt += " where name like '%" + tk + "%'"
//                            + " or id like '%" + tk + "%'";
//                }
//            }
//            listProduct = data.select(txt);
//            //màu sản phẩm
//            if (iColor >= 0) {
//                List<Product> newlist = new ArrayList();
//                Color c = listColor.get(iColor);
//                for (Product e : listProduct) {
//                    String txt2 = "from ProductColor where product=" + e.getId();
//                    List<ProductColor> lpc = data.select(txt2);
//                    if (lpc.size() > 0) {
//                        for (ProductColor a : lpc) {
//                            if (a.getColor().getId().equals(c.getId())) {
//                                newlist.add(e);
//                                break;
//                            }
//                        }
//                    }
//                }
//                if (newlist.size() > 0) {
//                    listProduct = newlist;
//                }
//
//            }
//
//        } catch (Exception e) {
//            System.out.println("loi timSPtraCuu " + e);
//        }
//
//    }
    //</editor-fold>
    public void timSPtraCuu(int iBrand, int iCate, int iColor, String tt, String tk) {
        String txt = "from Product where isDelete=0";
        try {
            DataTransacter data = new DataTransacter();
            if (iBrand >= 0) {
                txt += " and brand=" + listBrand.get(iBrand).getId();
            }
            if (iCate >= 0) {
                txt += " and category=" + listCategory.get(iCate).getId();
            }
            if (!tt.equals("Tất cả")) {
                txt += " and status='" + tt + "'";
            }
            if (tk != null && tk.length() > 0) {
                txt += " and name like '%" + tk + "%'"
                        + " or id like '%" + tk + "%'";
            }
            listProduct = data.select(txt);
            if (iColor >= 0) {
                List<Product> newlist = new ArrayList();
                Color c = listColor.get(iColor);
                for (Product e : listProduct) {
                    String txt2 = "from ProductColor where product=" + e.getId();
                    List<ProductColor> lpc = data.select(txt2);
                    if (lpc.size() > 0) {
                        for (ProductColor a : lpc) {
                            if (a.getColor().getId().equals(c.getId())) {
                                newlist.add(e);
                                break;
                            }
                        }
                    }
                }
                if (newlist.size() > 0) {
                    listProduct = newlist;
                }

            }

        } catch (Exception e) {
            System.out.println("loi timSPtraCuu " + e);
        }

    }

    public String laySPToMau(String id) {
        String re = "white";
        try {
            String txt = "from ProductSize where product=" + id;
            DataTransacter data = new DataTransacter();
            List<ProductSize> lps = data.select(txt);
            if (lps.size() > 0) {
                for (ProductSize e : lps) {
                    if (e.getQuantity() <= 5) {
                        re = "yellow";
                    }
                    if (e.getQuantity() == 0) {
                        re = "red";
                        break;
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("loi layDsToMauSP " + e);
        }
        return re;
    }

    public Product layChiTietSP(String id) {
        Product re = new Product();
        try {
            DataTransacter data = new DataTransacter();
            String txt = "from Product where id=" + id;
            List<Product> lp = data.select(txt);
            if (lp.size() > 0) {
                Product p = lp.get(0);
                String txt2 = "from Brand where id=" + p.getBrand().getId();
                List<Brand> lb = data.select(txt2);
                if (lb.size() > 0) {
                    Brand b = lb.get(0);
                    String txt3 = "from Category where id=" + p.getCategory().getId();
                    List<Category> lc = data.select(txt3);
                    if (lc.size() > 0) {
                        Category c = lc.get(0);
                        p.setBrand(b);
                        p.setCategory(c);
                        re = p;
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("loi lay chi tiet " + e);
        }
        return re;
    }

    public List<ProductSize> layProductSize(String id) {
        List<ProductSize> re = new ArrayList();
        try {
            DataTransacter data = new DataTransacter();
            String txt = "from ProductSize where product=" + id;
            List<ProductSize> lp = data.select(txt);
            if (lp.size() > 0) {
                re = lp;
            }
        } catch (Exception e) {
            System.out.println("loi layProductSize " + e);
        }
        return re;
    }

    public int kiemTraSoLuongSP(String id) {
        int re = 0;
        try {
            DataTransacter data = new DataTransacter();
            String txt = "from ProductSize where product=" + id;
            List<ProductSize> lps = data.select(txt);
            if (lps.size() > 0) {
                for (ProductSize e : lps) {
                    if (e.getQuantity() <= 5) {
                        re = 1;
                        if (e.getQuantity() == 0) {
                            re = 2;
                        }
                    }
                }
            }
        } catch (Exception e) {

        }
        return re;
    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="CẬP NHẬT SẢN PHẨM">
    public Product timKiem(String id) {
        Product re = null;
        try {
            DataTransacter data = new DataTransacter();
            String txt = "from Product where isDelete=0 and id=" + id;
            List<Product> lp = data.select(txt);
            if (lp.size() > 0) {
                Product p = lp.get(0);
                String txt2 = "from ProductSize where product=" + p.getId();
                List<ProductSize> lps = data.select(txt2);
                Converter cv = new Converter();
                if (lps.size() > 0) {
                    Set<ProductSize> sps = cv.ListToSet(lps);
                    p.setProductSizes(sps);
                }
                String txt3 = "from ProductColor where product=" + p.getId();
                List<ProductColor> lpc = data.select(txt3);
                if (lpc.size() > 0) {
                    Set<ProductColor> spc = cv.ListToSet(lpc);
                    p.setProductColors(spc);
                }
                String txt4 = "from ProductImage where product=" + p.getId();
                List<ProductImage> lip = data.select(txt4);
                if (lip.size() > 0) {
                    Set<ProductImage> spi = cv.ListToSet(lip);
                    p.setProductImages(spi);
                }
                re = p;
            } else {
                System.out.println("Không tìm thấy sp qlsp!");
            }
        } catch (Exception e) {
            System.out.println("loi timKiem " + e);
        }
        return re;
    }

    public int layViTriNhanhieu(long id) {
        int re = 0;
        try {
            String txt = "from Brand";
            DataTransacter data = new DataTransacter();
            List<Brand> lb = data.select(txt);
            int i = 0;
            for (Brand o : lb) {
                if (o.getId() == id) {
                    re = i;
                    break;
                }
                i++;
            }
        } catch (Exception e) {
            System.out.println("loi layDsNhanhieu " + e);
        }
        return re;
    }

    public int layViTriLoai(long id) {
        int re = 0;
        try {
            String txt = "from Category";
            DataTransacter data = new DataTransacter();
            List<Category> lb = data.select(txt);
            int i = 0;
            for (Category o : lb) {
                if (o.getId() == id) {
                    re = i;
                    break;
                }
                i++;
            }
        } catch (Exception e) {
            System.out.println("loi layDsNhanhieu " + e);
        }
        return re;
    }

    public List<ProductColor> laydsMauSP(String id) {
        List<ProductColor> re = new ArrayList();
        try {
            DataTransacter data = new DataTransacter();
            String txt = "from ProductColor where product=" + id;
            List<ProductColor> lc = data.select(txt);
            if (lc.size() > 0) {
                re = lc;
            }
        } catch (Exception e) {
            System.out.println("loi layMauSP " + e);
        }
        return re;
    }

    public Color layMau(long id) {
        Color re = new Color();
        try {
            DataTransacter data = new DataTransacter();
            String txt = "from Color where id=" + id;
            List<Color> lc = data.select(txt);
            if (lc.size() > 0) {
                re = lc.get(0);
            }
        } catch (Exception e) {
            System.out.println("loi layMau " + e);
        }
        return re;
    }

    public List<ProductSize> laydsSizeSP(String id) {
        List<ProductSize> re = new ArrayList();
        try {
            DataTransacter data = new DataTransacter();
            String txt = "from ProductSize where product=" + id;
            List<ProductSize> lc = data.select(txt);
            if (lc.size() > 0) {
                re = lc;
            }
        } catch (Exception e) {
            System.out.println("loi size sp " + e);
        }
        return re;
    }

    public List<ProductImage> laydsAnh(String id) {
        List<ProductImage> re = new ArrayList();
        try {
            DataTransacter data = new DataTransacter();
            String txt = "from ProductImage where product=" + id;
            List<ProductImage> lpi = data.select(txt);
            if (lpi.size() > 0) {
                re = lpi;
            }
        } catch (Exception e) {
        }
        return re;
    }

    public Brand layNhanHieu(int i) {
        Brand re = null;
        try {
            DataTransacter data = new DataTransacter();
            List<Brand> lb = data.select("from Brand");
            if (lb.size() > i) {
                re = lb.get(i);
            }
        } catch (Exception e) {
            System.out.println("loi lay nhan hieu " + e);
        }
        return re;
    }

    public Category layLoai(int i) {
        Category re = null;
        try {
            DataTransacter data = new DataTransacter();
            List<Category> lb = data.select("from Category");
            if (lb.size() > i) {
                re = lb.get(i);
            }
        } catch (Exception e) {
            System.out.println("loi lay loai " + e);
        }
        return re;
    }

    public byte[] layHinhAnh(String id) {
        byte[] re = null;
        try {
            String txt = "from ProductImage where id=" + id;
            DataTransacter data = new DataTransacter();
            List<ProductImage> lpi = data.select(txt);
            if (lpi.size() > 0) {
                re = lpi.get(0).getData();
            }
        } catch (Exception e) {
            System.out.println("loi lay hinh anh " + e);
        }
        return re;
    }

    public boolean CapNhatSP(Product p) {
        boolean re = true;
        try {
            DataTransacter data = new DataTransacter();
            if (data.update(p) == false) {
                re = false;
            }
//            String txt = "from Product where id=" + id;
//            List<Product> lp = data.select(txt);
//            if (lp.size() > 0) {
//                Product kq = lp.get(0);
//                kq.setName(p.getName());
//                kq.setPrice(p.getPrice());
//                kq.setBrand(p.getBrand());
//                kq.setCategory(p.getCategory());
//                kq.setIsDelete(p.isIsDelete());
//                kq.setDescription(p.getDescription());
//                kq.setCreatedAt(p.getCreatedAt());
//                kq.setUpdatedAt(p.getUpdatedAt());
//                kq.setVersionName(p.getVersionName());
//                if (data.update(kq) == false) {
//                    re = false;
//                }
//            }
        } catch (Exception e) {
            System.out.println("loi CapNhatSP - qlsp" + e);
        }
        return re;
    }

    public boolean xoaAnSP(String id) {
        boolean re = true;
        try {
            DataTransacter data = new DataTransacter();
            String txt = "from Product where id=" + id;
            List<Product> lp = data.select(txt);
            if (lp.size() > 0) {
                Product p = lp.get(0);
                p.setIsDelete(true);
                p.setUpdatedAt(new Date());
                p.setBrand(null);
                p.setCategory(null);
                re = data.update(p);
                String txt2 = "from ProductImage where product=" + p.getId();
                List<ProductImage> lpi = data.select(txt2);
                if (lpi.size() > 0) {
                    for (ProductImage e : lpi) {
                        data.delete(e);
                    }
                }
                String txt3 = "from ProductColor where product=" + p.getId();
                List<ProductColor> lpc = data.select(txt3);
                if (lpc.size() > 0) {
                    for (ProductColor e : lpc) {
                        data.delete(e);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("loi xoa an qlspn " + e);
        }
        return re;
    }

    public boolean xoaSP(String id) {
        boolean re = true;
        try {
            DataTransacter data = new DataTransacter();
            String txt = "from Product where id=" + id;
            List<Product> lp = data.select(txt);
            Product kq = null;
            if (lp.size() > 0) {
                kq = lp.get(0);
                String pstxt = "from ProductSize where product=" + kq.getId();
                List<ProductSize> lps = data.select(pstxt);
                if (lps.size() > 0) {
                    for (ProductSize e : lps) {
                        String oadtxt = "from OrderAppDetail where productSize=" + e.getId();
                        List<OrderAppDetail> load = data.select(oadtxt);
                        if (load.size() > 0) {
                            re = false;
                            break;
                        }
                        String owdtxt = "from OrderWebDetail where productSize=" + e.getId();
                        List<OrderWebDetail> lowd = data.select(owdtxt);
                        if (lowd.size() > 0) {
                            re = false;
                            break;
                        }
                    }
                }
            }
            //NẾU XÓA ĐƯỢC THI XÓA
            if (re && kq != null) {
                String pstxt = "from ProductSize where product=" + kq.getId();
                List<ProductSize> lps = data.select(pstxt);
                if (lps.size() > 0) {
                    for (ProductSize e : lps) {
                        data.delete(e);
                    }
                }
                String cltxt = "from ProductColor where product=" + kq.getId();
                List<ProductColor> lpc = data.select(cltxt);
                if (lps.size() > 0) {
                    for (ProductColor e : lpc) {
                        data.delete(e);
                    }
                }
                String imtxt = "from ProductImage where product=" + kq.getId();
                List<ProductImage> lpi = data.select(imtxt);
                if (lps.size() > 0) {
                    for (ProductImage e : lpi) {
                        data.delete(e);
                    }
                }
                data.delete(kq);
                System.out.println("XÓA SẢN PHẨM XONG !");
            }
        } catch (Exception e) {
            System.out.println("loi xoa qlsp " + e);
        }
        return re;
    }

    public boolean themSize(String id, int size, int soluong) {
        boolean re = true;
        try {
            DataTransacter data = new DataTransacter();
            String txt = "from ProductSize where product=" + id + " and size=" + size;
            List<ProductSize> list = data.select(txt);
            if (list.isEmpty()) {
                ProductSize ps = new ProductSize();
                ps.setSize(size);
                ps.setQuantity(soluong);
                long pr = Long.parseLong(id);
                Product p = new Product();
                p.setId(pr);
                ps.setProduct(p);
                ps.setCreatedAt(new Date());
                ps.setUpdatedAt(new Date());
                data.insert(ps);
            } else {
                re = false;
            }
        } catch (Exception e) {
            System.out.println("loi them size " + e);
        }
        return re;
    }

    public boolean xoaSize(String id) {
        boolean re = true;
        try {
            String txt = "from ProductSize where id=" + id;
            DataTransacter data = new DataTransacter();
            List<ProductSize> lps = data.select(txt);
            if (lps.size() > 0) {
                re = data.delete(lps.get(0));
            }
        } catch (Exception e) {
            System.out.println("loi xoa size qlsp " + e);
        }
        return re;
    }

    public boolean capNhatSoluong(String id, int quantity) {
        boolean re = true;
        try {
            String txt = "from ProductSize where id=" + id;
            DataTransacter data = new DataTransacter();
            List<ProductSize> lps = data.select(txt);
            if (lps.size() > 0) {
                ProductSize ps = lps.get(0);
                if (ps.getQuantity() != quantity) {
                    ps.setQuantity(quantity);
                    ps.setUpdatedAt(new Date());
                    re = data.update(ps);
                } else {
                    re = false;
                }
            }
        } catch (Exception e) {
            System.out.println("loi xoa size qlsp " + e);
        }
        return re;
    }

    public void themMau(String id, Color c) {
        try {
            DataTransacter data = new DataTransacter();
            ProductColor pc = new ProductColor();
            pc.setColor(c);
            pc.setCreatedAt(new Date());
            pc.setUpdatedAt(new Date());
            Product p = new Product();
            long lid = Long.parseLong(id);
            p.setId(lid);
            pc.setProduct(p);
            data.insert(pc);
        } catch (Exception e) {
            System.out.println("loi them mau qlsp " + e);
        }
    }

    public boolean capNhatMau(String id, Color c) {
        boolean re = true;
        try {
            DataTransacter data = new DataTransacter();
            String txt = "from ProductColor where id=" + id;
            List<ProductColor> list = data.select(txt);
            if (list.size() > 0) {
                ProductColor p = list.get(0);
                p.setColor(c);
                p.setUpdatedAt(new Date());
                re = data.update(p);
            }
        } catch (Exception e) {
            System.out.println("loi xoa mau qlsp " + e);
        }
        return re;
    }

    public boolean kiemTraTrungMau(String pid, String cid) {
        boolean re = true;
        try {
            DataTransacter data = new DataTransacter();
            String kttxt = "from ProductColor where product=" + pid + " and color=" + cid;
            List<ProductColor> list = data.select(kttxt);
            if (list.size() > 0) {
                re = false;
            }
        } catch (Exception e) {
        }
        return re;
    }

    public boolean xoaMau(String id) {
        boolean re = true;
        try {
            DataTransacter data = new DataTransacter();
            String txt = "from ProductColor where id=" + id;
            List<ProductColor> list = data.select(txt);
            if (list.size() > 0) {
                re = data.delete(list.get(0));
            }
        } catch (Exception e) {
            System.out.println("loi xoa mau qlsp " + e);
        }
        return re;
    }

    public boolean themAnh(String spid, String path, byte[] anh) {
        boolean re = true;
        try {
            long pid = Long.parseLong(spid);

            Product p = new Product();
            p.setId(pid);

            ProductImage pi = new ProductImage();
            pi.setProduct(p);
            pi.setCreatedAt(new Date());
            pi.setUpdatedAt(new Date());
            pi.setData(anh);
            pi.setPath(path);
            pi.setIsPrimary(false);
            pi.setSize(anh.length);
            DataTransacter data = new DataTransacter();
            String txt = "from ProductImage where product=" + spid;
            List<ProductImage> lpi = data.select(txt);
            if (lpi.isEmpty()) {
                pi.setIsPrimary(true);
            }
            re = data.insert(pi);
        } catch (Exception e) {
            System.out.println("loi them anh qlsp " + e);
        }
        return re;
    }

    public boolean xoaAnh(String id, String pid) {
        boolean re = true;
        try {
            String txt = "from ProductImage where id=" + id;
            DataTransacter data = new DataTransacter();
            List<ProductImage> lpi = data.select(txt);
            if (lpi.size() > 0) {
                ProductImage pi = lpi.get(0);
                re = data.delete(pi);
                if (pi.getIsPrimary()) {
                    setAnhchinhkhixoa(data, pid);
                }
            }
        } catch (Exception e) {
            System.out.println("loi  xoa anh qlsp " + e);
        }
        return re;
    }

    private void setAnhchinhkhixoa(DataTransacter data, String id) {
        try {
            String txt2 = "from ProductImage where product=" + id;
            List<ProductImage> lpi2 = data.select(txt2);
            if (lpi2.size() > 0) {
                ProductImage pi2 = lpi2.get(0);
                pi2.setIsPrimary(true);
                data.update(pi2);
            }
        } catch (Exception e) {
            System.out.println("loi setAnhchinhkhixoa qlsp " + e);
        }
    }

    public void datLamAnhChinh(String pid, String piid) {
        try {
            String txt = "from ProductImage where product=" + pid;
            DataTransacter data = new DataTransacter();
            List<ProductImage> lpi = data.select(txt);
            if (lpi.size() > 0) {
                for (ProductImage e : lpi) {
                    if (e.getIsPrimary() == false && e.getId().toString().equals(piid)) {
                        e.setIsPrimary(true);
                        e.setUpdatedAt(new Date());
                        data.update(e);
                    } else {
                        if (e.getIsPrimary()) {
                            e.setIsPrimary(false);
                            e.setUpdatedAt(new Date());
                            data.update(e);
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("loi datLamAnhChinh qlsp " + e);
        }
    }

    public boolean capNhatAnh(String iid, String ten, byte[] anh) {
        boolean re = true;
        try {
            String txt = "from ProductImage where id=" + iid;
            DataTransacter data = new DataTransacter();
            List<ProductImage> lpi = data.select(txt);
            if (lpi.size() > 0) {
                ProductImage pi = lpi.get(0);
                pi.setData(anh);
                pi.setPath(ten);
                pi.setUpdatedAt(new Date());
                re = data.update(pi);
            }
        } catch (Exception e) {
            System.out.println("loi cap nhat anh qlsp" + e);
        }
        return re;
    }
//</editor-fold>

    public List<Brand> getListBrand() {
        return listBrand;
    }

    public void setListBrand(List<Brand> listBrand) {
        this.listBrand = listBrand;
    }

    public List<Category> getListCategory() {
        return listCategory;
    }

    public void setListCategory(List<Category> listCategory) {
        this.listCategory = listCategory;
    }

    public List<Product> getListProduct() {
        return listProduct;
    }

    public void setListProduct(List<Product> listProduct) {
        this.listProduct = listProduct;
    }

    public List<Color> getListColor() {
        return listColor;
    }

    public void setListColor(List<Color> listColor) {
        this.listColor = listColor;
    }

}
