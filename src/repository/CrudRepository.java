package repository;

import java.util.List;

// Generics қолданылды (<T>)
public interface CrudRepository<T> {
    T create(T entity);
    List<T> getAll();
    T getById(int id);
    void update(int id, T entity);
    void delete(int id);
}