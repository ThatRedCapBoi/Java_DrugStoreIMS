package service;

import exception.VendorException;
import java.util.List;
import java.util.Optional;
import model.Vendor;

public interface VendorService {

    List<Vendor> list();

    List<Vendor> search(String query);

    Optional<Vendor> get(long id);

    Vendor create(Vendor v, String role) throws VendorException;

    void update(Vendor v, String role) throws VendorException;

    void delete(long id, String role) throws VendorException;
}
