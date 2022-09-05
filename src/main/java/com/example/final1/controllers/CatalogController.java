package com.example.final1.controllers;

import com.example.final1.models.Book;
import com.example.final1.models.Person;
import com.example.final1.servises.BookService;
import com.example.final1.servises.PersonService;
import com.example.final1.util.BookFilter;
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
    private final BookFilter bookFilter;
    private final PersonService personSer;



    @GetMapping("/{id}")
    public String bookPage(@PathVariable("id") int id,
                           @RequestParam( name = "isCatalog", required = false) String isCatalog,
                           Model model){

        boolean isLibrary;
        boolean response;
        Book bookResp =  bookService.findById(id).get();

        if (isPersonAuth()) {
            response = personSer.getCurrentUser().isOwnerThisBook(id);
        }else response = false;
        if (isCatalog == null) {
            isLibrary = false;
        } else isLibrary = true;

        model.addAttribute("isLibrary", isLibrary);
        model.addAttribute("isBookOwnedByCurrentUser", response);
        model.addAttribute("lastSearch", lastSearch);
        model.addAttribute("book", bookResp);
        return "book/BookPage";
    }



    @GetMapping
    public String catalog(@RequestParam(name = "q", required = false) String qr,
                          @RequestParam(name = "page", required = false) Integer req,
                          @RequestParam(name = "CS", required = false) boolean CS,
                          @RequestParam(name = "fan", required = false) boolean FICTION,
                          @RequestParam(name = "hist", required = false) boolean HISTORY,
                          @RequestParam(name = "comics", required = false) boolean COMICS,
                          @RequestParam(name = "isAll", required = false) String isAll,
                          Model model){

        bookFilter.updateFilter(CS, FICTION, HISTORY, COMICS, isAll);
        // Этот блок создан, чтобы при запросе без параметров
        // система отработала бы хорошо
        if (req != null) lastPage = req.intValue();
        if (qr != null) lastSearch = qr;
        if (qr != null && req == null) lastPage = 0;     //Если у нас новый поисковой запрос, то lastPage сбрасывается
//        if (qr != null && isAll == null && req == null) bookFilter.clear();  //Если у нас новый поисковой запрос, то фильтры сбрасывается
        if (qr != null && isAll != null) lastSearch = "";
        if (qr == null && req == null && isAll != null) lastPage=0;


        List<Integer> pageIterator;
        List<Book> listBook = bookService.findAll(lastSearch, lastPage);
        if (bookFilter.isHaveAFilter()){
            pageIterator = PageIterator(bookService.findAllWithFilter(lastSearch));
        }else
            pageIterator = PageIterator(bookService.findAll(lastSearch));


        model.addAttribute("bookFilter", bookFilter);
        model.addAttribute("currentPage", lastPage);
        model.addAttribute("searchVal", lastSearch);
        model.addAttribute("bookList", listBook);
        model.addAttribute("PageIterator", pageIterator);
        return "book/BookCatalog";
    }







    @PostMapping("/{id}")
    public String addBookOwner(@PathVariable("id") int id, Model model){
        if (personSer.isAuth()) {
            log.warn("Попытка добавить книгу");
            bookService.addBookOwner(id, personSer.getCurrentUser().getId());
        }


        model.addAttribute("searchVal", lastSearch);
        model.addAttribute("book", bookService.findById(id).get());
        return "redirect:/catalog/" + id;
    }







    public List<Integer> PageIterator(List<Book> a){
        List<Integer> list = new ArrayList<>();
        log.warn("Длинна листа с книгами составила {}", a.size());
        int numOfPage = a.size()/15;
        if (a.size()%15 != 0) numOfPage++;
        for (int i = 0; i < numOfPage; i++){
            list.add(i);
        }
        return list;
    }//нужно, чтобы можно было по страничкам ходить

    private String lastSearch = "";
    private int lastPage = 0;


    @ModelAttribute(name = "isAuth")
    public boolean isPersonAuth(){ return personSer.isAuth();}

    @ModelAttribute(name = "AuthPerson")
    public Person getAuthPerson(){ return personSer.getCurrentUser();}




}
