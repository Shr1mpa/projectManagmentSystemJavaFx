package com.example.projectsystem.Repositories;

import com.example.projectsystem.DAO.ProjectDao;
import com.example.projectsystem.Models.Owner;
import com.example.projectsystem.Models.Project;
import com.example.projectsystem.Utils.HibernateUtil;
import org.hibernate.SessionFactory;

import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Predicate;

public class ProjectRepository extends Repository<Project> {
    private ProjectDao projectDao;

    public ProjectRepository() {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        projectDao = new ProjectDao(sessionFactory);
        data = projectDao.getAll();
    }

    @Override
    void sort(Comparator comparator) {

    }

    @Override
    public List search(Predicate predicate) {
        return null;
    }
    public Project searchByName(String name) {
        Optional<Project> foundOwner = data.stream()
                .filter(project -> project.getName().equals(name))
                .findFirst();
        return foundOwner.orElseThrow(NoSuchElementException::new);
    }
}
