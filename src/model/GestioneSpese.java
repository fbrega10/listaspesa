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
 * La gestione delle categorie avviene attraverso il campo statico Set<String> categorie che e' un HashSet
 * e garantisce l'unicita' delle categorie (string). Il metodo equals non deve essere implementato (String).
 * Un inizializzatore statico di classe istanzia il campo categorie con una nuova istanza di HashSet a cui
 * aggiunge direttamente la categoria di default (da requisito).
 */
public class GestioneSpese {
    private List<ListaSpesa> listaSpese;
    private static Set<String> categorie;

    static {
        categorie = new HashSet<>();
        GestioneSpese.categorie.add(Costanti.CATEGORIA_DEFAULT);
    }

    /**
     * Costruttore GestioneSpese.
     */
    public GestioneSpese() {
        this.listaSpese = new ArrayList<>();
    }

    /**
     * Aggiunge una categoria
     *
     * @param categoria la categoria rappresentata come stringa
     * @return the boolean
     */
    public boolean addCategoria(String categoria) {
        if (categoria == null || GestioneSpese.categorie.contains(categoria)) {
            return false;
        }
        GestioneSpese.categorie.add(categoria);
        return true;
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
        if (GestioneSpese.categorie.contains(categoria)) {
            Optional.of(this.listaSpese)
                    .orElse(Collections.emptyList())
                    .forEach(lista -> lista.eliminaCategoria(categoria));
            GestioneSpese.categorie.remove(categoria);
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
     * Reset gestione spese riporta l'oggetto alle impostazioni di fabbrica: ovvero elimina tutte le categorie
     * (ad eccezione di quella di default) e svuota il gestore di tutte le spese che erano state precedentemente
     * aggiunte.
     */
    public void resetGestioneSpese() {
        this.listaSpese.clear();
        GestioneSpese.categorie.clear();
        GestioneSpese.categorie.add(Costanti.CATEGORIA_DEFAULT);
    }
}
