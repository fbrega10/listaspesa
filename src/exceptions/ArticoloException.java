package exceptions;

/**
 * Tipo di Eccezione legata all'oggetto Articolo.
 * Estende Exception e viene lanciata nel momento in cui la costruzione dell'articolo
 * non rispetta i requisiti della documentazione e non e' un oggetto valido nel contesto.
 */
public class ArticoloException extends Exception{
    /**
     * Istanzia un nuovo oggetto ArticoloException.
     *
     * @param msg messaggio dell'eccezione
     */
    public ArticoloException(String msg){
        super(msg);
    }
}
