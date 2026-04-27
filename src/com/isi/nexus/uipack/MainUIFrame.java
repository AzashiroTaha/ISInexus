package com.isi.nexus.uipack;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class MainUIFrame extends JFrame {
    private final EtudiantUI etudiantUI = new EtudiantUI();
    private final EnseignantUI enseignantUI = new EnseignantUI();
    private final SalleUI salleUI = new SalleUI();
    private final SoutenanceUI soutenanceUI = new SoutenanceUI();

    public MainUIFrame() {
        setTitle("ISI NEXUS - Gestion des Soutenances");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(1300, 760);
        setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(UITheme.WHITE);

        JPanel side = buildSidePanel();
        JPanel center = buildCenterPanel();

        etudiantUI.setDataChangeListener(() -> soutenanceUI.refreshData());
        enseignantUI.setDataChangeListener(() -> soutenanceUI.refreshData());
        salleUI.setDataChangeListener(() -> soutenanceUI.refreshData());

        root.add(side, BorderLayout.WEST);
        root.add(center, BorderLayout.CENTER);

        setContentPane(root);
    }

    private JPanel buildSidePanel() {
        JPanel side = new JPanel(new BorderLayout());
        side.setBackground(UITheme.BLUE_DARK);
        side.setBorder(new EmptyBorder(18, 14, 18, 14));
        side.setPreferredSize(new Dimension(260, 760));

        JPanel top = new JPanel();
        top.setOpaque(false);
        top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));

        JLabel project = new JLabel("ISI NEXUS");
        project.setForeground(UITheme.WHITE);
        project.setFont(new Font("Segoe UI", Font.BOLD, 26));
        project.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitle = new JLabel("Gestion simple");
        subtitle.setForeground(new Color(207, 226, 255));
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        top.add(project);
        top.add(Box.createVerticalStrut(4));
        top.add(subtitle);

        JPanel menu = new JPanel();
        menu.setOpaque(false);
        menu.setLayout(new BoxLayout(menu, BoxLayout.Y_AXIS));

        JButton btnEtudiants = navButton("Etudiants");
        JButton btnEnseignants = navButton("Enseignants");
        JButton btnSalles = navButton("Salles");
        JButton btnSoutenances = navButton("Soutenances");

        btnEtudiants.addActionListener(e -> switchPanel("etudiants"));
        btnEnseignants.addActionListener(e -> switchPanel("enseignants"));
        btnSalles.addActionListener(e -> switchPanel("salles"));
        btnSoutenances.addActionListener(e -> switchPanel("soutenances"));

        menu.add(Box.createVerticalStrut(14));
        menu.add(btnEtudiants);
        menu.add(Box.createVerticalStrut(10));
        menu.add(btnEnseignants);
        menu.add(Box.createVerticalStrut(10));
        menu.add(btnSalles);
        menu.add(Box.createVerticalStrut(10));
        menu.add(btnSoutenances);

        side.add(top, BorderLayout.NORTH);
        side.add(menu, BorderLayout.CENTER);
        return side;
    }

    private final CardLayout cards = new CardLayout();
    private final JPanel cardPanel = new JPanel(cards);

    private JPanel buildCenterPanel() {
        JPanel center = new JPanel(new BorderLayout());
        center.setBackground(UITheme.WHITE);
        center.setBorder(new EmptyBorder(12, 12, 12, 12));

        cardPanel.setBackground(UITheme.WHITE);
        cardPanel.add(etudiantUI, "etudiants");
        cardPanel.add(enseignantUI, "enseignants");
        cardPanel.add(salleUI, "salles");
        cardPanel.add(soutenanceUI, "soutenances");

        JPanel topInfo = new JPanel(new BorderLayout());
        topInfo.setBackground(UITheme.BLUE_LIGHT);
        topInfo.setBorder(new EmptyBorder(10, 12, 10, 12));

        JLabel info = new JLabel("Choisissez un module a gauche pour travailler plus simplement.");
        info.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        info.setForeground(UITheme.BLUE_DARK);
        topInfo.add(info, BorderLayout.WEST);

        center.add(topInfo, BorderLayout.NORTH);
        center.add(cardPanel, BorderLayout.CENTER);
        cards.show(cardPanel, "etudiants");
        return center;
    }

    private JButton navButton(String text) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        button.setBackground(new Color(33, 93, 167));
        button.setForeground(UITheme.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return button;
    }

    private void switchPanel(String key) {
        cards.show(cardPanel, key);
    }
}
