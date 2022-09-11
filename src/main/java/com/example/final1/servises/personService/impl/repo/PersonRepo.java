package com.example.final1.servises.personService.impl.repo;

import com.example.final1.servises.personService.impl.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface PersonRepo extends JpaRepository<Person, Integer> {

    Optional<Person> findPersonByEmail(String email);
}
