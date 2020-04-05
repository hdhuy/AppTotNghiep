package View;

import Controller.DataTransacter;
import Model.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
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
        doDuLieuQlSP("from Product");
        cboNhanhieu();
        cboLoaiSP();
        dodulieuNhanhieu();
        dodulieuLoai();
        doDuLieuBangTV("from User");

        DateFormat fm = new SimpleDateFormat("yyyy-MM-dd");
        Date now = new Date();
        doDuLieuHoaDon("from OrderApp where createdAt like '%" + fm.format(now) + "%'");

        lblTennv.setText(user.getName() + " - Nhân viên thu ngân");
    }
    DataTransacter dt = new DataTransacter();

    private void cboLoaiSP() {
        try {
            List<Category> list = dt.select("from Category");
            cboLoai.removeAllItems();
            cboLoaiqt.removeAllItems();
            cboLoaiCapnhat.removeAllItems();
            cboLoaiThem.removeAllItems();

            cboLoai.addItem("Tất cả");
            cboLoaiqt.addItem("Tất cả");

            cboLoaiCapnhat.setPrototypeDisplayValue(list);
            cboLoaiThem.setPrototypeDisplayValue(list);
            for (int i = 0; i < list.size(); i++) {
                cboLoai.addItem(list.get(i).getName());
                cboLoaiqt.addItem(list.get(i).getName());
                cboLoaiCapnhat.addItem(list.get(i).getName());
                cboLoaiThem.addItem(list.get(i).getName());
            }
        } catch (Exception e) {
            System.out.println("lỗi đổ dữ liệu combobox sp " + e);
        }
    }

    private void cboNhanhieu() {
        try {
            List<Brand> list = dt.select("from Brand");
            cboNhanhieu.removeAllItems();
            cboNhanhieuqt.removeAllItems();
            cboNhanhieuCapnhat.removeAllItems();
            cboNhanhieuThem.removeAllItems();

            cboNhanhieu.addItem("Tất cả");
            cboNhanhieuqt.addItem("Tất cả");

            cboNhanhieuCapnhat.setPrototypeDisplayValue(list);
            cboNhanhieuThem.setPrototypeDisplayValue(list);

            for (int i = 0; i < list.size(); i++) {
                cboNhanhieu.addItem(list.get(i).getName());
                cboNhanhieuqt.addItem(list.get(i).getName());
                cboNhanhieuCapnhat.addItem(list.get(i).getName());
                cboNhanhieuThem.addItem(list.get(i).getName());
            }
        } catch (Exception e) {
            System.out.println("lỗi đổ combox box nhãn hiệu " + e);
        }
    }

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

    private void doDuLieuQlSP(String txt) {
        try {
            List<Product> list = dt.select(txt);
            DefaultTableModel model2 = (DefaultTableModel) tblQlsp.getModel();
            model2.setRowCount(0);
            for (int i = 0; i < list.size(); i++) {
                Product p = list.get(i);
                model2.addRow(new Object[]{i + 1, p.getId(), p.getName(), p.getPrice(), p.getColor(),
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
                    txt += " and name like '%" + txtTimkiemtracuu.getText() + "%'";
                } else {
                    txt += " where name like '%" + txtTimkiemtracuu.getText() + "%'";
                }
            }
        } catch (Exception e) {
            System.out.println("Loc sai: " + e);
        }
        return txt;
    }

    private String scriptQLSP() {
        String txt = "from Product";
        try {
            List<Brand> b = dt.select("from Brand");
            List<Category> c = dt.select("from Category");
            int brandSelectedItem = cboNhanhieuqt.getSelectedIndex() - 1;
            int categorySelectedItem = cboLoaiqt.getSelectedIndex() - 1;
            //loc nhan hieu
            if (brandSelectedItem >= 0) {
                txt += " where brand=" + b.get(brandSelectedItem).getId();
                //cboNhanhieuqt.setPrototypeDisplayValue(b.get(brandSelectedItem));
            }
            //loc loai
            if (categorySelectedItem >= 0) {
                //cboLoaiqt.setPrototypeDisplayValue(c.get(categorySelectedItem));
                if (txt != "from Product") {
                    txt += " and category=" + c.get(categorySelectedItem).getId();
                } else {
                    txt += " where category=" + c.get(categorySelectedItem).getId();
                }
            }
            //trang thai san pham
            if (cboTrangthaispQt.getSelectedIndex() > 0) {
                if (txt != "from Product") {
                    if (cboTrangthaispQt.getSelectedIndex() == 1) {
                        txt += " and isDelete=0";
                    } else {
                        txt += " and isDelete=1";
                    }
                } else {
                    if (cboTrangthaispQt.getSelectedIndex() == 1) {
                        txt += " where isDelete=0";
                    } else {
                        txt += " where isDelete=1";
                    }
                }
            }
            if (txtTimkiemtracuuQt.getText() != null && txtTimkiemtracuuQt.getText().length() > 0) {
                if (txt != "from Product") {
                    txt += " and name like '%" + txtTimkiemtracuuQt.getText() + "%' or id like '%" + txtTimkiemtracuuQt.getText() + "%'";
                } else {
                    txt += " where name like '%" + txtTimkiemtracuuQt.getText() + "% or id like '%" + txtTimkiemtracuuQt.getText() + "%'";
                }
            }
        } catch (Exception e) {
            System.out.println("Loc sai: " + e);
        }
        return txt;
    }
    Product selectedProduct = null;

    private void doThongTinQLSP() {
        try {
            int index = tblQlsp.getSelectedRow();
            String id = (String) tblQlsp.getValueAt(index, 1).toString();
            String txt = "from Product where id=" + id;
            List<Product> list = dt.select(txt);
            if (list.size() > 0) {
                selectedProduct = list.get(0);
                txtTenSpCapnhat.setText(selectedProduct.getName());
                spnGiaSpCapnhat.setValue(selectedProduct.getPrice());
                txtMauSpCapnhat.setText(selectedProduct.getColor());
                txtLinkanhSpCapnhat.setText(selectedProduct.getImage());
                txtMotaSpCapnhat.setText(selectedProduct.getDescription());
            }
        } catch (Exception e) {
            System.out.println("Lỗi đổ dữ liệu tt qlsp" + e);
        }
    }

    private void doComboboxNhanhieuCapnhat() {
        try {
            if (selectedProduct != null) {
                List<Brand> list = dt.select("from Brand");
                for (int i = 0; i < list.size(); i++) {
                    Brand b = selectedProduct.getBrand();
                    if (list.get(i).getId() == b.getId()) {
                        cboNhanhieuCapnhat.setSelectedIndex(i);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("loi doComboboxNhanhieuCapnhat " + e);
        }
    }

    private void doComboboxLoaiCapnhat() {
        try {
            if (selectedProduct != null) {
                List<Category> list = dt.select("from Category");
                for (int i = 0; i < list.size(); i++) {
                    Category b = selectedProduct.getCategory();
                    if (list.get(i).getId() == b.getId()) {
                        cboLoaiCapnhat.setSelectedIndex(i);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("loi doComboboxLoaiCapnhat " + e);
        }
    }

    private void doComboboxTrangthaiCapnhat() {
        try {
            if (selectedProduct != null) {
                if (selectedProduct.isIsDelete()) {
                    cboTrangthaiCapnhat.setSelectedIndex(1);
                } else {
                    cboTrangthaiCapnhat.setSelectedIndex(0);
                }
            }
        } catch (Exception e) {
            System.out.println("loi doComboboxTrangthaiCapnhat " + e);
        }
    }

    private void capnhatSanpham() {
        try {
            if (selectedProduct == null) {
                JOptionPane.showMessageDialog(null, "Chưa có sản phẩm nào được chọn", "Lỗi cập nhật", 0);
            } else {
                Product p = selectedProduct;
                p.setName(txtTenSpCapnhat.getText());
                p.setPrice(new Long(spnGiaSpCapnhat.getValue().toString()));
                p.setColor(txtMauSpCapnhat.getText());
                p.setImage(txtLinkanhSpCapnhat.getText());
                p.setDescription((txtMotaSpCapnhat.getText()));
                int iLoai = cboLoaiCapnhat.getSelectedIndex();
                int iNhanhieu = cboNhanhieuCapnhat.getSelectedIndex();
                int iTrangthai = cboTrangthaiCapnhat.getSelectedIndex();
                List<Brand> b = (List<Brand>) cboNhanhieuCapnhat.getPrototypeDisplayValue();
                List<Category> c = (List<Category>) cboLoaiCapnhat.getPrototypeDisplayValue();
                p.setBrand(b.get(iNhanhieu));
                p.setCategory(c.get(iLoai));
                if (iTrangthai == 0) {
                    p.setIsDelete(false);
                } else {
                    p.setIsDelete(true);
                }
                p.setUpdatedAt(new Date());
                if (JOptionPane.showConfirmDialog(null, "Bạn có muốn thay đổi thông tin của sản phẩm này ?", "Xác nhận cập nhật !", 0) == 0) {
                    if (dt.update(p) == false) {
                        JOptionPane.showMessageDialog(null, "Không thể cập nhật sản phẩm, bản ghi có thể đã tồn tại");
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("lỗi capnhatSanpham " + e);
        }
    }

    private void themSanPham() {
        try {
            Product p = new Product();
            p.setName(txtTenSpThem.getText());
            p.setPrice(new Long(spnGiaSpThem.getValue().toString()));
            p.setColor(txtMauSpThem.getText());
            p.setImage(txtLinkanhSpThem.getText());
            p.setDescription(txtMotaSpThem.getText());
            int iLoai = cboLoaiThem.getSelectedIndex();
            int iNhanhieu = cboNhanhieuThem.getSelectedIndex();
            int iTrangthai = cboTrangthaiThem.getSelectedIndex();
            List<Brand> b = (List<Brand>) cboNhanhieuThem.getPrototypeDisplayValue();
            List<Category> c = (List<Category>) cboLoaiThem.getPrototypeDisplayValue();
            p.setBrand(b.get(iNhanhieu));
            p.setCategory(c.get(iLoai));
            if (iTrangthai == 0) {
                p.setIsDelete(false);
            } else {
                p.setIsDelete(true);
            }
            p.setCreatedAt(new Date());
            //p.setUpdatedAt(new Date());
            if (JOptionPane.showConfirmDialog(null, "Bạn có muốn thêm sản phẩm này ?", "Xác nhận cập nhật !", 0) == 0) {
                if (dt.insert(p) == false) {
                    JOptionPane.showMessageDialog(null, "Không thể thêm sản phẩm, bản ghi có thể đã tồn tại");
                }
            }
        } catch (Exception e) {
            System.out.println("lỗi thêm sp " + e);
        }
    }

    private void XoaSP() {
        try {
            if (selectedProduct != null) {
                if (dt.delete(selectedProduct) == false) {
                    JOptionPane.showMessageDialog(null, "Không thể xóa được vì dữ liệu còn tồn tại trong size và hóa đơn !");
                }
            }
        } catch (Exception e) {
            System.out.println("xao sp");
        }
    }

    private void doComboboxSize() {
        try {
            if (selectedProduct != null) {
                List<ProductSize> list = dt.select("from ProductSize where product=" + selectedProduct.getId());
                cboSize.setPrototypeDisplayValue(list);
                cboSize.removeAllItems();
                for (ProductSize n : list) {
                    cboSize.addItem(n.getSize());
                }
            }
        } catch (Exception e) {
            System.out.println("loi doDuLieuSize " + e);
        }
    }

    private void soLuongSizeSP() {
        try {
            List<ProductSize> list = (List<ProductSize>) cboSize.getPrototypeDisplayValue();
            if (list.size() > 0) {
                int i = cboSize.getSelectedIndex();
                if (i > -1) {
                    spnSoluongSp.setValue(list.get(i).getQuantity());
                }
            }
        } catch (Exception e) {
            System.out.println("loi soluongsp " + e);
        }
    }

    private void capnhatSoluongSizeSP() {
        try {
            List<ProductSize> list = (List<ProductSize>) cboSize.getPrototypeDisplayValue();
            if (list.size() > 0) {
                int i = cboSize.getSelectedIndex();
                if (i > -1) {
                    int sl = (int) spnSoluongSp.getValue();
                    ProductSize p = list.get(i);
                    p.setQuantity(sl);
                    if (JOptionPane.showConfirmDialog(null, "Cập nhật số lượng của size sản phẩm này ?", "Xác nhận cập nhật !", 0) == 0) {
                        if (dt.update(p) == false) {
                            JOptionPane.showMessageDialog(null, "Không thể cập nhật sản phẩm, bản ghi có thể đã tồn tại");
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("loi capnhatSoluongSP " + e);
        }
    }

    private void themSize() {
        try {
            if (selectedProduct != null) {
                ProductSize ps = new ProductSize();
                ps.setSize((int) spnSize.getValue());
                ps.setCreatedAt(new Date());
                ps.setUpdatedAt(new Date());
                ps.setProduct(selectedProduct);
                ps.setQuantity((int) spnSoluongSize.getValue());
                dt.insert(ps);
            }
        } catch (Exception e) {
            System.out.println("loi them size " + e);
        }
    }

    private void xoaSize() {
        try {
            List<ProductSize> list = (List<ProductSize>) cboSize.getPrototypeDisplayValue();
            if (list.size() > 0) {
                int i = cboSize.getSelectedIndex();
                if (i > -1) {
                    ProductSize p = list.get(i);
                    if (dt.delete(p) == false) {
                        JOptionPane.showMessageDialog(null, "Không thể xóa được vì dữ liệu còn tồn tại trong hóa đơn !");
                    }
                }

            }
        } catch (Exception e) {
            System.out.println("loi xoa size " + e);
        }
    }

    //QUẢN LÍ NHÃN HIỆU, LOẠI
    private void dodulieuNhanhieu() {
        try {
            List<Brand> list = dt.select("from Brand");
            int index = 1;
            DefaultTableModel model = (DefaultTableModel) tblNhanhieu.getModel();
            model.setRowCount(0);
            for (Brand n : list) {
                model.addRow(new Object[]{index, n.getId(), n.getName(), n.getCreatedAt(), n.getUpdatedAt()});
                index++;
            }
        } catch (Exception e) {
            System.out.println("loi do du lieu nhan hieu " + e);
        }
    }

    private void dodulieuLoai() {
        try {
            List<Category> list = dt.select("from Category");
            int index = 1;
            DefaultTableModel model = (DefaultTableModel) tblLoai.getModel();
            model.setRowCount(0);
            for (Category n : list) {
                model.addRow(new Object[]{index, n.getId(), n.getName(), n.getCreatedAt(), n.getUpdatedAt()});
                index++;
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
            List<Brand> list = dt.select("from Brand");
            int index = tblNhanhieu.getSelectedRow();
            Brand b = list.get(index);
            if (dt.delete(b) == false) {
                JOptionPane.showMessageDialog(null, "Không thể xóa nhãn hiệu do có sản phẩm đang sử dụng !");
            }
        } catch (Exception e) {
            System.out.println("xoa nhan hieu loi " + e);
        }
    }

    private void doitenNhanhieu() {
        try {
            List<Brand> list = dt.select("from Brand");
            int index = tblNhanhieu.getSelectedRow();
            if (list.size() > 0 && index > -1) {
                Brand b = list.get(index);
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
            List<Category> list = dt.select("from Category");
            int index = tblLoai.getSelectedRow();
            Category b = list.get(index);
            if (dt.delete(b) == false) {
                JOptionPane.showMessageDialog(null, "Không thể xóa loại do có sản phẩm đang sử dụng !");
            }
        } catch (Exception e) {
            System.out.println("xoa nhan hieu loi " + e);
        }
    }

    private void doitenLoai() {
        try {
            List<Category> list = dt.select("from Category");
            int index = tblLoai.getSelectedRow();
            if (list.size() > 0 && index > -1) {
                Category b = list.get(index);
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

    //----------
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
    Product SPdatim = null;
    List<ProductSize> dsSizeSPdatim = new ArrayList();
    List<OrderAppDetail> dsChiTietDonHang = new ArrayList();

    private void timKiemSP(String id) {
        try {
            dsSizeSPdatim = new ArrayList();
            String txt = "from Product where id=" + id;
            List<Product> list = dt.select(txt);
            if (list.size() > 0) {
                String txt2 = "from ProductSize where product=" + list.get(0).getId();
                List<ProductSize> list2 = dt.select(txt2);
                if (list2.size() > 0) {
                    dsSizeSPdatim = list2;
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
            if (SPdatim != null) {
                if (dsSizeSPdatim.size() > 0) {
                    txtKetqua.setText(SPdatim.getName());
                    cboSizeTT.removeAllItems();
                    for (ProductSize e : dsSizeSPdatim) {
                        cboSizeTT.addItem(e.getSize());
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("loi doDuLieuTK " + e);
        }
    }

    private void hienThiSoLuongSP() {
        try {
            if (cboSizeTT.getItemCount() > 0) {
                int index = cboSizeTT.getSelectedIndex();
                if (dsSizeSPdatim.size() > index && index > -1) {
                    int sl = dsSizeSPdatim.get(index).getQuantity();
                    String x = sl + "";
                    lblSancoTT.setText(x);
                }
            }

        } catch (Exception e) {
            System.out.println("loi hienThiSoLuongSP " + e);
        }
    }

    private void themSpTT() {
        try {
            //!spnSoluongSpTT.getValue().toString().equals("0")
            if (SPdatim != null) {
                if (dsSizeSPdatim.size() > 0) {
                    //size *cretae_at *price *quantity *amount
                    OrderAppDetail SPmoi = new OrderAppDetail();

                    int ProductSize_Index = cboSizeTT.getSelectedIndex();
                    ProductSize ps = dsSizeSPdatim.get(ProductSize_Index);
                    SPmoi.setProductSize(ps);
                    SPmoi.setCreatedAt(new Date());
                    SPmoi.setPrice(SPdatim.getPrice());
                    SPmoi.setQuantity((int) spnSoluongSpTT.getValue());
                    int total = (int) (SPmoi.getQuantity() * SPmoi.getPrice());
                    SPmoi.setTotalAmount(total);
                    int SPdaco = 0;
                    boolean isHave = false;
                    for (OrderAppDetail x : dsChiTietDonHang) {
                        if (SPmoi.getProductSize().getId().equals(x.getProductSize().getId())) {
                            isHave = true;
                            break;
                        } else {
                            SPdaco++;
                        }
                    }
                    if (isHave == true) {
                        int quantiOld = dsChiTietDonHang.get(SPdaco).getQuantity();
                        int quantiNew = (int) spnSoluongSpTT.getValue();
                        int quanti = quantiNew + quantiOld;
                        int price = (int) dsChiTietDonHang.get(SPdaco).getPrice();
                        int total2 = quanti * price;
                        //set
                        int quantiLimit = ps.getQuantity();
                        if (quanti <= quantiLimit) {
                            dsChiTietDonHang.get(SPdaco).setQuantity(quanti);
                            dsChiTietDonHang.get(SPdaco).setTotalAmount(total2);
                        } else {
                            JOptionPane.showMessageDialog(null, "Hiện tại chỉ còn: " + quantiLimit, "Số lượng không đủ", 2);
                        }
                    } else {
                        int quantiLimit = ps.getQuantity();
                        int quantiNew = (int) spnSoluongSpTT.getValue();
                        if (quantiNew <= quantiLimit) {
                            dsChiTietDonHang.add(SPmoi);
                        } else {
                            JOptionPane.showMessageDialog(null, "Hiện tại chỉ còn: " + quantiLimit, "Số lượng không đủ", 2);
                        }

                    }

                }
            }
        } catch (Exception e) {
            System.out.println("loi themsp tt " + e);
        }
    }

    private void doDuLieuSPTT() {
        try {
            DefaultTableModel model = (DefaultTableModel) tblChitietSP.getModel();
            model.setRowCount(0);
            if (dsChiTietDonHang.size() > 0) {
                int i = 1;
                int totalOrder = 0;
                int tongsoluonghang = 0;
                for (OrderAppDetail oad : dsChiTietDonHang) {
                    //tim sp
                    totalOrder += oad.getTotalAmount();
                    tongsoluonghang += oad.getQuantity();
                    model.addRow(new Object[]{i, SPdatim.getId(), SPdatim.getName(), oad.getQuantity(),
                        oad.getProductSize().getSize(), oad.getPrice(), oad.getTotalAmount()});
                    i++;
                }
                lblTongtienTT.setText(totalOrder + " đ");
                lblTongtienTT2.setText(totalOrder + " đ");
                lblTongsohang.setText(tongsoluonghang + "");
            } else {
                lblTongtienTT.setText("0đ");
                lblTongtienTT2.setText("0đ");
                lblTongsohang.setText("0");
            }
        } catch (Exception e) {
            System.out.println("loi doDuLieuSPTT " + e);
        }
    }

    private void chonSP() {
        try {
            int index = tblChitietSP.getSelectedRow();
            String id = tblChitietSP.getValueAt(index, 1).toString();
            String size = tblChitietSP.getValueAt(index, 4).toString();
            timKiemSP(id);
            for (int i = 0; i < cboSizeTT.getItemCount(); i++) {
                if (cboSizeTT.getItemAt(i).toString().equals(size)) {
                    cboSizeTT.setSelectedIndex(i);
                }
            }
            hienThiSoLuongSP();
        } catch (Exception e) {
            System.out.println("loi chonSP " + e);
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
            doDuLieuSPTT();
        } catch (Exception e) {
            System.out.println("loi xoaSpTT " + e);
        }
    }

    private String thanhToan_1() {
        String re = null;
        try {
            if (dsChiTietDonHang.size() > 0) {
                OrderApp od = new OrderApp();
                od.setUser(user);
                od.setCreatedAt(new Date());
                DateFormat fm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                re = fm.format(od.getCreatedAt());
                //JOptionPane.showMessageDialog(null,x );
                dt.insert(od);
            }
        } catch (Exception e) {
            System.out.println("loi thanhToan 1" + e);
        }
        return re;
    }

    private void thanhtoan_2(String time) {
        try {
            String txt = "from OrderApp where createdAt like '%" + time + "%'";
            List<OrderApp> list = dt.select(txt);
            if (list.size() > 0) {
                OrderApp oa = list.get(0);
                int total = 0;
                // tong, hoa don chi tiet
                for (OrderAppDetail o : dsChiTietDonHang) {
                    total += o.getTotalAmount();
                    o.setOrderApp(oa);
                    dt.insert(o);
                    ProductSize ps = o.getProductSize();
                    ps.setQuantity(ps.getQuantity() - o.getQuantity());
                    dt.update(ps);
                }
                oa.setTotalAmount(total);
                dt.update(oa);
            }
        } catch (Exception e) {
            System.out.println("loi thanh toan 2" + e);
        }
    }

    private void resetTT() {
        try {
            Product SPdatim = null;
            List<ProductSize> dsSizeSPdatim = new ArrayList();
            List<OrderAppDetail> dsChiTietDonHang = new ArrayList();
            DefaultTableModel model = (DefaultTableModel) tblChitietSP.getModel();
            model.setRowCount(0);

            lblSancoTT.setText("0");
            cboSizeTT.removeAllItems();
            spnSoluongSpTT.setValue(1);
            txtKetqua.setText("");
            lblTongtienTT2.setText("0đ");
            lblTongtienTT.setText("0đ");
            lblTongsohang.setText("0");
        } catch (Exception e) {
            System.out.println("loi resetTT");
        }
    }

    private void InHoaDon() {
        try {

        } catch (Exception e) {
            System.out.println("loi InHoaDon " + e);
        }
    }

    private void capNhatDTB() {
        try {
            String host = txtHost.getText();
            String port = txtPort.getText();
            String dtbn = txtDtbname.getText();
            String userdtb = txtTendangnhapDtb.getText();
            String pass = txtPassDtb.getText();

            Configuration config = new Configuration();
            config.setProperty("hibernate.connection.url", "jdbc:mysql://"+host+":"+port+"/bookstoredb");
            config.setProperty("hibernate.connection.username", userdtb);
            config.setProperty("hibernate.connection.password", pass);
            
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e, "Lỗi cập nhật Database", 2);
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
        jLabel4 = new javax.swing.JLabel();
        lblTongtienTT2 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        lblTongsohang = new javax.swing.JLabel();
        lblTongtienTT = new javax.swing.JLabel();
        btnThanhtoan = new javax.swing.JButton();
        jCheckBox1 = new javax.swing.JCheckBox();
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
        tabQuanli = new javax.swing.JPanel();
        TabQTV = new javax.swing.JTabbedPane();
        tabSanpham = new javax.swing.JPanel();
        jPanel19 = new javax.swing.JPanel();
        jLabel30 = new javax.swing.JLabel();
        cboLoaiqt = new javax.swing.JComboBox();
        jLabel31 = new javax.swing.JLabel();
        cboNhanhieuqt = new javax.swing.JComboBox();
        txtTimkiemtracuuQt = new javax.swing.JTextField();
        btnTimkiemQlSP = new javax.swing.JButton();
        cboTrangthaispQt = new javax.swing.JComboBox();
        jLabel33 = new javax.swing.JLabel();
        jScrollPane12 = new javax.swing.JScrollPane();
        tblQlsp = new javax.swing.JTable();
        tabQLSP = new javax.swing.JTabbedPane();
        tabCapnhat = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        txtTenSpCapnhat = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        txtMauSpCapnhat = new javax.swing.JTextField();
        jLabel35 = new javax.swing.JLabel();
        cboNhanhieuCapnhat = new javax.swing.JComboBox();
        jLabel36 = new javax.swing.JLabel();
        cboLoaiCapnhat = new javax.swing.JComboBox();
        jLabel37 = new javax.swing.JLabel();
        cboTrangthaiCapnhat = new javax.swing.JComboBox();
        jLabel38 = new javax.swing.JLabel();
        jLabel39 = new javax.swing.JLabel();
        txtLinkanhSpCapnhat = new javax.swing.JTextField();
        btnCapnhatSp = new javax.swing.JButton();
        jScrollPane7 = new javax.swing.JScrollPane();
        txtMotaSpCapnhat = new javax.swing.JTextArea();
        spnGiaSpCapnhat = new javax.swing.JSpinner();
        btnXoaSp = new javax.swing.JButton();
        tabThem = new javax.swing.JPanel();
        btnThemSp = new javax.swing.JButton();
        jScrollPane13 = new javax.swing.JScrollPane();
        txtMotaSpThem = new javax.swing.JTextArea();
        jLabel66 = new javax.swing.JLabel();
        txtLinkanhSpThem = new javax.swing.JTextField();
        jLabel76 = new javax.swing.JLabel();
        jLabel77 = new javax.swing.JLabel();
        cboTrangthaiThem = new javax.swing.JComboBox();
        cboLoaiThem = new javax.swing.JComboBox();
        jLabel78 = new javax.swing.JLabel();
        jLabel79 = new javax.swing.JLabel();
        cboNhanhieuThem = new javax.swing.JComboBox();
        txtMauSpThem = new javax.swing.JTextField();
        jLabel80 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        txtTenSpThem = new javax.swing.JTextField();
        jLabel28 = new javax.swing.JLabel();
        spnGiaSpThem = new javax.swing.JSpinner();
        tabSizesoluong = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        jLabel67 = new javax.swing.JLabel();
        cboSize = new javax.swing.JComboBox();
        jLabel68 = new javax.swing.JLabel();
        btnCapnhatSoluongSP = new javax.swing.JButton();
        spnSoluongSp = new javax.swing.JSpinner();
        btnXoaSize = new javax.swing.JButton();
        jPanel14 = new javax.swing.JPanel();
        jLabel69 = new javax.swing.JLabel();
        spnSize = new javax.swing.JSpinner();
        btnThemsize = new javax.swing.JButton();
        jLabel40 = new javax.swing.JLabel();
        jLabel63 = new javax.swing.JLabel();
        spnSoluongSize = new javax.swing.JSpinner();
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
        jLabel21 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jPanel9 = new javax.swing.JPanel();
        dàdssdfsd1 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jLabel26 = new javax.swing.JLabel();
        txtTendangnhapDtb = new javax.swing.JTextField();
        jLabel27 = new javax.swing.JLabel();
        txtPassDtb = new javax.swing.JPasswordField();
        btnCapnhatDtb = new javax.swing.JButton();
        txtHost = new javax.swing.JTextField();
        dàdssdfsd = new javax.swing.JLabel();
        txtPort = new javax.swing.JTextField();
        txtDtbname = new javax.swing.JTextField();
        jPanel11 = new javax.swing.JPanel();
        jTextField1 = new javax.swing.JTextField();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jTextField2 = new javax.swing.JTextField();
        jButton5 = new javax.swing.JButton();
        jTextField3 = new javax.swing.JTextField();
        jButton6 = new javax.swing.JButton();
        jTextField4 = new javax.swing.JTextField();
        jButton7 = new javax.swing.JButton();
        jLabel24 = new javax.swing.JLabel();

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
        jLabel2.setText("Kết quả tìm");

        txtKetqua.setEditable(false);
        txtKetqua.setBackground(new java.awt.Color(255, 255, 255));
        txtKetqua.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N

        btnThem.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnThem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/add_25px.png"))); // NOI18N
        btnThem.setText("Thêm");
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
                            .addComponent(txtKetqua)
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
                                .addComponent(spnSoluongSpTT, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(30, 30, 30)
                .addGroup(pnlTimkiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlTimkiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(btnThem, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnTim, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(btnXoaSpTT, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE))
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
            .addGap(0, 0, Short.MAX_VALUE)
            .addGroup(pnlResultLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 701, Short.MAX_VALUE))
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

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel4.setText("Tổng tiền");

        lblTongtienTT2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblTongtienTT2.setText("0");

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

        jCheckBox1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jCheckBox1.setText("IN HÓA ĐƠN");

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
            .addComponent(lblTennv, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 435, Short.MAX_VALUE)
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
                        .addGroup(pnlThanhtoanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(pnlThanhtoanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblTongtienTT2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblTongsohang, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(pnlThanhtoanLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(pnlThanhtoanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jCheckBox1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnThanhtoan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        pnlThanhtoanLayout.setVerticalGroup(
            pnlThanhtoanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlThanhtoanLayout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(pnlThanhtoanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(lblTongtienTT2))
                .addGap(18, 18, 18)
                .addGroup(pnlThanhtoanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(lblTongsohang))
                .addGap(26, 26, 26)
                .addComponent(lblTongtienTT, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(55, 55, 55)
                .addComponent(jCheckBox1)
                .addGap(18, 18, 18)
                .addComponent(btnThanhtoan, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(214, Short.MAX_VALUE))
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
                    .addComponent(btnHoadonhomnay, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 639, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 670, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 613, Short.MAX_VALUE)
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
                .addContainerGap(406, Short.MAX_VALUE))
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
        jScrollPane8.setViewportView(tblTracuu);
        if (tblTracuu.getColumnModel().getColumnCount() > 0) {
            tblTracuu.getColumnModel().getColumn(0).setMinWidth(30);
            tblTracuu.getColumnModel().getColumn(0).setMaxWidth(50);
        }

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane8)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 597, Short.MAX_VALUE)
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

        jPanel19.setBackground(new java.awt.Color(204, 204, 204));

        jLabel30.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel30.setText("Loại");

        cboLoaiqt.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cboLoaiqt.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tất cả", "Bình thường", "Bị khóa" }));

        jLabel31.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel31.setText("Nhãn hiệu");

        cboNhanhieuqt.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cboNhanhieuqt.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tất cả", "Bình thường", "Bị khóa" }));

        txtTimkiemtracuuQt.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        btnTimkiemQlSP.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnTimkiemQlSP.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/search_16px.png"))); // NOI18N
        btnTimkiemQlSP.setText("Tìm");
        btnTimkiemQlSP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTimkiemQlSPActionPerformed(evt);
            }
        });

        cboTrangthaispQt.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cboTrangthaispQt.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tất cả", "Đang bán", "Tạm ngưng" }));

        jLabel33.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel33.setText("Trạng thái");

        javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
        jPanel19.setLayout(jPanel19Layout);
        jPanel19Layout.setHorizontalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel30)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cboLoaiqt, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel31, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cboNhanhieuqt, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel33, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cboTrangthaispQt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(txtTimkiemtracuuQt, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnTimkiemQlSP)
                .addContainerGap(110, Short.MAX_VALUE))
        );
        jPanel19Layout.setVerticalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel30, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cboLoaiqt)
                    .addComponent(jLabel31, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cboNhanhieuqt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel33, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cboTrangthaispQt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTimkiemtracuuQt, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnTimkiemQlSP, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        tblQlsp.setModel(new javax.swing.table.DefaultTableModel(
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
        tblQlsp.setRowHeight(25);
        tblQlsp.setShowVerticalLines(false);
        tblQlsp.getTableHeader().setReorderingAllowed(false);
        tblQlsp.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblQlspMouseClicked(evt);
            }
        });
        jScrollPane12.setViewportView(tblQlsp);
        if (tblQlsp.getColumnModel().getColumnCount() > 0) {
            tblQlsp.getColumnModel().getColumn(0).setMinWidth(30);
            tblQlsp.getColumnModel().getColumn(0).setMaxWidth(50);
        }

        tabQLSP.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        tabQLSP.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        tabQLSP.setPreferredSize(new java.awt.Dimension(363, 789));

        tabCapnhat.setPreferredSize(new java.awt.Dimension(363, 789));

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel12.setText("Tên");

        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel13.setText("Giá");

        jLabel34.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel34.setText("Màu");

        jLabel35.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel35.setText("Nhãn hiệu");

        cboNhanhieuCapnhat.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cboNhanhieuCapnhat.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel36.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel36.setText("Loại");

        cboLoaiCapnhat.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cboLoaiCapnhat.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel37.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel37.setText("Trạng thái");

        cboTrangthaiCapnhat.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cboTrangthaiCapnhat.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Đang bán", "Tạm ngưng" }));

        jLabel38.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel38.setText("Mô tả");

        jLabel39.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel39.setText("Link ảnh");

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

        txtMotaSpCapnhat.setColumns(20);
        txtMotaSpCapnhat.setRows(5);
        jScrollPane7.setViewportView(txtMotaSpCapnhat);

        btnXoaSp.setBackground(new java.awt.Color(204, 204, 204));
        btnXoaSp.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnXoaSp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/delete_16px.png"))); // NOI18N
        btnXoaSp.setText(" Xóa");
        btnXoaSp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaSpActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout tabCapnhatLayout = new javax.swing.GroupLayout(tabCapnhat);
        tabCapnhat.setLayout(tabCapnhatLayout);
        tabCapnhatLayout.setHorizontalGroup(
            tabCapnhatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tabCapnhatLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(tabCapnhatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel38, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(tabCapnhatLayout.createSequentialGroup()
                        .addGroup(tabCapnhatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(tabCapnhatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jLabel37, javax.swing.GroupLayout.DEFAULT_SIZE, 60, Short.MAX_VALUE)
                                .addComponent(jLabel35, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 60, Short.MAX_VALUE)
                                .addComponent(jLabel34, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 60, Short.MAX_VALUE)
                                .addComponent(jLabel13, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 60, Short.MAX_VALUE)
                                .addComponent(jLabel12, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 60, Short.MAX_VALUE)
                                .addComponent(jLabel36, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 60, Short.MAX_VALUE))
                            .addComponent(jLabel39, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(15, 15, 15)
                        .addGroup(tabCapnhatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtLinkanhSpCapnhat)
                            .addComponent(cboLoaiCapnhat, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtMauSpCapnhat, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(cboNhanhieuCapnhat, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cboTrangthaiCapnhat, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtTenSpCapnhat)
                            .addGroup(tabCapnhatLayout.createSequentialGroup()
                                .addComponent(spnGiaSpCapnhat, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE)))))
                .addGap(10, 10, 10))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tabCapnhatLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane7)
                .addContainerGap())
            .addGroup(tabCapnhatLayout.createSequentialGroup()
                .addGroup(tabCapnhatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(tabCapnhatLayout.createSequentialGroup()
                        .addGap(77, 77, 77)
                        .addComponent(btnCapnhatSp, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(tabCapnhatLayout.createSequentialGroup()
                        .addGap(104, 104, 104)
                        .addComponent(btnXoaSp, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(54, Short.MAX_VALUE))
        );
        tabCapnhatLayout.setVerticalGroup(
            tabCapnhatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tabCapnhatLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(tabCapnhatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTenSpCapnhat, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(tabCapnhatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(spnGiaSpCapnhat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(tabCapnhatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel34, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtMauSpCapnhat, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(tabCapnhatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel35, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboNhanhieuCapnhat, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(tabCapnhatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel36, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboLoaiCapnhat, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(tabCapnhatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel37, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboTrangthaiCapnhat, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(tabCapnhatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel39, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtLinkanhSpCapnhat, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addComponent(jLabel38, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(39, 39, 39)
                .addComponent(btnCapnhatSp, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnXoaSp, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(24, Short.MAX_VALUE))
        );

        tabQLSP.addTab("Cập nhật", new javax.swing.ImageIcon(getClass().getResource("/Images/available_updates_16px.png")), tabCapnhat); // NOI18N

        tabThem.setPreferredSize(new java.awt.Dimension(363, 789));

        btnThemSp.setBackground(new java.awt.Color(255, 51, 51));
        btnThemSp.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnThemSp.setForeground(new java.awt.Color(51, 51, 51));
        btnThemSp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/add_25px.png"))); // NOI18N
        btnThemSp.setText("THÊM");
        btnThemSp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemSpActionPerformed(evt);
            }
        });

        txtMotaSpThem.setColumns(20);
        txtMotaSpThem.setRows(5);
        jScrollPane13.setViewportView(txtMotaSpThem);

        jLabel66.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel66.setText("Mô tả");

        jLabel76.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel76.setText("Link ảnh");

        jLabel77.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel77.setText("Trạng thái");

        cboTrangthaiThem.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cboTrangthaiThem.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Đang bán", "Tạm ngưng" }));

        cboLoaiThem.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cboLoaiThem.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel78.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel78.setText("Loại");

        jLabel79.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel79.setText("Nhãn hiệu");

        cboNhanhieuThem.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cboNhanhieuThem.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel80.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel80.setText("Màu");

        jLabel22.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel22.setText("Giá");

        jLabel28.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel28.setText("Tên");

        javax.swing.GroupLayout tabThemLayout = new javax.swing.GroupLayout(tabThem);
        tabThem.setLayout(tabThemLayout);
        tabThemLayout.setHorizontalGroup(
            tabThemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tabThemLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(tabThemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel66, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(tabThemLayout.createSequentialGroup()
                        .addGroup(tabThemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(tabThemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jLabel77, javax.swing.GroupLayout.DEFAULT_SIZE, 60, Short.MAX_VALUE)
                                .addComponent(jLabel79, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 60, Short.MAX_VALUE)
                                .addComponent(jLabel80, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 60, Short.MAX_VALUE)
                                .addComponent(jLabel22, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 60, Short.MAX_VALUE)
                                .addComponent(jLabel28, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 60, Short.MAX_VALUE)
                                .addComponent(jLabel78, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 60, Short.MAX_VALUE))
                            .addComponent(jLabel76, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(15, 15, 15)
                        .addGroup(tabThemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtLinkanhSpThem)
                            .addComponent(cboLoaiThem, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtMauSpThem, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(cboNhanhieuThem, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cboTrangthaiThem, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtTenSpThem)
                            .addGroup(tabThemLayout.createSequentialGroup()
                                .addComponent(spnGiaSpThem, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE)))))
                .addGap(10, 10, 10))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tabThemLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane13)
                .addContainerGap())
            .addGroup(tabThemLayout.createSequentialGroup()
                .addGap(77, 77, 77)
                .addComponent(btnThemSp, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(54, Short.MAX_VALUE))
        );
        tabThemLayout.setVerticalGroup(
            tabThemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tabThemLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(tabThemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTenSpThem, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(tabThemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(spnGiaSpThem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(tabThemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel80, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtMauSpThem, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(tabThemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel79, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboNhanhieuThem, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(tabThemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel78, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboLoaiThem, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(tabThemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel77, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboTrangthaiThem, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(tabThemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel76, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtLinkanhSpThem, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addComponent(jLabel66, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane13, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(39, 39, 39)
                .addComponent(btnThemSp, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(76, Short.MAX_VALUE))
        );

        tabQLSP.addTab("Thêm", new javax.swing.ImageIcon(getClass().getResource("/Images/plus_16px.png")), tabThem); // NOI18N

        jPanel10.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel67.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel67.setText("Size");

        cboSize.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cboSize.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboSizeItemStateChanged(evt);
            }
        });

        jLabel68.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel68.setText("Số lượng");

        btnCapnhatSoluongSP.setBackground(new java.awt.Color(153, 255, 153));
        btnCapnhatSoluongSP.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnCapnhatSoluongSP.setText("Cập nhật");
        btnCapnhatSoluongSP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCapnhatSoluongSPActionPerformed(evt);
            }
        });

        spnSoluongSp.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        btnXoaSize.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnXoaSize.setText("Xóa size");
        btnXoaSize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaSizeActionPerformed(evt);
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
                        .addGap(32, 32, 32)
                        .addComponent(btnXoaSize, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(btnCapnhatSoluongSP, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel67, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel68, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(29, 29, 29)
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cboSize, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(spnSoluongSp))))
                .addContainerGap())
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel67)
                    .addComponent(cboSize, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 16, Short.MAX_VALUE)
                        .addComponent(spnSoluongSp, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jLabel68, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(18, 18, 18)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnCapnhatSoluongSP, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
                    .addComponent(btnXoaSize, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(12, 12, 12))
        );

        jPanel14.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel69.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel69.setText("Thêm size vào sản phẩm này");

        btnThemsize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/plus_16px.png"))); // NOI18N
        btnThemsize.setText("Thêm");
        btnThemsize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemsizeActionPerformed(evt);
            }
        });

        jLabel40.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel40.setText("Size");

        jLabel63.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel63.setText("Số lượng");

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel69, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addComponent(jLabel40)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(spnSize, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel63)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(spnSoluongSize, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnThemsize, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 1, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel69)
                .addGap(18, 18, 18)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(spnSize, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel40)
                    .addComponent(jLabel63)
                    .addComponent(spnSoluongSize, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnThemsize, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(39, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout tabSizesoluongLayout = new javax.swing.GroupLayout(tabSizesoluong);
        tabSizesoluong.setLayout(tabSizesoluongLayout);
        tabSizesoluongLayout.setHorizontalGroup(
            tabSizesoluongLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel10, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        tabSizesoluongLayout.setVerticalGroup(
            tabSizesoluongLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tabSizesoluongLayout.createSequentialGroup()
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(308, Short.MAX_VALUE))
        );

        tabQLSP.addTab("Size và số lượng", new javax.swing.ImageIcon(getClass().getResource("/Images/enlarge_16px.png")), tabSizesoluong); // NOI18N

        javax.swing.GroupLayout tabSanphamLayout = new javax.swing.GroupLayout(tabSanpham);
        tabSanpham.setLayout(tabSanphamLayout);
        tabSanphamLayout.setHorizontalGroup(
            tabSanphamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tabSanphamLayout.createSequentialGroup()
                .addGroup(tabSanphamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane12))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tabQLSP, javax.swing.GroupLayout.DEFAULT_SIZE, 354, Short.MAX_VALUE))
        );
        tabSanphamLayout.setVerticalGroup(
            tabSanphamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(tabSanphamLayout.createSequentialGroup()
                .addComponent(jPanel19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane12, javax.swing.GroupLayout.PREFERRED_SIZE, 578, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(tabQLSP, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
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
                .addContainerGap(384, Short.MAX_VALUE))
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
                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 552, Short.MAX_VALUE))
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
                .addContainerGap(436, Short.MAX_VALUE))
            .addComponent(jScrollPane6, javax.swing.GroupLayout.Alignment.TRAILING)
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
                .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 552, Short.MAX_VALUE))
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

        TabQTV.addTab("Chi tiết", new javax.swing.ImageIcon(getClass().getResource("/Images/detail_16px.png")), tabChitiet); // NOI18N

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
                .addContainerGap(56, Short.MAX_VALUE))
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
                .addContainerGap(56, Short.MAX_VALUE))
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
                .addContainerGap(231, Short.MAX_VALUE))
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

        jLabel21.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel21.setText("Cơ sở dữ liệu");

        jButton1.setBackground(new java.awt.Color(255, 0, 0));
        jButton1.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jButton1.setText("Đăng xuất");

        dàdssdfsd1.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        dàdssdfsd1.setText("Port");

        jLabel23.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel23.setText("Database");

        jButton2.setBackground(new java.awt.Color(51, 255, 204));
        jButton2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jButton2.setText("Kiểm tra kết nối");

        jLabel26.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel26.setText("Tên đăng nhập");

        txtTendangnhapDtb.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        jLabel27.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel27.setText("Mật khẩu");

        btnCapnhatDtb.setBackground(new java.awt.Color(255, 51, 51));
        btnCapnhatDtb.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnCapnhatDtb.setText("Cập nhật");
        btnCapnhatDtb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCapnhatDtbActionPerformed(evt);
            }
        });

        txtHost.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        dàdssdfsd.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        dàdssdfsd.setText("Host");

        txtPort.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        txtDtbname.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(dàdssdfsd, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel27, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel26, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(40, 40, 40)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtPassDtb, javax.swing.GroupLayout.PREFERRED_SIZE, 480, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(txtTendangnhapDtb, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(txtDtbname, javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel9Layout.createSequentialGroup()
                            .addComponent(txtHost, javax.swing.GroupLayout.PREFERRED_SIZE, 294, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(dàdssdfsd1)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(txtPort, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnCapnhatDtb, javax.swing.GroupLayout.PREFERRED_SIZE, 218, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtHost, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dàdssdfsd, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtPort, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dàdssdfsd1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(21, 21, 21)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtDtbname, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(43, 43, 43)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTendangnhapDtb, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtPassDtb, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(47, 47, 47)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCapnhatDtb, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(31, Short.MAX_VALUE))
        );

        jButton3.setText("Slide 1");

        jButton4.setText("Slide 2");

        jButton5.setText("Slide 3");

        jButton6.setText("Slide 4");

        jButton7.setBackground(new java.awt.Color(153, 255, 153));
        jButton7.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButton7.setText("Lưu");

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel11Layout.createSequentialGroup()
                                .addComponent(jTextField3)
                                .addGap(18, 18, 18)
                                .addComponent(jButton5))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                                .addComponent(jTextField2)
                                .addGap(18, 18, 18)
                                .addComponent(jButton4))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                                .addComponent(jTextField1)
                                .addGap(18, 18, 18)
                                .addComponent(jButton3))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                                .addComponent(jTextField4)
                                .addGap(18, 18, 18)
                                .addComponent(jButton6)))))
                .addGap(37, 37, 37))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(28, 28, 28)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(28, 28, 28)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(38, 38, 38)
                .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel24.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel24.setText("Cơ sở dữ liệu");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 335, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 335, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 86, Short.MAX_VALUE)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18))))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel21)
                            .addComponent(jLabel24)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(229, Short.MAX_VALUE))
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
            .addComponent(Tab, javax.swing.GroupLayout.DEFAULT_SIZE, 717, Short.MAX_VALUE)
        );

        Tab.getAccessibleContext().setAccessibleDescription("");

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnTimkiemtracuuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTimkiemtracuuActionPerformed
        doDuLieuTraCuu(scriptTraCuuSP());
    }//GEN-LAST:event_btnTimkiemtracuuActionPerformed

    private void btnTimkiemQlSPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTimkiemQlSPActionPerformed

        doDuLieuQlSP(scriptQLSP());
    }//GEN-LAST:event_btnTimkiemQlSPActionPerformed

    private void tblQlspMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblQlspMouseClicked
        spnSoluongSp.setValue(0);
        doThongTinQLSP();
        doComboboxLoaiCapnhat();
        doComboboxNhanhieuCapnhat();
        doComboboxTrangthaiCapnhat();
        doComboboxSize();
    }//GEN-LAST:event_tblQlspMouseClicked

    private void btnCapnhatSpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCapnhatSpActionPerformed
        capnhatSanpham();
        doDuLieuQlSP(scriptQLSP());
        doDuLieuTraCuu(scriptTraCuuSP());
    }//GEN-LAST:event_btnCapnhatSpActionPerformed

    private void btnThemSpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemSpActionPerformed
        themSanPham();
        doDuLieuQlSP(scriptQLSP());
        doDuLieuTraCuu(scriptTraCuuSP());
    }//GEN-LAST:event_btnThemSpActionPerformed

    private void cboSizeItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboSizeItemStateChanged
        soLuongSizeSP();
    }//GEN-LAST:event_cboSizeItemStateChanged

    private void btnCapnhatSoluongSPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCapnhatSoluongSPActionPerformed
        capnhatSoluongSizeSP();
        doDuLieuQlSP(scriptQLSP());
        doDuLieuTraCuu(scriptTraCuuSP());
    }//GEN-LAST:event_btnCapnhatSoluongSPActionPerformed

    private void btnXoaSpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaSpActionPerformed
        XoaSP();
        doDuLieuQlSP(scriptQLSP());
        doDuLieuTraCuu(scriptTraCuuSP());
    }//GEN-LAST:event_btnXoaSpActionPerformed

    private void btnXoaSizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaSizeActionPerformed
        xoaSize();
        doComboboxSize();
    }//GEN-LAST:event_btnXoaSizeActionPerformed

    private void btnThemsizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemsizeActionPerformed
        themSize();
        doComboboxSize();
    }//GEN-LAST:event_btnThemsizeActionPerformed

    private void btnThemnhanhieuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemnhanhieuActionPerformed
        themNhanhieu();
        dodulieuNhanhieu();
        cboNhanhieu();
    }//GEN-LAST:event_btnThemnhanhieuActionPerformed

    private void btnXoanhanhieuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoanhanhieuActionPerformed
        xoaNhanhieu();
        dodulieuNhanhieu();
        cboNhanhieu();
    }//GEN-LAST:event_btnXoanhanhieuActionPerformed

    private void btnDoitenLoaiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDoitenLoaiActionPerformed
        doitenLoai();
        dodulieuLoai();
        cboLoaiSP();
    }//GEN-LAST:event_btnDoitenLoaiActionPerformed

    private void btnDoitenNhanhieuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDoitenNhanhieuActionPerformed
        doitenNhanhieu();
        dodulieuNhanhieu();
        cboNhanhieu();
    }//GEN-LAST:event_btnDoitenNhanhieuActionPerformed

    private void btnThemLoaiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemLoaiActionPerformed
        themLoai();
        dodulieuLoai();
        cboLoaiSP();
    }//GEN-LAST:event_btnThemLoaiActionPerformed

    private void btnXoaLoaiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaLoaiActionPerformed
        xoaLoai();
        dodulieuLoai();
        cboLoaiSP();
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
    }//GEN-LAST:event_btnTimActionPerformed

    private void btnThemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemActionPerformed
        themSpTT();
        doDuLieuSPTT();
    }//GEN-LAST:event_btnThemActionPerformed

    private void cboSizeTTItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboSizeTTItemStateChanged
        //System.out.println(cboSizeTT.getSelectedItem().toString());
        hienThiSoLuongSP();
    }//GEN-LAST:event_cboSizeTTItemStateChanged

    private void btnXoaSpTTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaSpTTActionPerformed
        xoaSpTT();
    }//GEN-LAST:event_btnXoaSpTTActionPerformed

    private void btnThanhtoanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThanhtoanActionPerformed
        if (dsChiTietDonHang.size() > 0) {
            if (JOptionPane.showConfirmDialog(null, "Xác nhận thanh toán", "Thanh toán", 0) == 0) {
                thanhtoan_2(thanhToan_1());
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

    private void btnCapnhatDtbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCapnhatDtbActionPerformed
        capNhatDTB();
    }//GEN-LAST:event_btnCapnhatDtbActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTabbedPane Tab;
    private javax.swing.JTabbedPane TabQTV;
    private javax.swing.JButton btnCapnhatDtb;
    private javax.swing.JButton btnCapnhatSoluongSP;
    private javax.swing.JButton btnCapnhatSp;
    private javax.swing.JButton btnCapnhatTV;
    private javax.swing.JButton btnDoitenLoai;
    private javax.swing.JButton btnDoitenNhanhieu;
    private javax.swing.JButton btnHoadonhomnay;
    private javax.swing.JButton btnLochoadon;
    private javax.swing.JButton btnThanhtoan;
    private javax.swing.JButton btnThem;
    private javax.swing.JButton btnThemLoai;
    private javax.swing.JButton btnThemSp;
    private javax.swing.JButton btnThemmoi;
    private javax.swing.JButton btnThemnhanhieu;
    private javax.swing.JButton btnThemsize;
    private javax.swing.JButton btnTim;
    private javax.swing.JButton btnTimTV;
    private javax.swing.JButton btnTimkiemQlSP;
    private javax.swing.JButton btnTimkiemtracuu;
    private javax.swing.JButton btnXoaLoai;
    private javax.swing.JButton btnXoaSize;
    private javax.swing.JButton btnXoaSp;
    private javax.swing.JButton btnXoaSpTT;
    private javax.swing.JButton btnXoanhanhieu;
    private javax.swing.JComboBox cboChucvuTV;
    private javax.swing.JComboBox cboChucvutvSua;
    private javax.swing.JComboBox cboChucvutvThem;
    private javax.swing.JComboBox cboLoai;
    private javax.swing.JComboBox cboLoaiCapnhat;
    private javax.swing.JComboBox cboLoaiThem;
    private javax.swing.JComboBox cboLoaiqt;
    private javax.swing.JComboBox cboNhanhieu;
    private javax.swing.JComboBox cboNhanhieuCapnhat;
    private javax.swing.JComboBox cboNhanhieuThem;
    private javax.swing.JComboBox cboNhanhieuqt;
    private javax.swing.JComboBox cboSize;
    private javax.swing.JComboBox cboSizeTT;
    private javax.swing.JComboBox cboTrangthaiCapnhat;
    private javax.swing.JComboBox cboTrangthaiTV;
    private javax.swing.JComboBox cboTrangthaiThem;
    private javax.swing.JComboBox cboTrangthaisp;
    private javax.swing.JComboBox cboTrangthaispQt;
    private javax.swing.JComboBox cboTrangthaitvSua;
    private javax.swing.JComboBox cboTrangthaitvThem;
    private com.toedter.calendar.JDateChooser dcHoadonBatdau;
    private com.toedter.calendar.JDateChooser dcHoadonKetthuc;
    private com.toedter.calendar.JDateChooser dcNgaysinhtvSua;
    private com.toedter.calendar.JDateChooser dcNgaysinhtvThem;
    private javax.swing.JLabel dàdssdfsd;
    private javax.swing.JLabel dàdssdfsd1;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private com.toedter.calendar.JCalendar jCalendar1;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JComboBox jComboBox11;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
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
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
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
    private javax.swing.JLabel jLabel63;
    private javax.swing.JLabel jLabel64;
    private javax.swing.JLabel jLabel65;
    private javax.swing.JLabel jLabel66;
    private javax.swing.JLabel jLabel67;
    private javax.swing.JLabel jLabel68;
    private javax.swing.JLabel jLabel69;
    private javax.swing.JLabel jLabel70;
    private javax.swing.JLabel jLabel71;
    private javax.swing.JLabel jLabel76;
    private javax.swing.JLabel jLabel77;
    private javax.swing.JLabel jLabel78;
    private javax.swing.JLabel jLabel79;
    private javax.swing.JLabel jLabel80;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane12;
    private javax.swing.JScrollPane jScrollPane13;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JLabel lblSancoTT;
    private javax.swing.JLabel lblTennv;
    private javax.swing.JLabel lblTonghoadon;
    private javax.swing.JLabel lblTongsohang;
    private javax.swing.JLabel lblTongthu;
    private javax.swing.JLabel lblTongtienTT;
    private javax.swing.JLabel lblTongtienTT2;
    private javax.swing.JPanel pnlResult;
    private javax.swing.JPanel pnlShow;
    private javax.swing.JPanel pnlThanhtoan;
    private javax.swing.JPanel pnlTimkiem;
    private javax.swing.JSpinner spnGiaSpCapnhat;
    private javax.swing.JSpinner spnGiaSpThem;
    private javax.swing.JSpinner spnSize;
    private javax.swing.JSpinner spnSoluongSize;
    private javax.swing.JSpinner spnSoluongSp;
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
    private javax.swing.JPanel tabSizesoluong;
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
    private javax.swing.JTable tblQlsp;
    private javax.swing.JTable tblTV;
    private javax.swing.JTable tblTracuu;
    private javax.swing.JTextField txtDiachitvSua;
    private javax.swing.JTextField txtDiachitvThem;
    private javax.swing.JTextField txtDtbname;
    private javax.swing.JTextField txtEmailtvSua;
    private javax.swing.JTextField txtEmailtvThem;
    private javax.swing.JTextField txtHost;
    private javax.swing.JTextField txtKetqua;
    private javax.swing.JTextField txtLinkanhSpCapnhat;
    private javax.swing.JTextField txtLinkanhSpThem;
    private javax.swing.JTextField txtMKtvThem;
    private javax.swing.JTextField txtMauSpCapnhat;
    private javax.swing.JTextField txtMauSpThem;
    private javax.swing.JTextField txtMktvSua;
    private javax.swing.JTextArea txtMotaSpCapnhat;
    private javax.swing.JTextArea txtMotaSpThem;
    private javax.swing.JPasswordField txtPassDtb;
    private javax.swing.JTextField txtPort;
    private javax.swing.JTextField txtSdttvSua;
    private javax.swing.JTextField txtSdttvThem;
    private javax.swing.JTextField txtTenLoai;
    private javax.swing.JTextField txtTenLoaiMoi;
    private javax.swing.JTextField txtTenNhanhieumoi;
    private javax.swing.JTextField txtTenSpCapnhat;
    private javax.swing.JTextField txtTenSpThem;
    private javax.swing.JTextField txtTendangnhapDtb;
    private javax.swing.JTextField txtTennhanhhieu;
    private javax.swing.JTextField txtTentvSua;
    private javax.swing.JTextField txtTentvThem;
    private javax.swing.JTextField txtTimkiem;
    private javax.swing.JTextField txtTimkiemTV;
    private javax.swing.JTextField txtTimkiemtracuu;
    private javax.swing.JTextField txtTimkiemtracuuQt;
    // End of variables declaration//GEN-END:variables
}
