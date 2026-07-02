/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package repository;

import dto.ReportFilter;
import dto.ReportRow;
import infra.DBManager;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import model.Product;

/**
 *
 * @author Itadori
 */
public class MySqlProductRepo implements ProductRepo {

    private final DBManager db;

    public MySqlProductRepo(DBManager db) {
        this.db = db;
    }

    private Product map(ResultSet rs) throws SQLException {
        Product p = new Product();
        p.setId(rs.getLong("id"));
        p.setSku(rs.getString("sku"));
        p.setName(rs.getString("name"));
        p.setPrice(rs.getBigDecimal("price"));
        p.setQuantity(rs.getInt("quantity"));
        p.setCategoryId(rs.getLong("category_id"));
        long vId = rs.getLong("vendor_id");
        p.setVendorId(rs.wasNull() ? null : vId);
        Timestamp ct = rs.getTimestamp("created_at");
        if (ct != null) p.setCreatedAt(ct.toLocalDateTime());
        Timestamp ut = rs.getTimestamp("updated_at");
        if (ut != null) p.setUpdatedAt(ut.toLocalDateTime());
        return p;
    }

    @Override
    public List<Product> findAll() {
        String sql = "SELECT id, sku, name, price, quantity, category_id, vendor_id, created_at, updated_at FROM products ORDER BY name";
        List<Product> list = new ArrayList<>();

        try (Connection c = db.getConnection(); PreparedStatement ps = c.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(map(rs));
            }
            return list;

        } catch (SQLException e) {
            throw new RuntimeException("Product findAll failed: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Product> findById(long id) {
        String sql = "SELECT id, sku, name, price, quantity, category_id, vendor_id, created_at, updated_at FROM products WHERE id = ?";

        try (Connection c = db.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return Optional.empty();
                }
                return Optional.of(map(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Product findById failed: " + e.getMessage(), e);
        }
    }

    @Override
    public Product save(Product p) {
        String sql = "INSERT INTO products (sku, name, price, quantity, category_id, vendor_id) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection c = db.getConnection(); PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, p.getSku());
            ps.setString(2, p.getName());
            ps.setBigDecimal(3, p.getPrice());
            ps.setInt(4, p.getQuantity());
            ps.setLong(5, p.getCategoryId());
            if (p.getVendorId() != null) {
                ps.setLong(6, p.getVendorId());
            } else {
                ps.setNull(6, java.sql.Types.INTEGER);
            }
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    p.setId(keys.getLong(1));
                }
            }
            return p;

        } catch (SQLException e) {
            throw new RuntimeException("Product save failed: " + e.getMessage(), e);
        }
    }

    @Override
    public void update(Product p) {
        String sql = "UPDATE products SET sku=?, name=?, price=?, quantity=?, category_id=?, vendor_id=? WHERE id=?";

        try (Connection c = db.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, p.getSku());
            ps.setString(2, p.getName());
            ps.setBigDecimal(3, p.getPrice());
            ps.setInt(4, p.getQuantity());
            ps.setLong(5, p.getCategoryId());
            if (p.getVendorId() != null) {
                ps.setLong(6, p.getVendorId());
            } else {
                ps.setNull(6, java.sql.Types.INTEGER);
            }
            ps.setLong(7, p.getId());
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Product update failed: " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(long id) {
        String sql = "DELETE FROM products WHERE id=?";

        try (Connection c = db.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setLong(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Product delete failed: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Product> search(String query) {
        String q = (query == null) ? "" : query.trim();
        String sql = "SELECT id, sku, name, price, quantity, category_id, vendor_id, created_at, updated_at "
                + "FROM products WHERE sku LIKE ? OR name LIKE ? ORDER BY name";

        List<Product> list = new ArrayList<>();
        try (Connection c = db.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, "%" + q + "%");
            ps.setString(2, "%" + q + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(map(rs));
                }
            }
            return list;

        } catch (SQLException e) {
            throw new RuntimeException("Product search failed: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean existsBySku(String sku) {
        String sql = "SELECT 1 FROM products WHERE LOWER(sku) = LOWER(?) LIMIT 1";

        try (Connection c = db.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, sku);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            throw new RuntimeException("Product existsBySku failed: " + e.getMessage(), e);
        }
    }

    @Override
    public int countAll() {
        String sql = "SELECT COUNT(*) FROM products";
        try (Connection c = db.getConnection(); PreparedStatement ps = c.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            rs.next();
            return rs.getInt(1);
        } catch (SQLException e) {
            throw new RuntimeException("countAll failed: " + e.getMessage(), e);
        }
    }

    @Override
    public int countLowStock(int threshold) {
        String sql = "SELECT COUNT(*) FROM products WHERE quantity <= ?";
        try (Connection c = db.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, threshold);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException("countLowStock failed: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Product> findLowStock(int threshold) {
        String sql = "SELECT id, sku, name, price, quantity, category_id, vendor_id, created_at, updated_at "
                + "FROM products WHERE quantity <= ? ORDER BY quantity ASC, name ASC";
        List<Product> list = new ArrayList<>();

        try (Connection c = db.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, threshold);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(map(rs));
                }
            }
            return list;

        } catch (SQLException e) {
            throw new RuntimeException("findLowStock failed: " + e.getMessage(), e);
        }
    }

    @Override
    public List<ReportRow> findByFilter(ReportFilter filter) {
        StringBuilder sql = new StringBuilder(
                "SELECT p.id, p.sku, p.name, p.price, p.quantity, "
                + "c.name AS category_name, p.created_at, p.updated_at "
                + "FROM products p LEFT JOIN categories c ON p.category_id = c.id "
                + "WHERE 1=1");

        List<Object> params = new ArrayList<>();

        String keyword = (filter.getProductQuery() == null) ? "" : filter.getProductQuery().trim();
        if (!keyword.isEmpty()) {
            sql.append(" AND (p.sku LIKE ? OR p.name LIKE ?)");
            params.add("%" + keyword + "%");
            params.add("%" + keyword + "%");
        }

        if (filter.getCategoryId() != null) {
            sql.append(" AND p.category_id = ?");
            params.add(filter.getCategoryId());
        }

        if (filter.getFrom() != null) {
            sql.append(" AND DATE(p.created_at) >= ?");
            params.add(java.sql.Date.valueOf(filter.getFrom()));
        }

        if (filter.getTo() != null) {
            sql.append(" AND DATE(p.created_at) <= ?");
            params.add(java.sql.Date.valueOf(filter.getTo()));
        }

        sql.append(" ORDER BY p.name");

        List<ReportRow> list = new ArrayList<>();
        try (Connection c = db.getConnection();
                PreparedStatement ps = c.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                Object param = params.get(i);
                if (param instanceof String) {
                    ps.setString(i + 1, (String) param);
                } else if (param instanceof Long) {
                    ps.setLong(i + 1, (Long) param);
                } else if (param instanceof java.sql.Date) {
                    ps.setDate(i + 1, (java.sql.Date) param);
                }
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ReportRow row = new ReportRow();
                    row.setId(rs.getLong("id"));
                    row.setSku(rs.getString("sku"));
                    row.setName(rs.getString("name"));
                    row.setPrice(rs.getBigDecimal("price"));
                    row.setQuantity(rs.getInt("quantity"));
                    row.setCategoryName(rs.getString("category_name"));

                    Timestamp created = rs.getTimestamp("created_at");
                    row.setCreatedAt(created != null ? created.toLocalDateTime().toString().replace('T', ' ') : "");

                    Timestamp updated = rs.getTimestamp("updated_at");
                    row.setUpdatedAt(updated != null ? updated.toLocalDateTime().toString().replace('T', ' ') : "");

                    list.add(row);
                }
            }
            return list;

        } catch (SQLException e) {
            throw new RuntimeException("Product findByFilter failed: " + e.getMessage(), e);
        }
    }
}
