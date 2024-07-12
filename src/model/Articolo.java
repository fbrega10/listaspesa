package model;

import utils.Costanti;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Comparator;

public class Articolo implements Serializable {
    private String nomeArticolo;
    private BigDecimal prezzoUnitario;
    private int quantita;
    private String categoria;

    public Articolo(String nomeArticolo, BigDecimal prezzoUnitario) {
        //da requisito occorre inizializzare la categoria a default in mancanza di un dato
        //cosi come la quantita che va impostata a 1
        if (prezzoUnitario.compareTo(BigDecimal.ZERO) < 0)
            throw new IllegalArgumentException(Costanti.ECCEZ_PREZZO_NEGATIVO);
        this.nomeArticolo = nomeArticolo;
        this.prezzoUnitario = prezzoUnitario;
        this.categoria = Costanti.CATEGORIA_DEFAULT;
        this.quantita = Costanti.QUANTITA_DEFAULT;
    }

    public Articolo(String nomeArticolo, BigDecimal prezzoUnitario, int quantita, String categoria) {
        this(nomeArticolo, prezzoUnitario);
        if (quantita <= 0) {
            throw new IllegalArgumentException(Costanti.ECCEZ_QUANTITA_NON_ACCETTATA);
        }
        this.quantita = quantita;
        this.categoria = categoria;
    }

    @Override
    public boolean equals(Object obj) {
        if (! (obj instanceof Articolo s)) return false;
        return this.nomeArticolo.equals(s.getNomeArticolo())
                && this.prezzoUnitario.equals(s.getPrezzoUnitario())
                && this.quantita == s.getQuantita()
                && this.categoria.equals(s.getCategoria());
    }

    public BigDecimal calcolaPrezzo() {
        //calcola il prezzo del prodotto corrente
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
        StringBuilder sb = new StringBuilder();
        sb.append("nome articolo : ")
                .append(this.nomeArticolo)
                .append(" prezzo unitario: ")
                .append(this.prezzoUnitario.toString())
                .append(" quantita : ")
                .append(this.quantita)
                .append(" categoria : ")
                .append(this.categoria);
        return sb.toString();
    }

    public static class ArticoloPrezzoComparator implements Comparator<Articolo> {
        @Override
        public int compare(Articolo a, Articolo b) {
            return (a.calcolaPrezzo().compareTo(b.calcolaPrezzo()));
        }
    }

    public static class ArticoloPrezzoUnitarioComparator implements Comparator<Articolo>{
        @Override
        public int compare(Articolo a, Articolo b){
            return (a.getPrezzoUnitario().compareTo(b.getPrezzoUnitario()));
        }
    }
}