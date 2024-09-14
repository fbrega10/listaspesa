package gui.controllo;

import exceptions.ArticoloException;
import exceptions.ListaSpesaException;
import gui.vista.ContentPanel;
import model.Articolo;
import model.GestioneSpese;
import model.ListaSpesa;
import utils.Costanti;
import utils.StringUtils;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.GridLayout;
import java.math.BigDecimal;

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
                outputErrore("Errore nome lista non valido : " + nomeLista);
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
        ListaSpesa listaSpesa = retrieveListaSelezionata();
        if (listaSpesa != null) {
            model.rimuoviListaSpesa(listaSpesa);
            mostraMessaggio("lista rimossa con successo.");
        } else {
            outputErrore("selezionare una lista prima di rimuoverla.");
        }
    }

    public void rimuoviCategoria() {
        String categoriaSelezionata = this.contenutoGestioneSpese.getCategorieJList().getSelectedValue();
        if (categoriaSelezionata == null) {
            outputErrore("categoria non selezionata");
            return;
        }
        if (!categoriaSelezionata.equalsIgnoreCase(Costanti.CATEGORIA_DEFAULT)) {
            model.removeCategoria(categoriaSelezionata);
            mostraMessaggio("Categoria " + categoriaSelezionata + " eliminata con successo.");
        } else {
            outputErrore("Selezionare una categoria prima di procedere con l'eliminazione");
        }
    }

    public void calcolaTotale() {
        ListaSpesa listaAttuale = retrieveListaSelezionata();
        if (listaAttuale != null) {
            mostraMessaggio("il totale dovuto e' : \n" + listaAttuale.calcolaCostoTotaleSpesa() + " €");
        } else {
            outputErrore("Errore: selezionare una lista per vedere il totale. ");
        }
    }

    public void aggiungiArticolo() {
        ListaSpesa listaAttuale = retrieveListaSelezionata();
        if (listaAttuale != null) {
            JTextField nomeField = new JTextField();
            JTextField prezzoField = new JTextField();
            JTextField quantitaField = new JTextField();
            JTextField categoriaField = new JTextField();
            JPanel panel = new JPanel(new GridLayout(4, 2));
            panel.add(new JLabel("Nome Articolo:"));
            panel.add(nomeField);
            panel.add(new JLabel("Prezzo Unitario:"));
            panel.add(prezzoField);
            panel.add(new JLabel("Quantità:"));
            panel.add(quantitaField);
            panel.add(new JLabel("Categoria:"));
            panel.add(categoriaField);

            int result = JOptionPane.showConfirmDialog(new JOptionPane(""), panel, "Aggiungi Articolo", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                try {

                    String nomeArticolo = nomeField.getText();
                    BigDecimal prezzoUnitario = new BigDecimal(prezzoField.getText());
                    int quantita = Integer.parseInt(quantitaField.getText());
                    String categoria = categoriaField.getText();

                    Articolo nuovoArticolo = new Articolo(nomeArticolo, prezzoUnitario, quantita, categoria);
                    listaAttuale.addArticolo(nuovoArticolo);
                    model.addCategoria(categoria);

                } catch (NumberFormatException e) {
                    outputErrore("Errore nei dati inseriti, i campi prezzo/quantita' devono essere numeri.");
                } catch (ArticoloException e) {
                    outputErrore("Errore nei dati inseriti : " + e.getMessage());
                }
            }
        } else {
            outputErrore("Errore: selezionare una lista per rimuovere un articolo.");
        }
    }

    public void rimuoviArticolo() {
        ListaSpesa listaAttuale = retrieveListaSelezionata();
        if (listaAttuale != null) {
            String result = JOptionPane.showInputDialog("Inserisci nome articolo da rimuovere : ");
        }

    }

    private static void mostraMessaggio(String messaggio) {
        JOptionPane.showMessageDialog(new JOptionPane(), messaggio);
    }

    private ListaSpesa retrieveListaSelezionata() {
        return this.contenutoGestioneSpese.getSpesaJList().getSelectedValue();
    }

    private static void outputErrore(String messaggio) {
        JOptionPane.showMessageDialog(new JOptionPane(), messaggio, "Errore", JOptionPane.ERROR_MESSAGE);
    }
}
