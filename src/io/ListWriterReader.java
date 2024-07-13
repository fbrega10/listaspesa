package io;

import exceptions.ListWriterReaderException;
import model.Articolo;
import model.ListaSpesa;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Alla classe ListWriterReader e' delegata la gestione dell'I/O
 * su file tramite i metodi corrispondenti
 */
public class ListWriterReader {
    public static final String path = new File("").getAbsoluteFile() + "/src/resources/";
    private static final String delimiter = ";";

    /**
     * Read file lista spesa in formato *.csv.
     * La prima riga rappresenta il nome della lista, le altre sono i field dell'oggetto articolo
     * separato dal carattere ';'.
     * Si assume che tutti i file siano presenti nella cartella src/resources/*.csv
     *
     * @param fileName nome del file
     * @return Oggetto di tipo ListaSpesa
     * @throws ListWriterReaderException eccezione in caso di eccezioni per I/O o file non trovato o cast exception per
     *                                   formato errato nei tipi dato di un Articolo/ListaSpesa
     */
    public static ListaSpesa readFile(String fileName) throws ListWriterReaderException {
        try {
            String file = path + fileName;
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            String listName = bufferedReader.readLine();
            ArrayList<Articolo> list = new ArrayList<>();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                List<String> buffer = ListWriterReader.splitBySemicolon(line);
                switch (buffer.size()) {
                    case 2 -> list.add(new Articolo(buffer.get(0), new BigDecimal(buffer.get(1))));
                    case 4 -> list.add(new Articolo(buffer.get(0), new BigDecimal(buffer.get(1)),
                            Integer.parseInt(buffer.get(2)), buffer.get(3)));
                }
            }
            bufferedReader.close();
            return new ListaSpesa(listName, list);
        } catch (Exception e) {
            throw new ListWriterReaderException(e.getMessage());
        }
    }

    /**
     * Scrive una lista su file di estensione .csv
     *
     * @param fileName   nome del file (estensione .csv)
     * @param listaSpesa oggetto lista della spesa da scrivere in output
     * @throws IOException Eccezione per problemi IO
     */
    public static void writeToCsv(String fileName, ListaSpesa listaSpesa) throws IOException {
        String file = path + fileName;
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
        bufferedWriter.write(listaSpesa.toCsv());
        bufferedWriter.close();
    }

    /**
     * Metodo che divide una stringa per token ';' e restituisce una Lista di stringhe immutabili
     *
     * @param s la stringa di input (buffer)
     * @return List<String>
     */
    public static List<String> splitBySemicolon(String s) {
        return Arrays.asList(s.split(ListWriterReader.delimiter));
    }

}
