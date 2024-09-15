package gui.vista;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import java.awt.Color;
import java.awt.Font;

public class VistaUtils {
    private static Color lightGreen = new Color(222, 228, 229);

    public static void setJListTitledBorder(JList<?> jList, String titolo, Font font, int justification){
        TitledBorder titledBorder = BorderFactory.createTitledBorder(titolo);
        titledBorder.setTitleColor(VistaUtils.lightGreen);
        titledBorder.setTitleJustification(justification);
        titledBorder.setTitleFont(font);
        jList.setBorder(titledBorder);
    }
    public static void cellRendererGiustificato(JList<?> jList){
        DefaultListCellRenderer renderer = (DefaultListCellRenderer) jList.getCellRenderer();
        renderer.setHorizontalAlignment(SwingConstants.CENTER);
        renderer.setForeground(new Color(222, 228, 229));
        jList.setFont(new Font("Italic", Font.ITALIC, 16));
    }
}
