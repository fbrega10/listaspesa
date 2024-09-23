package model;

import exceptions.ListaSpesaException;
import utils.Costanti;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Lista spesa rappresenta una collezione di Articoli, ogni lista ha un nome proprio che la definisce.
 * Deve essere iterabile in modo da poter scorrere dal gestore le liste.
 * La lista deve essere mutabile, motivo per cui utilizzo ArrayList, utilizzare un List generico avrebbe
 * introdotto la possibilita' di assegnare Collezioni immutabili  che non supportano direttamente
 * le operazioni di add/remove
 */
public class ListaSpesa implements Serializable, Iterable<Articolo> {
    //class fields
    private String nome;
    private List<Articolo> listaArticoli;

    public ListaSpesa() {
    }

    /**
     * Istanzia una nuova lista a partire dal suo nome.
     *
     * @param nome Stringa che rappresenta il nome della lista
     */
    public ListaSpesa(String nome) throws ListaSpesaException {
        //non accetto come argomento della funzione un nome null
        if (nome == null) throw new ListaSpesaException(Costanti.ECCEZ_NOME_INVALIDO_LISTA);
        this.nome = nome;
        listaArticoli = new ArrayList<>();
    }

    /**
     * @param nome Nome della lista della spesa.
     * @param list lista degli articoli.
     * @throws ListaSpesaException nel caso in cui il nome assegnato alla lista sia null.
     */
    public ListaSpesa(String nome, List<Articolo> list) throws ListaSpesaException {
        this(nome);
        this.listaArticoli = list;
        this.listaArticoli.sort(Comparator.comparing(Articolo::getNomeArticolo));
    }

    /**
     * Add articolo -> aggiunge un articolo alla lista della spesa.
     *
     * @param articolo rappresenta l'articolo da aggiungere
     * @return ListaSpesa, in modo che piu' chiamate possano essere eseguite in sequenza
     */
    public ListaSpesa addArticolo(Articolo articolo) {
        //chainable (metodo)
        if (articolo == null) return this;
        this.listaArticoli.add(articolo);
        this.listaArticoli.sort(Comparator.comparing(Articolo::getNomeArticolo));
        return this;
    }

    /**
     * Remove articolo: toglie un articolo dalla spesa
     * Metodo chainable
     *
     * @param articolo
     * @return ListaSpesa in maniera che sia chainable
     */
    public ListaSpesa removeArticolo(Articolo articolo) {
        this.listaArticoli.remove(articolo);
        return this;
    }

    /**
     * @param nomeArticolo dato un nome articolo
     * @return un booleano che indica se l'azione di rimuovere l'articolo ha avuto
     * o meno buon fine (il primo della lista di quelli che matchano col nome indicato).
     */
    public boolean removeArticolo(String nomeArticolo) {
        if (nomeArticolo == null) return false;
        List<Articolo> list = Optional.of(this.listaArticoli)
                .orElse(Collections.emptyList())
                .stream()
                .filter(articolo -> articolo.getNomeArticolo().equalsIgnoreCase(nomeArticolo))
                .toList();
        if (list.isEmpty()) return false;
        else {
            this.listaArticoli.remove(list.get(0));
            return true;
        }
    }

    /**
     * Data una stringa rappresentante una categoria, si vuole ritornare tutti gli articoli che hanno
     * categoria che inizia con la stringa come parametro, altrimenti la lista restituita e' vuota
     *
     * @param categoria, una stringa da cui filtrare gli articoli della lista
     * @return una lista di Articoli che matchano la categoria rappresentata
     */
    public List<Articolo> getArticoliDiCategoria(String categoria) {
        if (categoria == null) return Collections.emptyList();
        return Optional.of(listaArticoli)
                .orElse(new ArrayList<>())
                .stream()
                .filter(articolo -> articolo.getCategoria() != null && articolo.getCategoria().equalsIgnoreCase(categoria))
                .collect(Collectors.toList());
    }

