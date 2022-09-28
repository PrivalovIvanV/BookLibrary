package com.example.final1.servises.userService;

import com.example.final1.servises.bookService.impl.entity.Book;
import com.example.final1.servises.personService.impl.entity.Person;
import com.example.final1.servises.settingsService.impl.entity.CatalogSettings;
import com.example.final1.servises.userService.api.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping()
    public String home() {
        return "permitAll/home";
    }


    @GetMapping("/catalog/{id}")
    public String bookPage(@PathVariable("id") int id,
                           @RequestParam(name = "isCatalog", required = false) String isCatalog,
                           Model model) {
        Book book = userService.getBook(id);
        boolean isLibrary = true;
        boolean isBookOwnedByCurrentUser = userService.isOwner(id);

        if (isCatalog == null) isLibrary = false;

        model.addAttribute("isLibrary", isLibrary);
        model.addAttribute("isBookOwnedByCurrentUser", isBookOwnedByCurrentUser);
        model.addAttribute("lastSearch", lastSearch());
        model.addAttribute("book", book);
        return "book/BookPage";
    }


    @GetMapping("/catalog")
    public String catalog(@RequestParam(name = "q", required = false) String query,
                          @RequestParam(name = "page", required = false) Integer page,
                          @RequestParam(name = "CS", required = false) boolean CS,
                          @RequestParam(name = "fan", required = false) boolean FICTION,
                          @RequestParam(name = "hist", required = false) boolean HISTORY,
                          @RequestParam(name = "comics", required = false) boolean COMICS,
                          @RequestParam(name = "isAll", required = false) String isAll,
                          Model model) {

        CatalogSettings settings = new CatalogSettings(page, query, isAll, CS, FICTION, HISTORY, COMICS);
        userService.addSettings(settings);

        userService.getSettings(CatalogSettings.class);
        List<Book> unsortedListWithBook = userService.getAll();
        List<List<Book>> pagesList = allocateListToPage(unsortedListWithBook);

        //пробуем отправить лист с книгами
        try {
            model.addAttribute("bookList", pagesList.get(lastPage()));
        } catch (IndexOutOfBoundsException e) {
            model.addAttribute("bookList", new ArrayList<>());

        }
        model.addAttribute("bookFilter", userService.getSettings(CatalogSettings.class));
        model.addAttribute("currentPage", lastPage());
        model.addAttribute("searchVal", lastSearch());
        model.addAttribute("PageIterator", PageIterator(pagesList.size()));
        return "book/BookCatalog";
    }


    @PostMapping("/catalog/{id}")
    public String addBookOwner(@PathVariable("id") int id, Model model) {
        userService.addOwner(id);
        model.addAttribute("searchVal", lastSearch());
        model.addAttribute("book", userService.getBook(id));
        return "redirect:/catalog/" + id;
    }


    ////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////Доступно только после авторизации///////////////////
    ////////////////////////////////////////////////////////////////////////////////////
    @GetMapping("/account")
    public String getAccountInfo() {
        return "person/account";
    }

    @GetMapping("/account/edit")
    public String AccountEdit(Model model) {
        Person bufForClone = userService.getUser();
        Person updatedPerson = new Person(bufForClone);
        model.addAttribute("currentUser", updatedPerson);
        return "person/edit";
    }


    @GetMapping("account/edit/deleteAvatar")
    public String deleteAvatar() {
        userService.removeAvatar();
        return "person/edit";
    }

    @GetMapping("/myLibrary")
    public String catalog(Model model) {
        List<Book> bookList = userService.getUserLibrary();
        model.addAttribute("bookList", bookList);
        return "book/myLibrary";
    }

    @PostMapping("/account/edit")
    public String updatePerson(@ModelAttribute("currentUser") @Valid Person person,
                               BindingResult bindingResult,
                               @RequestParam("file1") MultipartFile file1) {

        if (bindingResult.hasErrors()) {
            return "/person/edit";
        }
        userService.addAvatar(file1);
        log.info("Пользователь с почтой {} обновил данные.", person.getEmail());
        return "redirect:/account";
    }


    @ModelAttribute(name = "isAuth")
    public boolean isPersonAuth() {
        return userService.isAuth();
    }

    @ModelAttribute(name = "AuthPerson")
    public Person getAuthPerson() {
        return userService.getUser();
    }


    private List<List<Book>> allocateListToPage(List<Book> unsortedList) {

        int size = unsortedList.size();
        int countOfPage = size / 15;
        if (size % 15 != 0) countOfPage++;

        List<List<Book>> listWithPage = new ArrayList<>();
        for (int i = 0; i < countOfPage; i++) {
            if (i == (countOfPage - 1)) {
                listWithPage.add(unsortedList.subList(i * 15, size));
            } else {
                listWithPage.add(unsortedList.subList(i * 15, (i + 1) * 15));
            }
        }
        return listWithPage;
    }

    private List<Integer> PageIterator(int size) {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            list.add(i);
        }
        return list;
    }//нужно, чтобы можно было по страничкам ходить


    private int lastPage() {
        if (userService.isPresent(CatalogSettings.class)) {
            CatalogSettings catalogSettings = userService.getSettings(CatalogSettings.class);
            return catalogSettings.getLastPage();
        } else return 0;
    }

    private String lastSearch() {
        if (userService.isPresent(CatalogSettings.class)) {
            CatalogSettings catalogSettings = userService.getSettings(CatalogSettings.class);
            return catalogSettings.getLastSearch();
        } else return "";
    }

}
