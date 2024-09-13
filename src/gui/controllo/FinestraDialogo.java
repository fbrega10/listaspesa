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
                JOptionPane.showMessageDialog(new JOptionPane(), "Errore nome lista non valido : " + nomeLista);
                return;
            }

        }
        System.out.println("nome lista " + nomeLista);
    }

    public void aggiungiCategoria() {
        String nomeCategoria = JOptionPane.showInputDialog("Inserisci una nuova categoria: ");
        if (StringUtils.checkNullOrEmpty(nomeCategoria)) {
            if (model.getCategorie().contains(nomeCategoria)) {
                JOptionPane.showMessageDialog(new JOptionPane(), "Categoria " + nomeCategoria + " gia' presente.");
            } else {
                model.addCategoria(nomeCategoria);
            }
        }
    }

    public void rimuoviLista() {
        String nomeLista = JOptionPane.showInputDialog("Inserisci nome lista da rimuovere: ");
        if (StringUtils.checkNullOrEmpty(nomeLista)) {
            if (this.model.rimuoviListaSpesa(nomeLista)) {
                JOptionPane.showMessageDialog(new JOptionPane(), "lista : " + nomeLista + " rimossa.");
            } else {
                JOptionPane.showMessageDialog(new JOptionPane(), "lista : " + nomeLista + " non presente.");
            }
        } else {
            JOptionPane.showMessageDialog(new JOptionPane(), "Nome inserito non valido.");
        }
    }

    public void rimuoviCategoria() {
        String nomeCategoria = JOptionPane.showInputDialog("Inserisci categoria da rimuovere: ");
        if (StringUtils.checkNullOrEmpty(nomeCategoria)) {
            if (model.removeCategoria(nomeCategoria)) {
                JOptionPane.showMessageDialog(new JOptionPane(), "Categoria " + nomeCategoria + " rimossa.");
                System.out.println("categoria aggiunta : " + nomeCategoria);
            } else JOptionPane.showMessageDialog(null, "Categoria non presente");
        } else {
            JOptionPane.showMessageDialog(new JOptionPane(), "Errore nella categoria inserita: deve essere inserita una stringa");
        }
    }

}
