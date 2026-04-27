package com.isi.nexus.uipack;

import com.isi.nexus.model.Enseignant;
import com.isi.nexus.model.Etudiant;
import com.isi.nexus.model.Salle;
import com.isi.nexus.model.Soutenance;
import com.isi.nexus.service.EnseignantService;
import com.isi.nexus.service.EtudiantService;
import com.isi.nexus.service.SalleService;
import com.isi.nexus.service.SoutenanceService;
import com.isi.nexus.util.funcs;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class SoutenanceUI extends JPanel {
    private final JTextField txtId = UITheme.field(8);
    private final JTextField txtDate = UITheme.field(10);
    private final JTextField txtHeureDebut = UITheme.field(6);
    private final JTextField txtHeureFin = UITheme.field(6);
    private final JTextField txtSearchId = UITheme.field(8);

    private final JComboBox<Salle> cboSalle = new JComboBox<>();
    private final JComboBox<Etudiant> cboEtudiant = new JComboBox<>();
    private final JComboBox<Enseignant> cboPresident = new JComboBox<>();
    private final JComboBox<Enseignant> cboExaminateur = new JComboBox<>();
    private final JComboBox<Enseignant> cboEncadreur = new JComboBox<>();

    private final DefaultTableModel model = new DefaultTableModel(
            new String[]{"ID", "Date", "Debut", "Fin", "Salle", "Etudiant", "President", "Examinateur", "Encadreur"}, 0
    ) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    private final JTable table = new JTable(model);

    public SoutenanceUI() {
        setLayout(new BorderLayout(12, 12));
        setBackground(UITheme.WHITE);

        txtId.setEditable(false);

        styleCombo(cboSalle);
        styleCombo(cboEtudiant);
        styleCombo(cboPresident);
        styleCombo(cboExaminateur);
        styleCombo(cboEncadreur);

        cboSalle.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Object display = value instanceof Salle ? ((Salle) value).getClasse_name() : "-";
                return super.getListCellRendererComponent(list, display, index, isSelected, cellHasFocus);
            }
        });

        cboEtudiant.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Object display;
                if (value instanceof Etudiant) {
                    Etudiant etu = (Etudiant) value;
                    display = etu.getMatricule() + " - " + etu.getEtuName() + " " + etu.getEtuNicknm();
                } else {
                    display = "-";
                }
                return super.getListCellRendererComponent(list, display, index, isSelected, cellHasFocus);
            }
        });

        DefaultListCellRenderer enseignantRenderer = new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Object display;
                if (value instanceof Enseignant) {
                    Enseignant en = (Enseignant) value;
                    display = en.getEn_nom() + " " + en.getEn_pr() + " (" + en.getEn_id() + ")";
                } else {
                    display = "-";
                }
                return super.getListCellRendererComponent(list, display, index, isSelected, cellHasFocus);
            }
        };

        cboPresident.setRenderer(enseignantRenderer);
        cboExaminateur.setRenderer(enseignantRenderer);
        cboEncadreur.setRenderer(enseignantRenderer);

        JPanel top = UITheme.createPanel();
        top.setLayout(new BorderLayout(8, 8));
        top.add(UITheme.title("Gestion des Soutenances"), BorderLayout.NORTH);
        top.add(buildFormPanel(), BorderLayout.CENTER);

        UITheme.styleTable(table);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getSelectionModel().addListSelectionListener(e -> fillFromSelection());

        add(top, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);

        refreshAll();
    }

    public void refreshData() {
        refreshAll();
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
        addRow(form, gbc, y++, "Date (dd/MM/yyyy)", txtDate);
        addRow(form, gbc, y++, "Heure debut (HH:mm)", txtHeureDebut);
        addRow(form, gbc, y++, "Heure fin (HH:mm)", txtHeureFin);
        addRow(form, gbc, y++, "Salle", cboSalle);
        addRow(form, gbc, y++, "Etudiant", cboEtudiant);
        addRow(form, gbc, y++, "President", cboPresident);
        addRow(form, gbc, y++, "Examinateur", cboExaminateur);
        addRow(form, gbc, y++, "Encadreur", cboEncadreur);
        addRow(form, gbc, y++, "Recherche (ID)", txtSearchId);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        buttons.setBackground(UITheme.WHITE);

        JButton btnCreate = UITheme.button("Creer");
        JButton btnUpdate = UITheme.button("Modifier");
        JButton btnDelete = UITheme.secondaryButton("Supprimer");
        JButton btnSearch = UITheme.secondaryButton("Rechercher");
        JButton btnRefresh = UITheme.secondaryButton("Actualiser");
        JButton btnClear = UITheme.secondaryButton("Vider");

        btnCreate.addActionListener(e -> createSoutenance());
        btnUpdate.addActionListener(e -> updateSoutenance());
        btnDelete.addActionListener(e -> deleteSoutenance());
        btnSearch.addActionListener(e -> searchSoutenance());
        btnRefresh.addActionListener(e -> refreshAll());
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

    private void createSoutenance() {
        try {
            LocalDate date = funcs.dateFR(txtDate.getText().trim());
            LocalTime start = funcs.heureFR(txtHeureDebut.getText().trim());
            LocalTime end = funcs.heureFR(txtHeureFin.getText().trim());

            SoutenanceService.createSoutenanceService(
                    date,
                    (Salle) cboSalle.getSelectedItem(),
                    (Etudiant) cboEtudiant.getSelectedItem(),
                    (Enseignant) cboPresident.getSelectedItem(),
                    (Enseignant) cboExaminateur.getSelectedItem(),
                    (Enseignant) cboEncadreur.getSelectedItem(),
                    start,
                    end
            );

            UITheme.info(this, "Soutenance creee avec succes.");
            refreshAll();
            clearForm();
        } catch (Exception ex) {
            UITheme.error(this, ex.getMessage());
        }
    }

    private void updateSoutenance() {
        try {
            int id;
            if (!txtId.getText().trim().isEmpty()) {
                id = Integer.parseInt(txtId.getText().trim());
            } else {
                int selected = table.getSelectedRow();
                if (selected < 0) {
                    UITheme.error(this, "Selectionnez une soutenance a modifier.");
                    return;
                }
                id = Integer.parseInt(String.valueOf(model.getValueAt(selected, 0)));
            }

            LocalDate date = funcs.dateFR(txtDate.getText().trim());
            LocalTime start = funcs.heureFR(txtHeureDebut.getText().trim());
            LocalTime end = funcs.heureFR(txtHeureFin.getText().trim());

            SoutenanceService.updateSoutenanceService(
                    id,
                    date,
                    (Salle) cboSalle.getSelectedItem(),
                    (Etudiant) cboEtudiant.getSelectedItem(),
                    (Enseignant) cboPresident.getSelectedItem(),
                    (Enseignant) cboExaminateur.getSelectedItem(),
                    (Enseignant) cboEncadreur.getSelectedItem(),
                    start,
                    end
            );

            UITheme.info(this, "Soutenance modifiee avec succes.");
            refreshAll();
        } catch (Exception ex) {
            UITheme.error(this, ex.getMessage());
        }
    }

    private void deleteSoutenance() {
        try {
            int id;
            if (!txtId.getText().trim().isEmpty()) {
                id = Integer.parseInt(txtId.getText().trim());
            } else if (!txtSearchId.getText().trim().isEmpty()) {
                id = Integer.parseInt(txtSearchId.getText().trim());
            } else {
                int selected = table.getSelectedRow();
                if (selected < 0) {
                    UITheme.error(this, "Selectionnez une soutenance ou renseignez l'ID de recherche.");
                    return;
                }
                id = Integer.parseInt(String.valueOf(model.getValueAt(selected, 0)));
            }

            SoutenanceService.delSoutenance(id);
            UITheme.info(this, "Soutenance supprimee avec succes.");
            refreshAll();
            clearForm();
        } catch (Exception ex) {
            UITheme.error(this, ex.getMessage());
        }
    }

    private void searchSoutenance() {
        try {
            String raw = txtSearchId.getText().trim();
            if (raw.isEmpty()) {
                UITheme.error(this, "Renseignez un ID pour la recherche.");
                return;
            }

            int id = Integer.parseInt(raw);
            Soutenance soutenance = SoutenanceService.getSoutenance(id);
            model.setRowCount(0);
            if (soutenance != null) {
                addRow(soutenance);
                selectValues(soutenance);
            } else {
                UITheme.info(this, "Aucune soutenance trouvee.");
            }
        } catch (NumberFormatException ex) {
            UITheme.error(this, "ID invalide.");
        } catch (Exception ex) {
            UITheme.error(this, ex.getMessage());
        }
    }

    private void refreshAll() {
        refreshCombos();
        refreshTable();
    }

    private void refreshCombos() {
        Salle selectedSalle = (Salle) cboSalle.getSelectedItem();
        Etudiant selectedEtudiant = (Etudiant) cboEtudiant.getSelectedItem();
        Enseignant selectedPresident = (Enseignant) cboPresident.getSelectedItem();
        Enseignant selectedExaminateur = (Enseignant) cboExaminateur.getSelectedItem();
        Enseignant selectedEncadreur = (Enseignant) cboEncadreur.getSelectedItem();

        cboSalle.removeAllItems();
        cboEtudiant.removeAllItems();
        cboPresident.removeAllItems();
        cboExaminateur.removeAllItems();
        cboEncadreur.removeAllItems();

        try {
            List<Salle> salles = SalleService.getAllSalle();
            for (Salle salle : salles) {
                cboSalle.addItem(salle);
            }
        } catch (Exception ignored) {
        }

        try {
            List<Etudiant> etudiants = EtudiantService.getAllEtuService();
            for (Etudiant etu : etudiants) {
                cboEtudiant.addItem(etu);
            }
        } catch (Exception ignored) {
        }

        try {
            List<Enseignant> enseignants = EnseignantService.getAllEnseignantService();
            for (Enseignant en : enseignants) {
                cboPresident.addItem(en);
                cboExaminateur.addItem(en);
                cboEncadreur.addItem(en);
            }
        } catch (Exception ignored) {
        }

        reselectSalle(selectedSalle);
        reselectEtudiant(selectedEtudiant);
        reselectEnseignant(cboPresident, selectedPresident);
        reselectEnseignant(cboExaminateur, selectedExaminateur);
        reselectEnseignant(cboEncadreur, selectedEncadreur);
    }

    private void refreshTable() {
        try {
            List<Soutenance> soutenances = SoutenanceService.getAllSoutenanceService();
            model.setRowCount(0);
            for (Soutenance sout : soutenances) {
                addRow(sout);
            }
        } catch (Exception ex) {
            model.setRowCount(0);
        }
    }

    private void addRow(Soutenance sout) {
        model.addRow(new Object[]{
                sout.getSoutenance_id(),
                sout.getSoutenance_date(),
                sout.getHeure_start(),
                sout.getHeure_end(),
                sout.getSoutenance_salle() == null ? "-" : sout.getSoutenance_salle().getClasse_name(),
                sout.getSoutenance_etudiant() == null ? "-" : sout.getSoutenance_etudiant().getMatricule(),
                sout.getSoutenance_president() == null ? "-" : sout.getSoutenance_president().getEn_nom(),
                sout.getsoutenance_examinateur() == null ? "-" : sout.getsoutenance_examinateur().getEn_nom(),
                sout.getSoutenance_encadreur() == null ? "-" : sout.getSoutenance_encadreur().getEn_nom()
        });
    }

    private void fillFromSelection() {
        int selected = table.getSelectedRow();
        if (selected < 0) {
            return;
        }

        try {
            int id = Integer.parseInt(String.valueOf(model.getValueAt(selected, 0)));
            Soutenance soutenance = SoutenanceService.getSoutenance(id);
            if (soutenance != null) {
                selectValues(soutenance);
            }
        } catch (Exception ex) {
            UITheme.error(this, ex.getMessage());
        }
    }

    private void selectValues(Soutenance soutenance) {
        txtId.setText(String.valueOf(soutenance.getSoutenance_id()));
        txtDate.setText(formatDate(soutenance.getSoutenance_date()));
        txtHeureDebut.setText(String.valueOf(soutenance.getHeure_start()));
        txtHeureFin.setText(String.valueOf(soutenance.getHeure_end()));

        reselectSalle(soutenance.getSoutenance_salle());
        reselectEtudiant(soutenance.getSoutenance_etudiant());
        reselectEnseignant(cboPresident, soutenance.getSoutenance_president());
        reselectEnseignant(cboExaminateur, soutenance.getsoutenance_examinateur());
        reselectEnseignant(cboEncadreur, soutenance.getSoutenance_encadreur());
    }

    private void reselectSalle(Salle selectedSalle) {
        if (selectedSalle == null) {
            return;
        }
        for (int i = 0; i < cboSalle.getItemCount(); i++) {
            Salle item = cboSalle.getItemAt(i);
            if (item != null && item.getClasse_id() == selectedSalle.getClasse_id()) {
                cboSalle.setSelectedIndex(i);
                return;
            }
        }
    }

    private void reselectEtudiant(Etudiant selectedEtudiant) {
        if (selectedEtudiant == null) {
            return;
        }
        for (int i = 0; i < cboEtudiant.getItemCount(); i++) {
            Etudiant item = cboEtudiant.getItemAt(i);
            if (item != null && item.getIdEtudiant() == selectedEtudiant.getIdEtudiant()) {
                cboEtudiant.setSelectedIndex(i);
                return;
            }
        }
    }

    private void reselectEnseignant(JComboBox<Enseignant> comboBox, Enseignant selectedEnseignant) {
        if (selectedEnseignant == null) {
            return;
        }
        for (int i = 0; i < comboBox.getItemCount(); i++) {
            Enseignant item = comboBox.getItemAt(i);
            if (item != null && item.getEn_id() == selectedEnseignant.getEn_id()) {
                comboBox.setSelectedIndex(i);
                return;
            }
        }
    }

    private void clearForm() {
        txtId.setText("");
        txtDate.setText("");
        txtHeureDebut.setText("");
        txtHeureFin.setText("");
        txtSearchId.setText("");
        table.clearSelection();
    }

    private void styleCombo(JComboBox<?> comboBox) {
        comboBox.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        comboBox.setBackground(Color.WHITE);
    }

    private String formatDate(LocalDate date) {
        if (date == null) {
            return "";
        }
        String day = String.format("%02d", date.getDayOfMonth());
        String month = String.format("%02d", date.getMonthValue());
        return day + "/" + month + "/" + date.getYear();
    }
}
