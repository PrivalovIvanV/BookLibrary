package com.example.final1.servises.bookService.impl;




import com.example.final1.servises.bookService.api.BookService;
import com.example.final1.servises.bookService.impl.entity.Book;
import com.example.final1.servises.bookService.impl.repo.BookRepoApi;
import com.example.final1.servises.personService.api.UserNotAuthException;
import com.example.final1.servises.personService.impl.entity.Person;
import com.example.final1.servises.personService.impl.PersonServiceImpl;
import com.example.final1.servises.settingsService.api.SettingsService;
import com.example.final1.servises.settingsService.impl.entity.SettingsForCatalog;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepoApi bookRepo;
    private final JdbcTemplate jdbcTemplate;
    private final SettingsService settingsService;
    private final PersonServiceImpl personService;


    @SneakyThrows
    public List<Book> findAll(){

        SettingsForCatalog filter =
                settingsService.getSettings(SettingsForCatalog.class);
        String query = filter.getLastSearch();

        if (filter.isHaveAFilter()){
            return sorted(query);
        }else
            return bookRepo.getBooksByQuery(query);
    }


    @Override
    @Transactional
    public void addOwner(int bookId, int personId){
        Person owner = null;
        try {
            owner = personService.getCurrentUser();
        } catch (UserNotAuthException e) {
            throw new RuntimeException(e);
        }
        Book addedBook = bookRepo.getBook(bookId);

        if (addedBook.isAccess()){
            owner.addBook(addedBook);
            addedBook.setOwner(owner);

            bookRepo.saveBook(addedBook);
            personService.save(owner);
        }
    }
    public List<Book> findByPersonId(int id){
        int idCurrentUser = id;
        return jdbcTemplate.query("select * from book where person_id = ?",
                new Object[]{idCurrentUser},
                new BeanPropertyRowMapper<>(Book.class));

    }

    public Book findById(int id){
        return bookRepo.getBook(id);
    }

    public void save(Book book){
        bookRepo.saveBook(book);
    }




    @SneakyThrows
    private List<Book> sorted(String query){
        List<Book> responseList = new ArrayList<>();
        List<Book> untreatedList = bookRepo.getBooksByQuery(query);
        SettingsForCatalog catalogPageSettings = settingsService.getSettings(SettingsForCatalog.class);
        List<String> filterList = catalogPageSettings.getContent();
        if (filterList.size() != 0) {
            for (String filter : filterList) {
                for (Book book : untreatedList){
                    if (book.getBook_genres().equals(filter))
                        responseList.add(book);
                }

            }
        } else responseList = untreatedList;

        if (!catalogPageSettings.isAll()){
            responseList = responseList.stream().filter(book -> book.isAccess()).collect(Collectors.toList());
        }
        log.info("Количество книг до и после сортировки сортировки: {} и {}, isAll = {}",
                untreatedList.size(), responseList.size(), catalogPageSettings.isAll());

        /////////////////////////////////////////////////////////////////////////////////////////////////////
        ///////////На этом моменте мы закончили с сортировкой всех книг по нужным нам фильтрам///////////////
        /////////////////////////////////////////////////////////////////////////////////////////////////////

        return responseList;
    }
}
