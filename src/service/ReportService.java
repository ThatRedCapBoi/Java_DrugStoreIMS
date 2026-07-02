package service;

import dto.ReportFilter;
import dto.ReportRow;
import java.util.List;

public interface ReportService {

    /**
     * Returns a list of inventory report rows matching the given filters.
     * Any null/zero filter field is treated as "no restriction".
     */
    List<ReportRow> generateReport(ReportFilter filter);
}
