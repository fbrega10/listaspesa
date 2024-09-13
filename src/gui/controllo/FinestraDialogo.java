package gui.controllo;

import exceptions.ListaSpesaException;
import model.GestioneSpese;
import model.ListaSpesa;
import utils.StringUtils;

import javax.swing.JOptionPane;

public class FinestraDialogo {

    private GestioneSpese model;

    public FinestraDialogo(GestioneSpese model) {
        this.model = model;
    }

    public void aggiungiLista() {
        //TODO: inserisci nuova lista
        String nomeLista = JOptionPane.showInputDialog("Inserisci il nome della lista : ");
        if (StringUtils.checkNullOrEmpty(nomeLista)) {
            try {
                ListaSpesa listaSpesa = new ListaSpesa(nomeLista);
            } catch (ListaSpesaException e) {
                mostraMessaggio("Errore nome lista non valido : " + nomeLista);
                return;
            }
        }
        System.out.println("nome lista " + nomeLista);
    }

    public void aggiungiCategoria() {
        String nomeCategoria = JOptionPane.showInputDialog("Inserisci una nuova categoria: ");
        if (StringUtils.checkNullOrEmpty(nomeCategoria)) {
            if (model.getCategorie().contains(nomeCategoria)) {
                mostraMessaggio("Categoria " + nomeCategoria + " gia' presente.");
            } else {
                model.addCategoria(nomeCategoria);
                mostraMessaggio("categoria " + nomeCategoria + " aggiunta.");
            }
        }
    }

    public void rimuoviLista() {
        String nomeLista = JOptionPane.showInputDialog("Inserisci nome lista da rimuovere: ");
        if (StringUtils.checkNullOrEmpty(nomeLista)) {
            if (this.model.rimuoviListaSpesa(nomeLista)) {
                mostraMessaggio("lista : " + nomeLista + " rimossa.");
            } else {
                mostraMessaggio("lista : " + nomeLista + " non presente.");
            }
        } else {
            mostraMessaggio("Nome inserito non valido.");
        }
    }

    public void rimuoviCategoria() {
        String nomeCategoria = JOptionPane.showInputDialog("Inserisci categoria da rimuovere: ");
        if (StringUtils.checkNullOrEmpty(nomeCategoria)) {
            if (model.removeCategoria(nomeCategoria)) {
                mostraMessaggio("Categoria " + nomeCategoria + " rimossa.");
                System.out.println("categoria aggiunta : " + nomeCategoria);
            } else mostraMessaggio("Categoria non presente");
        } else {
            mostraMessaggio("Errore nella categoria inserita: deve essere inserita una stringa");
        }
    }

    private static void mostraMessaggio(String messaggio) {
        JOptionPane.showMessageDialog(new JOptionPane(), messaggio);
    }
}
