package model;

public abstract class BaseEntity {
    protected int id;
    protected String name;

    public BaseEntity(int id, String name) {
        this.id = id;
        this.name = name;
    }

    // getters / setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("id must be > 0");
        }
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name cannot be empty");
        }
        this.name = name;
    }

    // abstract methods (міндетті түрде override болады)
    public abstract String getDetails();
    public abstract String getType();

    // concrete method
    public String describe() {
        return getType() + ": " + name + " (id=" + id + ")";
    }
}
