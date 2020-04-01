/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import Controller.DataTransacter;
import Model.User;
import java.awt.Color;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Icon;
import javax.swing.JOptionPane;

public class Login extends javax.swing.JFrame {

    public Login() {
        this.setUndecorated(true);
        initComponents();
        slide(1);
    }

    public void slide(int i) {
        try {
            s1.setBackground(Color.white);
            s2.setBackground(Color.white);
            s3.setBackground(Color.white);
            s4.setBackground(Color.white);
            if (i == 1) {
                s1.setBackground(Color.gray);
                Icon ic = null;
                pnlDisplay.removeAll();
                pnlDisplay.add(sl1);
                pnlDisplay.repaint();
                pnlDisplay.revalidate();
            }
            if (i == 2) {
                s2.setBackground(Color.gray);
                pnlDisplay.removeAll();
                pnlDisplay.add(sl2);
                pnlDisplay.repaint();
                pnlDisplay.revalidate();
            }
            if (i == 3) {
                s3.setBackground(Color.gray);
                pnlDisplay.removeAll();
                pnlDisplay.add(sl3);
                pnlDisplay.repaint();
                pnlDisplay.revalidate();
            }
            if (i == 4) {
                s4.setBackground(Color.gray);
                pnlDisplay.removeAll();
                pnlDisplay.add(sl4);
                pnlDisplay.repaint();
                pnlDisplay.revalidate();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Lỗi slide \n" + e);
        }
    }
    DataTransacter dt = new DataTransacter();

    private void dangnhap() {
        try {
            String name = txtTendangnhap.getText();
            String pass = txtPass.getText();
            List<User> list = dt.select("select from User where id='" + name + "' and password='" + pass + "'");
            if (list.size() > 0) {
                Working w = new Working(list.get(0));
                w.setVisible(true);
                this.setVisible(false);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Lỗi đăng nhập \n" + e);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jButton3 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        pnlHinhanh = new javax.swing.JPanel();
        pnlDisplay = new javax.swing.JPanel();
        sl1 = new javax.swing.JLabel();
        sl2 = new javax.swing.JLabel();
        sl3 = new javax.swing.JLabel();
        sl4 = new javax.swing.JLabel();
        s1 = new javax.swing.JButton();
        s2 = new javax.swing.JButton();
        s3 = new javax.swing.JButton();
        s4 = new javax.swing.JButton();
        pnlDangnhap = new javax.swing.JPanel();
        lblTieudedangnhap = new javax.swing.JLabel();
        txtTendangnhap = new javax.swing.JTextField();
        pnlLot = new javax.swing.JPanel();
        lblTieudematkhau = new javax.swing.JLabel();
        pnlLot1 = new javax.swing.JPanel();
        txtPass = new javax.swing.JPasswordField();
        jButton2 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Đăng nhập");
        setBackground(new java.awt.Color(102, 255, 102));
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jButton3.setBackground(new java.awt.Color(255, 255, 255));
        jButton3.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButton3.setText("Đóng");
        jButton3.setBorder(null);
        jButton3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton3.setDebugGraphicsOptions(javax.swing.DebugGraphics.NONE_OPTION);
        jButton3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 102, 102));
        jLabel1.setText("Cửa hàng giày Top Shoe");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 303, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(5, 5, 5))
        );

        jPanel2.setBackground(new java.awt.Color(255, 102, 102));

        pnlHinhanh.setBackground(new java.awt.Color(51, 255, 51));

        pnlDisplay.setBackground(new java.awt.Color(51, 51, 255));
        pnlDisplay.setPreferredSize(new java.awt.Dimension(10, 441));
        pnlDisplay.setLayout(new java.awt.CardLayout());

        sl1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/slide-1.png"))); // NOI18N
        pnlDisplay.add(sl1, "card2");

        sl2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/slide-2.png"))); // NOI18N
        pnlDisplay.add(sl2, "card3");

        sl3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/slide-3.png"))); // NOI18N
        pnlDisplay.add(sl3, "card4");

        sl4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/slide-4.png"))); // NOI18N
        pnlDisplay.add(sl4, "card5");

