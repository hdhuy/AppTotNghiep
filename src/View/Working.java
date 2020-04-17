package View;

import Controller.DataTransacter;
import Model.*;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import org.hibernate.cfg.Configuration;

public class Working extends javax.swing.JFrame {

    User user = new User();

    public Working(User user) {
        initComponents();
        //Tab.setEnabledAt(3, false);
        this.user = user;
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        doDuLieuTraCuu("from Product");
        dodulieuNhanhieu();
        dodulieuLoai();
        doDuLieuBangTV("from User");

        DateFormat fm = new SimpleDateFormat("yyyy-MM-dd");
        Date now = new Date();
        doDuLieuHoaDon("from OrderApp where createdAt like '%" + fm.format(now) + "%'");

        lblTennv.setText(user.getName() + " - Nhân viên thu ngân");
        resetTT();
    }
    DataTransacter dt = new DataTransacter();

    //BẢNG TRA CỨU
    private void doDuLieuTraCuu(String txt) {
        try {
            List<Product> list = dt.select(txt);
            DefaultTableModel model = (DefaultTableModel) tblTracuu.getModel();
            model.setRowCount(0);
            for (int i = 0; i < list.size(); i++) {
                Product p = list.get(i);
                model.addRow(new Object[]{i + 1, p.getId(), p.getName(), p.getPrice(), p.getColor(),
                    p.isIsDelete(), p.getCreatedAt(), p.getUpdatedAt()
                });
            }
        } catch (Exception e) {
        }
    }

    private String scriptTraCuuSP() {
        String txt = "from Product";
        try {
            List<Brand> b = dt.select("from Brand");
            List<Category> c = dt.select("from Category");
            int brandSelectedItem = cboNhanhieu.getSelectedIndex() - 1;
            int categorySelectedItem = cboLoai.getSelectedIndex() - 1;
            //loc nhan hieu
            if (brandSelectedItem >= 0) {
                txt += " where brand=" + b.get(brandSelectedItem).getId();
                //cboNhanhieu.setPrototypeDisplayValue(b.get(brandSelectedItem));
            }
            //loc loai
            if (categorySelectedItem >= 0) {
                //cboLoai.setPrototypeDisplayValue(c.get(categorySelectedItem));
                if (txt != "from Product") {
                    txt += " and category=" + c.get(categorySelectedItem).getId();
                } else {
                    txt += " where category=" + c.get(categorySelectedItem).getId();
                }
            }
            //trang thai san pham
            if (cboTrangthaisp.getSelectedIndex() > 0) {

                if (txt != "from Product") {
                    if (cboTrangthaisp.getSelectedIndex() == 1) {
                        txt += " and isDelete=0";
                    } else {
                        txt += " and isDelete=1";
                    }
                } else {
                    if (cboTrangthaisp.getSelectedIndex() == 1) {
                        txt += " where isDelete=0";
                    } else {
                        txt += " where isDelete=1";
                    }
                }

            }
            if (txtTimkiemtracuu.getText() != null && txtTimkiemtracuu.getText().length() > 0) {
                if (txt != "from Product") {
                    txt += " and name like '%" + txtTimkiemtracuu.getText() + "%'"
                            + " or color like '%" + txtTimkiemtracuu.getText() + "%'"
                            + " or price like '%" + txtTimkiemtracuu.getText() + "%'";
                } else {
                    txt += " where name like '%" + txtTimkiemtracuu.getText() + "%'"
                            + " or color like '%" + txtTimkiemtracuu.getText() + "%'"
                            + " or price like '%" + txtTimkiemtracuu.getText() + "%'";
                }
            }
        } catch (Exception e) {
            System.out.println("Loc sai: " + e);
        }
        return txt;
    }

