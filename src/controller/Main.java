package controller;

import model.Car;
import model.Customer;
import model.Rental;
import repository.CarRepository;
import repository.CustomerRepository;
import repository.RentalRepository;
import service.CarService;
import service.CustomerService;
import service.RentalService;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        CarRepository carRepository = new CarRepository();
        CustomerRepository customerRepository = new CustomerRepository();
        RentalRepository rentalRepository = new RentalRepository(carRepository, customerRepository);

        CarService carService = new CarService(carRepository);
        CustomerService customerService = new CustomerService(customerRepository);
        RentalService rentalService = new RentalService(rentalRepository, carRepository, customerRepository);

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n=== Car Rental CLI ===");
            System.out.println("1. List cars");
            System.out.println("2. Add car");
            System.out.println("3. List customers");
            System.out.println("4. Add customer");
            System.out.println("5. List rentals");
            System.out.println("6. Create rental");
            System.out.println("0. Exit");
            System.out.print("Choose: ");

            String choice = scanner.nextLine();

            try {
                switch (choice) {
                    case "1":
                        listCars(carService);
                        break;
                    case "2":
                        addCar(scanner, carService);
                        break;
                    case "3":
                        listCustomers(customerService);
                        break;
                    case "4":
                        addCustomer(scanner, customerService);
                        break;
                    case "5":
                        listRentals(rentalService);
                        break;
                    case "6":
                        createRental(scanner, rentalService);
                        break;
                    case "0":
                        System.out.println("Bye!");
                        return;
                    default:
                        System.out.println("Unknown option");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private static void listCars(CarService carService) {
        List<Car> cars = carService.getAllCars();
        System.out.println("\n--- Cars ---");
        for (Car c : cars) {
            System.out.println(c.getDetails());
        }
    }

    private static void addCar(Scanner scanner, CarService carService) {
        System.out.print("Car name: ");
        String name = scanner.nextLine();
        System.out.print("License plate: ");
        String plate = scanner.nextLine();
        System.out.print("Daily price: ");
        double price = Double.parseDouble(scanner.nextLine());
        System.out.print("Status (AVAILABLE/RENTED/MAINTENANCE): ");
        String status = scanner.nextLine();

        Car car = carService.createCar(name, plate, price, status);
        System.out.println("Created car: " + car.getDetails());
    }

    private static void listCustomers(CustomerService customerService) {
        List<Customer> customers = customerService.getAllCustomers();
        System.out.println("\n--- Customers ---");
        for (Customer c : customers) {
            System.out.println(c.getDetails());
        }
    }

    private static void addCustomer(Scanner scanner, CustomerService customerService) {
        System.out.print("Customer name: ");
        String name = scanner.nextLine();
        System.out.print("Driver license: ");
        String dl = scanner.nextLine();
        System.out.print("Phone: ");
        String phone = scanner.nextLine();

        Customer customer = customerService.createCustomer(name, dl, phone);
        System.out.println("Created customer: " + customer.getDetails());
    }

    private static void listRentals(RentalService rentalService) {
        List<Rental> rentals = rentalService.getAllRentals();
        System.out.println("\n--- Rentals ---");
        for (Rental r : rentals) {
            System.out.println(r.getSummary());
        }
    }

    private static void createRental(Scanner scanner, RentalService rentalService) {
        System.out.print("Car id: ");
        int carId = Integer.parseInt(scanner.nextLine());
        System.out.print("Customer id: ");
        int customerId = Integer.parseInt(scanner.nextLine());
        System.out.print("Start date (YYYY-MM-DD): ");
        LocalDate start = LocalDate.parse(scanner.nextLine());
        System.out.print("End date (YYYY-MM-DD): ");
        LocalDate end = LocalDate.parse(scanner.nextLine());

        Rental rental = rentalService.createRental(carId, customerId, start, end);
        System.out.println("Created rental: " + rental.getSummary());
    }
}
