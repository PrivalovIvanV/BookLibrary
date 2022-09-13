package com.example.final1.controllers;

import com.example.final1.servises.bookService.api.BookService;
import com.example.final1.servises.bookService.impl.entity.Book;
import com.example.final1.servises.personService.api.UserNotAuthException;
import com.example.final1.servises.personService.impl.entity.Person;
import com.example.final1.servises.personService.impl.PersonServiceImpl;
import com.example.final1.servises.settingsService.api.SettingsService;
import com.example.final1.servises.settingsService.impl.entity.SettingsForCatalog;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/catalog")
@RequiredArgsConstructor
public class CatalogController {

    private final BookService bookService;
    private final PersonServiceImpl personSer;
    private final SettingsService settingsService;



    @GetMapping("/{id}")
    public String bookPage(@PathVariable("id") int id,
                           @RequestParam( name = "isCatalog", required = false) String isCatalog,
                           Model model){
        boolean isLibrary = true;
        boolean isBookOwnedByCurrentUser;
        Book book =  bookService.findById(id);

        try {
            isBookOwnedByCurrentUser = personSer.getCurrentUser().isOwner(id);
        } catch (UserNotAuthException e) {
            isBookOwnedByCurrentUser = false;
        }
        if (isCatalog == null) isLibrary = false;


        model.addAttribute("isLibrary", isLibrary);
        model.addAttribute("isBookOwnedByCurrentUser", isBookOwnedByCurrentUser);
        model.addAttribute("lastSearch", lastSearch());
        model.addAttribute("book", book);
        return "book/BookPage";
    }



    @GetMapping
    public String catalog(@RequestParam(name = "q", required = false) String query,
                          @RequestParam(name = "page", required = false) Integer page,
                          @RequestParam(name = "CS", required = false) boolean CS,
                          @RequestParam(name = "fan", required = false) boolean FICTION,
                          @RequestParam(name = "hist", required = false) boolean HISTORY,
                          @RequestParam(name = "comics", required = false) boolean COMICS,
                          @RequestParam(name = "isAll", required = false) String isAll,
                          Model model){

        SettingsForCatalog settings = new SettingsForCatalog(page, query, isAll, CS, FICTION, HISTORY, COMICS);
        settingsService.addSettings(settings);

        SettingsForCatalog settingsForCatalog = settingsService.getSettings(SettingsForCatalog.class);
        List<Book> unsortedListWithBook = bookService.findAll();
        List<List<Book>> pagesList = allocateListToPage(unsortedListWithBook);

        //пробуем отправить лист с книгами
        try{
            model.addAttribute("bookList", pagesList.get(lastPage()));
        } catch (IndexOutOfBoundsException e){
            model.addAttribute("bookList", new ArrayList<>());

        }
        model.addAttribute("bookFilter", settingsForCatalog);
        model.addAttribute("currentPage", lastPage());
        model.addAttribute("searchVal", lastSearch());
        model.addAttribute("PageIterator", PageIterator(pagesList.size()));
        return "book/BookCatalog";
    }







    @PostMapping("/{id}")
    public String addBookOwner(@PathVariable("id") int id, Model model){

        try {
            bookService.addOwnerForBook(id, personSer.getCurrentUser().getId());
        } catch (UserNotAuthException e) {
            log.warn("Неавторизированный пользователь пытается добавить книгу");
        }


        model.addAttribute("searchVal", lastSearch());
        model.addAttribute("book", bookService.findById(id));
        return "redirect:/catalog/" + id;
    }





    private List<List<Book>> allocateListToPage(List<Book> unsortedList){

        int size = unsortedList.size();
        int countOfPage = size/15;
        if (size%15 != 0) countOfPage++;

        List<List<Book>> listWithPage = new ArrayList<>();
        for(int i = 0; i < countOfPage; i++){
            if (i == ( countOfPage - 1)) {
                listWithPage.add( unsortedList.subList(i * 15, size) );
            } else {
                listWithPage.add( unsortedList.subList(i * 15, (i + 1) * 15) );
            }

        }
        return listWithPage;
    }


    private List<Integer> PageIterator(int size){
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < size; i++){
            list.add(i);
        }
        return list;
    }//нужно, чтобы можно было по страничкам ходить



    @ModelAttribute(name = "isAuth")
    public boolean isPersonAuth(){
        return personSer.isAuth();
    }

    @ModelAttribute(name = "AuthPerson")
    public Person getAuthPerson(){
        return personSer.getCurrentUser();
    }

    private int lastPage(){
        if (settingsService.isPresent(SettingsForCatalog.class)){
            SettingsForCatalog settingsForCatalog = settingsService.getSettings(SettingsForCatalog.class);
            return settingsForCatalog.getLastPage();
        } else return 0;
    }

    private String lastSearch(){
        if ( settingsService.isPresent(SettingsForCatalog.class) ){
            SettingsForCatalog settingsForCatalog = settingsService.getSettings(SettingsForCatalog.class);
            return settingsForCatalog.getLastSearch();
        } else return "";
    }


}
