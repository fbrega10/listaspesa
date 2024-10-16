package gui.vista;

import model.GestioneSpese;
import model.ListaSpesa;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.util.Collections;
import java.util.Optional;

@SuppressWarnings("serial")
public class ContentPanel extends JPanel {

    private GestioneSpese model;

    private JList<ListaSpesa> spesaJList;
    private DefaultListModel<ListaSpesa> listModel;

    private JList<String> categorieJList;
    private DefaultListModel<String> categorieModel;

    /**
     * @param gestioneSpese Pannello che contiene il cuore del gestore.
     *                      Dispone due JList che rappresentano categorie/liste spesa.
     *                      Tramite il metodo updateView aggiorna sulla base dell'oggetto
     *                      la vista di queste due JList.
     */
    public ContentPanel(GestioneSpese gestioneSpese) {
        //fields initialization
        this.model = gestioneSpese;

        this.listModel = new DefaultListModel<>();
        this.categorieModel = new DefaultListModel<>();

        updateView();

        this.spesaJList = new JList<>(listModel);
        this.spesaJList.setBackground(new Color(139, 201, 218));
        this.spesaJList.setFont(new Font("Times New Roman", Font.BOLD, 14));

        this.categorieJList = new JList<>(categorieModel);
        this.categorieJList.setBackground(new Color(95, 179, 211));
        this.categorieJList.setFont(new Font("Arial", Font.BOLD, 14));

        //layout specs
        setLayout(new BorderLayout());
        setSize(700, 700);

        spesaJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        VistaUtils.setJListTitledBorder(spesaJList,
                "Liste della spesa",
                new Font("Times New Roman", Font.BOLD, 20),
                TitledBorder.CENTER);
        VistaUtils.cellRendererGiustificato(spesaJList);
        add(new JScrollPane(spesaJList), BorderLayout.CENTER);

        categorieJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        VistaUtils.cellRendererGiustificato(categorieJList);
        VistaUtils.setJListTitledBorder(categorieJList,
                "Categorie Prodotti",
                new Font("Times New Roman", Font.BOLD, 20),
                TitledBorder.CENTER);
        add(new JScrollPane(categorieJList), BorderLayout.SOUTH);
    }

    /**
     * Aggiorna i contenuti delle due JList e quindi l'interfaccia grafica
     * rispetto ai cambiamenti dell'oggetto gestioneSpese originale
     */
    public void updateView() {
        //refresh the view
        aggiornaListeModel();
        aggiornaCategorieModel();
    }

    /**
     * Aggiorna JList degli elementi ListaSpesa con il contenuto del gestore
     */
    private void aggiornaListeModel() {
        model.updateListModel(listModel);
    }

    /**
     * Aggiorna JList degli elementi categorie
     */
    private void aggiornaCategorieModel() {
        model.updateCategorieListModel(categorieModel);
    }

    //GETTERS
    public GestioneSpese getModel() {
        return model;
    }

    public JList<ListaSpesa> getSpesaJList() {
        return spesaJList;
    }

    public DefaultListModel<ListaSpesa> getListModel() {
        return listModel;
    }

    public JList<String> getCategorieJList() {
        return categorieJList;
    }

    public DefaultListModel<String> getCategorieModel() {
        return categorieModel;
    }
}
