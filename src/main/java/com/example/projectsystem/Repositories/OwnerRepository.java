package com.example.projectsystem.Repositories;

import com.example.projectsystem.DAO.OwnerDao;
import com.example.projectsystem.Models.Owner;
import org.hibernate.SessionFactory;

import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Predicate;

public class OwnerRepository extends Repository<Owner> {
    OwnerDao ownerDao;
    public OwnerRepository(SessionFactory sessionFactory) {
        ownerDao = new OwnerDao(sessionFactory);
        data = ownerDao.getAll();
    }

    @Override
    public void sort(Comparator<Owner> comparator) {

    }

    @Override
    public List<Owner> search(Predicate<Owner> predicate) {
        return null;
    }

    public Owner findOwner(String firstName, String lastName) {
        Optional<Owner> foundOwner = data.stream()
                .filter(owner -> owner.getFirstName().equals(firstName)
                        && owner.getLastName().equals(lastName))
                .findFirst();
        return foundOwner.orElseThrow(NoSuchElementException::new);
    }
}
