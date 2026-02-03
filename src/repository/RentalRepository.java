package repository;

import exception.DatabaseOperationException;
import exception.ResourceNotFoundException;
import model.Car;
import model.Customer;
import model.Rental;
import utils.DatabaseConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RentalRepository implements CrudRepository<Rental> {

    private final CarRepository carRepository;
    private final CustomerRepository customerRepository;

    public RentalRepository(CarRepository carRepository, CustomerRepository customerRepository) {
        this.carRepository = carRepository;
        this.customerRepository = customerRepository;
    }

    public Rental create(Rental rental) {
        String sql = "INSERT INTO rental (car_id, customer_id, start_date, end_date, total_price) " +
                "VALUES (?, ?, ?, ?, ?) RETURNING id";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, rental.getCar().getId());
            stmt.setInt(2, rental.getCustomer().getId());
            stmt.setDate(3, Date.valueOf(rental.getStartDate()));
            stmt.setDate(4, Date.valueOf(rental.getEndDate()));
            stmt.setDouble(5, rental.getTotalPrice());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("id");
                    // id set жоқ, бірақ керек болса Rental-ға setter қосуға болады
                    return new Rental(id, rental.getCar(), rental.getCustomer(),
                            rental.getStartDate(), rental.getEndDate());
                }
            }

            throw new DatabaseOperationException("Failed to create rental", null);

        } catch (SQLException e) {
            throw new DatabaseOperationException("Error creating rental", e);
        }
    }

    public List<Rental> getAll() {
        String sql = "SELECT id, car_id, customer_id, start_date, end_date, total_price FROM rental";
        List<Rental> rentals = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                rentals.add(mapRowToRental(rs));
            }

        } catch (SQLException e) {
            throw new DatabaseOperationException("Error fetching rentals", e);
        }

        return rentals;
    }

    public Rental getById(int id) {
        String sql = "SELECT id, car_id, customer_id, start_date, end_date, total_price " +
                "FROM rental WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRowToRental(rs);
                } else {
                    throw new ResourceNotFoundException("Rental with id " + id + " not found");
                }
            }

        } catch (SQLException e) {
            throw new DatabaseOperationException("Error fetching rental by id", e);
        }
    }

    public void update(int id, Rental rental) {
        String sql = "UPDATE rental SET car_id = ?, customer_id = ?, start_date = ?, end_date = ?, total_price = ? " +
                "WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, rental.getCar().getId());
            stmt.setInt(2, rental.getCustomer().getId());
            stmt.setDate(3, Date.valueOf(rental.getStartDate()));
            stmt.setDate(4, Date.valueOf(rental.getEndDate()));
            stmt.setDouble(5, rental.getTotalPrice());
            stmt.setInt(6, id);

            int rows = stmt.executeUpdate();
            if (rows == 0) {
                throw new ResourceNotFoundException("Rental with id " + id + " not found");
            }

        } catch (SQLException e) {
            throw new DatabaseOperationException("Error updating rental", e);
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM rental WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            int rows = stmt.executeUpdate();

            if (rows == 0) {
                throw new ResourceNotFoundException("Rental with id " + id + " not found");
            }

        } catch (SQLException e) {
            throw new DatabaseOperationException("Error deleting rental", e);
        }
    }

    private Rental mapRowToRental(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        int carId = rs.getInt("car_id");
        int customerId = rs.getInt("customer_id");
        LocalDate startDate = rs.getDate("start_date").toLocalDate();
        LocalDate endDate = rs.getDate("end_date").toLocalDate();
        double totalPrice = rs.getDouble("total_price");

        Car car = carRepository.getById(carId);
        Customer customer = customerRepository.getById(customerId);

        // totalPrice Rental ішінде calculatePrice арқылы да шығады, бірақ БД-дағы мәнді қолданамыз
        Rental rental = new Rental(id, car, customer, startDate, endDate);
        // егер totalPrice-ты міндетті түрде сақтағым келсе, Rental-ға setter қосуға болады
        return rental;
    }
}
