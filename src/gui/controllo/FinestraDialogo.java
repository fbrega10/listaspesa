package gui.controllo;

import exceptions.ListaSpesaException;
import gui.vista.ContentPanel;
import model.GestioneSpese;
import model.ListaSpesa;
import utils.Costanti;
import utils.StringUtils;

import javax.swing.JOptionPane;

public class FinestraDialogo {

    private GestioneSpese model;
    private ContentPanel contenutoGestioneSpese;

    public FinestraDialogo(GestioneSpese model, ContentPanel contenutoGestioneSpese) {
        this.model = model;
        this.contenutoGestioneSpese = contenutoGestioneSpese;
    }

    public void aggiungiLista() {
        String nomeLista = JOptionPane.showInputDialog("Inserisci il nome della lista : ");
        if (StringUtils.checkNullOrEmpty(nomeLista)) {
            try {
                ListaSpesa listaSpesa = new ListaSpesa(nomeLista);
                model.addListaSpesa(listaSpesa);
                mostraMessaggio("Aggiunta lista spesa : " + nomeLista);
            } catch (ListaSpesaException e) {
                mostraMessaggio("Errore nome lista non valido : " + nomeLista);
            }
        }
    }

    public void aggiungiCategoria() {
        String nomeCategoria = JOptionPane.showInputDialog("Inserisci una nuova categoria : ");
        if (StringUtils.checkNullOrEmpty(nomeCategoria)) {
            if (model.getCategorie().contains(nomeCategoria)) {
                mostraMessaggio("Categoria " + nomeCategoria + " gia' presente.");
            } else {
                model.addCategoria(nomeCategoria);
                mostraMessaggio("categoria " + nomeCategoria + " aggiunta.");
            }
        }
    }

    public void rimuoviLista() {
        ListaSpesa listaSpesa = this.contenutoGestioneSpese.getSpesaJList().getSelectedValue();
        if (listaSpesa != null) {
            model.rimuoviListaSpesa(listaSpesa);
            mostraMessaggio("lista rimossa con successo.");
        } else {
            mostraMessaggio("selezionare una lista prima di rimuoverla.");
        }
    }

    public void rimuoviCategoria() {
        String categoriaSelezionata = this.contenutoGestioneSpese.getCategorieJList().getSelectedValue();
        if (categoriaSelezionata == null) {
            mostraMessaggio("categoria non selezionata");
            return;
        }
        if (!categoriaSelezionata.equalsIgnoreCase(Costanti.CATEGORIA_DEFAULT)) {
            model.removeCategoria(categoriaSelezionata);
            mostraMessaggio("Categoria " + categoriaSelezionata + " eliminata con successo.");
        } else {
            mostraMessaggio("Selezionare una categoria prima di procedere con l'eliminazione");
        }
    }

    public void calcolaTotale() {
        ListaSpesa listaAttuale = this.contenutoGestioneSpese.getSpesaJList().getSelectedValue();
        if (listaAttuale != null) {
            mostraMessaggio("il totale dovuto e' : \n" + listaAttuale.calcolaCostoTotaleSpesa() + " €");
        } else {
            mostraMessaggio("Errore: selezionare una lista per vedere il totale. ");
        }
    }

    private static void mostraMessaggio(String messaggio) {
        JOptionPane.showMessageDialog(new JOptionPane(), messaggio);
    }
}
