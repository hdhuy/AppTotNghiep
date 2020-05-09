package Controller;

import Pojos.ProductSize;
import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

public class TblChitietTracuu_Color implements TableCellRenderer {

    private static final TableCellRenderer RENDERER = new DefaultTableCellRenderer();
    String result;

    public TblChitietTracuu_Color(String result) {
        this.result = result;
    }

    @Override

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

        Component c = RENDERER.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        String result = table.getModel().getValueAt(row, 2).toString();
        c.setBackground(java.awt.Color.white);
        c.setForeground(java.awt.Color.black);
        if (result.equals("5")||result.equals("4")||result.equals("3")||result.equals("2")||result.equals("1")) {
            c.setBackground(java.awt.Color.yellow);
            c.setForeground(java.awt.Color.black);
        }
        if (result.equals("0")) {
            c.setBackground(java.awt.Color.red);
            c.setForeground(java.awt.Color.white);
        }
        return c;
    }
}
