package com.example.projectsystem.Managers;

import com.example.projectsystem.Models.Project;
import com.example.projectsystem.Models.Status;
import com.example.projectsystem.Utils.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.List;

public class QueryManager {
    private SessionFactory sessionFactory;

    public QueryManager() {
        sessionFactory = HibernateUtil.getSessionFactory();
    }

    public List<Project> getProjectsByStatus(Status status) {
        try (Session session = sessionFactory.openSession()) {
            String hql = "FROM Project p WHERE p.status = :status";
            Query<Project> query = session.createQuery(hql, Project.class);
            query.setParameter("status", status);
            return query.list();
        }
    }
    public List<Project> getProjectsByStatusAndOwner(Status status, String ownerLastName, String ownerFirstName) {
        try (Session session = sessionFactory.openSession()) {
            String hql = "SELECT p FROM Project p JOIN p.owner o WHERE p.status = :status AND o.lastName = :lastName AND o.firstName = :firstName";
            Query<Project> query = session.createQuery(hql, Project.class);
            query.setParameter("status", status);
            query.setParameter("lastName", ownerLastName);
            query.setParameter("firstName", ownerFirstName);
            return query.list();
        }
    }
    public List<Project> getProjectsByOwnerName(String ownerLastName, String ownerFirstName) {
        try (Session session = sessionFactory.openSession()) {
            String hql = "SELECT p FROM Project p JOIN p.owner o WHERE o.lastName = :lastName AND o.firstName = :firstName";
            Query<Project> query = session.createQuery(hql, Project.class);
            query.setParameter("lastName", ownerLastName);
            query.setParameter("firstName", ownerFirstName);
            return query.list();
        }
    }
}
