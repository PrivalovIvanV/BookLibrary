package com.example.final1.servises.personService.api;

import com.example.final1.servises.personService.impl.entity.Person;

public interface PersonService {
    Person get(int id);
    void update(Person person);

}
