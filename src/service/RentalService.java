package service;

import exception.InvalidInputException;
import model.Car;
import model.Customer;
import model.Rental;
import repository.CarRepository;
import repository.CustomerRepository;
import repository.RentalRepository;

import java.time.LocalDate;
import java.util.List;

public class RentalService {

    private final RentalRepository rentalRepository;
    private final CarRepository carRepository;
    private final CustomerRepository customerRepository;

    public RentalService(RentalRepository rentalRepository,
                         CarRepository carRepository,
                         CustomerRepository customerRepository) {
        this.rentalRepository = rentalRepository;
        this.carRepository = carRepository;
        this.customerRepository = customerRepository;
    }

    public Rental createRental(int carId, int customerId, LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            throw new InvalidInputException("dates cannot be null");
        }
        if (endDate.isBefore(startDate)) {
            throw new InvalidInputException("end date cannot be before start date");
        }

        Car car = carRepository.getById(carId);
        Customer customer = customerRepository.getById(customerId);

        // business rule: тек AVAILABLE car жалға беріледі
        if (!"AVAILABLE".equalsIgnoreCase(car.getStatus())) {
            throw new InvalidInputException("car is not available for rental");
        }

        Rental rental = new Rental(0, car, customer, startDate, endDate);
        return rentalRepository.create(rental);
    }

    public List<Rental> getAllRentals() {
        return rentalRepository.getAll();
    }

    public Rental getRentalById(int id) {
        return rentalRepository.getById(id);
    }

    public void updateRental(int id, int carId, int customerId, LocalDate startDate, LocalDate endDate) {
        if (endDate.isBefore(startDate)) {
            throw new InvalidInputException("end date cannot be before start date");
        }

        Car car = carRepository.getById(carId);
        Customer customer = customerRepository.getById(customerId);

        Rental rental = new Rental(id, car, customer, startDate, endDate);
        rentalRepository.update(id, rental);
    }

    public void deleteRental(int id) {
        rentalRepository.delete(id);
    }
}
