package gui.controllo;

import exceptions.ArticoloException;
import exceptions.ListaSpesaException;
import gui.vista.ContentPanel;
import io.ListWriterReader;
import model.Articolo;
import model.GestioneSpese;
import model.ListaSpesa;
import utils.Costanti;
import utils.StringUtils;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.GridLayout;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;

/**
 * Classe che gestisce gli eventi della nostra GUI.
 */
public class FinestraDialogo {

    private GestioneSpese model;
    private ContentPanel contenutoGestioneSpese;

    /**
     * @param model modello dati
     * @param contenutoGestioneSpese pannello che controlla le liste della gui.
     */
    public FinestraDialogo(GestioneSpese model, ContentPanel contenutoGestioneSpese) {
        this.model = model;
        this.contenutoGestioneSpese = contenutoGestioneSpese;
    }

    /**
     * Aggiunge una lista spesa al modello dati e successivamente aggiorna la GUI.
     */
    public void aggiungiLista() {
        String nomeLista = finestraInput("Inserisci il nome della lista : ");
        if (StringUtils.checkNullOrEmpty(nomeLista)) {
            try {
                ListaSpesa listaSpesa = new ListaSpesa(nomeLista);
                model.addListaSpesa(listaSpesa);
                mostraMessaggio("Aggiunta lista spesa : '" + nomeLista + "'");
            } catch (ListaSpesaException e) {
                outputErrore("Errore nome lista non valido : '" + nomeLista + "'");
            }
        }
    }

    /**
     * Aggiunge una categoria al gestore.
     */
    public void aggiungiCategoria() {
        String nomeCategoria = finestraInput("Inserisci una nuova categoria : ");
        if (StringUtils.checkNullOrEmpty(nomeCategoria)) {
            if (model.getCategorie().contains(nomeCategoria)) {
                mostraMessaggio("Categoria '" + nomeCategoria + "' gia' presente.");
            } else {
                model.addCategoria(nomeCategoria);
                mostraMessaggio("categoria '" + nomeCategoria + "' aggiunta.");
            }
        }
    }

    /**
     * Rimuove la lista selezionata, se presente.
     */
    public void rimuoviLista() {
        ListaSpesa listaSpesa = retrieveListaSelezionata();
        if (listaSpesa != null) {
            model.rimuoviListaSpesa(listaSpesa);
            mostraMessaggio("lista rimossa con successo.");
        } else {
            outputErrore("selezionare una lista per rimuoverla!");
        }
    }

    /**
     * Rimuove la categoria che viene selezionata tramite input.
     */
    public void rimuoviCategoria() {
        String categoriaSelezionata = this.contenutoGestioneSpese.getCategorieJList().getSelectedValue();
        if (categoriaSelezionata == null) {
            outputErrore("categoria non selezionata.");
            return;
        }
        if (!categoriaSelezionata.equalsIgnoreCase(Costanti.CATEGORIA_DEFAULT)) {
            model.removeCategoria(categoriaSelezionata);
            mostraMessaggio("Categoria '" + categoriaSelezionata + "' eliminata con successo.");
        } else {
            outputErrore("Selezionare una categoria prima di procedere con l'eliminazione!");
        }
    }

    /**
     * Restituisce il totale rispetto alla lista selezionata.
     */
    public void calcolaTotale() {
        ListaSpesa listaAttuale = retrieveListaSelezionata();
        if (listaAttuale != null) {
            mostraMessaggio("il totale dovuto e' : \n" + listaAttuale.calcolaCostoTotaleSpesa() + " €");
        } else {
            outputErrore("Errore: selezionare una lista per vederne il totale.");
        }
    }

    /**
     * Aggiunge un articolo alla lista selezionata.
     * Nome e prezzo sono campi obbligatori.
     */
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
                    Integer quantita = quantitaField.getText() == null || quantitaField.getText().isEmpty() ?
                            null : Integer.parseInt(quantitaField.getText());
                    String categoria = categoriaField.getText().isEmpty() ? null : categoriaField.getText();

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

