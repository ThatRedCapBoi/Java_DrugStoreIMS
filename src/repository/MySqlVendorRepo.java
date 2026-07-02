package repository;

import infra.DBManager;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import model.Vendor;

public class MySqlVendorRepo implements VendorRepo {

    private final DBManager db;

    public MySqlVendorRepo(DBManager db) {
        this.db = db;
    }

    private Vendor map(ResultSet rs) throws SQLException {
        Vendor v = new Vendor();
        v.setId(rs.getLong("id"));
        v.setVendorId(rs.getString("vendor_id"));
        v.setName(rs.getString("name"));
        v.setPersonIncharge(rs.getString("person_incharge"));
        v.setAddress(rs.getString("address"));
        return v;
    }

    @Override
    public List<Vendor> findAll() {
        String sql = "SELECT id, vendor_id, name, person_incharge, address FROM vendors ORDER BY name";
        List<Vendor> list = new ArrayList<>();

        try (Connection c = db.getConnection(); PreparedStatement ps = c.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(map(rs));
            }
            return list;
        } catch (SQLException e) {
            throw new RuntimeException("Vendor findAll failed: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Vendor> findById(long id) {
        String sql = "SELECT id, vendor_id, name, person_incharge, address FROM vendors WHERE id = ?";

        try (Connection c = db.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return Optional.empty();
                }
                return Optional.of(map(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Vendor findById failed: " + e.getMessage(), e);
        }
    }

    @Override
    public Vendor save(Vendor v) {
        String sql = "INSERT INTO vendors (vendor_id, name, person_incharge, address) VALUES (?, ?, ?, ?)";

        try (Connection c = db.getConnection(); PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, v.getVendorId());
            ps.setString(2, v.getName());
            ps.setString(3, v.getPersonIncharge());
            ps.setString(4, v.getAddress());
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    v.setId(keys.getLong(1));
                }
            }
            return v;
        } catch (SQLException e) {
            throw new RuntimeException("Vendor save failed: " + e.getMessage(), e);
        }
    }

    @Override
    public void update(Vendor v) {
        String sql = "UPDATE vendors SET vendor_id=?, name=?, person_incharge=?, address=? WHERE id=?";

        try (Connection c = db.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, v.getVendorId());
            ps.setString(2, v.getName());
            ps.setString(3, v.getPersonIncharge());
            ps.setString(4, v.getAddress());
            ps.setLong(5, v.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Vendor update failed: " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(long id) {
        String sql = "DELETE FROM vendors WHERE id=?";

        try (Connection c = db.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Vendor delete failed: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Vendor> search(String query) {
        String q = (query == null) ? "" : query.trim();
        String sql = "SELECT id, vendor_id, name, person_incharge, address "
                + "FROM vendors WHERE vendor_id LIKE ? OR name LIKE ? ORDER BY name";

        List<Vendor> list = new ArrayList<>();
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
            throw new RuntimeException("Vendor search failed: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean existsByVendorId(String vendorId) {
        String sql = "SELECT 1 FROM vendors WHERE LOWER(vendor_id) = LOWER(?) LIMIT 1";

        try (Connection c = db.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, vendorId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Vendor existsByVendorId failed: " + e.getMessage(), e);
        }
    }
}
