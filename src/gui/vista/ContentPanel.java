package gui.vista;

import model.GestioneSpese;
import model.ListaSpesa;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableColumnModel;
import java.awt.BorderLayout;
import java.util.Collections;
import java.util.Optional;

@SuppressWarnings("serial")
public class ContentPanel extends JPanel {

    private GestioneSpese model;

    private JList<ListaSpesa> spesaJList;
    private DefaultListModel<ListaSpesa> listModel;

    private JList<String> categorieJList;
    private DefaultListModel<String> categorieModel;

    public ContentPanel(GestioneSpese gestioneSpese) {
        //fields initialization
        this.model = gestioneSpese;

        this.listModel = new DefaultListModel<>();
        this.categorieModel = new DefaultListModel<>();

        updateView();

        this.spesaJList = new JList<>(listModel);
        this.categorieJList = new JList<>(categorieModel);

        //layout specs
        setLayout(new BorderLayout());
        setSize(700, 700);

        spesaJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        spesaJList.setBorder(BorderFactory.createTitledBorder("Liste della spesa"));
        add(new JScrollPane(spesaJList), BorderLayout.CENTER);
        //categorieJList.addListSelectionListener(new DefaultTableColumnModel());
        //spesaJList.addListSelectionListener(new DefaultTableColumnModel());

        categorieJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        categorieJList.setBorder(BorderFactory.createTitledBorder("Categorie prodotti"));
        add(new JScrollPane(categorieJList), BorderLayout.EAST);

    }

    public void updateView() {
        //refresh the view
        aggiornaListeModel();
        aggiornaCategorieModel();
    }

    private void aggiornaListeModel() {
        listModel.clear();
        Optional.of(model)
                .map(GestioneSpese::getListaSpese)
                .orElse(Collections.emptyList())
                .forEach(lista -> listModel.addElement(lista));
    }

    private void aggiornaCategorieModel() {
        categorieModel.clear();
        Optional.of(model)
                .map(GestioneSpese::getCategorie)
                .orElse(Collections.emptySet())
                .forEach(categorieModel::addElement);
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
