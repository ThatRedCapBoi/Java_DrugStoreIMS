package service;

import exception.VendorException;
import java.util.List;
import java.util.Optional;
import model.Vendor;
import repository.VendorRepo;

public class VendorServiceImpl implements VendorService {

    private final VendorRepo repo;

    public VendorServiceImpl(VendorRepo repo) {
        this.repo = repo;
    }

    @Override
    public List<Vendor> list() {
        return repo.findAll();
    }

    @Override
    public List<Vendor> search(String query) {
        return repo.search(query);
    }

    @Override
    public Optional<Vendor> get(long id) {
        return repo.findById(id);
    }

    private void validate(Vendor v) throws VendorException {
        if (v == null) {
            throw new VendorException("Vendor cannot be null.");
        }
        if (v.getVendorId() == null || v.getVendorId().trim().isEmpty()) {
            throw new VendorException("Vendor ID is mandatory.");
        }
        if (v.getName() == null || v.getName().trim().isEmpty()) {
            throw new VendorException("Vendor Name is mandatory.");
        }
        if (v.getAddress() == null || v.getAddress().trim().isEmpty()) {
            throw new VendorException("Address is mandatory.");
        }
        if (v.getPersonIncharge() == null || v.getPersonIncharge().trim().isEmpty()) {
            throw new VendorException("Person Incharge is mandatory.");
        }
    }

    @Override
    public Vendor create(Vendor v, String role) throws VendorException {
        validate(v);

        if (repo.existsByVendorId(v.getVendorId().trim())) {
            throw new VendorException("Vendor ID already exists.");
        }

        v.setVendorId(v.getVendorId().trim());
        v.setName(v.getName().trim());
        v.setAddress(v.getAddress().trim());
        v.setPersonIncharge(v.getPersonIncharge().trim());
        return repo.save(v);
    }

    @Override
    public void update(Vendor v, String role) throws VendorException {
        validate(v);
        if (v.getId() <= 0) {
            throw new VendorException("Invalid vendor id.");
        }
        v.setVendorId(v.getVendorId().trim());
        v.setName(v.getName().trim());
        v.setAddress(v.getAddress().trim());
        v.setPersonIncharge(v.getPersonIncharge().trim());
        repo.update(v);
    }

    @Override
    public void delete(long id, String role) throws VendorException {
        requireManager(role);
        if (id <= 0) {
            throw new VendorException("Invalid vendor id.");
        }
        repo.delete(id);
    }

    private void requireManager(String role) {
        if (role == null) {
            throw new IllegalArgumentException("Access denied.");
        }
        if (!"INVENTORY_MANAGER".equalsIgnoreCase(role.trim())) {
            throw new IllegalArgumentException("Access denied. Manager only.");
        }
    }
}
