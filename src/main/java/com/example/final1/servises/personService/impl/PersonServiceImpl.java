package com.example.final1.servises.personService.impl;


import com.example.final1.servises.personService.api.PersonNotFoundException;
import com.example.final1.servises.personService.api.PersonServiceExtended;
import com.example.final1.servises.personService.impl.entity.Person;
import com.example.final1.servises.personService.impl.repo.PersonRepo;
import com.example.final1.security.PersonDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PersonServiceImpl implements UserDetailsService, PersonServiceExtended {

    private final JdbcTemplate jdbcTemplate;
    private final PersonRepo repo;





    @Override
    public Person get(int id) {
        if (!isPresent(id)){
            throw new PersonNotFoundException("Пользователь с Id " + id + " не найден в базе данных");
        }else {
            return jdbcTemplate.query(
                    "select * from person where id = ?",
                    new Object[]{id},
                    new BeanPropertyRowMapper<>(Person.class)
            ).stream().findFirst().get();
        }
    }


    @Override
    public void remove(int id) {
        if(!isPresent(id)){
            throw new PersonNotFoundException("Ошибка при удалении пользователя с id " + id);
        } else
            jdbcTemplate.update("delete from person where id = ?", id);
    }


    @Override
    public List<Person> getAll() {
        return repo.findAll();
    }



    @Transactional
    public void update(Person person){
        jdbcTemplate.update("update Person set first_name=?, last_name=? where email = ?",
                person.getFirst_name(),
                person.getLast_name(),
                person.getEmail()
        );
    }


    private boolean isPresent(int id){
        Optional<Person> person = Optional.ofNullable(
                jdbcTemplate.query(
                        "select * from person where id = ?",
                        new Object[]{id},
                        new BeanPropertyRowMapper<>(Person.class)
                ).stream().findAny().orElse(null));
        if (!person.isPresent()) return false;
        return true;
    }



//
//    @Transactional
//    public void save(Person person){
//        repo.save(person);

//    }
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Person> user = repo.findPersonByEmail(email); //пытаемся получить из БД человека с Таким Email-ом

        if(user.isEmpty()) {
            log.info("Попытка войти в систему под не зарегистрированной почтой {}", email);
            throw new UsernameNotFoundException("Пользователь с такой почтой не зарегистрирован");
        }
        return new PersonDetails(user.get());
    }
}
