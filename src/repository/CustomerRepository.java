package repository;

import exception.DatabaseOperationException;
import exception.ResourceNotFoundException;
import model.Customer;
import utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerRepository {

    public Customer create(Customer customer) {
        String sql = "INSERT INTO customer (name, driver_license, phone) " +
                "VALUES (?, ?, ?) RETURNING id";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, customer.getName());
            stmt.setString(2, customer.getDriverLicense());
            stmt.setString(3, customer.getPhone());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("id");
                    customer.setId(id);
                }
            }

            return customer;

        } catch (SQLException e) {
            throw new DatabaseOperationException("Error creating customer", e);
        }
    }

    public List<Customer> getAll() {
        String sql = "SELECT id, name, driver_license, phone FROM customer";
        List<Customer> customers = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                customers.add(mapRowToCustomer(rs));
            }

        } catch (SQLException e) {
            throw new DatabaseOperationException("Error fetching customers", e);
        }

        return customers;
    }

    public Customer getById(int id) {
        String sql = "SELECT id, name, driver_license, phone FROM customer WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRowToCustomer(rs);
                } else {
                    throw new ResourceNotFoundException("Customer with id " + id + " not found");
                }
            }

        } catch (SQLException e) {
            throw new DatabaseOperationException("Error fetching customer by id", e);
        }
    }

    public void update(int id, Customer customer) {
        String sql = "UPDATE customer SET name = ?, driver_license = ?, phone = ? " +
                "WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, customer.getName());
            stmt.setString(2, customer.getDriverLicense());
            stmt.setString(3, customer.getPhone());
            stmt.setInt(4, id);

            int rows = stmt.executeUpdate();
            if (rows == 0) {
                throw new ResourceNotFoundException("Customer with id " + id + " not found");
            }

        } catch (SQLException e) {
            throw new DatabaseOperationException("Error updating customer", e);
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM customer WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            int rows = stmt.executeUpdate();

            if (rows == 0) {
                throw new ResourceNotFoundException("Customer with id " + id + " not found");
            }

        } catch (SQLException e) {
            throw new DatabaseOperationException("Error deleting customer", e);
        }
    }

    private Customer mapRowToCustomer(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        String driverLicense = rs.getString("driver_license");
        String phone = rs.getString("phone");

        return new Customer(id, name, driverLicense, phone);
    }
}
