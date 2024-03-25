package com.example.projectsystem.Repositories;

import com.example.projectsystem.Models.User;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

public abstract class Repository <T> {
    protected List<T> data;
    abstract void sort(Comparator<T> comparator);
    public abstract List<T> search(Predicate<T> predicate);
    public List<T> getData() {
        return data;
    }
}
