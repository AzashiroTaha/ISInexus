package com.isi.nexus.uipack;

import com.isi.nexus.model.Etudiant;
import com.isi.nexus.service.EtudiantService;
import com.isi.nexus.util.funcs;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class EtudiantUI extends JPanel {
    private final JTextField txtNom = UITheme.field(18);
    private final JTextField txtPrenom = UITheme.field(18);
    private final JTextField txtTheme = UITheme.field(28);
    private final JTextField txtSearchMat = UITheme.field(18);

    private final DefaultTableModel model = new DefaultTableModel(
            new String[]{"ID", "Matricule", "Nom", "Prenom", "Theme"}, 0
    ) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    private final JTable table = new JTable(model);
    private DataChangeListener dataChangeListener;

    public EtudiantUI() {
        setLayout(new BorderLayout(12, 12));
        setBackground(UITheme.WHITE);

        JPanel top = UITheme.createPanel();
        top.setLayout(new BorderLayout(8, 8));
        top.add(UITheme.title("Gestion des Etudiants"), BorderLayout.NORTH);
        top.add(buildFormPanel(), BorderLayout.CENTER);

        UITheme.styleTable(table);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getSelectionModel().addListSelectionListener(e -> fillFromSelection());

        add(top, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);

        refreshTable();
    }

    public void setDataChangeListener(DataChangeListener dataChangeListener) {
        this.dataChangeListener = dataChangeListener;
    }

    public void refreshData() {
        refreshTable();
    }

    private JPanel buildFormPanel() {
        JPanel wrapper = new JPanel(new BorderLayout(8, 8));
        wrapper.setBackground(UITheme.WHITE);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(UITheme.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int y = 0;
        addRow(form, gbc, y++, "Nom", txtNom);
        addRow(form, gbc, y++, "Prenom", txtPrenom);
        addRow(form, gbc, y++, "Theme", txtTheme);
        addRow(form, gbc, y++, "Recherche (matricule)", txtSearchMat);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        buttons.setBackground(UITheme.WHITE);

        JButton btnCreate = UITheme.button("Creer");
        JButton btnUpdate = UITheme.button("Modifier");
        JButton btnDelete = UITheme.secondaryButton("Supprimer");
        JButton btnSearch = UITheme.secondaryButton("Rechercher");
        JButton btnRefresh = UITheme.secondaryButton("Actualiser");
        JButton btnClear = UITheme.secondaryButton("Vider");

        btnCreate.addActionListener(e -> createEtudiant());
        btnUpdate.addActionListener(e -> updateEtudiant());
        btnDelete.addActionListener(e -> deleteEtudiant());
        btnSearch.addActionListener(e -> searchEtudiant());
        btnRefresh.addActionListener(e -> refreshTable());
        btnClear.addActionListener(e -> clearForm());

        buttons.add(btnCreate);
        buttons.add(btnUpdate);
        buttons.add(btnDelete);
        buttons.add(btnSearch);
        buttons.add(btnRefresh);
        buttons.add(btnClear);

        wrapper.add(form, BorderLayout.CENTER);
        wrapper.add(buttons, BorderLayout.SOUTH);
        return wrapper;
    }

    private void addRow(JPanel panel, GridBagConstraints gbc, int y, String label, JComponent field) {
        gbc.gridx = 0;
        gbc.gridy = y;
        gbc.weightx = 0.0;
        panel.add(new JLabel(label + " :"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        panel.add(field, gbc);
    }

    private void createEtudiant() {
        try {
            String mat = generateUniqueMatricule();
            String nom = txtNom.getText().trim();
            String prenom = txtPrenom.getText().trim();
            String theme = txtTheme.getText().trim();

            EtudiantService.createEtudiantService(mat, prenom, nom, theme);
            UITheme.info(this, "Etudiant cree avec succes.");
            refreshTable();
            clearForm();
            notifyDataChange();
        } catch (Exception ex) {
            UITheme.error(this, ex.getMessage());
        }
    }

    private void updateEtudiant() {
        try {
            int selected = table.getSelectedRow();
            if (selected < 0) {
                UITheme.error(this, "Selectionnez un etudiant dans le tableau.");
                return;
            }

            String oldMat = String.valueOf(model.getValueAt(selected, 1));
            String nom = txtNom.getText().trim();
            String prenom = txtPrenom.getText().trim();
            String theme = txtTheme.getText().trim();

            EtudiantService.updateEtudiantService(
                    oldMat,
                    null,
                    emptyToNull(nom),
                    emptyToNull(prenom),
                    emptyToNull(theme)
            );

            UITheme.info(this, "Etudiant modifie avec succes.");
            refreshTable();
            notifyDataChange();
        } catch (Exception ex) {
            UITheme.error(this, ex.getMessage());
        }
    }

    private void deleteEtudiant() {
        try {
            int selected = table.getSelectedRow();
            String mat;
            if (selected >= 0) {
                mat = String.valueOf(model.getValueAt(selected, 1));
            } else {
                mat = txtSearchMat.getText().trim();
            }

            if (mat.isEmpty()) {
                UITheme.error(this, "Selectionnez un etudiant ou renseignez sa matricule de recherche.");
                return;
            }

            EtudiantService.delEtuByMatService(mat);
            UITheme.info(this, "Etudiant supprime avec succes.");
            refreshTable();
            clearForm();
            notifyDataChange();
        } catch (Exception ex) {
            UITheme.error(this, ex.getMessage());
        }
    }

    private void searchEtudiant() {
        try {
            String mat = txtSearchMat.getText().trim();
            if (mat.isEmpty()) {
                UITheme.error(this, "Renseignez une matricule pour la recherche.");
                return;
            }

            Etudiant etu = EtudiantService.getEtudiantService(mat);
            model.setRowCount(0);
            if (etu != null) {
                addRow(etu);
            } else {
                UITheme.info(this, "Aucun etudiant trouve.");
            }
        } catch (Exception ex) {
            UITheme.error(this, ex.getMessage());
        }
    }

    private void refreshTable() {
        try {
            List<Etudiant> etudiants = EtudiantService.getAllEtuService();
            model.setRowCount(0);
            for (Etudiant etu : etudiants) {
                addRow(etu);
            }
        } catch (Exception ex) {
            model.setRowCount(0);
        }
    }

    private void addRow(Etudiant etu) {
        model.addRow(new Object[]{
                etu.getIdEtudiant(),
                etu.getMatricule(),
                etu.getEtuName(),
                etu.getEtuNicknm(),
                etu.getTheme()
        });
    }

    private void fillFromSelection() {
        int selected = table.getSelectedRow();
        if (selected < 0) {
            return;
        }

        txtNom.setText(String.valueOf(model.getValueAt(selected, 2)));
        txtPrenom.setText(String.valueOf(model.getValueAt(selected, 3)));
        txtTheme.setText(String.valueOf(model.getValueAt(selected, 4)));
    }

    private void clearForm() {
        txtNom.setText("");
        txtPrenom.setText("");
        txtTheme.setText("");
        txtSearchMat.setText("");
        table.clearSelection();
    }

    private String generateUniqueMatricule() throws Exception {
        String mat = funcs.etu_mat();
        while (EtudiantService.getEtudiantService(mat) != null) {
            mat = funcs.etu_mat();
        }
        return mat;
    }

    private String emptyToNull(String value) {
        return value == null || value.trim().isEmpty() ? null : value.trim();
    }

    private void notifyDataChange() {
        if (dataChangeListener != null) {
            dataChangeListener.onDataChanged();
        }
    }
}
