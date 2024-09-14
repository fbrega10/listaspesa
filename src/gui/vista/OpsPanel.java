package gui.vista;

import gui.controllo.ControlloGestioneSpese;

import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.FlowLayout;

import static utils.Costanti.BUTTON_AGGIUNGI_CATEGORIA;
import static utils.Costanti.BUTTON_AGGIUNGI_LISTA;
import static utils.Costanti.BUTTON_CALCOLA_TOTALE;
import static utils.Costanti.BUTTON_RIMUOVI_CATEGORIA;
import static utils.Costanti.BUTTON_RIMUOVI_LISTA;

public class OpsPanel extends JPanel {

    private ControlloGestioneSpese controllo;

    public OpsPanel(ControlloGestioneSpese controllo) {

        this.controllo = controllo;
        setLayout(new FlowLayout());

        JButton addLista = new JButton(BUTTON_AGGIUNGI_LISTA);
        add(addLista);

        JButton removeLista = new JButton(BUTTON_RIMUOVI_LISTA);
        add(removeLista);

        JButton addCategoria = new JButton(BUTTON_AGGIUNGI_CATEGORIA);
        add(addCategoria);

        JButton rimuoviCategoria = new JButton(BUTTON_RIMUOVI_CATEGORIA);
        add(rimuoviCategoria);

        JButton calcolaTotale = new JButton(BUTTON_CALCOLA_TOTALE);
        add(calcolaTotale);

        addLista.addActionListener(controllo);
        removeLista.addActionListener(controllo);
        addCategoria.addActionListener(controllo);
        rimuoviCategoria.addActionListener(controllo);
        calcolaTotale.addActionListener(controllo);
    }
}
