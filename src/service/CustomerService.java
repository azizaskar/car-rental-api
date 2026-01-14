package service;

import exception.DuplicateResourceException;
import exception.InvalidInputException;
import model.Customer;
import repository.CustomerRepository;

import java.util.List;

public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Customer createCustomer(String name, String driverLicense, String phone) {
        if (name == null || name.isBlank()) {
            throw new InvalidInputException("name cannot be empty");
        }
        if (driverLicense == null || driverLicense.isBlank()) {
            throw new InvalidInputException("driver license cannot be empty");
        }

        List<Customer> all = customerRepository.getAll();
        boolean exists = all.stream()
                .anyMatch(c -> c.getDriverLicense().equalsIgnoreCase(driverLicense));
        if (exists) {
            throw new DuplicateResourceException("Customer with driver license " + driverLicense + " already exists");
        }

        Customer customer = new Customer(0, name, driverLicense, phone);
        return customerRepository.create(customer);
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.getAll();
    }

    public Customer getCustomerById(int id) {
        return customerRepository.getById(id);
    }

    public void updateCustomer(int id, String name, String driverLicense, String phone) {
        if (name == null || name.isBlank()) {
            throw new InvalidInputException("name cannot be empty");
        }
        if (driverLicense == null || driverLicense.isBlank()) {
            throw new InvalidInputException("driver license cannot be empty");
        }

        Customer existing = customerRepository.getById(id);
        existing.setName(name);
        existing.setDriverLicense(driverLicense);
        existing.setPhone(phone);

        customerRepository.update(id, existing);
    }

    public void deleteCustomer(int id) {
        customerRepository.delete(id);
    }
}
