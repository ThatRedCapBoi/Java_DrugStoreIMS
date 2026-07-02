/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

/**
 *
 * @author Itadori
 */
import dto.ReportFilter;
import dto.ReportResult;
import service.DashboardService;

public class DashboardController {

    private final DashboardService service;

    public DashboardController(DashboardService service) {
        this.service = service;
    }

    public ReportResult generateReport(ReportFilter filter) {
        return service.generateReport(filter);
    }
}
