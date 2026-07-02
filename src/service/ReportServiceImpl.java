package service;

import dto.ReportFilter;
import dto.ReportRow;
import java.util.List;
import repository.ProductRepo;

public class ReportServiceImpl implements ReportService {

    private final ProductRepo productRepo;

    public ReportServiceImpl(ProductRepo productRepo) {
        this.productRepo = productRepo;
    }

    @Override
    public List<ReportRow> generateReport(ReportFilter filter) {
        if (filter == null) {
            filter = new ReportFilter(null, null, null, null, 0);
        }
        return productRepo.findByFilter(filter);
    }
}
