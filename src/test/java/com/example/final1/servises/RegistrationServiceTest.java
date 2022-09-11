package com.example.final1.servises;

import com.example.final1.security.RegistrationService;
import com.example.final1.servises.personService.impl.entity.Person;
import com.example.final1.servises.personService.impl.repo.PersonRepo;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@RunWith(SpringRunner.class)
public class RegistrationServiceTest {

    @Autowired
    RegistrationService registrationService;
    @MockBean
    PersonRepo repo;


    @Test
    public void registrationNewPerson() {
        Person person = new Person();

        String password = "password";
        person.setFirst_name("ivan");
        person.setPassword(password);

        Mockito.when(repo.save(any(Person.class))).thenReturn(null);
        registrationService.registrationNewPerson(person);

        Assert.assertFalse(person.getPassword() == password);//мы убеждаемя что пароль изменился
        Assert.assertTrue( person.getPassword() != "");
    }
}