package controller;

import dto.ReportFilter;
import dto.ReportRow;
import java.util.List;
import service.ReportService;

public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    public List<ReportRow> generateReport(ReportFilter filter) {
        return reportService.generateReport(filter);
    }
}
