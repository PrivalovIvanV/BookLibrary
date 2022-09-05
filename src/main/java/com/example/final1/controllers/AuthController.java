package com.example.final1.controllers;

import com.example.final1.models.Person;
import com.example.final1.servises.RegistrationService;
import com.example.final1.util.PersonValidate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {


    private final PersonValidate validate;
    private final RegistrationService registrationService;



    @GetMapping("/login")
    public String login(){
        System.out.println("человек зашел на сайт");
        return "auth/login";}






    ///////////////////////////////////////
    /////////////Регистрация///////////////
    ///////////////////////////////////////

    @GetMapping("/registration")
    public String registrationPage(Model model){
        model.addAttribute("new_person", new Person());
        return "auth/registration";
    }


    @PostMapping("/registration")
    public String reg(@ModelAttribute("new_person") @Valid Person person,
                      BindingResult bindingResult){
        validate.validate(person, bindingResult);       //Валидирую на наличие коллизии по Email
        if (bindingResult.hasErrors()){
            return "auth/registration";
        }

        registrationService.registrationNewPerson(person); //Если все ок, то добавляем в базу
        return "redirect:/auth/login";
    }


}
