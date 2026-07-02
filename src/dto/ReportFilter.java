package dto;

import java.time.LocalDate;

/**
 * Filter criteria for the Manager Generate Report (M02). Null/blank fields mean
 * "no bound / all".
 *
 * @author Itadori
 */
public class ReportFilter {

    private final LocalDate from;      // null = no lower bound (on created_at)
    private final LocalDate to;        // null = no upper bound
    private final Long categoryId;     // null = all categories
    private final String productQuery; // null/blank = all products (matches name or SKU)
    private final int threshold;       // low-stock threshold

    public ReportFilter(LocalDate from, LocalDate to, Long categoryId, String productQuery, int threshold) {
        this.from = from;
        this.to = to;
        this.categoryId = categoryId;
        this.productQuery = productQuery;
        this.threshold = threshold;
    }

    public LocalDate getFrom() {
        return from;
    }

    public LocalDate getTo() {
        return to;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public String getProductQuery() {
        return productQuery;
    }

    public int getThreshold() {
        return threshold;
    }
}
