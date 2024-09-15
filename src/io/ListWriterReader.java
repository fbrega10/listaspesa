package io;

import exceptions.ArticoloException;
import exceptions.ListWriterReaderException;
import exceptions.ListaSpesaException;
import model.Articolo;
import model.ListaSpesa;
import utils.Costanti;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
     * Scrive una lista su file indicato.
     *
     * @param file       : nome file + path
     * @param listaSpesa istanza dell'oggetto listaSpesa che si vuole serializzare su file.
     * @throws IOException nel caso in cui l'operazione di I/O non andasse a buon fine.
     */
    public static void writeListaSuFile(String file, ListaSpesa listaSpesa) throws IOException {
        file = path + file;
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
        objectOutputStream.writeObject(listaSpesa);
        objectOutputStream.flush();
        objectOutputStream.close();
    }

    /**
     * legge e restituisce una lista da file indicato.
     *
     * @param file : nome file + path
     * @throws IOException            nel caso in cui l'operazione di I/O non andasse a buon fine.
     * @throws ClassNotFoundException nel caso in cui la classe dell'istanza da deserializzare non esista.
     */
    public static ListaSpesa readListaDaFile(String file) throws IOException, ClassNotFoundException {
        FileInputStream fileInputStream = new FileInputStream(file);
        ObjectInputStream inputStream = new ObjectInputStream(fileInputStream);
        ListaSpesa listaSpesa = (ListaSpesa) inputStream.readObject();
        inputStream.close();
        return listaSpesa;
    }

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
    public static ListaSpesa readCsvFile(String fileName) throws ListWriterReaderException, IOException, ArticoloException, ListaSpesaException {
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
        } catch (NumberFormatException e) {
            throw new ArticoloException(Costanti.ECCEZ_VALIDAZIONE_QUANTITA);
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

    public static void main(String[] args) throws IOException, ListaSpesaException, ClassNotFoundException, ArticoloException {


    }
}
