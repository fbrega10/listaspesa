package model;

import utils.Costanti;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ListaSpesa implements Serializable {
    private String nome;
    private List<Articolo> listaArticoli;

    public ListaSpesa(String nome) {
        //non accetto come argomento della funzione un nome null
        if (nome == null) throw new IllegalArgumentException(Costanti.ECCEZ_NOME_INVALIDO_LISTA);
        this.nome = nome;
        listaArticoli = new ArrayList<Articolo>();
    }

    public ListaSpesa addArticolo(Articolo articolo) {
        //chainable (metodo)
        if (articolo == null) return this;
        this.listaArticoli.add(articolo);
        return this;
    }

    public ListaSpesa removeArticolo(Articolo articolo) {
        //chainable (metodo)
        if (this.listaArticoli.contains(articolo)) {
            System.out.println("Rimosso articolo " + articolo.toString());
            this.listaArticoli.remove(articolo);
        }
        return this;
    }

    public BigDecimal getTotaleSpesa() {
        //safe conteggio spesa totale, se null o non specificato il valore, allora abbiamo 0 come risultato
        return Optional.of(this.listaArticoli)
                .orElse(Collections.emptyList())
                .stream()
                .filter(articolo -> articolo.getPrezzoUnitario() != null && articolo.getQuantita() > 0)
                .map(Articolo::calcolaPrezzo)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }

    public List<Articolo> getArticoliDiCategoria(String categoria) {
        //Data una stringa rappresentante una categoria, si vuole ritornare tutti gli articoli che hanno
        //categoria che inizia con la stringa come parametro, altrimenti la lista restituita e null
        if (categoria == null) return Collections.emptyList();
        return Optional.of(this.listaArticoli)
                .orElse(Collections.emptyList())
                .stream()
                .filter(articolo -> articolo.getCategoria() != null && articolo.getCategoria().startsWith(categoria))
                .collect(Collectors.toList());
    }

    public Articolo getArticoloPiuCostoso() {
        if (this.listaArticoli.isEmpty()) return null;
        return this.listaArticoli.
                stream()
                .max(new Articolo.ArticoloPrezzoComparator())
                .orElse(null);
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<Articolo> getListaArticoli() {
        return listaArticoli;
    }

    public void setListaArticoli(List<Articolo> listaArticoli) {
        this.listaArticoli = listaArticoli;
    }

    @Override
    public boolean equals(Object obj) {
        if (! (obj instanceof ListaSpesa temp)) return false;
        return this.nome.equals(temp.getNome()) && this.listaArticoli.equals(temp.getListaArticoli());
    }
}
