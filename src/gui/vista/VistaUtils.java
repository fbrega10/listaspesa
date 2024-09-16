package gui.vista;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import java.awt.Color;
import java.awt.Font;

/**
 * Classe di utility che ci permette di modificare la view della nostra gui.
 */
public class VistaUtils {
    private static Color lightGreen = new Color(222, 228, 229);

    /**
     * @param jList         lista degli oggetti del pannello scrollabile (categorie/liste spesa)
     * @param titolo        titolo della JList
     * @param font          Font scelto da user.
     * @param justification Giustificazione del testo nella JList
     */
    public static void setJListTitledBorder(JList<?> jList, String titolo, Font font, int justification) {
        TitledBorder titledBorder = BorderFactory.createTitledBorder(titolo);
        titledBorder.setTitleColor(VistaUtils.lightGreen);
        titledBorder.setTitleJustification(justification);
        titledBorder.setTitleFont(font);
        jList.setBorder(titledBorder);
    }

    /**
     * @param jList prende in input una JList e la formatta.
     */
    public static void cellRendererGiustificato(JList<?> jList) {
        DefaultListCellRenderer renderer = (DefaultListCellRenderer) jList.getCellRenderer();
        renderer.setHorizontalAlignment(SwingConstants.CENTER);
        renderer.setForeground(new Color(222, 228, 229));
        jList.setFont(new Font("Italic", Font.ITALIC, 16));
    }
}
