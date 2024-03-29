package com.example.projectsystem.Repositories;

import com.example.projectsystem.DAO.TaskDao;
import com.example.projectsystem.Utils.HibernateUtil;
import org.hibernate.SessionFactory;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

public class TaskRepository extends Repository {
    TaskDao taskDao;

    public TaskRepository() {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        taskDao = new TaskDao(sessionFactory);
        data = taskDao.getAll();
    }

    @Override
    void sort(Comparator comparator) {

    }

    @Override
    public List search(Predicate predicate) {
        return null;
    }
}
