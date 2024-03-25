package com.example.projectsystem.DAO;

import com.example.projectsystem.Models.Task;

import java.util.List;
import java.util.Optional;

public class TaskDao implements Dao<Task> {
    @Override
    public Optional<Task> get(long id) {
        return Optional.empty();
    }

    @Override
    public List<Task> getAll() {
        return null;
    }

    @Override
    public void save(Task entity) {

    }

    @Override
    public void update(Task entity, String[] params) {

    }

    @Override
    public void delete(Task entity) {

    }
}
