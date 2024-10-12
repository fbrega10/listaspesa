package model;

import utils.Costanti;

import javax.swing.DefaultListModel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

/**
 * La classe GestioneSpese permette di gestire contemporaneamente piu' spese e tiene traccia di tutte
 * le categorie che vengono introdotte nei vari articoli che compongono.
 * La gestione delle categorie avviene attraverso il campo Set<String> categorie che e' un HashSet
 * e garantisce l'unicita' delle categorie (string). Il metodo equals non deve essere implementato (String).
 */
public class GestioneSpese {

    private List<ListaSpesa> listaSpese;
    private Set<String> categorie;

    /**
     * Costruttore di default GestioneSpese.
     * Istanzia un HashSet che rappresenta le categorie e ne istanzia sempre la categoria di default
     */
    public GestioneSpese() {
        categorie = new HashSet<>();
        this.categorie.add(Costanti.CATEGORIA_DEFAULT);
        this.listaSpese = new ArrayList<>();
    }

    /**
     * Aggiunge una categoria
     *
     * @param categoria la categoria rappresentata come stringa
     *                  Non accetto un puntamento a null come valore di dominio.
     * @return the boolean
     */
    public boolean addCategoria(String categoria) {
        if (categoria == null) return false;
        return this.categorie.add(categoria);

    }

    /**
     * L'azione di rimuove la categoria ha come effetto collaterale l'aggiornamento di tutti gli oggetti
     * presenti nelle liste (tipo ListaSpesa) e il settaggio al valore di default previsto per categoria.
     * Il metodo è NullPointer free.
     *
     * @param categoria : rappresenta la stringa indicata come categoria dell'articolo in questione.
     * @return the boolean
     */
    public boolean removeCategoria(String categoria) {
        if (categoria == null || categoria.equals(Costanti.CATEGORIA_DEFAULT)) return false;
        if (this.categorie.contains(categoria)) {
            Optional.of(this.listaSpese)
                    .orElse(Collections.emptyList())
                    .forEach(lista -> lista.eliminaCategoria(categoria));
            this.categorie.remove(categoria);
            return true;
        } else return false;
    }

    /**
     * Aggiunge una lista della spesa
     *
     * @param listaSpesa oggetto ListaSpesa
     * @return il booleano che indica se ha avuto o meno successo l'operazione
     * se la lista e' gia' presente questa non viene aggiunta in quanto rappresenta un duplicato.
     * Il metodo è NullPointer free.
     */
    public boolean addListaSpesa(ListaSpesa listaSpesa) {
        if (listaSpesa == null || this.listaSpese.contains(listaSpesa)) return false;
        this.listaSpese.add(listaSpesa);
        for (Articolo s : listaSpesa) {
            this.addCategoria(s.getCategoria());
        }
        return true;
    }

    /**
     * Rimuovi dalla collezione di liste quella indicata.
     *
     * @param listaSpesa l'oggetto ListaSpesa
     * @return il booleano che rappresenta il successo/fallimento dell'operazione
     */
    public boolean rimuoviListaSpesa(ListaSpesa listaSpesa) {
        if (!this.listaSpese.contains(listaSpesa)) return false;
        this.listaSpese.remove(listaSpesa);
        return true;
    }

    /**
     * @param nomeLista : Stringa che rappresenta il nome della lista della spesa
     * @return booleano che significa che l'operazione é stata o meno eseguita.
     * In caso di successo viene eliminata la prima lista della spesa che matcha
     * il nome indicato da parametro e si restituisce true, false negli altri casi.
     */
    public boolean rimuoviListaSpesa(String nomeLista) {
        if (nomeLista != null) {
            return this.listaSpese
                    .remove(this.listaSpese
                            .stream()
                            .filter(lista -> lista.getNome().equalsIgnoreCase(nomeLista))
                            .findFirst()
                            .orElse(null));
        }
        return false;
    }

    /**
     * @param nomeLista in stringa
     * @return la ListaSpesa con il nome corrispondente, o un reference a null.
     */
    public ListaSpesa getListaByNome(String nomeLista) {
        if (nomeLista != null) {
            return Optional.ofNullable(this.listaSpese)
                    .orElse(Collections.emptyList())
                    .stream()
                    .filter(list -> list.getNome().equalsIgnoreCase(nomeLista))
                    .findFirst()
                    .orElse(null);
        }
        return null;
    }

    /**
     * @return verifica se il contenuto del field
     * listaSpese è o meno vuoto
     */
    public boolean isEmptyOfListaSpese() {
        return this.listaSpese.isEmpty();
    }

    /**
     * @return ritorna il numero di Liste della spesa
     */
    public int getListaSpeseSize() {
        return this.listaSpese.size();
    }

    /**
     * Stampa i contenuti delle liste della spesa che compongono il gestore
     */
    public void stampaContenutoSpese() {
        Optional.of(this.listaSpese)
                .orElse(Collections.emptyList())
                .forEach(lista -> System.out.println(lista.toString()));
    }

    /**
     * Stampa i nomi delle liste della spesa del gestore
     */
    public void stampaNomiListeSpesa() {
        System.out.println("nomi liste attuali : \n" + Optional.of(this.listaSpese)
                .orElse(Collections.emptyList())
                .stream()
                .map(ListaSpesa::getNome)
                .toList());
    }

    /**
     * @param listModel permette un aggiornamento del listModel sulla base del contenuto
     *                  attuale del gestore. Utility per GUI.
     */
    public void updateListModel(DefaultListModel<ListaSpesa> listModel) {
        listModel.clear();
        Optional.of(this.listaSpese)
                .orElse(Collections.emptyList())
                .forEach(listModel::addElement);
    }


    /**
     * @param categorieModel permette un aggiornamento del listModel sulla base del contenuto
     *                       attuale del gestore. Utility per GUI.
     */
    public void updateCategorieListModel(DefaultListModel<String> categorieModel) {
        categorieModel.clear();
        Optional.of(this.categorie)
                .orElse(Collections.emptySet())
                .forEach(categorieModel::addElement);
    }

    /**
     * Stampa su standard output il contenuto delle categorie attuali
     */
    public void stampaContenutoCategorie() {
        Stream.of(this.categorie)
                .forEach(System.out::println);
    }

    /**
     * @param categoria String
     * @return booleano che verifica la presenza della categoria nel set
     */
    public boolean containsCategoria(String categoria) {
        return this.categorie.contains(categoria);
    }

    /**
     * @return il numero di categorie attuali
     */
    public int getCategorieSize() {
        return this.categorie.size();
    }

    /**
     * @return Gestione della spesa in formato String
     */
    @Override
    public String toString() {
        return "categorie = " + categorie + "\n" +
                "liste della spesa = " + listaSpese;
    }

    /**
     * Reset gestione spese riporta l'oggetto alle impostazioni di fabbrica: ovvero elimina tutte le categorie
     * (ad eccezione di quella di default) e svuota il gestore di tutte le spese che erano state precedentemente
     * aggiunte. Stesso comportamento iniziale dell'oggetto successivamente alla sua costruzione.
     */
    public void resetGestioneSpese() {
        this.listaSpese.clear();
        this.categorie.clear();
        this.categorie.add(Costanti.CATEGORIA_DEFAULT);
    }

}
