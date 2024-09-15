package commandline;

import exceptions.ArticoloException;
import exceptions.ListaSpesaException;
import io.ListWriterReader;
import model.Articolo;
import model.GestioneSpese;
import model.ListaSpesa;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Stream;

public class InterfacciaGrafica {

    private GestioneSpese gestioneSpese;
    private Scanner inputScanner;

    public InterfacciaGrafica(GestioneSpese gestioneSpese) {
        this.gestioneSpese = gestioneSpese;
        this.inputScanner = new Scanner(System.in);
    }

    public static void main(String[] args) {
        InterfacciaGrafica interfacciaGrafica = new InterfacciaGrafica(new GestioneSpese());
        interfacciaGrafica.inizio();
    }

    public void inizio() {
        boolean uscita = false;
        while (false == uscita) {
            System.out.println("\n\nCosa vuoi fare ? Seleziona un'azione : \n\n");
            System.out.println("1 - stampa liste della spesa");
            System.out.println("2 - stampa categorie attuali");
            System.out.println("3 - operazioni sulle liste");
            System.out.println("4 - operazioni sulle categorie");
            System.out.println("5 - esporta/importa su/da file");
            System.out.println("6 - svuota gestore della spesa");
            System.out.println("9 - esci (fine programma)");
            int scelta = this.inputScanner.nextInt();

            switch (scelta) {
                case 1 -> stampaListe();
                case 2 -> stampaCategorie();
                case 3 -> operazioniListe();
                case 4 -> operazioniCategorie();
                case 5 -> operazioniFile();
                case 6 -> svuotaGestore();
                case 9 -> uscita = true;
            }
        }
        exitSaluto();
    }

    public void stampaListe() {
        if (gestioneSpese.getListaSpese().isEmpty()) {
            System.out.println("Il gestore è vuoto, riempire per visualizzare liste.");
        } else {
            Optional.of(gestioneSpese)
                    .map(GestioneSpese::getListaSpese)
                    .orElse(Collections.emptyList())
                    .forEach(lista -> System.out.println(lista.toString()));
        }
        continuaEPulisci();
    }

    private void stampaCategorie() {
        Stream.of(gestioneSpese)
                .map(GestioneSpese::getCategorie)
                .forEach(System.out::println);
        continuaEPulisci();
    }

    private void operazioniListe() {
        pulisciSchermo();
        System.out.println("Quale operazione si vuole eseguire sulle liste?");
        System.out.println("1 - aggiungi nuova lista");
        System.out.println("2 - rimuovi lista");
        System.out.println("3 - aggiungi articolo");
        System.out.println("4 - rimuovi articolo");
        System.out.println("9 - esci");
        int scelta = inputScanner.nextInt();
        switch (scelta) {
            case 1 -> aggiungiLista();
            case 2 -> rimuoviLista();
            case 3 -> aggiungiArticolo();
            case 4 -> rimuoviArticolo();
            default -> pulisciSchermo();
        }
    }

    private void aggiungiArticolo() {
        System.out.println("Inserisci il nome della lista a cui aggiungere l'articolo :");
        String nomeLista = inputScanner.next();
        ListaSpesa listaSpesa = gestioneSpese.getListaByNome(nomeLista);
        if (listaSpesa == null) {
            System.out.println("Nome lista non presente nel gestore.");
            continuaEPulisci();
            return;
        }

        System.out.println("Inserisci il nome dell'articolo da inserire nella lista : ");
        String nome = inputScanner.next();
        System.out.println("Inserisci il prezzo :");
        BigDecimal prezzo = inputScanner.nextBigDecimal();
        System.out.println("Inserisci la quantità :");
        Integer quantita = inputScanner.nextInt();
        System.out.println("Inserisci la categoria :");
        String categoria = inputScanner.next();

        try {
            Articolo articolo = new Articolo(nome, prezzo, quantita, categoria);
            listaSpesa.addArticolo(articolo);
        } catch (ArticoloException e) {
            System.out.println("Dati inseriti non validi, prezzo e nome obbligatori! Riprovare");
        }
        continuaEPulisci();
    }

    private void rimuoviArticolo() {
        System.out.println("Inserisci il nome della lista a cui rimuovere l'articolo :");
        String nomeLista = inputScanner.next();
        ListaSpesa listaSpesa = gestioneSpese.getListaByNome(nomeLista);
        if (listaSpesa == null) {
            System.out.println("Nome lista non presente nel gestore.");
            continuaEPulisci();
            return;
        }

        System.out.println("Inserisci il nome dell'articolo da rimuovere : ");
        String nome = inputScanner.next();
        Articolo articolo = listaSpesa.getArticoloByNome(nome);
        if (listaSpesa.getArticoloByNome(nome) != null) listaSpesa.removeArticolo(articolo);
        else {
            System.out.println("Articolo non presente, riprovare con un articolo valido.");
        }
        continuaEPulisci();
    }

