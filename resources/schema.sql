-- Customers table
CREATE TABLE IF NOT EXISTS customer (
                                        id              SERIAL PRIMARY KEY,
                                        name            VARCHAR(100) NOT NULL,
    driver_license  VARCHAR(50) NOT NULL UNIQUE,
    phone           VARCHAR(30)
    );

-- Cars table
CREATE TABLE IF NOT EXISTS car (
                                   id             SERIAL PRIMARY KEY,
                                   name           VARCHAR(100) NOT NULL,
    license_plate  VARCHAR(20) NOT NULL UNIQUE,
    daily_price    NUMERIC(10,2) NOT NULL CHECK (daily_price > 0),
    status         VARCHAR(20) NOT NULL
    );

-- Rentals table
CREATE TABLE IF NOT EXISTS rental (
                                      id           SERIAL PRIMARY KEY,
                                      car_id       INTEGER NOT NULL,
                                      customer_id  INTEGER NOT NULL,
                                      start_date   DATE NOT NULL,
                                      end_date     DATE NOT NULL,
                                      total_price  NUMERIC(10,2) NOT NULL CHECK (total_price >= 0),

    CONSTRAINT fk_rental_car
    FOREIGN KEY (car_id) REFERENCES car(id),
    CONSTRAINT fk_rental_customer
    FOREIGN KEY (customer_id) REFERENCES customer(id)
    );

-- Sample data
INSERT INTO customer (name, driver_license, phone) VALUES
                                                       ('Alice Johnson', 'DL-12345', '+1-555-1111'),
                                                       ('Bob Smith', 'DL-67890', '+1-555-2222');

INSERT INTO car (name, license_plate, daily_price, status) VALUES
                                                               ('Toyota Corolla', 'ABC-001', 15000.00, 'AVAILABLE'),
                                                               ('Honda Civic', 'ABC-002', 16000.00, 'AVAILABLE'),
                                                               ('Tesla Model 3', 'EV-777', 20000.00, 'MAINTENANCE'),
                                                               ('Toyota Camry 75', 'KZ 001 AAA', 25000.00, 'AVAILABLE'),
                                                               ('BMW X5', 'KZ 777 BMW', 45000.00, 'RENTED'),
                                                               ('Hyundai Elantra', 'KZ 123 ABC', 18000.00, 'AVAILABLE'),
                                                               ('Mercedes S-Class', 'KZ 002 SSS', 60000.00, 'MAINTENANCE');

INSERT INTO rental (car_id, customer_id, start_date, end_date, total_price) VALUES
                                                                                (1, 1, '2026-01-10', '2026-01-12', 100.00),
                                                                                (2, 2, '2026-01-11', '2026-01-13', 120.00);
