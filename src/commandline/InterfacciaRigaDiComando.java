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

public class InterfacciaRigaDiComando {

    private GestioneSpese gestioneSpese;
    private Scanner inputScanner;

    public InterfacciaRigaDiComando(GestioneSpese gestioneSpese) {
        this.gestioneSpese = gestioneSpese;
        this.inputScanner = new Scanner(System.in);
    }

    public void inizio() {
        boolean uscita = false;
        while (!uscita) {
            stampaMenuPrincipale();
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
        pulisciSchermo();
        if (gestioneSpese.getListaSpese().isEmpty()) {
            System.out.println("Il gestore è vuoto, aggiungere almeno una lista.");
        } else {
            stampaContenutoListe();
        }
        continuaEPulisci();
    }

    private void stampaCategorie() {
        pulisciSchermo();
        stampaContenutoCategorie();
        continuaEPulisci();
    }

    private void operazioniListe() {
        pulisciSchermo();
        stampaContenutoListe();
        stampaMenuOperazioniListe();
        int scelta = inputScanner.nextInt();
        switch (scelta) {
            case 1 -> aggiungiLista();
            case 2 -> rimuoviLista();
            case 3 -> aggiungiArticolo();
            case 4 -> rimuoviArticolo();
            case 5 -> modificaArticolo();
            default -> pulisciSchermo();
        }
    }

    private void aggiungiArticolo() {
        String nomeLista = getInputConMessaggio("Inserisci il nome della lista a cui aggiungere l'articolo :");
        ListaSpesa listaSpesa = gestioneSpese.getListaByNome(nomeLista);
        if (listaSpesa == null) {
            System.out.println("Nome lista non presente nel gestore.");
            continuaEPulisci();
            return;
        }
        try {
            String nome = getInputConMessaggio("Inserisci il nome dell'articolo da inserire nella lista : ");
            BigDecimal prezzo = new BigDecimal(getInputConMessaggio("Inserisci il prezzo :"));
            Integer quantita = Integer.parseInt(getInputConMessaggio("Inserisci la quantità :"));
            String categoria = getInputConMessaggio("Inserisci la categoria :");
            Articolo articolo = new Articolo(nome, prezzo, quantita, categoria);
            listaSpesa.addArticolo(articolo);
        } catch (ArticoloException | NumberFormatException e) {
            System.out.println("Dati inseriti non validi, prezzo e nome obbligatori! Riprovare");
        }
        continuaEPulisci();
    }

    private void modificaArticolo() {
        String nomeLista = getInputConMessaggio("Inserisci il nome della lista a cui rimuovere l'articolo :");
        ListaSpesa listaSpesa = gestioneSpese.getListaByNome(nomeLista);
        if (listaSpesa == null) {
            System.out.println("Nome lista non presente nel gestore.");
            continuaEPulisci();
            return;
        }
        String nome = getInputConMessaggio("Inserisci il nome dell'articolo da modificare : ");
        Articolo articolo = listaSpesa.getArticoloByNome(nome);
        if (articolo != null) {
            System.out.println("articolo selezionato: " + articolo);
            String categoria = getInputConMessaggio("Inserisci nuova categoria : (oppure 'n' per mantenere la precedente)");
            String prez = getInputConMessaggio("Inserisci nuovo prezzo : (oppure 'n' per manterere il precedente)");
            String quant = getInputConMessaggio("Inserisci nuova quantità : (oppure 'n' per manterere la precedente)");
            categoria = categoria.equalsIgnoreCase("n") ? articolo.getCategoria() : categoria;
            BigDecimal prezzo;
            int quantita;
            try {
                prezzo = prez.equalsIgnoreCase("n") ? articolo.getPrezzoUnitario() : new BigDecimal(prez);
                quantita = quant.equalsIgnoreCase("n") ? articolo.getQuantita() : Integer.parseInt(quant);
                Articolo articoloCopia = new Articolo(articolo.getNomeArticolo(), prezzo, quantita, categoria);
                listaSpesa.removeArticolo(articolo);
                listaSpesa.addArticolo(articoloCopia);
            } catch (NumberFormatException | ArticoloException e) {
                System.out.println("Errore nell'inserimento dei dati, prezzo e quantità devono essere numeri");
            }
        }else System.out.println("Articolo non presente, riprovare prego.");
        continuaEPulisci();
    }

    private void rimuoviArticolo() {
        String nomeLista = getInputConMessaggio("Inserisci il nome della lista a cui rimuovere l'articolo :");
        ListaSpesa listaSpesa = gestioneSpese.getListaByNome(nomeLista);
        if (listaSpesa == null) {
            System.out.println("Nome lista non presente nel gestore.");
            continuaEPulisci();
            return;
        }
        String nome = getInputConMessaggio("Inserisci il nome dell'articolo da rimuovere : ");
        Articolo articolo = listaSpesa.getArticoloByNome(nome);
        if (listaSpesa.getArticoloByNome(nome) != null) listaSpesa.removeArticolo(articolo);
        else {
            System.out.println("Articolo non presente, riprovare con un articolo valido.");
        }
        continuaEPulisci();
    }

    private void operazioniCategorie() {
        pulisciSchermo();
        stampaContenutoCategorie();
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
        String categoria = getInputConMessaggio("Inserisci la categoria da aggiungere :");
        if (gestioneSpese.addCategoria(categoria)) {
            System.out.println("Categoria aggiunta con successo!");
        } else {
            System.out.println("categoria già presente, riprovare.");
        }
        continuaEPulisci();
    }

    private void rimuoviCategoria() {
        pulisciSchermo();
        String categoria = getInputConMessaggio("Inserisci la categoria da rimuovere :");
        if (gestioneSpese.removeCategoria(categoria)) {
            System.out.println("Categoria rimossa con successo!");
        } else {
            System.out.println("Categoria non presente.");
        }
        continuaEPulisci();
    }

    private void rimuoviLista() {
        String nomeLista = getInputConMessaggio("Inserisci il nome della lista da rimuovere :");
        if (gestioneSpese.rimuoviListaSpesa(nomeLista)) {
            System.out.println("Lista della spesa eliminata correttamente!");
        } else {
            System.out.println("Lista della spesa non trovata, riprovare con un nome valido.");
        }
        continuaEPulisci();
    }

    private void aggiungiLista() {
        String nomeLista = getInputConMessaggio("Inserisci il nome della nuova lista :");
        try {
            ListaSpesa nuovaLista = new ListaSpesa(nomeLista);
            gestioneSpese.addListaSpesa(nuovaLista);
            System.out.println("Lista '" + nomeLista + "' inserita con successo.");
            continuaEPulisci();
        } catch (ListaSpesaException e) {
            System.out.println("Nome inserito non valido, riprovare.");
        }
    }

    private void operazioniFile() {
        stampaMenuOperazioniFile();
        int scelta = inputScanner.nextInt();
        switch (scelta) {
            case 1 -> caricaListaDaFile();
            case 2 -> scriviListaSuFile();
            default -> pulisciSchermo();
        }
        continuaEPulisci();
    }

    private void caricaListaDaFile() {
        System.out.println("path attuale : " + new File("").getAbsoluteFile());
        String path = getInputConMessaggio("Inserisci il path completo al file (incluso) :");
        try {
            ListaSpesa listaSpesa = ListWriterReader.readListaDaFile(path);
            gestioneSpese.addListaSpesa(listaSpesa);
            System.out.println("Lista inserita con successo!");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Errore nella lettura del file.");
        }
    }

    private void scriviListaSuFile() {
        pulisciSchermo();
        String nomeLista = getInputConMessaggio("Inserisci il nome della lista :");
        ListaSpesa lista = gestioneSpese.getListaByNome(nomeLista);
        if (lista == null) {
            System.out.println("Lista non presente, riprovare");
            return;
        }
        String nomeFile = getInputConMessaggio("Inserisci nome file di destinazione :");
        try {
            ListWriterReader.writeListaSuFile(nomeFile, lista);
            System.out.println("Operazione eseguita con successo!");
        } catch (IOException e) {
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
        this.pulisciSchermo();
    }

    private void svuotaGestore() {
        pulisciSchermo();
        gestioneSpese.resetGestioneSpese();
        System.out.println("Gestore svuotato con successo!");
        continuaEPulisci();
    }

    private void pulisciSchermo() {
        System.out.println("\n".repeat(100));
    }

    private String getInputConMessaggio(String messaggio) {
        System.out.println(messaggio);
        return inputScanner.next();
    }

    private void stampaMenuOperazioniListe() {
        System.out.println("Quale operazione si vuole eseguire sulle liste?\n");
        System.out.println("1 - aggiungi nuova lista");
        System.out.println("2 - rimuovi lista");
        System.out.println("3 - aggiungi articolo");
        System.out.println("4 - rimuovi articolo");
        System.out.println("5 - modifica articolo");
        System.out.println("9 - esci");
    }

    private void stampaMenuPrincipale() {
        System.out.println("\n\nCosa vuoi fare ? Seleziona un'azione : \n");
        System.out.println("1 - stampa liste della spesa");
        System.out.println("2 - stampa categorie attuali");
        System.out.println("3 - operazioni su liste/articoli");
        System.out.println("4 - operazioni sulle categorie");
        System.out.println("5 - esporta/importa su/da file");
        System.out.println("6 - svuota gestore della spesa");
        System.out.println("9 - esci (fine programma)");
        System.out.println("\n");
    }

    private void stampaMenuOperazioniFile() {
        pulisciSchermo();
        System.out.println("Quale operazione vuoi eseguire?\n");
        System.out.println("1 - carica lista da file");
        System.out.println("2 - scrivi lista su file");
        System.out.println("9 - esci");
    }

    private void stampaContenutoListe() {
        System.out.println("Contenuto attuale liste :\n");
        Optional.of(gestioneSpese)
                .map(GestioneSpese::getListaSpese)
                .orElse(Collections.emptyList())
                .forEach(lista -> System.out.println(lista.toString()));
        System.out.println();
    }

    private void stampaContenutoCategorie() {
        System.out.println("Ecco le categorie attualmente presenti :\n");
        Stream.of(gestioneSpese)
                .map(GestioneSpese::getCategorie)
                .forEach(System.out::println);
        System.out.println();
    }

    private void exitSaluto() {
        System.out.println("\n\nProgramma terminato. Arrivederci!");
        gestioneSpese.resetGestioneSpese();
        System.exit(0);
    }
}