package View;

import Pojos.Brand;
import Pojos.User;
import Pojos.OrderApp;
import Pojos.Product;
import Pojos.ProductSize;
import Pojos.Category;
import Pojos.OrderAppDetail;
import Controller.Config;
import Controller.Converter;
import Controller.DataTransacter;
import Controller.QL_HoaDon;
import Controller.QL_SanPham;
import Controller.QL_ThanhToan;
import Controller.QL_ThanhVien;
import Controller.QL_ThemSP;
import Controller.TblChitietTracuu_Color;
import Controller.TblTracuu_Color;
import Model.thongke;
import Pojos.OrderWeb;
import Pojos.OrderWebDetail;
import Pojos.ProductColor;
import Pojos.Color;
import Pojos.ProductImage;
import java.awt.image.BufferedImage;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import javax.swing.table.DefaultTableModel;

public class Working extends javax.swing.JFrame {

    User user = new User();
    Login login;

    public Working(User user, Login login) {
        initComponents();
        this.user = user;
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        comboBox_tblMauNhanAnh();
        bangTraCuu();
        doDuLieuBangTV();
        doDuLieuHoaDon_TracuuHD();
        lblTennv.setText(user.getName() + " - Nhân viên thu ngân");
        hienthiCuahang();
        hienthiSlide();
        resetGiaodien_Thanhtoan();
        doDuLieuHoaDon_Thongke();
        antab();
        chonLoaiHoaDon_Thongke(true);
        doDuLieuHoaDon_Thongke();
    }

    private void antab() {
        try {
            if (user.getRole().equals("ROLE_APP_STAFF")) {
                Tab.remove(tabCaidat);
                Tab.remove(tabQuanli);
            }
        } catch (Exception e) {
            System.out.println("loix hân quyền ẩn ta " + e);
        }
    }

    private String formatText(int num) {
        String re = "--";
        try {
            String pattern = "###,###.###";
            DecimalFormat decimalFormat = new DecimalFormat(pattern);
            re = decimalFormat.format(num);
        } catch (Exception e) {
            System.out.println("loi format text " + e);
        }
        return re;
    }
    Product sptt = null;
    List<OrderAppDetail> dstt = new ArrayList();
    //<editor-fold defaultstate="collapsed" desc="BÁN HÀNG - THANH TOÁN">

    private void timKiemSP_ThanhToan(String id) {
        try {
            QL_ThanhToan tt = new QL_ThanhToan();
            cboSizeTT.removeAllItems();
            txtKetqua.setText("");
            Product p = tt.timKiem(id);
            if (p != null) {
                txtKetqua.setText(p.getName());
                Converter cv = new Converter();
                List<ProductSize> lps = cv.SetToList(p.getProductSizes());
                for (ProductSize e : lps) {
                    cboSizeTT.addItem(e.getSize());
                }
                sptt = p;
            } else {
                System.out.println("Không tìm thấy sản phẩm !");
            }

        } catch (Exception e) {
            System.out.println("loi tim kiem " + e);
        }
    }

    private void hienThiSoLuongSP_ThanhToan() {
        try {
            lblSancoTT.setText("--");
            int i = cboSizeTT.getSelectedIndex();
            if (i > -1 && sptt != null) {
                Converter cv = new Converter();
                List<ProductSize> lps = cv.SetToList(sptt.getProductSizes());
                int sl = lps.get(i).getQuantity();
                lblSancoTT.setText(sl + " đôi");
            }
        } catch (Exception e) {
            System.out.println("loi hienThiSoLuongSP " + e);
        }
    }

    private void themChiTietDonHang_ThanhToan() {
        try {
            if (sptt != null) {
                QL_ThanhToan tt = new QL_ThanhToan();
                int sizeIndex = cboSizeTT.getSelectedIndex();
                System.out.println("size=" + sptt.getProductSizes().size());
                Converter cv = new Converter();
                List<ProductSize> lps = cv.SetToList(sptt.getProductSizes());
                int soluonghientai = lps.get(sizeIndex).getQuantity();
                int soluongmuonmua = (int) spnSoluongSpTT.getValue();
                if (soluongmuonmua <= soluonghientai) {
                    ProductSize ps = lps.get(cboSizeTT.getSelectedIndex());
                    ps.setProduct(sptt);
                    OrderAppDetail oad = new OrderAppDetail();
                    oad.setProductSize(ps);
                    oad.setCreatedAt(new Date());
                    oad.setPrice(sptt.getPrice());
                    oad.setQuantity((int) spnSoluongSpTT.getValue());
                    oad.setTotalAmount(oad.getPrice() * oad.getQuantity());
                    dstt = tt.themChiTiet_Thanhtoan(oad, dstt);
                    //ĐỔ DỮ LIỆU CHI TIÊT ĐƠN HÀNG
                    doDuLieuChitietDonHang_Thanhtoan();
                } else {
                    JOptionPane.showMessageDialog(null, "Hiện tại chỉ còn " + soluonghientai + " đôi !", "Số lượng hàng không đủ", 2);
                }

            }
        } catch (Exception e) {
            System.out.println("loi capNhatChiTietDonHang_ThanhToan " + e);
        }
    }

    private void doDuLieuChitietDonHang_Thanhtoan() {
        try {
            DefaultTableModel model = (DefaultTableModel) tblChitietSP.getModel();
            model.setRowCount(0);
            lblTongtienTT.setText("--");
            lblTongsohang.setText("--");
            int i = 1;
            int tongtien = 0;
            int tonghang = 0;
            for (OrderAppDetail e : dstt) {
                model.addRow(new Object[]{i, e.getProductSize().getProduct().getId(),
                    e.getProductSize().getProduct().getName(),
                    e.getQuantity(),
                    e.getProductSize().getSize(),
                    formatText((int) e.getPrice()),
                    formatText((int) e.getTotalAmount())
                });
                tongtien += e.getTotalAmount();
                tonghang += e.getQuantity();
                String tongtienFormat = formatText(tongtien);
                lblTongtienTT.setText(tongtienFormat + " đ");
                lblTongsohang.setText(tonghang + " đôi");
                i++;
            }
        } catch (Exception e) {
            System.out.println("loi doDuLieuChitietSP_Thanhtoan " + e);
        }
    }

    private void xoaSpTT_ThanhToan() {
        try {
            int i = tblChitietSP.getSelectedRow();
            if (i > -1) {
                dstt.remove(i);
            } else {
                System.out.println("hàng được chọn = " + i);
            }
        } catch (Exception e) {
            System.out.println("loi xoaSpTT " + e);
        }
    }

    private void chonSP_ThanhToan() {
        try {
            int row = tblChitietSP.getSelectedRow();
            String idsp = tblChitietSP.getValueAt(row, 1).toString();
            timKiemSP_ThanhToan(idsp);
            Long idSize = dstt.get(row).getProductSize().getId();
            QL_ThanhToan tt = new QL_ThanhToan();
            Converter cv = new Converter();
            List<ProductSize> lps = cv.SetToList(sptt.getProductSizes());
            int i = 0;
            for (ProductSize e : lps) {
                if (e.getId().equals(idSize)) {
                    cboSizeTT.setSelectedIndex(i);
                    spnSoluongSpTT.setValue(dstt.get(row).getQuantity());
                    break;
                }
                i++;
            }
            hienThiSoLuongSP_ThanhToan();
        } catch (Exception e) {
            System.out.println("loi chonSP " + e);
        }
    }

    private void thanhToan() {
        try {
            QL_ThanhToan tt = new QL_ThanhToan();
            if (dstt.size() > 0) {
                if (JOptionPane.showConfirmDialog(null, "Thanh toán hóa đơn này ?", "Xác nhận thanh toán !", 0) == 0) {
                    btnThanhtoan.enable(false);
                    tt.thanhToan(user, dstt, ckInhoadon.isSelected());
                    dstt = null;
                    dstt = new ArrayList();
                    resetGiaodien_Thanhtoan();
                    btnThanhtoan.enable(true);
                }
            }
        } catch (Exception e) {
            System.out.println("loi thanhToan 1" + e);
        }
    }

    private void resetGiaodien_Thanhtoan() {
        try {
            doDuLieuChitietDonHang_Thanhtoan();
            doDuLieuHoaDon_TracuuHD();
            bangTraCuu();
            lblTongtienTT.setText("--");
            lblTongsohang.setText("--");
        } catch (Exception e) {
            System.out.println("loi resetTT");
        }
    }
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="XEM HÓA ĐƠN">

    private void doDuLieuHoaDon_TracuuHD() {
        try {
            lblTongthu_Xemhoadon.setText("--");
            lblTonghoadon.setText("--");
            String txt2 = "from OrderApp where user=" + user.getId() + " and status='Chưa giao tiền'";
            QL_HoaDon qlhd = new QL_HoaDon();
            List<OrderApp> oa = qlhd.layHoaDon(txt2);
            DefaultTableModel model = (DefaultTableModel) tblHoadon_Xemhoadon.getModel();
            model.setRowCount(0);
            int i = 1;
            int count = 0;
            for (OrderApp n : oa) {
                model.addRow(new Object[]{i, n.getId(), formatText((int) n.getTotalAmount()), n.getCreatedAt()});
                i++;
                count += n.getTotalAmount();
            }
            lblTongthu_Xemhoadon.setText(formatText(count) + " đ");
            lblTonghoadon.setText(formatText(tblHoadon_Xemhoadon.getRowCount()) + " đơn");
            chonHoaDon_TracuuHD();
        } catch (Exception e) {
            System.out.println("loi doDuLieuHoaDon " + e);
        }
    }

    private void timKiemHoaDon() {
        try {
            lblTongthu_Xemhoadon.setText("--");
            lblTonghoadon.setText("--");
            String txt2 = "from OrderApp where id=" + txtMahoadon_Xemhoadon.getText();
            QL_HoaDon qlhd = new QL_HoaDon();
            List<OrderApp> oa = qlhd.layHoaDon(txt2);
            DefaultTableModel model = (DefaultTableModel) tblHoadon_Xemhoadon.getModel();
            model.setRowCount(0);
            int i = 1;
            int count = 0;
            for (OrderApp n : oa) {
                model.addRow(new Object[]{i, n.getId(), formatText((int) n.getTotalAmount()), n.getCreatedAt()});
                i++;
                count += n.getTotalAmount();
            }
            lblTongthu_Xemhoadon.setText(count + " đ");
            lblTonghoadon.setText(tblHoadon_Xemhoadon.getRowCount() + " đơn");
            chonHoaDon_TracuuHD();
        } catch (Exception e) {
            System.out.println("loi timKiemHoaDon " + e);
        }
    }

    private void chonHoaDon_TracuuHD() {
        try {
            int si = tblHoadon_Xemhoadon.getSelectedRow();
            DefaultTableModel model = (DefaultTableModel) tblChitiethoadon.getModel();
            model.setRowCount(0);
            lblTongtienHD_Xemhd.setText("--");
            if (si > -1) {
                String hdid = tblHoadon_Xemhoadon.getValueAt(si, 1).toString();
                QL_HoaDon qlhd = new QL_HoaDon();
                List<OrderAppDetail> oad = qlhd.layChiTietHoaDonApp(hdid);
                if (oad.size() > 0) {
                    int i = 1;
                    for (OrderAppDetail e : oad) {
                        model.addRow(new Object[]{i, e.getProductSize().getProduct().getId(),
                            e.getProductSize().getProduct().getName(),
                            e.getQuantity(), formatText((int) e.getPrice()),
                            e.getProductSize().getSize(), formatText((int) e.getTotalAmount())});
                        i++;
                    }
                }
                List<OrderApp> load = qlhd.layHoaDon("from OrderApp where id=" + hdid);
                if (load.size() > 0) {
                    lblTongtienHD_Xemhd.setText(formatText((int) load.get(0).getTotalAmount()) + " đ");
                }
            }
        } catch (Exception e) {
            System.out.println("loi chonHoaDon " + e);
        }
    }

    private void inHoaDon_TracuuHD() {
        try {
            QL_HoaDon qlhd = new QL_HoaDon();
            int si = tblHoadon_Xemhoadon.getSelectedRow();
            String hdid = tblHoadon_Xemhoadon.getValueAt(si, 1).toString();
            List<OrderApp> loa = qlhd.layHoaDon("from OrderApp where id=" + hdid);
            if (loa.size() > 0) {
                List<OrderAppDetail> load = qlhd.layChiTietHoaDonApp(hdid);
                qlhd.inHoaDon(load, loa.get(0));
            }
        } catch (Exception e) {
            System.out.println("loi in hoa don " + e);
        }
    }

    private void banGiaoTien() {
        try {
            if (JOptionPane.showConfirmDialog(null, "Bàn giao các hóa đơn này ?", "Xác nhận bàn giao !", 0) == 0) {
                String txt2 = "from OrderApp where user=" + user.getId() + " and status='Chưa giao tiền'";
                QL_HoaDon qlhd = new QL_HoaDon();
                qlhd.banGiaoTien(txt2);
                doDuLieuHoaDon_TracuuHD();
            }
        } catch (Exception e) {
            System.out.println("loi ban giao tien " + e);
        }
    }
//</editor-fold>
    QL_SanPham qlspTracuu = new QL_SanPham();

    //<editor-fold defaultstate="collapsed" desc="TRA CỨU SẢN PHẨM">
    private void comboBox_tblMauNhanAnh() {
        try {
            qlspTracuu = new QL_SanPham();
            qlspTracuu.layNhanhieu_Loai_Mau();

            cboLoai_Tracuu.removeAllItems();
            cboNhanhieu_Tracuu.removeAllItems();
            cboMau_Tracuu.removeAllItems();
            cboNhanhieu_Tracuu.addItem("Tất cả");
            cboLoai_Tracuu.addItem("Tất cả");

            cboMau_Tracuu.addItem("Tất cả");
            //đổ dữ liệu chung cập nhật sp
            cboLoai_Capnhat.removeAllItems();
            cboNhanhieu_Capnhat.removeAllItems();
            cboMau_Capnhat.removeAllItems();
            //thêm sp
            cboMau_Themsp.removeAllItems();
            cboNhanhieu_Themsp.removeAllItems();
            cboLoai_Themsp.removeAllItems();
            for (Brand e : qlspTracuu.getListBrand()) {
                cboNhanhieu_Tracuu.addItem(e.getName());
                cboNhanhieu_Capnhat.addItem(e.getName());
                cboNhanhieu_Themsp.addItem(e.getName());
            }
            dodulieuNhanhieu_nhlm(qlspTracuu.getListBrand());
            for (Category e : qlspTracuu.getListCategory()) {
                cboLoai_Tracuu.addItem(e.getName());
                cboLoai_Capnhat.addItem(e.getName());
                cboLoai_Themsp.addItem(e.getName());
            }
            dodulieuLoai_nhlm(qlspTracuu.getListCategory());

            for (Color e : qlspTracuu.getListColor()) {
                cboMau_Tracuu.addItem(e.getName());
                cboMau_Capnhat.addItem(e.getName());
                cboMau_Themsp.addItem(e.getName());
            }
            dodulieuMau_nhlm(qlspTracuu.getListColor());
        } catch (Exception e) {
            System.out.println("loi duLieuTraCuu " + e);
        }
    }

    private void bangTraCuu() {
        int iCate = cboLoai_Tracuu.getSelectedIndex() - 1;
        int iBrand = cboNhanhieu_Tracuu.getSelectedIndex() - 1;
        int iColor = cboMau_Tracuu.getSelectedIndex() - 1;
        String tt = cboTrangthai_Tracuu.getSelectedItem().toString();
        String tk = txtTimkiem_Tracuu.getText();
        qlspTracuu.timSPtraCuu(iBrand, iCate, iColor, tt, tk);

        DefaultTableModel model = (DefaultTableModel) tblTracuu.getModel();
        model.setRowCount(0);
        DefaultTableModel model2 = (DefaultTableModel) tblSizeTracuu.getModel();
        model2.setRowCount(0);
        for (int i = 0; i < qlspTracuu.getListProduct().size(); i++) {
            Product p = qlspTracuu.getListProduct().get(i);
            model.addRow(new Object[]{i + 1, p.getId(), p.getName(), formatText((int) p.getPrice()),
                p.getVersionName(), p.getCreatedAt(), p.getUpdatedAt()
            });
        }
        TblTracuu_Color tccr = new TblTracuu_Color();
        tblTracuu.getColumnModel().getColumn(0).setCellRenderer(tccr);
        tblTracuu.getColumnModel().getColumn(1).setCellRenderer(tccr);
        tblTracuu.getColumnModel().getColumn(2).setCellRenderer(tccr);
        tblTracuu.getColumnModel().getColumn(3).setCellRenderer(tccr);
        tblTracuu.getColumnModel().getColumn(4).setCellRenderer(tccr);
        tblTracuu.getColumnModel().getColumn(5).setCellRenderer(tccr);
        tblTracuu.getColumnModel().getColumn(6).setCellRenderer(tccr);
    }

