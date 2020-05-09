package Controller;

import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

public class TblTracuu_Color implements TableCellRenderer {

    private static final TableCellRenderer RENDERER = new DefaultTableCellRenderer();

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

        JLabel c = (JLabel) RENDERER.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        String result = table.getModel().getValueAt(row, 1).toString();
        QL_SanPham q = new QL_SanPham();
        String e = q.laySPToMau(result);
        if (e.equals("white")) {
            c.setBackground(java.awt.Color.white);
            c.setForeground(java.awt.Color.black);
        }
        if (e.equals("yellow")) {
                c.setBackground(java.awt.Color.yellow);
                c.setForeground(java.awt.Color.black);
            }
        if (e.equals("red")) {
            c.setBackground(java.awt.Color.red);
            c.setForeground(java.awt.Color.white);
        }
        return c;
    }
//    public Component prepareRenderer(TableCellRenderer r,int rw,int col){
//        Component c=super.prepareRenderer(r,rw,col);
//        if(col==0){
//            c.setBackground(java.awt.Color.red);
//        }
//        return c;
//    }
}
