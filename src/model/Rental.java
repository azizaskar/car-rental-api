package model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Rental implements Validatable, PricedItem {
    private int id;
    private Car car;
    private Customer customer;
    private LocalDate startDate;
    private LocalDate endDate;
    private double totalPrice;

    public Rental(int id, Car car, Customer customer, LocalDate startDate, LocalDate endDate) {
        this.id = id;
        this.car = car;
        this.customer = customer;
        this.startDate = startDate;
        this.endDate = endDate;
        validate();
        this.totalPrice = calculatePrice();
    }

    public int getId() {
        return id;
    }

    public Car getCar() {
        return car;
    }

    public Customer getCustomer() {
        return customer;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    @Override
    public void validate() {
        if (car == null) {
            throw new IllegalArgumentException("car cannot be null");
        }
        if (customer == null) {
            throw new IllegalArgumentException("customer cannot be null");
        }
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("dates cannot be null");
        }
        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("end date cannot be before start date");
        }
        // мысал business rule: car AVAILABLE болу керек
        if (!"AVAILABLE".equalsIgnoreCase(car.getStatus())) {
            throw new IllegalArgumentException("car is not available");
        }
    }

    @Override
    public double calculatePrice() {
        long days = ChronoUnit.DAYS.between(startDate, endDate);
        if (days <= 0) {
            days = 1; // кем дегенде 1 күн деп алайық
        }
        return days * car.getDailyPrice();
    }

    public String getSummary() {
        return "Rental #" + id + " car=" + car.getName() +
                ", customer=" + customer.getName() +
                ", from " + startDate + " to " + endDate +
                ", total=" + totalPrice;
    }
}
