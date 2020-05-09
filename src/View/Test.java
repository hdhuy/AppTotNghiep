/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import Model.HoaDon;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.mindrot.jbcrypt.BCrypt;

/**
 *
 * @author Admin
 */
public class Test extends javax.swing.JFrame {

    /**
     * Creates new form Test
     */
    public Test() {
        initComponents();
        //Print();
    }
    void mahoa(){
        String txt1=txtMk.getText();
        String hash = BCrypt.hashpw(txt1, BCrypt.gensalt(12));
        txtXuat.setText(hash);
        }
    void giaima(){
        //giai ma
        boolean valuate = BCrypt.checkpw(txtMk.getText(),txtXuat.getText());
        if(valuate){
            JOptionPane.showMessageDialog(null, "đúng");
        }
    }
    
    void config(String tencuahang,String fanpage,String diachi,String sodienthoai) {
        Properties prop = new Properties();
        OutputStream output = null;
        try {
            output = new FileOutputStream("src/Controller/config.properties");

            // set the properties value
            prop.setProperty("tencuahang",tencuahang);
            prop.setProperty("fanpage", fanpage);
            prop.setProperty("diachi", diachi);
            prop.setProperty("sodienthoai", sodienthoai);

            // save properties to project root folder
            prop.store(output, null);
            output.close();
        } catch (Exception io) {
            System.out.println("loi out put " + io);
        }
    }
    void config_2(){
        Properties prop = new Properties();
        OutputStream output = null;
        try {
            output = new FileOutputStream("src/Controller/config.properties");

            // set the properties value
            prop.setProperty("userss", "1ddsawddas.100");
            prop.setProperty("co", "dfdsfds");
            prop.setProperty("compa", "dsfsdf");
            prop.setProperty("comp", "sdfsdf");

            // save properties to project root folder
            prop.store(output, null);
            output.close();
        } catch (Exception e) {
            System.out.println("loi config_2 "+e);
        }
    }
    void getConfig() throws IOException {
        String result = "";
        InputStream inputStream;
        try {
            Properties prop = new Properties();
            String propFileName = "src/View/config.properties";
            inputStream = new FileInputStream(propFileName);
            if (inputStream != null) {
                prop.load(inputStream);
                String user = prop.getProperty("user");
                String company1 = prop.getProperty("company1");
                String company2 = prop.getProperty("company2");
                String company3 = prop.getProperty("company3");

                result = "Company List = " + company1 + ", " + company2 + ", " + company3 + "\nProgram Ran on by user=" + user;
                JOptionPane.showMessageDialog(null, result);
                inputStream.close();
            }else{
                JOptionPane.showMessageDialog(null, "Không tìm thấy đường dẫn");
            }
        } catch (Exception e) {
            System.out.println("Exception: " + e);
        }
    }

    void changeHibernate() {
        try {
            Configuration config = new Configuration();
            config.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
            config.setProperty("hibernate.connection.driver_class", "com.mysql.jdbc.Driver");
            config.setProperty("hibernate.connection.url", "jdbc:mysql://localhost:3306/" + "shoe_store");
            config.setProperty("hibernate.connection.username", "rootxxx");
            config.setProperty("hibernate.connection.password", "password");
            SessionFactory sessionFactory = config.buildSessionFactory();
        } catch (Exception e) {
            System.out.println("loi change hiber " + e);
        }
    }

    void Print() {
        try {
            List<HoaDon> hd = new ArrayList();
            HoaDon h1 = new HoaDon();
            h1.id = 1;
            h1.ten = "xx";
            h1.soluong = 1;
            h1.size = 33;
            h1.gia = 1000;
            h1.tt = 10200;

            HoaDon h2 = new HoaDon();
            h2.id = 3;
            h2.ten = "xxddd";
            h2.soluong = 1;
            h2.size = 33;
            h2.gia = 1000;
            h2.tt = 10200;

            hd.add(h1);
            hd.add(h2);
            JRBeanCollectionDataSource list = new JRBeanCollectionDataSource(hd);
            Map<String, Object> parameters = new HashMap<String, Object>();
            parameters.put("list", list);
            parameters.put("mahoadon", "mama");
            parameters.put("size", hd.size() + "");
            JasperPrint jasperPrint = JasperFillManager.fillReport("src/View/report.jasper", parameters, new JREmptyDataSource());
            JasperViewer.viewReport(jasperPrint);
        } catch (Exception e) {
            System.out.println("loi in " + e);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btn = new javax.swing.JButton();
        txtMk = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtXuat = new javax.swing.JTextArea();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        btn.setText("Mã hóa");
        btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnActionPerformed(evt);
            }
        });

        txtXuat.setColumns(20);
        txtXuat.setRows(5);
        jScrollPane1.setViewportView(txtXuat);

        jButton1.setText("giải mã");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 234, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btn, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txtMk))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtMk, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 114, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btn, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActionPerformed
        mahoa();
    }//GEN-LAST:event_btnActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        giaima();
    }//GEN-LAST:event_jButton1ActionPerformed

    /**
     * @param args the command line arguments
     */
//    public static void main(String args[]) {
//        /* Set the Nimbus look and feel */
//        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
//        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
//         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
//         */
//        try {
//            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
//                if ("Nimbus".equals(info.getName())) {
//                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
//                    break;
//                }
//            }
//        } catch (ClassNotFoundException ex) {
//            java.util.logging.Logger.getLogger(Test.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(Test.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(Test.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(Test.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        //</editor-fold>
//
//        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                new Test().setVisible(true);
//            }
//        });
//    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn;
    private javax.swing.JButton jButton1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField txtMk;
    private javax.swing.JTextArea txtXuat;
    // End of variables declaration//GEN-END:variables
}
