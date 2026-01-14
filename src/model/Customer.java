package model;

public class Customer extends BaseEntity implements Validatable {
    private String driverLicense;
    private String phone;

    public Customer(int id, String name, String driverLicense, String phone) {
        super(id, name);
        this.driverLicense = driverLicense;
        this.phone = phone;
        validate();
    }

    public String getDriverLicense() {
        return driverLicense;
    }

    public void setDriverLicense(String driverLicense) {
        if (driverLicense == null || driverLicense.isBlank()) {
            throw new IllegalArgumentException("driver license cannot be empty");
        }
        this.driverLicense = driverLicense;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        // phone бос болса да болады, сондықтан қатты тексермейміз
        this.phone = phone;
    }

    @Override
    public void validate() {
        setName(this.name);
        setDriverLicense(this.driverLicense);
    }

    @Override
    public String getDetails() {
        return "Customer " + name + " (DL=" + driverLicense + ", phone=" + phone + ")";
    }

    @Override
    public String getType() {
        return "Customer";
    }
}
