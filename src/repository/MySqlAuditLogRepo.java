package repository;

import infra.DBManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import model.AuditLog;

public class MySqlAuditLogRepo implements AuditLogRepo {

    private final DBManager db;

    public MySqlAuditLogRepo(DBManager db) {
        this.db = db;
    }

    private AuditLog map(ResultSet rs) throws SQLException {
        AuditLog a = new AuditLog();
        a.setId(rs.getLong("id"));
        long userId = rs.getLong("user_id");
        a.setUserId(rs.wasNull() ? null : userId);
        a.setUsername(rs.getString("username"));
        a.setAction(rs.getString("action"));
        long productId = rs.getLong("product_id");
        a.setProductId(rs.wasNull() ? null : productId);
        a.setDetails(rs.getString("details"));
        Timestamp ct = rs.getTimestamp("created_at");
        if (ct != null) a.setCreatedAt(ct.toLocalDateTime());
        return a;
    }

    @Override
    public List<AuditLog> findAll() {
        String sql = "SELECT id, user_id, username, action, product_id, details, created_at "
                + "FROM audit_log ORDER BY created_at DESC, id DESC";
        List<AuditLog> list = new ArrayList<>();

        try (Connection c = db.getConnection(); PreparedStatement ps = c.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(map(rs));
            }
            return list;

        } catch (SQLException e) {
            throw new RuntimeException("AuditLog findAll failed: " + e.getMessage(), e);
        }
    }

    @Override
    public AuditLog save(AuditLog a) {
        String sql = "INSERT INTO audit_log (user_id, username, action, product_id, details) VALUES (?, ?, ?, ?, ?)";

        try (Connection c = db.getConnection(); PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            if (a.getUserId() != null) {
                ps.setLong(1, a.getUserId());
            } else {
                ps.setNull(1, Types.BIGINT);
            }
            ps.setString(2, a.getUsername());
            ps.setString(3, a.getAction());
            if (a.getProductId() != null) {
                ps.setLong(4, a.getProductId());
            } else {
                ps.setNull(4, Types.INTEGER);
            }
            ps.setString(5, a.getDetails());
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    a.setId(keys.getLong(1));
                }
            }
            return a;

        } catch (SQLException e) {
            throw new RuntimeException("AuditLog save failed: " + e.getMessage(), e);
        }
    }
}
