package gui;

import gui.vista.GestioneSpesePanel;
import model.GestioneSpese;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *  GUI del gestore delle liste della spesa
 */
@SuppressWarnings("serial")
public class GestioneSpeseGui extends JFrame {

    private GestioneSpese model;

    /**
     * @param model Il costruttore prende come argomento l'istanza dell'oggetto gestione.
     */
    public GestioneSpeseGui(GestioneSpese model) {
        this.model = model;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(200, 200, 1150, 900);
        setTitle(" Gestione liste della spesa ");
        JPanel gestioneSpesePanel = new GestioneSpesePanel(model);
        setContentPane(gestioneSpesePanel);
        setVisible(true);
    }

}