    /**
     * Rimuove un articolo dalla lista selezionata.
     */
    public void rimuoviArticolo() {
        ListaSpesa listaAttuale = retrieveListaSelezionata();
        if (listaAttuale == null) {
            outputErrore("Errore : nessuna lista selezionata! Riprovare prego.");
            return;
        }
        int sizeLista = listaAttuale.size();
        String result = finestraInput("Nome articolo da rimuovere");
        if (listaAttuale.removeArticolo(result)) {
            mostraMessaggio("Articolo rimosso correttamente.");
        }
        else{
            outputErrore("Articolo non presente, riprovare con articolo valido.");
        }
    }

    /**
     * Modifica un articolo rispetto alla lista selezionata.
     */
    public void modificaArticolo(){
        ListaSpesa listaAttuale = retrieveListaSelezionata();
        if (listaAttuale == null) {
            outputErrore("Errore : nessuna lista selezionata! Riprovare prego.");
            return;
        }
        Articolo articolo = listaAttuale.getArticoloByNome(finestraInput("Nome articolo da modificare"));
        if (articolo != null){

            JTextField prezzoField = new JTextField(articolo.getPrezzoUnitario().toString());
            JTextField quantitaField = new JTextField(String.valueOf(articolo.getQuantita()));
            JTextField categoriaField = new JTextField(articolo.getCategoria());
            JPanel panel = new JPanel(new GridLayout(3, 2));
            panel.add(new JLabel("Prezzo Unitario:"));
            panel.add(prezzoField);
            panel.add(new JLabel("Quantità:"));
            panel.add(quantitaField);
            panel.add(new JLabel("Categoria:"));
            panel.add(categoriaField);

            int result = JOptionPane.showConfirmDialog(new JOptionPane(""), panel, "Modifica Articolo " + articolo.getNomeArticolo() , JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                try {
                    Articolo copia = new Articolo(articolo.getNomeArticolo(), new BigDecimal(prezzoField.getText()),
                            Integer.parseInt(quantitaField.getText().isEmpty() ? null : quantitaField.getText()),
                            categoriaField.getText().isEmpty() ? null : categoriaField.getText());
                    if (!copia.equals(articolo)) {
                        //fatto per recepire l'aggiornamento a model ed aggiungere eventuali nuove categorie
                        listaAttuale.removeArticolo(articolo);
                        listaAttuale.addArticolo(copia);
                        model.rimuoviListaSpesa(listaAttuale);
                        model.addListaSpesa(listaAttuale);
                        mostraMessaggio("Articolo modificato correttamente.");
                    } else mostraMessaggio("Articolo non modificato.");
                } catch (ArticoloException | NumberFormatException e) {
                    outputErrore("Errore: valori inseriti non corretti. Prezzo e quantita' devono essere interi");
                }
            } else mostraMessaggio("Nessuna azione eseguita.");
        }


    }

    /**
     * Filtra e restituisce gli elementi della lista selezionata la cui
     * categoria corrisponde a quella presa in input dall'utente.
     */
    public void filtraPerCategoria() {

        ListaSpesa listaSpesa = retrieveListaSelezionata();
        if (listaSpesa == null) {
            outputErrore("Selezionare una lista prima di filtrare per categoria.");
            return;
        }
        String categoria = finestraInput("Inserisci filtro categoria: ");
        if (categoria != null) {
            StringBuilder sb = new StringBuilder();
            Optional.of(listaSpesa)
                    .map(lista -> lista.getArticoliDiCategoria(categoria))
                    .orElse(Collections.emptyList())
                    .forEach(articolo -> sb.append("\n").append(articolo.toString()));
            if (sb.isEmpty()) {
                mostraMessaggio("Nessun risultato per la categoria selezionata.");
            }
            mostraMessaggio("Gli articoli per cui risulta un match sono : " + sb.toString());
        } else {
            outputErrore("Errore : inserire categoria valida.");
        }
    }

