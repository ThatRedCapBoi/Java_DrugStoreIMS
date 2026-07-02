package controller;

import java.util.List;
import model.AuditLog;
import service.AuditLogService;

public class AuditLogController {

    private final AuditLogService service;

    public AuditLogController(AuditLogService service) {
        this.service = service;
    }

    public List<AuditLog> list(String role) {
        return service.list(role);
    }
}
