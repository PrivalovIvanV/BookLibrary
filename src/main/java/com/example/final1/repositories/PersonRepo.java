package com.example.final1.repositories;

import com.example.final1.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface PersonRepo extends JpaRepository<Person, Integer> {

    Optional<Person> findPersonByEmail(String email);
}
