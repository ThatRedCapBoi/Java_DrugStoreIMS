package repository;

import java.util.List;
import java.util.Optional;
import model.Vendor;

public interface VendorRepo {

    List<Vendor> findAll();

    Optional<Vendor> findById(long id);

    Vendor save(Vendor v);

    void update(Vendor v);

    void delete(long id);

    List<Vendor> search(String query);

    boolean existsByVendorId(String vendorId);
}
