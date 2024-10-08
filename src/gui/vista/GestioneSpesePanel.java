package gui.vista;

import gui.controllo.ControlloGestioneSpese;
import model.GestioneSpese;

import javax.swing.JPanel;
import java.awt.BorderLayout;

public class GestioneSpesePanel extends JPanel {
    /**
     * @param model Prende in input al costruttore l'oggetto gestore e dispone
     *              i componenti secondo il layout scelto.
     */
    public GestioneSpesePanel(GestioneSpese model) {
        setLayout(new BorderLayout());

        ContentPanel contenutoGestioneSpese = new ContentPanel(model);
        ControlloGestioneSpese controllo = new ControlloGestioneSpese(contenutoGestioneSpese, model);
        OpsPanel operazioniGestioneSpese = new OpsPanel(controllo);
        SouthOpsPanel operazioniGestione = new SouthOpsPanel(controllo);

        add(operazioniGestioneSpese, BorderLayout.NORTH);
        add(operazioniGestione, BorderLayout.SOUTH);
        add(contenutoGestioneSpese, BorderLayout.CENTER);
    }
}
