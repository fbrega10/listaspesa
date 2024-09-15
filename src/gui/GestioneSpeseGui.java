package gui;

import gui.vista.GestioneSpesePanel;
import model.GestioneSpese;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Color;

@SuppressWarnings("serial")
public class GestioneSpeseGui extends JFrame {

    private GestioneSpese model;

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
