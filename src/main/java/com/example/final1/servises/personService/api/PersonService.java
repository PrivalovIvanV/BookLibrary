package com.example.final1.servises.personService.api;

import com.example.final1.servises.personService.impl.entity.Person;
import org.springframework.web.multipart.MultipartFile;

public interface PersonService {
    boolean isAuth();
    Person getCurrentUser() throws UserNotAuthException;
    void UpdatePerson(Person person);
    void addAvatar(MultipartFile file);
    void save(Person person);
    void deleteAvatar();
}