        s1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        s1.setName("s1"); // NOI18N
        s1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                s1ActionPerformed(evt);
            }
        });

        s2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        s2.setName("s2"); // NOI18N
        s2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                s2ActionPerformed(evt);
            }
        });

        s3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        s3.setName("s3"); // NOI18N
        s3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                s3ActionPerformed(evt);
            }
        });

        s4.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        s4.setName("s4"); // NOI18N
        s4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                s4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlHinhanhLayout = new javax.swing.GroupLayout(pnlHinhanh);
        pnlHinhanh.setLayout(pnlHinhanhLayout);
        pnlHinhanhLayout.setHorizontalGroup(
            pnlHinhanhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlDisplay, javax.swing.GroupLayout.DEFAULT_SIZE, 456, Short.MAX_VALUE)
            .addGroup(pnlHinhanhLayout.createSequentialGroup()
                .addGap(167, 167, 167)
                .addComponent(s1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(s2, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(s3, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(s4, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlHinhanhLayout.setVerticalGroup(
            pnlHinhanhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlHinhanhLayout.createSequentialGroup()
                .addComponent(pnlDisplay, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlHinhanhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(s1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(s2, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(s3, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(s4, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        pnlDangnhap.setBackground(new java.awt.Color(255, 102, 102));

        lblTieudedangnhap.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        lblTieudedangnhap.setText("Mã nhân viên");

        txtTendangnhap.setBackground(new java.awt.Color(255, 102, 102));
        txtTendangnhap.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        txtTendangnhap.setForeground(new java.awt.Color(255, 255, 255));
        txtTendangnhap.setBorder(null);

        pnlLot.setBackground(new java.awt.Color(255, 255, 255));
        pnlLot.setPreferredSize(new java.awt.Dimension(0, 2));

        javax.swing.GroupLayout pnlLotLayout = new javax.swing.GroupLayout(pnlLot);
        pnlLot.setLayout(pnlLotLayout);
        pnlLotLayout.setHorizontalGroup(
            pnlLotLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        pnlLotLayout.setVerticalGroup(
            pnlLotLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 2, Short.MAX_VALUE)
        );

        lblTieudematkhau.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        lblTieudematkhau.setText("Mật khẩu");

        pnlLot1.setBackground(new java.awt.Color(255, 255, 255));
        pnlLot1.setPreferredSize(new java.awt.Dimension(0, 2));

        javax.swing.GroupLayout pnlLot1Layout = new javax.swing.GroupLayout(pnlLot1);
        pnlLot1.setLayout(pnlLot1Layout);
        pnlLot1Layout.setHorizontalGroup(
            pnlLot1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        pnlLot1Layout.setVerticalGroup(
            pnlLot1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 2, Short.MAX_VALUE)
        );

        txtPass.setBackground(new java.awt.Color(255, 102, 102));
        txtPass.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        txtPass.setForeground(new java.awt.Color(255, 255, 255));
        txtPass.setBorder(null);

        jButton2.setBackground(new java.awt.Color(255, 0, 0));
        jButton2.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setText("ĐĂNG NHẬP");
        jButton2.setBorder(null);
        jButton2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton2.setName("btnDangnhap"); // NOI18N
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/TopShoeLogo.png"))); // NOI18N

        javax.swing.GroupLayout pnlDangnhapLayout = new javax.swing.GroupLayout(pnlDangnhap);
        pnlDangnhap.setLayout(pnlDangnhapLayout);
        pnlDangnhapLayout.setHorizontalGroup(
            pnlDangnhapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDangnhapLayout.createSequentialGroup()
                .addGap(81, 81, 81)
                .addGroup(pnlDangnhapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlDangnhapLayout.createSequentialGroup()
                .addContainerGap(37, Short.MAX_VALUE)
                .addGroup(pnlDangnhapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lblTieudematkhau)
                    .addComponent(pnlLot1, javax.swing.GroupLayout.DEFAULT_SIZE, 265, Short.MAX_VALUE)
                    .addComponent(txtTendangnhap)
                    .addComponent(pnlLot, javax.swing.GroupLayout.DEFAULT_SIZE, 265, Short.MAX_VALUE)
                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtPass, javax.swing.GroupLayout.PREFERRED_SIZE, 265, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblTieudedangnhap))
                .addGap(31, 31, 31))
        );
        pnlDangnhapLayout.setVerticalGroup(
            pnlDangnhapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlDangnhapLayout.createSequentialGroup()
                .addComponent(jLabel2)
                .addGap(24, 24, 24)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(24, 24, 24)
                .addComponent(lblTieudedangnhap)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtTendangnhap, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(pnlLot, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(44, 44, 44)
                .addComponent(lblTieudematkhau, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtPass, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(pnlLot1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(45, 45, 45)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(98, 98, 98))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(pnlHinhanh, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(pnlDangnhap, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlHinhanh, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(pnlDangnhap, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        dangnhap();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        System.exit(0);
    }//GEN-LAST:event_jButton3ActionPerformed

    private void s1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_s1ActionPerformed
        slide(1);
    }//GEN-LAST:event_s1ActionPerformed

    private void s2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_s2ActionPerformed
        slide(2);
    }//GEN-LAST:event_s2ActionPerformed

    private void s3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_s3ActionPerformed
        slide(3);
    }//GEN-LAST:event_s3ActionPerformed

    private void s4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_s4ActionPerformed
        slide(4);
    }//GEN-LAST:event_s4ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JLabel lblTieudedangnhap;
    private javax.swing.JLabel lblTieudematkhau;
    private javax.swing.JPanel pnlDangnhap;
    private javax.swing.JPanel pnlDisplay;
    private javax.swing.JPanel pnlHinhanh;
    private javax.swing.JPanel pnlLot;
    private javax.swing.JPanel pnlLot1;
    private javax.swing.JButton s1;
    private javax.swing.JButton s2;
    private javax.swing.JButton s3;
    private javax.swing.JButton s4;
    private javax.swing.JLabel sl1;
    private javax.swing.JLabel sl2;
    private javax.swing.JLabel sl3;
    private javax.swing.JLabel sl4;
    private javax.swing.JPasswordField txtPass;
    private javax.swing.JTextField txtTendangnhap;
    // End of variables declaration//GEN-END:variables

}
