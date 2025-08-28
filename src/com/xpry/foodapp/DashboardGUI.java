package com.xpry.foodapp;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import com.formdev.flatlaf.FlatLightLaf;
import org.jdatepicker.impl.*;

import java.awt.*;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.List;
import java.util.Properties;




import javax.swing.table.JTableHeader;





public class DashboardGUI extends JFrame {

    private final FoodDAO dao = new FoodDAO();
    private final DefaultTableModel tableModel = new DefaultTableModel(new String[]{"ID", "Food Name", "Expiry Date", "Status"}, 0);
    private final JTable table = new JTable(tableModel);
    private final TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
    private final JLabel totalLabel = new JLabel();
    private final JLabel expiredLabel = new JLabel();
    private final JLabel soonLabel = new JLabel();

    public DashboardGUI() {
        FlatLightLaf.setup();
        setTitle("Xpry - Food Expiry Dashboard");
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        setupSidebar();
        setupHeader();
        setupTableAndSearch();

        refreshTable();
        setVisible(true);
    }

    // ---------------- Sidebar ----------------

    private void setupSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(new Color(35, 35, 55));
        sidebar.setPreferredSize(new Dimension(180, getHeight()));
        sidebar.setBorder(new EmptyBorder(20, 10, 20, 10));

        sidebar.add(Box.createVerticalGlue());
        sidebar.add(makeSidebarButton("Dashboard", e -> refreshTable()));
        sidebar.add(Box.createVerticalStrut(10));
        sidebar.add(makeSidebarButton("Add Item", e -> showAddDialog()));
        sidebar.add(Box.createVerticalStrut(10));
        sidebar.add(makeSidebarButton("Delete Selected", e -> deleteSelectedItem()));
        sidebar.add(Box.createVerticalStrut(10));
        sidebar.add(makeSidebarButton("Show Expired", e -> showFiltered(true)));
        sidebar.add(Box.createVerticalStrut(10));
        sidebar.add(makeSidebarButton("Show All", e -> refreshTable()));
        sidebar.add(Box.createVerticalGlue());

        add(sidebar, BorderLayout.WEST);
    }

    private JButton makeSidebarButton(String name, ActionListener listener) {
        JButton btn = new JButton(name);
        btn.setFocusPainted(false);
        btn.setForeground(Color.WHITE);
        btn.setBackground(new Color(60, 60, 90));
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btn.setBorder(new EmptyBorder(10, 20, 10, 20));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(160, 40));
        btn.addActionListener(listener);
        return btn;
    }

    // ---------------- Header Cards ----------------

    private void setupHeader() {
        JPanel header = new JPanel(new GridLayout(1, 3, 15, 0));
        
        header.setBorder(new EmptyBorder(15, 15, 10, 15));

        header.add(makeCard("Total Items", totalLabel));
        header.add(makeCard("Expired", expiredLabel));
        header.add(makeCard("Expiring Soon", soonLabel));
        header.setBackground(new Color(35, 35, 55));
        
        add(header, BorderLayout.NORTH);
    }

    private JPanel makeCard(String title, JLabel valueLabel) {
        JPanel card = new JPanel(new BorderLayout());
        
        card.setBackground(new Color(60, 60, 90));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(210, 210, 210)),
                new EmptyBorder(10, 10, 10, 10)));
        card.setPreferredSize(new Dimension(200, 60));
        card.setMaximumSize(new Dimension(200, 60));

        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        titleLabel.setForeground(Color.WHITE);

        valueLabel.setHorizontalAlignment(SwingConstants.CENTER);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        valueLabel.setForeground(Color.RED);

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        return card;
    }

    // ---------------- Table & Search ----------------

    private void setupTableAndSearch() {
        table.setRowHeight(28);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.setGridColor(new Color(60, 60, 60));
        table.setShowVerticalLines(false);
        table.setSelectionBackground(new Color(100, 149, 237));
        table.setRowSorter(sorter);
        table.setBackground(new Color(60, 60, 90));
        table.setForeground(Color.WHITE); 

        
        
        
        
        
        
     // Table Header Customization
        JTableHeader header = table.getTableHeader();
        header.setBackground(new Color(35, 35, 55));  // Dark background
        header.setForeground(Color.WHITE);            // White text
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        
        header.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));

        
        
        
        
        
        // Search UI
        JLabel searchLabel = new JLabel("Search:");
        searchLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        searchLabel.setForeground(Color.WHITE);

        JTextField searchField = new JTextField(20);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { filter(searchField.getText()); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { filter(searchField.getText()); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { filter(searchField.getText()); }
        });
        searchField.setBackground(new Color(60, 60, 90));
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        searchPanel.setBackground(Color.WHITE);
        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        searchPanel.setBackground(new Color(60, 60, 90));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(new EmptyBorder(10, 15, 15, 15));

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(searchPanel, BorderLayout.NORTH);
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        centerPanel.setBackground(new Color(35, 35, 55));

        add(centerPanel, BorderLayout.CENTER);
    }

    private void filter(String text) {
        sorter.setRowFilter(text.trim().isEmpty() ? null : RowFilter.regexFilter("(?i)" + text));
    }

    // ---------------- CRUD Operations ----------------

    private void refreshTable() {
        List<FoodItem> items = dao.getAllItems();
        updateDashboard(items);
        fillTable(items);
    }

    private void showFiltered(boolean expiredOnly) {
        List<FoodItem> filtered = dao.getAllItems().stream()
                .filter(f -> f.isExpired() == expiredOnly)
                .toList();
        fillTable(filtered);
    }

    private void fillTable(List<FoodItem> items) {
        tableModel.setRowCount(0);
        for (FoodItem item : items) {
            String status = item.isExpired() ? "❌ Expired" : "✅ Fresh";
            tableModel.addRow(new Object[]{item.getId(), item.getName(), item.getExpiryDate(), status});
        }
    }

    private void updateDashboard(List<FoodItem> items) {
        long total = items.size();
        long expired = items.stream().filter(FoodItem::isExpired).count();
        long soon = items.stream()
                .filter(i -> !i.isExpired() && i.getExpiryDate().isBefore(LocalDate.now().plusDays(3)))
                .count();

        totalLabel.setText(String.valueOf(total));
        expiredLabel.setText(String.valueOf(expired));
        soonLabel.setText(String.valueOf(soon));
    }

    private void showAddDialog() {
        JTextField nameField = new JTextField();

        UtilDateModel model = new UtilDateModel();
        Properties p = new Properties();
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");

        JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
        JDatePickerImpl datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());

        JPanel panel = new JPanel(new GridLayout(2, 2, 5, 5));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        panel.add(new JLabel("Food Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Expiry Date:"));
        panel.add(datePicker);
       

        int result = JOptionPane.showConfirmDialog(this, panel, "Add New Food", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                String name = nameField.getText();
                java.util.Date selectedDate = (java.util.Date) datePicker.getModel().getValue();

                if (selectedDate != null) {
                    LocalDate expiryDate = new java.sql.Date(selectedDate.getTime()).toLocalDate();
                    dao.addItem(name, expiryDate);
                    refreshTable();
                } else {
                    JOptionPane.showMessageDialog(this, "Please select a valid date.");
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Invalid input.");
            }
        }
    }

    private void deleteSelectedItem() {
        int row = table.getSelectedRow();
        if (row != -1) {
            int id = (int) tableModel.getValueAt(row, 0);
            dao.deleteItem(id);
            refreshTable();
        } else {
            JOptionPane.showMessageDialog(this, "Select an item to delete.");
        }
    }
}
