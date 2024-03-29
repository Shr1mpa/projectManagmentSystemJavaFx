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
    public Optional<Project> get(int id) {
        try (Session session = sessionFactory.openSession()) {
            Project project = session.get(Project.class, id);
            return Optional.ofNullable(project);
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public List<Project> getAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM Project ", Project.class).list();
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
    public void update(Project entity) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.update(entity);
            session.getTransaction().commit();
        }
    }
}
