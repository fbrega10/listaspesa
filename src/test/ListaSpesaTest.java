package test;

import exceptions.ArticoloException;
import exceptions.ListaSpesaException;
import model.Articolo;
import model.ListaSpesa;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.Costanti;

import java.math.BigDecimal;
import java.util.ArrayList;

class ListaSpesaTest {

    private static final String CATEGORIA_DOLCI = "Alimentari";
    private static final BigDecimal PREZZO_UNITARIO = BigDecimal.TEN;
    private static final BigDecimal PREZZO_UNITARIO_DUE = BigDecimal.ONE;
    private static final String NOME_ARTICOLO = "Pizza";
    private static final String NOME_ARTICOLO_DUE = "Focaccia";
    private static final String NOME_LISTA = "lista1";
    private static final int QUANTITA_DEFAULT = 3;

    //istanze di oggetti di test
    private ListaSpesa listaSpesa;

    @BeforeEach
    void setUp() throws ArticoloException, ListaSpesaException {
        Articolo articolo = new Articolo(NOME_ARTICOLO, PREZZO_UNITARIO, QUANTITA_DEFAULT, CATEGORIA_DOLCI);
        Articolo articolo2 = new Articolo(NOME_ARTICOLO_DUE, PREZZO_UNITARIO_DUE, QUANTITA_DEFAULT + 2,
                CATEGORIA_DOLCI);
        ArrayList<Articolo> list = new ArrayList<>();
        list.add(articolo);
        list.add(articolo2);
        listaSpesa = new ListaSpesa(NOME_LISTA, list);
    }

    @Test
    void addArticolo() throws ArticoloException {
        this.listaSpesa.addArticolo(mockArticolo()).addArticolo(mockArticolo());
        Assertions.assertEquals(4, this.listaSpesa.size());
    }

    @Test
    void removeArticolo() throws ArticoloException {
        Assertions.assertEquals(1, this.listaSpesa.removeArticolo(new Articolo(NOME_ARTICOLO, PREZZO_UNITARIO, QUANTITA_DEFAULT, CATEGORIA_DOLCI)).size());
    }

    @Test
    void getArticoliDiCategoria() {
        Assertions.assertEquals(2, this.listaSpesa.getArticoliDiCategoria(CATEGORIA_DOLCI).size());
        Assertions.assertEquals(0, this.listaSpesa.getArticoliDiCategoria("Categoria che non esiste").size());
    }

    @Test
    void getArticoloPiuCostoso() {
        Assertions.assertEquals(NOME_ARTICOLO, this.listaSpesa.getArticoloPiuCostoso().getNomeArticolo());
    }

    @Test
    void calcolaCostoTotaleSpesa() {
        Assertions.assertEquals(BigDecimal.valueOf(35), this.listaSpesa.calcolaCostoTotaleSpesa());
    }

    @Test
    void eliminaCategoria() {
        this.listaSpesa.eliminaCategoria(CATEGORIA_DOLCI);
        Assertions.assertEquals(0, this.listaSpesa.getArticoliDiCategoria(CATEGORIA_DOLCI).size());
        Assertions.assertEquals(2, this.listaSpesa.getArticoliDiCategoria(Costanti.CATEGORIA_DEFAULT).size());
    }

    @Test
    void svuotaLista() {
        this.listaSpesa.svuotaLista();
        Assertions.assertEquals(0, this.listaSpesa.size());
    }

    @Test
    void setListaArticoli() {
        this.listaSpesa.setListaArticoli(new ArrayList<>());
        Assertions.assertEquals(0, listaSpesa.size());
    }
    @Test
    void getArticoliDiNomePrefixTest(){
        Assertions.assertEquals(2, this.listaSpesa.getArticoliDiNomePrefix("Ali").size());
        Assertions.assertEquals(0, this.listaSpesa.getArticoliDiNomePrefix(null).size());
    }

    private Articolo mockArticolo() throws ArticoloException {
        return new Articolo(NOME_ARTICOLO, PREZZO_UNITARIO, QUANTITA_DEFAULT, CATEGORIA_DOLCI);
    }
}