package com.example.projectsystem.DAO;

import com.example.projectsystem.Exceptions.RegisterException;
import com.example.projectsystem.Models.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Optional;

public class UserDao implements Dao<User>{

    private final SessionFactory sessionFactory;
    public UserDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
    @Override
    public Optional<User> get(long id) {
        return Optional.empty();
    }

    @Override
    public List<User> getAll() {
        return null;
    }

    @Override
    public void save(User entity) throws RegisterException{
        try (Session session = sessionFactory.openSession()) {
            checkUserExist(entity);
            session.beginTransaction();
            session.save(entity);
            session.getTransaction().commit();
        }
    }

    @Override
    public void update(User entity, String[] params) {

    }

    @Override
    public void delete(User entity) {

    }

    private void checkUserExist (User entity) throws RegisterException {
        try (Session session = sessionFactory.openSession()) {
            // Проверяем, существует ли пользователь с таким же именем пользователя
            Query<User> usernameQuery = session.createQuery("FROM User WHERE username = :username", User.class);
            usernameQuery.setParameter("username", entity.getUsername());
            List<User> usersWithSameUsername = usernameQuery.getResultList();

            // Проверяем, существует ли пользователь с такой же электронной почтой
            Query<User> emailQuery = session.createQuery("FROM User WHERE email = :email", User.class);
            emailQuery.setParameter("email", entity.getEmail());
            List<User> usersWithSameEmail = emailQuery.getResultList();

            if (!usersWithSameUsername.isEmpty() || !usersWithSameEmail.isEmpty()) {
                throw new RegisterException("A user with the same username or email already exists");
            }

        }

    }
}
