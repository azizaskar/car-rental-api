package service;

import exception.DuplicateResourceException;
import exception.InvalidInputException;
import model.Car;
import repository.CarRepository;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class CarService {

    private final CarRepository carRepository;

    public CarService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    public Car createCar(String name, String licensePlate, double dailyPrice, String status) {
        if (name == null || name.isBlank()) {
            throw new InvalidInputException("name cannot be empty");
        }
        if (licensePlate == null || licensePlate.isBlank()) {
            throw new InvalidInputException("license plate cannot be empty");
        }
        if (dailyPrice <= 0) {
            throw new InvalidInputException("daily price must be > 0");
        }
        List<Car> all = carRepository.getAll();
        boolean exists = all.stream()
                .anyMatch(c -> c.getLicensePlate().equalsIgnoreCase(licensePlate));
        if (exists) {
            throw new DuplicateResourceException("Car with plate " + licensePlate + " already exists");
        }

        Car car = new Car(0, name, licensePlate, dailyPrice, status);
        return carRepository.create(car);
    }

    public List<Car> getAllCars() {
        return carRepository.getAll();
    }

    public Car getCarById(int id) {
        return carRepository.getById(id);
    }

    public void updateCar(int id, String name, String licensePlate, double dailyPrice, String status) {
        if (dailyPrice <= 0) {
            throw new InvalidInputException("daily price must be > 0");
        }

        Car existing = carRepository.getById(id); // not found болса, репо өзі exception лақтырады
        existing.setName(name);
        existing.setLicensePlate(licensePlate);
        existing.setDailyPrice(dailyPrice);
        existing.setStatus(status);

        carRepository.update(id, existing);
    }
    public void deleteCar(int id) {
        carRepository.delete(id);
    }
    public List<Car> getCarsSortedByPrice() {
        return carRepository.getAll().stream()
                .sorted(Comparator.comparingDouble(Car::getDailyPrice)) // Lambda
                .collect(Collectors.toList());
    }

}
