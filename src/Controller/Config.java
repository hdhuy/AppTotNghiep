package Controller;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;
import javax.swing.JOptionPane;

public class Config {

    String tencuahang;
    String fanpage;
    String diachi;
    String sodienthoai;
    
    String slide1;
    String slide2;
    String slide3;
    String slide4;
    
    boolean hienthigiaodienin;
    boolean luuhoadon;
    String linkhoadon;

    public void setCuahang(String tencuahang, String fanpage, String diachi, String sodienthoai) {
        Properties prop = new Properties();
        OutputStream output = null;
        try {
            output = new FileOutputStream("src/Config_File/configCuahang.properties");
            // setCuahang the properties value
            prop.setProperty("tencuahang", tencuahang);
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
    public void setPhanmem(String slide1, String slide2, String slide3, String slide4) {
        Properties prop = new Properties();
        OutputStream output = null;
        try {
            output = new FileOutputStream("src/Config_File/configPhanmem.properties");
            // setCuahang the properties value
            prop.setProperty("slide1", slide1);
            prop.setProperty("slide2", slide2);
            prop.setProperty("slide3", slide3);
            prop.setProperty("slide4", slide4);
            // save properties to project root folder
            prop.store(output, null);
            output.close();
        } catch (Exception io) {
            System.out.println("loi out put " + io);
        }
    }
    public boolean getCuahang() {
        boolean re=false;
        try {
            Properties prop = new Properties();
            String propFileName = "src/Config_File/configCuahang.properties";
            InputStream inputStream = new FileInputStream(propFileName);
            if (inputStream != null) {
                prop.load(inputStream);
                setTencuahang(prop.getProperty("tencuahang"));
                setFanpage(prop.getProperty("fanpage"));
                setDiachi(prop.getProperty("diachi"));
                setSodienthoai(prop.getProperty("sodienthoai"));
                re=true;
                inputStream.close();
            } else {
                JOptionPane.showMessageDialog(null, "Không tìm thấy đường dẫn "+propFileName);
            }
        } catch (Exception e) {
            System.out.println("Exception: " + e);
        }
        return re;
    }
    public boolean getPhanmem() {
        boolean re=false;
        try {
            Properties prop = new Properties();
            String propFileName = "src/Config_File/configPhanmem.properties";
            InputStream inputStream = new FileInputStream(propFileName);
            if (inputStream != null) {
                prop.load(inputStream);
                setSlide1(prop.getProperty("slide1"));
                setSlide2(prop.getProperty("slide2"));
                setSlide3(prop.getProperty("slide3"));
                setSlide4(prop.getProperty("slide4"));
                re=true;
                inputStream.close();
            } else {
                JOptionPane.showMessageDialog(null, "Không tìm thấy đường dẫn "+propFileName);
            }
        } catch (Exception e) {
            System.out.println("Exception: " + e);
        }
        return re;
    }

    public String getTencuahang() {
        return tencuahang;
    }

    public void setTencuahang(String tencuahang) {
        this.tencuahang = tencuahang;
    }

    public String getFanpage() {
        return fanpage;
    }

    public void setFanpage(String fanpage) {
        this.fanpage = fanpage;
    }

    public String getDiachi() {
        return diachi;
    }

    public void setDiachi(String diachi) {
        this.diachi = diachi;
    }

    public String getSodienthoai() {
        return sodienthoai;
    }

    public void setSodienthoai(String sodienthoai) {
        this.sodienthoai = sodienthoai;
    }

    public String getSlide1() {
        return slide1;
    }

    public void setSlide1(String slide1) {
        this.slide1 = slide1;
    }

    public String getSlide2() {
        return slide2;
    }

    public void setSlide2(String slide2) {
        this.slide2 = slide2;
    }

    public String getSlide3() {
        return slide3;
    }

    public void setSlide3(String slide3) {
        this.slide3 = slide3;
    }

    public String getSlide4() {
        return slide4;
    }

    public void setSlide4(String slide4) {
        this.slide4 = slide4;
    }

    public boolean isHienthigiaodienin() {
        return hienthigiaodienin;
    }

    public void setHienthigiaodienin(boolean hienthigiaodienin) {
        this.hienthigiaodienin = hienthigiaodienin;
    }

    public boolean isLuuhoadon() {
        return luuhoadon;
    }

    public void setLuuhoadon(boolean luuhoadon) {
        this.luuhoadon = luuhoadon;
    }

    public String getLinkhoadon() {
        return linkhoadon;
    }

    public void setLinkhoadon(String linkhoadon) {
        this.linkhoadon = linkhoadon;
    }

}
