package com.example.projectsystem.Repositories;

import com.example.projectsystem.DAO.ProjectDao;
import com.example.projectsystem.DAO.UserDao;
import com.example.projectsystem.Models.User;
import com.example.projectsystem.Models.Worker;
import com.example.projectsystem.Utils.HibernateUtil;
import org.hibernate.SessionFactory;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

public class WorkerRepository extends Repository<User>{
    private UserDao userDao;

    public WorkerRepository() {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        userDao = new UserDao(sessionFactory);
        data = userDao.getAllWorkers();
    }

    @Override
    void sort(Comparator<User> comparator) {

    }

    @Override
    public List<User> search(Predicate<User> predicate) {
        return null;
    }
}
