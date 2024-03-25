package com.example.projectsystem.Managers;

import com.example.projectsystem.Models.User;
import com.example.projectsystem.Utils.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.Optional;

public class AuthenticationService {
    private static final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

    public static Optional<User> authenticate(String username, String password) {
        try (Session session = sessionFactory.openSession()) {
            User user = session.createQuery("FROM User WHERE username = :username", User.class)
                    .setParameter("username", username)
                    .uniqueResult();
            if (user != null && user.getPassword().equals(password)) {
                return Optional.of(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }
}
