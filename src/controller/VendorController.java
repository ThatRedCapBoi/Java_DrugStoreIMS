package controller;

import exception.VendorException;
import java.util.List;
import java.util.Optional;
import model.Vendor;
import service.VendorService;

public class VendorController {

    private final VendorService service;

    public VendorController(VendorService service) {
        this.service = service;
    }

    public List<Vendor> listVendors() {
        return service.list();
    }

    public List<Vendor> search(String q) {
        return service.search(q);
    }

    public Optional<Vendor> getVendor(long id) {
        return service.get(id);
    }

    public Vendor create(Vendor v, String role) throws VendorException {
        return service.create(v, role);
    }

    public void update(Vendor v, String role) throws VendorException {
        service.update(v, role);
    }

    public void delete(long id, String role) throws VendorException {
        service.delete(id, role);
    }
}
