package com.isi.nexus.uipack;

import com.isi.nexus.model.Enseignant;
import com.isi.nexus.service.EnseignantService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class EnseignantUI extends JPanel {
    private final JTextField txtId = UITheme.field(10);
    private final JTextField txtNom = UITheme.field(16);
    private final JTextField txtPrenom = UITheme.field(16);
    private final JTextField txtGrade = UITheme.field(14);
    private final JTextField txtSpecialite = UITheme.field(20);
    private final JTextField txtSearchId = UITheme.field(10);

    private final DefaultTableModel model = new DefaultTableModel(
            new String[]{"ID", "Nom", "Prenom", "Grade", "Specialite"}, 0
    ) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    private final JTable table = new JTable(model);
    private DataChangeListener dataChangeListener;

    public EnseignantUI() {
        setLayout(new BorderLayout(12, 12));
        setBackground(UITheme.WHITE);

        txtId.setEditable(false);

        JPanel top = UITheme.createPanel();
        top.setLayout(new BorderLayout(8, 8));
        top.add(UITheme.title("Gestion des Enseignants"), BorderLayout.NORTH);
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
        addRow(form, gbc, y++, "ID", txtId);
        addRow(form, gbc, y++, "Nom", txtNom);
        addRow(form, gbc, y++, "Prenom", txtPrenom);
        addRow(form, gbc, y++, "Grade", txtGrade);
        addRow(form, gbc, y++, "Specialite", txtSpecialite);
        addRow(form, gbc, y++, "Recherche (ID)", txtSearchId);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        buttons.setBackground(UITheme.WHITE);

        JButton btnCreate = UITheme.button("Creer");
        JButton btnUpdate = UITheme.button("Modifier");
        JButton btnDelete = UITheme.secondaryButton("Supprimer");
        JButton btnSearch = UITheme.secondaryButton("Rechercher");
        JButton btnRefresh = UITheme.secondaryButton("Actualiser");
        JButton btnClear = UITheme.secondaryButton("Vider");

        btnCreate.addActionListener(e -> createEnseignant());
        btnUpdate.addActionListener(e -> updateEnseignant());
        btnDelete.addActionListener(e -> deleteEnseignant());
        btnSearch.addActionListener(e -> searchEnseignant());
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

    private void createEnseignant() {
        try {
            String nom = txtNom.getText().trim();
            String prenom = txtPrenom.getText().trim();
            String grade = txtGrade.getText().trim();
            String specialite = txtSpecialite.getText().trim();

            EnseignantService.createEnseignantService(nom, prenom, grade, specialite);
            UITheme.info(this, "Enseignant cree avec succes.");
            refreshTable();
            clearForm();
            notifyDataChange();
        } catch (Exception ex) {
            UITheme.error(this, ex.getMessage());
        }
    }

    private void updateEnseignant() {
        try {
            int selected = table.getSelectedRow();
            if (selected < 0) {
                UITheme.error(this, "Selectionnez un enseignant dans le tableau.");
                return;
            }

            int id = Integer.parseInt(String.valueOf(model.getValueAt(selected, 0)));

            EnseignantService.updateEnseignant(
                    id,
                    emptyToNull(txtNom.getText()),
                    emptyToNull(txtPrenom.getText()),
                    emptyToNull(txtGrade.getText()),
                    emptyToNull(txtSpecialite.getText())
            );

            UITheme.info(this, "Enseignant modifie avec succes.");
            refreshTable();
            notifyDataChange();
        } catch (Exception ex) {
            UITheme.error(this, ex.getMessage());
        }
    }

    private void deleteEnseignant() {
        try {
            int id;
            int selected = table.getSelectedRow();
            if (selected >= 0) {
                id = Integer.parseInt(String.valueOf(model.getValueAt(selected, 0)));
            } else if (!txtSearchId.getText().trim().isEmpty()) {
                id = Integer.parseInt(txtSearchId.getText().trim());
            } else {
                UITheme.error(this, "Selectionnez un enseignant ou renseignez l'ID de recherche.");
                return;
            }

            EnseignantService.delEnseignant(id);
            UITheme.info(this, "Enseignant supprime avec succes.");
            refreshTable();
            clearForm();
            notifyDataChange();
        } catch (Exception ex) {
            UITheme.error(this, ex.getMessage());
        }
    }

    private void searchEnseignant() {
        try {
            String raw = txtSearchId.getText().trim();
            if (raw.isEmpty()) {
                UITheme.error(this, "Renseignez un ID pour la recherche.");
                return;
            }
            int id = Integer.parseInt(raw);
            Enseignant en = EnseignantService.getEnseignantByIDService(id);

            model.setRowCount(0);
            if (en != null) {
                addRow(en);
            } else {
                UITheme.info(this, "Aucun enseignant trouve.");
            }
        } catch (NumberFormatException ex) {
            UITheme.error(this, "ID invalide.");
        } catch (Exception ex) {
            UITheme.error(this, ex.getMessage());
        }
    }

    private void refreshTable() {
        try {
            List<Enseignant> enseignants = EnseignantService.getAllEnseignantService();
            model.setRowCount(0);
            for (Enseignant en : enseignants) {
                addRow(en);
            }
        } catch (Exception ex) {
            model.setRowCount(0);
        }
    }

    private void addRow(Enseignant en) {
        model.addRow(new Object[]{
                en.getEn_id(),
                en.getEn_nom(),
                en.getEn_pr(),
                en.getGrade(),
                en.getSpeciality()
        });
    }

    private void fillFromSelection() {
        int selected = table.getSelectedRow();
        if (selected < 0) {
            return;
        }
        txtId.setText(String.valueOf(model.getValueAt(selected, 0)));
        txtNom.setText(String.valueOf(model.getValueAt(selected, 1)));
        txtPrenom.setText(String.valueOf(model.getValueAt(selected, 2)));
        txtGrade.setText(String.valueOf(model.getValueAt(selected, 3)));
        txtSpecialite.setText(String.valueOf(model.getValueAt(selected, 4)));
    }

    private void clearForm() {
        txtId.setText("");
        txtNom.setText("");
        txtPrenom.setText("");
        txtGrade.setText("");
        txtSpecialite.setText("");
        txtSearchId.setText("");
        table.clearSelection();
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
