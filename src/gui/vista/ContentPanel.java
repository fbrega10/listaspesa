package gui.vista;

import model.GestioneSpese;
import model.ListaSpesa;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import java.awt.BorderLayout;
import java.util.Collections;
import java.util.Optional;

@SuppressWarnings("serial")
public class ContentPanel extends JPanel {

    private GestioneSpese model;

    private JList<ListaSpesa> spesaJList;
    private DefaultListModel<ListaSpesa> defaultListModel;

    private JList<String> categorieJList;
    private DefaultListModel<String> categorieDefaultModel;

    public ContentPanel(GestioneSpese gestioneSpese) {
        //fields initialization
        this.model = gestioneSpese;

        this.defaultListModel = new DefaultListModel<>();
        this.categorieDefaultModel = new DefaultListModel<>();

        this.spesaJList = new JList<>(defaultListModel);
        this.categorieJList = new JList<>(categorieDefaultModel);

        updateView();

        //layout specs
        setLayout(new BorderLayout());
        setSize(700, 700);

        spesaJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        spesaJList.setBorder(BorderFactory.createTitledBorder("Liste della spesa"));
        add(new JScrollPane(spesaJList), BorderLayout.CENTER);

        categorieJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        categorieJList.setBorder(BorderFactory.createTitledBorder("Categorie prodotti"));
        add(new JScrollPane(categorieJList), BorderLayout.EAST);

    }

    public void updateView() {
        //refresh all the views
        modelToDefault(model);
        categorieModelSetter(model);
        this.categorieJList = new JList<>(categorieDefaultModel);
        this.spesaJList = new JList<>(defaultListModel);
    }

    private void modelToDefault(GestioneSpese model) {
        defaultListModel.clear();
        Optional.of(model)
                .map(GestioneSpese::getListaSpese)
                .orElse(Collections.emptyList())
                .forEach(lista -> defaultListModel.addElement(lista));
    }

    private void categorieModelSetter(GestioneSpese model) {
        categorieDefaultModel.clear();
        Optional.of(model)
                .map(GestioneSpese::getCategorie)
                .orElse(Collections.emptySet())
                .forEach(this.categorieDefaultModel::addElement);
    }

    //GETTERS
    public GestioneSpese getModel() {
        return model;
    }

    public JList<ListaSpesa> getSpesaJList() {
        return spesaJList;
    }

    public DefaultListModel<ListaSpesa> getDefaultListModel() {
        return defaultListModel;
    }

    public JList<String> getCategorieJList() {
        return categorieJList;
    }

    public DefaultListModel<String> getCategorieDefaultModel() {
        return categorieDefaultModel;
    }
}
