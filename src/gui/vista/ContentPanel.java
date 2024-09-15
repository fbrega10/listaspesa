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
