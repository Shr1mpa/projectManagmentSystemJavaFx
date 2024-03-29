package com.example.projectsystem.DAO;

import com.example.projectsystem.Exceptions.RegisterException;
import com.example.projectsystem.Models.Role;
import com.example.projectsystem.Models.User;
import com.example.projectsystem.Models.Worker;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class UserDao implements Dao<User>{

    private final SessionFactory sessionFactory;
    public UserDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
    @Override
    public Optional<User> get(long id) {
        try (Session session = sessionFactory.openSession()) {
            User user = session.get(User.class, id);
            return Optional.ofNullable(user);
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public List<User> getAll() {
        return null;
    }

    @Override
    public void save(User entity){
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

    private void checkUserExist (User entity) {
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
    public Optional<User> getByUsernameAndPassword(String username, String password) {
        try (Session session = sessionFactory.openSession()) {
            Query<User> query = session.createQuery("FROM User WHERE username = :username AND password = :password", User.class);
            query.setParameter("username", username);
            query.setParameter("password", password);
            User user = query.uniqueResult();
            return Optional.ofNullable(user);
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }
    public List<User> getAllWorkers() {
        try (Session session = sessionFactory.openSession()) {
            String hql = "FROM User WHERE role != :role";
            Query<User> query = session.createQuery(hql, User.class);
            query.setParameter("role", Role.MANAGER);
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }

    }
}
