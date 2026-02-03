package repository;

import exception.DatabaseOperationException;
import exception.ResourceNotFoundException;
import model.Car;
import utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CarRepository implements CrudRepository<Car> {

    public Car create(Car car) {
        String sql = "INSERT INTO car (name, license_plate, daily_price, status) " +
                "VALUES (?, ?, ?, ?) RETURNING id";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, car.getName());
            stmt.setString(2, car.getLicensePlate());
            stmt.setDouble(3, car.getDailyPrice());
            stmt.setString(4, car.getStatus());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("id");
                    car.setId(id);
                }
            }

            return car;

        } catch (SQLException e) {
            throw new DatabaseOperationException("Error creating car", e);
        }
    }

    public List<Car> getAll() {
        String sql = "SELECT id, name, license_plate, daily_price, status FROM car";
        List<Car> cars = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Car car = mapRowToCar(rs);
                cars.add(car);
            }

        } catch (SQLException e) {
            e.printStackTrace(); // ← МІНДЕТТІ ҚОС
            throw new DatabaseOperationException("Error fetching cars", e);
        }

        return cars;
    }

    public Car getById(int id) {
        String sql = "SELECT id, name, license_plate, daily_price, status FROM car WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRowToCar(rs);
                } else {
                    throw new ResourceNotFoundException("Car with id " + id + " not found");
                }
            }

        } catch (SQLException e) {
            throw new DatabaseOperationException("Error fetching car by id", e);
        }
    }

    public void update(int id, Car car) {
        String sql = "UPDATE car SET name = ?, license_plate = ?, daily_price = ?, status = ? " +
                "WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, car.getName());
            stmt.setString(2, car.getLicensePlate());
            stmt.setDouble(3, car.getDailyPrice());
            stmt.setString(4, car.getStatus());
            stmt.setInt(5, id);

            int rows = stmt.executeUpdate();
            if (rows == 0) {
                throw new ResourceNotFoundException("Car with id " + id + " not found");
            }

        } catch (SQLException e) {
            throw new DatabaseOperationException("Error updating car", e);
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM car WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            int rows = stmt.executeUpdate();

            if (rows == 0) {
                throw new ResourceNotFoundException("Car with id " + id + " not found");
            }

        } catch (SQLException e) {
            throw new DatabaseOperationException("Error deleting car", e);
        }
    }

    private Car mapRowToCar(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        String plate = rs.getString("license_plate");
        double price = rs.getDouble("daily_price");
        String status = rs.getString("status");

        return new Car(id, name, plate, price, status);
    }
}
