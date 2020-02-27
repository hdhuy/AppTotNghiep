/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import javax.swing.JOptionPane;

public class Login extends javax.swing.JFrame {

    
    public Login() {
        this.setUndecorated(true);
        initComponents();
        setUp();
    }
    private void setUp(){
        try {
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Lỗi setup \n"+e);
        }
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel3 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        pnlBelow = new javax.swing.JPanel();
        lblTendangnhap = new javax.swing.JLabel();
        txtTdn = new javax.swing.JTextField();
        pnlFooter = new javax.swing.JPanel();
        pnlFooter1 = new javax.swing.JPanel();
        pass = new javax.swing.JPasswordField();
        lblTendangnhap1 = new javax.swing.JLabel();
        btnDangnhap = new javax.swing.JButton();
        btnThoat = new javax.swing.JButton();
        lblBackground = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Đăng nhập");
        setBackground(new java.awt.Color(102, 255, 102));
        setResizable(false);

        jPanel3.setBackground(new java.awt.Color(51, 51, 51));

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/close.png"))); // NOI18N
        jLabel4.setName("lblClose"); // NOI18N
        jLabel4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblClose_Click(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Tiệm giày");

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 0, 204));
        jLabel2.setText("DA XU Ô <3");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addContainerGap())
        );

        pnlBelow.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblTendangnhap.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblTendangnhap.setText("Tên đăng nhập");
        pnlBelow.add(lblTendangnhap, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 20, 120, -1));

        txtTdn.setBackground(new java.awt.Color(122, 184, 245));
        txtTdn.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtTdn.setBorder(null);
        txtTdn.setPreferredSize(new java.awt.Dimension(9, 20));
        pnlBelow.add(txtTdn, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 50, 270, 30));

        pnlFooter.setBackground(new java.awt.Color(255, 153, 204));
        pnlFooter.setPreferredSize(new java.awt.Dimension(258, 3));

        javax.swing.GroupLayout pnlFooterLayout = new javax.swing.GroupLayout(pnlFooter);
        pnlFooter.setLayout(pnlFooterLayout);
        pnlFooterLayout.setHorizontalGroup(
            pnlFooterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        pnlFooterLayout.setVerticalGroup(
            pnlFooterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 3, Short.MAX_VALUE)
        );

        pnlBelow.add(pnlFooter, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 80, 270, 3));

        pnlFooter1.setBackground(new java.awt.Color(255, 153, 204));
        pnlFooter1.setPreferredSize(new java.awt.Dimension(258, 3));

        javax.swing.GroupLayout pnlFooter1Layout = new javax.swing.GroupLayout(pnlFooter1);
        pnlFooter1.setLayout(pnlFooter1Layout);
        pnlFooter1Layout.setHorizontalGroup(
            pnlFooter1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        pnlFooter1Layout.setVerticalGroup(
            pnlFooter1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 3, Short.MAX_VALUE)
        );

        pnlBelow.add(pnlFooter1, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 160, 270, 3));

        pass.setBackground(new java.awt.Color(149, 178, 244));
        pass.setBorder(null);
        pnlBelow.add(pass, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 130, 270, 30));

        lblTendangnhap1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblTendangnhap1.setText("Mật khẩu");
        pnlBelow.add(lblTendangnhap1, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 100, 87, -1));

        btnDangnhap.setBackground(new java.awt.Color(255, 51, 51));
        btnDangnhap.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnDangnhap.setForeground(new java.awt.Color(255, 255, 255));
        btnDangnhap.setText("Thoát");
        btnDangnhap.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDangnhapActionPerformed(evt);
            }
        });
        pnlBelow.add(btnDangnhap, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 240, 90, -1));

        btnThoat.setBackground(new java.awt.Color(153, 204, 255));
        btnThoat.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        btnThoat.setForeground(new java.awt.Color(102, 102, 102));
        btnThoat.setText("Đăng nhập");
        btnThoat.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                btnLogin_mouseMoved(evt);
            }
        });
        pnlBelow.add(btnThoat, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 190, 270, 40));

        lblBackground.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/hinh nen dang nhap.jpg"))); // NOI18N
        lblBackground.setText("dsdsf");
        pnlBelow.add(lblBackground, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, -30, 490, 320));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlBelow, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(pnlBelow, javax.swing.GroupLayout.PREFERRED_SIZE, 281, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void lblClose_Click(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblClose_Click
        System.exit(1);
    }//GEN-LAST:event_lblClose_Click

    private void btnLogin_mouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLogin_mouseMoved
        
    }//GEN-LAST:event_btnLogin_mouseMoved

    private void btnDangnhapActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDangnhapActionPerformed
        System.exit(1);
    }//GEN-LAST:event_btnDangnhapActionPerformed

    public static void main(String args[]) {
        
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Login().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDangnhap;
    private javax.swing.JButton btnThoat;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JLabel lblBackground;
    private javax.swing.JLabel lblTendangnhap;
    private javax.swing.JLabel lblTendangnhap1;
    private javax.swing.JPasswordField pass;
    private javax.swing.JPanel pnlBelow;
    private javax.swing.JPanel pnlFooter;
    private javax.swing.JPanel pnlFooter1;
    private javax.swing.JTextField txtTdn;
    // End of variables declaration//GEN-END:variables
}
