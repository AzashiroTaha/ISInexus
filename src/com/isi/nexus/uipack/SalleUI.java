package com.isi.nexus.uipack;

import com.isi.nexus.model.Salle;
import com.isi.nexus.service.SalleService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class SalleUI extends JPanel {
    private final JTextField txtId = UITheme.field(10);
    private final JTextField txtNom = UITheme.field(16);
    private final JTextField txtCapacite = UITheme.field(8);
    private final JComboBox<String> cboVproj = UITheme.combo("oui", "non");
    private final JTextField txtSearchNom = UITheme.field(16);

    private final DefaultTableModel model = new DefaultTableModel(
            new String[]{"ID", "Nom", "Capacite", "VideoProjecteur"}, 0
    ) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    private final JTable table = new JTable(model);
    private DataChangeListener dataChangeListener;

    public SalleUI() {
        setLayout(new BorderLayout(12, 12));
        setBackground(UITheme.WHITE);

        txtId.setEditable(false);

        JPanel top = UITheme.createPanel();
        top.setLayout(new BorderLayout(8, 8));
        top.add(UITheme.title("Gestion des Salles"), BorderLayout.NORTH);
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
        addRow(form, gbc, y++, "Capacite", txtCapacite);
        addRow(form, gbc, y++, "Video projecteur", cboVproj);
        addRow(form, gbc, y++, "Recherche (nom)", txtSearchNom);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        buttons.setBackground(UITheme.WHITE);

        JButton btnCreate = UITheme.button("Creer");
        JButton btnUpdate = UITheme.button("Modifier");
        JButton btnDelete = UITheme.secondaryButton("Supprimer");
        JButton btnSearch = UITheme.secondaryButton("Rechercher");
        JButton btnRefresh = UITheme.secondaryButton("Actualiser");
        JButton btnClear = UITheme.secondaryButton("Vider");

        btnCreate.addActionListener(e -> createSalle());
        btnUpdate.addActionListener(e -> updateSalle());
        btnDelete.addActionListener(e -> deleteSalle());
        btnSearch.addActionListener(e -> searchSalle());
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

    private void createSalle() {
        try {
            String nom = txtNom.getText().trim();
            int capacite = Integer.parseInt(txtCapacite.getText().trim());
            String vproj = String.valueOf(cboVproj.getSelectedItem());

            SalleService.createSalle(nom, capacite, vproj);
            UITheme.info(this, "Salle creee avec succes.");
            refreshTable();
            clearForm();
            notifyDataChange();
        } catch (NumberFormatException ex) {
            UITheme.error(this, "Capacite invalide.");
        } catch (Exception ex) {
            UITheme.error(this, ex.getMessage());
        }
    }

    private void updateSalle() {
        try {
            int selected = table.getSelectedRow();
            if (selected < 0) {
                UITheme.error(this, "Selectionnez une salle dans le tableau.");
                return;
            }

            String oldNom = String.valueOf(model.getValueAt(selected, 1));
            String newNom = txtNom.getText().trim();
            Integer newCap = txtCapacite.getText().trim().isEmpty() ? null : Integer.parseInt(txtCapacite.getText().trim());
            String newVproj = String.valueOf(cboVproj.getSelectedItem());

            SalleService.updateSalleService(
                    oldNom,
                    emptyToNull(newNom),
                    newCap,
                    emptyToNull(newVproj)
            );

            UITheme.info(this, "Salle modifiee avec succes.");
            refreshTable();
            notifyDataChange();
        } catch (NumberFormatException ex) {
            UITheme.error(this, "Capacite invalide.");
        } catch (Exception ex) {
            UITheme.error(this, ex.getMessage());
        }
    }

    private void deleteSalle() {
        try {
            String nom;
            int selected = table.getSelectedRow();
            if (selected >= 0) {
                nom = String.valueOf(model.getValueAt(selected, 1));
            } else {
                nom = txtSearchNom.getText().trim();
            }

            if (nom.isEmpty()) {
                UITheme.error(this, "Selectionnez une salle ou renseignez le nom de recherche.");
                return;
            }

            SalleService.delSalleService(nom);
            UITheme.info(this, "Salle supprimee avec succes.");
            refreshTable();
            clearForm();
            notifyDataChange();
        } catch (Exception ex) {
            UITheme.error(this, ex.getMessage());
        }
    }

    private void searchSalle() {
        try {
            String nom = txtSearchNom.getText().trim();
            if (nom.isEmpty()) {
                UITheme.error(this, "Renseignez un nom pour la recherche.");
                return;
            }

            Salle salle = SalleService.getSalle(nom);
            model.setRowCount(0);
            if (salle != null) {
                addRow(salle);
            } else {
                UITheme.info(this, "Aucune salle trouvee.");
            }
        } catch (Exception ex) {
            UITheme.error(this, ex.getMessage());
        }
    }

    private void refreshTable() {
        try {
            List<Salle> salles = SalleService.getAllSalle();
            model.setRowCount(0);
            for (Salle salle : salles) {
                addRow(salle);
            }
        } catch (Exception ex) {
            model.setRowCount(0);
        }
    }

    private void addRow(Salle salle) {
        model.addRow(new Object[]{
                salle.getClasse_id(),
                salle.getClasse_name(),
                salle.getCapacity(),
                salle.getVProjector() ? "oui" : "non"
        });
    }

    private void fillFromSelection() {
        int selected = table.getSelectedRow();
        if (selected < 0) {
            return;
        }
        txtId.setText(String.valueOf(model.getValueAt(selected, 0)));
        txtNom.setText(String.valueOf(model.getValueAt(selected, 1)));
        txtCapacite.setText(String.valueOf(model.getValueAt(selected, 2)));
        cboVproj.setSelectedItem(String.valueOf(model.getValueAt(selected, 3)));
    }

    private void clearForm() {
        txtId.setText("");
        txtNom.setText("");
        txtCapacite.setText("");
        txtSearchNom.setText("");
        cboVproj.setSelectedIndex(0);
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
