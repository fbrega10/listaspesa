package test;

import exceptions.ListWriterReaderException;
import io.ListWriterReader;
import model.ListaSpesa;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

class ListWriterReaderTest {

    private final String fileName = "example1.csv";
    private static final String output = "output.csv";

    @AfterAll
    static void exitStrategy(){
        File f = new File(ListWriterReader.path + ListWriterReaderTest.output);
       if (f.exists()){
          f.deleteOnExit();
       }
    }

    @Test
    void readFileCsv() throws ListWriterReaderException {
        System.out.println(new File("").getAbsoluteFile());
        ListaSpesa list = ListWriterReader.readFile(fileName);
        System.out.println(list);
        Assertions.assertNotNull(list);
        Assertions.assertEquals(2, list.size());
    }
    @Test
    void writeFileCsv() throws ListWriterReaderException, IOException{
      ListaSpesa spesa = ListWriterReader.readFile(fileName);
      ListWriterReader.writeToCsv(output, spesa);
      Assertions.assertNotNull(ListWriterReader.readFile(fileName));
    }

    @Test
    void splitBySemicolon() {
        Assertions.assertEquals(4, ListWriterReader.splitBySemicolon("nome2;34.50;1;Alimentari").size());
        Assertions.assertEquals(2, ListWriterReader.splitBySemicolon("nome2;34.50").size());
    }
}