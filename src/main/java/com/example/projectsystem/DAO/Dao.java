package com.example.projectsystem.DAO;

import java.util.List;
import java.util.Optional;

public interface Dao<T> {
    Optional<T> get(long id);

    List<T> getAll();

    void save(T entity);

    void update(T entity, String[] params);

    void delete(T entity);

}