    /**
     * Filtra per un prefisso (Stringa) chiesta all'utente e restituisce gli articoli
     * il cui nome matcha con il prefisso.
     */
    public void filtraPerPrefissoNome() {

        ListaSpesa listaSpesa = retrieveListaSelezionata();
        if (listaSpesa == null) {
            outputErrore("Selezionare una lista prima di filtrare per nome.");
            return;
        }
        String prefissoNome = finestraInput("Inserisci filtro prefisso nome");
        if (prefissoNome != null) {
            StringBuilder sb = new StringBuilder();
            Optional.of(listaSpesa)
                    .map(lista -> lista.getArticoliDiNomePrefix(prefissoNome))
                    .orElse(Collections.emptyList())
                    .forEach(articolo -> sb.append("\n").append(articolo.toString()));

            if (sb.isEmpty()) {
                mostraMessaggio("Nessun risultato per il nome inserito.");
            }
            mostraMessaggio("Gli articoli per cui risulta un match sono : " + sb.toString());
        } else {
            outputErrore("Prefisso non inserito, riprovare.");
        }
    }

    /**
     * Carica da file la lista della spesa (deserializzata) e la aggiunge
     * alla lista dell'istanza del gestore.
     */
    public void caricaDaFile() {
        JFrame frame = new JFrame("Menu file");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JFileChooser fileChooser = new JFileChooser();
        int valore = fileChooser.showOpenDialog(frame);
        try {
            if (valore == JFileChooser.APPROVE_OPTION) {
                ListaSpesa listaSpesa = ListWriterReader.readListaDaFile(fileChooser.getSelectedFile().toString());
                model.addListaSpesa(listaSpesa);
                mostraMessaggio("Aggiunta lista spesa da file : " + fileChooser.getSelectedFile());
            }
        } catch (IOException | ClassNotFoundException e) {
            outputErrore("Errore nell'apertura file, riprovare prego.");
        }
    }

    /**
     * Serializza la lista selezionata e la stampa in output su un file del nome deciso
     * dall'utente (sotto la directory /src/resources del progetto)
     */
    public void esportaLista() {
        ListaSpesa listaSpesa = retrieveListaSelezionata();
        if (listaSpesa == null) {
            outputErrore("Errore : selezionare una lista prima di esportarla");
            return;
        }
        String fileName = finestraInput("Inserisci nome file di output: ");
        if (fileName != null && !fileName.isEmpty()) {
            try {
                ListWriterReader.writeListaSuFile(fileName, listaSpesa);
                mostraMessaggio("File correttamente scritto in : /src/resources.");
            } catch (IOException e) {
                outputErrore("Errore nella scrittura su file, riprovare prego.");
            }
        } else {
            outputErrore("Errore! Indicare nome file valido");
        }
    }

    /**
     * Trova e restituisce l'articolo col prezzo unitario più costoso.
     */
    public void trovaPiuCostoso() {
        ListaSpesa listaSpesa = retrieveListaSelezionata();
        if (listaSpesa == null) {
            outputErrore("Selezionare lista prima per trovare articolo più costoso");
            return;
        }
        if (listaSpesa.isEmpty()) {
            mostraMessaggio("Lista selezionata vuota");
            return;
        }
        mostraMessaggio("L'articolo più costoso è: \n " + listaSpesa.getArticoloPiuCostoso());
    }

    /**
     * Riporta il gestore alle impostazioni di fabbrica : cancella categorie e liste spesa.
     */
    public void reset() {
        model.resetGestioneSpese();
        mostraMessaggio("Reset avvenuto con successo!");
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

    private static String finestraInput(String messaggio) {
        return JOptionPane.showInputDialog(messaggio);
    }
}
