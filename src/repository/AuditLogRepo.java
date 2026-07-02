package repository;

import java.util.List;
import model.AuditLog;

public interface AuditLogRepo {

    // ponytail: append-only log, no update/delete/findById — audit rows are never edited

    List<AuditLog> findAll();

    AuditLog save(AuditLog a);
}
