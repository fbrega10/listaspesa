package gui.controllo;

import gui.vista.ContentPanel;
import model.GestioneSpese;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static utils.Costanti.BUTTON_AGGIUNGI_CATEGORIA;
import static utils.Costanti.BUTTON_AGGIUNGI_LISTA;
import static utils.Costanti.BUTTON_RIMUOVI_CATEGORIA;

public class ControlloGestioneSpese implements ActionListener {

    private ContentPanel contenutoGestioneSpese;
    private GestioneSpese model;

    public ControlloGestioneSpese(ContentPanel contenutoGestioneSpese, GestioneSpese model) {
        this.contenutoGestioneSpese = contenutoGestioneSpese;
        this.model = model;
    }

    //E' l'handler delle azioni (eventi), vanno usate la vista e il modello
    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        JButton source = ( JButton ) actionEvent.getSource();
        switch (source.getText()) {
            case BUTTON_AGGIUNGI_LISTA -> {
                String nomeLista = JOptionPane.showInputDialog("Inserisci il nome della lista : ");
                //if (nomeLista != null && !nomeLista.trim().isEmpty())
                System.out.println("nome lista " + nomeLista);}
            case BUTTON_AGGIUNGI_CATEGORIA -> {
                String nomeCategoria = JOptionPane.showInputDialog(this, "Inserisci una nuova categoria");
                if (nomeCategoria != null && !nomeCategoria.trim().isEmpty()){
                    model.addCategoria(nomeCategoria);
                    System.out.println("categoria aggiunta : "+ nomeCategoria);
                }
            }
            case BUTTON_RIMUOVI_CATEGORIA -> {
                JPanel jPanel = new JPanel(new GridLayout(1, 1));
                JTextField categoriaDaRimuovereField = new JTextField();
                jPanel.add(new Label("categoria:"));
                int result = JOptionPane.showConfirmDialog(null, jPanel, "Rimuovi categoria", JOptionPane.OK_CANCEL_OPTION);
                if (result == JOptionPane.OK_OPTION){
                    model.removeCategoria(categoriaDaRimuovereField.getText());
                }
                else{
                    JOptionPane.showConfirmDialog(null, "Inserire una categoria valida", "Errore", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
        this.contenutoGestioneSpese.updateView();
    }
}
