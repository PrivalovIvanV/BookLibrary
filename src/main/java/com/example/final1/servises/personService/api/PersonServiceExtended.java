package com.example.final1.servises.personService.api;

import com.example.final1.servises.personService.impl.entity.Person;

import java.util.List;

public interface PersonServiceExtended extends PersonService {
    void remove(int id);
    List<Person> getAll();
}
