package com.example.final1.controllers;


import com.example.final1.servises.bookService.api.BookService;
import com.example.final1.servises.bookService.impl.entity.Book;
import com.example.final1.servises.personService.impl.entity.Person;
import com.example.final1.servises.personService.impl.PersonServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("")
@RequiredArgsConstructor
public class PersonController {

    private final PersonServiceImpl personSer;
    private final BookService bookService;



    @GetMapping()
    public String home(){
        log.info(SecurityContextHolder.getContext().getAuthentication().getDetails().toString());
        return "permitAll/home";}

    @GetMapping("/account")
    public String getAccountInfo(){ return "person/account";}

    @GetMapping("/account/edit")
    public String AccountEdit (Model model){
        Person bufForClone = personSer.getCurrentUser();
        Person updatedPerson = new Person(bufForClone);
        model.addAttribute("currentUser", updatedPerson);
        return "person/edit";}


    @GetMapping("account/edit/deleteAvatar")
    public String deleteAvatar(Model model){
        Person bufForClone = personSer.getCurrentUser();
        Person updatedPerson = new Person(bufForClone);
        model.addAttribute("currentUser", updatedPerson);
        personSer.deleteAvatar();
        return "person/edit";
    }


    @PostMapping("/account/edit")
    public String updatePerson (@ModelAttribute("currentUser") @Valid Person person,
                                BindingResult bindingResult,
                                @RequestParam("file1") MultipartFile file1){

        if (bindingResult.hasErrors()){return "/person/edit";}

        personSer.addAvatar(file1);
        personSer.UpdatePerson(person);
        log.info("Попытка обновить данные человека с почтой {}", person.getEmail());
        return "redirect:/account";}





    ///////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////Все, что связанно с книгами////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////



    @GetMapping("/myLibrary")
    public String catalog(Model model){
        int personId = personSer.getCurrentUser().getId();
        List<Book> bookList = bookService.findBooksByPersonId(personId);
        model.addAttribute("bookList", bookList);
        return "book/myLibrary";
    }



    @ModelAttribute(name = "isAuth")
    public boolean isAuth(){
        return personSer.isAuth();
    }

    @ModelAttribute(name = "AuthPerson")
    public Person getAuthPerson(){
        return personSer.getCurrentUser();
    }



}
