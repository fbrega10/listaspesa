package model;

import exceptions.ArticoloException;
import utils.Costanti;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Comparator;

public class Articolo implements Serializable {

    private String nomeArticolo;
    private BigDecimal prezzoUnitario;
    private int quantita;
    private String categoria;

    /**
     * Istanzia un nuovo Articolo.
     * Lancia un'eccezione di tipo ArticoloException per prezzo negativo, non possibile per il dominio del dato,
     * oppure per nome articolo settato a null.
     * Di default la categoria e' preimpostata, cosi' come la quantita' a 1 se non definita (da requisito).
     *
     * @param nomeArticolo   nome dell'articolo (String)
     * @param prezzoUnitario il prezzo unitario (BigDecimal)
     * @throws ArticoloException Eccezione custom
     */
    public Articolo(String nomeArticolo, BigDecimal prezzoUnitario) throws ArticoloException {
        //da requisito occorre inizializzare la categoria a default in mancanza di un dato
        //cosi come la quantita che va impostata a 1
        if (prezzoUnitario.compareTo(BigDecimal.ZERO) <= 0)
            throw new ArticoloException(Costanti.ECCEZ_PREZZO_NEGATIVO);
        if (nomeArticolo == null) throw new ArticoloException(Costanti.NOME_ARTICOLO_INVALIDO);
        this.nomeArticolo = nomeArticolo;
        this.prezzoUnitario = prezzoUnitario;
        this.categoria = Costanti.CATEGORIA_DEFAULT;
        this.quantita = Costanti.QUANTITA_DEFAULT;
    }

    /**
     * Istanzia un nuovo Articolo.
     * Lancia un'eccezione di tipo ArticoloException per prezzo negativo, non possibile per il dominio del dato,
     * oppure per nome articolo settato a null.
     * Di default la categoria e' preimpostata, cosi' come la quantita' a 1 (se non definita).
     * La quantita' é Integer per lasciare la possibilita' all'utente di non inserire alcun valore e gestirlo
     * da applicativo.
     *
     * @param nomeArticolo   nome dell'articolo (String)
     * @param prezzoUnitario il prezzo unitario (BigDecimal)
     * @param quantita       la quantita
     * @param categoria      la categoria (String)
     * @throws ArticoloException Eccezione custom nel caso in cui la quantita' sia negativa
     */
    public Articolo(String nomeArticolo, BigDecimal prezzoUnitario, Integer quantita, String categoria) throws ArticoloException {
        this(nomeArticolo, prezzoUnitario);
        if (categoria != null) this.categoria = categoria;
        if (quantita != null && quantita > 0) {
            this.quantita = quantita;
        } else if (quantita != null && quantita < 0) {
            throw new ArticoloException("Non si accettano quantita' negative.");
        }
    }

    /**
     * @param obj : Due oggetti sono identici se sono istanze di Articolo
     *            e presentano tutti i medesimi campi
     * @return boolean
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Articolo s)) return false;
        return this.nomeArticolo.equals(s.getNomeArticolo())
                && this.prezzoUnitario.equals(s.getPrezzoUnitario())
                && this.quantita == s.getQuantita()
                && this.categoria.equals(s.getCategoria());
    }

    /**
     * Calcola prezzo del prodotto come moltiplicazione tra la quantita' e il suo prezzo unitario
     * @return BigDecimal corrispondente alla moltiplicazione tra prezzo unitario e quantita'
     */
    public BigDecimal calcolaPrezzo() {
        return this.getPrezzoUnitario().multiply(BigDecimal.valueOf(this.getQuantita()));
    }

    /**
     * @return nome dell'articolo
     */
    public String getNomeArticolo() {
        return nomeArticolo;
    }

    /**
     * @return BigDecimal che rappresenta il prezzo unitario
     */
    public BigDecimal getPrezzoUnitario() {
        return prezzoUnitario;
    }

    /**
     * @return int: rappresenta la quantita'
     */
    public int getQuantita() {
        return quantita;
    }

    /**
     * @return la categoria di appartenenza
     */
    public String getCategoria() {
        return categoria;
    }

    /**
     * @param nomeArticolo setta il nome dell'articolo all'oggetto.
     */
    public void setNomeArticolo(String nomeArticolo) {
        this.nomeArticolo = nomeArticolo;
    }

    /**
     * @param prezzoUnitario setta il prezzo unitario dell'oggetto.
     */
    public void setPrezzoUnitario(BigDecimal prezzoUnitario) {
        this.prezzoUnitario = prezzoUnitario;
    }

    /**
     * @param quantita setta la quantita' dell'oggetto.
     */
    public void setQuantita(int quantita) {
        this.quantita = quantita;
    }

    /**
     * @param categoria setta la categoria dell'oggetto.
     */
    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    @Override
    public String toString() {
        return "{ nomeArticolo: '" + nomeArticolo + '\'' + ", " +
                quantita + " x " +
                prezzoUnitario + " €" +
                ", categoria: '" + categoria + '\'' + " }";
    }

    /**
     * Trasforma l'Articolo in formato stringa per scrittura su csv
     * Delimita i vari campi da un carattere ';'
     *
     * @return la stringa formattata
     */
    public String toCsvFormat() {
        return this.nomeArticolo
                + ";"
                + this.prezzoUnitario
                + ";"
                + this.quantita
                + ";"
                + this.categoria;
    }

    public static class ArticoloPrezzoComparator implements Comparator<Articolo> {
        @Override
        public int compare(Articolo a, Articolo b) {
            return (a.calcolaPrezzo().compareTo(b.calcolaPrezzo()));
        }
    }

    public static class ArticoloPrezzoUnitarioComparator implements Comparator<Articolo> {
        @Override
        public int compare(Articolo a, Articolo b) {
            return (a.getPrezzoUnitario().compareTo(b.getPrezzoUnitario()));
        }
    }
}