    private void tblTracuuClick() {
        try {
            DefaultTableModel model = (DefaultTableModel) tblSizeTracuu.getModel();
            model.setRowCount(0);
            lblNhanhieu.setText("");
            lblLoai.setText("");
            lblTrangthai.setText("");
            if (tblTracuu.getRowCount() > 0) {
                int index = tblTracuu.getSelectedRow();
                String id = tblTracuu.getValueAt(index, 1).toString();
                String txtSp = "from Product where id=" + id;
                List<Product> lp = dt.select(txtSp);
                if (lp.size() > 0) {
                    if (lp.get(0).isIsDelete() == false) {
                        lblTrangthai.setText("Đang bán");
                    } else {
                        lblTrangthai.setText("Tạm ngưng");
                    }

                    String txtBr = "from Brand where id=" + lp.get(0).getBrand().getId();
                    List<Brand> lb = dt.select(txtBr);
                    if (lb.size() > 0) {
                        lblNhanhieu.setText(lb.get(0).getName());
                    }

                    String txtCa = "from Category where id=" + lp.get(0).getCategory().getId();
                    List<Category> lc = dt.select(txtCa);
                    if (lc.size() > 0) {
                        lblLoai.setText(lc.get(0).getName());
                    }
                }
                String txt = "from ProductSize where product=" + id;
                List<ProductSize> list = dt.select(txt);
                if (list.size() > 0) {
                    for (ProductSize ps : list) {
                        model.addRow(new Object[]{ps.getId(), ps.getSize(), ps.getQuantity(), ps.getCreatedAt(), ps.getUpdatedAt()});
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("loi tblTracuuClick " + e);
        }
    }

    //QUẢN LÍ SẢN PHẨM
    //----------------------------------------------------------------------------
    //String txtImgCapnhat = "";
    private List<Brand> dsNhanHieuCapnhat = null;
    private List<Category> dsLoaiCapnhat = null;
    private List<ProductSize> dsSizeCapnhat = null;

    private boolean dieuKienCapnhat_QLSP() {
        boolean re = false;
        try {

        } catch (Exception e) {
            System.out.println("loi dieuKienCapnhat_QLSP " + e);
        }
        return re;
    }

    private void capnhatSanpham_QLSP() {
        try {
            if (txtMaspQL.getText().length() > 0) {
                String txt = "from Product where id=" + txtMaspQL.getText();
                List<Product> list = dt.select(txt);
                if (list.size() > 0) {
                    if (JOptionPane.showConfirmDialog(null, "Bạn có muốn thay đổi thông tin của sản phẩm này ?", "Xác nhận cập nhật !", 0) == 0) {
                        Product p = list.get(0);
                        p.setName(txtTenSpCapnhat.getText());
                        p.setPrice(new Long(spnGiaSpCapnhat.getValue().toString()));
                        p.setColor(txtMauSpCapnhat.getText());
//                        if (!"".equals(txtImgCapnhat)) {
//                            p.setImage(txtImgCapnhat);
//                        }
                        p.setImage(txtAnhSpCapnhat.getText());
                        p.setDescription((txtMotaSpCapnhat.getText()));
                        int iLoai = cboLoaiCapnhat.getSelectedIndex();
                        int iNhanhieu = cboNhanhieuCapnhat.getSelectedIndex();
                        int iTrangthai = cboTrangthaiCapnhat.getSelectedIndex();
                        p.setBrand(dsNhanHieuCapnhat.get(iNhanhieu));
                        p.setCategory(dsLoaiCapnhat.get(iLoai));
                        if (iTrangthai == 0) {
                            p.setIsDelete(false);
                        } else {
                            p.setIsDelete(true);
                        }
                        p.setUpdatedAt(new Date());
                        if (dt.update(p) == false) {
                            JOptionPane.showMessageDialog(null, "Một bản ghi tương tự đã tồn tại hoặc bạn đã để trống một trường !", "Không thể cập nhật", 0);
                        } else {
                            lblTgsuasp.setText(p.getUpdatedAt().toString());
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("lỗi capnhatSanpham " + e);
        }
    }

    private void XoaSP() {
        try {
            if (txtTkspCapnhat.getText().length() > 0) {
                String id = txtTkspCapnhat.getText();
                String txt = "from Product where id=" + id;
                List<Product> list = dt.select(txt);
                if (list.size() > 0) {
                    if (dt.delete(list.get(0)) == false) {
                        JOptionPane.showMessageDialog(null, "Không thể xóa được vì dữ liệu còn tồn tại trong size và hóa đơn !");
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("xao sp");
        }
    }

    private void Reset_QLSP() {
        try {
            dsNhanHieuCapnhat = null;
            dsLoaiCapnhat = null;
            dsSizeCapnhat = null;
            spnNhapsizeCapnhat.setValue(0);
            spnNhapsoluongCapnhat.setValue(0);
            lblTgtaosp.setText("");
            lblTgsuasp.setText("");
            lblTgsuasize.setText("");
            lblTgtaosize.setText("");
            cboNhanhieuCapnhat.removeAllItems();
            cboLoaiCapnhat.removeAllItems();

            txtMaspQL.setText("");
            txtTenSpCapnhat.setText("");
            txtMauSpCapnhat.setText("");
            txtAnhSpCapnhat.setText("");
            spnGiaSpCapnhat.setValue(0);
            cboNhanhieuCapnhat.removeAllItems();
            cboLoaiCapnhat.removeAllItems();
            cboTrangthaiCapnhat.setSelectedIndex(0);
            txtMotaSpCapnhat.setText("");
        } catch (Exception e) {
            System.out.println("loi TimKiemSP_Reset_QL " + e);
        }
    }

    private void TimKiemSP_QLSP() {
        try {
            Reset_QLSP();
            if (txtTkspCapnhat.getText().length() > 0) {
                String id = txtTkspCapnhat.getText();
                String txt = "from Product where id=" + id;
                List<Product> list = dt.select(txt);
                if (list.size() > 0) {
                    Product p = list.get(0);
                    txtMaspQL.setText(p.getId() + "");
                    txtTenSpCapnhat.setText(p.getName());
                    txtMauSpCapnhat.setText(p.getColor());
                    txtAnhSpCapnhat.setText(p.getImage());
                    spnGiaSpCapnhat.setValue(p.getPrice());
                    txtMotaSpCapnhat.setText(p.getDescription());
                    lblTgtaosp.setText(p.getCreatedAt().toString());
                    lblTgsuasp.setText(p.getUpdatedAt().toString());
                    List<Brand> lb = dt.select("from Brand");
                    if (lb.size() > 0) {
                        dsNhanHieuCapnhat = lb;
                        for (int i = 0; i < dsNhanHieuCapnhat.size(); i++) {
                            cboNhanhieuCapnhat.addItem(dsNhanHieuCapnhat.get(i).getName());
                            if (p.getBrand().getId().equals(dsNhanHieuCapnhat.get(i).getId())) {
                                cboNhanhieuCapnhat.setSelectedIndex(i);
                            }
                        }
                    }
                    List<Category> lc = dt.select("from Category");
                    if (lc.size() > 0) {
                        dsLoaiCapnhat = lc;
                        for (int i = 0; i < dsLoaiCapnhat.size(); i++) {
                            cboLoaiCapnhat.addItem(dsLoaiCapnhat.get(i).getName());
                            if (p.getCategory().getId().equals(dsLoaiCapnhat.get(i).getId())) {
                                cboLoaiCapnhat.setSelectedIndex(i);
                            }
                        }
                    }
                    if (p.isIsDelete() == false) {
                        cboTrangthaiCapnhat.setSelectedIndex(0);
                    } else {
                        cboTrangthaiCapnhat.setSelectedIndex(1);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("loi TimKiemSP_QL " + e);
        }
    }

    private void hienthiSizeSP_QLSP() {
        try {
            cboSizespCapnhat.removeAllItems();
            spnSoluongSpCapnhat.setValue(0);
            dsSizeCapnhat = null;
            if (txtTkspCapnhat.getText().length() > 0) {
                String txt = "from ProductSize where  product_id=" + txtTkspCapnhat.getText();
                List<ProductSize> list = dt.select(txt);
                if (list.size() > 0) {
                    dsSizeCapnhat = list;
                    for (ProductSize x : dsSizeCapnhat) {
                        cboSizespCapnhat.addItem(x.getSize());
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("loi hienthiSizeSP_QL " + e);
        }
    }

    private void hienthiSoluongSizeSP_QLSP() {
        try {
            int i = cboSizespCapnhat.getSelectedIndex();
            if (i > -1 && dsSizeCapnhat != null) {
                spnSoluongSpCapnhat.setValue(dsSizeCapnhat.get(i).getQuantity());
                lblTgsuasize.setText(dsSizeCapnhat.get(i).getUpdatedAt().toString());
                lblTgtaosize.setText(dsSizeCapnhat.get(i).getCreatedAt().toString());
            } else {
                spnSoluongSpCapnhat.setValue(0);
                lblTgsuasize.setText("");
                lblTgtaosize.setText("");
            }
        } catch (Exception e) {
            System.out.println("loi hienthiSoluongSize_QL " + e);
        }
    }

    private int capNhatSoLuongSize_QLSP() {
        int re = -1;
        try {
            if (dsSizeCapnhat != null) {
                int i = cboSizespCapnhat.getSelectedIndex();
                int c = (int) spnSoluongSpCapnhat.getValue();
                ProductSize ps = dsSizeCapnhat.get(i);
                ps.setQuantity(c);
                ps.setUpdatedAt(new Date());
                boolean kq = dt.update(ps);
                if (kq == false) {
                    JOptionPane.showMessageDialog(null, "Cập nhật Size gặp lỗi !", "Lỗi cập nhật", 0);
                } else {
                    re = i;
                }
            }
        } catch (Exception e) {
            System.out.println("loi capNhatSloLuongSize " + e);
        }
        return re;
    }

    private boolean themSize_QLSP() {
        boolean re = false;
        try {
            int size = (int) spnNhapsizeCapnhat.getValue();
            int quan = (int) spnNhapsoluongCapnhat.getValue();
            String txt = "from ProductSize where  product_id=" + txtTkspCapnhat.getText();
            List<ProductSize> list = dt.select(txt);
            if (list.size() > 0) {
                ProductSize ps = list.get(0);
                ps.setSize(size);
                ps.setCreatedAt(new Date());
                ps.setQuantity(quan);
                ps.setId(null);
                ps.setUpdatedAt(new Date());
                re = dt.insert(ps);
            }

        } catch (Exception e) {
            System.out.println("loi themSize_QLSP " + e);
        }
        return re;
    }

    private boolean xoaSize_QLSP() {
        boolean re = false;
        try {
            if (dsSizeCapnhat != null) {
                int i = cboSizespCapnhat.getSelectedIndex();
                ProductSize ps = dsSizeCapnhat.get(i);
                re = dt.delete(ps);
                if (re == false) {
                    JOptionPane.showMessageDialog(null, "Không thể xóa Size vì đã có dữ liệu trong hóa đơn !", "Lỗi cập nhật", 0);
                }
            }
        } catch (Exception e) {
            System.out.println("loi xoaSize_QLSP " + e);
        }
        return re;
    }

    private void loadHinhAnh() {
        try {
            //txtImgCapnhat="";
            txtAnhSpCapnhat.setText("");
            JFileChooser jdc = new JFileChooser("C:\\Users\\Admin\\Desktop");
            FileFilter imageFilter = new FileNameExtensionFilter(
                    "Image files", ImageIO.getReaderFileSuffixes());
            jdc.setFileFilter(imageFilter);
            int re = jdc.showOpenDialog(null);
            if (re == JFileChooser.APPROVE_OPTION) {
                File f = jdc.getSelectedFile();
//                BufferedImage bImage = ImageIO.read(new File(f.getPath()));
//                ByteArrayOutputStream bos = new ByteArrayOutputStream();
//                ImageIO.write(bImage, "jpg", bos);
//                byte[] data = bos.toByteArray();
//                for (byte e : data) {
//                    txtImgCapnhat += e + "*";
//                }
//                System.out.println(txtImgCapnhat.length());
                txtAnhSpCapnhat.setText(f.getPath());
            } else {
                System.out.println("chua chon file");
            }
        } catch (Exception e) {
            System.out.println("loi loadHinhAnh " + e);
        }
    }
    //QUẢN LÍ NHÃN HIỆU, LOẠI

    private void dodulieuNhanhieu() {
        try {
            List<Brand> list = dt.select("from Brand");
            if (list.size() > 0) {
                int index = 1;
                DefaultTableModel model = (DefaultTableModel) tblNhanhieu.getModel();
                model.setRowCount(0);
                for (Brand n : list) {
                    model.addRow(new Object[]{index, n.getId(), n.getName(), n.getCreatedAt(), n.getUpdatedAt()});
                    index++;
                }
            }
        } catch (Exception e) {
            System.out.println("loi do du lieu nhan hieu " + e);
        }
    }

    private void dodulieuLoai() {
        try {
            List<Category> list = dt.select("from Category");
            if (list.size() > 0) {
                int index = 1;
                DefaultTableModel model = (DefaultTableModel) tblLoai.getModel();
                model.setRowCount(0);
                for (Category n : list) {
                    model.addRow(new Object[]{index, n.getId(), n.getName(), n.getCreatedAt(), n.getUpdatedAt()});
                    index++;
                }
            }
        } catch (Exception e) {
            System.out.println("loi do du lieu nhan hieu " + e);
        }
    }

    private void themNhanhieu() {
        try {
            Brand b = new Brand();
            b.setName(txtTennhanhhieu.getText());
            b.setCreatedAt(new Date());
            dt.insert(b);
        } catch (Exception e) {
            System.out.println("them nhan hieu loi " + e);
        }
    }

    private void xoaNhanhieu() {
        try {
            int index = tblNhanhieu.getSelectedRow();
            String id = tblNhanhieu.getValueAt(index, 1).toString();
            List<Brand> list = dt.select("from Brand where id=" + id);
            if (list.size() > 0) {
                Brand b = list.get(0);
                if (dt.delete(b) == false) {
                    JOptionPane.showMessageDialog(null, "Không thể xóa nhãn hiệu do có sản phẩm đang sử dụng !");
                }
            }
        } catch (Exception e) {
            System.out.println("xoa nhan hieu loi " + e);
        }
    }

    private void doitenNhanhieu() {
        try {
            int index = tblNhanhieu.getSelectedRow();
            String id = tblNhanhieu.getValueAt(index, 1).toString();
            List<Brand> list = dt.select("from Brand where id=" + id);
            if (list.size() > 0) {
                Brand b = list.get(0);
                b.setName(txtTenNhanhieumoi.getText());
                b.setUpdatedAt(new Date());
                dt.update(b);
            } else {
                JOptionPane.showMessageDialog(null, "Hãy chọn nhãn hiệu muốn đổi !");
            }
        } catch (Exception e) {
            System.out.println("doi ten nhan hieu loi " + e);
        }
    }

    //loai
    private void themLoai() {
        try {
            Category b = new Category();
            b.setName(txtTenLoai.getText());
            b.setCreatedAt(new Date());
            dt.insert(b);
        } catch (Exception e) {
            System.out.println("them nhan hieu loi " + e);
        }
    }

    private void xoaLoai() {
        try {
            int index = tblLoai.getSelectedRow();
            String id = tblLoai.getValueAt(index, 1).toString();
            List<Category> list = dt.select("from Category where id=" + id);
            if (list.size() > 0) {
                Category b = list.get(0);
                if (dt.delete(b) == false) {
                    JOptionPane.showMessageDialog(null, "Không thể xóa loại do có sản phẩm đang sử dụng !");
                }
            }
        } catch (Exception e) {
            System.out.println("xoa nhan hieu loi " + e);
        }
    }

    private void doitenLoai() {
        try {
            int index = tblLoai.getSelectedRow();
            String id = tblLoai.getValueAt(index, 1).toString();
            List<Category> list = dt.select("from Category where id=" + id);
            if (list.size() > 0) {
                Category b = list.get(0);
                b.setName(txtTenLoaiMoi.getText());
                b.setUpdatedAt(new Date());
                dt.update(b);
            } else {
                JOptionPane.showMessageDialog(null, "Hãy chọn loại muốn đổi !");
            }
        } catch (Exception e) {
            System.out.println("doi ten nhan hieu loi " + e);
        }
    }
//QUẢN LÍ THÀNH VIÊN

    private void doDuLieuBangTV(String txt) {
        try {
            DefaultTableModel model = (DefaultTableModel) tblTV.getModel();
            model.setRowCount(0);
            List<User> list = dt.select(txt);
            if (list.size() > 0) {
                int i = 1;
                for (User n : list) {
                    model.addRow(new Object[]{i, n.getId(), n.getName(), n.getPassword(), n.getCreatedAt(), n.getUpdatedAt()});
                    i++;
                }
            }
        } catch (Exception e) {
            System.out.println("lỗi đổ bảng thành viên " + e);
        }
    }

    private String scriptTimTV() {
        String txt = "from User";
        try {
            int tt = cboTrangthaiTV.getSelectedIndex();
            int cv = cboChucvuTV.getSelectedIndex();
            String trangthai = "";
            switch (tt) {
                case 1:
                    trangthai = "false";
                    break;
                case 2:
                    trangthai = "true";
                    break;
            }
            String chucvu = "";
            switch (cv) {
                case 1:
                    chucvu = "ROLE_ADMIN";
                    break;
                case 2:
                    chucvu = "ROLE_APP_MANAGER";
                    break;
                case 3:
                    chucvu = "ROLE_APP_STAFF";
                    break;
            }
            if (tt != 0) {
                if (cv == 0) {
                    txt += " where isDelete=" + trangthai;
                } else {
                    txt += " where isDelete=" + trangthai + " and role='" + chucvu + "'";
                }
            } else {
                if (tt == 0) {
                    if (cv != 0) {
                        txt += " where role='" + chucvu + "'";
                    }
                }
            }
            if (txtTimkiemTV.getText() != null && txtTimkiemTV.getText().length() > 0) {
                if (txt != "from User") {
                    txt += " and name like '%" + txtTimkiemTV.getText() + "%' or id like '%" + txtTimkiemTV.getText() + "%'";
                } else {
                    txt += " where name like '%" + txtTimkiemTV.getText() + "%' or id like '%" + txtTimkiemTV.getText() + "%'";
                }
            }
        } catch (Exception e) {
            System.out.println("loi cript loc tv " + e);
        }
        return txt;
    }
    User selectedUser = null;

    private void doDuLieuThongtinTV() {
        try {
            int i = tblTV.getSelectedRow();
            String id = tblTV.getValueAt(i, 1).toString();
            String txt = "from User where id=" + id;
            List<User> list = dt.select(txt);
            if (list.size() > 0) {
                selectedUser = list.get(0);
                txtTentvSua.setText(selectedUser.getName());
                txtMktvSua.setText(selectedUser.getPassword());
                txtSdttvSua.setText(selectedUser.getPhone());
                txtDiachitvSua.setText(selectedUser.getAddress());
                txtEmailtvSua.setText(selectedUser.getEmail());
                dcNgaysinhtvSua.setDate(selectedUser.getDateOfBirth());
                if (selectedUser.getRole().equals("ROLE_ADMIN")) {
                    cboChucvutvSua.setSelectedIndex(0);
                } else {
                    if (selectedUser.getRole().equals("ROLE_APP_MANAGER")) {
                        cboChucvutvSua.setSelectedIndex(1);
                    } else {
                        if (selectedUser.getRole().equals("ROLE_APP_STAFF")) {
                            cboChucvutvSua.setSelectedIndex(2);
                        } else {
                            if (selectedUser.getRole().equals("ROLE_WEB_MANAGER")) {
                                cboChucvutvSua.setSelectedIndex(3);
                            } else {
                                if (selectedUser.getRole().equals("ROLE_WEB_STAFF")) {
                                    cboChucvutvSua.setSelectedIndex(4);
                                }
                            }
                        }
                    }
                }
                if (selectedUser.isIsDelete() == true) {
                    cboTrangthaitvSua.setSelectedIndex(1);
                } else {
                    cboTrangthaitvSua.setSelectedIndex(0);
                }
            }
        } catch (Exception e) {
            System.out.println("loi doDuLieuTV " + e);
        }
    }

    private User scriptCapNhatThanhVien() {
        User u = null;
        try {
            if (selectedUser != null) {
                u = selectedUser;
                u.setName(txtTentvSua.getText());
                u.setPassword(txtMktvSua.getText());
                u.setPhone(txtSdttvSua.getText());
                u.setAddress(txtDiachitvSua.getText());
                u.setEmail(txtEmailtvSua.getText());
                u.setDateOfBirth(dcNgaysinhtvSua.getDate());
                if (cboChucvutvSua.getSelectedIndex() == 0) {
                    u.setRole("ROLE_ADMIN");
                } else {
                    if (cboChucvutvSua.getSelectedIndex() == 1) {
                        u.setRole("ROLE_APP_MANAGER");
                    } else {
                        if (cboChucvutvSua.getSelectedIndex() == 2) {
                            u.setRole("ROLE_APP_STAFF");
                        } else {
                            if (cboChucvutvSua.getSelectedIndex() == 3) {
                                u.setRole("ROLE_WEB_MANAGER");
                            } else {
                                if (cboChucvutvSua.getSelectedIndex() == 4) {
                                    u.setRole("ROLE_WEB_STAFF");
                                }
                            }
                        }
                    }
                }
                if (cboTrangthaiTV.getSelectedIndex() == 0) {
                    u.setIsDelete(false);
                } else {
                    u.setIsDelete(true);
                }
                u.setUpdatedAt(new Date());
            }
        } catch (Exception e) {
            System.out.println("capNhatThanhVien " + e);
        }
        return u;
    }

    private boolean kiemTraChucNangThanhVien(User u) {
        boolean re = false;
        try {
            if (u != null) {
                if (user.getRole().equals("ROLE_APP_MANAGER")) {
                    if (u.getRole().equals("ROLE_APP_STAFF")) {
                        re = true;
                    }
                }
                if (user.getRole().equals("ROLE_ADMIN")) {
                    re = true;
                }
            }
        } catch (Exception e) {
        }
        return re;
    }

    private void capnhatThanhVien() {
        try {
            User u = scriptCapNhatThanhVien();
            if (kiemTraChucNangThanhVien(u) == true) {
                boolean a = dt.update(u);
                if (a == true) {
                    JOptionPane.showMessageDialog(null, "Đã đổi thông tin của " + u.getName());
                } else {
                    JOptionPane.showMessageDialog(null, "Lỗi không xác định");
                }
            } else {
                if (tblTV.getSelectedRow() < 0) {
                    JOptionPane.showMessageDialog(null, "Chưa có thành viên được chọn", "Lỗi", 0);
                } else {
                    JOptionPane.showMessageDialog(null, "Phân quyền Quản lí App chỉ có thể thay đổi nhân viên App", "Lỗi phân quyền", 0);
                }
            }
        } catch (Exception e) {
            System.out.println("loi themThanhVien " + e);
        }
    }

    private User scriptThemthanhvien() {
        User u = null;
        try {
            u = new User();
            u.setName(txtTentvThem.getText());
            u.setPassword(txtMKtvThem.getText());
            u.setPhone(txtSdttvThem.getText());
            u.setEmail(txtEmailtvThem.getText());
            u.setDateOfBirth(dcNgaysinhtvThem.getDate());
            //-------------------------
            if (cboChucvutvThem.getSelectedIndex() == 0) {
                u.setRole("ROLE_ADMIN");
            } else {
                if (cboChucvutvThem.getSelectedIndex() == 1) {
                    u.setRole("ROLE_APP_MANAGER");
                } else {
                    if (cboChucvutvThem.getSelectedIndex() == 2) {
                        u.setRole("ROLE_APP_STAFF");
                    } else {
                        if (cboChucvutvThem.getSelectedIndex() == 3) {
                            u.setRole("ROLE_WEB_MANAGER");
                        } else {
                            if (cboChucvutvThem.getSelectedIndex() == 4) {
                                u.setRole("ROLE_WEB_STAFF");
                            }
                        }
                    }
                }
            }
            if (cboTrangthaitvThem.getSelectedIndex() == 0) {
                u.setIsDelete(false);
            } else {
                u.setIsDelete(true);
            }
            u.setCreatedAt(new Date());
        } catch (Exception e) {
            System.out.println("loi scriptThemthanhvien " + e);
        }
        return u;
    }

    private void themThanhVien() {
        try {
            User u = scriptThemthanhvien();
            if (kiemTraChucNangThanhVien(u) == true) {
                boolean a = dt.insert(u);
                if (a == true) {
                    JOptionPane.showMessageDialog(null, "Đã thêm " + u.getName());
                } else {
                    JOptionPane.showMessageDialog(null, "Lỗi không xác định");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Phân quyền Quản lí App chỉ có thể thêm nhân viên App");
            }
        } catch (Exception e) {
            System.out.println("loi themThanhVien " + e);
        }
    }
//HÓA ĐƠN

    private void doDuLieuHoaDon(String txt) {
        try {
            List<OrderApp> oa = dt.select(txt);
            if (oa.size() > 0) {
                DefaultTableModel model = (DefaultTableModel) tblHoadon.getModel();
                model.setRowCount(0);
                lblTonghoadon.setText(oa.size() + "");
                int i = 1;
                int count = 0;
                for (OrderApp n : oa) {
                    model.addRow(new Object[]{i, n.getId(), n.getUser().getId(), n.getTotalAmount(), n.getCreatedAt()});
                    i++;
                    count += n.getTotalAmount();
                }
                if (count < 0) {
                    count = count * -1;
                }
                lblTongthu.setText(count + " đ");
            }
        } catch (Exception e) {
            System.out.println("loi doDuLieuHoaDon " + e);
        }
    }

    private void Hoadonhomnay() {
        try {
            DateFormat fm = new SimpleDateFormat("yyyy-MM-dd");
            Date now = new Date();
            doDuLieuHoaDon("from OrderApp where createdAt like '%" + fm.format(now) + "%'");
        } catch (Exception e) {
            System.out.println("loi Hoadonhomnay " + e);
        }
    }

    private void locHoaDon() {
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date bd = dcHoadonBatdau.getDate();
            Date kt = dcHoadonKetthuc.getDate();
            String strBd = dateFormat.format(bd);
            String strKt = dateFormat.format(kt);
            String txt = "from OrderApp where createdAt BETWEEN  '" + strBd + "' and '" + strKt + "'";
            doDuLieuHoaDon(txt);
            // JOptionPane.showMessageDialog(null, txt);
        } catch (Exception e) {
            System.out.println("loi locHoaDon " + e);
        }
    }

    private void chonHoaDon() {
        try {
            int si = tblHoadon.getSelectedRow();
            DefaultTableModel model = (DefaultTableModel) tblChitiethoadon.getModel();
            model.setRowCount(0);
            if (si > -1) {
                String hdid = tblHoadon.getValueAt(si, 1).toString();
                String txt = "from OrderAppDetail where orderApp=" + hdid;
                List<OrderAppDetail> oad = dt.select(txt);
                if (oad.size() > 0) {
                    int i = 1;
                    for (OrderAppDetail show : oad) {
                        List<ProductSize> ps = dt.select("from ProductSize where id=" + show.getProductSize().getId());
                        if (ps.size() > 0) {
                            List<Product> p = dt.select("from Product where id=" + ps.get(0).getProduct().getId());
                            //JOptionPane.showMessageDialog(null, show.getId()+"-"+ps.get(0).getId()+"-"+p.get(0).getId());
                            model.addRow(new Object[]{i, p.get(0).getId(), p.get(0).getName(),
                                show.getQuantity(), show.getPrice(), ps.get(0).getSize(), show.getTotalAmount()});
                            i++;
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("loi chonHoaDon " + e);
        }
    }
    //-------------
    //THANH TOÁN ------------------------------------------------------------------------
    Product SPdatim = null;
    List<ProductSize> dsSizeKetquaTK = null;
    List<OrderAppDetail> dsChiTietDonHang = new ArrayList<>();
//TÌM KIẾM VÀ HIỂN THỊ

    private void timKiemSP(String id) {
        try {
            dsSizeKetquaTK = null;
            SPdatim = null;
            String txt = "from Product where id=" + id;
            List<Product> list = dt.select(txt);
            resetTT();
            if (list.size() > 0) {
                String txt2 = "from ProductSize where product=" + list.get(0).getId();
                List<ProductSize> list2 = dt.select(txt2);
                if (list2.size() > 0) {
                    dsSizeKetquaTK = list2;
                    SPdatim = list.get(0);
                } else {
                    JOptionPane.showMessageDialog(tabCapnhat, "Sản phẩm này chưa có thông tin về size và số lượng", "Không có size", 2);
                }
            }
        } catch (Exception e) {
            System.out.println("loi tim kiem " + e);
        }
    }

    private void doDuLieuTK() {
        try {
            if (SPdatim != null && dsSizeKetquaTK != null) {
                txtKetqua.setText(SPdatim.getName());
                cboSizeTT.removeAllItems();
                for (ProductSize e : dsSizeKetquaTK) {
                    cboSizeTT.addItem(e.getSize());
                }
            }
        } catch (Exception e) {
            System.out.println("loi doDuLieuTK " + e);
        }
    }

    private void hienThiSoLuongSP_TT() {
        try {
            if (cboSizeTT.getItemCount() > 0) {
                int index = cboSizeTT.getSelectedIndex();
                if (dsSizeKetquaTK != null) {
                    int sl = dsSizeKetquaTK.get(index).getQuantity();
                    String x = sl + " đôi";
                    lblSancoTT.setText(x);
                }
            }

        } catch (Exception e) {
            System.out.println("loi hienThiSoLuongSP " + e);
        }
    }

//CẬP NHẬT ĐƠN HÀNG, ĐỔ DỮ LIỆU VÀO BẢNG
    private void capNhatChiTietDonHang() {
        try {
            if (SPdatim != null & dsSizeKetquaTK != null) {
                int sizeIndex = cboSizeTT.getSelectedIndex();
                ProductSize ps = dsSizeKetquaTK.get(sizeIndex);
                ps.setProduct(SPdatim);
                int soLuong = (int) spnSoluongSpTT.getValue();
                int soLuongHienCo = ps.getQuantity();

                if (soLuong <= soLuongHienCo) {
                    OrderAppDetail oad = new OrderAppDetail();
                    oad.setCreatedAt(new Date());
                    oad.setPrice(SPdatim.getPrice());
                    oad.setProductSize(ps);
                    oad.setQuantity(soLuong);
                    oad.setTotalAmount(soLuong * SPdatim.getPrice());
                    boolean isHave = false;
                    for (int i = 0; i < dsChiTietDonHang.size(); i++) {
                        if (dsChiTietDonHang.get(i).getProductSize().getId().equals(ps.getId())) {
                            dsChiTietDonHang.get(i).setQuantity(oad.getQuantity());
                            dsChiTietDonHang.get(i).setTotalAmount(oad.getTotalAmount());
                            isHave = true;
                            break;
                        }
                    }
                    if (isHave == false) {
                        //JOptionPane.showMessageDialog(null, "ishave = false");
                        dsChiTietDonHang.add(oad);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Sản phẩm hiện tại chỉ còn " + soLuongHienCo + " đôi !", "Số lượng không đủ", 2);
                }
            }
        } catch (Exception e) {
            System.out.println("loi themSPTT2 " + e);
        }
    }

    private void xoaSpTT() {
        try {
            int row = tblChitietSP.getSelectedRow();
            if (row != 0) {
                dsChiTietDonHang.remove(row);
            } else {
                dsChiTietDonHang = new ArrayList();
            }
            doDuLieuChiTietHoaDon();
        } catch (Exception e) {
            System.out.println("loi xoaSpTT " + e);
        }
    }

    private void doDuLieuChiTietHoaDon() {
        try {
            DefaultTableModel model = (DefaultTableModel) tblChitietSP.getModel();
            model.setRowCount(0);
            if (dsChiTietDonHang.size() > 0) {
                int i = 1;
                int tongTien = 0;
                int tongSoLuong = 0;
                for (OrderAppDetail oad : dsChiTietDonHang) {
                    tongTien += oad.getTotalAmount();
                    tongSoLuong += oad.getQuantity();
                    model.addRow(new Object[]{i, SPdatim.getId(), SPdatim.getName(), oad.getQuantity(),
                        oad.getProductSize().getSize(), oad.getPrice(), oad.getTotalAmount()});
                    i++;
                }
                lblTongtienTT.setText(tongTien + " đ");
                lblTongsohang.setText(tongSoLuong + " đôi");
            } else {
                lblTongtienTT.setText("0 đ");
                lblTongsohang.setText("0 đôi");
            }
        } catch (Exception e) {
            System.out.println("loi doDuLieuSPTT " + e);
        }
    }

//CHỌN SẢN PHẨM BÊN DƯỚI, HIỂN THỊ LÊN BÊN TRÊN
    private void chonSP() {
        try {
            int index = tblChitietSP.getSelectedRow();
            String id = tblChitietSP.getValueAt(index, 1).toString();
            String size = tblChitietSP.getValueAt(index, 4).toString();
            timKiemSP(id);
            doDuLieuTK();
            for (int i = 0; i < cboSizeTT.getItemCount(); i++) {
                if (cboSizeTT.getItemAt(i).toString().equals(size)) {
                    cboSizeTT.setSelectedIndex(i);
                    break;
                }
            }
            hienThiSoLuongSP_TT();
        } catch (Exception e) {
            System.out.println("loi chonSP " + e);
        }
    }

    private void thanhToan_1() {
        try {
            if (dsChiTietDonHang.size() > 0) {
                OrderApp od = new OrderApp();
                od.setUser(user);
                od.setCreatedAt(new Date());
                //DateFormat fm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                dt.insert(od);
            }
        } catch (Exception e) {
            System.out.println("loi thanhToan 1" + e);
        }
    }

    private void thanhtoan_2() {
        try {
            String txt = "from OrderApp";
            List<OrderApp> list = dt.select(txt);
            if (list.size() > 0) {
                int size = list.size();
                OrderApp oa = list.get(size - 1);
                System.out.println("oid=" + oa.getId());
                int total = 0;
                List<HoaDon> listhoadon = new ArrayList();
                for (OrderAppDetail oad : dsChiTietDonHang) {
                    total += oad.getTotalAmount();
                    oad.setOrderApp(oa);
                    dt.insert(oad);
                    System.out.println("1");
                    //giảm số lượng sản phẩm tại PRODUCT SIZE
                    ProductSize ps = oad.getProductSize();
                    ps.setQuantity(ps.getQuantity() - oad.getQuantity());
                    dt.update(ps);
                    System.out.println("2");
                    //in hóa đơn
                    HoaDon hd = new HoaDon();
                    hd.setId(oad.getProductSize().getProduct().getId());
                    hd.setTen(oad.getProductSize().getProduct().getName());
                    hd.setSoluong(oad.getQuantity());
                    hd.setSize(oad.getProductSize().getSize());
                    hd.setGia(oad.getPrice());
                    hd.setTt(oad.getTotalAmount());
                    listhoadon.add(hd);
                }
                oa.setTotalAmount(total);
                dt.update(oa);
                System.out.println("3");
                if (ckInhoadon.isSelected() == true) {
                    System.out.println("in true");
                    InHoaDon ihd = new InHoaDon(listhoadon, oa);
                    ihd.in();
                    System.out.println("in xong");
                }
            }
        } catch (Exception e) {
            System.out.println("loi thanh toan 2" + e);
        }
    }

    private void resetTT() {
        try {
            SPdatim = null;
            dsSizeKetquaTK = null;
            dsChiTietDonHang = new ArrayList();

            doDuLieuChiTietHoaDon();
            lblSancoTT.setText("");
            cboSizeTT.removeAllItems();
            spnSoluongSpTT.setValue(1);
            txtKetqua.setText("");
            lblTongtienTT.setText("0đ");
            lblTongsohang.setText("0 đôi");
        } catch (Exception e) {
            System.out.println("loi resetTT");
        }
    }

    private void InHoaDon() {
        try {
            int index = tblHoadon.getSelectedRow();
            String id = tblHoadon.getValueAt(index, 1).toString();
            String txt = "from OrderApp where id=" + id;
            System.out.println(txt);
            List<OrderApp> OAlist = dt.select(txt);
            if (OAlist.size() > 0) {
                OrderApp oa = OAlist.get(0);
                String txt2 = "from OrderAppDetail where orderApp=" + oa.getId();
                System.out.println(txt2);
                List<OrderAppDetail> OADlist = dt.select(txt2);
                if (OADlist.size() > 0) {
                    List<HoaDon> listHD = new ArrayList();
                    for (OrderAppDetail x : OADlist) {
                        OrderAppDetail oad = OADlist.get(0);
                        String txt3 = "from ProductSize where id=" + oad.getProductSize().getId().toString();
                        System.out.println(txt3);
                        List<ProductSize> PSlist = dt.select(txt3);
                        if (PSlist.size() > 0) {
                            ProductSize ps = PSlist.get(0);
                            String txt4 = "from Product where id=" + ps.getProduct().getId().toString();
                            System.out.println(txt4);
                            List<Product> Plist = dt.select(txt4);
                            if (Plist.size() > 0) {
                                Product p = Plist.get(0);

                                HoaDon hd = new HoaDon();
                                hd.setId(p.getId());
                                hd.setTen(p.getName());
                                hd.setSoluong(oad.getQuantity());
                                hd.setSize(ps.getSize());
                                hd.setGia(oad.getPrice());
                                hd.setTt(oad.getTotalAmount());
                                listHD.add(hd);

                            }
                        }
                    }
                    InHoaDon ihd = new InHoaDon(listHD, oa);
                    ihd.in();
                    System.out.println("in xong");
                }
            }
        } catch (Exception e) {
            System.out.println("loi InHoaDon " + e);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jComboBox11 = new javax.swing.JComboBox();
        jCalendar1 = new com.toedter.calendar.JCalendar();
        Tab = new javax.swing.JTabbedPane();
        tabBanhang = new javax.swing.JPanel();
        pnlTimkiem = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtTimkiem = new javax.swing.JTextField();
        btnTim = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        txtKetqua = new javax.swing.JTextField();
        btnThem = new javax.swing.JButton();
        jLabel16 = new javax.swing.JLabel();
        spnSoluongSpTT = new javax.swing.JSpinner();
        jLabel19 = new javax.swing.JLabel();
        btnXoaSpTT = new javax.swing.JButton();
        cboSizeTT = new javax.swing.JComboBox();
        jLabel5 = new javax.swing.JLabel();
        lblSancoTT = new javax.swing.JLabel();
        pnlShow = new javax.swing.JPanel();
        pnlResult = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblChitietSP = new javax.swing.JTable();
        pnlThanhtoan = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        lblTongsohang = new javax.swing.JLabel();
        lblTongtienTT = new javax.swing.JLabel();
        btnThanhtoan = new javax.swing.JButton();
        ckInhoadon = new javax.swing.JCheckBox();
        jPanel1 = new javax.swing.JPanel();
        lblTennv = new javax.swing.JLabel();
        tabXemhoadon = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        lblTongthu = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        dcHoadonBatdau = new com.toedter.calendar.JDateChooser();
        dcHoadonKetthuc = new com.toedter.calendar.JDateChooser();
        jLabel11 = new javax.swing.JLabel();
        btnLochoadon = new javax.swing.JButton();
        jLabel52 = new javax.swing.JLabel();
        lblTonghoadon = new javax.swing.JLabel();
        btnHoadonhomnay = new javax.swing.JButton();
        btnInhoadon = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblHoadon = new javax.swing.JTable();
        jScrollPane4 = new javax.swing.JScrollPane();
        tblChitiethoadon = new javax.swing.JTable();
        tabTracuu = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        cboLoai = new javax.swing.JComboBox();
        jLabel18 = new javax.swing.JLabel();
        cboNhanhieu = new javax.swing.JComboBox();
        txtTimkiemtracuu = new javax.swing.JTextField();
        btnTimkiemtracuu = new javax.swing.JButton();
        jLabel65 = new javax.swing.JLabel();
        cboTrangthaisp = new javax.swing.JComboBox();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane8 = new javax.swing.JScrollPane();
        tblTracuu = new javax.swing.JTable();
        jPanel12 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblSizeTracuu = new javax.swing.JTable();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        lblNhanhieu = new javax.swing.JLabel();
        lblLoai = new javax.swing.JLabel();
        lblTrangthai = new javax.swing.JLabel();
        tabQuanli = new javax.swing.JPanel();
        TabQTV = new javax.swing.JTabbedPane();
        tabSanpham = new javax.swing.JPanel();
        tabQLSP = new javax.swing.JTabbedPane();
        tabCapnhat = new javax.swing.JPanel();
        pnlCapNhatSP = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        txtTenSpCapnhat = new javax.swing.JTextField();
        txtMauSpCapnhat = new javax.swing.JTextField();
        jLabel34 = new javax.swing.JLabel();
        jLabel39 = new javax.swing.JLabel();
        txtAnhSpCapnhat = new javax.swing.JTextField();
        jLabel38 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jScrollPane7 = new javax.swing.JScrollPane();
        txtMotaSpCapnhat = new javax.swing.JTextArea();
        jLabel35 = new javax.swing.JLabel();
        cboNhanhieuCapnhat = new javax.swing.JComboBox();
        spnGiaSpCapnhat = new javax.swing.JSpinner();
        jLabel36 = new javax.swing.JLabel();
        cboLoaiCapnhat = new javax.swing.JComboBox();
        jLabel37 = new javax.swing.JLabel();
        cboTrangthaiCapnhat = new javax.swing.JComboBox();
        btnCapnhatSp = new javax.swing.JButton();
        btnXoaSp = new javax.swing.JButton();
        jPanel15 = new javax.swing.JPanel();
        txtTkspCapnhat = new javax.swing.JTextField();
        btnTkspQL = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        txtMaspQL = new javax.swing.JTextField();
        jPanel17 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        cboSizespCapnhat = new javax.swing.JComboBox();
        jLabel20 = new javax.swing.JLabel();
        spnSoluongSpCapnhat = new javax.swing.JSpinner();
        btnCapnhatSoluongSize = new javax.swing.JButton();
        jButton12 = new javax.swing.JButton();
        jPanel18 = new javax.swing.JPanel();
        jLabel25 = new javax.swing.JLabel();
        btnThemSize = new javax.swing.JButton();
        spnNhapsizeCapnhat = new javax.swing.JSpinner();
        jLabel28 = new javax.swing.JLabel();
        spnNhapsoluongCapnhat = new javax.swing.JSpinner();
        jPanel13 = new javax.swing.JPanel();
        jLabel90 = new javax.swing.JLabel();
        lblTgtaosp = new javax.swing.JLabel();
        jLabel92 = new javax.swing.JLabel();
        lblTgsuasp = new javax.swing.JLabel();
        jLabel94 = new javax.swing.JLabel();
        lblTgsuasize = new javax.swing.JLabel();
        jLabel96 = new javax.swing.JLabel();
        lblTgtaosize = new javax.swing.JLabel();
        tabThem = new javax.swing.JPanel();
        pnlCapNhatSP4 = new javax.swing.JPanel();
        jLabel80 = new javax.swing.JLabel();
        txtTenSpCapnhat5 = new javax.swing.JTextField();
        txtMauSpCapnhat4 = new javax.swing.JTextField();
        jLabel81 = new javax.swing.JLabel();
        jLabel82 = new javax.swing.JLabel();
        txtLinkanhSpCapnhat4 = new javax.swing.JTextField();
        jLabel83 = new javax.swing.JLabel();
        jLabel84 = new javax.swing.JLabel();
        jScrollPane13 = new javax.swing.JScrollPane();
        txtMotaSpCapnhat4 = new javax.swing.JTextArea();
        jLabel85 = new javax.swing.JLabel();
        cboNhanhieuCapnhat4 = new javax.swing.JComboBox();
        spnGiaSpCapnhat4 = new javax.swing.JSpinner();
        jLabel86 = new javax.swing.JLabel();
        cboLoaiCapnhat4 = new javax.swing.JComboBox();
        jLabel87 = new javax.swing.JLabel();
        cboTrangthaiCapnhat4 = new javax.swing.JComboBox();
        jButton17 = new javax.swing.JButton();
        jPanel10 = new javax.swing.JPanel();
        jLabel88 = new javax.swing.JLabel();
        jSpinner2 = new javax.swing.JSpinner();
        jLabel89 = new javax.swing.JLabel();
        jSpinner4 = new javax.swing.JSpinner();
        jButton16 = new javax.swing.JButton();
        jButton18 = new javax.swing.JButton();
        jScrollPane14 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        btnCapnhatSp4 = new javax.swing.JButton();
        tabChitiet = new javax.swing.JPanel();
        tabTabChitiet = new javax.swing.JTabbedPane();
        tabNhanhieu = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        tblNhanhieu = new javax.swing.JTable();
        txtTennhanhhieu = new javax.swing.JTextField();
        btnThemnhanhieu = new javax.swing.JButton();
        btnXoanhanhieu = new javax.swing.JButton();
        txtTenNhanhieumoi = new javax.swing.JTextField();
        btnDoitenNhanhieu = new javax.swing.JButton();
        tabLoai = new javax.swing.JPanel();
        jPanel16 = new javax.swing.JPanel();
        txtTenLoai = new javax.swing.JTextField();
        btnThemLoai = new javax.swing.JButton();
        btnXoaLoai = new javax.swing.JButton();
        txtTenLoaiMoi = new javax.swing.JTextField();
        btnDoitenLoai = new javax.swing.JButton();
        jScrollPane6 = new javax.swing.JScrollPane();
        tblLoai = new javax.swing.JTable();
        tabThungan = new javax.swing.JPanel();
        jScrollPane9 = new javax.swing.JScrollPane();
        tblTV = new javax.swing.JTable();
        jPanel6 = new javax.swing.JPanel();
        tabSetThanhvien = new javax.swing.JTabbedPane();
        tabSuaThanhvien = new javax.swing.JPanel();
        btnCapnhatTV = new javax.swing.JButton();
        cboTrangthaitvSua = new javax.swing.JComboBox();
        jLabel56 = new javax.swing.JLabel();
        jLabel47 = new javax.swing.JLabel();
        cboChucvutvSua = new javax.swing.JComboBox();
        txtTentvSua = new javax.swing.JTextField();
        jLabel32 = new javax.swing.JLabel();
        jLabel43 = new javax.swing.JLabel();
        txtMktvSua = new javax.swing.JTextField();
        jLabel54 = new javax.swing.JLabel();
        txtSdttvSua = new javax.swing.JTextField();
        jLabel55 = new javax.swing.JLabel();
        txtDiachitvSua = new javax.swing.JTextField();
        jLabel57 = new javax.swing.JLabel();
        txtEmailtvSua = new javax.swing.JTextField();
        jLabel59 = new javax.swing.JLabel();
        dcNgaysinhtvSua = new com.toedter.calendar.JDateChooser();
        tabThemThanhvien = new javax.swing.JPanel();
        btnThemmoi = new javax.swing.JButton();
        cboTrangthaitvThem = new javax.swing.JComboBox();
        jLabel58 = new javax.swing.JLabel();
        jLabel60 = new javax.swing.JLabel();
        cboChucvutvThem = new javax.swing.JComboBox();
        jLabel61 = new javax.swing.JLabel();
        jLabel62 = new javax.swing.JLabel();
        txtEmailtvThem = new javax.swing.JTextField();
        txtDiachitvThem = new javax.swing.JTextField();
        jLabel64 = new javax.swing.JLabel();
        jLabel70 = new javax.swing.JLabel();
        txtSdttvThem = new javax.swing.JTextField();
        txtMKtvThem = new javax.swing.JTextField();
        jLabel45 = new javax.swing.JLabel();
        jLabel71 = new javax.swing.JLabel();
        txtTentvThem = new javax.swing.JTextField();
        dcNgaysinhtvThem = new com.toedter.calendar.JDateChooser();
        jPanel7 = new javax.swing.JPanel();
        btnTimTV = new javax.swing.JButton();
        txtTimkiemTV = new javax.swing.JTextField();
        jLabel51 = new javax.swing.JLabel();
        cboChucvuTV = new javax.swing.JComboBox();
        jLabel49 = new javax.swing.JLabel();
        cboTrangthaiTV = new javax.swing.JComboBox();
        tabCaidat = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jLabel33 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel40 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jTextField3 = new javax.swing.JTextField();
        jLabel41 = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jLabel44 = new javax.swing.JLabel();
        jTextField5 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jLabel46 = new javax.swing.JLabel();
        jTextField6 = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        jTextField7 = new javax.swing.JTextField();
        jTextField8 = new javax.swing.JTextField();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jLabel48 = new javax.swing.JLabel();
        jLabel50 = new javax.swing.JLabel();
        jButton5 = new javax.swing.JButton();

        jComboBox11.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jComboBox11.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tất cả", "Đang làm việc", "Tạm nghỉ", "Đã nghỉ" }));

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        Tab.setBackground(new java.awt.Color(255, 255, 255));
        Tab.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        Tab.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N

        pnlTimkiem.setBackground(new java.awt.Color(204, 204, 204));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel1.setText("Nhập mã sản phẩm");

        txtTimkiem.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        btnTim.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnTim.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/search_25px.png"))); // NOI18N
        btnTim.setText("Tìm");
        btnTim.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTimActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel2.setText("Tên sản phẩm");

        txtKetqua.setEditable(false);
        txtKetqua.setBackground(new java.awt.Color(255, 255, 255));
        txtKetqua.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtKetqua.setText("thay thành xombo box ?");

        btnThem.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnThem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/add_25px.png"))); // NOI18N
        btnThem.setText("Cập nhật");
        btnThem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemActionPerformed(evt);
            }
        });

        jLabel16.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel16.setText("Size");

        spnSoluongSpTT.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(1), Integer.valueOf(1), null, Integer.valueOf(1)));

        jLabel19.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel19.setText("Số lượng");

        btnXoaSpTT.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnXoaSpTT.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/trash_25px.png"))); // NOI18N
        btnXoaSpTT.setText("  Xóa");
        btnXoaSpTT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaSpTTActionPerformed(evt);
            }
        });

        cboSizeTT.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cboSizeTT.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboSizeTTItemStateChanged(evt);
            }
        });

        jLabel5.setText("Sẵn có:");

        lblSancoTT.setText("0");

        javax.swing.GroupLayout pnlTimkiemLayout = new javax.swing.GroupLayout(pnlTimkiem);
        pnlTimkiem.setLayout(pnlTimkiemLayout);
        pnlTimkiemLayout.setHorizontalGroup(
            pnlTimkiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTimkiemLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlTimkiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(pnlTimkiemLayout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtTimkiem, javax.swing.GroupLayout.PREFERRED_SIZE, 421, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlTimkiemLayout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(pnlTimkiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlTimkiemLayout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblSancoTT, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cboSizeTT, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(spnSoluongSpTT, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(pnlTimkiemLayout.createSequentialGroup()
                                .addComponent(txtKetqua, javax.swing.GroupLayout.PREFERRED_SIZE, 420, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE)))))
                .addGap(30, 30, 30)
                .addGroup(pnlTimkiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnThem, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnTim, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnXoaSpTT, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlTimkiemLayout.setVerticalGroup(
            pnlTimkiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTimkiemLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlTimkiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtTimkiem, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnTim, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlTimkiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnThem, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pnlTimkiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel2)
                        .addComponent(txtKetqua, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlTimkiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnXoaSpTT, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(spnSoluongSpTT, javax.swing.GroupLayout.DEFAULT_SIZE, 24, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlTimkiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(cboSizeTT, javax.swing.GroupLayout.DEFAULT_SIZE, 24, Short.MAX_VALUE)
                        .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jLabel19, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblSancoTT, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        pnlShow.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N

        pnlResult.setBackground(new java.awt.Color(255, 153, 0));

        tblChitietSP.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        tblChitietSP.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "STT", "Mã sản phẩm", "Tên sản phẩm", "Số lượng", "Size", "Đơn giá", "Thành tiền"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblChitietSP.setRowHeight(30);
        tblChitietSP.setSelectionBackground(new java.awt.Color(153, 153, 255));
        tblChitietSP.setShowVerticalLines(false);
        tblChitietSP.getTableHeader().setReorderingAllowed(false);
        tblChitietSP.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblChitietSPMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblChitietSP);
        if (tblChitietSP.getColumnModel().getColumnCount() > 0) {
            tblChitietSP.getColumnModel().getColumn(0).setPreferredWidth(40);
            tblChitietSP.getColumnModel().getColumn(0).setMaxWidth(40);
        }

        javax.swing.GroupLayout pnlResultLayout = new javax.swing.GroupLayout(pnlResult);
        pnlResult.setLayout(pnlResultLayout);
        pnlResultLayout.setHorizontalGroup(
            pnlResultLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 880, Short.MAX_VALUE)
            .addGroup(pnlResultLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 880, Short.MAX_VALUE))
        );
        pnlResultLayout.setVerticalGroup(
            pnlResultLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 525, Short.MAX_VALUE)
            .addGroup(pnlResultLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 460, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout pnlShowLayout = new javax.swing.GroupLayout(pnlShow);
        pnlShow.setLayout(pnlShowLayout);
        pnlShowLayout.setHorizontalGroup(
            pnlShowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlResult, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        pnlShowLayout.setVerticalGroup(
            pnlShowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlResult, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pnlThanhtoan.setRequestFocusEnabled(false);

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel6.setText("Tổng hàng");

        lblTongsohang.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblTongsohang.setText("0");

        lblTongtienTT.setFont(new java.awt.Font("Tahoma", 1, 44)); // NOI18N
        lblTongtienTT.setForeground(new java.awt.Color(255, 0, 51));
        lblTongtienTT.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTongtienTT.setText("0");

        btnThanhtoan.setBackground(new java.awt.Color(153, 255, 153));
        btnThanhtoan.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        btnThanhtoan.setForeground(new java.awt.Color(255, 255, 255));
        btnThanhtoan.setText("THANH TOÁN");
        btnThanhtoan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThanhtoanActionPerformed(evt);
            }
        });

        ckInhoadon.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        ckInhoadon.setText("IN HÓA ĐƠN");

        jPanel1.setBackground(new java.awt.Color(204, 204, 204));

        lblTennv.setBackground(new java.awt.Color(255, 255, 255));
        lblTennv.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblTennv.setForeground(new java.awt.Color(153, 153, 153));
        lblTennv.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTennv.setText("xxx - Nhân viên thu ngân");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblTennv, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 1158, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addComponent(lblTennv, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(46, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout pnlThanhtoanLayout = new javax.swing.GroupLayout(pnlThanhtoan);
        pnlThanhtoan.setLayout(pnlThanhtoanLayout);
        pnlThanhtoanLayout.setHorizontalGroup(
            pnlThanhtoanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblTongtienTT, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(pnlThanhtoanLayout.createSequentialGroup()
                .addGroup(pnlThanhtoanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlThanhtoanLayout.createSequentialGroup()
                        .addGap(39, 39, 39)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 920, Short.MAX_VALUE)
                        .addComponent(lblTongsohang, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlThanhtoanLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(pnlThanhtoanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(ckInhoadon, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnThanhtoan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        pnlThanhtoanLayout.setVerticalGroup(
            pnlThanhtoanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlThanhtoanLayout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addGroup(pnlThanhtoanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(lblTongsohang))
                .addGap(49, 49, 49)
                .addComponent(lblTongtienTT, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(55, 55, 55)
                .addComponent(ckInhoadon)
                .addGap(18, 18, 18)
                .addComponent(btnThanhtoan, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout tabBanhangLayout = new javax.swing.GroupLayout(tabBanhang);
        tabBanhang.setLayout(tabBanhangLayout);
        tabBanhangLayout.setHorizontalGroup(
            tabBanhangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tabBanhangLayout.createSequentialGroup()
                .addGroup(tabBanhangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(pnlShow, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlTimkiem, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, 0)
                .addComponent(pnlThanhtoan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        tabBanhangLayout.setVerticalGroup(
            tabBanhangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tabBanhangLayout.createSequentialGroup()
                .addComponent(pnlTimkiem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(pnlShow, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(pnlThanhtoan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        Tab.addTab("Bán hàng", new javax.swing.ImageIcon(getClass().getResource("/Images/shop.png")), tabBanhang); // NOI18N

        tabXemhoadon.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N

        jPanel3.setBackground(new java.awt.Color(204, 204, 204));

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel10.setText("Từ ngày");

        lblTongthu.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblTongthu.setForeground(new java.awt.Color(255, 51, 51));
        lblTongthu.setText("10.000");

        jLabel15.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel15.setText("Tổng thu:");

        dcHoadonBatdau.setDateFormatString("dd/MM/yyyy");

        dcHoadonKetthuc.setDateFormatString("dd/MM/yyyy");

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel11.setText("Đến");

        btnLochoadon.setBackground(new java.awt.Color(204, 255, 255));
        btnLochoadon.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnLochoadon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/filter_16px.png"))); // NOI18N
        btnLochoadon.setText("Lọc");
        btnLochoadon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLochoadonActionPerformed(evt);
            }
        });

        jLabel52.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel52.setText("Tổng hóa đơn:");

        lblTonghoadon.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblTonghoadon.setForeground(new java.awt.Color(255, 51, 51));
        lblTonghoadon.setText("100");

        btnHoadonhomnay.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnHoadonhomnay.setText("Hôm nay");
        btnHoadonhomnay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHoadonhomnayActionPerformed(evt);
            }
        });

        btnInhoadon.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnInhoadon.setText("In");
        btnInhoadon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInhoadonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(dcHoadonBatdau, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(37, 37, 37)
                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(dcHoadonKetthuc, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(37, 37, 37)
                .addComponent(btnLochoadon, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnHoadonhomnay, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnInhoadon, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel52)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblTonghoadon, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel15)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblTongthu, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(dcHoadonKetthuc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11)
                    .addComponent(dcHoadonBatdau, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel52)
                        .addComponent(lblTonghoadon)
                        .addComponent(jLabel15)
                        .addComponent(lblTongthu)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnLochoadon, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnHoadonhomnay, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnInhoadon)))
                .addContainerGap())
        );

        jLabel10.getAccessibleContext().setAccessibleName("lblThoigian");
        jLabel52.getAccessibleContext().setAccessibleName("Tổng hóa đơn");

        tblHoadon.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        tblHoadon.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "*", "Mã hóa đơn", "Mã nhân viên", "Tổng tiền", "Thời gian"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblHoadon.setRowHeight(30);
        tblHoadon.setSelectionBackground(new java.awt.Color(204, 255, 255));
        tblHoadon.setShowVerticalLines(false);
        tblHoadon.getTableHeader().setReorderingAllowed(false);
        tblHoadon.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblHoadonMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tblHoadon);
        if (tblHoadon.getColumnModel().getColumnCount() > 0) {
            tblHoadon.getColumnModel().getColumn(0).setPreferredWidth(50);
            tblHoadon.getColumnModel().getColumn(0).setMaxWidth(50);
        }

        tblChitiethoadon.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "*", "Mã sản phẩm", "Tên sản phẩm", "Số lượng", "Giá", "Size", "Tổng tiền"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblChitiethoadon.getTableHeader().setReorderingAllowed(false);
        jScrollPane4.setViewportView(tblChitiethoadon);
        if (tblChitiethoadon.getColumnModel().getColumnCount() > 0) {
            tblChitiethoadon.getColumnModel().getColumn(0).setPreferredWidth(40);
            tblChitiethoadon.getColumnModel().getColumn(0).setMaxWidth(40);
        }

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 1362, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 670, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 597, Short.MAX_VALUE)
            .addComponent(jScrollPane4)
        );

        javax.swing.GroupLayout tabXemhoadonLayout = new javax.swing.GroupLayout(tabXemhoadon);
        tabXemhoadon.setLayout(tabXemhoadonLayout);
        tabXemhoadonLayout.setHorizontalGroup(
            tabXemhoadonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        tabXemhoadonLayout.setVerticalGroup(
            tabXemhoadonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tabXemhoadonLayout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        Tab.addTab("Xem hóa đơn", new javax.swing.ImageIcon(getClass().getResource("/Images/money.png")), tabXemhoadon); // NOI18N

        jPanel2.setBackground(new java.awt.Color(204, 204, 204));

        jLabel17.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel17.setText("Loại");

        cboLoai.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cboLoai.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tất cả", "Thể thao", "Da", "Cao gót" }));

        jLabel18.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel18.setText("Nhãn hiệu");

        cboNhanhieu.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cboNhanhieu.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tất cả", "ADIDAS", "NIKE" }));

        btnTimkiemtracuu.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnTimkiemtracuu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/search_25px.png"))); // NOI18N
        btnTimkiemtracuu.setText("Tìm");
        btnTimkiemtracuu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTimkiemtracuuActionPerformed(evt);
            }
        });

        jLabel65.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel65.setText("Trạng thái");

        cboTrangthaisp.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cboTrangthaisp.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tất cả", "Đang bán", "Tạm ngưng" }));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel17)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cboLoai, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel18)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cboNhanhieu, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel65)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cboTrangthaisp, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(txtTimkiemtracuu, javax.swing.GroupLayout.PREFERRED_SIZE, 219, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnTimkiemtracuu)
                .addContainerGap(1129, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboLoai, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboNhanhieu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel65, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboTrangthaisp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTimkiemtracuu, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnTimkiemtracuu, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(20, Short.MAX_VALUE))
        );

        tblTracuu.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "STT", "Mã sản phẩm", "Tên sản phẩm", "Giá", "Màu", "Trạng thái", "Thời gian tạo", "Thời gian sửa"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblTracuu.setRowHeight(25);
        tblTracuu.setShowVerticalLines(false);
        tblTracuu.getTableHeader().setReorderingAllowed(false);
        tblTracuu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblTracuuMouseClicked(evt);
            }
        });
        jScrollPane8.setViewportView(tblTracuu);
        if (tblTracuu.getColumnModel().getColumnCount() > 0) {
            tblTracuu.getColumnModel().getColumn(0).setMinWidth(30);
            tblTracuu.getColumnModel().getColumn(0).setMaxWidth(50);
        }

        jPanel12.setBorder(javax.swing.BorderFactory.createTitledBorder("Chi tiết sản phẩm"));

        tblSizeTracuu.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Mã", "Size", "Số lượng còn", "Ngày tạo", "Đã cập nhật"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblSizeTracuu.setRowHeight(25);
        tblSizeTracuu.setShowVerticalLines(false);
        tblSizeTracuu.getTableHeader().setReorderingAllowed(false);
        jScrollPane3.setViewportView(tblSizeTracuu);
        if (tblSizeTracuu.getColumnModel().getColumnCount() > 0) {
            tblSizeTracuu.getColumnModel().getColumn(0).setPreferredWidth(60);
            tblSizeTracuu.getColumnModel().getColumn(0).setMaxWidth(60);
            tblSizeTracuu.getColumnModel().getColumn(1).setPreferredWidth(100);
            tblSizeTracuu.getColumnModel().getColumn(1).setMaxWidth(100);
            tblSizeTracuu.getColumnModel().getColumn(2).setPreferredWidth(120);
            tblSizeTracuu.getColumnModel().getColumn(2).setMaxWidth(120);
            tblSizeTracuu.getColumnModel().getColumn(3).setPreferredWidth(200);
            tblSizeTracuu.getColumnModel().getColumn(3).setMaxWidth(200);
            tblSizeTracuu.getColumnModel().getColumn(4).setPreferredWidth(200);
            tblSizeTracuu.getColumnModel().getColumn(4).setMaxWidth(200);
        }

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel3.setText("Nhãn hiệu:");

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel4.setText("Loại:");

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel7.setText("Trạng thái:");

        lblNhanhieu.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblNhanhieu.setText(" ");

        lblLoai.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblLoai.setText(" ");

        lblTrangthai.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblTrangthai.setText(" ");

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 440, Short.MAX_VALUE)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(18, 18, 18)
                        .addComponent(lblNhanhieu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addGap(18, 18, 18)
                        .addComponent(lblTrangthai, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addGap(18, 18, 18)
                        .addComponent(lblLoai, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(lblNhanhieu))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(lblLoai))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(lblTrangthai))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 463, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jScrollPane8, javax.swing.GroupLayout.DEFAULT_SIZE, 1580, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane8)
            .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout tabTracuuLayout = new javax.swing.GroupLayout(tabTracuu);
        tabTracuu.setLayout(tabTracuuLayout);
        tabTracuuLayout.setHorizontalGroup(
            tabTracuuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        tabTracuuLayout.setVerticalGroup(
            tabTracuuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tabTracuuLayout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        Tab.addTab("Tra cứu", new javax.swing.ImageIcon(getClass().getResource("/Images/reading_40px.png")), tabTracuu); // NOI18N

        TabQTV.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N

        tabSanpham.setBackground(new java.awt.Color(204, 204, 204));

        tabQLSP.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        tabQLSP.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        tabQLSP.setPreferredSize(new java.awt.Dimension(363, 789));

        tabCapnhat.setPreferredSize(new java.awt.Dimension(363, 789));

        pnlCapNhatSP.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel12.setText("Tên");

        txtTenSpCapnhat.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        txtMauSpCapnhat.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        jLabel34.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel34.setText("Màu");

        jLabel39.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel39.setText("Ảnh");

        txtAnhSpCapnhat.setEditable(false);
        txtAnhSpCapnhat.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        jLabel38.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel38.setText("Mô tả");

        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel13.setText("Giá");

        txtMotaSpCapnhat.setColumns(20);
        txtMotaSpCapnhat.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtMotaSpCapnhat.setRows(5);
        jScrollPane7.setViewportView(txtMotaSpCapnhat);

        jLabel35.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel35.setText("Nhãn hiệu");

        cboNhanhieuCapnhat.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N

        spnGiaSpCapnhat.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        spnGiaSpCapnhat.setModel(new javax.swing.SpinnerNumberModel());

        jLabel36.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel36.setText("Loại");

        cboLoaiCapnhat.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N

        jLabel37.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel37.setText("Trạng thái");

        cboTrangthaiCapnhat.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        cboTrangthaiCapnhat.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Đang bán", "Tạm ngưng" }));

        btnCapnhatSp.setBackground(new java.awt.Color(102, 255, 102));
        btnCapnhatSp.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnCapnhatSp.setForeground(new java.awt.Color(51, 51, 51));
        btnCapnhatSp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/available_updates_32px.png"))); // NOI18N
        btnCapnhatSp.setText("CẬP NHẬT");
        btnCapnhatSp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCapnhatSpActionPerformed(evt);
            }
        });

        btnXoaSp.setBackground(new java.awt.Color(204, 204, 204));
        btnXoaSp.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnXoaSp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/delete_16px.png"))); // NOI18N
        btnXoaSp.setText(" Xóa");
        btnXoaSp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaSpActionPerformed(evt);
            }
        });

        jPanel15.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        btnTkspQL.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnTkspQL.setText("Tìm");
        btnTkspQL.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTkspQLActionPerformed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel8.setText("Nhập mã sản phẩm");

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel8)
                .addGap(18, 18, 18)
                .addComponent(txtTkspCapnhat, javax.swing.GroupLayout.PREFERRED_SIZE, 245, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnTkspQL, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnTkspQL, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE)
                    .addComponent(txtTkspCapnhat, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jLabel14.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel14.setText("Mã");

        txtMaspQL.setEditable(false);
        txtMaspQL.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        javax.swing.GroupLayout pnlCapNhatSPLayout = new javax.swing.GroupLayout(pnlCapNhatSP);
        pnlCapNhatSP.setLayout(pnlCapNhatSPLayout);
        pnlCapNhatSPLayout.setHorizontalGroup(
            pnlCapNhatSPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(pnlCapNhatSPLayout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(pnlCapNhatSPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlCapNhatSPLayout.createSequentialGroup()
                        .addGroup(pnlCapNhatSPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel39, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel34, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(pnlCapNhatSPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlCapNhatSPLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(pnlCapNhatSPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtMauSpCapnhat)
                                    .addComponent(txtTenSpCapnhat, javax.swing.GroupLayout.PREFERRED_SIZE, 352, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlCapNhatSPLayout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addGroup(pnlCapNhatSPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(spnGiaSpCapnhat, javax.swing.GroupLayout.DEFAULT_SIZE, 352, Short.MAX_VALUE)
                                    .addComponent(txtAnhSpCapnhat)))))
                    .addGroup(pnlCapNhatSPLayout.createSequentialGroup()
                        .addGroup(pnlCapNhatSPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel35, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel36, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(pnlCapNhatSPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cboNhanhieuCapnhat, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cboLoaiCapnhat, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(pnlCapNhatSPLayout.createSequentialGroup()
                        .addComponent(jLabel37, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cboTrangthaiCapnhat, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlCapNhatSPLayout.createSequentialGroup()
                        .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtMaspQL, javax.swing.GroupLayout.PREFERRED_SIZE, 352, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(pnlCapNhatSPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlCapNhatSPLayout.createSequentialGroup()
                        .addGap(37, 37, 37)
                        .addGroup(pnlCapNhatSPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel38, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 366, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 11, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlCapNhatSPLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(pnlCapNhatSPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnCapnhatSp, javax.swing.GroupLayout.PREFERRED_SIZE, 247, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnXoaSp, javax.swing.GroupLayout.PREFERRED_SIZE, 247, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(77, 77, 77))))
        );
        pnlCapNhatSPLayout.setVerticalGroup(
            pnlCapNhatSPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlCapNhatSPLayout.createSequentialGroup()
                .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20)
                .addGroup(pnlCapNhatSPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtMaspQL, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel38, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(pnlCapNhatSPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlCapNhatSPLayout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addGroup(pnlCapNhatSPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtTenSpCapnhat, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(20, 20, 20)
                        .addGroup(pnlCapNhatSPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel34, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtMauSpCapnhat, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(20, 20, 20)
                        .addGroup(pnlCapNhatSPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtAnhSpCapnhat, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel39, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(22, 22, 22)
                        .addGroup(pnlCapNhatSPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(spnGiaSpCapnhat, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(20, 20, 20)
                        .addGroup(pnlCapNhatSPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel35, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cboNhanhieuCapnhat, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(pnlCapNhatSPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cboLoaiCapnhat, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel36, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(20, 20, 20)
                        .addGroup(pnlCapNhatSPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel37, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cboTrangthaiCapnhat, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(pnlCapNhatSPLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 286, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(38, 38, 38)
                        .addComponent(btnCapnhatSp, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnXoaSp, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel17.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED), "Size và số lượng", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12))); // NOI18N
        jPanel17.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel9.setText("Số lượng:");

        cboSizespCapnhat.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cboSizespCapnhat.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboSizespCapnhatItemStateChanged(evt);
            }
        });

        jLabel20.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel20.setText("Size:");

        spnSoluongSpCapnhat.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(0), Integer.valueOf(0), null, Integer.valueOf(1)));

        btnCapnhatSoluongSize.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnCapnhatSoluongSize.setText("Cập nhật");
        btnCapnhatSoluongSize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCapnhatSoluongSizeActionPerformed(evt);
            }
        });

        jButton12.setText("Xóa Size");
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9))
                .addGap(27, 27, 27)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnCapnhatSoluongSize, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(spnSoluongSpCapnhat)
                    .addComponent(cboSizespCapnhat, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(34, 34, 34))
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cboSizespCapnhat, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel17Layout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addComponent(spnSoluongSpCapnhat, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel17Layout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(btnCapnhatSoluongSize, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton12, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel18.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED), "Thêm Size", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12))); // NOI18N

        jLabel25.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel25.setText("Nhập size:");

        btnThemSize.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnThemSize.setText("Thêm");
        btnThemSize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemSizeActionPerformed(evt);
            }
        });

        spnNhapsizeCapnhat.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(0), Integer.valueOf(0), null, Integer.valueOf(1)));

        jLabel28.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel28.setText("Nhập số lượng:");

        spnNhapsoluongCapnhat.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(0), Integer.valueOf(0), null, Integer.valueOf(1)));

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel25, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel28, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(24, 24, 24)
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnThemSize, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(spnNhapsizeCapnhat)
                    .addComponent(spnNhapsoluongCapnhat, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGap(38, 38, 38))
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(spnNhapsizeCapnhat, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(spnNhapsoluongCapnhat, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel28, javax.swing.GroupLayout.DEFAULT_SIZE, 34, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(btnThemSize, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18))
        );

        jPanel13.setBorder(javax.swing.BorderFactory.createTitledBorder("Lịch sử"));

        jLabel90.setText("- Thời gian tạo sản phẩm:");

        lblTgtaosp.setText("yyyy-mm-dd HH:mm:sss");

        jLabel92.setText("- Thời gian sửa sản phẩm:");

        lblTgsuasp.setText("yyyy-mm-dd HH:mm:sss");

        jLabel94.setText("- Thời gian sửa Size:");

        lblTgsuasize.setText("yyyy-mm-dd HH:mm:sss");

        jLabel96.setText("- Thời gian tạo Size");

        lblTgtaosize.setText("yyyy-mm-dd HH:mm:sss");

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel90)
                            .addComponent(jLabel92))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(lblTgtaosp, javax.swing.GroupLayout.DEFAULT_SIZE, 197, Short.MAX_VALUE)
                            .addComponent(lblTgsuasp, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel96)
                            .addComponent(jLabel94))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 805, Short.MAX_VALUE)
                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(lblTgtaosize, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblTgsuasize, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel90)
                    .addComponent(lblTgtaosp))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel92)
                    .addComponent(lblTgsuasp))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel96)
                    .addComponent(lblTgtaosize))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel94)
                    .addComponent(lblTgsuasize))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout tabCapnhatLayout = new javax.swing.GroupLayout(tabCapnhat);
        tabCapnhat.setLayout(tabCapnhatLayout);
        tabCapnhatLayout.setHorizontalGroup(
            tabCapnhatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tabCapnhatLayout.createSequentialGroup()
                .addComponent(pnlCapNhatSP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(tabCapnhatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        tabCapnhatLayout.setVerticalGroup(
            tabCapnhatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tabCapnhatLayout.createSequentialGroup()
                .addComponent(jPanel17, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(pnlCapNhatSP, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        tabQLSP.addTab("Cập nhật", new javax.swing.ImageIcon(getClass().getResource("/Images/available_updates_16px.png")), tabCapnhat); // NOI18N

        tabThem.setPreferredSize(new java.awt.Dimension(363, 789));

        pnlCapNhatSP4.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel80.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel80.setText("Tên");

        jLabel81.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel81.setText("Màu");

        jLabel82.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel82.setText("Ảnh");

        jLabel83.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel83.setText("Mô tả");

        jLabel84.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel84.setText("Giá");

        txtMotaSpCapnhat4.setColumns(20);
        txtMotaSpCapnhat4.setRows(5);
        jScrollPane13.setViewportView(txtMotaSpCapnhat4);

        jLabel85.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel85.setText("Nhãn hiệu");

        cboNhanhieuCapnhat4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cboNhanhieuCapnhat4.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        spnGiaSpCapnhat4.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        spnGiaSpCapnhat4.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(0), Integer.valueOf(0), null, Integer.valueOf(1)));

        jLabel86.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel86.setText("Loại");

        cboLoaiCapnhat4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cboLoaiCapnhat4.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel87.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel87.setText("Trạng thái");

        cboTrangthaiCapnhat4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cboTrangthaiCapnhat4.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Đang bán", "Tạm ngưng" }));

        jButton17.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jButton17.setText("Chọn");

        javax.swing.GroupLayout pnlCapNhatSP4Layout = new javax.swing.GroupLayout(pnlCapNhatSP4);
        pnlCapNhatSP4.setLayout(pnlCapNhatSP4Layout);
        pnlCapNhatSP4Layout.setHorizontalGroup(
            pnlCapNhatSP4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlCapNhatSP4Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(pnlCapNhatSP4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel83, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pnlCapNhatSP4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jScrollPane13, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel84, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(pnlCapNhatSP4Layout.createSequentialGroup()
                            .addGroup(pnlCapNhatSP4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jLabel82, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel81, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel80, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addGroup(pnlCapNhatSP4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(pnlCapNhatSP4Layout.createSequentialGroup()
                                    .addComponent(txtLinkanhSpCapnhat4, javax.swing.GroupLayout.PREFERRED_SIZE, 331, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(jButton17, javax.swing.GroupLayout.DEFAULT_SIZE, 104, Short.MAX_VALUE))
                                .addComponent(txtTenSpCapnhat5)
                                .addComponent(txtMauSpCapnhat4)))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pnlCapNhatSP4Layout.createSequentialGroup()
                            .addComponent(jLabel87, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(cboTrangthaiCapnhat4, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pnlCapNhatSP4Layout.createSequentialGroup()
                            .addGroup(pnlCapNhatSP4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel85, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel86, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addGroup(pnlCapNhatSP4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(cboNhanhieuCapnhat4, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(cboLoaiCapnhat4, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(pnlCapNhatSP4Layout.createSequentialGroup()
                                    .addComponent(spnGiaSpCapnhat4, javax.swing.GroupLayout.PREFERRED_SIZE, 331, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(0, 0, Short.MAX_VALUE))))))
                .addContainerGap(768, Short.MAX_VALUE))
        );
        pnlCapNhatSP4Layout.setVerticalGroup(
            pnlCapNhatSP4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlCapNhatSP4Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(pnlCapNhatSP4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel80, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTenSpCapnhat5, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(pnlCapNhatSP4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel81, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtMauSpCapnhat4, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(pnlCapNhatSP4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtLinkanhSpCapnhat4, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel82, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton17, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pnlCapNhatSP4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel84, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(spnGiaSpCapnhat4, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(pnlCapNhatSP4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel85, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cboNhanhieuCapnhat4, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pnlCapNhatSP4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cboLoaiCapnhat4, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel86, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(pnlCapNhatSP4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel87, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboTrangthaiCapnhat4, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel83, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane13)
                .addContainerGap())
        );

        jPanel10.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED), "Size và số lượng", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12))); // NOI18N

        jLabel88.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel88.setText("Nhập Size:");

        jSpinner2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jSpinner2.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(0), Integer.valueOf(0), null, Integer.valueOf(1)));

        jLabel89.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel89.setText("Số lượng:");

        jSpinner4.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jSpinner4.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(1), Integer.valueOf(1), null, Integer.valueOf(1)));

        jButton16.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButton16.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/plus_16px.png"))); // NOI18N
        jButton16.setText("Thêm Size");

        jButton18.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/delete_16px.png"))); // NOI18N
        jButton18.setText("Xóa Size");

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Size", "Số lượng"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.setRowHeight(25);
        jTable1.setShowVerticalLines(false);
        jTable1.getTableHeader().setReorderingAllowed(false);
        jScrollPane14.setViewportView(jTable1);

        btnCapnhatSp4.setBackground(new java.awt.Color(255, 204, 204));
        btnCapnhatSp4.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnCapnhatSp4.setForeground(new java.awt.Color(51, 51, 51));
        btnCapnhatSp4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/add_25px.png"))); // NOI18N
        btnCapnhatSp4.setText("THÊM SẢN PHẨM");
        btnCapnhatSp4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCapnhatSp4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jScrollPane14, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel10Layout.createSequentialGroup()
                                .addComponent(jLabel88, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jSpinner2, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel89, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jSpinner4, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jButton16)
                                .addGap(18, 18, 18)
                                .addComponent(jButton18)))
                        .addContainerGap())
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addComponent(btnCapnhatSp4, javax.swing.GroupLayout.PREFERRED_SIZE, 247, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel88, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSpinner2, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel89, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSpinner4, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton16, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton18, javax.swing.GroupLayout.DEFAULT_SIZE, 41, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane14, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnCapnhatSp4, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout tabThemLayout = new javax.swing.GroupLayout(tabThem);
        tabThem.setLayout(tabThemLayout);
        tabThemLayout.setHorizontalGroup(
            tabThemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tabThemLayout.createSequentialGroup()
                .addComponent(pnlCapNhatSP4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        tabThemLayout.setVerticalGroup(
            tabThemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlCapNhatSP4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        tabQLSP.addTab("Thêm", new javax.swing.ImageIcon(getClass().getResource("/Images/plus_16px.png")), tabThem); // NOI18N

        javax.swing.GroupLayout tabSanphamLayout = new javax.swing.GroupLayout(tabSanpham);
        tabSanpham.setLayout(tabSanphamLayout);
        tabSanphamLayout.setHorizontalGroup(
            tabSanphamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabQLSP, javax.swing.GroupLayout.DEFAULT_SIZE, 2033, Short.MAX_VALUE)
        );
        tabSanphamLayout.setVerticalGroup(
            tabSanphamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addComponent(tabQLSP, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 617, Short.MAX_VALUE)
        );

        TabQTV.addTab("Sản phẩm", new javax.swing.ImageIcon(getClass().getResource("/Images/sneakers_16px.png")), tabSanpham); // NOI18N

        tabTabChitiet.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N

        tblNhanhieu.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "STT", "Mã nhãn hiệu", "Tên nhãn hiệu", "Ngày thêm", "Ngày cập nhật"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblNhanhieu.setRowHeight(25);
        tblNhanhieu.setShowVerticalLines(false);
        tblNhanhieu.getTableHeader().setReorderingAllowed(false);
        jScrollPane5.setViewportView(tblNhanhieu);
        if (tblNhanhieu.getColumnModel().getColumnCount() > 0) {
            tblNhanhieu.getColumnModel().getColumn(0).setPreferredWidth(50);
            tblNhanhieu.getColumnModel().getColumn(0).setMaxWidth(50);
        }

        btnThemnhanhieu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/plus_16px.png"))); // NOI18N
        btnThemnhanhieu.setText("Thêm nhãn hiệu");
        btnThemnhanhieu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemnhanhieuActionPerformed(evt);
            }
        });

        btnXoanhanhieu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/delete_16px.png"))); // NOI18N
        btnXoanhanhieu.setText("Xóa");
        btnXoanhanhieu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoanhanhieuActionPerformed(evt);
            }
        });

        btnDoitenNhanhieu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/hand_with_pen_16px.png"))); // NOI18N
        btnDoitenNhanhieu.setText("Đổi tên");
        btnDoitenNhanhieu.setName("btnDoitennhanhieu"); // NOI18N
        btnDoitenNhanhieu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDoitenNhanhieuActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout tabNhanhieuLayout = new javax.swing.GroupLayout(tabNhanhieu);
        tabNhanhieu.setLayout(tabNhanhieuLayout);
        tabNhanhieuLayout.setHorizontalGroup(
            tabNhanhieuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tabNhanhieuLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtTennhanhhieu, javax.swing.GroupLayout.PREFERRED_SIZE, 256, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnThemnhanhieu, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addComponent(btnXoanhanhieu)
                .addGap(82, 82, 82)
                .addComponent(txtTenNhanhieumoi, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnDoitenNhanhieu)
                .addContainerGap(1107, Short.MAX_VALUE))
            .addComponent(jScrollPane5, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        tabNhanhieuLayout.setVerticalGroup(
            tabNhanhieuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tabNhanhieuLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(tabNhanhieuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtTenNhanhieumoi, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDoitenNhanhieu, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(tabNhanhieuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtTennhanhhieu, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnThemnhanhieu, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnXoanhanhieu, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 536, Short.MAX_VALUE))
        );

        tabTabChitiet.addTab("Nhãn hiệu", new javax.swing.ImageIcon(getClass().getResource("/Images/identification_documents_16px.png")), tabNhanhieu); // NOI18N

        btnThemLoai.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/plus_16px.png"))); // NOI18N
        btnThemLoai.setText("Thêm loại");
        btnThemLoai.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemLoaiActionPerformed(evt);
            }
        });

        btnXoaLoai.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/delete_16px.png"))); // NOI18N
        btnXoaLoai.setText(" Xóa");
        btnXoaLoai.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaLoaiActionPerformed(evt);
            }
        });

        btnDoitenLoai.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/hand_with_pen_16px.png"))); // NOI18N
        btnDoitenLoai.setText("Đổi tên");
        btnDoitenLoai.setName("btnDoitennhanhieu"); // NOI18N
        btnDoitenLoai.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDoitenLoaiActionPerformed(evt);
            }
        });

        tblLoai.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "STT", "Mã loại", "Tên loại", "Ngày thêm", "Ngày cập nhật"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblLoai.setRowHeight(25);
        tblLoai.setShowVerticalLines(false);
        tblLoai.getTableHeader().setReorderingAllowed(false);
        jScrollPane6.setViewportView(tblLoai);
        if (tblLoai.getColumnModel().getColumnCount() > 0) {
            tblLoai.getColumnModel().getColumn(0).setPreferredWidth(50);
            tblLoai.getColumnModel().getColumn(0).setMaxWidth(50);
        }

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtTenLoai, javax.swing.GroupLayout.PREFERRED_SIZE, 256, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnThemLoai)
                .addGap(25, 25, 25)
                .addComponent(btnXoaLoai)
                .addGap(82, 82, 82)
                .addComponent(txtTenLoaiMoi, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnDoitenLoai)
                .addContainerGap(1159, Short.MAX_VALUE))
            .addComponent(jScrollPane6)
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtTenLoaiMoi, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDoitenLoai, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtTenLoai, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnThemLoai, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnXoaLoai, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 536, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout tabLoaiLayout = new javax.swing.GroupLayout(tabLoai);
        tabLoai.setLayout(tabLoaiLayout);
        tabLoaiLayout.setHorizontalGroup(
            tabLoaiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        tabLoaiLayout.setVerticalGroup(
            tabLoaiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel16, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        tabTabChitiet.addTab("Loại", new javax.swing.ImageIcon(getClass().getResource("/Images/elective_16px.png")), tabLoai); // NOI18N

        javax.swing.GroupLayout tabChitietLayout = new javax.swing.GroupLayout(tabChitiet);
        tabChitiet.setLayout(tabChitietLayout);
        tabChitietLayout.setHorizontalGroup(
            tabChitietLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabTabChitiet)
        );
        tabChitietLayout.setVerticalGroup(
            tabChitietLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabTabChitiet)
        );

        TabQTV.addTab("Nhãn hiệu & Loại", new javax.swing.ImageIcon(getClass().getResource("/Images/detail_16px.png")), tabChitiet); // NOI18N

        tblTV.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        tblTV.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "STT", "Mã người dùng", "Tên người dùng", "Mật khẩu", "Ngày tạo", "Ngày cập nhật"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblTV.setRowHeight(25);
        tblTV.setShowVerticalLines(false);
        tblTV.getTableHeader().setReorderingAllowed(false);
        tblTV.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblTVMouseClicked(evt);
            }
        });
        jScrollPane9.setViewportView(tblTV);
        if (tblTV.getColumnModel().getColumnCount() > 0) {
            tblTV.getColumnModel().getColumn(0).setPreferredWidth(50);
            tblTV.getColumnModel().getColumn(0).setMaxWidth(50);
        }

        jPanel6.setBackground(new java.awt.Color(153, 153, 153));

        tabSetThanhvien.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N

        btnCapnhatTV.setBackground(new java.awt.Color(102, 153, 255));
        btnCapnhatTV.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnCapnhatTV.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/available_updates_32px.png"))); // NOI18N
        btnCapnhatTV.setText(" Cập nhật");
        btnCapnhatTV.setToolTipText("");
        btnCapnhatTV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCapnhatTVActionPerformed(evt);
            }
        });

        cboTrangthaitvSua.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cboTrangthaitvSua.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Đang làm", "Đã nghỉ" }));

        jLabel56.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel56.setText("Trạng thái");

        jLabel47.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel47.setText("Chức vụ");

        cboChucvutvSua.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cboChucvutvSua.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Admin", "Quản lí App", "Thu ngân", "Quản lí Web", "Nhân viên Web" }));

        txtTentvSua.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        jLabel32.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel32.setText("Họ và Tên");

        jLabel43.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel43.setText("Mật khẩu");

        txtMktvSua.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        jLabel54.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel54.setText("Số điện thoại");

        txtSdttvSua.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        jLabel55.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel55.setText("Địa chỉ");

        txtDiachitvSua.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        jLabel57.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel57.setText("Email");

        txtEmailtvSua.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        jLabel59.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel59.setText("Ngày sinh");

        dcNgaysinhtvSua.setDateFormatString("dd/MM/yyyy");

        javax.swing.GroupLayout tabSuaThanhvienLayout = new javax.swing.GroupLayout(tabSuaThanhvien);
        tabSuaThanhvien.setLayout(tabSuaThanhvienLayout);
        tabSuaThanhvienLayout.setHorizontalGroup(
            tabSuaThanhvienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tabSuaThanhvienLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(tabSuaThanhvienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(tabSuaThanhvienLayout.createSequentialGroup()
                        .addGroup(tabSuaThanhvienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(tabSuaThanhvienLayout.createSequentialGroup()
                                .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtTentvSua))
                            .addGroup(tabSuaThanhvienLayout.createSequentialGroup()
                                .addComponent(jLabel43, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtMktvSua))
                            .addGroup(tabSuaThanhvienLayout.createSequentialGroup()
                                .addComponent(jLabel54, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtSdttvSua))
                            .addGroup(tabSuaThanhvienLayout.createSequentialGroup()
                                .addComponent(jLabel55, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtDiachitvSua))
                            .addGroup(tabSuaThanhvienLayout.createSequentialGroup()
                                .addComponent(jLabel57, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtEmailtvSua))
                            .addGroup(tabSuaThanhvienLayout.createSequentialGroup()
                                .addGroup(tabSuaThanhvienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(tabSuaThanhvienLayout.createSequentialGroup()
                                        .addComponent(jLabel47, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(cboChucvutvSua, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(tabSuaThanhvienLayout.createSequentialGroup()
                                        .addComponent(jLabel56, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(cboTrangthaitvSua, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(tabSuaThanhvienLayout.createSequentialGroup()
                                .addComponent(jLabel59, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(dcNgaysinhtvSua, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tabSuaThanhvienLayout.createSequentialGroup()
                        .addGap(0, 81, Short.MAX_VALUE)
                        .addComponent(btnCapnhatTV, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(79, 79, 79))))
        );
        tabSuaThanhvienLayout.setVerticalGroup(
            tabSuaThanhvienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tabSuaThanhvienLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(tabSuaThanhvienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtTentvSua, javax.swing.GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE)
                    .addComponent(jLabel32, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(20, 20, 20)
                .addGroup(tabSuaThanhvienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtMktvSua)
                    .addComponent(jLabel43, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(tabSuaThanhvienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtSdttvSua)
                    .addComponent(jLabel54, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(tabSuaThanhvienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtDiachitvSua)
                    .addComponent(jLabel55, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(tabSuaThanhvienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtEmailtvSua)
                    .addComponent(jLabel57, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(tabSuaThanhvienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel59, javax.swing.GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE)
                    .addComponent(dcNgaysinhtvSua, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(20, 20, 20)
                .addGroup(tabSuaThanhvienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(cboChucvutvSua, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)
                    .addComponent(jLabel47, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(20, 20, 20)
                .addGroup(tabSuaThanhvienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cboTrangthaitvSua, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel56, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addComponent(btnCapnhatTV, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tabSetThanhvien.addTab("Sửa", new javax.swing.ImageIcon(getClass().getResource("/Images/available_updates_16px.png")), tabSuaThanhvien); // NOI18N

        btnThemmoi.setBackground(new java.awt.Color(255, 204, 255));
        btnThemmoi.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnThemmoi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/add_user32px.png"))); // NOI18N
        btnThemmoi.setText("Thêm mới");
        btnThemmoi.setToolTipText("");
        btnThemmoi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemmoiActionPerformed(evt);
            }
        });

        cboTrangthaitvThem.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cboTrangthaitvThem.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Đang làm", "Đã nghỉ" }));

        jLabel58.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel58.setText("Trạng thái");

        jLabel60.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel60.setText("Chức vụ");

        cboChucvutvThem.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cboChucvutvThem.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Admin", "Quản lí App", "Thu ngân", "Quản lí Web", "Nhân viên Web" }));

        jLabel61.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel61.setText("Ngày sinh");

        jLabel62.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel62.setText("Email");

        txtEmailtvThem.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        txtDiachitvThem.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        jLabel64.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel64.setText("Địa chỉ");

        jLabel70.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel70.setText("Số điện thoại");

        txtSdttvThem.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        txtMKtvThem.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        jLabel45.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel45.setText("Mật khẩu");

        jLabel71.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel71.setText("Họ và Tên");

        txtTentvThem.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        dcNgaysinhtvThem.setDateFormatString("dd/MM/yyyy");

        javax.swing.GroupLayout tabThemThanhvienLayout = new javax.swing.GroupLayout(tabThemThanhvien);
        tabThemThanhvien.setLayout(tabThemThanhvienLayout);
        tabThemThanhvienLayout.setHorizontalGroup(
            tabThemThanhvienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tabThemThanhvienLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(tabThemThanhvienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(tabThemThanhvienLayout.createSequentialGroup()
                        .addComponent(jLabel71, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtTentvThem))
                    .addGroup(tabThemThanhvienLayout.createSequentialGroup()
                        .addComponent(jLabel45, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtMKtvThem))
                    .addGroup(tabThemThanhvienLayout.createSequentialGroup()
                        .addComponent(jLabel70, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtSdttvThem))
                    .addGroup(tabThemThanhvienLayout.createSequentialGroup()
                        .addComponent(jLabel64, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtDiachitvThem))
                    .addGroup(tabThemThanhvienLayout.createSequentialGroup()
                        .addComponent(jLabel62, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtEmailtvThem))
                    .addGroup(tabThemThanhvienLayout.createSequentialGroup()
                        .addGroup(tabThemThanhvienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(tabThemThanhvienLayout.createSequentialGroup()
                                .addComponent(jLabel60, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(cboChucvutvThem, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(tabThemThanhvienLayout.createSequentialGroup()
                                .addComponent(jLabel58, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(cboTrangthaitvThem, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(tabThemThanhvienLayout.createSequentialGroup()
                        .addComponent(jLabel61, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(dcNgaysinhtvThem, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tabThemThanhvienLayout.createSequentialGroup()
                .addContainerGap(90, Short.MAX_VALUE)
                .addComponent(btnThemmoi, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(80, 80, 80))
        );
        tabThemThanhvienLayout.setVerticalGroup(
            tabThemThanhvienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tabThemThanhvienLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(tabThemThanhvienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtTentvThem, javax.swing.GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE)
                    .addComponent(jLabel71, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(20, 20, 20)
                .addGroup(tabThemThanhvienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtMKtvThem)
                    .addComponent(jLabel45, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(tabThemThanhvienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtSdttvThem)
                    .addComponent(jLabel70, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(tabThemThanhvienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtDiachitvThem)
                    .addComponent(jLabel64, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(tabThemThanhvienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtEmailtvThem)
                    .addComponent(jLabel62, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(tabThemThanhvienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel61, javax.swing.GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE)
                    .addComponent(dcNgaysinhtvThem, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(20, 20, 20)
                .addGroup(tabThemThanhvienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(cboChucvutvThem, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)
                    .addComponent(jLabel60, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(20, 20, 20)
                .addGroup(tabThemThanhvienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cboTrangthaitvThem, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel58, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addComponent(btnThemmoi, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tabSetThanhvien.addTab("Thêm", new javax.swing.ImageIcon(getClass().getResource("/Images/add_user_16px.png")), tabThemThanhvien); // NOI18N

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabSetThanhvien)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabSetThanhvien)
        );

        btnTimTV.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnTimTV.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/search_16px.png"))); // NOI18N
        btnTimTV.setText("Tìm");
        btnTimTV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTimTVActionPerformed(evt);
            }
        });

        txtTimkiemTV.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N

        jLabel51.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel51.setText("Trạng thái");

        cboChucvuTV.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cboChucvuTV.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tất cả", "Admin", "Quản lí", "Thu ngân" }));

        jLabel49.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel49.setText("Chức vụ");

        cboTrangthaiTV.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cboTrangthaiTV.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tất cả", "Đang làm", "Đã nghỉ" }));

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jLabel51)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cboTrangthaiTV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel49)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cboChucvuTV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(txtTimkiemTV, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnTimTV)
                .addContainerGap(954, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel51)
                    .addComponent(cboChucvuTV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel49)
                    .addComponent(cboTrangthaiTV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTimkiemTV, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnTimTV))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout tabThunganLayout = new javax.swing.GroupLayout(tabThungan);
        tabThungan.setLayout(tabThunganLayout);
        tabThunganLayout.setHorizontalGroup(
            tabThunganLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tabThunganLayout.createSequentialGroup()
                .addGroup(tabThunganLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        tabThunganLayout.setVerticalGroup(
            tabThunganLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(tabThunganLayout.createSequentialGroup()
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane9))
        );

        TabQTV.addTab("Thành viên", new javax.swing.ImageIcon(getClass().getResource("/Images/user_groups_16px.png")), tabThungan); // NOI18N

        javax.swing.GroupLayout tabQuanliLayout = new javax.swing.GroupLayout(tabQuanli);
        tabQuanli.setLayout(tabQuanliLayout);
        tabQuanliLayout.setHorizontalGroup(
            tabQuanliLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(TabQTV)
        );
        tabQuanliLayout.setVerticalGroup(
            tabQuanliLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(TabQTV)
        );

        Tab.addTab("Quản trị ", new javax.swing.ImageIcon(getClass().getResource("/Images/manager.png")), tabQuanli); // NOI18N

        jPanel8.setBackground(new java.awt.Color(204, 204, 204));

        jLabel33.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel33.setText("Tên cửa hàng:");

        jTextField1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        jLabel40.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel40.setText("Fanpage:");

        jTextField2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        jTextField3.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        jLabel41.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel41.setText("Địa chỉ:");

        jLabel42.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel42.setText("Số điện thoại:");

        jTextField4.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        jLabel44.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel44.setText("Slide 1:");

        jTextField5.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        jButton1.setText("Chọn");

        jLabel46.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel46.setText("Slide 2:");

        jTextField6.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        jButton2.setText("Chọn");

        jTextField7.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        jTextField8.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        jButton3.setText("Chọn");

        jButton4.setText("Chọn");

        jLabel48.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel48.setText("Slide 4:");

        jLabel50.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel50.setText("Slide 3:");

        jButton5.setBackground(new java.awt.Color(102, 255, 102));
        jButton5.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jButton5.setText("CẬP NHẬT");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addContainerGap(783, Short.MAX_VALUE)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel46, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, 254, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(jPanel8Layout.createSequentialGroup()
                            .addComponent(jLabel44, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 254, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addComponent(jLabel42, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 356, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addComponent(jLabel41, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 356, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addComponent(jLabel40, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 356, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addComponent(jLabel33, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 356, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel48, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, 254, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel50, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, 254, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(766, 766, 766))
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(943, 943, 943)
                .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(122, 122, 122)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel33, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel40, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel41, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel42, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(41, 41, 41)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel44, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1))
                .addGap(18, 18, 18)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel46, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2))
                .addGap(18, 18, 18)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel50, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton4))
                .addGap(18, 18, 18)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel48, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton3))
                .addGap(55, 55, 55)
                .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(55, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout tabCaidatLayout = new javax.swing.GroupLayout(tabCaidat);
        tabCaidat.setLayout(tabCaidatLayout);
        tabCaidatLayout.setHorizontalGroup(
            tabCaidatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        tabCaidatLayout.setVerticalGroup(
            tabCaidatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        Tab.addTab("Cài đặt", new javax.swing.ImageIcon(getClass().getResource("/Images/setting.png")), tabCaidat); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Tab)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Tab)
        );

        Tab.getAccessibleContext().setAccessibleDescription("");

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnTimkiemtracuuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTimkiemtracuuActionPerformed
        doDuLieuTraCuu(scriptTraCuuSP());
    }//GEN-LAST:event_btnTimkiemtracuuActionPerformed

    private void btnCapnhatSpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCapnhatSpActionPerformed
        capnhatSanpham_QLSP();
        doDuLieuTraCuu(scriptTraCuuSP());
    }//GEN-LAST:event_btnCapnhatSpActionPerformed

    private void btnXoaSpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaSpActionPerformed
        XoaSP();
        doDuLieuTraCuu(scriptTraCuuSP());
    }//GEN-LAST:event_btnXoaSpActionPerformed

    private void btnThemnhanhieuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemnhanhieuActionPerformed
        themNhanhieu();
        dodulieuNhanhieu();
    }//GEN-LAST:event_btnThemnhanhieuActionPerformed

    private void btnXoanhanhieuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoanhanhieuActionPerformed
        xoaNhanhieu();
        dodulieuNhanhieu();
    }//GEN-LAST:event_btnXoanhanhieuActionPerformed

    private void btnDoitenLoaiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDoitenLoaiActionPerformed
        doitenLoai();
        dodulieuLoai();
    }//GEN-LAST:event_btnDoitenLoaiActionPerformed

    private void btnDoitenNhanhieuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDoitenNhanhieuActionPerformed
        doitenNhanhieu();
        dodulieuNhanhieu();
    }//GEN-LAST:event_btnDoitenNhanhieuActionPerformed

    private void btnThemLoaiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemLoaiActionPerformed
        themLoai();
        dodulieuLoai();
    }//GEN-LAST:event_btnThemLoaiActionPerformed

    private void btnXoaLoaiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaLoaiActionPerformed
        xoaLoai();
        dodulieuLoai();
    }//GEN-LAST:event_btnXoaLoaiActionPerformed

    private void btnTimTVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTimTVActionPerformed
        doDuLieuBangTV(scriptTimTV());
    }//GEN-LAST:event_btnTimTVActionPerformed

    private void tblTVMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblTVMouseClicked
        doDuLieuThongtinTV();
    }//GEN-LAST:event_tblTVMouseClicked

    private void btnThemmoiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemmoiActionPerformed
        themThanhVien();
        doDuLieuBangTV(scriptTimTV());
    }//GEN-LAST:event_btnThemmoiActionPerformed

    private void btnCapnhatTVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCapnhatTVActionPerformed
        capnhatThanhVien();
        doDuLieuBangTV(scriptTimTV());
    }//GEN-LAST:event_btnCapnhatTVActionPerformed

    private void btnLochoadonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLochoadonActionPerformed
        locHoaDon();
    }//GEN-LAST:event_btnLochoadonActionPerformed

    private void tblHoadonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblHoadonMouseClicked
        chonHoaDon();
    }//GEN-LAST:event_tblHoadonMouseClicked

    private void btnTimActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTimActionPerformed
        timKiemSP(txtTimkiem.getText());
        doDuLieuTK();
        hienThiSoLuongSP_TT();
    }//GEN-LAST:event_btnTimActionPerformed

    private void btnThemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemActionPerformed
        capNhatChiTietDonHang();
        doDuLieuChiTietHoaDon();
    }//GEN-LAST:event_btnThemActionPerformed

    private void cboSizeTTItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboSizeTTItemStateChanged
        //System.out.println(cboSizeTT.getSelectedItem().toString());
        hienThiSoLuongSP_TT();
    }//GEN-LAST:event_cboSizeTTItemStateChanged

    private void btnXoaSpTTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaSpTTActionPerformed
        xoaSpTT();
    }//GEN-LAST:event_btnXoaSpTTActionPerformed

    private void btnThanhtoanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThanhtoanActionPerformed
        if (dsChiTietDonHang.size() > 0) {
            if (JOptionPane.showConfirmDialog(null, "Xác nhận thanh toán", "Thanh toán", 0) == 0) {
                thanhToan_1();
                thanhtoan_2();
                resetTT();
            }
        }

    }//GEN-LAST:event_btnThanhtoanActionPerformed

    private void btnHoadonhomnayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHoadonhomnayActionPerformed
        Hoadonhomnay();
    }//GEN-LAST:event_btnHoadonhomnayActionPerformed

    private void tblChitietSPMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblChitietSPMouseClicked
        chonSP();
    }//GEN-LAST:event_tblChitietSPMouseClicked

    private void tblTracuuMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblTracuuMouseClicked
        tblTracuuClick();
    }//GEN-LAST:event_tblTracuuMouseClicked

    private void btnCapnhatSp4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCapnhatSp4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnCapnhatSp4ActionPerformed

    private void btnTkspQLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTkspQLActionPerformed
        TimKiemSP_QLSP();
        hienthiSizeSP_QLSP();
        hienthiSoluongSizeSP_QLSP();
    }//GEN-LAST:event_btnTkspQLActionPerformed

    private void cboSizespCapnhatItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboSizespCapnhatItemStateChanged
        hienthiSoluongSizeSP_QLSP();
    }//GEN-LAST:event_cboSizespCapnhatItemStateChanged

    private void btnCapnhatSoluongSizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCapnhatSoluongSizeActionPerformed
        int i = capNhatSoLuongSize_QLSP();
        hienthiSizeSP_QLSP();
        hienThiSoLuongSP_TT();
        cboSizespCapnhat.setSelectedIndex(i);
    }//GEN-LAST:event_btnCapnhatSoluongSizeActionPerformed

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
        int i = cboSizespCapnhat.getSelectedIndex();
        boolean x = xoaSize_QLSP();
        if (x == true) {
            hienthiSizeSP_QLSP();
            if (i != 0) {
                cboSizespCapnhat.setSelectedIndex(i - 1);
            }
            hienThiSoLuongSP_TT();
        } else {
            cboSizespCapnhat.setSelectedIndex(i);
        }
    }//GEN-LAST:event_jButton12ActionPerformed

    private void btnThemSizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemSizeActionPerformed
        int i = cboSizespCapnhat.getItemCount();
        boolean x = themSize_QLSP();
        if (x == true) {
            hienthiSizeSP_QLSP();
            if (i != 0) {
                cboSizespCapnhat.setSelectedIndex(i);
            }
            hienThiSoLuongSP_TT();
        } else {
            cboSizespCapnhat.setSelectedIndex(i);
        }
    }//GEN-LAST:event_btnThemSizeActionPerformed

    private void btnInhoadonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInhoadonActionPerformed
        InHoaDon();
    }//GEN-LAST:event_btnInhoadonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTabbedPane Tab;
    private javax.swing.JTabbedPane TabQTV;
    private javax.swing.JButton btnCapnhatSoluongSize;
    private javax.swing.JButton btnCapnhatSp;
    private javax.swing.JButton btnCapnhatSp4;
    private javax.swing.JButton btnCapnhatTV;
    private javax.swing.JButton btnDoitenLoai;
    private javax.swing.JButton btnDoitenNhanhieu;
    private javax.swing.JButton btnHoadonhomnay;
    private javax.swing.JButton btnInhoadon;
    private javax.swing.JButton btnLochoadon;
    private javax.swing.JButton btnThanhtoan;
    private javax.swing.JButton btnThem;
    private javax.swing.JButton btnThemLoai;
    private javax.swing.JButton btnThemSize;
    private javax.swing.JButton btnThemmoi;
    private javax.swing.JButton btnThemnhanhieu;
    private javax.swing.JButton btnTim;
    private javax.swing.JButton btnTimTV;
    private javax.swing.JButton btnTimkiemtracuu;
    private javax.swing.JButton btnTkspQL;
    private javax.swing.JButton btnXoaLoai;
    private javax.swing.JButton btnXoaSp;
    private javax.swing.JButton btnXoaSpTT;
    private javax.swing.JButton btnXoanhanhieu;
    private javax.swing.JComboBox cboChucvuTV;
    private javax.swing.JComboBox cboChucvutvSua;
    private javax.swing.JComboBox cboChucvutvThem;
    private javax.swing.JComboBox cboLoai;
    private javax.swing.JComboBox cboLoaiCapnhat;
    private javax.swing.JComboBox cboLoaiCapnhat4;
    private javax.swing.JComboBox cboNhanhieu;
    private javax.swing.JComboBox cboNhanhieuCapnhat;
    private javax.swing.JComboBox cboNhanhieuCapnhat4;
    private javax.swing.JComboBox cboSizeTT;
    private javax.swing.JComboBox cboSizespCapnhat;
    private javax.swing.JComboBox cboTrangthaiCapnhat;
    private javax.swing.JComboBox cboTrangthaiCapnhat4;
    private javax.swing.JComboBox cboTrangthaiTV;
    private javax.swing.JComboBox cboTrangthaisp;
    private javax.swing.JComboBox cboTrangthaitvSua;
    private javax.swing.JComboBox cboTrangthaitvThem;
    private javax.swing.JCheckBox ckInhoadon;
    private com.toedter.calendar.JDateChooser dcHoadonBatdau;
    private com.toedter.calendar.JDateChooser dcHoadonKetthuc;
    private com.toedter.calendar.JDateChooser dcNgaysinhtvSua;
    private com.toedter.calendar.JDateChooser dcNgaysinhtvThem;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton16;
    private javax.swing.JButton jButton17;
    private javax.swing.JButton jButton18;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private com.toedter.calendar.JCalendar jCalendar1;
    private javax.swing.JComboBox jComboBox11;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel56;
    private javax.swing.JLabel jLabel57;
    private javax.swing.JLabel jLabel58;
    private javax.swing.JLabel jLabel59;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel60;
    private javax.swing.JLabel jLabel61;
    private javax.swing.JLabel jLabel62;
    private javax.swing.JLabel jLabel64;
    private javax.swing.JLabel jLabel65;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel70;
    private javax.swing.JLabel jLabel71;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel80;
    private javax.swing.JLabel jLabel81;
    private javax.swing.JLabel jLabel82;
    private javax.swing.JLabel jLabel83;
    private javax.swing.JLabel jLabel84;
    private javax.swing.JLabel jLabel85;
    private javax.swing.JLabel jLabel86;
    private javax.swing.JLabel jLabel87;
    private javax.swing.JLabel jLabel88;
    private javax.swing.JLabel jLabel89;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabel90;
    private javax.swing.JLabel jLabel92;
    private javax.swing.JLabel jLabel94;
    private javax.swing.JLabel jLabel96;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane13;
    private javax.swing.JScrollPane jScrollPane14;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JSpinner jSpinner2;
    private javax.swing.JSpinner jSpinner4;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JTextField jTextField8;
    private javax.swing.JLabel lblLoai;
    private javax.swing.JLabel lblNhanhieu;
    private javax.swing.JLabel lblSancoTT;
    private javax.swing.JLabel lblTennv;
    private javax.swing.JLabel lblTgsuasize;
    private javax.swing.JLabel lblTgsuasp;
    private javax.swing.JLabel lblTgtaosize;
    private javax.swing.JLabel lblTgtaosp;
    private javax.swing.JLabel lblTonghoadon;
    private javax.swing.JLabel lblTongsohang;
    private javax.swing.JLabel lblTongthu;
    private javax.swing.JLabel lblTongtienTT;
    private javax.swing.JLabel lblTrangthai;
    private javax.swing.JPanel pnlCapNhatSP;
    private javax.swing.JPanel pnlCapNhatSP4;
    private javax.swing.JPanel pnlResult;
    private javax.swing.JPanel pnlShow;
    private javax.swing.JPanel pnlThanhtoan;
    private javax.swing.JPanel pnlTimkiem;
    private javax.swing.JSpinner spnGiaSpCapnhat;
    private javax.swing.JSpinner spnGiaSpCapnhat4;
    private javax.swing.JSpinner spnNhapsizeCapnhat;
    private javax.swing.JSpinner spnNhapsoluongCapnhat;
    private javax.swing.JSpinner spnSoluongSpCapnhat;
    private javax.swing.JSpinner spnSoluongSpTT;
    private javax.swing.JPanel tabBanhang;
    private javax.swing.JPanel tabCaidat;
    private javax.swing.JPanel tabCapnhat;
    private javax.swing.JPanel tabChitiet;
    private javax.swing.JPanel tabLoai;
    private javax.swing.JPanel tabNhanhieu;
    private javax.swing.JTabbedPane tabQLSP;
    private javax.swing.JPanel tabQuanli;
    private javax.swing.JPanel tabSanpham;
    private javax.swing.JTabbedPane tabSetThanhvien;
    private javax.swing.JPanel tabSuaThanhvien;
    private javax.swing.JTabbedPane tabTabChitiet;
    private javax.swing.JPanel tabThem;
    private javax.swing.JPanel tabThemThanhvien;
    private javax.swing.JPanel tabThungan;
    private javax.swing.JPanel tabTracuu;
    private javax.swing.JPanel tabXemhoadon;
    private javax.swing.JTable tblChitietSP;
    private javax.swing.JTable tblChitiethoadon;
    private javax.swing.JTable tblHoadon;
    private javax.swing.JTable tblLoai;
    private javax.swing.JTable tblNhanhieu;
    private javax.swing.JTable tblSizeTracuu;
    private javax.swing.JTable tblTV;
    private javax.swing.JTable tblTracuu;
    private javax.swing.JTextField txtAnhSpCapnhat;
    private javax.swing.JTextField txtDiachitvSua;
    private javax.swing.JTextField txtDiachitvThem;
    private javax.swing.JTextField txtEmailtvSua;
    private javax.swing.JTextField txtEmailtvThem;
    private javax.swing.JTextField txtKetqua;
    private javax.swing.JTextField txtLinkanhSpCapnhat4;
    private javax.swing.JTextField txtMKtvThem;
    private javax.swing.JTextField txtMaspQL;
    private javax.swing.JTextField txtMauSpCapnhat;
    private javax.swing.JTextField txtMauSpCapnhat4;
    private javax.swing.JTextField txtMktvSua;
    private javax.swing.JTextArea txtMotaSpCapnhat;
    private javax.swing.JTextArea txtMotaSpCapnhat4;
    private javax.swing.JTextField txtSdttvSua;
    private javax.swing.JTextField txtSdttvThem;
    private javax.swing.JTextField txtTenLoai;
    private javax.swing.JTextField txtTenLoaiMoi;
    private javax.swing.JTextField txtTenNhanhieumoi;
    private javax.swing.JTextField txtTenSpCapnhat;
    private javax.swing.JTextField txtTenSpCapnhat5;
    private javax.swing.JTextField txtTennhanhhieu;
    private javax.swing.JTextField txtTentvSua;
    private javax.swing.JTextField txtTentvThem;
    private javax.swing.JTextField txtTimkiem;
    private javax.swing.JTextField txtTimkiemTV;
    private javax.swing.JTextField txtTimkiemtracuu;
    private javax.swing.JTextField txtTkspCapnhat;
    // End of variables declaration//GEN-END:variables
}
