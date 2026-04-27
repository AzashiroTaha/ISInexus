package com.isi.nexus.uipack;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.JTableHeader;
import java.awt.*;

public final class UITheme {
    public static final Color BLUE_DARK = new Color(19, 65, 135);
    public static final Color BLUE_PRIMARY = new Color(32, 97, 181);
    public static final Color BLUE_LIGHT = new Color(225, 238, 255);
    public static final Color WHITE = Color.WHITE;
    public static final Color TEXT = new Color(26, 32, 44);

    private UITheme() {
    }

    public static JPanel createPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(WHITE);
        panel.setBorder(new EmptyBorder(14, 14, 14, 14));
        return panel;
    }

    public static JLabel title(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(BLUE_DARK);
        label.setFont(new Font("Segoe UI", Font.BOLD, 22));
        return label;
    }

    public static JButton button(String text) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setBackground(BLUE_PRIMARY);
        button.setForeground(WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return button;
    }

    public static JButton secondaryButton(String text) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setBackground(BLUE_LIGHT);
        button.setForeground(BLUE_DARK);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return button;
    }

    public static JTextField field(int columns) {
        JTextField field = new JTextField(columns);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        return field;
    }

    public static JComboBox<String> combo(String... values) {
        JComboBox<String> combo = new JComboBox<>(values);
        combo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        return combo;
    }

    public static void styleTable(JTable table) {
        table.setRowHeight(24);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.setSelectionBackground(BLUE_LIGHT);
        table.setSelectionForeground(TEXT);
        table.setGridColor(new Color(220, 228, 240));
        JTableHeader header = table.getTableHeader();
        header.setBackground(BLUE_PRIMARY);
        header.setForeground(WHITE);
        header.setFont(new Font("Segoe UI", Font.BOLD, 12));
    }

    public static void info(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "Information", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void error(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "Erreur", JOptionPane.ERROR_MESSAGE);
    }
}
