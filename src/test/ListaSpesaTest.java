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
import java.util.Collections;

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
    private Articolo articolo;
    private Articolo articolo2;

    @BeforeEach
    void setUp() throws ArticoloException, ListaSpesaException {
        articolo = new Articolo(NOME_ARTICOLO, PREZZO_UNITARIO, QUANTITA_DEFAULT, CATEGORIA_DOLCI);
        articolo2 = new Articolo(NOME_ARTICOLO_DUE, PREZZO_UNITARIO_DUE, QUANTITA_DEFAULT + 2,
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
    void getArticoliDiNomePrefixTest() {
        Assertions.assertEquals(1, this.listaSpesa.getArticoliDiNomePrefix("Pi").size());
        Assertions.assertEquals(0, this.listaSpesa.getArticoliDiNomePrefix(null).size());
    }

    @Test
    void removeArticoloDaNomeTest() {
        Assertions.assertTrue(this.listaSpesa.removeArticolo(NOME_ARTICOLO));
        Assertions.assertFalse(this.listaSpesa.removeArticolo(NOME_ARTICOLO));
    }

    @Test
    void getArticoloCostoTotaleMaggioreTest() throws ListaSpesaException {
        Assertions.assertEquals(this.articolo, this.listaSpesa.getArticoloCostoTotaleMaggiore());
        Assertions.assertNull(new ListaSpesa("abcd").getArticoloCostoTotaleMaggiore());
    }

    @Test
    void getArticoloByNomeTest() {
        Assertions.assertNotNull(this.listaSpesa.getArticoloByNome(articolo.getNomeArticolo()));
        Assertions.assertNull(this.listaSpesa.getArticoloByNome("nomeacaso"));
    }

    @Test
    void testListaCostruzioneNomeNullException() {
        Assertions.assertThrows(ListaSpesaException.class, () -> new ListaSpesa(null, Collections.emptyList()));
    }

    @Test
    void testListaCostruzioneArticoloException() {
        Assertions.assertThrows(ArticoloException.class, () -> new ListaSpesa("lista1",
                Collections.nCopies(22, new Articolo("articolo", BigDecimal.ONE, -1, "categoria"))));
        Assertions.assertThrows(ArticoloException.class, () -> new ListaSpesa("lista1",
                Collections.singletonList(new Articolo("articolo", BigDecimal.ZERO, 2, "categoria"))));
    }

    private Articolo mockArticolo() throws ArticoloException {
        return new Articolo(NOME_ARTICOLO, PREZZO_UNITARIO, QUANTITA_DEFAULT, CATEGORIA_DOLCI);
    }
}