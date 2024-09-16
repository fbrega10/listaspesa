package test;

import exceptions.ArticoloException;
import exceptions.ListaSpesaException;
import model.Articolo;
import model.GestioneSpese;
import model.ListaSpesa;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;

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
        Assertions.assertEquals(1, this.gestioneSpese.getListaSpese().size());
        Assertions.assertEquals(4, this.gestioneSpese.getCategorie().size());
    }

    @Test
    void addCategoria() {
        final String daAggiungere = "Non identificata";
        this.gestioneSpese.addCategoria(daAggiungere);
        Assertions.assertTrue(this.gestioneSpese.getCategorie().contains(daAggiungere));
        Assertions.assertFalse(this.gestioneSpese.addCategoria(null));
    }

    @Test
    void removeCategoria() {
        final String scarpe = "Scarpe";
        this.gestioneSpese.removeCategoria(scarpe);
        Assertions.assertFalse(this.gestioneSpese.getCategorie().contains(scarpe));
        Assertions.assertFalse(this.gestioneSpese.removeCategoria(null));
    }

    @Test
    void addListaSpesa() throws ListaSpesaException, ArticoloException {
        this.gestioneSpese.addListaSpesa(new ListaSpesa("lista_base2", mockListaSpesa()));
        Assertions.assertEquals(2, this.gestioneSpese.getListaSpese().size());
        gestioneSpese.addListaSpesa(null);
        Assertions.assertEquals(2, this.gestioneSpese.getListaSpese().size());
    }

    @Test
    void rimuoviListaSpesa() throws ListaSpesaException, ArticoloException {
        this.gestioneSpese.rimuoviListaSpesa(new ListaSpesa("lista_base", mockListaSpesa()));
        Assertions.assertEquals(0, this.gestioneSpese.getListaSpese().size());
    }

    @Test
    void rimuoviListaSpesaPerNome(){
        this.gestioneSpese.rimuoviListaSpesa("lista_base");
        Assertions.assertEquals(0, this.gestioneSpese.getListaSpese().size());
    }

    @Test
    void getListaByNome() {
        Assertions.assertNotNull(this.gestioneSpese.getListaByNome("lista_base"));
    }

    @Test
    void resetGestioneSpese() {
        this.gestioneSpese.resetGestioneSpese();
        Assertions.assertEquals(1, this.gestioneSpese.getCategorie().size());
        Assertions.assertEquals(0, this.gestioneSpese.getListaSpese().size());
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