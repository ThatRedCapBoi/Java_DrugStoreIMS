/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

/**
 *
 * @author Itadori
 */
import dto.ReportFilter;
import dto.ReportResult;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import model.Category;
import model.Product;
import repository.CategoryRepo;
import repository.ProductRepo;

public class DashboardServiceImpl implements DashboardService {

    private final ProductRepo productRepo;
    private final CategoryRepo categoryRepo;

    public DashboardServiceImpl(ProductRepo productRepo, CategoryRepo categoryRepo) {
        this.productRepo = productRepo;
        this.categoryRepo = categoryRepo;
    }

    @Override
    public ReportResult generateReport(ReportFilter filter) {
        if (filter.getThreshold() < 0) {
            throw new IllegalArgumentException("Threshold cannot be negative.");
        }

        // Note: in-memory filter over findAll(); push predicates into SQL only if the product table ever grows large
        String query = filter.getProductQuery() == null ? "" : filter.getProductQuery().trim().toLowerCase();
        List<Product> filtered = new ArrayList<>();
        for (Product p : productRepo.findAll()) {
            if (!matchesDate(p, filter.getFrom(), filter.getTo())) {
                continue;
            }
            if (filter.getCategoryId() != null && p.getCategoryId() != filter.getCategoryId()) {
                continue;
            }
            if (!query.isEmpty()
                    && !contains(p.getName(), query)
                    && !contains(p.getSku(), query)) {
                continue;
            }
            filtered.add(p);
        }

        int lowStockCount = 0;
        for (Product p : filtered) {
            if (p.getQuantity() <= filter.getThreshold()) {
                lowStockCount++;
            }
        }

        return new ReportResult(filtered.size(), lowStockCount, filter.getThreshold(),
                filtered, stockByCategory(filtered));
    }

    private boolean matchesDate(Product p, LocalDate from, LocalDate to) {
        if (from == null && to == null) {
            return true;
        }
        if (p.getCreatedAt() == null) {
            return false;
        }
        LocalDate created = p.getCreatedAt().toLocalDate();
        if (from != null && created.isBefore(from)) {
            return false;
        }
        return to == null || !created.isAfter(to);
    }

    private boolean contains(String value, String lowerQuery) {
        return value != null && value.toLowerCase().contains(lowerQuery);
    }

    /** Sum quantity per category name, ordered by category name for a stable chart. */
    private LinkedHashMap<String, Integer> stockByCategory(List<Product> products) {
        Map<Long, String> names = new HashMap<>();
        for (Category c : categoryRepo.findAll()) {
            names.put(c.getId(), c.getName());
        }
        LinkedHashMap<String, Integer> series = new LinkedHashMap<>();
        for (Product p : products) {
            String name = names.getOrDefault(p.getCategoryId(), "Uncategorized");
            series.merge(name, p.getQuantity(), Integer::sum);
        }
        return series;
    }
}
