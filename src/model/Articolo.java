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
     * Di default la categoria e' preimpostata, cosi' come la quantita' a 1(se non definita).
     *
     * @param nomeArticolo   nome dell'articolo (String)
     * @param prezzoUnitario il prezzo unitario (BigDecimal)
     * @throws ArticoloException Eccezione custom
     */
    public Articolo(String nomeArticolo, BigDecimal prezzoUnitario) throws ArticoloException {
        //da requisito occorre inizializzare la categoria a default in mancanza di un dato
        //cosi come la quantita che va impostata a 1
        if (prezzoUnitario.compareTo(BigDecimal.ZERO) < 0)
            throw new ArticoloException(Costanti.ECCEZ_PREZZO_NEGATIVO); if (nomeArticolo == null) throw new ArticoloException(Costanti.NOME_ARTICOLO_INVALIDO); this.nomeArticolo = nomeArticolo;
        this.prezzoUnitario = prezzoUnitario;
        this.categoria = Costanti.CATEGORIA_DEFAULT;
        this.quantita = Costanti.QUANTITA_DEFAULT;
    }

    /**
     * Istanzia un nuovo Articolo.
     * Lancia un'eccezione di tipo ArticoloException per prezzo negativo, non possibile per il dominio del dato,
     * oppure per nome articolo settato a null.
     * Di default la categoria e' preimpostata, cosi' come la quantita' a 1 (se non definita).
     *
     * @param nomeArticolo   nome dell'articolo (String)
     * @param prezzoUnitario il prezzo unitario (BigDecimal)
     * @param quantita       la quantita
     * @param categoria      la categoria (String)
     * @throws ArticoloException Eccezione custom
     */
    public Articolo(String nomeArticolo, BigDecimal prezzoUnitario, int quantita, String categoria) throws ArticoloException {
        this(nomeArticolo, prezzoUnitario);
        if (quantita <= 0) {
            throw new ArticoloException(Costanti.ECCEZ_QUANTITA_NON_CONSENTITA);
        }
        this.quantita = quantita;
        this.categoria = categoria;
    }

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
     *
     * @return BigDecimal corrispondente alla moltiplicazione tra prezzo unitario e quantita'
     */
    public BigDecimal calcolaPrezzo() {
        return this.getPrezzoUnitario().multiply(BigDecimal.valueOf(this.getQuantita()));
    }

    //metodi getter/setter
    public String getNomeArticolo() {
        return nomeArticolo;
    }

    public BigDecimal getPrezzoUnitario() {
        return prezzoUnitario;
    }

    public int getQuantita() {
        return quantita;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setNomeArticolo(String nomeArticolo) {
        this.nomeArticolo = nomeArticolo;
    }

    public void setPrezzoUnitario(BigDecimal prezzoUnitario) {
        this.prezzoUnitario = prezzoUnitario;
    }

    public void setQuantita(int quantita) {
        this.quantita = quantita;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    @Override
    public String toString() {
        return "{ nomeArticolo: '" + nomeArticolo + '\'' + ", " +
                quantita + " x " +
                prezzoUnitario + "€" +
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