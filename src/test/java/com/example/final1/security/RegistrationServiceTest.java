//package com.example.final1.security;
//
//import com.example.final1.servises.personService.impl.entity.Person;
//import com.example.final1.servises.personService.impl.repo.PersonRepo;
//import com.example.final1.util.UserRole;
//import lombok.AllArgsConstructor;
//import lombok.NoArgsConstructor;
//import lombok.RequiredArgsConstructor;
//import org.junit.After;
//import org.junit.Before;
//import org.junit.BeforeClass;
//import org.junit.Test;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.runner.RunWith;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import java.util.List;
//
//import static org.junit.Assert.*;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest
//@RequiredArgsConstructor
//public class RegistrationServiceTest {
//
//    private final RegistrationService service;
//    private final PersonRepo personRepo;
//    private final List<Person> list;
//
//    @Before
//    public void setUp() throws Exception {
//        Person person = new Person();
//        person.setPersonRole(UserRole.ROLE_USER);
//        person.setFirst_name("Ivan");
//        person.setEmail("emilForTest@mail.ru");
//        person.setNotBanned(true);
//        list.add(person);
//
//        personRepo.saveAll(list);
//    }
//
//    @After
//    public void tearDown() throws Exception {
//        personRepo.deleteAll(list);
//        list.clear();
//    }
//
//    @Test
//    public void registrationNewPerson() {
//
//        service.registrationNewPerson(new Person());
//    }
//
//    @Test
//    public void isExist() {
//    }
//
//
//    private Person createDefaultPerson(){
//        Person person = new Person();
//        person.setPassword("12345");
//        person.setNotBanned(true);
//        person.setPersonRole(UserRole.ROLE_USER);
//        person.setFirst_name("NameForTest");
//        list.add(person);
//        return person;
//    }
//}