package com.example.projectsystem.DAO;

import com.example.projectsystem.Models.Owner;
import com.example.projectsystem.Models.Project;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Optional;

public class ProjectDao implements Dao<Project>{
    private final SessionFactory sessionFactory;
    public ProjectDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
    @Override
    public Optional<Project> get(long id) {
        return Optional.empty();
    }

    @Override
    public List<Project> getAll() {
        try (Session session = sessionFactory.openSession()) {
            // Використовуємо JOIN FETCH, щоб витягнути і завдання для кожного проєкту
            Query<Project> query = session.createQuery("FROM Project p LEFT JOIN FETCH p.tasks", Project.class);
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void save(Project entity) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(entity);
            session.getTransaction().commit();
        }
    }

    @Override
    public void update(Project entity, String[] params) {

    }

    @Override
    public void delete(Project entity) {

    }
}
