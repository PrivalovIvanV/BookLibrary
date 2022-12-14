package com.example.final1.security;


import com.example.final1.servises.personService.impl.entity.Person;
import com.example.final1.servises.personService.impl.repo.PersonRepo;
import com.example.final1.util.UserRole;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.sql.Timestamp;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RegistrationService {

    private final PersonRepo repo;
    private final PasswordEncoder passwordEncoder;



    @SneakyThrows
    @Transactional
    public void registrationNewPerson(Person person){          ///Тут мы кодируем пришедший к нам пароль


        String encodedPassword = passwordEncoder.encode(person.getPassword());      ///и уже Person с зашифрованным паролем
        person.setPassword(encodedPassword);                                        ///улетает на сервер
        person.setPersonRole(UserRole.ROLE_USER);                                   ///
        person.setLast_active(new Timestamp(System.currentTimeMillis()));
        person.setAccount_created(new Date(System.currentTimeMillis()));
        person.setNotBanned(true);

        repo.save(person);                                                          ///
    }


    public boolean isExist(String email){
        return repo.findPersonByEmail(email).isPresent();
    } // проверяем на совпадение



}
