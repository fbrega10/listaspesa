package model;

import utils.Costanti;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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
     * @return the boolean
     */
    public boolean addCategoria(String categoria) {
        return this.categorie.add(categoria);

    }

    /**
     * L'azione di rimuove la categoria ha come effetto collaterale l'aggiornamento di tutti gli oggetti
     * presenti nelle liste (tipo ListaSpesa) e il settaggio al valore di default previsto per categoria.
     *
     * @param categoria : rappresenta la stringa indicata come categoria dell'articolo in questione.
     * @return the boolean
     */
    public boolean removeCategoria(String categoria) {
        if (categoria.equals(Costanti.CATEGORIA_DEFAULT)) return false;
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
     */
    public boolean addListaSpesa(ListaSpesa listaSpesa) {
        if (this.listaSpese.contains(listaSpesa)) return false;
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
     * Gets lista spese.
     *
     * @return le liste della spesa
     */
    public List<ListaSpesa> getListaSpese() {
        return listaSpese;
    }

    /**
     * Gets categorie.
     *
     * @return il set di categorie
     */
    public Set<String> getCategorie() {
        return categorie;
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