    /**
     * Data una categoria viene restituita la lista degli Articoli il cui nome
     * inizia con la stringa di input, altrimenti si ottiene una lista vuota.
     *
     * @param nomeArticolo : prefisso in stringa
     * @return List<Articolo>, può essere vuota.
     */
    public List<Articolo> getArticoliDiNomePrefix(String nomeArticolo) {
        if (nomeArticolo == null) return Collections.emptyList();
        return Optional.of(listaArticoli)
                .orElse(new ArrayList<>())
                .stream()
                .filter(articolo -> articolo.getNomeArticolo() != null && articolo.getNomeArticolo().startsWith(nomeArticolo))
                .collect(Collectors.toList());
    }

    /**
     * Get dell'articolo piu' costoso della lista (maggiore prezzo unitario).
     *
     * @return Articolo della lista, null se non ci sono elementi
     */
    public Articolo getArticoloPiuCostoso() {
        if (listaArticoli.isEmpty()) return null;
        return this.listaArticoli.
                stream()
                .max((Comparator.comparing(Articolo::getPrezzoUnitario)))
                .orElse(null);
    }

    /**
     * @return Articolo il cui prezzo complessivo (prezzo x quantità) è il maggiore della lista
     */
    public Articolo getArticoloCostoTotaleMaggiore() {
        if (listaArticoli.isEmpty()) return null;
        return this.listaArticoli.
                stream()
                .max(new Articolo.ArticoloPrezzoComparator())
                .orElse(null);
    }

    /**
     * @param nomeArticolo : nome dell'articolo da cercare
     * @return l'Articolo oppure null se non presente nella lista.
     */
    public Articolo getArticoloByNome(String nomeArticolo) {
        if (nomeArticolo == null) return null;
        return Optional.of(this.listaArticoli)
                .orElse(Collections.emptyList())
                .stream()
                .filter(articolo -> articolo.getNomeArticolo().equalsIgnoreCase(nomeArticolo))
                .findFirst()
                .orElse(null);
    }

    /**
     * Calcola costo totale spesa big decimal.
     *
     * @return BigDecimal che rappresenta il costo di ogni articolo (prezzo x quantita)
     */
    public BigDecimal calcolaCostoTotaleSpesa() {
        return Optional.ofNullable(this.listaArticoli)
                .orElse(new ArrayList<>())
                .stream()
                .map(Articolo::calcolaPrezzo)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }

    /**
     * Elimina la categoria indicata come parametro. Ad ogni articolo impattato viene sostituita con quella
     * di default, ovvero : "Non Categorizzati"
     *
     * @param categoria the categoria
     */
    public void eliminaCategoria(String categoria) {
        if (listaArticoli.isEmpty()) return;
        this.listaArticoli.stream()
                .filter(articolo -> articolo.getCategoria().equals(categoria))
                .forEach(articolo -> articolo.setCategoria(Costanti.CATEGORIA_DEFAULT));
    }

    /**
     * @return booleano che rappresenta lista vuota o meno
     */
    public boolean isEmpty() {
        return this.listaArticoli.isEmpty();
    }

    /**
     * Size int
     *
     * @return la size della lista di articoli.
     */
    public int size() {
        return this.listaArticoli.size();
    }

    /**
     * Svuota lista e rimuove tutti gli articoli presenti.
     */
    public void svuotaLista() {
        this.listaArticoli.clear();
    }

    //GETTERS AND SETTERS

    /**
     * Gets nome.
     *
     * @return nome
     */
    public String getNome() {
        return nome;
    }

    /**
     * Sets nome.
     *
     * @param nome stringa
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public Iterator<Articolo> iterator() {
        return this.listaArticoli.iterator();
    }

    /**
     * @param obj : rappresenta l'oggetto di confronto
     * @return : booleano che indica se gli oggetti sono identici
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ListaSpesa altra)) return false;
        return this.nome.equals(altra.getNome()) && this.listaArticoli.equals(altra.listaArticoli);
    }

    /**
     * @return Stringa che restituisce l'oggetto formattato come stringa
     * per la scrittura in Csv
     */
    public String toCsv() {
        if (this.listaArticoli.isEmpty()) return null;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.nome);
        for (Articolo a : this.listaArticoli) {
            stringBuilder.append("\n");
            stringBuilder.append(a.toCsvFormat());
        }
        return stringBuilder.toString();
    }

    /**
     * @return Stringa dell'oggetto formattato.
     */
    @Override
    public String toString() {
        return "nome  : '" + nome + '\'' +
                ",  " + listaArticoli;
    }
}