    private void bangTraCuuClick() {
        try {
            DefaultTableModel model = (DefaultTableModel) tblSizeTracuu.getModel();
            model.setRowCount(0);
            lblNhanhieu.setText("");
            lblLoai.setText("");
            lblTrangthai.setText("");
            lblTensp.setText("");
            if (tblTracuu.getRowCount() > 0) {
                int index = tblTracuu.getSelectedRow();
                String id = tblTracuu.getValueAt(index, 1).toString();
                String ten = tblTracuu.getValueAt(index, 2).toString();
                Product p = qlspTracuu.layChiTietSP(id);
                lblNhanhieu.setText(p.getBrand().getName());
                lblLoai.setText(p.getCategory().getName());
                lblTensp.setText(ten);
                lblTrangthai.setText(p.getStatus());
                List<ProductSize> lp = qlspTracuu.layProductSize(id);
                for (ProductSize ps : lp) {
                    model.addRow(new Object[]{ps.getId(), ps.getSize(), formatText(ps.getQuantity()), ps.getCreatedAt(), ps.getUpdatedAt()});
                }
                TblChitietTracuu_Color tccr = new TblChitietTracuu_Color(id);
                tblSizeTracuu.getColumnModel().getColumn(0).setCellRenderer(tccr);
                tblSizeTracuu.getColumnModel().getColumn(1).setCellRenderer(tccr);
                tblSizeTracuu.getColumnModel().getColumn(2).setCellRenderer(tccr);
                tblSizeTracuu.getColumnModel().getColumn(3).setCellRenderer(tccr);
                tblSizeTracuu.getColumnModel().getColumn(4).setCellRenderer(tccr);
            }
        } catch (Exception e) {
            System.out.println("loi tblTracuuClick " + e);
        }
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="QUẢN TRỊ - CẬP NHẬT SẢN PHẨM">
    private void timKiemSP_CapnhatSP(String txt) {
        try {
            Product p = qlspTracuu.timKiem(txt);
            if (p != null) {
                doDuLieuTK_CapnhatSP(p);
            }
        } catch (Exception e) {
            System.out.println("loi timKiemSP_CapnhatSP " + e);
        }
    }

    private void doDuLieuTK_CapnhatSP(Product spcapnhat) {
        try {
            if (spcapnhat != null) {
                txtMaSP_CapnhatSP.setText(spcapnhat.getId() + "");
                txtTenSp_CapnhatSP.setText(spcapnhat.getName());
                spnGia_CapnhatSP.setValue(spcapnhat.getPrice());
                txtPhienban_CapnhatSP.setText(spcapnhat.getVersionName());
                //COMBOBOX NHÃN HIỆU, LOẠI
                int vitrinhan = qlspTracuu.layViTriNhanhieu(spcapnhat.getBrand().getId());
                int vitriloai = qlspTracuu.layViTriLoai(spcapnhat.getCategory().getId());
                cboNhanhieu_Capnhat.setSelectedIndex(vitrinhan);
                cboLoai_Capnhat.setSelectedIndex(vitriloai);
                cboTrangthaiCapnhat.setSelectedItem(spcapnhat.getStatus());
                txtMotaSpCapnhat.setText(spcapnhat.getDescription());
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                lblTgtaosp.setText(formatter.format(spcapnhat.getCreatedAt()));
                lblTgsuasp_Capnhatsp.setText(formatter.format(spcapnhat.getUpdatedAt()));
                //DỮ LIỆU TAB PHỤ
                doDuLieuSize_CapnhatSP();
                doDuLieuMau_CapnhatSP();
                doDuLieuHinhAnh_CapnhatSP();
            }
        } catch (Exception e) {
            System.out.println("loi doDuLieuTK_CapnhatSP " + e);
        }
    }

    private void voHieuHoaCapnhat(boolean v) {
        try {
            if (v) {
                txtTenSp_CapnhatSP.setEditable(false);
                spnGia_CapnhatSP.setEnabled(false);
                txtPhienban_CapnhatSP.setEditable(false);
                txtMotaSpCapnhat.setEditable(false);

                cboNhanhieu_Capnhat.setEnabled(false);
                cboLoai_Capnhat.setEnabled(false);
                cboTrangthaiCapnhat.setEnabled(false);

                btnCapnhatSp.setEnabled(false);
                //btnXoaAn_Capnhatsp.setEnabled(false);
                btnXoaSp_vinhvien.setEnabled(false);
                btnCapnhatSize_CapnhatSP.setEnabled(false);
                btnThemSize_CapnhatSP.setEnabled(false);
                btnXoaSize_CapnhatSP.setEnabled(false);
                btnCapnhatMau_CapnhatSP.setEnabled(false);
                btnXoamau_CapnhatSP.setEnabled(false);
                btnThemmau_CapnhatSP.setEnabled(false);
                btnChonfileAnh_CapnhatSP.setEnabled(false);
                btnThemAnh_CapnhatSP.setEnabled(false);
                btnCapnhatanh.setEnabled(false);
                btnDatlamanhchinh_Capnhatsp.setEnabled(false);
                btnXoaanh_Capnhatsp.setEnabled(false);
            } else {
                txtTenSp_CapnhatSP.setEditable(true);
                spnGia_CapnhatSP.setEnabled(true);
                txtPhienban_CapnhatSP.setEditable(true);
                txtMotaSpCapnhat.setEditable(true);

                cboNhanhieu_Capnhat.setEnabled(true);
                cboLoai_Capnhat.setEnabled(true);
                cboTrangthaiCapnhat.setEnabled(true);

                btnCapnhatSp.setEnabled(true);
                //btnXoaAn_Capnhatsp.setEnabled(true);
                btnXoaSp_vinhvien.setEnabled(true);
                btnCapnhatSize_CapnhatSP.setEnabled(true);
                btnThemSize_CapnhatSP.setEnabled(true);
                btnXoaSize_CapnhatSP.setEnabled(true);
                btnCapnhatMau_CapnhatSP.setEnabled(true);
                btnXoamau_CapnhatSP.setEnabled(true);
                btnThemmau_CapnhatSP.setEnabled(true);
                btnChonfileAnh_CapnhatSP.setEnabled(true);
                btnThemAnh_CapnhatSP.setEnabled(true);
                btnCapnhatanh.setEnabled(true);
                btnDatlamanhchinh_Capnhatsp.setEnabled(true);
                btnXoaanh_Capnhatsp.setEnabled(true);
            }
        } catch (Exception e) {
            System.out.println("loi voHieuHoaCapnhat " + e);
        }
    }

    private void doDuLieuMau_CapnhatSP() {
        try {
            String id = txtMaSP_CapnhatSP.getText();
            if (id.length() > 0) {
                QL_SanPham qlsp = new QL_SanPham();
                List<ProductColor> lc = qlsp.laydsMauSP(id);
                DefaultTableModel model2 = (DefaultTableModel) tblMau_CapnhatSP.getModel();
                model2.setRowCount(0);
                if (lc.size() > 0) {
                    int i = 1;
                    for (ProductColor e : lc) {
                        Color c = qlsp.layMau(e.getColor().getId());
                        model2.addRow(new Object[]{i, e.getId(), c.getName(), c.getCode(), e.getCreatedAt(), e.getUpdatedAt()});
                        i++;
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("loi doDuLieuMau_CapnhatSP " + e);
        }
    }

    private void doDuLieuSize_CapnhatSP() {
        try {
            String id = txtMaSP_CapnhatSP.getText();
            if (id.length() > 0) {
                QL_SanPham qlsp = new QL_SanPham();
                List<ProductSize> lps = qlsp.laydsSizeSP(id);
                DefaultTableModel model = (DefaultTableModel) tblSize_CapnhatSP.getModel();
                model.setRowCount(0);
                if (lps.size() > 0) {
                    int i = 1;
                    for (ProductSize e : lps) {
                        model.addRow(new Object[]{i, e.getId(), e.getSize(), formatText(e.getQuantity()), e.getCreatedAt(), e.getUpdatedAt()});
                        i++;
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("loi doDuLieuSize_CapnhatSP " + e);
        }
    }

    private void doDuLieuHinhAnh_CapnhatSP() {
        try {
            String id = txtMaSP_CapnhatSP.getText();
            if (id.length() > 0) {
                QL_SanPham qlsp = new QL_SanPham();
                List<ProductImage> lpi = qlsp.laydsAnh(id);
                DefaultTableModel model = (DefaultTableModel) tblAnh_CapnhatSP.getModel();
                model.setRowCount(0);
                if (lpi.size() > 0) {
                    int i = 1;
                    for (ProductImage e : lpi) {
                        String ac = "";
                        if (e.getIsPrimary()) {
                            ac = "Ảnh chính";
                        }
                        model.addRow(new Object[]{i, e.getId(), e.getData().length, e.getPath(), ac, e.getCreatedAt(), e.getUpdatedAt()});
                        i++;
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("loi doDuLieuHinhAnh " + e);
        }
    }

    private void loadHinhAnh_CapnhatSP() {
        try {
            int i = tblAnh_CapnhatSP.getSelectedRow();
            if (i > -1) {
                String id = tblAnh_CapnhatSP.getValueAt(i, 1).toString();
                QL_SanPham qlsp = new QL_SanPham();
                byte[] blob = qlsp.layHinhAnh(id);
                Converter cv = new Converter();
                BufferedImage theImage = cv.ByteToImage(blob);
                if (theImage != null) {
                    XemHinhAnh x = new XemHinhAnh(theImage);
                    x.setVisible(true);
                } else {
                    System.out.println("hình anh");
                }
            }
        } catch (Exception e) {
            System.out.println("loi loadHinhAnh_CapnhatSP " + e);
        }
    }

    private void capnhatSanpham_QLSP() {
        try {
            String id = txtMaSP_CapnhatSP.getText();
            if (id.length() > 0) {
                String ten = txtTenSp_CapnhatSP.getText().trim();
                String phienban = txtPhienban_CapnhatSP.getText().trim();
                if (ten.length() > 0 && phienban.length() > 0) {
                    if (JOptionPane.showConfirmDialog(null, "Cập nhật sản phẩm này ?", "Xác nhận cập nhật", 0) == 0) {
                        String gia = (String) spnGia_CapnhatSP.getValue().toString();
                        String mota = txtMotaSpCapnhat.getText().trim();
                        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                        Product p = new Product();
                        p.setCreatedAt(formatter.parse(lblTgtaosp.getText()));
                        p.setId(Long.parseLong(id));
                        p.setVersionName(phienban);
                        p.setName(ten);
                        p.setPrice(Long.parseLong(gia));
                        p.setDescription(mota);
                        p.setUpdatedAt(new Date());
                        int nhanhieu = cboNhanhieu_Capnhat.getSelectedIndex();
                        int loai = cboLoai_Capnhat.getSelectedIndex();
                        Brand b = qlspTracuu.layNhanHieu(nhanhieu);
                        Category c = qlspTracuu.layLoai(loai);
                        if (b != null) {
                            p.setBrand(b);
                        }
                        if (c != null) {
                            p.setCategory(c);
                        }
                        String trangthai = cboTrangthaiCapnhat.getSelectedItem().toString();
                        p.setStatus(trangthai);
                        p.setIsDelete(false);
                        //cap nhat
                        if (qlspTracuu.CapNhatSP(p) == true) {
                            System.out.println("CẬP NHẬT OK !");
                        } else {
                            System.out.println("CẬP NHẬT THẤT BẠI");
                        }
                        Thread.sleep(1000);
                        timKiemSP_CapnhatSP(id);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Một số trường bị bỏ trống !", "Lỗi thông tin cập nhật", 2);
                }
            } else {
                System.out.println("tìm kiếm cập nhật null");
            }
        } catch (Exception e) {
            System.out.println("lỗi capnhatSanpham - worfking" + e);
        }
    }

    private void XOASANPHAM() {
        try {
            if (JOptionPane.showConfirmDialog(null, "Xóa sản phẩm này ?", "Xác nhận xóa", 0) == 0) {
                String id = txtMaSP_CapnhatSP.getText();
                if (id.length() > 0) {
                    String ten = txtTenSp_CapnhatSP.getText();
                    if (qlspTracuu.xoaSP(id) == false) {
                        boolean re = qlspTracuu.xoaAnSP(id);
                        System.out.println("xoa xong " + re);
                    } else {
                        Reset_CapnhatSP();
                        JOptionPane.showMessageDialog(null, "Đã xóa sản phẩm " + ten + " !", "Thông báo", 1);
                    }
                }
            }

        } catch (Exception e) {
            System.out.println("xoA sp " + e);
        }
    }

//    private void XoaSPAn() {
//        try {
//            if (JOptionPane.showConfirmDialog(null, "Xóa ẩn sản phẩm này ?", "Xác nhận xóa", 0) == 0) {
//                String id = txtMaSP_CapnhatSP.getText();
//                if (id.length() > 0) {
//                    String ten = txtTenSp_CapnhatSP.getText();
//                    qlspTracuu.xoaAnSP(id);
//                }
//            }
//
//        } catch (Exception e) {
//            System.out.println("xoA sp " + e);
//        }
//    }
    private void themSize_CapnhatSP() {
        try {
            String id = txtMaSP_CapnhatSP.getText();
            if (id.length() > 0) {
                int size = (int) spnSize_CapnhatSP.getValue();
                int soluong = (int) spnSoluong_CapnhatSP.getValue();
                QL_SanPham ql = new QL_SanPham();
                boolean re = ql.themSize(id, size, soluong);
                if (re == false) {
                    JOptionPane.showMessageDialog(null, "Size này đã tồn tại !", "Trùng dữ liệu", 0);
                }
                doDuLieuSize_CapnhatSP();
            }
        } catch (Exception e) {
            System.out.println("loi them size " + e);
        }
    }

    private void xoaSize_CapnhatSP() {
        try {
            int i = tblSize_CapnhatSP.getSelectedRow();
            if (i > -1) {
                String id = tblSize_CapnhatSP.getValueAt(i, 1).toString();
                QL_SanPham ql = new QL_SanPham();
                boolean re = ql.xoaSize(id);
                if (re == false) {
                    JOptionPane.showMessageDialog(null, "Size này không thể xóa vì đã có dữ liệu trong hóa đơn !", "Không thể xóa", 0);
                }
                doDuLieuSize_CapnhatSP();
            }
        } catch (Exception e) {
            System.out.println("loi them size " + e);
        }
    }

    private void sizeClick_CapnhatSP() {
        try {
            int i = tblSize_CapnhatSP.getSelectedRow();
            if (i > -1) {
                String soluong = tblSize_CapnhatSP.getValueAt(i, 3).toString();
                int sl = Integer.parseInt(soluong);
                spnSanco_CapnhatSP.setValue(sl);
            }
        } catch (Exception e) {
            System.out.println("loi sizeClick_CapnhatSP " + e);
        }
    }

    private void capNhatSoluongSize_CapnhatSP() {
        try {
            int i = tblSize_CapnhatSP.getSelectedRow();
            if (i > -1) {
                String id = tblSize_CapnhatSP.getValueAt(i, 1).toString();
                int soluong = (int) spnSanco_CapnhatSP.getValue();
                QL_SanPham ql = new QL_SanPham();
                boolean re = ql.capNhatSoluong(id, soluong);
                if (re == false) {
                    JOptionPane.showMessageDialog(null, "Số lượng bằng với số cũ !", "Trùng dữ liệu", 1);
                }
                doDuLieuSize_CapnhatSP();
            }
        } catch (Exception e) {
            System.out.println("loi capNhatSoluongSize wotking");
        }
    }

    private void themMau_CapnhatSP() {
        try {
            int index = cboMau_Capnhat.getSelectedIndex();
            Color c = qlspTracuu.getListColor().get(index);
            int rowcount = tblMau_CapnhatSP.getRowCount();
            boolean isOk = true;
            for (int i = 0; i < rowcount; i++) {
                String id = tblMau_CapnhatSP.getValueAt(0, 1).toString();
                if (c.getId().toString().equals(id)) {
                    isOk = false;
                    break;
                }
            }
            if (isOk) {
                QL_SanPham q = new QL_SanPham();
                String id = txtMaSP_CapnhatSP.getText();
                q.themMau(id, c);
                doDuLieuMau_CapnhatSP();
            } else {
                JOptionPane.showMessageDialog(null, "Sản phẩm đã có màu này !", "Lỗi thêm màu", 0);
            }

        } catch (Exception e) {
            System.out.println("loi themMau_CapnhatSP " + e);
        }
    }

    private void capnhatMau_CapnhatSP() {
        try {
            int iColor = cboMau_Capnhat.getSelectedIndex();
            Color c = qlspTracuu.getListColor().get(iColor);
            int iRow = tblMau_CapnhatSP.getSelectedRow();
            if (iRow > -1) {
                String id = tblMau_CapnhatSP.getValueAt(iRow, 1).toString();
                QL_SanPham q = new QL_SanPham();
                String pid = txtMaSP_CapnhatSP.getText();
                if (pid.length() > 0) {
                    if (q.kiemTraTrungMau(pid, c.getId().toString())) {
                        boolean re = q.capNhatMau(id, c);
                        if (re == false) {
                            System.out.println("không cập nhật được màu");
                        }
                        doDuLieuMau_CapnhatSP();
                    } else {
                        JOptionPane.showMessageDialog(null, "Hiện tại sản phẩm đã có màu này !", "Trùng dữ liệu", 0);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("loi cap nhat mau working " + e);
        }
    }

    private void xoaMau_Capnhatsp() {
        try {
            int i = tblMau_CapnhatSP.getSelectedRow();
            if (i > -1) {
                String id = tblMau_CapnhatSP.getValueAt(i, 1).toString();
                QL_SanPham q = new QL_SanPham();
                q.xoaMau(id);
                doDuLieuMau_CapnhatSP();
            }
        } catch (Exception e) {
            System.out.println("loi xoa mau working " + e);
        }
    }

    private void chonFile_CapnhatSP() {
        try {
            Converter cv = new Converter();
            File b = cv.chonFile();
            if (b != null) {
                txtLinkanh_CapnhatSP.setText(b.getPath());
            }
        } catch (Exception e) {
            System.out.println("loi chonFile_CapnhatSP " + e);
        }
    }

    private void themAnh_CapnhatSP() {
        try {
            String txtlink = txtLinkanh_CapnhatSP.getText();
            String id = txtMaSP_CapnhatSP.getText();
            if (txtlink.length() > 0 && id.length() > 0) {
                File f = new File(txtlink);
                if (f.exists()) {
                    Converter cv = new Converter();
                    byte[] re = cv.chuyenThanhByte(f);
                    if (re != null) {
                        QL_SanPham q = new QL_SanPham();
                        boolean b = q.themAnh(id, f.getName(), re);
                        if (b == false) {
                            System.out.println("khong them duoc anh");
                        };
                        doDuLieuHinhAnh_CapnhatSP();
                    } else {
                        JOptionPane.showMessageDialog(null, "Chưa chọn file !", "Lỗi file", 0);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Link không còn khả dụng !", "Lỗi link", 0);
                }
            }
        } catch (Exception e) {
            System.out.println("loi themAnh_working " + e);
        }
    }

    private void xoaAnh_CapnhatSP() {
        try {
            int i = tblAnh_CapnhatSP.getSelectedRow();
            String pid = txtMaSP_CapnhatSP.getText();
            if (i > -1) {
                String id = tblAnh_CapnhatSP.getValueAt(i, 1).toString();
                QL_SanPham q = new QL_SanPham();
                boolean re = q.xoaAnh(id, pid);
                if (re == false) {
                    System.out.println("không xóa đucợ ảnh");
                }
                doDuLieuHinhAnh_CapnhatSP();
            }
        } catch (Exception e) {
            System.out.println("loi  xoa anh working " + e);
        }
    }

    private void datAnhChinh_CapnhatSP() {
        try {
            String pid = txtMaSP_CapnhatSP.getText();
            int i = tblAnh_CapnhatSP.getSelectedRow();
            if (i > -1 && pid.length() > 0) {
                String piid = tblAnh_CapnhatSP.getValueAt(i, 1).toString();
                QL_SanPham q = new QL_SanPham();
                q.datLamAnhChinh(pid, piid);
                doDuLieuHinhAnh_CapnhatSP();
            }
        } catch (Exception e) {
            System.out.println("loi datAnhChinh_CapnhatSP wotking " + e);
        }
    }

    private void capNhatAnh_CapnhatSP() {
        try {
            int row = tblAnh_CapnhatSP.getSelectedRow();
            String link = txtLinkanh_CapnhatSP.getText();
            if (row > -1 && link.length() > 0) {
                String id = tblAnh_CapnhatSP.getValueAt(row, 1).toString();
                File f = new File(link);
                if (f.exists()) {
                    Converter cv = new Converter();
                    byte[] re = cv.chuyenThanhByte(f);
                    QL_SanPham q = new QL_SanPham();
                    boolean b = q.capNhatAnh(id, f.getName(), re);
                    if (b == false) {
                        System.out.println("không cập nhật đc ảnh");
                    }
                    doDuLieuHinhAnh_CapnhatSP();
                } else {
                    JOptionPane.showMessageDialog(null, "Link không còn khả dụng !", "Lỗi link", 0);
                }
            }
        } catch (Exception e) {
            System.out.println("loi cap nhat anh working " + e);
        }
    }

    private void Reset_CapnhatSP() {
        try {
            txtMaSP_CapnhatSP.setText("");
            txtTenSp_CapnhatSP.setText("");
            spnGia_CapnhatSP.setValue(0);
            txtMotaSpCapnhat.setText("");
            lblTgtaosp.setText("yyyy-mm-dd HH:mm:sss");
            lblTgsuasp_Capnhatsp.setText("yyyy-mm-dd HH:mm:sss");
            DefaultTableModel model = (DefaultTableModel) tblSize_CapnhatSP.getModel();
            model.setRowCount(0);
            DefaultTableModel model2 = (DefaultTableModel) tblAnh_CapnhatSP.getModel();
            model2.setRowCount(0);
            DefaultTableModel model3 = (DefaultTableModel) tblMau_CapnhatSP.getModel();
            model3.setRowCount(0);
        } catch (Exception e) {
            System.out.println("loi TimKiemSP_Reset_QL " + e);
        }
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="QUẢN TRỊ - THÊM SẢN PHẨM">
    private void themSP() {
        try {
            String ten = txtTen_Themsp.getText().trim();
            String phienban = txtPhienban_Themsp.getText().trim();
            if (ten.length() > 0 && phienban.length() > 0) {
                int gia = (int) spnGia_Themsp.getValue();
                Product p = new Product();
                p.setVersionName(phienban);
                p.setName(ten);
                p.setPrice(gia);
                p.setDescription(txtMota_Themsp.getText().trim());
                p.setCreatedAt(new Date());
                p.setUpdatedAt(new Date());
                if (cboTrangthai_Themsp.getSelectedIndex() == 0) {
                    p.setIsDelete(false);
                    p.setStatus("Đang bán");
                } else {
                    if (cboTrangthai_Themsp.getSelectedIndex() == 1) {
                        p.setIsDelete(true);
                        p.setStatus("Tạm ngưng");
                    }
                }
                int iNhanhieu = cboNhanhieu_Themsp.getSelectedIndex();
                int iLoai = cboLoai_Themsp.getSelectedIndex();
                if (qlspTracuu.getListBrand().size() > iNhanhieu) {
                    Brand b = qlspTracuu.getListBrand().get(iNhanhieu);
                    p.setBrand(b);
                }
                if (qlspTracuu.getListCategory().size() > iLoai) {
                    Category c = qlspTracuu.getListCategory().get(iLoai);
                    p.setCategory(c);
                }
                //SIZE MÀU ẢNH
                int sizecount = tblSize_Themsp.getRowCount();
                List<ProductSize> lps = null;
                if (sizecount > 0) {
                    lps = new ArrayList();
                    for (int i = 0; i < sizecount; i++) {
                        String size = tblSize_Themsp.getValueAt(i, 1).toString();
                        String soluongsize = tblSize_Themsp.getValueAt(i, 2).toString();
                        ProductSize ps = new ProductSize();
                        ps.setSize(Integer.parseInt(size));
                        ps.setQuantity(Integer.parseInt(soluongsize));
                        ps.setCreatedAt(new Date());
                        ps.setUpdatedAt(new Date());
                        lps.add(ps);
                    }
                }
                List<ProductColor> lpc = null;
                int colorcount = tblMau_Themsp.getRowCount();
                if (colorcount > 0) {
                    lpc = new ArrayList();
                    for (int i = 0; i < colorcount; i++) {
                        Color colo = new Color();
                        String idmau = tblMau_Themsp.getValueAt(i, 1).toString();
                        colo.setId(Long.parseLong(idmau));
                        ProductColor pc = new ProductColor();
                        pc.setColor(colo);
                        pc.setCreatedAt(new Date());
                        pc.setUpdatedAt(new Date());
                        lpc.add(pc);
                    }
                }
                List<ProductImage> lpi = null;
                int imagecount = tblAnh_Themsp.getRowCount();
                if (imagecount > 0) {
                    lpi = new ArrayList();
                    for (int i = 0; i < imagecount; i++) {
                        String linkanh = tblAnh_Themsp.getValueAt(i, 1).toString();
                        File f = new File(linkanh);
                        if (f.exists()) {
                            Converter cv = new Converter();
                            byte[] anh = cv.chuyenThanhByte(f);
                            ProductImage pi = new ProductImage();
                            pi.setCreatedAt(new Date());
                            pi.setUpdatedAt(new Date());
                            pi.setData(anh);
                            pi.setPath(f.getName());
                            pi.setSize(anh.length);
                            pi.setIsPrimary(false);
                            if (i == imagecount - 1) {
                                pi.setIsPrimary(true);
                            }
                            lpi.add(pi);
                        }
                    }
                }
                QL_ThemSP q = new QL_ThemSP();
                String re = q.themSanPham(p, lps, lpc, lpi);
                if (re != null) {
                    JOptionPane.showMessageDialog(null, re, "Đã thêm", 1);
                } else {
                    System.out.println("khong them dc sp");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Một số trường bị bỏ trống !", "Lỗi thông tin", 0);
            }
        } catch (Exception e) {
            System.out.println("loi them sp " + e);
        }
    }

    private void themSize_Themsp() {
        try {
            int c = tblSize_Themsp.getRowCount();
            boolean isHave = false;
            int size = (int) spnSize_Themsp.getValue();
            if (c > 0) {
                for (int i = 0; i < c; i++) {
                    String re = tblSize_Themsp.getValueAt(i, 1).toString();
                    if (re.equals(size + "")) {
                        isHave = true;
                        break;
                    }
                }

            }
            //them size
            if (isHave == false) {
                c = c + 1;
                DefaultTableModel model = (DefaultTableModel) tblSize_Themsp.getModel();
                model.addRow(new Object[]{c, size, spnSoluong_Themsp.getValue()});
            } else {
                JOptionPane.showMessageDialog(null, "Size đã tồn tại !", "Lỗi thêm", 0);
            }
        } catch (Exception e) {
            System.out.println("loi them size themsp " + e);
        }
    }

    private void xoaSize_Themsp() {
        try {
            int i = tblSize_Themsp.getSelectedRow();
            if (i > -1) {
                DefaultTableModel model = (DefaultTableModel) tblSize_Themsp.getModel();
                model.removeRow(i);
            }
        } catch (Exception e) {
            System.out.println("loi xoaSize_Themsp " + e);
        }
    }

    private void themMau_Themsp() {
        try {
            int index = cboMau_Themsp.getSelectedIndex();
            Color color = qlspTracuu.getListColor().get(index);
            boolean isHave = false;
            for (int i = 0; i < tblMau_Themsp.getRowCount(); i++) {
                String id = tblMau_Themsp.getValueAt(i, 1).toString();
                if (id.equals(color.getId().toString())) {
                    isHave = true;
                    break;
                }
            }
            //them mau
            if (isHave == false) {
                int c = tblMau_Themsp.getRowCount() + 1;
                DefaultTableModel model = (DefaultTableModel) tblMau_Themsp.getModel();
                model.addRow(new Object[]{c, color.getId(), color.getName(), color.getCode()});
            } else {
                JOptionPane.showMessageDialog(null, "Màu đã tồn tại !", "Lỗi thêm", 0);
            }
        } catch (Exception e) {
            System.out.println("loi themMau_Themsp " + e);
        }
    }

    private void xoaMau_Themsp() {
        try {
            int index = tblMau_Themsp.getSelectedRow();
            if (index > -1) {
                DefaultTableModel model = (DefaultTableModel) tblMau_Themsp.getModel();
                model.removeRow(index);
            }
        } catch (Exception e) {
            System.out.println("loi xoaMau_Themsp " + e);
        }
    }

    private void loadFile_Themsp() {
        try {
            Converter cv = new Converter();
            File b = cv.chonFile();
            if (b != null) {
                txtLink_Themsp.setText(b.getPath());
            }
        } catch (Exception e) {
            System.out.println("loi loadFile_Themsp " + e);
        }
    }

    private void themAnh_Themsp() {
        try {
            String link = txtLink_Themsp.getText();
            if (link.length() > 0) {
                File f = new File(link);
                if (f.exists()) {
                    int rowcount = tblAnh_Themsp.getRowCount();
                    boolean isHave = false;
                    for (int i = 0; i < rowcount; i++) {
                        String getlink = tblAnh_Themsp.getValueAt(i, 1).toString();
                        if (f.getPath().equals(getlink)) {
                            isHave = true;
                            break;
                        }
                    }
                    if (isHave == false) {
                        DefaultTableModel model = (DefaultTableModel) tblAnh_Themsp.getModel();
                        model.addRow(new Object[]{rowcount + 1, f.getPath(), f.getName()});
                    } else {
                        JOptionPane.showMessageDialog(null, "Đường dẫn bị trùng !", "Lỗi thêm", 0);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Đường dẫn không khả dụng !", "Lỗi link", 0);
                }
            }
        } catch (Exception e) {
            System.out.println("loi them anh them sp " + e);
        }
    }

    private void xoaAnh_Themsp() {
        try {
            int index = tblAnh_Themsp.getSelectedRow();
            if (index > -1) {
                DefaultTableModel model = (DefaultTableModel) tblAnh_Themsp.getModel();
                model.removeRow(index);
            }
        } catch (Exception e) {
            System.out.println("loi xoaAnh_Themsp " + e);
        }
    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="QUẢN TRỊ - NHÃN HIỆU, LOẠI, MÀU">

    private void dodulieuNhanhieu_nhlm(List<Brand> list) {
        try {
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

    private void dodulieuLoai_nhlm(List<Category> list) {
        try {
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

    private void dodulieuMau_nhlm(List<Color> list) {
        try {
            if (list.size() > 0) {
                int index = 1;
                DefaultTableModel model = (DefaultTableModel) tblMau.getModel();
                model.setRowCount(0);
                for (Color n : list) {
                    model.addRow(new Object[]{index, n.getId(), n.getCode(), n.getName(), n.getCreatedAt(), n.getUpdatedAt()});
                    index++;
                }
            }
        } catch (Exception e) {
            System.out.println("loi do du lieu nhan hieu " + e);
        }
    }

    private void themNhanhieu() {
        try {
            String ten = txtTennhanhhieu.getText().trim();
            if (ten.length() > 0) {
                Brand b = new Brand();
                b.setName(ten);
                b.setCreatedAt(new Date());
                b.setUpdatedAt(new Date());
                int rowcount = tblNhanhieu.getRowCount();
                boolean isOk = true;
                for (int i = 0; i < rowcount; i++) {
                    String name = tblNhanhieu.getValueAt(i, 2).toString();
                    if (name.equals(b.getName())) {
                        JOptionPane.showMessageDialog(null, "Trùng tên với nhãn hiệu khác !", "Lỗi thêm", 0);
                        isOk = false;
                        break;
                    }
                }
                if (isOk) {
                    DataTransacter data = new DataTransacter();
                    boolean re = data.insert(b);
                    if (re == true) {
                        qlspTracuu.layNhanhieu_Loai_Mau();
                        cboNhanhieu_Tracuu.addItem(b.getName());
                        cboNhanhieu_Capnhat.addItem(b.getName());
                        cboNhanhieu_Themsp.addItem(b.getName());
                        dodulieuNhanhieu_nhlm(qlspTracuu.getListBrand());
                    } else {
                        System.out.println("khong them dc nhan hieu");
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("them nhan hieu loi " + e);
        }
    }

    private void doitenNhanhieu() {
        try {
            int iCboCapnhat = cboNhanhieu_Capnhat.getSelectedIndex();
            int iCboTracuu = cboNhanhieu_Tracuu.getSelectedIndex();
            int iCboThemsp = cboNhanhieu_Themsp.getSelectedIndex();
            int row = tblNhanhieu.getSelectedRow();
            if (row > -1) {
                String ten = txtTenNhanhieumoi.getText().trim();
                if (ten.length() > 0) {
                    int rowcount = tblNhanhieu.getRowCount();
                    boolean isOk = true;
                    for (int i = 0; i < rowcount; i++) {
                        String name = tblNhanhieu.getValueAt(i, 2).toString();
                        if (name.equals(ten)) {
                            JOptionPane.showMessageDialog(null, "Trùng tên với nhãn hiệu khác !", "Lỗi thêm", 0);
                            isOk = false;
                            break;
                        }
                    }
                    if (isOk) {
                        Brand b = qlspTracuu.getListBrand().get(row);
                        String tencu = b.getName();
                        b.setUpdatedAt(new Date());
                        b.setName(ten);
                        if (!ten.equals(tencu)) {
                            DataTransacter data = new DataTransacter();
                            data.update(b);
                            qlspTracuu.layNhanhieu_Loai_Mau();
                            comboBox_tblMauNhanAnh();
                            cboNhanhieu_Capnhat.setSelectedIndex(iCboCapnhat);
                            cboNhanhieu_Tracuu.setSelectedIndex(iCboTracuu);
                            cboNhanhieu_Themsp.setSelectedIndex(iCboThemsp);
                            dodulieuNhanhieu_nhlm(qlspTracuu.getListBrand());
                            JOptionPane.showMessageDialog(null, "Đã đổi tên nhãn hiệu " + tencu + " thành " + b.getName(), "Thông báo", 1);
                        } else {
                            JOptionPane.showMessageDialog(null, "Trùng tên cũ !", "Lỗi nhập", 0);
                        }
                    }
                } else {
                    System.out.println("rỗng");
                }
            }
        } catch (Exception e) {
            System.out.println("doi ten nah hieu loi " + e);
        }
    }

    private void xoaNhanhieu() {
        try {
            int row = tblNhanhieu.getSelectedRow();
            if (row > -1) {
                Brand b = qlspTracuu.getListBrand().get(row);
                DataTransacter dt = new DataTransacter();
                if (dt.delete(b) == false) {
                    JOptionPane.showMessageDialog(null, "Không thể xóa nhãn hiệu do có sản phẩm đang sử dụng !");
                } else {
                    qlspTracuu.layNhanhieu_Loai_Mau();
                    cboNhanhieu_Tracuu.removeItem(b.getName());
                    cboNhanhieu_Capnhat.removeItem(b.getName());
                    cboNhanhieu_Themsp.removeItem(b.getName());
                    dodulieuNhanhieu_nhlm(qlspTracuu.getListBrand());
                }
            }
        } catch (Exception e) {
            System.out.println("xoa nhan hieu loi " + e);
        }
    }

    //loai
    private void themLoai() {
        try {
            String ten = txtTenLoai.getText().trim();
            if (ten.length() > 0) {
                Category b = new Category();
                b.setName(ten);
                b.setCreatedAt(new Date());
                b.setUpdatedAt(new Date());
                int rowcount = tblLoai.getRowCount();
                boolean isOk = true;
                for (int i = 0; i < rowcount; i++) {
                    String name = tblLoai.getValueAt(i, 2).toString();
                    if (name.equals(b.getName())) {
                        JOptionPane.showMessageDialog(null, "Trùng tên với loại khác !", "Lỗi thêm", 0);
                        isOk = false;
                        break;
                    }
                }
                if (isOk) {
                    DataTransacter data = new DataTransacter();
                    boolean re = data.insert(b);
                    if (re == true) {
                        qlspTracuu.layNhanhieu_Loai_Mau();
                        cboLoai_Tracuu.addItem(b.getName());
                        cboLoai_Capnhat.addItem(b.getName());
                        cboLoai_Themsp.addItem(b.getName());
                        dodulieuLoai_nhlm(qlspTracuu.getListCategory());
                    } else {
                        System.out.println("khong them dc nhan hieu");
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("them nhan hieu loi " + e);
        }
    }

    private void doitenLoai() {
        try {
            int iCboCapnhat = cboLoai_Capnhat.getSelectedIndex();
            int iCboTracuu = cboLoai_Tracuu.getSelectedIndex();
            int iCboThemsp = cboLoai_Themsp.getSelectedIndex();
            int row = tblLoai.getSelectedRow();
            if (row > -1) {
                String ten = txtTenLoaiMoi.getText().trim();
                if (ten.length() > 0) {
                    int rowcount = tblLoai.getRowCount();
                    boolean isOk = true;
                    for (int i = 0; i < rowcount; i++) {
                        String name = tblLoai.getValueAt(i, 2).toString();
                        if (name.equals(ten)) {
                            JOptionPane.showMessageDialog(null, "Trùng tên với loại khác !", "Lỗi thêm", 0);
                            isOk = false;
                            break;
                        }
                    }
                    if (isOk) {
                        Category b = qlspTracuu.getListCategory().get(row);
                        String tencu = b.getName();
                        b.setUpdatedAt(new Date());
                        b.setName(ten);
                        if (!ten.equals(tencu)) {
                            DataTransacter data = new DataTransacter();
                            data.update(b);
                            qlspTracuu.layNhanhieu_Loai_Mau();
                            comboBox_tblMauNhanAnh();
                            cboLoai_Capnhat.setSelectedIndex(iCboCapnhat);
                            cboLoai_Tracuu.setSelectedIndex(iCboTracuu);
                            cboLoai_Themsp.setSelectedIndex(iCboThemsp);
                            dodulieuLoai_nhlm(qlspTracuu.getListCategory());
                            JOptionPane.showMessageDialog(null, "Đã đổi tên loại " + tencu + " thành " + b.getName(), "Thông báo", 1);
                        } else {
                            JOptionPane.showMessageDialog(null, "Trùng tên cũ !", "Lỗi nhập", 0);
                        }
                    }
                } else {
                    System.out.println("rỗng");
                }
            }
        } catch (Exception e) {
            System.out.println("doi ten loai loi " + e);
        }
    }

    private void xoaLoai() {
        try {
            int row = tblNhanhieu.getSelectedRow();
            if (row > -1) {
                Category b = qlspTracuu.getListCategory().get(row);
                DataTransacter dt = new DataTransacter();
                if (dt.delete(b) == false) {
                    JOptionPane.showMessageDialog(null, "Không thể xóa loại do có sản phẩm đang sử dụng !");
                } else {
                    qlspTracuu.layNhanhieu_Loai_Mau();
                    cboLoai_Tracuu.removeItem(b.getName());
                    cboLoai_Capnhat.removeItem(b.getName());
                    cboLoai_Themsp.removeItem(b.getName());
                    dodulieuLoai_nhlm(qlspTracuu.getListCategory());
                }
            }
        } catch (Exception e) {
            System.out.println("xoa loai loi " + e);
        }
    }

    //mau
    private void themMau() {
        try {
            String ten = txtTenmau.getText().trim();
            String code = txtCodemau.getText().trim();
            if (ten.length() > 0 && code.length() > 0) {
                Color b = new Color();
                b.setName(ten);
                b.setCode(code);
                b.setCreatedAt(new Date());
                b.setUpdatedAt(new Date());
                int rowcount = tblMau.getRowCount();
                boolean isOk = true;
                boolean isOk2 = true;
                for (int i = 0; i < rowcount; i++) {
                    String tencu = tblMau.getValueAt(i, 3).toString();
                    String codecu = tblMau.getValueAt(i, 2).toString();
                    if (tencu.equals(b.getName())) {
                        JOptionPane.showMessageDialog(null, "Trùng tên với màu khác !", "Lỗi thêm", 0);
                        isOk2 = false;
                        break;
                    }
                    if (codecu.equals(b.getCode())) {
                        JOptionPane.showMessageDialog(null, "Trùng với code khác !", "Lỗi thêm", 0);
                        isOk = false;
                        break;
                    }
                }
                if (isOk && isOk2) {
                    DataTransacter data = new DataTransacter();
                    boolean re = data.insert(b);
                    if (re == true) {
                        qlspTracuu.layNhanhieu_Loai_Mau();
                        cboMau_Tracuu.addItem(b.getName());
                        cboMau_Capnhat.addItem(b.getName());
                        cboMau_Themsp.addItem(b.getName());
                        dodulieuMau_nhlm(qlspTracuu.getListColor());
                    } else {
                        System.out.println("khong them dc nhan hieu");
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("them mau loi " + e);
        }
    }

    private void doitenMau() {
        try {
            int iCboCapnhat = cboMau_Capnhat.getSelectedIndex();
            int iCboTracuu = cboMau_Tracuu.getSelectedIndex();
            int iCboThemsp = cboMau_Themsp.getSelectedIndex();
            int row = tblMau.getSelectedRow();
            if (row > -1) {
                String ten = txtTenmaumoi.getText().trim();
                if (ten.length() > 0) {
                    int rowcount = tblMau.getRowCount();
                    boolean isOk = true;
                    for (int i = 0; i < rowcount; i++) {
                        String name = tblMau.getValueAt(i, 2).toString();
                        if (name.equals(ten)) {
                            JOptionPane.showMessageDialog(null, "Trùng tên với màu khác !", "Lỗi thêm", 0);
                            isOk = false;
                            break;
                        }
                    }
                    if (isOk) {
                        Color b = qlspTracuu.getListColor().get(row);
                        String tencu = b.getName();
                        b.setUpdatedAt(new Date());
                        b.setName(ten);
                        if (!ten.equals(tencu)) {
                            DataTransacter data = new DataTransacter();
                            data.update(b);
                            qlspTracuu.layNhanhieu_Loai_Mau();
                            comboBox_tblMauNhanAnh();
                            cboMau_Capnhat.setSelectedIndex(iCboCapnhat);
                            cboMau_Tracuu.setSelectedIndex(iCboTracuu);
                            cboMau_Themsp.setSelectedIndex(iCboThemsp);
                            dodulieuMau_nhlm(qlspTracuu.getListColor());
                            JOptionPane.showMessageDialog(null, "Đã đổi tên màu " + tencu + " thành " + b.getName(), "Thông báo", 1);
                        } else {
                            JOptionPane.showMessageDialog(null, "Trùng tên cũ !", "Lỗi nhập", 0);
                        }
                    }
                } else {
                    System.out.println("rỗng");
                }
            }
        } catch (Exception e) {
            System.out.println("doi mau loi " + e);
        }
    }

    private void xoaMau() {
        try {
            int row = tblMau.getSelectedRow();
            if (row > -1) {
                Color b = qlspTracuu.getListColor().get(row);
                DataTransacter dt = new DataTransacter();
                if (dt.delete(b) == false) {
                    JOptionPane.showMessageDialog(null, "Không thể xóa màu do có sản phẩm đang sử dụng !");
                } else {
                    qlspTracuu.layNhanhieu_Loai_Mau();
                    cboMau_Tracuu.removeItem(b.getName());
                    cboMau_Capnhat.removeItem(b.getName());
                    cboMau_Themsp.removeItem(b.getName());
                    dodulieuMau_nhlm(qlspTracuu.getListColor());
                }
            }
        } catch (Exception e) {
            System.out.println("xoa mau loi " + e);
        }
    }
    //</editor-fold>
    User thanhvienclick = null;

    //<editor-fold defaultstate="collapsed" desc="QUẢN TRỊ - THÀNH VIÊN">
    private void doDuLieuBangTV() {
        try {
            DefaultTableModel model = (DefaultTableModel) tblTV.getModel();
            model.setRowCount(0);
            resetCapnhatTV();
            thanhvienclick = null;
            DataTransacter dt = new DataTransacter();
            List<User> list = dt.select(scriptTimTV());
            if (list.size() > 0) {
                int i = 1;
                for (User n : list) {
                    model.addRow(new Object[]{i, n.getId(), n.getName(), n.getEmail(), n.getCreatedAt(), n.getUpdatedAt()});
                    i++;
                }
            }
        } catch (Exception e) {
            System.out.println("lỗi đổ bảng thành viên " + e);
        }
    }

    private String scriptTimTV() {
        String txt = "from User where isDelete=0 ";
        try {
            if (cboChucvuTV.getSelectedIndex() > 0) {
                int index = cboChucvuTV.getSelectedIndex();
                if (index == 1) {
                    txt += " and role='ROLE_ADMIN'";
                }
                if (index == 2) {
                    txt += " and role='ROLE_WEB_STAFF'";
                }
                if (index == 3) {
                    txt += " and role='ROLE_APP_STAFF'";
                }
            }
            if (jdDataoTV.getDate() != null && jdDataoTV2.getDate() != null) {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                String date1 = formatter.format(jdDataoTV.getDate());
                String date2 = formatter.format(jdDataoTV2.getDate());
                txt += " and createdAt between '" + date1 + "' and '" + date2 + "'";
            }
            String nhap = txtTimthanhvien_QLTV.getText().trim();
            if (nhap.length() > 0) {
                txt += " and name like '%" + nhap + "%' or email like '%" + nhap + "%'";
            }
            System.out.println(txt);
        } catch (Exception e) {
            System.out.println("loi cript loc tv " + e);
        }
        return txt;
    }

    private void ClickThanhvien() {
        try {
            int row = tblTV.getSelectedRow();
            if (row > -1) {
                String id = tblTV.getValueAt(row, 1).toString();
                QL_ThanhVien tv = new QL_ThanhVien();
                thanhvienclick = tv.layThanhVien(id);
                txtTen_Suatv.setText(thanhvienclick.getName());
                txtEmail_Suatv.setText(thanhvienclick.getEmail());
                if (thanhvienclick.getGender()) {
                    cboGioitinh_Suatv.setSelectedIndex(1);
                } else {
                    cboGioitinh_Suatv.setSelectedIndex(0);
                }
                txtSdt_Suatv.setText(thanhvienclick.getPhone());
                jdNgaysinh_Suatv.setDate(thanhvienclick.getDateOfBirth());

                if (thanhvienclick.getRole().equals("ROLE_ADMIN")) {
                    cboChucvu_Suatv.setSelectedIndex(0);
                } else if (thanhvienclick.getRole().equals("ROLE_WEB_STAFF")) {
                    cboChucvu_Suatv.setSelectedIndex(1);
                } else if (thanhvienclick.getRole().equals("ROLE_APP_STAFF")) {
                    cboChucvu_Suatv.setSelectedIndex(2);
                }
                txtDiachi_Suatv.setText(thanhvienclick.getAddress());
                String mk = thanhvienclick.getPassword();
                if (mk.contains("##moithem##")) {
                    lblDoimk_Suatv.setText("Tài khoản này chưa đổi mật khẩu");
                } else {
                    lblDoimk_Suatv.setText("Cho phép đổi mật khẩu");
                }
//                boolean valuate = BCrypt.checkpw("##yeucau",txtXuat.getText());
            }
        } catch (Exception e) {
            System.out.println("loi click thanh vien " + e);
        }
    }

    private void chonFile_Suatv() {
        try {
            Converter cv = new Converter();
            File b = cv.chonFile();
            if (b != null) {
                txtAnh_Suatv.setText(b.getPath());
            }
        } catch (Exception e) {
            System.out.println("loi chonFile_CapnhatSP " + e);
        }
    }

    private void resetCapnhatTV() {
        try {
            txtTen_Suatv.setText("");
            txtEmail_Suatv.setText("");
            txtSdt_Suatv.setText("");
            txtAnh_Suatv.setText("");
            txtDiachi_Suatv.setText("");
        } catch (Exception e) {
            System.out.println("loi resetCapnhatTV " + e);
        }
    }

    private void CapNhatTV() {
        try {
            if (thanhvienclick != null) {
                String ten = txtTen_Suatv.getText().trim();
                String email = txtEmail_Suatv.getText().trim();
                String sdt = txtSdt_Suatv.getText().trim();
                String diachi = txtDiachi_Suatv.getText().trim();

                File f = new File(txtAnh_Suatv.getText().trim());
                int role = cboChucvu_Suatv.getSelectedIndex();
                String err = validateTV(ten, email, sdt, diachi, f, role);

                if (err == null && JOptionPane.showConfirmDialog(null, "Cập nhật thành viên này ?", "Xác nhận cập nhật", 0) == 0) {
                    Date ngaysinh = jdNgaysinh_Suatv.getDate();
                    User u = thanhvienclick;
                    u.setName(ten);
                    u.setEmail(email);
                    u.setPhone(sdt);
                    u.setAddress(diachi);
                    if (role == 0) {
                        u.setRole("ROLE_ADMIN");
                    } else {
                        if (role == 1) {
                            u.setRole("ROLE_WEB_STAFF");
                        } else {
                            if (role == 2) {
                                u.setRole("ROLE_APP_STAFF");
                            }
                        }
                    }
                    System.out.println("role la " + u.getRole());
                    if (cboGioitinh_Suatv.getSelectedIndex() == 0) {
                        u.setGender(false);
                    } else {
                        u.setGender(true);
                    }
                    if (f.exists()) {
                        Converter cv = new Converter();
                        u.setImageData(cv.chuyenThanhByte(f));
                        u.setImagePath(f.getName());
                    }
                    u.setDateOfBirth(ngaysinh);
                    u.setIsDelete(false);
                    u.setUpdatedAt(new Date());
                    QL_ThanhVien q = new QL_ThanhVien();
                    boolean re = q.CapnhatTV(u);
                    if (re == false) {
                        JOptionPane.showMessageDialog(null, "Không cập nhật vì chỉ còn 1 admin !", "Thiếu chức vụ", 1);
                    } else {
                        resetCapnhatTV();
                    }
                } else {
                    JOptionPane.showMessageDialog(null, err, "Lỗi nhập !", 0);
                }
                System.out.println(err);
            }
        } catch (Exception e) {
            System.out.println("loi capNhatThanhVien " + e);
        }
    }

    private void xoaThanhvien() {
        try {
            if (thanhvienclick != null) {
                if (user.getRole().equals("ROLE_ADMIN")
                        || user.getRole().equals("ROLE_APP_MANAGER") && thanhvienclick.getRole().equals("ROLE_APP_STAFF")) {
                    if (JOptionPane.showConfirmDialog(null, "Xóa thành viên " + thanhvienclick.getName() + " ?", "Xác nhận xóa", 0) == 0) {
                        QL_ThanhVien q = new QL_ThanhVien();
                        boolean re = q.xoaThanhvien(thanhvienclick);
                        if (re == false) {
                            System.out.println("không xóa dduocj thành viên");
                        } else {
                            resetCapnhatTV();
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Phân quyền không cho phép xóa thành viên này !", "Lỗi phân quyền", 1);
                }
            }
        } catch (Exception e) {
            System.out.println("loi  xoa thanh vien " + e);
        }
    }

    private void xemAnh_Suatv() {
        try {
            if (thanhvienclick != null) {
                Converter cv = new Converter();
                BufferedImage theImage = cv.ByteToImage(thanhvienclick.getImageData());
                if (theImage != null) {
                    XemHinhAnh x = new XemHinhAnh(theImage);
                    x.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(null, "Thành viên này chưa có ảnh !");
                }
            }
        } catch (Exception e) {
        }
    }

    private String validateTV(String ten, String email, String sdt, String diachi, File f, int role) {
        String err = null;
        try {
            if (ten.length() == 0) {
                err = "Chưa nhập tên !";
            }
            if (email.length() == 0 || !email.contains("@") || !email.contains(".")) {
                if (err == null) {
                    err += "Email không đúng !";
                } else {
                    err += "\nEmail không đúng !";
                }
            }
            if (sdt.length() < 10 || sdt.length() > 11) {
                if (err == null) {
                    err = "Số điện thoại không dúng !";
                } else {
                    err += "\nSố điện thoại không dúng !";
                }
            }
            if (diachi.length() == 0) {
                if (err == null) {
                    err = "Chưa nhập địa chỉ !";
                } else {
                    err += "\nChưa nhập địa chỉ !";
                }
            }

//            if (!f.exists()) {
//                if (err == null) {
//                    err = "File không còn tồn tại !";
//                } else {
//                    err += "\nFile không còn tồn tại !";
//                }
//            }
            if (!user.getRole().equals("ROLE_ADMIN") && role == 0 || role == 1) {
                if (err == null) {
                    err = "Phân quyền chỉ cho phép thao tác với chức vụ nhân viên !";
                } else {
                    err += "\nPhân quyền chỉ cho phép thao tác với chức vụ nhân viên !";
                }
            }
        } catch (Exception e) {
            System.out.println("loi validate " + e);
        }
        return err;
    }

    private void chonFile_Themtv() {
        try {
            Converter cv = new Converter();
            File b = cv.chonFile();
            if (b != null) {
                txtAnh_Themtv.setText(b.getPath());
            }
        } catch (Exception e) {
            System.out.println("loi chonFile_CapnhatSP " + e);
        }
    }

    private void themThanhVien() {
        try {
            String ten = txtTen_Themtv.getText().trim();
            String email = txtEmail_Themtv.getText().trim();
            String sdt = txtSdt_Themtv.getText().trim();
            String diachi = txtDiachi_Themtv.getText().trim();

            File f = new File(txtAnh_Themtv.getText().trim());
            int role = cboChucvu_Themtv.getSelectedIndex();
            String err = validateTV(ten, email, sdt, diachi, f, role);

            if (err == null) {
                String password = "##moithem##";
                Converter cv = new Converter();
                String mahoa = cv.mahoa(password);
                Date ngaysinh = jdNgaysinh_Themtv.getDate();

                User u = new User();
                u.setName(ten);
                u.setEmail(email);
                u.setPhone(sdt);
                u.setAddress(diachi);
                if (role == 0) {
                    u.setRole("ROLE_ADMIN");
                } else {
                    if (role == 1) {
                        u.setRole("ROLE_WEB_STAFF");
                    } else {
                        if (role == 2) {
                            u.setRole("ROLE_APP_STAFF");
                        }
                    }
                }
                System.out.println("role la " + u.getRole());
                if (cboGioitinh_Suatv.getSelectedIndex() == 0) {
                    u.setGender(false);
                } else {
                    u.setGender(true);
                }
                if (f.exists()) {
                    u.setImageData(cv.chuyenThanhByte(f));
                    u.setImagePath(f.getName());
                }
                u.setPassword(mahoa);
                u.setDateOfBirth(ngaysinh);
                u.setIsDelete(false);
                u.setCreatedAt(new Date());
                DataTransacter dt = new DataTransacter();
                if (dt.insert(u)) {
                    JOptionPane.showMessageDialog(null, "Đã thêm thành viên " + u.getName());
                } else {
                    System.out.println("không thêm đc thành viên");
                }
            } else {
                JOptionPane.showMessageDialog(null, err, "lỗi nhập", 0);
            }
        } catch (Exception e) {
            System.out.println("loi themThanhVien " + e);
        }
    }

    private void datLaiMK() {
        try {
            if (thanhvienclick != null && JOptionPane.showConfirmDialog(null, "Cho phép thành viên này đổi mật khẩu ?", "Xác nhận thay đổi", 0) == 0) {
                String mk = thanhvienclick.getPassword();
                mk += "##moithem##";
                thanhvienclick.setPassword(mk);
                DataTransacter dt = new DataTransacter();
                dt.update(thanhvienclick);
            }
        } catch (Exception e) {
            System.out.println("loi dat lai mk " + e);
        }
    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="QUẢN TRỊ - THỐNG KÊ">

    private thongke laythongke_thongke() {
        thongke tk = new thongke();
        try {
            tk.setBangiao(1);
            tk.setTimtheotg(false);
            tk.setKhoangThoigian(false);
            tk.setMocThoigian(false);
            tk.setNgaythangnam(1);
            tk.setTimtheoSanpham(false);
            tk.setTimtheoNhanvien(false);
            tk.setTongsp(0);
            if (rbDabangiao.isSelected()) {
                tk.setBangiao(1);
            }
            if (rbChuabangiao.isSelected()) {
                tk.setBangiao(2);
            }
            if (rbTatcabangiao.isSelected()) {
                tk.setBangiao(3);
            }
            //thời gian
            if (ckTimtheotg.isSelected()) {
                tk.setTimtheotg(true);
            }
            if (rbKhoangtg.isSelected()) {
                tk.setKhoangThoigian(true);
            }
            if (rbMoctg.isSelected()) {
                tk.setMocThoigian(true);
            }
            //
            tk.setTg3(tg3_thongke.getDate());
            tk.setTg1(tg1_thongke.getDate());
            tk.setTg2(tg2_thongke.getDate());
            if (rbNgay_thongke.isSelected()) {
                tk.setNgaythangnam(1);
            }
            if (rbThang_thongke.isSelected()) {
                tk.setNgaythangnam(2);
            }
            if (rbNam_thongke.isSelected()) {
                tk.setNgaythangnam(3);
            }
            //sản phẩm
            if (ckTimtheosp.isSelected()) {
                tk.setTimtheoSanpham(true);
            }
            //tìm theo nhân viên
            if (ckTimtheonv.isSelected()) {
                tk.setTimtheoNhanvien(true);
            }
            tk.setMasanpham(txtMasp_thongke.getText());
            if (ckTimtheonv.isSelected()) {
                tk.setTimtheoNhanvien(true);
            }
            tk.setManhanvien(txtManv_thongke.getText());
        } catch (Exception e) {
        }
        return tk;
    }

    private void doDuLieuHoaDon_Thongke() {
        try {
            DefaultTableModel model = (DefaultTableModel) tblHoadon_Thongke.getModel();
            model.setRowCount(0);
            lblTonghoadon_Thongke.setText("0 đôi");
            lblThongthu_Thongke.setText("0 đ");
            lblTongsanpham_thongke.setText("0 đôi");
            thongke t = laythongke_thongke();
            QL_HoaDon qlhd = new QL_HoaDon();
            if (rbHoadonapp.isSelected()) {
                List<OrderApp> oa = qlhd.locHoadonApp(t);
                if (oa.size() > 0) {
                    int i = 1;
                    int tongthu = 0;
                    for (OrderApp e : oa) {
                        QL_ThanhVien qltv = new QL_ThanhVien();
                        User nv = qltv.layThanhVien(e.getUser().getId().toString());
                        model.addRow(new Object[]{i, e.getId(), formatText((int) e.getTotalAmount()), e.getCreatedAt(),
                            e.getUpdatedAt(), e.getUser().getId(), nv.getName()});
                        i++;
                        tongthu += e.getTotalAmount();
                    }
                    lblThongthu_Thongke.setText(formatText(tongthu) + " đ");
                    lblTonghoadon_Thongke.setText(formatText(oa.size()) + " đơn");
                    lblTongsanpham_thongke.setText(formatText(t.getTongsp()) + " đôi");
                }
            } else {
                if (rbHoadonweb.isSelected()) {
                    List<OrderWeb> oa = qlhd.locHoadonWeb(t);
                    if (oa.size() > 0) {
                        int i = 1;
                        int tongthu = 0;
                        for (OrderWeb e : oa) {
                            QL_ThanhVien qltv = new QL_ThanhVien();
                            User nv = qltv.layThanhVien(e.getUser().getId().toString());
                            model.addRow(new Object[]{i, e.getId(), formatText((int) e.getTotalAmount()), e.getConsignee(),
                                e.getConsigneePhone(), e.getDeliveryAddress(), e.getPaymentMethod(),
                                e.getCreatedAt(), e.getUpdatedAt(), e.getUser().getId(),
                                nv.getName()});
                            i++;
                            tongthu += e.getTotalAmount();
                        }
                        lblThongthu_Thongke.setText(formatText(tongthu) + " đ");
                        lblTonghoadon_Thongke.setText(formatText(oa.size()) + " đơn");
                        lblTongsanpham_thongke.setText(formatText(t.getTongsp()) + " đôi");
                    }
                }
            }

        } catch (Exception e) {
            System.out.println("loi doDuLieuHoaDon_Thongke " + e);
        }
    }

    private void chonLoaiHoaDon_Thongke(boolean b) {
        try {
            DefaultTableModel model = (DefaultTableModel) tblHoadon_Thongke.getModel();
            model.setRowCount(0);
            DefaultTableModel model2 = (DefaultTableModel) tblChitiethoadon_Thongke.getModel();
            model2.setRowCount(0);
            if (b) {
                rbDabangiao.setEnabled(true);
                rbChuabangiao.setEnabled(true);
                rbTatcabangiao.setEnabled(true);
                ckTimtheonv.setText("Tìm theo nhân viên");
                lblNhapmaTV_thongke.setText("Nhập mã nhân viên");
                model.setColumnCount(0);
                model.addColumn("#");
                model.addColumn("Mã HĐ");
                model.addColumn("Tổng tiền");
                model.addColumn("Thời gian");
                model.addColumn("Bàn giao");
                model.addColumn("Mã nhân viên");
                model.addColumn("Tên nhân viên");
                tblHoadon_Thongke.getColumnModel().getColumn(0).setPreferredWidth(30);

            } else {
                rbDabangiao.setEnabled(false);
                rbChuabangiao.setEnabled(false);
                rbTatcabangiao.setEnabled(false);
                ckTimtheonv.setText("Tìm theo khách hàng");
                lblNhapmaTV_thongke.setText("Nhập mã khách hàng");
                model.setColumnCount(0);
                model.addColumn("#");
                model.addColumn("Mã HĐ");
                model.addColumn("Tổng tiền");
                model.addColumn("Người nhận hàng");
                model.addColumn("Số điện thoại");
                model.addColumn("Địa chỉ");
                model.addColumn("PT thanh toán");
                model.addColumn("TG đặt");
                model.addColumn("TG hoàn thành");
                model.addColumn("Mã khách hàng");
                model.addColumn("Tên khách hàng");
                tblHoadon_Thongke.getColumnModel().getColumn(0).setPreferredWidth(30);
            }
        } catch (Exception e) {
            System.out.println("loi chonHoaDon_Thongke " + e);
        }
    }

    private void chiTietHoaDon_Thongke() {
        try {
            int si = tblHoadon_Thongke.getSelectedRow();
            DefaultTableModel model = (DefaultTableModel) tblChitiethoadon_Thongke.getModel();
            model.setRowCount(0);
            if (si > -1) {
                String hdid = tblHoadon_Thongke.getValueAt(si, 1).toString();
                QL_HoaDon qlhd = new QL_HoaDon();
                if (rbHoadonapp.isSelected()) {
                    List<OrderAppDetail> oad = qlhd.layChiTietHoaDonApp(hdid);
                    if (oad.size() > 0) {
                        int i = 1;
                        for (OrderAppDetail e : oad) {
                            model.addRow(new Object[]{i, e.getProductSize().getProduct().getId(),
                                e.getProductSize().getProduct().getName(),
                                e.getQuantity(), formatText((int) e.getPrice()), e.getProductSize().getSize(), formatText((int) e.getTotalAmount())});
                            i++;
                        }
                    }
                } else {
                    List<OrderWebDetail> oad = qlhd.layChiTietHoaDonWeb(hdid);
                    if (oad.size() > 0) {
                        int i = 1;
                        for (OrderWebDetail e : oad) {
                            model.addRow(new Object[]{i, e.getProductSize().getProduct().getId(),
                                e.getProductSize().getProduct().getName(),
                                e.getQuantity(), formatText((int) e.getPrice()),
                                e.getProductSize().getSize(), formatText((int) e.getTotalAmount())});
                            i++;
                        }
                    }
                }

            }
        } catch (Exception e) {
            System.out.println("loi chonHoaDon " + e);
        }
    }

    //</editor-fold>
    public void dangXuat() {
        try {
            if (JOptionPane.showConfirmDialog(null, "Xác nhân đăng xuất ?", "Đăng xuất", 0) == 0) {
                login = new Login();
                this.setVisible(false);
                login.setVisible(true);
            }

        } catch (Exception e) {
            System.out.println("li dang xuat    " + e);
        }
    }

    //CÀI ĐẶT
    private void hienthiCuahang() {
        try {
            Config cf = new Config();
            boolean re = cf.getCuahang();
            if (re) {
                txtTencuahangSett.setText(cf.getTencuahang());
                txtFanpageSett.setText(cf.getFanpage());
                txtDiachiSett.setText(cf.getDiachi());
                txtSdtSett.setText(cf.getSodienthoai());
            }
        } catch (Exception e) {
            System.out.println("loi hienthiCuahang " + e);
        }
    }

    private void caiDatCuaHang() {
        try {
            String tencuahang = txtTencuahangSett.getText();
            String fanpage = txtFanpageSett.getText();
            String diachi = txtDiachiSett.getText();
            String sodienthoai = txtSdtSett.getText();
            if (tencuahang.length() > 0 && fanpage.length() > 0 && diachi.length() > 0 && sodienthoai.length() > 0) {
                Config cf = new Config();
                cf.setCuahang(tencuahang, fanpage, diachi, sodienthoai);
                JOptionPane.showMessageDialog(null, "Đã cập nhật thông tin cửa hàng !");
            } else {
                JOptionPane.showMessageDialog(null, "Một số trường còn để trống !");
            }
        } catch (Exception e) {
            System.out.println("loi caiDatCuaHang " + e);
        }
    }
//cai dat slide

    private void hienthiSlide() {
        try {
            Config cf = new Config();
            boolean re = cf.getPhanmem();
            if (re) {
                txtSlide1Sett.setText(cf.getSlide1());
                txtSlide1Sett.setText(cf.getSlide2());
                txtSlide1Sett.setText(cf.getSlide3());
                txtSlide1Sett.setText(cf.getSlide4());
            }
        } catch (Exception e) {
            System.out.println("loi hienthiSlide " + e);
        }
    }

    private void caiDatSlide() {
        try {
            String slide1 = txtSlide1Sett.getText();
            String slide2 = txtSlide2Sett.getText();
            String slide3 = txtSlide3Sett.getText();
            String slide4 = txtSlide4Sett.getText();
            if (slide1.length() > 0 && slide2.length() > 0 && slide3.length() > 0 && slide4.length() > 0) {
                Config cf = new Config();
                cf.setPhanmem(slide1, slide2, slide3, slide4);
                JOptionPane.showMessageDialog(null, "Đã lưu thông tin slide đăng nhập!");
            } else {
                JOptionPane.showMessageDialog(null, "Một số trường còn để trống !");
            }
        } catch (Exception e) {
            System.out.println("loi caiDatSlide " + e);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup_LochoadontheoTGIAN = new javax.swing.ButtonGroup();
        buttonGroup_thongKeHoaDon = new javax.swing.ButtonGroup();
        buttonGroup_locHoaDonTheoMocTgian = new javax.swing.ButtonGroup();
        buttonGroup_bangiaohoadon = new javax.swing.ButtonGroup();
        Tab = new javax.swing.JTabbedPane();
        tabBanhang = new javax.swing.JPanel();
        pnlThanhtoan = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        lblTongsohang = new javax.swing.JLabel();
        lblTongtienTT = new javax.swing.JLabel();
        btnThanhtoan = new javax.swing.JButton();
        ckInhoadon = new javax.swing.JCheckBox();
        jPanel1 = new javax.swing.JPanel();
        lblTennv = new javax.swing.JLabel();
        btnDangxuat2 = new javax.swing.JButton();
        pnlTTLeft = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblChitietSP = new javax.swing.JTable();
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
        tabXemhoadon = new javax.swing.JPanel();
        jPanel22 = new javax.swing.JPanel();
        btnBangiaotien = new javax.swing.JButton();
        lblTongtienHD_Xemhd = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tblChitiethoadon = new javax.swing.JTable();
        jLabel52 = new javax.swing.JLabel();
        lblTonghoadon = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        lblTongthu_Xemhoadon = new javax.swing.JLabel();
        jPanel23 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblHoadon_Xemhoadon = new javax.swing.JTable();
        btnInhoadon_Xemhoadon = new javax.swing.JButton();
        jLabel22 = new javax.swing.JLabel();
        txtMahoadon_Xemhoadon = new javax.swing.JTextField();
        btnTkHd_Xemhoadon = new javax.swing.JButton();
        btnDatlai_Xemhoadon = new javax.swing.JButton();
        tabTracuu = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        cboLoai_Tracuu = new javax.swing.JComboBox();
        jLabel18 = new javax.swing.JLabel();
        cboNhanhieu_Tracuu = new javax.swing.JComboBox();
        txtTimkiem_Tracuu = new javax.swing.JTextField();
        btnTimkiem_Tracuu = new javax.swing.JButton();
        jLabel66 = new javax.swing.JLabel();
        cboMau_Tracuu = new javax.swing.JComboBox();
        jLabel67 = new javax.swing.JLabel();
        cboTrangthai_Tracuu = new javax.swing.JComboBox();
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
        jLabel11 = new javax.swing.JLabel();
        lblTensp = new javax.swing.JLabel();
        jLabel43 = new javax.swing.JLabel();
        tabQuanli = new javax.swing.JPanel();
        TabQTV = new javax.swing.JTabbedPane();
        tabSanpham = new javax.swing.JPanel();
        tabQLSP = new javax.swing.JTabbedPane();
        tabCapnhat = new javax.swing.JPanel();
        pnlCapNhatSP = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        txtTenSp_CapnhatSP = new javax.swing.JTextField();
        jLabel38 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jScrollPane7 = new javax.swing.JScrollPane();
        txtMotaSpCapnhat = new javax.swing.JTextArea();
        jLabel35 = new javax.swing.JLabel();
        cboNhanhieu_Capnhat = new javax.swing.JComboBox();
        spnGia_CapnhatSP = new javax.swing.JSpinner();
        jLabel36 = new javax.swing.JLabel();
        cboLoai_Capnhat = new javax.swing.JComboBox();
        jLabel37 = new javax.swing.JLabel();
        cboTrangthaiCapnhat = new javax.swing.JComboBox();
        btnCapnhatSp = new javax.swing.JButton();
        btnXoaSp_vinhvien = new javax.swing.JButton();
        jPanel15 = new javax.swing.JPanel();
        txtTk_CapnhatSP = new javax.swing.JTextField();
        btnTkspQL = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        txtMaSP_CapnhatSP = new javax.swing.JTextField();
        jLabel90 = new javax.swing.JLabel();
        lblTgtaosp = new javax.swing.JLabel();
        jLabel92 = new javax.swing.JLabel();
        lblTgsuasp_Capnhatsp = new javax.swing.JLabel();
        jLabel39 = new javax.swing.JLabel();
        txtPhienban_CapnhatSP = new javax.swing.JTextField();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        tabSizeSoluong_Capnhatsp = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane16 = new javax.swing.JScrollPane();
        tblSize_CapnhatSP = new javax.swing.JTable();
        jPanel13 = new javax.swing.JPanel();
        btnThemSize_CapnhatSP = new javax.swing.JButton();
        jLabel20 = new javax.swing.JLabel();
        spnSoluong_CapnhatSP = new javax.swing.JSpinner();
        btnXoaSize_CapnhatSP = new javax.swing.JButton();
        spnSize_CapnhatSP = new javax.swing.JSpinner();
        jLabel9 = new javax.swing.JLabel();
        btnCapnhatSize_CapnhatSP = new javax.swing.JButton();
        jLabel25 = new javax.swing.JLabel();
        spnSanco_CapnhatSP = new javax.swing.JSpinner();
        tabMau_Capnhatsp = new javax.swing.JPanel();
        jScrollPane15 = new javax.swing.JScrollPane();
        tblMau_CapnhatSP = new javax.swing.JTable();
        jPanel17 = new javax.swing.JPanel();
        btnThemmau_CapnhatSP = new javax.swing.JButton();
        btnXoamau_CapnhatSP = new javax.swing.JButton();
        jLabel30 = new javax.swing.JLabel();
        cboMau_Capnhat = new javax.swing.JComboBox();
        btnCapnhatMau_CapnhatSP = new javax.swing.JButton();
        tabHinhanh_Capnhatsp = new javax.swing.JPanel();
        jScrollPane12 = new javax.swing.JScrollPane();
        tblAnh_CapnhatSP = new javax.swing.JTable();
        jPanel18 = new javax.swing.JPanel();
        jLabel69 = new javax.swing.JLabel();
        txtLinkanh_CapnhatSP = new javax.swing.JTextField();
        btnChonfileAnh_CapnhatSP = new javax.swing.JButton();
        btnThemAnh_CapnhatSP = new javax.swing.JButton();
        btnXoaanh_Capnhatsp = new javax.swing.JButton();
        btnDatlamanhchinh_Capnhatsp = new javax.swing.JButton();
        btnXemanh = new javax.swing.JButton();
        btnCapnhatanh = new javax.swing.JButton();
        tabThem = new javax.swing.JPanel();
        pnlCapNhatSP4 = new javax.swing.JPanel();
        jLabel80 = new javax.swing.JLabel();
        txtTen_Themsp = new javax.swing.JTextField();
        jLabel83 = new javax.swing.JLabel();
        jLabel84 = new javax.swing.JLabel();
        jScrollPane13 = new javax.swing.JScrollPane();
        txtMota_Themsp = new javax.swing.JTextArea();
        jLabel85 = new javax.swing.JLabel();
        cboNhanhieu_Themsp = new javax.swing.JComboBox();
        spnGia_Themsp = new javax.swing.JSpinner();
        jLabel86 = new javax.swing.JLabel();
        cboLoai_Themsp = new javax.swing.JComboBox();
        jLabel87 = new javax.swing.JLabel();
        cboTrangthai_Themsp = new javax.swing.JComboBox();
        jPanel4 = new javax.swing.JPanel();
        jLabel28 = new javax.swing.JLabel();
        spnSize_Themsp = new javax.swing.JSpinner();
        spnSoluong_Themsp = new javax.swing.JSpinner();
        jLabel29 = new javax.swing.JLabel();
        btnThemSize_Themsp = new javax.swing.JButton();
        btnXoaSize_Themsp = new javax.swing.JButton();
        jScrollPane14 = new javax.swing.JScrollPane();
        tblSize_Themsp = new javax.swing.JTable();
        jPanel10 = new javax.swing.JPanel();
        jLabel31 = new javax.swing.JLabel();
        btnThemMau_Themsp = new javax.swing.JButton();
        btnXoaMau_Themsp = new javax.swing.JButton();
        jScrollPane17 = new javax.swing.JScrollPane();
        tblMau_Themsp = new javax.swing.JTable();
        cboMau_Themsp = new javax.swing.JComboBox();
        jPanel21 = new javax.swing.JPanel();
        jLabel34 = new javax.swing.JLabel();
        btnThemAnh_Themsp = new javax.swing.JButton();
        btnXoaAnh_Themsp = new javax.swing.JButton();
        jScrollPane18 = new javax.swing.JScrollPane();
        tblAnh_Themsp = new javax.swing.JTable();
        txtLink_Themsp = new javax.swing.JTextField();
        btnFile_Themsp = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jLabel81 = new javax.swing.JLabel();
        txtPhienban_Themsp = new javax.swing.JTextField();
        tabChitiet = new javax.swing.JPanel();
        tabTabChitiet = new javax.swing.JTabbedPane();
        tabNhanhieu = new javax.swing.JPanel();
        jPanel26 = new javax.swing.JPanel();
        txtTennhanhhieu = new javax.swing.JTextField();
        btnThemnhanhieu = new javax.swing.JButton();
        btnXoanhanhieu = new javax.swing.JButton();
        txtTenNhanhieumoi = new javax.swing.JTextField();
        btnDoitenNhanhieu = new javax.swing.JButton();
        jLabel45 = new javax.swing.JLabel();
        jPanel28 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        tblNhanhieu = new javax.swing.JTable();
        tabLoai = new javax.swing.JPanel();
        jPanel16 = new javax.swing.JPanel();
        txtTenLoai = new javax.swing.JTextField();
        btnThemLoai = new javax.swing.JButton();
        btnXoaLoai = new javax.swing.JButton();
        txtTenLoaiMoi = new javax.swing.JTextField();
        btnDoitenLoai = new javax.swing.JButton();
        jLabel65 = new javax.swing.JLabel();
        jPanel24 = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        tblLoai = new javax.swing.JTable();
        tabMau = new javax.swing.JPanel();
        jPanel27 = new javax.swing.JPanel();
        txtTenmau = new javax.swing.JTextField();
        btnThemLoai1 = new javax.swing.JButton();
        btnXoaLoai1 = new javax.swing.JButton();
        txtTenmaumoi = new javax.swing.JTextField();
        btnDoitenLoai1 = new javax.swing.JButton();
        jLabel68 = new javax.swing.JLabel();
        jLabel72 = new javax.swing.JLabel();
        txtCodemau = new javax.swing.JTextField();
        jPanel29 = new javax.swing.JPanel();
        jScrollPane19 = new javax.swing.JScrollPane();
        tblMau = new javax.swing.JTable();
        tabThongke = new javax.swing.JPanel();
        pnlTimLoc = new javax.swing.JPanel();
        pnlLoctheoTgian = new javax.swing.JPanel();
        jLabel24 = new javax.swing.JLabel();
        tg1_thongke = new com.toedter.calendar.JDateChooser();
        jLabel23 = new javax.swing.JLabel();
        tg2_thongke = new com.toedter.calendar.JDateChooser();
        rbMoctg = new javax.swing.JRadioButton();
        tg3_thongke = new com.toedter.calendar.JDateChooser();
        rbKhoangtg = new javax.swing.JRadioButton();
        jLabel26 = new javax.swing.JLabel();
        rbNgay_thongke = new javax.swing.JRadioButton();
        rbThang_thongke = new javax.swing.JRadioButton();
        rbNam_thongke = new javax.swing.JRadioButton();
        ckTimtheosp = new javax.swing.JCheckBox();
        ckTimtheotg = new javax.swing.JCheckBox();
        jPanel14 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        txtMasp_thongke = new javax.swing.JTextField();
        ckTimtheonv = new javax.swing.JCheckBox();
        jPanel19 = new javax.swing.JPanel();
        lblNhapmaTV_thongke = new javax.swing.JLabel();
        txtManv_thongke = new javax.swing.JTextField();
        btnReset_thongke = new javax.swing.JButton();
        rbHoadonapp = new javax.swing.JRadioButton();
        rbHoadonweb = new javax.swing.JRadioButton();
        btnTimhoadon_thongke = new javax.swing.JButton();
        rbChuabangiao = new javax.swing.JRadioButton();
        rbDabangiao = new javax.swing.JRadioButton();
        rbTatcabangiao = new javax.swing.JRadioButton();
        jScrollPane10 = new javax.swing.JScrollPane();
        tblHoadon_Thongke = new javax.swing.JTable();
        jScrollPane11 = new javax.swing.JScrollPane();
        tblChitiethoadon_Thongke = new javax.swing.JTable();
        jPanel20 = new javax.swing.JPanel();
        jLabel27 = new javax.swing.JLabel();
        jLabel53 = new javax.swing.JLabel();
        lblTonghoadon_Thongke = new javax.swing.JLabel();
        lblThongthu_Thongke = new javax.swing.JLabel();
        jLabel63 = new javax.swing.JLabel();
        lblTongsanpham_thongke = new javax.swing.JLabel();
        tabThanhvien = new javax.swing.JPanel();
        jScrollPane9 = new javax.swing.JScrollPane();
        tblTV = new javax.swing.JTable();
        jPanel6 = new javax.swing.JPanel();
        tabSetThanhvien = new javax.swing.JTabbedPane();
        tabSuaThanhvien = new javax.swing.JPanel();
        btnCapnhat_Suatv = new javax.swing.JButton();
        jLabel47 = new javax.swing.JLabel();
        cboChucvu_Suatv = new javax.swing.JComboBox();
        txtTen_Suatv = new javax.swing.JTextField();
        jLabel32 = new javax.swing.JLabel();
        jLabel54 = new javax.swing.JLabel();
        txtSdt_Suatv = new javax.swing.JTextField();
        jLabel55 = new javax.swing.JLabel();
        jLabel57 = new javax.swing.JLabel();
        txtEmail_Suatv = new javax.swing.JTextField();
        jLabel59 = new javax.swing.JLabel();
        jdNgaysinh_Suatv = new com.toedter.calendar.JDateChooser();
        jLabel73 = new javax.swing.JLabel();
        cboGioitinh_Suatv = new javax.swing.JComboBox();
        btnXoa_Suatv = new javax.swing.JButton();
        lblDoimk_Suatv = new javax.swing.JLabel();
        jLabel75 = new javax.swing.JLabel();
        txtAnh_Suatv = new javax.swing.JTextField();
        btnChonfile_Suatv = new javax.swing.JButton();
        btnXemanh_Suatv = new javax.swing.JButton();
        jScrollPane20 = new javax.swing.JScrollPane();
        txtDiachi_Suatv = new javax.swing.JTextArea();
        tabThemThanhvien = new javax.swing.JPanel();
        jLabel76 = new javax.swing.JLabel();
        txtTen_Themtv = new javax.swing.JTextField();
        jLabel77 = new javax.swing.JLabel();
        txtEmail_Themtv = new javax.swing.JTextField();
        jLabel78 = new javax.swing.JLabel();
        cboGioitinh_Themtv = new javax.swing.JComboBox();
        btnCapnhatTV1 = new javax.swing.JButton();
        jLabel79 = new javax.swing.JLabel();
        cboChucvu_Themtv = new javax.swing.JComboBox();
        jLabel82 = new javax.swing.JLabel();
        txtSdt_Themtv = new javax.swing.JTextField();
        jLabel88 = new javax.swing.JLabel();
        jLabel89 = new javax.swing.JLabel();
        jdNgaysinh_Themtv = new com.toedter.calendar.JDateChooser();
        btnLoadFile_Themtv = new javax.swing.JButton();
        txtAnh_Themtv = new javax.swing.JTextField();
        jLabel91 = new javax.swing.JLabel();
        jScrollPane21 = new javax.swing.JScrollPane();
        txtDiachi_Themtv = new javax.swing.JTextArea();
        jPanel7 = new javax.swing.JPanel();
        btnTimTV = new javax.swing.JButton();
        txtTimthanhvien_QLTV = new javax.swing.JTextField();
        cboChucvuTV = new javax.swing.JComboBox();
        jLabel49 = new javax.swing.JLabel();
        jLabel56 = new javax.swing.JLabel();
        jdDataoTV = new com.toedter.calendar.JDateChooser();
        jLabel74 = new javax.swing.JLabel();
        jdDataoTV2 = new com.toedter.calendar.JDateChooser();
        tabCaidat = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jLabel42 = new javax.swing.JLabel();
        txtSdtSett = new javax.swing.JTextField();
        jLabel33 = new javax.swing.JLabel();
        txtTencuahangSett = new javax.swing.JTextField();
        jLabel40 = new javax.swing.JLabel();
        txtFanpageSett = new javax.swing.JTextField();
        txtDiachiSett = new javax.swing.JTextField();
        jLabel41 = new javax.swing.JLabel();
        btnCapnhatSett = new javax.swing.JButton();
        jPanel11 = new javax.swing.JPanel();
        btnCapnhatSlideSett = new javax.swing.JButton();
        jLabel48 = new javax.swing.JLabel();
        jLabel44 = new javax.swing.JLabel();
        jLabel50 = new javax.swing.JLabel();
        txtSlide1Sett = new javax.swing.JTextField();
        jLabel46 = new javax.swing.JLabel();
        txtSlide2Sett = new javax.swing.JTextField();
        txtSlide4Sett = new javax.swing.JTextField();
        txtSlide3Sett = new javax.swing.JTextField();
        jButton8 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        jButton11 = new javax.swing.JButton();
        btnDangxuatSett = new javax.swing.JButton();
        jPanel25 = new javax.swing.JPanel();
        btnCapnhatSlideSett1 = new javax.swing.JButton();
        jCheckBox4 = new javax.swing.JCheckBox();
        txtSlide2Sett1 = new javax.swing.JTextField();
        jButton15 = new javax.swing.JButton();
        jCheckBox5 = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setSize(new java.awt.Dimension(1080, 720));

        Tab.setBackground(new java.awt.Color(204, 204, 204));
        Tab.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        Tab.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N

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
            .addComponent(lblTennv, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(42, 42, 42)
                .addComponent(lblTennv, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(44, Short.MAX_VALUE))
        );

        btnDangxuat2.setBackground(new java.awt.Color(255, 51, 51));
        btnDangxuat2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnDangxuat2.setText("Đăng xuất");
        btnDangxuat2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDangxuat2ActionPerformed(evt);
            }
        });

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
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 188, Short.MAX_VALUE)
                        .addComponent(lblTongsohang, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlThanhtoanLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(ckInhoadon, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(pnlThanhtoanLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(btnThanhtoan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(pnlThanhtoanLayout.createSequentialGroup()
                .addGap(114, 114, 114)
                .addComponent(btnDangxuat2, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlThanhtoanLayout.setVerticalGroup(
            pnlThanhtoanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlThanhtoanLayout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(pnlThanhtoanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(lblTongsohang))
                .addGap(49, 49, 49)
                .addComponent(lblTongtienTT, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(55, 55, 55)
                .addComponent(ckInhoadon)
                .addGap(36, 36, 36)
                .addComponent(btnThanhtoan, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnDangxuat2, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pnlTTLeft.setBackground(new java.awt.Color(204, 204, 204));
        pnlTTLeft.setPreferredSize(new java.awt.Dimension(877, 630));

        tblChitietSP.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        tblChitietSP.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "#", "Mã sản phẩm", "Tên sản phẩm", "Số lượng", "Size", "Đơn giá", "Thành tiền"
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

        btnThem.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnThem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/add_25px.png"))); // NOI18N
        btnThem.setText("Thêm/Sửa");
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
                .addContainerGap(172, Short.MAX_VALUE))
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
                    .addComponent(spnSoluongSpTT)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlTimkiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(cboSizeTT, javax.swing.GroupLayout.DEFAULT_SIZE, 24, Short.MAX_VALUE)
                        .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jLabel19, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblSancoTT, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.GroupLayout pnlTTLeftLayout = new javax.swing.GroupLayout(pnlTTLeft);
        pnlTTLeft.setLayout(pnlTTLeftLayout);
        pnlTTLeftLayout.setHorizontalGroup(
            pnlTTLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
            .addComponent(pnlTimkiem, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        pnlTTLeftLayout.setVerticalGroup(
            pnlTTLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTTLeftLayout.createSequentialGroup()
                .addComponent(pnlTimkiem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 516, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout tabBanhangLayout = new javax.swing.GroupLayout(tabBanhang);
        tabBanhang.setLayout(tabBanhangLayout);
        tabBanhangLayout.setHorizontalGroup(
            tabBanhangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tabBanhangLayout.createSequentialGroup()
                .addComponent(pnlTTLeft, javax.swing.GroupLayout.DEFAULT_SIZE, 933, Short.MAX_VALUE)
                .addGap(5, 5, 5)
                .addComponent(pnlThanhtoan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        tabBanhangLayout.setVerticalGroup(
            tabBanhangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlTTLeft, javax.swing.GroupLayout.DEFAULT_SIZE, 644, Short.MAX_VALUE)
            .addComponent(pnlThanhtoan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        Tab.addTab("Bán hàng", new javax.swing.ImageIcon(getClass().getResource("/Images/shop.png")), tabBanhang); // NOI18N

        tabXemhoadon.setBackground(new java.awt.Color(255, 204, 0));
        tabXemhoadon.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        tabXemhoadon.setPreferredSize(new java.awt.Dimension(1312, 630));

        btnBangiaotien.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        btnBangiaotien.setText("BÀN GIAO TIỀN");
        btnBangiaotien.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBangiaotienActionPerformed(evt);
            }
        });

        lblTongtienHD_Xemhd.setFont(new java.awt.Font("Tahoma", 1, 20)); // NOI18N
        lblTongtienHD_Xemhd.setForeground(new java.awt.Color(255, 0, 51));
        lblTongtienHD_Xemhd.setText("10 000 000 đ");

        jLabel21.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel21.setText("Tổng tiền:");

        tblChitiethoadon.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "#", "Mã sản phẩm", "Tên sản phẩm", "Số lượng", "Giá", "Size", "Thành tiền"
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

        jLabel52.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel52.setText("Tổng hóa đơn:");

        lblTonghoadon.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblTonghoadon.setForeground(new java.awt.Color(255, 51, 51));
        lblTonghoadon.setText("100");

        jLabel15.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel15.setText("Tổng thu:");

        lblTongthu_Xemhoadon.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblTongthu_Xemhoadon.setForeground(new java.awt.Color(255, 51, 51));
        lblTongthu_Xemhoadon.setText("10.000");

        javax.swing.GroupLayout jPanel22Layout = new javax.swing.GroupLayout(jPanel22);
        jPanel22.setLayout(jPanel22Layout);
        jPanel22Layout.setHorizontalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel22Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel22Layout.createSequentialGroup()
                        .addComponent(jLabel52)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblTonghoadon, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(88, 88, 88)
                        .addComponent(jLabel15)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblTongthu_Xemhoadon, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 94, Short.MAX_VALUE))
                    .addGroup(jPanel22Layout.createSequentialGroup()
                        .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblTongtienHD_Xemhd, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnBangiaotien, javax.swing.GroupLayout.PREFERRED_SIZE, 259, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
            .addComponent(jScrollPane4, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        jPanel22Layout.setVerticalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel22Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel52)
                    .addComponent(lblTonghoadon)
                    .addComponent(jLabel15)
                    .addComponent(lblTongthu_Xemhoadon))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 473, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel22Layout.createSequentialGroup()
                        .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblTongtienHD_Xemhd, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnBangiaotien, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(21, 21, 21))
        );

        jLabel52.getAccessibleContext().setAccessibleName("Tổng hóa đơn");

        jPanel23.setPreferredSize(new java.awt.Dimension(566, 630));

        tblHoadon_Xemhoadon.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        tblHoadon_Xemhoadon.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "#", "Mã hóa đơn", "Tổng tiền", "Thời gian"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblHoadon_Xemhoadon.setRowHeight(30);
        tblHoadon_Xemhoadon.setSelectionBackground(new java.awt.Color(204, 255, 255));
        tblHoadon_Xemhoadon.setShowVerticalLines(false);
        tblHoadon_Xemhoadon.getTableHeader().setReorderingAllowed(false);
        tblHoadon_Xemhoadon.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblHoadon_XemhoadonMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tblHoadon_Xemhoadon);
        if (tblHoadon_Xemhoadon.getColumnModel().getColumnCount() > 0) {
            tblHoadon_Xemhoadon.getColumnModel().getColumn(0).setMinWidth(40);
            tblHoadon_Xemhoadon.getColumnModel().getColumn(0).setPreferredWidth(40);
            tblHoadon_Xemhoadon.getColumnModel().getColumn(0).setMaxWidth(40);
            tblHoadon_Xemhoadon.getColumnModel().getColumn(1).setMinWidth(150);
            tblHoadon_Xemhoadon.getColumnModel().getColumn(1).setPreferredWidth(150);
            tblHoadon_Xemhoadon.getColumnModel().getColumn(1).setMaxWidth(150);
            tblHoadon_Xemhoadon.getColumnModel().getColumn(2).setMinWidth(150);
            tblHoadon_Xemhoadon.getColumnModel().getColumn(2).setPreferredWidth(150);
            tblHoadon_Xemhoadon.getColumnModel().getColumn(2).setMaxWidth(150);
            tblHoadon_Xemhoadon.getColumnModel().getColumn(3).setResizable(false);
        }

        btnInhoadon_Xemhoadon.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnInhoadon_Xemhoadon.setText("In");
        btnInhoadon_Xemhoadon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInhoadon_XemhoadonActionPerformed(evt);
            }
        });

        jLabel22.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel22.setText("Nhập mã");

        btnTkHd_Xemhoadon.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnTkHd_Xemhoadon.setText("Tìm");
        btnTkHd_Xemhoadon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTkHd_XemhoadonActionPerformed(evt);
            }
        });

        btnDatlai_Xemhoadon.setText("Đặt lại");
        btnDatlai_Xemhoadon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDatlai_XemhoadonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel23Layout = new javax.swing.GroupLayout(jPanel23);
        jPanel23.setLayout(jPanel23Layout);
        jPanel23Layout.setHorizontalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel23Layout.createSequentialGroup()
                .addComponent(btnInhoadon_Xemhoadon, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnDatlai_Xemhoadon)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel22)
                .addGap(18, 18, 18)
                .addComponent(txtMahoadon_Xemhoadon, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnTkHd_Xemhoadon)
                .addGap(21, 21, 21))
            .addGroup(jPanel23Layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 550, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel23Layout.setVerticalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel23Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel23Layout.createSequentialGroup()
                        .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnInhoadon_Xemhoadon, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(txtMahoadon_Xemhoadon, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnTkHd_Xemhoadon, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnDatlai_Xemhoadon, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 595, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout tabXemhoadonLayout = new javax.swing.GroupLayout(tabXemhoadon);
        tabXemhoadon.setLayout(tabXemhoadonLayout);
        tabXemhoadonLayout.setHorizontalGroup(
            tabXemhoadonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tabXemhoadonLayout.createSequentialGroup()
                .addComponent(jPanel23, javax.swing.GroupLayout.PREFERRED_SIZE, 560, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel22, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        tabXemhoadonLayout.setVerticalGroup(
            tabXemhoadonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel23, javax.swing.GroupLayout.DEFAULT_SIZE, 644, Short.MAX_VALUE)
            .addComponent(jPanel22, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        Tab.addTab("Xem hóa đơn", new javax.swing.ImageIcon(getClass().getResource("/Images/bill_40px.png")), tabXemhoadon); // NOI18N

        jPanel2.setBackground(new java.awt.Color(204, 204, 204));

        jLabel17.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel17.setText("Loại");

        cboLoai_Tracuu.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N

        jLabel18.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel18.setText("Nhãn hiệu");

        cboNhanhieu_Tracuu.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N

        btnTimkiem_Tracuu.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnTimkiem_Tracuu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/search_25px.png"))); // NOI18N
        btnTimkiem_Tracuu.setText("Tìm");
        btnTimkiem_Tracuu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTimkiem_TracuuActionPerformed(evt);
            }
        });

        jLabel66.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel66.setText("Màu");

        cboMau_Tracuu.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N

        jLabel67.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel67.setText("Trạng thái");

        cboTrangthai_Tracuu.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cboTrangthai_Tracuu.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tất cả", "Hết hàng", "Đang bán", "Sắp có", "Ẩn trên webshop", "Ngừng kinh doanh" }));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel17)
                .addGap(12, 12, 12)
                .addComponent(cboLoai_Tracuu, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel18)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cboNhanhieu_Tracuu, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel66)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cboMau_Tracuu, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel67)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cboTrangthai_Tracuu, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(txtTimkiem_Tracuu, javax.swing.GroupLayout.PREFERRED_SIZE, 219, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnTimkiem_Tracuu)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnTimkiem_Tracuu, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(cboNhanhieu_Tracuu, javax.swing.GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE)
                    .addComponent(jLabel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtTimkiem_Tracuu, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(cboLoai_Tracuu, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cboMau_Tracuu, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel66, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cboTrangthai_Tracuu, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel67, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        tblTracuu.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "#", "Mã sản phẩm", "Tên sản phẩm", "Giá", "Phiên bản", "Thời gian tạo", "Thời gian sửa"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblTracuu.setRowHeight(25);
        tblTracuu.setSelectionForeground(new java.awt.Color(0, 120, 215));
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

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel11.setText("Tên sản phẩm:");

        lblTensp.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblTensp.setText(" ");

        jLabel43.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel43.setText("Size và số lượng");

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
                        .addComponent(lblLoai, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addComponent(jLabel11)
                        .addGap(18, 18, 18)
                        .addComponent(lblTensp, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addComponent(jLabel43)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(lblTensp))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel43)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3))
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jScrollPane8, javax.swing.GroupLayout.DEFAULT_SIZE, 906, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane8, javax.swing.GroupLayout.DEFAULT_SIZE, 578, Short.MAX_VALUE)
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

        Tab.addTab("Tra cứu", new javax.swing.ImageIcon(getClass().getResource("/Images/search_40px.png")), tabTracuu); // NOI18N

        TabQTV.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N

        tabSanpham.setBackground(new java.awt.Color(204, 204, 204));

        tabQLSP.setBackground(new java.awt.Color(255, 255, 255));
        tabQLSP.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        tabQLSP.setPreferredSize(new java.awt.Dimension(363, 789));

        tabCapnhat.setPreferredSize(new java.awt.Dimension(363, 789));

        pnlCapNhatSP.setBackground(new java.awt.Color(204, 204, 204));

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel12.setText("Tên");

        txtTenSp_CapnhatSP.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        jLabel38.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel38.setText("Mô tả");

        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel13.setText("Giá");

        txtMotaSpCapnhat.setColumns(20);
        txtMotaSpCapnhat.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtMotaSpCapnhat.setRows(5);
        jScrollPane7.setViewportView(txtMotaSpCapnhat);

        jLabel35.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel35.setText("Nhãn hiệu");

        cboNhanhieu_Capnhat.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N

        spnGia_CapnhatSP.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        spnGia_CapnhatSP.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(0), Integer.valueOf(0), null, Integer.valueOf(1)));

        jLabel36.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel36.setText("Loại");

        cboLoai_Capnhat.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N

        jLabel37.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel37.setText("Trạng thái");

        cboTrangthaiCapnhat.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cboTrangthaiCapnhat.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Hết hàng", "Đang bán", "Sắp có", "Ẩn trên webshop", "Ngừng kinh doanh" }));

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

        btnXoaSp_vinhvien.setBackground(new java.awt.Color(204, 204, 204));
        btnXoaSp_vinhvien.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnXoaSp_vinhvien.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/delete_16px.png"))); // NOI18N
        btnXoaSp_vinhvien.setText("  Xóa");
        btnXoaSp_vinhvien.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaSp_vinhvienActionPerformed(evt);
            }
        });

        jPanel15.setBackground(new java.awt.Color(204, 204, 255));

        btnTkspQL.setBackground(new java.awt.Color(204, 204, 204));
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
                .addComponent(txtTk_CapnhatSP, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnTkspQL)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnTkspQL, javax.swing.GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtTk_CapnhatSP, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jLabel14.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel14.setText("Mã");

        txtMaSP_CapnhatSP.setEditable(false);
        txtMaSP_CapnhatSP.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        jLabel90.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel90.setText("Thời gian tạo:");

        lblTgtaosp.setFont(new java.awt.Font("Tahoma", 3, 12)); // NOI18N
        lblTgtaosp.setText("yyyy-mm-dd HH:mm:sss");

        jLabel92.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel92.setText("Thời gian sửa:");

        lblTgsuasp_Capnhatsp.setFont(new java.awt.Font("Tahoma", 3, 12)); // NOI18N
        lblTgsuasp_Capnhatsp.setText("yyyy-mm-dd HH:mm:sss");

        jLabel39.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel39.setText("Phiên bản");

        txtPhienban_CapnhatSP.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        javax.swing.GroupLayout pnlCapNhatSPLayout = new javax.swing.GroupLayout(pnlCapNhatSP);
        pnlCapNhatSP.setLayout(pnlCapNhatSPLayout);
        pnlCapNhatSPLayout.setHorizontalGroup(
            pnlCapNhatSPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlCapNhatSPLayout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(pnlCapNhatSPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlCapNhatSPLayout.createSequentialGroup()
                        .addComponent(jLabel39)
                        .addGap(20, 20, 20)
                        .addComponent(txtPhienban_CapnhatSP))
                    .addGroup(pnlCapNhatSPLayout.createSequentialGroup()
                        .addGroup(pnlCapNhatSPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel37, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(pnlCapNhatSPLayout.createSequentialGroup()
                                .addGap(2, 2, 2)
                                .addGroup(pnlCapNhatSPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel35)
                                    .addComponent(jLabel36))))
                        .addGroup(pnlCapNhatSPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlCapNhatSPLayout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addGroup(pnlCapNhatSPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(cboNhanhieu_Capnhat, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(cboLoai_Capnhat, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addGroup(pnlCapNhatSPLayout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(cboTrangthaiCapnhat, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                    .addGroup(pnlCapNhatSPLayout.createSequentialGroup()
                        .addGroup(pnlCapNhatSPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(pnlCapNhatSPLayout.createSequentialGroup()
                                .addComponent(jLabel90)
                                .addGap(18, 18, 18)
                                .addComponent(lblTgtaosp, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(pnlCapNhatSPLayout.createSequentialGroup()
                                .addGroup(pnlCapNhatSPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel13))
                                .addGap(40, 40, 40)
                                .addGroup(pnlCapNhatSPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtTenSp_CapnhatSP, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(txtMaSP_CapnhatSP, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(spnGia_CapnhatSP, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 279, Short.MAX_VALUE)))
                            .addGroup(pnlCapNhatSPLayout.createSequentialGroup()
                                .addComponent(jLabel92)
                                .addGap(18, 18, 18)
                                .addComponent(lblTgsuasp_Capnhatsp, javax.swing.GroupLayout.DEFAULT_SIZE, 256, Short.MAX_VALUE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGroup(pnlCapNhatSPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(pnlCapNhatSPLayout.createSequentialGroup()
                        .addGap(55, 55, 55)
                        .addComponent(jLabel38, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(pnlCapNhatSPLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 47, Short.MAX_VALUE)
                        .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 359, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(25, Short.MAX_VALUE))
                    .addGroup(pnlCapNhatSPLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(pnlCapNhatSPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(btnXoaSp_vinhvien, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnCapnhatSp, javax.swing.GroupLayout.DEFAULT_SIZE, 265, Short.MAX_VALUE))
                        .addGap(74, 74, 74))))
            .addComponent(jPanel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        pnlCapNhatSPLayout.setVerticalGroup(
            pnlCapNhatSPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlCapNhatSPLayout.createSequentialGroup()
                .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addGroup(pnlCapNhatSPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlCapNhatSPLayout.createSequentialGroup()
                        .addGroup(pnlCapNhatSPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtMaSP_CapnhatSP, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(25, 25, 25)
                        .addGroup(pnlCapNhatSPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtTenSp_CapnhatSP, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(25, 25, 25)
                        .addGroup(pnlCapNhatSPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(spnGia_CapnhatSP, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(25, 25, 25)
                        .addGroup(pnlCapNhatSPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel35, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cboNhanhieu_Capnhat, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(25, 25, 25)
                        .addGroup(pnlCapNhatSPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cboLoai_Capnhat, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel36, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(25, 25, 25)
                        .addGroup(pnlCapNhatSPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cboTrangthaiCapnhat, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel37, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(pnlCapNhatSPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel39, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtPhienban_CapnhatSP, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(28, 28, 28)
                        .addGroup(pnlCapNhatSPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel90)
                            .addComponent(lblTgtaosp))
                        .addGap(25, 25, 25)
                        .addGroup(pnlCapNhatSPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel92)
                            .addComponent(lblTgsuasp_Capnhatsp)))
                    .addGroup(pnlCapNhatSPLayout.createSequentialGroup()
                        .addComponent(jLabel38)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 296, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30)
                        .addComponent(btnCapnhatSp, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnXoaSp_vinhvien, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.setBackground(new java.awt.Color(204, 204, 204));
        jTabbedPane1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N

        tblSize_CapnhatSP.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "#", "ID", "Size", "Số lượng", "Ngày tạo", "Ngày sửa"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblSize_CapnhatSP.getTableHeader().setReorderingAllowed(false);
        tblSize_CapnhatSP.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblSize_CapnhatSPMouseClicked(evt);
            }
        });
        jScrollPane16.setViewportView(tblSize_CapnhatSP);
        if (tblSize_CapnhatSP.getColumnModel().getColumnCount() > 0) {
            tblSize_CapnhatSP.getColumnModel().getColumn(0).setMinWidth(30);
            tblSize_CapnhatSP.getColumnModel().getColumn(0).setPreferredWidth(30);
            tblSize_CapnhatSP.getColumnModel().getColumn(0).setMaxWidth(30);
            tblSize_CapnhatSP.getColumnModel().getColumn(2).setPreferredWidth(80);
            tblSize_CapnhatSP.getColumnModel().getColumn(2).setMaxWidth(80);
        }

        jPanel13.setBackground(new java.awt.Color(204, 204, 204));

        btnThemSize_CapnhatSP.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnThemSize_CapnhatSP.setText("Thêm Size");
        btnThemSize_CapnhatSP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemSize_CapnhatSPActionPerformed(evt);
            }
        });

        jLabel20.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel20.setText("Số lượng");

        spnSoluong_CapnhatSP.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        spnSoluong_CapnhatSP.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(0), Integer.valueOf(0), null, Integer.valueOf(1)));

        btnXoaSize_CapnhatSP.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnXoaSize_CapnhatSP.setText("Xóa Size");
        btnXoaSize_CapnhatSP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaSize_CapnhatSPActionPerformed(evt);
            }
        });

        spnSize_CapnhatSP.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        spnSize_CapnhatSP.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(1), Integer.valueOf(1), null, Integer.valueOf(1)));

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel9.setText("Size");

        btnCapnhatSize_CapnhatSP.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnCapnhatSize_CapnhatSP.setText("Cập nhật");
        btnCapnhatSize_CapnhatSP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCapnhatSize_CapnhatSPActionPerformed(evt);
            }
        });

        jLabel25.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel25.setText("Sẵn có");

        spnSanco_CapnhatSP.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(0), Integer.valueOf(0), null, Integer.valueOf(1)));

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addComponent(jLabel25)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(spnSanco_CapnhatSP))
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addGap(28, 28, 28)
                        .addComponent(spnSize_CapnhatSP, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addComponent(jLabel20)
                        .addGap(18, 18, 18)
                        .addComponent(spnSoluong_CapnhatSP)
                        .addGap(18, 18, 18)
                        .addComponent(btnThemSize_CapnhatSP)
                        .addGap(18, 18, 18))
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(btnCapnhatSize_CapnhatSP, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnXoaSize_CapnhatSP, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(spnSize_CapnhatSP, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(spnSoluong_CapnhatSP, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnThemSize_CapnhatSP, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(spnSanco_CapnhatSP, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCapnhatSize_CapnhatSP, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnXoaSize_CapnhatSP, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 7, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane16, javax.swing.GroupLayout.DEFAULT_SIZE, 509, Short.MAX_VALUE)
                    .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane16, javax.swing.GroupLayout.DEFAULT_SIZE, 463, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout tabSizeSoluong_CapnhatspLayout = new javax.swing.GroupLayout(tabSizeSoluong_Capnhatsp);
        tabSizeSoluong_Capnhatsp.setLayout(tabSizeSoluong_CapnhatspLayout);
        tabSizeSoluong_CapnhatspLayout.setHorizontalGroup(
            tabSizeSoluong_CapnhatspLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        tabSizeSoluong_CapnhatspLayout.setVerticalGroup(
            tabSizeSoluong_CapnhatspLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jTabbedPane1.addTab("Size & Số lượng", tabSizeSoluong_Capnhatsp);

        tblMau_CapnhatSP.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "#", "ID", "Tên", "Code", "Ngày tạo", "Ngày sửa"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblMau_CapnhatSP.getTableHeader().setReorderingAllowed(false);
        jScrollPane15.setViewportView(tblMau_CapnhatSP);
        if (tblMau_CapnhatSP.getColumnModel().getColumnCount() > 0) {
            tblMau_CapnhatSP.getColumnModel().getColumn(0).setMinWidth(30);
            tblMau_CapnhatSP.getColumnModel().getColumn(0).setPreferredWidth(30);
            tblMau_CapnhatSP.getColumnModel().getColumn(0).setMaxWidth(30);
            tblMau_CapnhatSP.getColumnModel().getColumn(2).setPreferredWidth(80);
            tblMau_CapnhatSP.getColumnModel().getColumn(2).setMaxWidth(80);
        }

        jPanel17.setBackground(new java.awt.Color(204, 204, 204));

        btnThemmau_CapnhatSP.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnThemmau_CapnhatSP.setText("Thêm màu");
        btnThemmau_CapnhatSP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemmau_CapnhatSPActionPerformed(evt);
            }
        });

        btnXoamau_CapnhatSP.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnXoamau_CapnhatSP.setText("Xóa màu");
        btnXoamau_CapnhatSP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoamau_CapnhatSPActionPerformed(evt);
            }
        });

        jLabel30.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel30.setText("Chọn màu:");

        cboMau_Capnhat.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N

        btnCapnhatMau_CapnhatSP.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnCapnhatMau_CapnhatSP.setText("Cập nhật");
        btnCapnhatMau_CapnhatSP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCapnhatMau_CapnhatSPActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel30)
                .addGap(18, 18, 18)
                .addComponent(cboMau_Capnhat, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnCapnhatMau_CapnhatSP)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnThemmau_CapnhatSP)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnXoamau_CapnhatSP, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cboMau_Capnhat, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel30, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnThemmau_CapnhatSP, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnXoamau_CapnhatSP, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnCapnhatMau_CapnhatSP, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(13, 13, 13))
        );

        javax.swing.GroupLayout tabMau_CapnhatspLayout = new javax.swing.GroupLayout(tabMau_Capnhatsp);
        tabMau_Capnhatsp.setLayout(tabMau_CapnhatspLayout);
        tabMau_CapnhatspLayout.setHorizontalGroup(
            tabMau_CapnhatspLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane15, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 509, Short.MAX_VALUE)
        );
        tabMau_CapnhatspLayout.setVerticalGroup(
            tabMau_CapnhatspLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tabMau_CapnhatspLayout.createSequentialGroup()
                .addComponent(jPanel17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane15, javax.swing.GroupLayout.DEFAULT_SIZE, 491, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Màu", tabMau_Capnhatsp);

        tblAnh_CapnhatSP.setBackground(new java.awt.Color(204, 204, 204));
        tblAnh_CapnhatSP.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "#", "ID", "Kích thước", "Tên ảnh", "Ảnh chính", "TG tạo", "TG thêm"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblAnh_CapnhatSP.setRowHeight(25);
        tblAnh_CapnhatSP.setShowVerticalLines(false);
        tblAnh_CapnhatSP.getTableHeader().setReorderingAllowed(false);
        jScrollPane12.setViewportView(tblAnh_CapnhatSP);
        if (tblAnh_CapnhatSP.getColumnModel().getColumnCount() > 0) {
            tblAnh_CapnhatSP.getColumnModel().getColumn(0).setMinWidth(40);
            tblAnh_CapnhatSP.getColumnModel().getColumn(0).setPreferredWidth(40);
            tblAnh_CapnhatSP.getColumnModel().getColumn(0).setMaxWidth(40);
        }

        jPanel18.setBackground(new java.awt.Color(204, 204, 204));

        jLabel69.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel69.setText("Link ảnh");

        btnChonfileAnh_CapnhatSP.setText("Chọn");
        btnChonfileAnh_CapnhatSP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnChonfileAnh_CapnhatSPActionPerformed(evt);
            }
        });

        btnThemAnh_CapnhatSP.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnThemAnh_CapnhatSP.setText("Thêm");
        btnThemAnh_CapnhatSP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemAnh_CapnhatSPActionPerformed(evt);
            }
        });

        btnXoaanh_Capnhatsp.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnXoaanh_Capnhatsp.setText("Xóa");
        btnXoaanh_Capnhatsp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaanh_CapnhatspActionPerformed(evt);
            }
        });

        btnDatlamanhchinh_Capnhatsp.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnDatlamanhchinh_Capnhatsp.setText("Đặt làm ảnh chính");
        btnDatlamanhchinh_Capnhatsp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDatlamanhchinh_CapnhatspActionPerformed(evt);
            }
        });

        btnXemanh.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnXemanh.setText("Xem ảnh");
        btnXemanh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXemanhActionPerformed(evt);
            }
        });

        btnCapnhatanh.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnCapnhatanh.setText("Cập nhật");
        btnCapnhatanh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCapnhatanhActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel18Layout.createSequentialGroup()
                        .addComponent(btnXemanh)
                        .addGap(18, 18, 18)
                        .addComponent(btnDatlamanhchinh_Capnhatsp)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnXoaanh_Capnhatsp, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel18Layout.createSequentialGroup()
                        .addComponent(jLabel69)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtLinkanh_CapnhatSP, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnChonfileAnh_CapnhatSP, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnThemAnh_CapnhatSP, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCapnhatanh)))
                .addGap(37, 37, 37))
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtLinkanh_CapnhatSP, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnChonfileAnh_CapnhatSP)
                    .addComponent(btnCapnhatanh)
                    .addComponent(btnThemAnh_CapnhatSP, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel69, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(17, 17, 17)
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnDatlamanhchinh_Capnhatsp, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnXoaanh_Capnhatsp, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnXemanh, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout tabHinhanh_CapnhatspLayout = new javax.swing.GroupLayout(tabHinhanh_Capnhatsp);
        tabHinhanh_Capnhatsp.setLayout(tabHinhanh_CapnhatspLayout);
        tabHinhanh_CapnhatspLayout.setHorizontalGroup(
            tabHinhanh_CapnhatspLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel18, javax.swing.GroupLayout.PREFERRED_SIZE, 509, Short.MAX_VALUE)
            .addComponent(jScrollPane12, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        tabHinhanh_CapnhatspLayout.setVerticalGroup(
            tabHinhanh_CapnhatspLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tabHinhanh_CapnhatspLayout.createSequentialGroup()
                .addComponent(jPanel18, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane12, javax.swing.GroupLayout.DEFAULT_SIZE, 458, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Hình ảnh", tabHinhanh_Capnhatsp);

        javax.swing.GroupLayout tabCapnhatLayout = new javax.swing.GroupLayout(tabCapnhat);
        tabCapnhat.setLayout(tabCapnhatLayout);
        tabCapnhatLayout.setHorizontalGroup(
            tabCapnhatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tabCapnhatLayout.createSequentialGroup()
                .addComponent(pnlCapNhatSP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 514, Short.MAX_VALUE)
                .addContainerGap())
        );
        tabCapnhatLayout.setVerticalGroup(
            tabCapnhatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlCapNhatSP, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jTabbedPane1)
        );

        tabQLSP.addTab("Cập nhật", new javax.swing.ImageIcon(getClass().getResource("/Images/available_updates_16px.png")), tabCapnhat); // NOI18N

        tabThem.setPreferredSize(new java.awt.Dimension(363, 789));

        pnlCapNhatSP4.setBackground(new java.awt.Color(204, 204, 204));

        jLabel80.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel80.setText("Tên");

        jLabel83.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel83.setText("Mô tả");

        jLabel84.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel84.setText("Giá");

        txtMota_Themsp.setColumns(20);
        txtMota_Themsp.setRows(5);
        jScrollPane13.setViewportView(txtMota_Themsp);

        jLabel85.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel85.setText("Nhãn hiệu");

        cboNhanhieu_Themsp.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N

        spnGia_Themsp.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        spnGia_Themsp.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(0), Integer.valueOf(0), null, Integer.valueOf(1)));

        jLabel86.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel86.setText("Loại");

        cboLoai_Themsp.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N

        jLabel87.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel87.setText("Trạng thái");

        cboTrangthai_Themsp.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cboTrangthai_Themsp.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Hết hàng", "Đang bán", "Sắp có", "Ẩn trên webshop", "Ngừng kinh doanh" }));

        jPanel4.setBackground(new java.awt.Color(204, 204, 255));
        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Size & Số lượng"));

        jLabel28.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel28.setText("Size");

        spnSize_Themsp.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(0), Integer.valueOf(0), null, Integer.valueOf(1)));

        spnSoluong_Themsp.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(0), Integer.valueOf(0), null, Integer.valueOf(1)));

        jLabel29.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel29.setText("Số lượng");

        btnThemSize_Themsp.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnThemSize_Themsp.setText("Thêm");
        btnThemSize_Themsp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemSize_ThemspActionPerformed(evt);
            }
        });

        btnXoaSize_Themsp.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnXoaSize_Themsp.setText("Xóa");
        btnXoaSize_Themsp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaSize_ThemspActionPerformed(evt);
            }
        });

        tblSize_Themsp.setBackground(new java.awt.Color(102, 255, 255));
        tblSize_Themsp.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "#", "Size", "Số lượng"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblSize_Themsp.setRowHeight(25);
        tblSize_Themsp.setShowVerticalLines(false);
        tblSize_Themsp.getTableHeader().setReorderingAllowed(false);
        jScrollPane14.setViewportView(tblSize_Themsp);
        if (tblSize_Themsp.getColumnModel().getColumnCount() > 0) {
            tblSize_Themsp.getColumnModel().getColumn(0).setPreferredWidth(50);
            tblSize_Themsp.getColumnModel().getColumn(0).setMaxWidth(50);
        }

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel29)
                    .addComponent(jLabel28))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(spnSize_Themsp, javax.swing.GroupLayout.DEFAULT_SIZE, 77, Short.MAX_VALUE)
                    .addComponent(spnSoluong_Themsp))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnThemSize_Themsp)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnXoaSize_Themsp)
                .addContainerGap(14, Short.MAX_VALUE))
            .addComponent(jScrollPane14, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel28)
                    .addComponent(spnSize_Themsp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnThemSize_Themsp)
                        .addComponent(spnSoluong_Themsp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnXoaSize_Themsp))
                    .addComponent(jLabel29, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane14))
        );

        jPanel10.setBackground(new java.awt.Color(204, 204, 255));
        jPanel10.setBorder(javax.swing.BorderFactory.createTitledBorder("Màu"));

        jLabel31.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel31.setText("Chọn màu");

        btnThemMau_Themsp.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnThemMau_Themsp.setText("Thêm");
        btnThemMau_Themsp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemMau_ThemspActionPerformed(evt);
            }
        });

        btnXoaMau_Themsp.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnXoaMau_Themsp.setText("Xóa");
        btnXoaMau_Themsp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaMau_ThemspActionPerformed(evt);
            }
        });

        tblMau_Themsp.setBackground(new java.awt.Color(102, 255, 255));
        tblMau_Themsp.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "#", "ID", "Tên", "Code"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblMau_Themsp.setRowHeight(25);
        tblMau_Themsp.setShowVerticalLines(false);
        tblMau_Themsp.getTableHeader().setReorderingAllowed(false);
        jScrollPane17.setViewportView(tblMau_Themsp);
        if (tblMau_Themsp.getColumnModel().getColumnCount() > 0) {
            tblMau_Themsp.getColumnModel().getColumn(0).setPreferredWidth(50);
            tblMau_Themsp.getColumnModel().getColumn(0).setMaxWidth(50);
        }

        cboMau_Themsp.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane17, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel31)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cboMau_Themsp, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnThemMau_Themsp)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnXoaMau_Themsp)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel31)
                    .addComponent(cboMau_Themsp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnThemMau_Themsp)
                    .addComponent(btnXoaMau_Themsp))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane17, javax.swing.GroupLayout.PREFERRED_SIZE, 507, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel21.setBackground(new java.awt.Color(204, 204, 255));
        jPanel21.setBorder(javax.swing.BorderFactory.createTitledBorder("Hình ảnh"));

        jLabel34.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel34.setText("Link");

        btnThemAnh_Themsp.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnThemAnh_Themsp.setText("Thêm");
        btnThemAnh_Themsp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemAnh_ThemspActionPerformed(evt);
            }
        });

        btnXoaAnh_Themsp.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnXoaAnh_Themsp.setText("Xóa");
        btnXoaAnh_Themsp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaAnh_ThemspActionPerformed(evt);
            }
        });

        tblAnh_Themsp.setBackground(new java.awt.Color(153, 255, 255));
        tblAnh_Themsp.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "#", "Link", "Tên"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblAnh_Themsp.setRowHeight(25);
        tblAnh_Themsp.setShowVerticalLines(false);
        tblAnh_Themsp.getTableHeader().setReorderingAllowed(false);
        jScrollPane18.setViewportView(tblAnh_Themsp);
        if (tblAnh_Themsp.getColumnModel().getColumnCount() > 0) {
            tblAnh_Themsp.getColumnModel().getColumn(0).setPreferredWidth(50);
            tblAnh_Themsp.getColumnModel().getColumn(0).setMaxWidth(50);
        }

        btnFile_Themsp.setText("jButton18");
        btnFile_Themsp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFile_ThemspActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel21Layout = new javax.swing.GroupLayout(jPanel21);
        jPanel21.setLayout(jPanel21Layout);
        jPanel21Layout.setHorizontalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane18, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel21Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel21Layout.createSequentialGroup()
                        .addComponent(jLabel34)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtLink_Themsp)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnFile_Themsp, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel21Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnThemAnh_Themsp)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnXoaAnh_Themsp)))
                .addGap(26, 26, 26))
        );
        jPanel21Layout.setVerticalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel21Layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(txtLink_Themsp))
                    .addComponent(btnFile_Themsp, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel34, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnThemAnh_Themsp)
                    .addComponent(btnXoaAnh_Themsp))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane18, javax.swing.GroupLayout.PREFERRED_SIZE, 348, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jButton3.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jButton3.setText("Thêm sản phẩm");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jLabel81.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel81.setText("Phiên bản");

        javax.swing.GroupLayout pnlCapNhatSP4Layout = new javax.swing.GroupLayout(pnlCapNhatSP4);
        pnlCapNhatSP4.setLayout(pnlCapNhatSP4Layout);
        pnlCapNhatSP4Layout.setHorizontalGroup(
            pnlCapNhatSP4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlCapNhatSP4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlCapNhatSP4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlCapNhatSP4Layout.createSequentialGroup()
                        .addGroup(pnlCapNhatSP4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel85, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel86))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(pnlCapNhatSP4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(cboNhanhieu_Themsp, 0, 248, Short.MAX_VALUE)
                            .addComponent(cboLoai_Themsp, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlCapNhatSP4Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(pnlCapNhatSP4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlCapNhatSP4Layout.createSequentialGroup()
                                .addGroup(pnlCapNhatSP4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jLabel80, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel84, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(62, 62, 62)
                                .addGroup(pnlCapNhatSP4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtTen_Themsp, javax.swing.GroupLayout.DEFAULT_SIZE, 249, Short.MAX_VALUE)
                                    .addComponent(spnGia_Themsp)))
                            .addComponent(txtPhienban_Themsp, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 248, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(pnlCapNhatSP4Layout.createSequentialGroup()
                        .addGroup(pnlCapNhatSP4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel83)
                            .addComponent(jLabel81))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(pnlCapNhatSP4Layout.createSequentialGroup()
                        .addComponent(jLabel87)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 23, Short.MAX_VALUE)
                        .addComponent(cboTrangthai_Themsp, javax.swing.GroupLayout.PREFERRED_SIZE, 248, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane13))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlCapNhatSP4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(pnlCapNhatSP4Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 43, Short.MAX_VALUE)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 254, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(48, 48, 48))))
        );
        pnlCapNhatSP4Layout.setVerticalGroup(
            pnlCapNhatSP4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(pnlCapNhatSP4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlCapNhatSP4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel80, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTen_Themsp, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(pnlCapNhatSP4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel84, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(spnGia_Themsp, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(pnlCapNhatSP4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel81, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtPhienban_Themsp, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(pnlCapNhatSP4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel85, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboNhanhieu_Themsp, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(19, 19, 19)
                .addGroup(pnlCapNhatSP4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cboLoai_Themsp, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel86, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(pnlCapNhatSP4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel87, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboTrangthai_Themsp, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel83, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane13)
                .addContainerGap())
            .addGroup(pnlCapNhatSP4Layout.createSequentialGroup()
                .addComponent(jPanel21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29))
        );

        javax.swing.GroupLayout tabThemLayout = new javax.swing.GroupLayout(tabThem);
        tabThem.setLayout(tabThemLayout);
        tabThemLayout.setHorizontalGroup(
            tabThemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlCapNhatSP4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        tabThemLayout.setVerticalGroup(
            tabThemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlCapNhatSP4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        tabQLSP.addTab("Thêm", new javax.swing.ImageIcon(getClass().getResource("/Images/plus_16px.png")), tabThem); // NOI18N

        javax.swing.GroupLayout tabSanphamLayout = new javax.swing.GroupLayout(tabSanpham);
        tabSanpham.setLayout(tabSanphamLayout);
        tabSanphamLayout.setHorizontalGroup(
            tabSanphamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tabSanphamLayout.createSequentialGroup()
                .addComponent(tabQLSP, javax.swing.GroupLayout.DEFAULT_SIZE, 1351, Short.MAX_VALUE)
                .addContainerGap())
        );
        tabSanphamLayout.setVerticalGroup(
            tabSanphamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, tabSanphamLayout.createSequentialGroup()
                .addComponent(tabQLSP, javax.swing.GroupLayout.PREFERRED_SIZE, 613, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        TabQTV.addTab("Sản phẩm", new javax.swing.ImageIcon(getClass().getResource("/Images/sneakers_16px.png")), tabSanpham); // NOI18N

        tabTabChitiet.setBackground(new java.awt.Color(255, 255, 255));
        tabTabChitiet.setForeground(new java.awt.Color(51, 51, 51));
        tabTabChitiet.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N

        jPanel26.setBackground(new java.awt.Color(153, 153, 255));

        btnThemnhanhieu.setBackground(new java.awt.Color(204, 204, 255));
        btnThemnhanhieu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/plus_16px.png"))); // NOI18N
        btnThemnhanhieu.setText("Thêm nhãn hiệu");
        btnThemnhanhieu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemnhanhieuActionPerformed(evt);
            }
        });

        btnXoanhanhieu.setBackground(new java.awt.Color(204, 204, 255));
        btnXoanhanhieu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/delete_16px.png"))); // NOI18N
        btnXoanhanhieu.setText("Xóa");
        btnXoanhanhieu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoanhanhieuActionPerformed(evt);
            }
        });

        btnDoitenNhanhieu.setBackground(new java.awt.Color(255, 255, 255));
        btnDoitenNhanhieu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/hand_with_pen_16px.png"))); // NOI18N
        btnDoitenNhanhieu.setText("Đổi tên");
        btnDoitenNhanhieu.setName("btnDoitennhanhieu"); // NOI18N
        btnDoitenNhanhieu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDoitenNhanhieuActionPerformed(evt);
            }
        });

        jLabel45.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel45.setText("Nhập tên");

        javax.swing.GroupLayout jPanel26Layout = new javax.swing.GroupLayout(jPanel26);
        jPanel26.setLayout(jPanel26Layout);
        jPanel26Layout.setHorizontalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel26Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel45)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtTennhanhhieu, javax.swing.GroupLayout.PREFERRED_SIZE, 256, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnThemnhanhieu, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnXoanhanhieu)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtTenNhanhieumoi, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnDoitenNhanhieu)
                .addContainerGap(464, Short.MAX_VALUE))
        );
        jPanel26Layout.setVerticalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel26Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel26Layout.createSequentialGroup()
                        .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtTennhanhhieu, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnThemnhanhieu, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnXoanhanhieu, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtTenNhanhieumoi, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnDoitenNhanhieu, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jLabel45, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jPanel28.setBackground(new java.awt.Color(153, 153, 153));

        tblNhanhieu.setBackground(new java.awt.Color(102, 255, 255));
        tblNhanhieu.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "#", "ID", "Tên nhãn hiệu", "Thời gian tạo", "Thời gian sửa"
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

        javax.swing.GroupLayout jPanel28Layout = new javax.swing.GroupLayout(jPanel28);
        jPanel28.setLayout(jPanel28Layout);
        jPanel28Layout.setHorizontalGroup(
            jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane5)
        );
        jPanel28Layout.setVerticalGroup(
            jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 528, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout tabNhanhieuLayout = new javax.swing.GroupLayout(tabNhanhieu);
        tabNhanhieu.setLayout(tabNhanhieuLayout);
        tabNhanhieuLayout.setHorizontalGroup(
            tabNhanhieuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel28, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel26, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        tabNhanhieuLayout.setVerticalGroup(
            tabNhanhieuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tabNhanhieuLayout.createSequentialGroup()
                .addComponent(jPanel26, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel28, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tabTabChitiet.addTab("Nhãn hiệu", new javax.swing.ImageIcon(getClass().getResource("/Images/identification_documents_16px.png")), tabNhanhieu); // NOI18N

        jPanel16.setBackground(new java.awt.Color(153, 153, 255));

        btnThemLoai.setBackground(new java.awt.Color(204, 204, 255));
        btnThemLoai.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/plus_16px.png"))); // NOI18N
        btnThemLoai.setText("Thêm loại");
        btnThemLoai.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemLoaiActionPerformed(evt);
            }
        });

        btnXoaLoai.setBackground(new java.awt.Color(204, 204, 255));
        btnXoaLoai.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/delete_16px.png"))); // NOI18N
        btnXoaLoai.setText(" Xóa");
        btnXoaLoai.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaLoaiActionPerformed(evt);
            }
        });

        btnDoitenLoai.setBackground(new java.awt.Color(255, 255, 255));
        btnDoitenLoai.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/hand_with_pen_16px.png"))); // NOI18N
        btnDoitenLoai.setText("Đổi tên");
        btnDoitenLoai.setName("btnDoitennhanhieu"); // NOI18N
        btnDoitenLoai.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDoitenLoaiActionPerformed(evt);
            }
        });

        jLabel65.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel65.setText("Nhập tên");

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel65)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtTenLoai, javax.swing.GroupLayout.PREFERRED_SIZE, 256, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnThemLoai)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnXoaLoai)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtTenLoaiMoi, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnDoitenLoai)
                .addContainerGap(518, Short.MAX_VALUE))
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel16Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtTenLoai, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnThemLoai, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnXoaLoai, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtTenLoaiMoi, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnDoitenLoai, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel65, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jPanel24.setBackground(new java.awt.Color(153, 153, 153));

        tblLoai.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "#", "ID", "Tên loại", "Thời gian tạo", "Thời gian sửa"
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

        javax.swing.GroupLayout jPanel24Layout = new javax.swing.GroupLayout(jPanel24);
        jPanel24.setLayout(jPanel24Layout);
        jPanel24Layout.setHorizontalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane6, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        jPanel24Layout.setVerticalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 528, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout tabLoaiLayout = new javax.swing.GroupLayout(tabLoai);
        tabLoai.setLayout(tabLoaiLayout);
        tabLoaiLayout.setHorizontalGroup(
            tabLoaiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel24, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        tabLoaiLayout.setVerticalGroup(
            tabLoaiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tabLoaiLayout.createSequentialGroup()
                .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel24, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tabTabChitiet.addTab("Loại", new javax.swing.ImageIcon(getClass().getResource("/Images/elective_16px.png")), tabLoai); // NOI18N

        jPanel27.setBackground(new java.awt.Color(153, 153, 255));

        btnThemLoai1.setBackground(new java.awt.Color(204, 204, 255));
        btnThemLoai1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/plus_16px.png"))); // NOI18N
        btnThemLoai1.setText("Thêm màu");
        btnThemLoai1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemLoai1ActionPerformed(evt);
            }
        });

        btnXoaLoai1.setBackground(new java.awt.Color(204, 204, 255));
        btnXoaLoai1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/delete_16px.png"))); // NOI18N
        btnXoaLoai1.setText(" Xóa");
        btnXoaLoai1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaLoai1ActionPerformed(evt);
            }
        });

        btnDoitenLoai1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/hand_with_pen_16px.png"))); // NOI18N
        btnDoitenLoai1.setText("Đổi tên");
        btnDoitenLoai1.setName("btnDoitennhanhieu"); // NOI18N
        btnDoitenLoai1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDoitenLoai1ActionPerformed(evt);
            }
        });

        jLabel68.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel68.setText("Nhập tên");

        jLabel72.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel72.setText("Nhập code");

        javax.swing.GroupLayout jPanel27Layout = new javax.swing.GroupLayout(jPanel27);
        jPanel27.setLayout(jPanel27Layout);
        jPanel27Layout.setHorizontalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel27Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel68)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtTenmau, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel72)
                .addGap(28, 28, 28)
                .addComponent(txtCodemau, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnThemLoai1)
                .addGap(30, 30, 30)
                .addComponent(btnXoaLoai1)
                .addGap(27, 27, 27)
                .addComponent(txtTenmaumoi, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnDoitenLoai1)
                .addContainerGap(419, Short.MAX_VALUE))
        );
        jPanel27Layout.setVerticalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel27Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel72, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtCodemau, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnThemLoai1, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel68, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel27Layout.createSequentialGroup()
                        .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(txtTenmaumoi, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(btnDoitenLoai1, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(btnXoaLoai1, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(txtTenmau, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        jPanel29.setBackground(new java.awt.Color(153, 153, 153));

        tblMau.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "#", "ID", "Code", "Tên màu", "Thời gian tạo", "Thời gian sửa"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, true, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblMau.setRowHeight(25);
        tblMau.setShowVerticalLines(false);
        tblMau.getTableHeader().setReorderingAllowed(false);
        jScrollPane19.setViewportView(tblMau);
        if (tblMau.getColumnModel().getColumnCount() > 0) {
            tblMau.getColumnModel().getColumn(0).setPreferredWidth(50);
            tblMau.getColumnModel().getColumn(0).setMaxWidth(50);
        }

        javax.swing.GroupLayout jPanel29Layout = new javax.swing.GroupLayout(jPanel29);
        jPanel29.setLayout(jPanel29Layout);
        jPanel29Layout.setHorizontalGroup(
            jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane19)
        );
        jPanel29Layout.setVerticalGroup(
            jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane19, javax.swing.GroupLayout.DEFAULT_SIZE, 521, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout tabMauLayout = new javax.swing.GroupLayout(tabMau);
        tabMau.setLayout(tabMauLayout);
        tabMauLayout.setHorizontalGroup(
            tabMauLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel27, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel29, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        tabMauLayout.setVerticalGroup(
            tabMauLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tabMauLayout.createSequentialGroup()
                .addComponent(jPanel27, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel29, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        tabTabChitiet.addTab("Màu", tabMau);

        javax.swing.GroupLayout tabChitietLayout = new javax.swing.GroupLayout(tabChitiet);
        tabChitiet.setLayout(tabChitietLayout);
        tabChitietLayout.setHorizontalGroup(
            tabChitietLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabTabChitiet)
        );
        tabChitietLayout.setVerticalGroup(
            tabChitietLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabTabChitiet, javax.swing.GroupLayout.PREFERRED_SIZE, 614, Short.MAX_VALUE)
        );

        TabQTV.addTab("Nhãn hiệu/Loại/Màu", new javax.swing.ImageIcon(getClass().getResource("/Images/detail_16px.png")), tabChitiet); // NOI18N

        pnlTimLoc.setBackground(new java.awt.Color(204, 204, 204));
        pnlTimLoc.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Tìm hóa đơn", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12))); // NOI18N

        pnlLoctheoTgian.setBackground(new java.awt.Color(204, 204, 204));

        jLabel24.setFont(new java.awt.Font("Tahoma", 2, 12)); // NOI18N
        jLabel24.setText("Từ");

        tg1_thongke.setDateFormatString("dd/MM/yyyy");

        jLabel23.setFont(new java.awt.Font("Tahoma", 2, 12)); // NOI18N
        jLabel23.setText("Đến");

        tg2_thongke.setDateFormatString("dd/MM/yyyy");

        rbMoctg.setBackground(new java.awt.Color(204, 204, 204));
        buttonGroup_LochoadontheoTGIAN.add(rbMoctg);
        rbMoctg.setFont(new java.awt.Font("Tahoma", 2, 12)); // NOI18N
        rbMoctg.setSelected(true);
        rbMoctg.setText("Tìm theo mốc thời gian:");

        tg3_thongke.setDateFormatString("dd/MM/yyyy");

        rbKhoangtg.setBackground(new java.awt.Color(204, 204, 204));
        buttonGroup_LochoadontheoTGIAN.add(rbKhoangtg);
        rbKhoangtg.setFont(new java.awt.Font("Tahoma", 2, 12)); // NOI18N
        rbKhoangtg.setText("Tìm theo khoảng thời gian:");

        jLabel26.setFont(new java.awt.Font("Tahoma", 2, 12)); // NOI18N
        jLabel26.setText("Chọn");

        rbNgay_thongke.setBackground(new java.awt.Color(204, 204, 204));
        buttonGroup_locHoaDonTheoMocTgian.add(rbNgay_thongke);
        rbNgay_thongke.setSelected(true);
        rbNgay_thongke.setText("Ngày");

        rbThang_thongke.setBackground(new java.awt.Color(204, 204, 204));
        buttonGroup_locHoaDonTheoMocTgian.add(rbThang_thongke);
        rbThang_thongke.setText("Tháng");

        rbNam_thongke.setBackground(new java.awt.Color(204, 204, 204));
        buttonGroup_locHoaDonTheoMocTgian.add(rbNam_thongke);
        rbNam_thongke.setText("Năm");

        javax.swing.GroupLayout pnlLoctheoTgianLayout = new javax.swing.GroupLayout(pnlLoctheoTgian);
        pnlLoctheoTgian.setLayout(pnlLoctheoTgianLayout);
        pnlLoctheoTgianLayout.setHorizontalGroup(
            pnlLoctheoTgianLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlLoctheoTgianLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlLoctheoTgianLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(rbKhoangtg)
                    .addComponent(rbMoctg))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlLoctheoTgianLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel26))
                .addGap(18, 18, 18)
                .addGroup(pnlLoctheoTgianLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tg1_thongke, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tg3_thongke, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(pnlLoctheoTgianLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlLoctheoTgianLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(rbNgay_thongke)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(rbThang_thongke)
                        .addGap(7, 7, 7)
                        .addComponent(rbNam_thongke)
                        .addContainerGap(12, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlLoctheoTgianLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(tg2_thongke, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18))))
        );
        pnlLoctheoTgianLayout.setVerticalGroup(
            pnlLoctheoTgianLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlLoctheoTgianLayout.createSequentialGroup()
                .addGroup(pnlLoctheoTgianLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tg1_thongke, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pnlLoctheoTgianLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel24)
                        .addComponent(rbKhoangtg))
                    .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tg2_thongke, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 7, Short.MAX_VALUE)
                .addGroup(pnlLoctheoTgianLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tg3_thongke, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pnlLoctheoTgianLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(rbNgay_thongke)
                        .addComponent(rbThang_thongke)
                        .addComponent(rbNam_thongke))
                    .addGroup(pnlLoctheoTgianLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel26, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(rbMoctg)))
                .addContainerGap())
        );

        ckTimtheosp.setBackground(new java.awt.Color(204, 204, 204));
        ckTimtheosp.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        ckTimtheosp.setText("Tìm theo sản phẩm");

        ckTimtheotg.setBackground(new java.awt.Color(204, 204, 204));
        ckTimtheotg.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        ckTimtheotg.setText("Tìm theo thời gian");

        jPanel14.setBackground(new java.awt.Color(204, 204, 204));

        jLabel10.setFont(new java.awt.Font("Tahoma", 2, 12)); // NOI18N
        jLabel10.setText("Nhập mã sản phẩm");

        txtMasp_thongke.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtMasp_thongke))
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(txtMasp_thongke, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        ckTimtheonv.setBackground(new java.awt.Color(204, 204, 204));
        ckTimtheonv.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        ckTimtheonv.setText("Tìm theo nhân viên");

        jPanel19.setBackground(new java.awt.Color(204, 204, 204));

        lblNhapmaTV_thongke.setFont(new java.awt.Font("Tahoma", 2, 12)); // NOI18N
        lblNhapmaTV_thongke.setText("Nhập mã nhân viên");

        txtManv_thongke.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
        jPanel19.setLayout(jPanel19Layout);
        jPanel19Layout.setHorizontalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(lblNhapmaTV_thongke, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtManv_thongke))
        );
        jPanel19Layout.setVerticalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(lblNhapmaTV_thongke, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(txtManv_thongke, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        btnReset_thongke.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnReset_thongke.setText("Reset");
        btnReset_thongke.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReset_thongkeActionPerformed(evt);
            }
        });

        rbHoadonapp.setBackground(new java.awt.Color(204, 204, 204));
        buttonGroup_thongKeHoaDon.add(rbHoadonapp);
        rbHoadonapp.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        rbHoadonapp.setForeground(new java.awt.Color(255, 51, 51));
        rbHoadonapp.setSelected(true);
        rbHoadonapp.setText("Hóa đơn App");
        rbHoadonapp.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                rbHoadonappMouseClicked(evt);
            }
        });

        rbHoadonweb.setBackground(new java.awt.Color(204, 204, 204));
        buttonGroup_thongKeHoaDon.add(rbHoadonweb);
        rbHoadonweb.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        rbHoadonweb.setForeground(new java.awt.Color(255, 51, 51));
        rbHoadonweb.setText("Hóa đơn Web");
        rbHoadonweb.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                rbHoadonwebMouseClicked(evt);
            }
        });

        btnTimhoadon_thongke.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnTimhoadon_thongke.setText("TÌM");
        btnTimhoadon_thongke.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTimhoadon_thongkeActionPerformed(evt);
            }
        });

        rbChuabangiao.setBackground(new java.awt.Color(204, 204, 204));
        buttonGroup_bangiaohoadon.add(rbChuabangiao);
        rbChuabangiao.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        rbChuabangiao.setForeground(new java.awt.Color(204, 0, 102));
        rbChuabangiao.setText("Chưa bàn giao");

        rbDabangiao.setBackground(new java.awt.Color(204, 204, 204));
        buttonGroup_bangiaohoadon.add(rbDabangiao);
        rbDabangiao.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        rbDabangiao.setForeground(new java.awt.Color(204, 0, 102));
        rbDabangiao.setSelected(true);
        rbDabangiao.setText("Đã bàn giao");

        rbTatcabangiao.setBackground(new java.awt.Color(204, 204, 204));
        buttonGroup_bangiaohoadon.add(rbTatcabangiao);
        rbTatcabangiao.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        rbTatcabangiao.setForeground(new java.awt.Color(204, 0, 102));
        rbTatcabangiao.setText("Tất cả");

        javax.swing.GroupLayout pnlTimLocLayout = new javax.swing.GroupLayout(pnlTimLoc);
        pnlTimLoc.setLayout(pnlTimLocLayout);
        pnlTimLocLayout.setHorizontalGroup(
            pnlTimLocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTimLocLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlTimLocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnlLoctheoTgian, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(pnlTimLocLayout.createSequentialGroup()
                        .addGroup(pnlTimLocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(pnlTimLocLayout.createSequentialGroup()
                                .addGroup(pnlTimLocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(ckTimtheosp)
                                    .addComponent(ckTimtheonv))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(jPanel19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap())
                    .addGroup(pnlTimLocLayout.createSequentialGroup()
                        .addGroup(pnlTimLocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(ckTimtheotg)
                            .addGroup(pnlTimLocLayout.createSequentialGroup()
                                .addGroup(pnlTimLocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(rbHoadonapp)
                                    .addComponent(rbDabangiao))
                                .addGap(38, 38, 38)
                                .addGroup(pnlTimLocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(rbHoadonweb)
                                    .addComponent(rbChuabangiao))
                                .addGap(46, 46, 46)
                                .addComponent(rbTatcabangiao)))
                        .addGap(0, 0, Short.MAX_VALUE))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlTimLocLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnReset_thongke, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(btnTimhoadon_thongke, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(68, 68, 68))
        );
        pnlTimLocLayout.setVerticalGroup(
            pnlTimLocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTimLocLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(pnlTimLocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rbHoadonapp)
                    .addComponent(rbHoadonweb))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlTimLocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rbChuabangiao)
                    .addComponent(rbDabangiao)
                    .addComponent(rbTatcabangiao))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 12, Short.MAX_VALUE)
                .addComponent(ckTimtheotg)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlLoctheoTgian, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(ckTimtheosp)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(ckTimtheonv)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlTimLocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnTimhoadon_thongke, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnReset_thongke, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        tblHoadon_Thongke.setBackground(new java.awt.Color(204, 255, 255));
        tblHoadon_Thongke.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "#", "Mã HĐ", "Tổng tiền", "Thời gian", "Bàn giao", "Mã nhân viên", "Tên nhân viên"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblHoadon_Thongke.setRowHeight(25);
        tblHoadon_Thongke.getTableHeader().setReorderingAllowed(false);
        tblHoadon_Thongke.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblHoadon_ThongkeMouseClicked(evt);
            }
        });
        jScrollPane10.setViewportView(tblHoadon_Thongke);
        if (tblHoadon_Thongke.getColumnModel().getColumnCount() > 0) {
            tblHoadon_Thongke.getColumnModel().getColumn(0).setPreferredWidth(20);
            tblHoadon_Thongke.getColumnModel().getColumn(1).setMinWidth(70);
            tblHoadon_Thongke.getColumnModel().getColumn(1).setPreferredWidth(70);
            tblHoadon_Thongke.getColumnModel().getColumn(1).setMaxWidth(70);
        }

        tblChitiethoadon_Thongke.setBackground(new java.awt.Color(204, 255, 255));
        tblChitiethoadon_Thongke.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "#", "Mã SP", "Tên SP", "SL", "Giá", "Size", "Thành tiền"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblChitiethoadon_Thongke.setRowHeight(25);
        tblChitiethoadon_Thongke.getTableHeader().setReorderingAllowed(false);
        jScrollPane11.setViewportView(tblChitiethoadon_Thongke);
        if (tblChitiethoadon_Thongke.getColumnModel().getColumnCount() > 0) {
            tblChitiethoadon_Thongke.getColumnModel().getColumn(0).setMinWidth(50);
            tblChitiethoadon_Thongke.getColumnModel().getColumn(0).setPreferredWidth(50);
            tblChitiethoadon_Thongke.getColumnModel().getColumn(0).setMaxWidth(50);
            tblChitiethoadon_Thongke.getColumnModel().getColumn(1).setMinWidth(70);
            tblChitiethoadon_Thongke.getColumnModel().getColumn(1).setPreferredWidth(70);
            tblChitiethoadon_Thongke.getColumnModel().getColumn(1).setMaxWidth(70);
        }

        jPanel20.setBackground(new java.awt.Color(204, 204, 204));
        jPanel20.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Kết quả", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12))); // NOI18N

        jLabel27.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel27.setText("Số lượng đơn:");

        jLabel53.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel53.setText("Tổng thu:");

        lblTonghoadon_Thongke.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblTonghoadon_Thongke.setForeground(new java.awt.Color(255, 0, 0));
        lblTonghoadon_Thongke.setText("0 đơn");

        lblThongthu_Thongke.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        lblThongthu_Thongke.setForeground(new java.awt.Color(255, 0, 0));
        lblThongthu_Thongke.setText("000000000000 đ");

        jLabel63.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel63.setText("Số sản phẩm được bán:");

        lblTongsanpham_thongke.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblTongsanpham_thongke.setForeground(new java.awt.Color(255, 0, 0));
        lblTongsanpham_thongke.setText("0 đ");

        javax.swing.GroupLayout jPanel20Layout = new javax.swing.GroupLayout(jPanel20);
        jPanel20.setLayout(jPanel20Layout);
        jPanel20Layout.setHorizontalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel20Layout.createSequentialGroup()
                        .addComponent(jLabel27)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblTonghoadon_Thongke, javax.swing.GroupLayout.DEFAULT_SIZE, 413, Short.MAX_VALUE))
                    .addGroup(jPanel20Layout.createSequentialGroup()
                        .addComponent(jLabel53)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblThongthu_Thongke, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel20Layout.createSequentialGroup()
                        .addComponent(jLabel63)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblTongsanpham_thongke, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel20Layout.setVerticalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel53, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblThongthu_Thongke))
                .addGap(40, 40, 40)
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel27)
                    .addComponent(lblTonghoadon_Thongke))
                .addGap(26, 26, 26)
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel63)
                    .addComponent(lblTongsanpham_thongke))
                .addContainerGap(50, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout tabThongkeLayout = new javax.swing.GroupLayout(tabThongke);
        tabThongke.setLayout(tabThongkeLayout);
        tabThongkeLayout.setHorizontalGroup(
            tabThongkeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tabThongkeLayout.createSequentialGroup()
                .addGroup(tabThongkeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(pnlTimLoc, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(tabThongkeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane11, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 794, Short.MAX_VALUE)
                    .addComponent(jScrollPane10))
                .addContainerGap())
        );
        tabThongkeLayout.setVerticalGroup(
            tabThongkeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tabThongkeLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(tabThongkeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(tabThongkeLayout.createSequentialGroup()
                        .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane11, javax.swing.GroupLayout.PREFERRED_SIZE, 316, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(tabThongkeLayout.createSequentialGroup()
                        .addComponent(pnlTimLoc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(21, 21, 21))
        );

        TabQTV.addTab("Thống kê", tabThongke);

        tblTV.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        tblTV.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "#", "Mã người dùng", "Tên người dùng", "Email", "Thời gian tạo", "Thời gian cập nhật"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, true, false, false
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

        btnCapnhat_Suatv.setBackground(new java.awt.Color(102, 153, 255));
        btnCapnhat_Suatv.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnCapnhat_Suatv.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/available_updates_32px.png"))); // NOI18N
        btnCapnhat_Suatv.setText("   Cập nhật");
        btnCapnhat_Suatv.setToolTipText("");
        btnCapnhat_Suatv.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCapnhat_SuatvActionPerformed(evt);
            }
        });

        jLabel47.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel47.setText("Chức vụ");

        cboChucvu_Suatv.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cboChucvu_Suatv.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Admin", "Nhân viên Website", "Nhân viên bán hàng" }));

        txtTen_Suatv.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        jLabel32.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel32.setText("Họ và Tên");

        jLabel54.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel54.setText("Số điện thoại");

        txtSdt_Suatv.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        jLabel55.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel55.setText("Địa chỉ");

        jLabel57.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel57.setText("Email");

        txtEmail_Suatv.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        jLabel59.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel59.setText("Ngày sinh");

        jdNgaysinh_Suatv.setDateFormatString("dd/MM/yyyy");

        jLabel73.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel73.setText("Giới tính");

        cboGioitinh_Suatv.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cboGioitinh_Suatv.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Nữ", "Nam" }));

        btnXoa_Suatv.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnXoa_Suatv.setText("Xóa");
        btnXoa_Suatv.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoa_SuatvActionPerformed(evt);
            }
        });

        lblDoimk_Suatv.setForeground(new java.awt.Color(255, 0, 51));
        lblDoimk_Suatv.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblDoimk_Suatv.setText("Xác nhận yêu cầu đổi mật khẩu ?");
        lblDoimk_Suatv.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblDoimk_SuatvMouseClicked(evt);
            }
        });

        jLabel75.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel75.setText("Ảnh");

        txtAnh_Suatv.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        btnChonfile_Suatv.setText("jButton5");
        btnChonfile_Suatv.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnChonfile_SuatvActionPerformed(evt);
            }
        });

        btnXemanh_Suatv.setText("Xem ảnh");
        btnXemanh_Suatv.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXemanh_SuatvActionPerformed(evt);
            }
        });

        txtDiachi_Suatv.setColumns(20);
        txtDiachi_Suatv.setRows(5);
        jScrollPane20.setViewportView(txtDiachi_Suatv);

        javax.swing.GroupLayout tabSuaThanhvienLayout = new javax.swing.GroupLayout(tabSuaThanhvien);
        tabSuaThanhvien.setLayout(tabSuaThanhvienLayout);
        tabSuaThanhvienLayout.setHorizontalGroup(
            tabSuaThanhvienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblDoimk_Suatv, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(tabSuaThanhvienLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(tabSuaThanhvienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane20)
                    .addGroup(tabSuaThanhvienLayout.createSequentialGroup()
                        .addComponent(jLabel75, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtAnh_Suatv)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnChonfile_Suatv, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(tabSuaThanhvienLayout.createSequentialGroup()
                        .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtTen_Suatv))
                    .addGroup(tabSuaThanhvienLayout.createSequentialGroup()
                        .addComponent(jLabel57, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtEmail_Suatv))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tabSuaThanhvienLayout.createSequentialGroup()
                        .addGap(23, 23, 23)
                        .addComponent(btnXemanh_Suatv, javax.swing.GroupLayout.DEFAULT_SIZE, 107, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnXoa_Suatv, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnCapnhat_Suatv, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(8, 8, 8))
                    .addGroup(tabSuaThanhvienLayout.createSequentialGroup()
                        .addGroup(tabSuaThanhvienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(tabSuaThanhvienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(tabSuaThanhvienLayout.createSequentialGroup()
                                    .addComponent(jLabel54, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(txtSdt_Suatv))
                                .addGroup(tabSuaThanhvienLayout.createSequentialGroup()
                                    .addComponent(jLabel59, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(jdNgaysinh_Suatv, javax.swing.GroupLayout.DEFAULT_SIZE, 166, Short.MAX_VALUE))
                                .addGroup(tabSuaThanhvienLayout.createSequentialGroup()
                                    .addComponent(jLabel73, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(cboGioitinh_Suatv, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(tabSuaThanhvienLayout.createSequentialGroup()
                                .addComponent(jLabel47, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(cboChucvu_Suatv, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel55, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        tabSuaThanhvienLayout.setVerticalGroup(
            tabSuaThanhvienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tabSuaThanhvienLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(tabSuaThanhvienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtTen_Suatv, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                    .addComponent(jLabel32, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE))
                .addGap(20, 20, 20)
                .addGroup(tabSuaThanhvienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtEmail_Suatv, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                    .addComponent(jLabel57, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(20, 20, 20)
                .addGroup(tabSuaThanhvienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(cboGioitinh_Suatv)
                    .addComponent(jLabel73, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(tabSuaThanhvienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtSdt_Suatv, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                    .addComponent(jLabel54, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(tabSuaThanhvienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel59, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jdNgaysinh_Suatv, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(tabSuaThanhvienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnChonfile_Suatv, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtAnh_Suatv)
                    .addComponent(jLabel75, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(tabSuaThanhvienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(cboChucvu_Suatv)
                    .addComponent(jLabel47, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jLabel55, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane20, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(tabSuaThanhvienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCapnhat_Suatv, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnXoa_Suatv, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnXemanh_Suatv, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblDoimk_Suatv)
                .addContainerGap())
        );

        tabSetThanhvien.addTab("Sửa", new javax.swing.ImageIcon(getClass().getResource("/Images/available_updates_16px.png")), tabSuaThanhvien); // NOI18N

        jLabel76.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel76.setText("Họ và Tên");

        txtTen_Themtv.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        jLabel77.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel77.setText("Email");

        txtEmail_Themtv.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        jLabel78.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel78.setText("Giới tính");

        cboGioitinh_Themtv.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cboGioitinh_Themtv.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Nữ", "Nam" }));

        btnCapnhatTV1.setBackground(new java.awt.Color(102, 153, 255));
        btnCapnhatTV1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnCapnhatTV1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/add_user32px.png"))); // NOI18N
        btnCapnhatTV1.setText("Thêm thành viên");
        btnCapnhatTV1.setToolTipText("");
        btnCapnhatTV1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCapnhatTV1ActionPerformed(evt);
            }
        });

        jLabel79.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel79.setText("Chức vụ");

        cboChucvu_Themtv.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cboChucvu_Themtv.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Admin", "Nhân viên Website", "Nhân viên bán hàng" }));

        jLabel82.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel82.setText("Số điện thoại");

        txtSdt_Themtv.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        jLabel88.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel88.setText("Địa chỉ");

        jLabel89.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel89.setText("Ngày sinh");

        jdNgaysinh_Themtv.setDateFormatString("dd/MM/yyyy");

        btnLoadFile_Themtv.setText("jButton5");
        btnLoadFile_Themtv.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLoadFile_ThemtvActionPerformed(evt);
            }
        });

        txtAnh_Themtv.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        jLabel91.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel91.setText("Ảnh");

        txtDiachi_Themtv.setColumns(20);
        txtDiachi_Themtv.setRows(5);
        jScrollPane21.setViewportView(txtDiachi_Themtv);

        javax.swing.GroupLayout tabThemThanhvienLayout = new javax.swing.GroupLayout(tabThemThanhvien);
        tabThemThanhvien.setLayout(tabThemThanhvienLayout);
        tabThemThanhvienLayout.setHorizontalGroup(
            tabThemThanhvienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tabThemThanhvienLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(tabThemThanhvienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane21)
                    .addGroup(tabThemThanhvienLayout.createSequentialGroup()
                        .addComponent(jLabel91, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtAnh_Themtv)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnLoadFile_Themtv, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(tabThemThanhvienLayout.createSequentialGroup()
                        .addComponent(jLabel76, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtTen_Themtv))
                    .addGroup(tabThemThanhvienLayout.createSequentialGroup()
                        .addComponent(jLabel77, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtEmail_Themtv))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tabThemThanhvienLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnCapnhatTV1, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(tabThemThanhvienLayout.createSequentialGroup()
                        .addGroup(tabThemThanhvienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(tabThemThanhvienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(tabThemThanhvienLayout.createSequentialGroup()
                                    .addComponent(jLabel82, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(txtSdt_Themtv))
                                .addGroup(tabThemThanhvienLayout.createSequentialGroup()
                                    .addComponent(jLabel89, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(jdNgaysinh_Themtv, javax.swing.GroupLayout.DEFAULT_SIZE, 166, Short.MAX_VALUE))
                                .addGroup(tabThemThanhvienLayout.createSequentialGroup()
                                    .addComponent(jLabel78, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(cboGioitinh_Themtv, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(tabThemThanhvienLayout.createSequentialGroup()
                                .addComponent(jLabel79, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(cboChucvu_Themtv, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel88, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 151, Short.MAX_VALUE)))
                .addContainerGap())
        );
        tabThemThanhvienLayout.setVerticalGroup(
            tabThemThanhvienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tabThemThanhvienLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(tabThemThanhvienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtTen_Themtv, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                    .addComponent(jLabel76, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE))
                .addGap(20, 20, 20)
                .addGroup(tabThemThanhvienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtEmail_Themtv, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                    .addComponent(jLabel77, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(20, 20, 20)
                .addGroup(tabThemThanhvienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(cboGioitinh_Themtv)
                    .addComponent(jLabel78, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(tabThemThanhvienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtSdt_Themtv, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                    .addComponent(jLabel82, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(tabThemThanhvienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel89, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jdNgaysinh_Themtv, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(tabThemThanhvienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnLoadFile_Themtv, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtAnh_Themtv)
                    .addComponent(jLabel91, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(tabThemThanhvienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(cboChucvu_Themtv)
                    .addComponent(jLabel79, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jLabel88, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnCapnhatTV1, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(24, Short.MAX_VALUE))
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
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(tabSetThanhvien, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        btnTimTV.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnTimTV.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/search_16px.png"))); // NOI18N
        btnTimTV.setText("Tìm");
        btnTimTV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTimTVActionPerformed(evt);
            }
        });

        txtTimthanhvien_QLTV.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N

        cboChucvuTV.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cboChucvuTV.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tất cả", "Admin", "Nhân viên Website", "Nhân viên bán hàng" }));

        jLabel49.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel49.setText("Chức vụ");

        jLabel56.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel56.setText("Đã tạo từ");

        jdDataoTV.setDateFormatString("dd/MM/yyyy");

        jLabel74.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel74.setText("Đến");

        jdDataoTV2.setDateFormatString("dd/MM/yyyy");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel49)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cboChucvuTV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35)
                .addComponent(jLabel56)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jdDataoTV, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(43, 43, 43)
                .addComponent(jLabel74, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jdDataoTV2, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(txtTimthanhvien_QLTV, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnTimTV)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addComponent(jLabel49, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addComponent(jLabel56, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(cboChucvuTV))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(txtTimthanhvien_QLTV, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(btnTimTV))
                            .addComponent(jdDataoTV, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jdDataoTV2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel74, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        javax.swing.GroupLayout tabThanhvienLayout = new javax.swing.GroupLayout(tabThanhvien);
        tabThanhvien.setLayout(tabThanhvienLayout);
        tabThanhvienLayout.setHorizontalGroup(
            tabThanhvienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tabThanhvienLayout.createSequentialGroup()
                .addGroup(tabThanhvienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane9, javax.swing.GroupLayout.DEFAULT_SIZE, 903, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        tabThanhvienLayout.setVerticalGroup(
            tabThanhvienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tabThanhvienLayout.createSequentialGroup()
                .addGroup(tabThanhvienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(tabThanhvienLayout.createSequentialGroup()
                        .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane9))
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        TabQTV.addTab("Thành viên", new javax.swing.ImageIcon(getClass().getResource("/Images/user_groups_16px.png")), tabThanhvien); // NOI18N

        javax.swing.GroupLayout tabQuanliLayout = new javax.swing.GroupLayout(tabQuanli);
        tabQuanli.setLayout(tabQuanliLayout);
        tabQuanliLayout.setHorizontalGroup(
            tabQuanliLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tabQuanliLayout.createSequentialGroup()
                .addComponent(TabQTV, javax.swing.GroupLayout.PREFERRED_SIZE, 1364, Short.MAX_VALUE)
                .addContainerGap())
        );
        tabQuanliLayout.setVerticalGroup(
            tabQuanliLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(TabQTV, javax.swing.GroupLayout.PREFERRED_SIZE, 644, Short.MAX_VALUE)
        );

        Tab.addTab("Quản trị ", new javax.swing.ImageIcon(getClass().getResource("/Images/manager.png")), tabQuanli); // NOI18N

        jPanel8.setBackground(new java.awt.Color(204, 204, 204));

        jPanel9.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Cửa hàng", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 2, 14))); // NOI18N
        jPanel9.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N

        jLabel42.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel42.setText("Số điện thoại:");

        txtSdtSett.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtSdtSett.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSdtSettActionPerformed(evt);
            }
        });

        jLabel33.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel33.setText("Tên cửa hàng:");

        txtTencuahangSett.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        jLabel40.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel40.setText("Fanpage:");

        txtFanpageSett.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        txtDiachiSett.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        jLabel41.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel41.setText("Địa chỉ:");

        btnCapnhatSett.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnCapnhatSett.setText("Cập nhật");
        btnCapnhatSett.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCapnhatSettActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnCapnhatSett, javax.swing.GroupLayout.PREFERRED_SIZE, 229, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGap(70, 70, 70)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel40, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel33, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtTencuahangSett, javax.swing.GroupLayout.DEFAULT_SIZE, 385, Short.MAX_VALUE)
                            .addComponent(txtFanpageSett))
                        .addGap(106, 106, 106)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel9Layout.createSequentialGroup()
                                .addComponent(jLabel42)
                                .addGap(18, 18, 18)
                                .addComponent(txtSdtSett))
                            .addGroup(jPanel9Layout.createSequentialGroup()
                                .addComponent(jLabel41, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(txtDiachiSett)))))
                .addGap(62, 62, 62))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtDiachiSett, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel41, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(15, 15, 15)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel42, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtSdtSett, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel33, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtTencuahangSett, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(15, 15, 15)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel40, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtFanpageSett, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(15, 15, 15)
                .addComponent(btnCapnhatSett, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel11.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Phần mềm", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 2, 14))); // NOI18N
        jPanel11.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N

        btnCapnhatSlideSett.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnCapnhatSlideSett.setText("Cập nhật");

        jLabel48.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel48.setText("Slide 4:");

        jLabel44.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel44.setText("Slide 1:");

        jLabel50.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel50.setText("Slide 3:");

        txtSlide1Sett.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        jLabel46.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel46.setText("Slide 2:");

        txtSlide2Sett.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        txtSlide4Sett.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        txtSlide3Sett.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        jButton8.setText("Chọn");

        jButton9.setText("Chọn");

        jButton10.setText("Chọn");

        jButton11.setText("Chọn");

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel46, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel44, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtSlide1Sett, javax.swing.GroupLayout.DEFAULT_SIZE, 395, Short.MAX_VALUE)
                    .addComponent(txtSlide2Sett))
                .addGap(18, 18, 18)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton8)
                    .addComponent(jButton10))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel48, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel50, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGap(18, 18, 18)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtSlide3Sett, javax.swing.GroupLayout.DEFAULT_SIZE, 389, Short.MAX_VALUE)
                            .addComponent(txtSlide4Sett))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton9)
                            .addComponent(jButton11)))
                    .addComponent(btnCapnhatSlideSett, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(62, 62, 62))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel44, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtSlide1Sett, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(15, 15, 15)
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel46, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtSlide2Sett, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(txtSlide3Sett, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jButton11, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel50, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(15, 15, 15)
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel48, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(txtSlide4Sett, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnCapnhatSlideSett, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15))
        );

        btnDangxuatSett.setBackground(new java.awt.Color(255, 0, 51));
        btnDangxuatSett.setFont(new java.awt.Font("Tahoma", 1, 20)); // NOI18N
        btnDangxuatSett.setForeground(new java.awt.Color(204, 204, 204));
        btnDangxuatSett.setText("ĐĂNG XUẤT");
        btnDangxuatSett.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDangxuatSettActionPerformed(evt);
            }
        });

        jPanel25.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "In hóa đơn", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 2, 14))); // NOI18N
        jPanel25.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N

        btnCapnhatSlideSett1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnCapnhatSlideSett1.setText("Cập nhật");

        jCheckBox4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jCheckBox4.setText("Hiển thị giao diện in trước khi in");

        txtSlide2Sett1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        jButton15.setText("Chọn");

        jCheckBox5.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jCheckBox5.setText("Lưu hóa đơn vào");

        javax.swing.GroupLayout jPanel25Layout = new javax.swing.GroupLayout(jPanel25);
        jPanel25.setLayout(jPanel25Layout);
        jPanel25Layout.setHorizontalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel25Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jCheckBox4)
                    .addGroup(jPanel25Layout.createSequentialGroup()
                        .addComponent(jCheckBox5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtSlide2Sett1, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton15)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnCapnhatSlideSett1, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(62, 62, 62))
        );
        jPanel25Layout.setVerticalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel25Layout.createSequentialGroup()
                .addContainerGap(24, Short.MAX_VALUE)
                .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel25Layout.createSequentialGroup()
                        .addComponent(btnCapnhatSlideSett1, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12))
                    .addGroup(jPanel25Layout.createSequentialGroup()
                        .addComponent(jCheckBox4)
                        .addGap(9, 9, 9)
                        .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel25Layout.createSequentialGroup()
                                .addComponent(jButton15, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(2, 2, 2))
                            .addComponent(jCheckBox5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtSlide2Sett1, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(10, 10, 10))))
        );

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(btnDangxuatSett, javax.swing.GroupLayout.PREFERRED_SIZE, 274, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jPanel25, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel25, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(btnDangxuatSett, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout tabCaidatLayout = new javax.swing.GroupLayout(tabCaidat);
        tabCaidat.setLayout(tabCaidatLayout);
        tabCaidatLayout.setHorizontalGroup(
            tabCaidatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        tabCaidatLayout.setVerticalGroup(
            tabCaidatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tabCaidatLayout.createSequentialGroup()
                .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        Tab.addTab("Cài đặt", new javax.swing.ImageIcon(getClass().getResource("/Images/setting.png")), tabCaidat); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Tab, javax.swing.GroupLayout.PREFERRED_SIZE, 1369, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Tab, javax.swing.GroupLayout.PREFERRED_SIZE, 698, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        Tab.getAccessibleContext().setAccessibleDescription("");

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnTimkiem_TracuuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTimkiem_TracuuActionPerformed
        //doDuLieuTraCuu(scriptTraCuuSP());
        bangTraCuu();
    }//GEN-LAST:event_btnTimkiem_TracuuActionPerformed

    private void btnCapnhatSpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCapnhatSpActionPerformed
        capnhatSanpham_QLSP();
    }//GEN-LAST:event_btnCapnhatSpActionPerformed

    private void btnXoaSp_vinhvienActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaSp_vinhvienActionPerformed
        XOASANPHAM();
    }//GEN-LAST:event_btnXoaSp_vinhvienActionPerformed

    private void btnThemnhanhieuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemnhanhieuActionPerformed
        themNhanhieu();
    }//GEN-LAST:event_btnThemnhanhieuActionPerformed

    private void btnXoanhanhieuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoanhanhieuActionPerformed
        xoaNhanhieu();
    }//GEN-LAST:event_btnXoanhanhieuActionPerformed

    private void btnDoitenLoaiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDoitenLoaiActionPerformed
        doitenLoai();
    }//GEN-LAST:event_btnDoitenLoaiActionPerformed

    private void btnDoitenNhanhieuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDoitenNhanhieuActionPerformed
        doitenNhanhieu();
    }//GEN-LAST:event_btnDoitenNhanhieuActionPerformed

    private void btnThemLoaiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemLoaiActionPerformed
        themLoai();
    }//GEN-LAST:event_btnThemLoaiActionPerformed

    private void btnXoaLoaiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaLoaiActionPerformed
        xoaLoai();
    }//GEN-LAST:event_btnXoaLoaiActionPerformed

    private void btnTimTVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTimTVActionPerformed
        doDuLieuBangTV();
    }//GEN-LAST:event_btnTimTVActionPerformed

    private void tblTVMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblTVMouseClicked
        ClickThanhvien();
    }//GEN-LAST:event_tblTVMouseClicked

    private void btnCapnhat_SuatvActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCapnhat_SuatvActionPerformed
        CapNhatTV();
        doDuLieuBangTV();
    }//GEN-LAST:event_btnCapnhat_SuatvActionPerformed

    private void tblHoadon_XemhoadonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblHoadon_XemhoadonMouseClicked
        chonHoaDon_TracuuHD();
    }//GEN-LAST:event_tblHoadon_XemhoadonMouseClicked

    private void btnTimActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTimActionPerformed
        timKiemSP_ThanhToan(txtTimkiem.getText());
        hienThiSoLuongSP_ThanhToan();
    }//GEN-LAST:event_btnTimActionPerformed

    private void btnThemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemActionPerformed
        themChiTietDonHang_ThanhToan();
    }//GEN-LAST:event_btnThemActionPerformed

    private void cboSizeTTItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboSizeTTItemStateChanged
        //System.out.println(cboSizeTT.getSelectedItem().toString());
        hienThiSoLuongSP_ThanhToan();
    }//GEN-LAST:event_cboSizeTTItemStateChanged

    private void btnXoaSpTTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaSpTTActionPerformed
        xoaSpTT_ThanhToan();
        doDuLieuChitietDonHang_Thanhtoan();
    }//GEN-LAST:event_btnXoaSpTTActionPerformed

    private void btnThanhtoanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThanhtoanActionPerformed
        thanhToan();
    }//GEN-LAST:event_btnThanhtoanActionPerformed

    private void tblChitietSPMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblChitietSPMouseClicked
        chonSP_ThanhToan();
    }//GEN-LAST:event_tblChitietSPMouseClicked

    private void tblTracuuMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblTracuuMouseClicked
        bangTraCuuClick();
    }//GEN-LAST:event_tblTracuuMouseClicked

    private void btnTkspQLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTkspQLActionPerformed
        String txt = txtTk_CapnhatSP.getText().trim();
        if (txt.length() > 0) {
            timKiemSP_CapnhatSP(txt);
        }
    }//GEN-LAST:event_btnTkspQLActionPerformed

    private void btnInhoadon_XemhoadonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInhoadon_XemhoadonActionPerformed
        inHoaDon_TracuuHD();
    }//GEN-LAST:event_btnInhoadon_XemhoadonActionPerformed

    private void btnDangxuatSettActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDangxuatSettActionPerformed
        dangXuat();
    }//GEN-LAST:event_btnDangxuatSettActionPerformed

    private void txtSdtSettActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSdtSettActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSdtSettActionPerformed

    private void btnCapnhatSettActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCapnhatSettActionPerformed
        caiDatCuaHang();
    }//GEN-LAST:event_btnCapnhatSettActionPerformed

    private void btnBangiaotienActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBangiaotienActionPerformed
        banGiaoTien();
    }//GEN-LAST:event_btnBangiaotienActionPerformed

    private void tblHoadon_ThongkeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblHoadon_ThongkeMouseClicked
        chiTietHoaDon_Thongke();
    }//GEN-LAST:event_tblHoadon_ThongkeMouseClicked

    private void btnTkHd_XemhoadonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTkHd_XemhoadonActionPerformed
        timKiemHoaDon();
    }//GEN-LAST:event_btnTkHd_XemhoadonActionPerformed

    private void btnDatlai_XemhoadonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDatlai_XemhoadonActionPerformed
        doDuLieuHoaDon_TracuuHD();
    }//GEN-LAST:event_btnDatlai_XemhoadonActionPerformed

    private void btnTimhoadon_thongkeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTimhoadon_thongkeActionPerformed
        DefaultTableModel model = (DefaultTableModel) tblChitiethoadon_Thongke.getModel();
        model.setRowCount(0);
        doDuLieuHoaDon_Thongke();
    }//GEN-LAST:event_btnTimhoadon_thongkeActionPerformed

    private void rbHoadonappMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_rbHoadonappMouseClicked
        chonLoaiHoaDon_Thongke(true);
        doDuLieuHoaDon_Thongke();
    }//GEN-LAST:event_rbHoadonappMouseClicked

    private void rbHoadonwebMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_rbHoadonwebMouseClicked
        chonLoaiHoaDon_Thongke(false);
        doDuLieuHoaDon_Thongke();
    }//GEN-LAST:event_rbHoadonwebMouseClicked

    private void btnXemanhActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXemanhActionPerformed
        loadHinhAnh_CapnhatSP();
    }//GEN-LAST:event_btnXemanhActionPerformed

    private void btnThemSize_CapnhatSPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemSize_CapnhatSPActionPerformed
        themSize_CapnhatSP();
    }//GEN-LAST:event_btnThemSize_CapnhatSPActionPerformed

    private void btnXoaSize_CapnhatSPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaSize_CapnhatSPActionPerformed
        xoaSize_CapnhatSP();
    }//GEN-LAST:event_btnXoaSize_CapnhatSPActionPerformed

    private void btnThemmau_CapnhatSPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemmau_CapnhatSPActionPerformed
        themMau_CapnhatSP();
    }//GEN-LAST:event_btnThemmau_CapnhatSPActionPerformed

    private void btnXoamau_CapnhatSPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoamau_CapnhatSPActionPerformed
        xoaMau_Capnhatsp();
    }//GEN-LAST:event_btnXoamau_CapnhatSPActionPerformed

    private void tblSize_CapnhatSPMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblSize_CapnhatSPMouseClicked
        sizeClick_CapnhatSP();
    }//GEN-LAST:event_tblSize_CapnhatSPMouseClicked

    private void btnCapnhatSize_CapnhatSPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCapnhatSize_CapnhatSPActionPerformed
        capNhatSoluongSize_CapnhatSP();
    }//GEN-LAST:event_btnCapnhatSize_CapnhatSPActionPerformed

    private void btnThemAnh_CapnhatSPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemAnh_CapnhatSPActionPerformed
        themAnh_CapnhatSP();
    }//GEN-LAST:event_btnThemAnh_CapnhatSPActionPerformed

    private void btnChonfileAnh_CapnhatSPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChonfileAnh_CapnhatSPActionPerformed
        chonFile_CapnhatSP();
    }//GEN-LAST:event_btnChonfileAnh_CapnhatSPActionPerformed

    private void btnCapnhatMau_CapnhatSPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCapnhatMau_CapnhatSPActionPerformed
        capnhatMau_CapnhatSP();
    }//GEN-LAST:event_btnCapnhatMau_CapnhatSPActionPerformed

    private void btnDatlamanhchinh_CapnhatspActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDatlamanhchinh_CapnhatspActionPerformed
        datAnhChinh_CapnhatSP();
    }//GEN-LAST:event_btnDatlamanhchinh_CapnhatspActionPerformed

    private void btnXoaanh_CapnhatspActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaanh_CapnhatspActionPerformed
        xoaAnh_CapnhatSP();
    }//GEN-LAST:event_btnXoaanh_CapnhatspActionPerformed

    private void btnCapnhatanhActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCapnhatanhActionPerformed
        capNhatAnh_CapnhatSP();
    }//GEN-LAST:event_btnCapnhatanhActionPerformed

    private void btnThemSize_ThemspActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemSize_ThemspActionPerformed
        themSize_Themsp();
    }//GEN-LAST:event_btnThemSize_ThemspActionPerformed

    private void btnXoaSize_ThemspActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaSize_ThemspActionPerformed
        xoaSize_Themsp();
    }//GEN-LAST:event_btnXoaSize_ThemspActionPerformed

    private void btnThemMau_ThemspActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemMau_ThemspActionPerformed
        themMau_Themsp();
    }//GEN-LAST:event_btnThemMau_ThemspActionPerformed

    private void btnXoaMau_ThemspActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaMau_ThemspActionPerformed
        xoaMau_Themsp();
    }//GEN-LAST:event_btnXoaMau_ThemspActionPerformed

    private void btnFile_ThemspActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFile_ThemspActionPerformed
        loadFile_Themsp();
    }//GEN-LAST:event_btnFile_ThemspActionPerformed

    private void btnThemAnh_ThemspActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemAnh_ThemspActionPerformed
        themAnh_Themsp();
    }//GEN-LAST:event_btnThemAnh_ThemspActionPerformed

    private void btnXoaAnh_ThemspActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaAnh_ThemspActionPerformed
        xoaAnh_Themsp();
    }//GEN-LAST:event_btnXoaAnh_ThemspActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        themSP();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void btnThemLoai1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemLoai1ActionPerformed
        themMau();
    }//GEN-LAST:event_btnThemLoai1ActionPerformed

    private void btnXoaLoai1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaLoai1ActionPerformed
        xoaMau();
    }//GEN-LAST:event_btnXoaLoai1ActionPerformed

    private void btnDoitenLoai1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDoitenLoai1ActionPerformed
        doitenMau();
    }//GEN-LAST:event_btnDoitenLoai1ActionPerformed

    private void btnCapnhatTV1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCapnhatTV1ActionPerformed
        themThanhVien();
        doDuLieuBangTV();
    }//GEN-LAST:event_btnCapnhatTV1ActionPerformed

    private void btnChonfile_SuatvActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChonfile_SuatvActionPerformed
        chonFile_Suatv();
    }//GEN-LAST:event_btnChonfile_SuatvActionPerformed

    private void btnXoa_SuatvActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoa_SuatvActionPerformed
        xoaThanhvien();
        doDuLieuBangTV();
    }//GEN-LAST:event_btnXoa_SuatvActionPerformed

    private void btnXemanh_SuatvActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXemanh_SuatvActionPerformed
        xemAnh_Suatv();
    }//GEN-LAST:event_btnXemanh_SuatvActionPerformed

    private void btnReset_thongkeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReset_thongkeActionPerformed

    }//GEN-LAST:event_btnReset_thongkeActionPerformed

    private void btnLoadFile_ThemtvActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLoadFile_ThemtvActionPerformed
        chonFile_Themtv();
    }//GEN-LAST:event_btnLoadFile_ThemtvActionPerformed

    private void lblDoimk_SuatvMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblDoimk_SuatvMouseClicked
        datLaiMK();
    }//GEN-LAST:event_lblDoimk_SuatvMouseClicked

    private void btnDangxuat2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDangxuat2ActionPerformed
        dangXuat();
    }//GEN-LAST:event_btnDangxuat2ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTabbedPane Tab;
    private javax.swing.JTabbedPane TabQTV;
    private javax.swing.JButton btnBangiaotien;
    private javax.swing.JButton btnCapnhatMau_CapnhatSP;
    private javax.swing.JButton btnCapnhatSett;
    private javax.swing.JButton btnCapnhatSize_CapnhatSP;
    private javax.swing.JButton btnCapnhatSlideSett;
    private javax.swing.JButton btnCapnhatSlideSett1;
    private javax.swing.JButton btnCapnhatSp;
    private javax.swing.JButton btnCapnhatTV1;
    private javax.swing.JButton btnCapnhat_Suatv;
    private javax.swing.JButton btnCapnhatanh;
    private javax.swing.JButton btnChonfileAnh_CapnhatSP;
    private javax.swing.JButton btnChonfile_Suatv;
    private javax.swing.JButton btnDangxuat2;
    private javax.swing.JButton btnDangxuatSett;
    private javax.swing.JButton btnDatlai_Xemhoadon;
    private javax.swing.JButton btnDatlamanhchinh_Capnhatsp;
    private javax.swing.JButton btnDoitenLoai;
    private javax.swing.JButton btnDoitenLoai1;
    private javax.swing.JButton btnDoitenNhanhieu;
    private javax.swing.JButton btnFile_Themsp;
    private javax.swing.JButton btnInhoadon_Xemhoadon;
    private javax.swing.JButton btnLoadFile_Themtv;
    private javax.swing.JButton btnReset_thongke;
    private javax.swing.JButton btnThanhtoan;
    private javax.swing.JButton btnThem;
    private javax.swing.JButton btnThemAnh_CapnhatSP;
    private javax.swing.JButton btnThemAnh_Themsp;
    private javax.swing.JButton btnThemLoai;
    private javax.swing.JButton btnThemLoai1;
    private javax.swing.JButton btnThemMau_Themsp;
    private javax.swing.JButton btnThemSize_CapnhatSP;
    private javax.swing.JButton btnThemSize_Themsp;
    private javax.swing.JButton btnThemmau_CapnhatSP;
    private javax.swing.JButton btnThemnhanhieu;
    private javax.swing.JButton btnTim;
    private javax.swing.JButton btnTimTV;
    private javax.swing.JButton btnTimhoadon_thongke;
    private javax.swing.JButton btnTimkiem_Tracuu;
    private javax.swing.JButton btnTkHd_Xemhoadon;
    private javax.swing.JButton btnTkspQL;
    private javax.swing.JButton btnXemanh;
    private javax.swing.JButton btnXemanh_Suatv;
    private javax.swing.JButton btnXoaAnh_Themsp;
    private javax.swing.JButton btnXoaLoai;
    private javax.swing.JButton btnXoaLoai1;
    private javax.swing.JButton btnXoaMau_Themsp;
    private javax.swing.JButton btnXoaSize_CapnhatSP;
    private javax.swing.JButton btnXoaSize_Themsp;
    private javax.swing.JButton btnXoaSpTT;
    private javax.swing.JButton btnXoaSp_vinhvien;
    private javax.swing.JButton btnXoa_Suatv;
    private javax.swing.JButton btnXoaanh_Capnhatsp;
    private javax.swing.JButton btnXoamau_CapnhatSP;
    private javax.swing.JButton btnXoanhanhieu;
    private javax.swing.ButtonGroup buttonGroup_LochoadontheoTGIAN;
    private javax.swing.ButtonGroup buttonGroup_bangiaohoadon;
    private javax.swing.ButtonGroup buttonGroup_locHoaDonTheoMocTgian;
    private javax.swing.ButtonGroup buttonGroup_thongKeHoaDon;
    private javax.swing.JComboBox cboChucvuTV;
    private javax.swing.JComboBox cboChucvu_Suatv;
    private javax.swing.JComboBox cboChucvu_Themtv;
    private javax.swing.JComboBox cboGioitinh_Suatv;
    private javax.swing.JComboBox cboGioitinh_Themtv;
    private javax.swing.JComboBox cboLoai_Capnhat;
    private javax.swing.JComboBox cboLoai_Themsp;
    private javax.swing.JComboBox cboLoai_Tracuu;
    private javax.swing.JComboBox cboMau_Capnhat;
    private javax.swing.JComboBox cboMau_Themsp;
    private javax.swing.JComboBox cboMau_Tracuu;
    private javax.swing.JComboBox cboNhanhieu_Capnhat;
    private javax.swing.JComboBox cboNhanhieu_Themsp;
    private javax.swing.JComboBox cboNhanhieu_Tracuu;
    private javax.swing.JComboBox cboSizeTT;
    private javax.swing.JComboBox cboTrangthaiCapnhat;
    private javax.swing.JComboBox cboTrangthai_Themsp;
    private javax.swing.JComboBox cboTrangthai_Tracuu;
    private javax.swing.JCheckBox ckInhoadon;
    private javax.swing.JCheckBox ckTimtheonv;
    private javax.swing.JCheckBox ckTimtheosp;
    private javax.swing.JCheckBox ckTimtheotg;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton15;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JCheckBox jCheckBox4;
    private javax.swing.JCheckBox jCheckBox5;
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
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
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
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel56;
    private javax.swing.JLabel jLabel57;
    private javax.swing.JLabel jLabel59;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel63;
    private javax.swing.JLabel jLabel65;
    private javax.swing.JLabel jLabel66;
    private javax.swing.JLabel jLabel67;
    private javax.swing.JLabel jLabel68;
    private javax.swing.JLabel jLabel69;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel72;
    private javax.swing.JLabel jLabel73;
    private javax.swing.JLabel jLabel74;
    private javax.swing.JLabel jLabel75;
    private javax.swing.JLabel jLabel76;
    private javax.swing.JLabel jLabel77;
    private javax.swing.JLabel jLabel78;
    private javax.swing.JLabel jLabel79;
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
    private javax.swing.JLabel jLabel91;
    private javax.swing.JLabel jLabel92;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel26;
    private javax.swing.JPanel jPanel27;
    private javax.swing.JPanel jPanel28;
    private javax.swing.JPanel jPanel29;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JScrollPane jScrollPane12;
    private javax.swing.JScrollPane jScrollPane13;
    private javax.swing.JScrollPane jScrollPane14;
    private javax.swing.JScrollPane jScrollPane15;
    private javax.swing.JScrollPane jScrollPane16;
    private javax.swing.JScrollPane jScrollPane17;
    private javax.swing.JScrollPane jScrollPane18;
    private javax.swing.JScrollPane jScrollPane19;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane20;
    private javax.swing.JScrollPane jScrollPane21;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JTabbedPane jTabbedPane1;
    private com.toedter.calendar.JDateChooser jdDataoTV;
    private com.toedter.calendar.JDateChooser jdDataoTV2;
    private com.toedter.calendar.JDateChooser jdNgaysinh_Suatv;
    private com.toedter.calendar.JDateChooser jdNgaysinh_Themtv;
    private javax.swing.JLabel lblDoimk_Suatv;
    private javax.swing.JLabel lblLoai;
    private javax.swing.JLabel lblNhanhieu;
    private javax.swing.JLabel lblNhapmaTV_thongke;
    private javax.swing.JLabel lblSancoTT;
    private javax.swing.JLabel lblTennv;
    private javax.swing.JLabel lblTensp;
    private javax.swing.JLabel lblTgsuasp_Capnhatsp;
    private javax.swing.JLabel lblTgtaosp;
    private javax.swing.JLabel lblThongthu_Thongke;
    private javax.swing.JLabel lblTonghoadon;
    private javax.swing.JLabel lblTonghoadon_Thongke;
    private javax.swing.JLabel lblTongsanpham_thongke;
    private javax.swing.JLabel lblTongsohang;
    private javax.swing.JLabel lblTongthu_Xemhoadon;
    private javax.swing.JLabel lblTongtienHD_Xemhd;
    private javax.swing.JLabel lblTongtienTT;
    private javax.swing.JLabel lblTrangthai;
    private javax.swing.JPanel pnlCapNhatSP;
    private javax.swing.JPanel pnlCapNhatSP4;
    private javax.swing.JPanel pnlLoctheoTgian;
    private javax.swing.JPanel pnlTTLeft;
    private javax.swing.JPanel pnlThanhtoan;
    private javax.swing.JPanel pnlTimLoc;
    private javax.swing.JPanel pnlTimkiem;
    private javax.swing.JRadioButton rbChuabangiao;
    private javax.swing.JRadioButton rbDabangiao;
    private javax.swing.JRadioButton rbHoadonapp;
    private javax.swing.JRadioButton rbHoadonweb;
    private javax.swing.JRadioButton rbKhoangtg;
    private javax.swing.JRadioButton rbMoctg;
    private javax.swing.JRadioButton rbNam_thongke;
    private javax.swing.JRadioButton rbNgay_thongke;
    private javax.swing.JRadioButton rbTatcabangiao;
    private javax.swing.JRadioButton rbThang_thongke;
    private javax.swing.JSpinner spnGia_CapnhatSP;
    private javax.swing.JSpinner spnGia_Themsp;
    private javax.swing.JSpinner spnSanco_CapnhatSP;
    private javax.swing.JSpinner spnSize_CapnhatSP;
    private javax.swing.JSpinner spnSize_Themsp;
    private javax.swing.JSpinner spnSoluongSpTT;
    private javax.swing.JSpinner spnSoluong_CapnhatSP;
    private javax.swing.JSpinner spnSoluong_Themsp;
    private javax.swing.JPanel tabBanhang;
    private javax.swing.JPanel tabCaidat;
    private javax.swing.JPanel tabCapnhat;
    private javax.swing.JPanel tabChitiet;
    private javax.swing.JPanel tabHinhanh_Capnhatsp;
    private javax.swing.JPanel tabLoai;
    private javax.swing.JPanel tabMau;
    private javax.swing.JPanel tabMau_Capnhatsp;
    private javax.swing.JPanel tabNhanhieu;
    private javax.swing.JTabbedPane tabQLSP;
    private javax.swing.JPanel tabQuanli;
    private javax.swing.JPanel tabSanpham;
    private javax.swing.JTabbedPane tabSetThanhvien;
    private javax.swing.JPanel tabSizeSoluong_Capnhatsp;
    private javax.swing.JPanel tabSuaThanhvien;
    private javax.swing.JTabbedPane tabTabChitiet;
    private javax.swing.JPanel tabThanhvien;
    private javax.swing.JPanel tabThem;
    private javax.swing.JPanel tabThemThanhvien;
    private javax.swing.JPanel tabThongke;
    private javax.swing.JPanel tabTracuu;
    private javax.swing.JPanel tabXemhoadon;
    private javax.swing.JTable tblAnh_CapnhatSP;
    private javax.swing.JTable tblAnh_Themsp;
    private javax.swing.JTable tblChitietSP;
    private javax.swing.JTable tblChitiethoadon;
    private javax.swing.JTable tblChitiethoadon_Thongke;
    private javax.swing.JTable tblHoadon_Thongke;
    private javax.swing.JTable tblHoadon_Xemhoadon;
    private javax.swing.JTable tblLoai;
    private javax.swing.JTable tblMau;
    private javax.swing.JTable tblMau_CapnhatSP;
    private javax.swing.JTable tblMau_Themsp;
    private javax.swing.JTable tblNhanhieu;
    private javax.swing.JTable tblSizeTracuu;
    private javax.swing.JTable tblSize_CapnhatSP;
    private javax.swing.JTable tblSize_Themsp;
    private javax.swing.JTable tblTV;
    private javax.swing.JTable tblTracuu;
    private com.toedter.calendar.JDateChooser tg1_thongke;
    private com.toedter.calendar.JDateChooser tg2_thongke;
    private com.toedter.calendar.JDateChooser tg3_thongke;
    private javax.swing.JTextField txtAnh_Suatv;
    private javax.swing.JTextField txtAnh_Themtv;
    private javax.swing.JTextField txtCodemau;
    private javax.swing.JTextField txtDiachiSett;
    private javax.swing.JTextArea txtDiachi_Suatv;
    private javax.swing.JTextArea txtDiachi_Themtv;
    private javax.swing.JTextField txtEmail_Suatv;
    private javax.swing.JTextField txtEmail_Themtv;
    private javax.swing.JTextField txtFanpageSett;
    private javax.swing.JTextField txtKetqua;
    private javax.swing.JTextField txtLink_Themsp;
    private javax.swing.JTextField txtLinkanh_CapnhatSP;
    private javax.swing.JTextField txtMaSP_CapnhatSP;
    private javax.swing.JTextField txtMahoadon_Xemhoadon;
    private javax.swing.JTextField txtManv_thongke;
    private javax.swing.JTextField txtMasp_thongke;
    private javax.swing.JTextArea txtMotaSpCapnhat;
    private javax.swing.JTextArea txtMota_Themsp;
    private javax.swing.JTextField txtPhienban_CapnhatSP;
    private javax.swing.JTextField txtPhienban_Themsp;
    private javax.swing.JTextField txtSdtSett;
    private javax.swing.JTextField txtSdt_Suatv;
    private javax.swing.JTextField txtSdt_Themtv;
    private javax.swing.JTextField txtSlide1Sett;
    private javax.swing.JTextField txtSlide2Sett;
    private javax.swing.JTextField txtSlide2Sett1;
    private javax.swing.JTextField txtSlide3Sett;
    private javax.swing.JTextField txtSlide4Sett;
    private javax.swing.JTextField txtTenLoai;
    private javax.swing.JTextField txtTenLoaiMoi;
    private javax.swing.JTextField txtTenNhanhieumoi;
    private javax.swing.JTextField txtTenSp_CapnhatSP;
    private javax.swing.JTextField txtTen_Suatv;
    private javax.swing.JTextField txtTen_Themsp;
    private javax.swing.JTextField txtTen_Themtv;
    private javax.swing.JTextField txtTencuahangSett;
    private javax.swing.JTextField txtTenmau;
    private javax.swing.JTextField txtTenmaumoi;
    private javax.swing.JTextField txtTennhanhhieu;
    private javax.swing.JTextField txtTimkiem;
    private javax.swing.JTextField txtTimkiem_Tracuu;
    private javax.swing.JTextField txtTimthanhvien_QLTV;
    private javax.swing.JTextField txtTk_CapnhatSP;
    // End of variables declaration//GEN-END:variables
}
