package com.example.final1.integratedTests.security;

import com.example.final1.integratedTests.config.RegistrationServiceTestConf;
import com.example.final1.security.RegistrationService;
import com.example.final1.servises.personService.impl.entity.Person;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.Assert.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertAll;


@SpringBootTest(classes = RegistrationServiceTestConf.class)
class RegistrationServiceTest {

    @Autowired RegistrationService registrationService;

    @Test
    void registrationNewPerson() {
        Person testPerson = new Person();
        String passwordBeforeEncode = "1234567";
        testPerson.setPassword(passwordBeforeEncode);
        testPerson.setFirst_name("Ivan");

        registrationService.registrationNewPerson(testPerson);
        assertAll(
                () -> assertNotEquals(passwordBeforeEncode, testPerson.getPassword()),
                () -> Assert.assertEquals(testPerson.getPassword(), "password")
        );


    }
}