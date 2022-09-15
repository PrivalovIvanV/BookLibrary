package com.example.final1.util;


import com.example.final1.servises.personService.impl.entity.Person;
import com.example.final1.security.RegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


@Component
@RequiredArgsConstructor
public class PersonValidate implements Validator {

    private final RegistrationService registrationService;


    @Override
    public void validate(Object target, Errors errors) {        //Проверяем чтобы не создать два аккаунта на один и тот же Email
        Person person = (Person) target;
        if (!registrationService.isExist(person.getEmail()))
            return;
        errors.rejectValue("email", "", "Человек с такой почтой уже зарегистрирован");
    }



    @Override
    public boolean supports(Class<?> clazz) {
        return Person.class.equals(clazz);
    }
}
