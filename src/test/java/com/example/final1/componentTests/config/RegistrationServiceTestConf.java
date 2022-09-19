package com.example.final1.componentTests.config;

import com.example.final1.componentTests.AbstractClassForTest;
import com.example.final1.security.RegistrationService;
import com.example.final1.servises.personService.impl.repo.PersonRepo;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.PostConstruct;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@TestConfiguration
public class RegistrationServiceTestConf extends AbstractClassForTest {

    @MockBean PasswordEncoder encoder;
    @MockBean PersonRepo personRepo;

    @Bean
    public RegistrationService registrationService(){
        return new RegistrationService(personRepo, encoder);
    }

    @PostConstruct
    public void mock(){
        when(encoder.encode(anyString())).thenReturn("password");
    }
}
