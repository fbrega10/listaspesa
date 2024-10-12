package test;

import exceptions.ArticoloException;
import exceptions.ListaSpesaException;
import model.Articolo;
import model.GestioneSpese;
import model.ListaSpesa;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.DefaultListModel;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;

class GestioneSpeseTest {

    private GestioneSpese gestioneSpese;

    @BeforeEach
    void testSetup() throws ArticoloException, ListaSpesaException {
        gestioneSpese = new GestioneSpese();
        ArrayList<Articolo> list = mockListaSpesa();
        gestioneSpese.addListaSpesa(new ListaSpesa("lista_base", list));
    }

    @Test
    void testCostruzione() {
        Assertions.assertEquals(1, this.gestioneSpese.getListaSpeseSize());
        Assertions.assertEquals(4, this.gestioneSpese.getCategorieSize());
    }

    @Test
    void addCategoria() {
        final String daAggiungere = "Non identificata";
        this.gestioneSpese.addCategoria(daAggiungere);
        Assertions.assertTrue(this.gestioneSpese.containsCategoria(daAggiungere));
        Assertions.assertFalse(this.gestioneSpese.addCategoria(null));
    }

    @Test
    void containsCategoriaTest() {
        final String daAggiungere = "Nuova categoria";
        this.gestioneSpese.addCategoria(daAggiungere);
        Assertions.assertTrue(this.gestioneSpese.containsCategoria(daAggiungere));
        Assertions.assertFalse(this.gestioneSpese.containsCategoria("Categoria non presente"));
    }

    @Test
    void categorieSizeTest() {
        final String daAggiungere = "Nuova categoria";
        this.gestioneSpese.addCategoria(daAggiungere);
        Assertions.assertTrue(this.gestioneSpese.containsCategoria(daAggiungere));
        Assertions.assertEquals(5, this.gestioneSpese.getCategorieSize());
    }

    @Test
    void listaSpeseSizeTest() throws ListaSpesaException {
        Assertions.assertEquals(1, this.gestioneSpese.getListaSpeseSize());
        this.gestioneSpese.addListaSpesa(new ListaSpesa("nome"));
        Assertions.assertEquals(2, this.gestioneSpese.getListaSpeseSize());
    }

    @Test
    void defaultListModelTest() {
        DefaultListModel<ListaSpesa> listModel = new DefaultListModel<>();
        gestioneSpese.updateListModel(listModel);
        Assertions.assertEquals(gestioneSpese.getListaSpeseSize(), listModel.size());
    }

    @Test
    void defaultCategorieListModelTest() {
        DefaultListModel<String> categorieModel = new DefaultListModel<>();
        gestioneSpese.updateCategorieListModel(categorieModel);
        Assertions.assertEquals(gestioneSpese.getCategorieSize(), categorieModel.size());
    }

    @Test
    void removeCategoria() {
        final String scarpe = "Scarpe";
        this.gestioneSpese.removeCategoria(scarpe);
        Assertions.assertFalse(this.gestioneSpese.containsCategoria(scarpe));
        Assertions.assertFalse(this.gestioneSpese.removeCategoria(null));
    }

    @Test
    void addListaSpesa() throws ListaSpesaException, ArticoloException {
        this.gestioneSpese.addListaSpesa(new ListaSpesa("lista_base2", mockListaSpesa()));
        Assertions.assertEquals(2, this.gestioneSpese.getListaSpeseSize());
        gestioneSpese.addListaSpesa(null);
        Assertions.assertEquals(2, this.gestioneSpese.getListaSpeseSize());
    }

    @Test
    void addListaSpesaException() throws ListaSpesaException, ArticoloException {
        Assertions.assertThrows(ArticoloException.class, () -> this.gestioneSpese
                .addListaSpesa(new ListaSpesa("lista_base2",
                        Collections.singletonList(
                                new Articolo("art", BigDecimal.ZERO, -1, "categoria")))));
        Assertions.assertThrows(ListaSpesaException.class, () -> this.gestioneSpese.addListaSpesa(new ListaSpesa(null)));
    }

    @Test
    void testCostruttoreConNomeNullListaException() throws ListaSpesaException {
        Assertions.assertThrows(ListaSpesaException.class, () -> this.gestioneSpese.addListaSpesa(new ListaSpesa(null)));
    }

    @Test
    void testSecondoCostruttoreConNomeNullListaException() throws ListaSpesaException {
        Assertions.assertThrows(ListaSpesaException.class, () -> this.gestioneSpese.addListaSpesa(new ListaSpesa(null, Collections.emptyList())));
    }

    @Test
    void rimuoviListaSpesa() throws ListaSpesaException, ArticoloException {
        this.gestioneSpese.rimuoviListaSpesa(new ListaSpesa("lista_base", mockListaSpesa()));
        Assertions.assertEquals(0, this.gestioneSpese.getListaSpeseSize());
    }

    @Test
    void rimuoviListaSpesaPerNome() {
        this.gestioneSpese.rimuoviListaSpesa("lista_base");
        Assertions.assertEquals(0, this.gestioneSpese.getListaSpeseSize());
    }

    @Test
    void getListaByNome() {
        Assertions.assertNotNull(this.gestioneSpese.getListaByNome("lista_base"));
    }

    @Test
    void resetGestioneSpese() {
        this.gestioneSpese.resetGestioneSpese();
        Assertions.assertEquals(1, this.gestioneSpese.getCategorieSize());
        Assertions.assertEquals(0, this.gestioneSpese.getListaSpeseSize());
    }

    private ArrayList<Articolo> mockListaSpesa() throws ArticoloException, ListaSpesaException {
        ArrayList<Articolo> list = new ArrayList<>();
        list.add(new Articolo("Ciabatte", BigDecimal.valueOf(5), 10, "Scarpe"));
        list.add(new Articolo("Nike", BigDecimal.TEN, 2, "Scarpe"));
        list.add(new Articolo("Vestito", BigDecimal.valueOf(40), 3, "Indumenti"));
        list.add(new Articolo("Cappello", BigDecimal.valueOf(11), 5, "Cappelli"));
        return list;
    }
}