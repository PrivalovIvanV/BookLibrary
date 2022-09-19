package com.example.final1.integratedTest;

import com.example.final1.servises. personService.api. PersonNotFoundException;
import com.example.final1.servises. personService.impl. PersonServiceImpl;
import com.example.final1.servises. personService.impl.entity. Person;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class PersonServiceImplTest {

        @Autowired
         PersonServiceImpl  personService;

        ///////////////////////////////////////////////////////
        ////////
        ////////    Тут используется не основная база данных
        ////////    в ней 62 книги из которых 6 принадлежат
        ////////    пользователю с id 1
        ////////    Всего в таблице 7 пользователей
        ////////
        ///////////////////////////////////////////////////////

        @Test
        void should_return_7() {
            List< Person>  personList =  personService.getAll();

            Assert.assertEquals( personList.size(), 7);

        }

        @Test
        void should_throw_exception(){
            assertAll(
                    () -> Assert.assertThrows( PersonNotFoundException.class, () ->  personService.get(-1)),
                    () -> Assert.assertThrows( PersonNotFoundException.class, () ->  personService.get(1000000))
            );
        }

        @Test
        void getAssertPerson(){
             Person  person =  personService.get(3);

            Assert.assertEquals( person.getId(), 3);
        }



        @Test
        void remove_nonexistentPerson(){
            assertAll(
                    () -> Assert.assertThrows( PersonNotFoundException.class, () ->  personService.remove(-1)),
                    () -> Assert.assertThrows( PersonNotFoundException.class, () ->  personService.remove(1000000))
            );
        }

        @Test
        void updatePerson(){
            Person person = new Person();
            person.setEmail("oleg@mail.ru");
            person.setFirst_name("updateName");

            personService.update(person);
            Person person1 = personService.get(4);
            Assert.assertEquals( person1.getFirst_name(), person.getFirst_name());
            Assert.assertEquals(person1.getEmail(), person.getEmail());

            person.setFirst_name("Oleg");
            personService.update(person);

        }

        @Test
        void loadByEmail(){
            Assert.assertThrows(UsernameNotFoundException.class, () ->  personService.loadUserByUsername("несуществующая почта"));
        }


    

}