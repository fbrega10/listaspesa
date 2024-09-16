package gui.controllo;

import gui.vista.ContentPanel;
import model.GestioneSpese;

import javax.swing.JButton;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static utils.Costanti.BUTTON_AGGIUNGI_ARTICOLO;
import static utils.Costanti.BUTTON_AGGIUNGI_CATEGORIA;
import static utils.Costanti.BUTTON_AGGIUNGI_LISTA;
import static utils.Costanti.BUTTON_CALCOLA_TOTALE;
import static utils.Costanti.BUTTON_CARICA_DA_FILE;
import static utils.Costanti.BUTTON_ESPORTA_LISTA;
import static utils.Costanti.BUTTON_FILTRA_PER_CATEGORIA;
import static utils.Costanti.BUTTON_FILTRA_PER_NOME;
import static utils.Costanti.BUTTON_MODIFICA_ARTICOLO;
import static utils.Costanti.BUTTON_PIU_COSTOSO;
import static utils.Costanti.BUTTON_RESET;
import static utils.Costanti.BUTTON_RIMUOVI_ARTICOLO;
import static utils.Costanti.BUTTON_RIMUOVI_CATEGORIA;
import static utils.Costanti.BUTTON_RIMUOVI_LISTA;

public class ControlloGestioneSpese implements ActionListener {

    private ContentPanel contenutoGestioneSpese;
    private GestioneSpese model;
    private FinestraDialogo finestraDialogo;

    /**
     * @param contenutoGestioneSpese oggetto di riferimento per la gestione della vista
     * @param model è l'istanza del gestore spese
     */
    public ControlloGestioneSpese(ContentPanel contenutoGestioneSpese, GestioneSpese model) {
        this.contenutoGestioneSpese = contenutoGestioneSpese;
        this.model = model;
        this.finestraDialogo = new FinestraDialogo(model, contenutoGestioneSpese);
    }

    /**
     * @param actionEvent E' l'handler delle azioni (eventi), a ogni evento
     *                    registrato corrisponde un'azione da applicare tramite
     *                    oggetto FinestraDialogo.
     *                    A seconda della provenienza (Button differente) ci sarà
     *                    un'azione precisa.
     *                    Alla fine verrà aggiornata la vista della gui.
     */
    @Override
    public void actionPerformed(ActionEvent actionEvent) {

        JButton source = (JButton) actionEvent.getSource();
        switch (source.getText()) {
            case BUTTON_AGGIUNGI_LISTA -> this.finestraDialogo.aggiungiLista();
            case BUTTON_RIMUOVI_LISTA -> this.finestraDialogo.rimuoviLista();
            case BUTTON_AGGIUNGI_CATEGORIA -> this.finestraDialogo.aggiungiCategoria();
            case BUTTON_RIMUOVI_CATEGORIA -> this.finestraDialogo.rimuoviCategoria();
            case BUTTON_CALCOLA_TOTALE -> this.finestraDialogo.calcolaTotale();
            case BUTTON_AGGIUNGI_ARTICOLO ->  this.finestraDialogo.aggiungiArticolo();
            case BUTTON_RIMUOVI_ARTICOLO -> this.finestraDialogo.rimuoviArticolo();
            case BUTTON_MODIFICA_ARTICOLO -> this.finestraDialogo.modificaArticolo();
            case BUTTON_FILTRA_PER_NOME -> this.finestraDialogo.filtraPerPrefissoNome();
            case BUTTON_FILTRA_PER_CATEGORIA -> this.finestraDialogo.filtraPerCategoria();
            case BUTTON_PIU_COSTOSO -> this.finestraDialogo.trovaPiuCostoso();
            case BUTTON_RESET -> this.finestraDialogo.reset();
            case BUTTON_CARICA_DA_FILE -> this.finestraDialogo.caricaDaFile();
            case BUTTON_ESPORTA_LISTA ->  this.finestraDialogo.esportaLista();
        }
        this.contenutoGestioneSpese.updateView();
    }
}