    private void operazioniCategorie() {
        pulisciSchermo();
        System.out.println("Quale operazione vuoi eseguire? ");
        System.out.println("1 - Aggiungi una categoria");
        System.out.println("2 - Rimuovi una categoria");
        System.out.println("9 - Esci");
        int scelta = inputScanner.nextInt();
        switch (scelta) {
            case 1 -> aggiungiCategoria();
            case 2 -> rimuoviCategoria();
            default -> pulisciSchermo();
        }
    }

    private void aggiungiCategoria() {
        pulisciSchermo();
        System.out.println("Inserisci la categoria da aggiungere :");
        String categoria = inputScanner.next();
        if (gestioneSpese.addCategoria(categoria)) {
            System.out.println("Categoria aggiunta con successo!");
        } else {
            System.out.println("categoria già presente, riprovare.");
        }
        continuaEPulisci();
    }

    private void rimuoviCategoria() {
        pulisciSchermo();
        System.out.println("Inserisci la categoria da rimuovere :");
        String categoria = inputScanner.next();
        if (gestioneSpese.removeCategoria(categoria)) {
            System.out.println("Categoria rimossa con successo!");
        } else {
            System.out.println("Categoria non presente.");
        }
        continuaEPulisci();
    }

    private void rimuoviLista() {
        System.out.println("Inserisci il nome della lista da rimuovere :");
        String nomeLista = inputScanner.next();
        if (gestioneSpese.rimuoviListaSpesa(nomeLista)) {
            System.out.println("Lista della spesa eliminata correttamente!");
        } else {
            System.out.println("Lista della spesa non trovata, riprovare con un nome valido.");
        }
        continuaEPulisci();
    }

    private void aggiungiLista() {
        System.out.println("Inserisci il nome della nuova lista :");
        String nomeLista = inputScanner.next();
        try {
            ListaSpesa nuovaLista = new ListaSpesa(nomeLista);
            gestioneSpese.addListaSpesa(nuovaLista);
            System.out.println("Lista '" + nomeLista + "' inserita con successo.");
            continuaEPulisci();
        } catch (ListaSpesaException e) {
            System.out.println("Nome inserito non valido, riprovare.");
        }
    }

    private void operazioniFile(){
        System.out.println("Quale operazione vuoi eseguire?");
        System.out.println("1 - carica lista da file");
        System.out.println("2 - scrivi lista su file");
        System.out.println("9 - esci");
        int scelta = inputScanner.nextInt();
        switch (scelta){
            case 1 -> caricaListaDaFile();
            case 2 -> scriviListaSuFile();
        }
        continuaEPulisci();
    }

    private void caricaListaDaFile(){
        System.out.println("path attuale : " + new File("").getAbsoluteFile());
        System.out.println("Inserisci il path completo al file (incluso) :");
        String path = inputScanner.next();
        try {
            ListaSpesa listaSpesa = ListWriterReader.readListaDaFile(path);
            gestioneSpese.addListaSpesa(listaSpesa);
            System.out.println("Lista inserita con successo!");
        }
        catch (IOException | ClassNotFoundException e){
           System.out.println("Errore nella lettura del file.");
        }
        continuaEPulisci();
    }

    private void scriviListaSuFile(){
        pulisciSchermo();
        System.out.println("Inserisci il nome della lista :");
        String nomeLista = inputScanner.next();
        ListaSpesa lista = gestioneSpese.getListaByNome(nomeLista);
        if (lista == null){
            System.out.println("Lista non presente, riprovare");
            continuaEPulisci();
            return;
        }
        System.out.println("Inserisci nome file di destinazione :");
        String nomeFile = inputScanner.next();
        try{
        ListWriterReader.writeListaSuFile(nomeFile, lista);
        System.out.println("Operazione eseguita con successo!");
        }catch (IOException e){
            System.out.println("Errore nella scrittura file, riprovare.");
            continuaEPulisci();
        }
        continuaEPulisci();
    }

    private void continuaEPulisci() {
        System.out.println("\n\npremi un tasto per continuare...");
        try {
            System.in.read();
        } catch (Exception e) {
        }
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    private void svuotaGestore() {
        gestioneSpese.resetGestioneSpese();
        System.out.println("Gestore svuotato con successo!");
        continuaEPulisci();
    }

    private void pulisciSchermo() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    private void exitSaluto() {
        System.out.println("\n\n\nProgramma terminato. Arrivederci!");
        gestioneSpese.resetGestioneSpese();
    }
}
