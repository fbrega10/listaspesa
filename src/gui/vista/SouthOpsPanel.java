package gui.vista;

import gui.controllo.ControlloGestioneSpese;

import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.FlowLayout;

import static utils.Costanti.BUTTON_AGGIUNGI_ARTICOLO;
import static utils.Costanti.BUTTON_CALCOLA_TOTALE;
import static utils.Costanti.BUTTON_FILTRA_PER_CATEGORIA;
import static utils.Costanti.BUTTON_RIMUOVI_ARTICOLO;

public class SouthOpsPanel extends JPanel {

    private ControlloGestioneSpese controllo;

    public SouthOpsPanel(ControlloGestioneSpese controllo) {

        this.controllo = controllo;
        setLayout(new FlowLayout());

        JButton calcolaTotale = new JButton(BUTTON_CALCOLA_TOTALE);
        add(calcolaTotale);

        JButton aggiungiArticolo = new JButton(BUTTON_AGGIUNGI_ARTICOLO);
        add(aggiungiArticolo);

        JButton rimuoviArticolo = new JButton(BUTTON_RIMUOVI_ARTICOLO);
        add(rimuoviArticolo);

        JButton filtraPerCategoria = new JButton(BUTTON_FILTRA_PER_CATEGORIA);
        add(filtraPerCategoria);

        calcolaTotale.addActionListener(controllo);
        aggiungiArticolo.addActionListener(controllo);
        rimuoviArticolo.addActionListener(controllo);
        filtraPerCategoria.addActionListener(controllo);
    }
}
