import exceptions.ArticoloException;
import exceptions.ListaSpesaException;
import gui.GestioneSpeseGui;
import model.Articolo;
import model.GestioneSpese;
import model.ListaSpesa;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Main {
    private static GestioneSpese gestioneSpese;

    public static void main(String[] args) throws ArticoloException, ListaSpesaException {
        gestioneSpese = new GestioneSpese();
        List<Articolo> articoli = new ArrayList<>();
        articoli.add(new Articolo("Scarpe", BigDecimal.ONE, 2, "indumenti"));
        articoli.add(new Articolo("Borse", BigDecimal.ONE, 2, "indumenti"));
        gestioneSpese.addListaSpesa(new ListaSpesa("lista1", articoli));
        //System.out.println(gestioneSpese.toString());
        interfacciaGrafica();
    }

    private static void interfacciaGrafica() {
        new GestioneSpeseGui(gestioneSpese);
    }
}