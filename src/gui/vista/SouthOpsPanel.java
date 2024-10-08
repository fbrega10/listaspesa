package gui.vista;

import gui.controllo.ControlloGestioneSpese;

import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.FlowLayout;

import static utils.Costanti.BUTTON_AGGIUNGI_ARTICOLO;
import static utils.Costanti.BUTTON_CALCOLA_TOTALE;
import static utils.Costanti.BUTTON_FILTRA_PER_CATEGORIA;
import static utils.Costanti.BUTTON_FILTRA_PER_NOME;
import static utils.Costanti.BUTTON_MODIFICA_ARTICOLO;
import static utils.Costanti.BUTTON_PIU_COSTOSO;
import static utils.Costanti.BUTTON_RIMUOVI_ARTICOLO;

public class SouthOpsPanel extends JPanel {

    private ControlloGestioneSpese controllo;

    /**
     * @param controllo Ha come argomento del costruttore il controllo di gestione.
     *                  Contiene i tasti posizionati a sud nel layout.
     */
    public SouthOpsPanel(ControlloGestioneSpese controllo) {

        this.controllo = controllo;
        setLayout(new FlowLayout());

        JButton calcolaTotale = new JButton(BUTTON_CALCOLA_TOTALE);
        add(calcolaTotale);

        JButton aggiungiArticolo = new JButton(BUTTON_AGGIUNGI_ARTICOLO);
        add(aggiungiArticolo);

        JButton rimuoviArticolo = new JButton(BUTTON_RIMUOVI_ARTICOLO);
        add(rimuoviArticolo);

        JButton modificaArticolo = new JButton(BUTTON_MODIFICA_ARTICOLO);
        add(modificaArticolo);

        JButton filtraPerCategoria = new JButton(BUTTON_FILTRA_PER_CATEGORIA);
        add(filtraPerCategoria);

        JButton filtraPerNome = new JButton(BUTTON_FILTRA_PER_NOME);
        add(filtraPerNome);

        JButton trovaPiuCostoso = new JButton(BUTTON_PIU_COSTOSO);
        add(trovaPiuCostoso);


        calcolaTotale.addActionListener(controllo);
        aggiungiArticolo.addActionListener(controllo);
        rimuoviArticolo.addActionListener(controllo);
        modificaArticolo.addActionListener(controllo);
        filtraPerCategoria.addActionListener(controllo);
        filtraPerNome.addActionListener(controllo);
        trovaPiuCostoso.addActionListener(controllo);
    }
}
