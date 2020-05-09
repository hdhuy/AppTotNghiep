package Controller;

import Pojos.Brand;
import Pojos.Product;
import Pojos.ProductColor;
import Pojos.ProductImage;
import Pojos.ProductSize;
import java.util.List;

public class QL_ThemSP {
    public String themSanPham(Product p,List<ProductSize> lps,List<ProductColor> lpc,List<ProductImage> lpi ){
        String re=null;
        try {
            DataTransacter data=new DataTransacter();
            data.insert(p);
            Product myp=themMauSizeAnh(lps, lpc, lpi);
            re="Đã thêm sản phẩm "+myp.getName()+" mã sản phẩm: "+myp.getId();
        } catch (Exception e) {
            System.out.println("loi them sp qltsp "+e);
        }
        return re;
    }
    private Product themMauSizeAnh(List<ProductSize> lps,List<ProductColor> lpc,List<ProductImage> lpi){
        Product re=new Product();
        try {
            DataTransacter data=new DataTransacter();
            String psql="from Product";
            List<Product> lp=data.select(psql);
            if(lp.size()>0){
                Product p=lp.get(lp.size()-1);
                re=p;
                if(lps!=null){
                    for (ProductSize e : lps) {
                        e.setProduct(p);
                        data.insert(e);
                    }
                }
                if(lpc!=null){
                    for(ProductColor e:lpc){
                        e.setProduct(p);
                        data.insert(e);
                    }
                }
                if(lpi!=null&&lpi.size()>0){
                    for(ProductImage e:lpi){
                        e.setProduct(p);
                        data.insert(e);
                    }
                }
            }
            
        } catch (Exception e) {
            System.out.println("loi themMauSizeAnh "+e);
        }
        return re;
    }
    
}
