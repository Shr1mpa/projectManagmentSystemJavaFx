package com.example.projectsystem.Managers;

import com.example.projectsystem.Models.User;
import com.example.projectsystem.Utils.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.Optional;

public class AuthenticationService {
    private static final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

    public static Optional<User> authenticate(String username, String password) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            User user = session.createQuery("FROM User WHERE username = :username", User.class)
                    .setParameter("username", username)
                    .uniqueResult();
            if (user != null && user.getPassword().equals(password)) {
                transaction.commit();
                return Optional.of(user);
            }
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
        return Optional.empty();
    }
}
