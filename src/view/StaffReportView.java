package view;

import controller.CategoryController;
import controller.ReportController;
import dto.ReportFilter;
import dto.ReportRow;
import model.Category;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 * Inventory Report view accessible to both Staff and Manager.
 * Allows filtering by date range (product added date), product SKU/name, and category.
 */
public class StaffReportView extends JFrame {

    private final ReportController reportController;
    private final CategoryController categoryController;
    private final Runnable onBack;

    private JTextField txtDateFrom;
    private JTextField txtDateTo;
    private JTextField txtKeyword;
    private JComboBox<CategoryItem> cmbCategory;
    private JButton btnGenerate;
    private JButton btnClear;
    private JButton btnBack;
    private JTable tblReport;
    private DefaultTableModel tableModel;
    private JLabel lblSummary;

    public StaffReportView(ReportController reportController,
            CategoryController categoryController,
            Runnable onBack) {
        this.reportController = reportController;
        this.categoryController = categoryController;
        this.onBack = onBack;

        buildUI();
        loadCategories();

        setTitle("Inventory Report");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setMinimumSize(new Dimension(900, 520));
        pack();
        setLocationRelativeTo(null);
    }

    private void buildUI() {
        // ── Filter panel ────────────────────────────────────────────────────
        JPanel filterPanel = new JPanel(new GridBagLayout());
        filterPanel.setBorder(BorderFactory.createTitledBorder("Filters"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 8, 5, 8);
        gbc.anchor = GridBagConstraints.WEST;

        // Row 0 – date range
        gbc.gridx = 0; gbc.gridy = 0;
        filterPanel.add(new JLabel("Date Added From (YYYY-MM-DD):"), gbc);
        gbc.gridx = 1;
        txtDateFrom = new JTextField(12);
        filterPanel.add(txtDateFrom, gbc);

        gbc.gridx = 2;
        filterPanel.add(new JLabel("Date Added To (YYYY-MM-DD):"), gbc);
        gbc.gridx = 3;
        txtDateTo = new JTextField(12);
        filterPanel.add(txtDateTo, gbc);

        // Row 1 – product keyword and category
        gbc.gridx = 0; gbc.gridy = 1;
        filterPanel.add(new JLabel("Product (SKU / Name):"), gbc);
        gbc.gridx = 1;
        txtKeyword = new JTextField(12);
        filterPanel.add(txtKeyword, gbc);

        gbc.gridx = 2;
        filterPanel.add(new JLabel("Category:"), gbc);
        gbc.gridx = 3;
        cmbCategory = new JComboBox<>();
        cmbCategory.setPreferredSize(new Dimension(160, 24));
        filterPanel.add(cmbCategory, gbc);

        // Row 2 – action buttons
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        btnGenerate = new JButton("Generate Report");
        btnGenerate.addActionListener(e -> onGenerate());
        filterPanel.add(btnGenerate, gbc);

        gbc.gridx = 2; gbc.gridwidth = 2;
        btnClear = new JButton("Clear Filters");
        btnClear.addActionListener(e -> onClear());
        filterPanel.add(btnClear, gbc);

        // ── Result table ─────────────────────────────────────────────────────
        String[] columns = {"ID", "SKU", "Name", "Price (RM)", "Qty", "Category", "Date Added", "Last Updated"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        tblReport = new JTable(tableModel);
        tblReport.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        tblReport.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblReport.getTableHeader().setReorderingAllowed(false);

        // Column width hints
        int[] widths = {40, 80, 160, 80, 50, 110, 145, 145};
        for (int i = 0; i < widths.length; i++) {
            tblReport.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
        }

        JScrollPane scrollPane = new JScrollPane(tblReport);
        scrollPane.setPreferredSize(new Dimension(860, 340));

        // ── Bottom bar ───────────────────────────────────────────────────────
        JPanel bottomPanel = new JPanel(new BorderLayout(8, 0));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(4, 6, 4, 6));

        lblSummary = new JLabel("Use the filters above and click Generate Report.");
        bottomPanel.add(lblSummary, BorderLayout.CENTER);

        btnBack = new JButton("Back to Dashboard");
        btnBack.addActionListener(e -> onBack());
        bottomPanel.add(btnBack, BorderLayout.EAST);

        // ── Frame layout ─────────────────────────────────────────────────────
        JPanel content = new JPanel(new BorderLayout(8, 8));
        content.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        content.add(filterPanel, BorderLayout.NORTH);
        content.add(scrollPane, BorderLayout.CENTER);
        content.add(bottomPanel, BorderLayout.SOUTH);

        setContentPane(content);
    }

    private void loadCategories() {
        cmbCategory.removeAllItems();
        cmbCategory.addItem(new CategoryItem(0L, "-- All Categories --"));
        try {
            List<Category> cats = categoryController.listCategories();
            for (Category c : cats) {
                cmbCategory.addItem(new CategoryItem(c.getId(), c.getName()));
            }
        } catch (Exception e) {
            // silently fall back to "All" if category load fails
        }
    }

    private void onGenerate() {
        // Parse date from
        LocalDate dateFrom = null;
        String fromStr = txtDateFrom.getText().trim();
        if (!fromStr.isEmpty()) {
            try {
                dateFrom = LocalDate.parse(fromStr);
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(this,
                        "Invalid 'Date Added From' format.\nPlease use YYYY-MM-DD (e.g. 2024-01-15).",
                        "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
        }

        // Parse date to
        LocalDate dateTo = null;
        String toStr = txtDateTo.getText().trim();
        if (!toStr.isEmpty()) {
            try {
                dateTo = LocalDate.parse(toStr);
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(this,
                        "Invalid 'Date Added To' format.\nPlease use YYYY-MM-DD (e.g. 2024-12-31).",
                        "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
        }

        // Validate range ordering
        if (dateFrom != null && dateTo != null && dateFrom.isAfter(dateTo)) {
            JOptionPane.showMessageDialog(this,
                    "'Date Added From' cannot be after 'Date Added To'.",
                    "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String keyword = txtKeyword.getText().trim();

        CategoryItem selected = (CategoryItem) cmbCategory.getSelectedItem();
        Long categoryId = (selected == null || selected.getId() <= 0) ? null : selected.getId();

        ReportFilter filter = new ReportFilter(dateFrom, dateTo, categoryId,
                keyword.isEmpty() ? null : keyword, 0);

        try {
            List<ReportRow> rows = reportController.generateReport(filter);
            populateTable(rows);
            lblSummary.setText("Report generated — " + rows.size() + " product(s) found.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Failed to generate report:\n" + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onClear() {
        txtDateFrom.setText("");
        txtDateTo.setText("");
        txtKeyword.setText("");
        if (cmbCategory.getItemCount() > 0) {
            cmbCategory.setSelectedIndex(0);
        }
        tableModel.setRowCount(0);
        lblSummary.setText("Filters cleared. Click Generate Report to load all products.");
    }

    private void onBack() {
        if (onBack != null) {
            onBack.run();
        }
        this.dispose();
    }

    private void populateTable(List<ReportRow> rows) {
        tableModel.setRowCount(0);
        for (ReportRow r : rows) {
            tableModel.addRow(new Object[]{
                r.getId(),
                r.getSku(),
                r.getName(),
                r.getPrice(),
                r.getQuantity(),
                r.getCategoryName(),
                r.getCreatedAt(),
                r.getUpdatedAt()
            });
        }
    }

    // ── Inner helper ──────────────────────────────────────────────────────────
    private static class CategoryItem {

        private final long id;
        private final String displayName;

        CategoryItem(long id, String displayName) {
            this.id = id;
            this.displayName = displayName;
        }

        public long getId() {
            return id;
        }

        @Override
        public String toString() {
            return displayName;
        }
    }
}
