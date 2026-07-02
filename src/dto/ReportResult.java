package dto;

import java.util.LinkedHashMap;
import java.util.List;
import model.Product;

/**
 * Result of a filtered Generate Report (M02): the matched products, headline
 * counts, and the chart series (stock summed per category).
 *
 * @author Itadori
 */
public class ReportResult {

    private final int totalProducts;
    private final int lowStockCount;
    private final int threshold;
    private final List<Product> products;
    private final LinkedHashMap<String, Integer> stockByCategory;

    public ReportResult(int totalProducts, int lowStockCount, int threshold,
            List<Product> products, LinkedHashMap<String, Integer> stockByCategory) {
        this.totalProducts = totalProducts;
        this.lowStockCount = lowStockCount;
        this.threshold = threshold;
        this.products = products;
        this.stockByCategory = stockByCategory;
    }

    public int getTotalProducts() {
        return totalProducts;
    }

    public int getLowStockCount() {
        return lowStockCount;
    }

    public int getThreshold() {
        return threshold;
    }

    public List<Product> getProducts() {
        return products;
    }

    public LinkedHashMap<String, Integer> getStockByCategory() {
        return stockByCategory;
    }
}
