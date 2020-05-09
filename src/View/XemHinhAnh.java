/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author Admin
 */
public final class XemHinhAnh extends javax.swing.JFrame {

    BufferedImage theImage;

    public XemHinhAnh(BufferedImage theImage) {
        initComponents();
        this.setResizable(false);
        this.theImage = theImage;
        loadHinhAnh();
    }

    void loadHinhAnh() {
        try {
            int w = theImage.getWidth();
            int h = theImage.getHeight();
            if (w > lblHinhanh.getWidth()) {
                int hieu = w - lblHinhanh.getWidth();
                w = lblHinhanh.getWidth();
                h = h - hieu;
            }
            Image i = theImage.getScaledInstance(w, h, Image.SCALE_SMOOTH);
            lblHinhanh.setIcon(new ImageIcon(i));
            lblChuvi.setText("Chiều rộng: " + theImage.getWidth() + " px - Chiều cao: " + theImage.getHeight() + " px");
        } catch (Exception e) {
            System.out.println("loi loadHinhAnh " + e);
        }

    }

    void luuhinh() {
        try {
            JFileChooser jdc = new JFileChooser("C:\\Users\\Admin\\Desktop");
            int re = jdc.showOpenDialog(null);
            File f = jdc.getSelectedFile();
            if (re == JFileChooser.APPROVE_OPTION) {
                String path=f.getPath();
                path+=".png";
                File outputfile = new File(path);
                System.out.println("Đường dẫn lưu hình: "+outputfile);
                ImageIO.write(theImage, "png", outputfile);
            }else{
                System.out.println("chưa chọn file");
            }
            
        } catch (Exception e) {
            System.out.println("loi luu hinh " + e);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        lblChuvi = new javax.swing.JLabel();
        lblHinhanh = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Xem hình ảnh");

        jPanel2.setBackground(new java.awt.Color(153, 255, 153));

        jButton1.setBackground(new java.awt.Color(255, 255, 255));
        jButton1.setText("LƯU ẢNH");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        lblChuvi.setText("Chiều rộng");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblChuvi)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblChuvi))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        lblHinhanh.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblHinhanh.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblHinhanh, javax.swing.GroupLayout.PREFERRED_SIZE, 680, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblHinhanh, javax.swing.GroupLayout.PREFERRED_SIZE, 425, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        luuhinh();
    }//GEN-LAST:event_jButton1ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JLabel lblChuvi;
    private javax.swing.JLabel lblHinhanh;
    // End of variables declaration//GEN-END:variables
}
