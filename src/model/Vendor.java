package model;

public class Vendor {

    private long id;
    private String vendorId;
    private String name;
    private String personIncharge;
    private String address;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getVendorId() {
        return vendorId;
    }

    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPersonIncharge() {
        return personIncharge;
    }

    public void setPersonIncharge(String personIncharge) {
        this.personIncharge = personIncharge;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
