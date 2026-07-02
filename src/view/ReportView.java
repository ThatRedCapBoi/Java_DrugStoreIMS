package view;

import controller.CategoryController;
import controller.DashboardController;
import dto.ReportFilter;
import dto.ReportResult;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import model.Category;
import model.Product;

/**
 * Manager Generate Report window (M02): filter by date/product/category, view a
 * stock-per-category bar chart, product table, and headline counts.
 *
 * @author Itadori
 */
public class ReportView extends JFrame {

    private static final int THRESHOLD = 10;

    private final DashboardController dashboardController;

    private final JComboBox<CategoryItem> cboCategory = new JComboBox<>();
    private final JTextField txtProduct = new JTextField(12);
    private final JTextField txtFrom = new JTextField(9);
    private final JTextField txtTo = new JTextField(9);
    private final BarChartPanel chart = new BarChartPanel();
    private final DefaultTableModel tableModel = new DefaultTableModel(
            new Object[]{"SKU", "Name", "Category", "Qty", "Price", "Created"}, 0) {
        @Override
        public boolean isCellEditable(int r, int c) {
            return false;
        }
    };
    private final JLabel lblSummary = new JLabel(" ");
    private final Map<Long, String> categoryNames = new HashMap<>();

    public ReportView(DashboardController dashboardController, CategoryController categoryController, String role) {
        this.dashboardController = dashboardController;

        setTitle("Generate Report (" + role + ")");
        setSize(720, 560);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        DefaultComboBoxModel<CategoryItem> model = new DefaultComboBoxModel<>();
        model.addElement(new CategoryItem("All categories", null));
        for (Category c : categoryController.listCategories()) {
            categoryNames.put(c.getId(), c.getName());
            model.addElement(new CategoryItem(c.getName(), c.getId()));
        }
        cboCategory.setModel(model);

        add(buildFilterBar(), BorderLayout.NORTH);

        JTable table = new JTable(tableModel);
        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                chart, new JScrollPane(table));
        split.setResizeWeight(0.55);
        add(split, BorderLayout.CENTER);

        lblSummary.setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10));
        add(lblSummary, BorderLayout.SOUTH);

        generate(); // initial unfiltered report
    }

    private JPanel buildFilterBar() {
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        bar.add(new JLabel("Category:"));
        bar.add(cboCategory);
        bar.add(new JLabel("Product:"));
        bar.add(txtProduct);
        bar.add(new JLabel("From:"));
        bar.add(txtFrom);
        bar.add(new JLabel("To:"));
        bar.add(txtTo);
        JButton btnGenerate = new JButton("Generate");
        btnGenerate.addActionListener(e -> generate());
        bar.add(btnGenerate);
        txtFrom.setToolTipText("yyyy-MM-dd (optional)");
        txtTo.setToolTipText("yyyy-MM-dd (optional)");
        return bar;
    }

    private void generate() {
        LocalDate from;
        LocalDate to;
        try {
            from = parseDate(txtFrom.getText());
            to = parseDate(txtTo.getText());
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this,
                    "Dates must be in yyyy-MM-dd format (leave blank for no bound).",
                    "Invalid date", JOptionPane.WARNING_MESSAGE);
            return;
        }

        CategoryItem selected = (CategoryItem) cboCategory.getSelectedItem();
        Long categoryId = (selected == null) ? null : selected.id;
        String product = txtProduct.getText().trim();

        ReportFilter filter = new ReportFilter(from, to, categoryId,
                product.isEmpty() ? null : product, THRESHOLD);

        try {
            ReportResult result = dashboardController.generateReport(filter);
            chart.setData(result.getStockByCategory());

            tableModel.setRowCount(0);
            for (Product p : result.getProducts()) {
                tableModel.addRow(new Object[]{
                    p.getSku(), p.getName(),
                    categoryNames.getOrDefault(p.getCategoryId(), "Uncategorized"),
                    p.getQuantity(), p.getPrice(), p.getCreatedAt()
                });
            }
            lblSummary.setText("Total: " + result.getTotalProducts()
                    + "   |   Low stock (≤ " + result.getThreshold() + "): " + result.getLowStockCount());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Generate report failed:\n" + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private LocalDate parseDate(String text) {
        String t = text.trim();
        return t.isEmpty() ? null : LocalDate.parse(t);
    }

    /** Combo entry: display label + backing category id (null = all). */
    private static class CategoryItem {

        final String label;
        final Long id;

        CategoryItem(String label, Long id) {
            this.label = label;
            this.id = id;
        }

        @Override
        public String toString() {
            return label;
        }
    }
}
