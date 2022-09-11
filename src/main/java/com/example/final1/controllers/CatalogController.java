package com.example.final1.controllers;

import com.example.final1.servises.bookService.impl.entity.Book;
import com.example.final1.servises.personService.impl.entity.Person;
import com.example.final1.servises.bookService.impl.BookServiceImpl;
import com.example.final1.servises.personService.impl.PersonService;
import com.example.final1.servises.settingsService.api.SettingsService;
import com.example.final1.servises.settingsService.impl.entity.CatalogSettings;
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

    private final BookServiceImpl bookService;
    private final PersonService personSer;
    private final SettingsService settingsService;



    @GetMapping("/{id}")
    public String bookPage(@PathVariable("id") int id,
                           @RequestParam( name = "isCatalog", required = false) String isCatalog,
                           Model model){

        boolean isLibrary = true;
        boolean isBookOwnedByCurrentUser = false;
        Book bookResp =  bookService.findById(id);

        if (isPersonAuth()) isBookOwnedByCurrentUser = personSer.getCurrentUser().isOwnerThisBook(id);
        if (isCatalog == null) isLibrary = false;


        model.addAttribute("isLibrary", isLibrary);
        model.addAttribute("isBookOwnedByCurrentUser", isBookOwnedByCurrentUser);
        model.addAttribute("lastSearch", lastSearch());
        model.addAttribute("book", bookResp);
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

        CatalogSettings settings =
                new CatalogSettings(page, query, isAll, CS, FICTION, HISTORY, COMICS);
        settingsService.addSettings(settings);

        List<Book> unsortedList = bookService.findAll();
        List<List<Book>> listForPage = allocateListToPage(unsortedList);
        CatalogSettings catalogSettings =
                (CatalogSettings) settingsService.getSettingsByName("CatalogSettings");

        log.info("Из {} книг, получилось {} страниц", unsortedList.size(), listForPage.size());
        model.addAttribute("bookFilter", catalogSettings);
        model.addAttribute("currentPage", lastPage());
        model.addAttribute("searchVal", lastSearch());
        model.addAttribute("bookList", listForPage.get(lastPage()));
        model.addAttribute("PageIterator", PageIterator(listForPage.size()));
        return "book/BookCatalog";
    }







    @PostMapping("/{id}")
    public String addBookOwner(@PathVariable("id") int id, Model model){
        if (personSer.isAuth()) {
            log.warn("Попытка добавить книгу");
            bookService.addOwnerForBook(id, personSer.getCurrentUser().getId());
        }


        model.addAttribute("searchVal", lastSearch());
        model.addAttribute("book", bookService.findById(id));
        return "redirect:/catalog/" + id;
    }





    private List<List<Book>> allocateListToPage(List<Book> unsortedList){

        int size = unsortedList.size();
        int countOfPage = size/15;
        if (size%15 != 0) countOfPage++;
//        log.warn("Мы вычислили что из массива в {} элементов можно сделать {} страниц", size, countOfPage);

        //////Мы приготовили все и нам осталось только раскидать большое количество книг по 15 штук на страничку


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


    public List<Integer> PageIterator(int size){
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
        CatalogSettings catalogSettings =
                (CatalogSettings) settingsService.getSettingsByName("CatalogSettings");
        return catalogSettings.getLastPage();
    }

    private String lastSearch(){
        CatalogSettings catalogSettings =
                (CatalogSettings) settingsService.getSettingsByName("CatalogSettings");
        return catalogSettings.getLastSearch();
    }


}
