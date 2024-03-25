package com.example.projectsystem.DAO;

import com.example.projectsystem.Exceptions.RegisterException;
import com.example.projectsystem.Models.Owner;
import com.example.projectsystem.Models.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Optional;

public class OwnerDao implements Dao<Owner> {
    private final SessionFactory sessionFactory;
    public OwnerDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
    @Override
    public Optional<Owner> get(long id) {
        return Optional.empty();
    }

    @Override
    public List<Owner> getAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM Owner", Owner.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void save(Owner entity){
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(entity);
            session.getTransaction().commit();
        }
    }

    @Override
    public void update(Owner entity, String[] params) {

    }

    @Override
    public void delete(Owner entity) {

    }

}
