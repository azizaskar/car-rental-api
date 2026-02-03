package model;

public interface Validatable {
    void validate();

    // Assignment 4 талабы: Default method
    default void checkId(int id) {
        if (id < 0) {
            throw new IllegalArgumentException("ID cannot be negative (checked by default method)");
        }
    }
}