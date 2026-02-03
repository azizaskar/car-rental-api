import controller.CarController; // <-- Controller-ді шақырамыз
import repository.CarRepository;
import service.CarService;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // 1. Бөліктерді құрастыру (Dependency Injection)
        CarRepository repo = new CarRepository();
        CarService service = new CarService(repo);
        Scanner scanner = new Scanner(System.in);

        // Controller-ге Service пен Scanner-ді береміз
        CarController controller = new CarController(service, scanner);

        // 2. Бағдарламаны іске қосу
        controller.start();
    }
}