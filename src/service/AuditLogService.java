package service;

import java.util.List;
import model.AuditLog;

public interface AuditLogService {

    List<AuditLog> list(String role);

    void record(String action, Long productId, String details);
}
