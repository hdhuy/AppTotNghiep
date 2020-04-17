
package View;

import Model.OrderApp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author Admin
 */
public class InHoaDon {
    List<HoaDon> listhoadon= new ArrayList();
    OrderApp o=new OrderApp();

    public InHoaDon(List<HoaDon> listhoadon,OrderApp o) {
        this.listhoadon=listhoadon;
        this.o=o;
    }
    
    public void in(){
        try {
            
            JRBeanCollectionDataSource list = new JRBeanCollectionDataSource(listhoadon);
            Map<String, Object> parameters = new HashMap<String, Object>();
            parameters.put("list", list);
            parameters.put("mahoadon", o.getId()+"");
            parameters.put("manhanvien", o.getUser().getId()+"");
            parameters.put("tgian", o.getCreatedAt()+"");
            parameters.put("diachi", "169 Mỹ Đinh - Nam Từ Liêm - Hà Nội");
            parameters.put("fanpage", "facebook/top_shoe_shop");
            parameters.put("tongtien", o.getTotalAmount()+" đ");
            parameters.put("sodienthoai", "0109213127");
            JasperPrint jasperPrint = JasperFillManager.fillReport("src/View/report.jasper", parameters, new JREmptyDataSource());
            JasperViewer.viewReport(jasperPrint);
        } catch (Exception e) {
            System.out.println("loi in "+e);
        }
    }
}
