package model;

public class Car extends BaseEntity implements Validatable {
    private String licensePlate;
    private double dailyPrice;
    private String status; // AVAILABLE, RENTED, MAINTENANCE

    public Car(int id, String name, String licensePlate, double dailyPrice, String status) {
        super(id, name);
        this.licensePlate = licensePlate;
        this.dailyPrice = dailyPrice;
        this.status = status;
        validate();
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        if (licensePlate == null || licensePlate.isBlank()) {
            throw new IllegalArgumentException("license plate cannot be empty");
        }
        this.licensePlate = licensePlate;
    }

    public double getDailyPrice() {
        return dailyPrice;
    }



    public void setDailyPrice(double dailyPrice) {
        if (dailyPrice <= 0) {
            throw new IllegalArgumentException("daily price must be > 0");
        }
        this.dailyPrice = dailyPrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        if (status == null || status.isBlank()) {
            throw new IllegalArgumentException("status cannot be empty");
        }
        this.status = status;
    }

    @Override
    public void validate() {
        setName(this.name);           // name бос емес
        setLicensePlate(this.licensePlate);
        setDailyPrice(this.dailyPrice);
        setStatus(this.status);
    }

    @Override
    public String getDetails() {
        return "Car " + name + " [" + licensePlate + "], " + dailyPrice + " per day, status=" + status;
    }

    @Override
    public String getType() {
        return "Car";
    }
}
