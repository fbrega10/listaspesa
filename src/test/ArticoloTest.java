package test;

import model.Articolo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;

import java.math.BigDecimal;

public class ArticoloTest {

    //variabili statiche di stringhe appoggio
    private static final String CATEGORIA_DOLCI = "Dolci";
    private static final BigDecimal PREZZO_UNITARIO = BigDecimal.ONE;
    private static final String NOME_ARTICOLO = "Object1";
    private static final int QUANTITA_DEFAULT = 3;

    //istanze di oggetti di test
    private Articolo articolo;
    private Articolo articolo1;


    @BeforeEach
    public void testSetup() {
        articolo = new Articolo(NOME_ARTICOLO, PREZZO_UNITARIO, 3, CATEGORIA_DOLCI);
        articolo1 = new Articolo(NOME_ARTICOLO, PREZZO_UNITARIO, 3, CATEGORIA_DOLCI);
    }


    @Test
    public void testEquals() {
        Assertions.assertEquals(articolo, articolo1);
    }

    @Test
    public void testNotEquals() {
        articolo.setCategoria("abcd");
        articolo.setQuantita(4);
        articolo.setNomeArticolo("ABCD");
        articolo.setPrezzoUnitario(BigDecimal.TEN);
        Assertions.assertNotEquals(articolo, articolo1);
    }

    @Test
    public void testCostruzione() {
        Assertions.assertEquals(CATEGORIA_DOLCI, articolo.getCategoria());
        Assertions.assertEquals(PREZZO_UNITARIO, articolo.getPrezzoUnitario());
    }

    @Test
    public void testEccezioneCostruttore() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            articolo = new Articolo(NOME_ARTICOLO, BigDecimal.valueOf(-1), QUANTITA_DEFAULT, CATEGORIA_DOLCI);
        });
    }
}