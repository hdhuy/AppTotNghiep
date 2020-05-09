package Controller;

import Pojos.ProductSize;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.mindrot.jbcrypt.BCrypt;

public class Converter {

    public String mahoa(String txt) {
        String re=null;
        try {
            re = BCrypt.hashpw(txt, BCrypt.gensalt(12));
        } catch (Exception e) {
            System.out.println("loi ma hoa " + e);
        }
        return re;
    }

    public Set ListToSet(List lps) {
        Set sps = null;
        try {
            if (lps.size() > 0) {
                sps = new HashSet<>();
                for (Object sp : lps) {
                    sps.add(sp);
                }
            }
        } catch (Exception e) {
            System.out.println("loi ListToSet " + e);
        }
        return sps;
    }

    public List SetToList(Set sps) {
        List lps = null;
        try {
            if (sps.size() > 0) {
                lps = new ArrayList();
                for (Object sp : sps) {
                    lps.add(sp);
                }
            }

        } catch (Exception e) {
            System.out.println("loi SetToList " + e);
        }
        return lps;
    }

    public String formatText(int num) {
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

    public BufferedImage ByteToImage(byte[] blob) {
        BufferedImage re = null;
        try {
            BufferedImage theImage = ImageIO.read(new ByteArrayInputStream(blob));
            re = theImage;
        } catch (Exception e) {
        }
        return re;
    }

    public File chonFile() {
        File re = null;
        try {
            JFileChooser jdc = new JFileChooser("C:\\Users\\Admin\\Desktop");
            FileFilter imageFilter = new FileNameExtensionFilter(
                    "Image files", ImageIO.getReaderFileSuffixes());
            jdc.setFileFilter(imageFilter);
            int s = jdc.showOpenDialog(null);
            if (s == JFileChooser.APPROVE_OPTION) {
                File f = jdc.getSelectedFile();
                re = f;
            }
        } catch (Exception e) {
            System.out.println("loi chonFile converter " + e);
        }
        return re;
    }

    public byte[] chuyenThanhByte(File f) {
        byte[] re = null;
        try {
            BufferedImage bImage = ImageIO.read(new File(f.getPath()));
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ImageIO.write(bImage, "jpg", bos);
            re = bos.toByteArray();
        } catch (Exception e) {
            System.out.println("loi chuyenThanhByte converter " + e);
        }
        return re;
    }
}
