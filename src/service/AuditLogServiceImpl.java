package service;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.AuditLog;
import model.User;
import repository.AuditLogRepo;

public class AuditLogServiceImpl implements AuditLogService {

    private static final Logger logger = Logger.getLogger(AuditLogServiceImpl.class.getName());

    private final AuditLogRepo repo;
    private final AuthService authService;

    public AuditLogServiceImpl(AuditLogRepo repo, AuthService authService) {
        this.repo = repo;
        this.authService = authService;
    }

    @Override
    public List<AuditLog> list(String role) {
        requireManager(role);
        return repo.findAll();
    }

    @Override
    public void record(String action, Long productId, String details) {
        // ponytail: best-effort — an audit write failure must never break the product operation it logs
        try {
            AuditLog a = new AuditLog();
            User u = authService.currentUser();
            if (u != null) {
                a.setUserId(u.getId());
                a.setUsername(u.getUsername());
            } else {
                a.setUsername("UNKNOWN");
            }
            a.setAction(action);
            a.setProductId(productId);
            a.setDetails(details);
            repo.save(a);
        } catch (Exception e) {
            logger.log(Level.WARNING, "Failed to record audit log entry: " + action, e);
        }
    }

    private void requireManager(String role) {
        if (role == null || !"INVENTORY_MANAGER".equalsIgnoreCase(role.trim())) {
            throw new IllegalArgumentException("Access denied. Manager only.");
        }
    }
}
