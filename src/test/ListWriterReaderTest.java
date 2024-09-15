package test;

import exceptions.ArticoloException;
import exceptions.ListWriterReaderException;
import exceptions.ListaSpesaException;
import io.ListWriterReader;
import model.Articolo;
import model.ListaSpesa;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Collections;

class ListWriterReaderTest {

    private final String fileName = "example1.csv";
    private static final String output = "output.csv";
    private static final String fileSerializerNome = "file.txt";

    @AfterAll
    static void exitStrategy() {
        File f = new File(ListWriterReader.path + ListWriterReaderTest.output);
        if (f.exists()) {
            f.deleteOnExit();
        }
        File g = new File(ListWriterReader.path + ListWriterReaderTest.fileSerializerNome);
        if (g.exists()) {
            g.deleteOnExit();
        }
    }

    @Test
    void readCsvFileCsv() throws ListWriterReaderException, ListaSpesaException, IOException, ArticoloException {
        System.out.println(new File("").getAbsoluteFile());
        ListaSpesa list = ListWriterReader.readCsvFile(fileName);
        System.out.println(list);
        Assertions.assertNotNull(list);
        Assertions.assertEquals(2, list.size());
    }

    @Test
    void writeFileCsv() throws ListWriterReaderException, IOException, ListaSpesaException, ArticoloException {
        ListaSpesa spesa = ListWriterReader.readCsvFile(fileName);
        ListWriterReader.writeToCsv(output, spesa);
        Assertions.assertNotNull(ListWriterReader.readCsvFile(fileName));
    }

    @Test
    void writeReadListaSuFileTest() throws IOException, ListaSpesaException, ArticoloException, ClassNotFoundException {
        String outputPath = ListWriterReader.path + fileSerializerNome;
        ListWriterReader.writeListaSuFile(fileSerializerNome, new ListaSpesa("listaSpesa",
                Collections.singletonList(new Articolo("articolo1", BigDecimal.ONE, 3, null))));
        Assertions.assertTrue(new File(outputPath).exists());
        ListaSpesa listaSpesa = ListWriterReader.readListaDaFile(outputPath);
        Assertions.assertEquals(1, listaSpesa.size());
    }

    @Test
    void splitBySemicolon() {
        Assertions.assertEquals(4, ListWriterReader.splitBySemicolon("nome2;34.50;1;Alimentari").size());
        Assertions.assertEquals(2, ListWriterReader.splitBySemicolon("nome2;34.50").size());
    }
}