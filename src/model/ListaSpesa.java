package model;

import exceptions.ListaSpesaException;
import utils.Costanti;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
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
        return this;
    }

    /**
     * Remove articolo: toglie un articolo dalla spesa
     *
     * @param articolo
     * @return ListaSpesa in maniera che sia chainable
     */
    public ListaSpesa removeArticolo(Articolo articolo) {
        //chainable (metodo)
        this.listaArticoli.remove(articolo);
        return this;
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
                .filter(articolo -> articolo.getCategoria() != null && articolo.getCategoria().equals(categoria))
                .collect(Collectors.toList());
    }

    /**
     * Data una categoria viene restituita la lista degli Articoli la cui categoria
     * inizia con la stringa di input, altrimenti si ottiene una lista vuota.
     * @param categoria : prefisso in stringa
     * @return List<Articolo>, può essere vuota.
     */
    public List<Articolo> getArticoliDiCategoriaPrefix(String categoria){
        if (categoria == null) return Collections.emptyList();
        return Optional.of(listaArticoli)
                .orElse(new ArrayList<>())
                .stream()
                .filter(articolo -> articolo.getCategoria() != null && articolo.getCategoria().startsWith(categoria))
                .collect(Collectors.toList());
    }

    /**
     * Get dell'articolo piu' costoso della lista, calcolando il costo come quantita' x prezzo unitario.
     *
     * @return l'Articolo in questione
     */
    public Articolo getArticoloPiuCostoso() {
        if (listaArticoli.isEmpty()) return null;
        return this.listaArticoli.
                stream()
                .max(new Articolo.ArticoloPrezzoComparator())
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
    public boolean isEmpty(){
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

    /**
     * Gets lista articoli.
     *
     * @return lista articoli
     */
    public List<Articolo> getListaArticoli() {
        return listaArticoli;
    }

    /**
     * Set della lista articoli all'oggetto.
     *
     * @param listaArticoli la lista di articoli
     */
    public void setListaArticoli(List<Articolo> listaArticoli) {
        this.listaArticoli = listaArticoli;
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
        return this.nome.equals(altra.getNome()) && this.listaArticoli.equals(altra.getListaArticoli());
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
