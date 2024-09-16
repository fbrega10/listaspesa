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
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Stream;

/**
 * Interfaccia riga di comando per la gestione della lista della spesa
 */
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
            int scelta = filtraSceltaMenu();
            switch (scelta) {
                case 1 -> stampaListe();
                case 2 -> stampaCategorie();
                case 3 -> operazioniListe();
                case 4 -> operazioniCategorie();
                case 5 -> operazioniFile();
                case 6 -> svuotaGestore();
                case 9 -> uscita = true;
                default -> System.out.println("scelta errata, riprovare");
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
        int scelta = filtraSceltaMenu();
        switch (scelta) {
            case 1 -> aggiungiLista();
            case 2 -> rimuoviLista();
            case 3 -> aggiungiArticolo();
            case 4 -> rimuoviArticolo();
            case 5 -> modificaArticolo();
            case 6 -> cercaArticoloPerPrefisso();
            case 7 -> cercaArticoloPerCategoria();
            case 8 -> cercaArticoloPiuCostoso();
            default -> pulisciSchermo();
        }
    }

    /**
     * Cerca e stampa a video una lista della spesa in cui gli articoli hanno come
     * prefisso la stringa di input
     */
    private void cercaArticoloPerPrefisso() {
        String nomeLista = getInputConMessaggio("Inserisci il nome della lista da cui cercare");
        ListaSpesa listaSpesa = gestioneSpese.getListaByNome(nomeLista);
        if (listaSpesa == null) {
            listaNonPresente();
            return;
        }
        String prefisso = getInputConMessaggio("Inserisci prefisso di ricerca articolo :");
        List<Articolo> list = listaSpesa.getArticoliDiNomePrefix(prefisso);
        if (!list.isEmpty()) {
            System.out.println("\nlista degli articoli trovati : ");
            Stream.of(list).forEach(System.out::println);
        } else {
            System.out.println("Nessun articolo trovato con prefisso : " + prefisso + " riprovare prego.");
        }

    }

    /**
     * Stampa a video gli articoli di una lista la cui categoria corrisponde a quella di input.
     */
    private void cercaArticoloPerCategoria() {
        String nomeLista = getInputConMessaggio("Inserisci il nome della lista da cui cercare");
        ListaSpesa listaSpesa = gestioneSpese.getListaByNome(nomeLista);
        if (listaSpesa == null) {
            listaNonPresente();
            return;
        }
        String categoria = getInputConMessaggio("Inserisci categoria : ");
        List<Articolo> list = listaSpesa.getArticoliDiCategoria(categoria);
        if (list.isEmpty()) {
            System.out.println("Nessun articolo trovato.");
            continuaEPulisci();
            return;
        }
        System.out.println("Articoli trovati : \n" + list.toString());
        continuaEPulisci();
    }

    /**
     * Stampa a video l'articolo con il prezzo unitario maggiore, se la lista non è vuota.
     */
    private void cercaArticoloPiuCostoso(){
        String nomeLista = getInputConMessaggio("Inserisci il nome della lista da cui cercare");
        ListaSpesa listaSpesa = gestioneSpese.getListaByNome(nomeLista);
        if (listaSpesa == null) {
            listaNonPresente();
            return;
        }
        if (listaSpesa.isEmpty()) {
            System.out.println("lista vuota... aggiungere almeno un elemento.");
            continuaEPulisci();
            return;
        }
        System.out.println("L'articolo più costoso è : " + listaSpesa.getArticoloPiuCostoso());
        continuaEPulisci();
    }

    /**
     * Aggiunge un articolo a una lista scelta dall'utente
     */
    private void aggiungiArticolo() {
        String nomeLista = getInputConMessaggio("Inserisci il nome della lista a cui aggiungere l'articolo :");
        ListaSpesa listaSpesa = gestioneSpese.getListaByNome(nomeLista);
        if (listaSpesa == null) {
            listaNonPresente();
            return;
        }
        try {
            String nome = getInputConMessaggio("Inserisci il nome dell'articolo da inserire nella lista : ");
            BigDecimal prezzo = new BigDecimal(getInputConMessaggio("Inserisci il prezzo :"));
            Integer quantita = Integer.parseInt(getInputConMessaggio("Inserisci la quantità : (0 se non si vuole inserire nulla)"));
            String categoria = getInputConMessaggio("Inserisci la categoria : (inserire 'n' per non valorizzarla )");
            Articolo articolo = new Articolo(nome,
                    prezzo,
                    quantita == 0 ? null : quantita,
                    categoria.equals("n") ? null : categoria);
            listaSpesa.addArticolo(articolo);
        } catch (ArticoloException | NumberFormatException e) {
            System.out.println("Dati inseriti non validi, prezzo e nome obbligatori! Riprovare");
        }
        continuaEPulisci();
    }


    /**
     * Permette la modifica di prezzo, quantità e categoria di un articolo.
     */
    private void modificaArticolo() {
        stampaNomiListeAttualmenteDisponibili();
        String nomeLista = getInputConMessaggio("Inserisci il nome della lista a cui rimuovere l'articolo :");
        ListaSpesa listaSpesa = gestioneSpese.getListaByNome(nomeLista);
        if (listaSpesa == null) {
            listaNonPresente();
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
        } else System.out.println("Articolo non presente, riprovare prego.");
        continuaEPulisci();
    }

    /**
     * Rimuove un articolo dalla lista della spesa selezionata.
     */
    private void rimuoviArticolo() {
        stampaNomiListeAttualmenteDisponibili();
        String nomeLista = getInputConMessaggio("Inserisci il nome della lista a cui rimuovere l'articolo :");
        ListaSpesa listaSpesa = gestioneSpese.getListaByNome(nomeLista);
        if (listaSpesa == null) {
            listaNonPresente();
            return;
        }
        String nome = getInputConMessaggio("Inserisci il nome dell'articolo da rimuovere : ");
        Articolo articolo = listaSpesa.getArticoloByNome(nome);
        if (listaSpesa.getArticoloByNome(nome) != null) {
            listaSpesa.removeArticolo(articolo);
            System.out.println("Articolo rimosso con successo!");
        } else {
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
        int scelta = filtraSceltaMenu();
        switch (scelta) {
            case 1 -> aggiungiCategoria();
            case 2 -> rimuoviCategoria();
            default -> pulisciSchermo();
        }
    }

    /**
     * Aggiunge una categoria al gestore.
     */
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


    /**
     * Rimuove una categoria dal gestore (se presente, ovviamente).
     */
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

    /**
     * Rimuove una lista della spesa dal gestore.
     */
    private void rimuoviLista() {
        stampaNomiListeAttualmenteDisponibili();
        String nomeLista = getInputConMessaggio("Inserisci il nome della lista da rimuovere :");
        if (gestioneSpese.rimuoviListaSpesa(nomeLista)) {
            System.out.println("Lista della spesa eliminata correttamente!");
        } else {
            System.out.println("Lista della spesa non trovata, riprovare con un nome valido.");
        }
        continuaEPulisci();
    }

    /**
     * Aggiunge una nuova lista della spesa al gestore.
     */
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
        int scelta = filtraSceltaMenu();
        switch (scelta) {
            case 1 -> caricaListaDaFile();
            case 2 -> scriviListaSuFile();
            default -> pulisciSchermo();
        }
        continuaEPulisci();
    }

    /**
     * Carica da file una lista e la aggiunge al gestore.
     * Path assoluto indicato dall'utente.
     */
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

    /**
     * Scrive su un file (path /src/resources/) la lista selezionata.
     */
    private void scriviListaSuFile() {
        pulisciSchermo();
        stampaNomiListeAttualmenteDisponibili();
        String nomeLista = getInputConMessaggio("Inserisci il nome della lista :");
        ListaSpesa lista = gestioneSpese.getListaByNome(nomeLista);
        if (lista == null) {
            listaNonPresente();
            return;
        }
        String nomeFile = getInputConMessaggio("Inserisci nome file di destinazione :");
        try {
            ListWriterReader.writeListaSuFile(nomeFile, lista);
            System.out.println("Operazione eseguita con successo!");
            continuaEPulisci();
        } catch (IOException e) {
            System.out.println("Errore nella scrittura file, riprovare.");
            continuaEPulisci();
        }
    }

    /**
     * Pulisce lo schermo.
     */
    private void continuaEPulisci() {
        System.out.println("\n\npremi un tasto per continuare...");
        try {
            System.in.read();
        } catch (Exception e) {
        }
        this.pulisciSchermo();
    }

    /**
     * Resetta il gestore svuotandolo delle liste e categorie.
     */
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
        System.out.println("6 - cerca articolo per prefisso");
        System.out.println("7 - cerca articoli per categoria");
        System.out.println("8 - cerca articolo più costoso");
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

    private void stampaNomiListeAttualmenteDisponibili() {
        System.out.println("nomi liste attuali : \n" + gestioneSpese.getListaSpese()
                .stream()
                .map(ListaSpesa::getNome)
                .toList());
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

    private void listaNonPresente() {
        System.out.println("Nome lista non presente nel gestore.");
        continuaEPulisci();
    }

    private int filtraSceltaMenu() {
        try {
            return Integer.parseInt(this.inputScanner.next());
        } catch (NumberFormatException e) {
            return 99;
        }
    }

    /**
     * Termina l'esecuzione del programma.
     */
    private void exitSaluto() {
        System.out.println("\n\nProgramma terminato. Arrivederci!");
        gestioneSpese.resetGestioneSpese();
        System.exit(0);
    }
}
