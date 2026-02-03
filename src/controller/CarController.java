package controller;

import service.CarService;
import java.util.Scanner;

public class CarController {

    private final CarService carService;
    private final Scanner scanner;

    public CarController(CarService carService, Scanner scanner) {
        this.carService = carService;
        this.scanner = scanner;
    }

    // Menu
    public void start() {
        while (true) {
            System.out.println("1. Көлік қосу");
            System.out.println("2. Көліктерді көру");
            System.out.print("Таңдаңыз: ");
            String choice = scanner.nextLine();

            if (choice.equals("1")) {
                createCar(); // Төмендегі әдісті шақырады
            } else if (choice.equals("2")) {
                listCars();
            } else {
                break;
            }
        }
    }

    // User Input
    private void createCar() {
        System.out.println("Аты қандай?");
        String name = scanner.nextLine();
        // Service-ке жіберу
        carService.createCar(name, "KZ01", 100.0, "AVAILABLE");
    }

    private void listCars() {
        carService.getAllCars().forEach(c -> System.out.println(c.getDetails()));
    }
}