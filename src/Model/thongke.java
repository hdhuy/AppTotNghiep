/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.util.Date;

/**
 *
 * @author Admin
 */
public class thongke {
    int bangiao=1;
    boolean timtheotg;
    //khoảng tgian
    boolean khoangThoigian;
    Date tg1;
    Date tg2;
    //mốc thời gian
    boolean mocThoigian;
    Date tg3;
    int ngaythangnam=1;
    boolean timtheoSanpham;
    String masanpham;
    boolean timtheoNhanvien;
    String manhanvien;
    int tongsp;

    public int getBangiao() {
        return bangiao;
    }

    public void setBangiao(int bangiao) {
        this.bangiao = bangiao;
    }

    public boolean isTimtheotg() {
        return timtheotg;
    }

    public void setTimtheotg(boolean timtheotg) {
        this.timtheotg = timtheotg;
    }

    public boolean isKhoangThoigian() {
        return khoangThoigian;
    }

    public void setKhoangThoigian(boolean khoangThoigian) {
        this.khoangThoigian = khoangThoigian;
    }

    public Date getTg1() {
        return tg1;
    }

    public void setTg1(Date tg1) {
        this.tg1 = tg1;
    }

    public Date getTg2() {
        return tg2;
    }

    public void setTg2(Date tg2) {
        this.tg2 = tg2;
    }

    public boolean isMocThoigian() {
        return mocThoigian;
    }

    public void setMocThoigian(boolean mocThoigian) {
        this.mocThoigian = mocThoigian;
    }

    public Date getTg3() {
        return tg3;
    }

    public void setTg3(Date tg3) {
        this.tg3 = tg3;
    }

    public int getNgaythangnam() {
        return ngaythangnam;
    }

    public void setNgaythangnam(int ngaythangnam) {
        this.ngaythangnam = ngaythangnam;
    }

    public boolean isTimtheoSanpham() {
        return timtheoSanpham;
    }

    public void setTimtheoSanpham(boolean timtheoSanpham) {
        this.timtheoSanpham = timtheoSanpham;
    }

    public String getMasanpham() {
        return masanpham;
    }

    public void setMasanpham(String masanpham) {
        this.masanpham = masanpham;
    }

    public boolean isTimtheoNhanvien() {
        return timtheoNhanvien;
    }

    public void setTimtheoNhanvien(boolean timtheoNhanvien) {
        this.timtheoNhanvien = timtheoNhanvien;
    }

    public String getManhanvien() {
        return manhanvien;
    }

    public void setManhanvien(String manhanvien) {
        this.manhanvien = manhanvien;
    }

    public int getTongsp() {
        return tongsp;
    }

    public void setTongsp(int tongsp) {
        this.tongsp = tongsp;
    }

}
