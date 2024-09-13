package gui.controllo;

import gui.vista.ContentPanel;
import model.GestioneSpese;

import javax.swing.JButton;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static utils.Costanti.BUTTON_AGGIUNGI_CATEGORIA;
import static utils.Costanti.BUTTON_AGGIUNGI_LISTA;
import static utils.Costanti.BUTTON_RIMUOVI_CATEGORIA;
import static utils.Costanti.BUTTON_RIMUOVI_LISTA;

public class ControlloGestioneSpese implements ActionListener {

    private ContentPanel contenutoGestioneSpese;
    private GestioneSpese model;
    private FinestraDialogo finestraDialogo;

    public ControlloGestioneSpese(ContentPanel contenutoGestioneSpese, GestioneSpese model) {
        this.contenutoGestioneSpese = contenutoGestioneSpese;
        this.model = model;
        this.finestraDialogo = new FinestraDialogo(model);
    }

    //E' l'handler delle azioni (eventi), vanno usate la vista e il modello
    @Override
    public void actionPerformed(ActionEvent actionEvent) {

        JButton source = (JButton) actionEvent.getSource();
        switch (source.getText()) {
            case BUTTON_AGGIUNGI_LISTA -> this.finestraDialogo.aggiungiLista();
            case BUTTON_RIMUOVI_LISTA -> this.finestraDialogo.rimuoviLista();
            case BUTTON_AGGIUNGI_CATEGORIA -> this.finestraDialogo.aggiungiCategoria();
            case BUTTON_RIMUOVI_CATEGORIA -> this.finestraDialogo.rimuoviCategoria();
        }
        this.contenutoGestioneSpese.updateView();
    }
}
