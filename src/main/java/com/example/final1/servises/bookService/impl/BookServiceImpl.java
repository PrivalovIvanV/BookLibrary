package com.example.final1.servises.bookService.impl;




import com.example.final1.servises.bookService.impl.entity.Book;
import com.example.final1.servises.bookService.impl.repo.BookRepoApi;
import com.example.final1.servises.personService.impl.entity.Person;
import com.example.final1.servises.personService.impl.PersonServiceImpl;
import com.example.final1.servises.settingsService.SettingsServiceImpl;
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
public class BookServiceImpl {

    private final JdbcTemplate jdbcTemplate;
    private final SettingsServiceImpl settingsServiceImpl;
    private final BookRepoApi bookRepo;
    private final PersonServiceImpl personService;


    @SneakyThrows
    public List<Book> findAll(){

        SettingsForCatalog filter =
                settingsServiceImpl.getSettings(SettingsForCatalog.class);
        int page = filter.getLastPage();
        String query = filter.getLastSearch();

        if (filter.isHaveAFilter()){
            return findAllWithFilter(query);
        }else
            return bookRepo.getBooksByQuery(query);
    }




    public List<Book> findBooksByPersonId(int id){
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


    @Transactional
    public void addOwnerForBook(int bookId, int personId){
        Person owner = personService.getCurrentUser();
        Book addedBook = bookRepo.getBook(bookId);

        if (addedBook.isAccess()){
            owner.addBook(addedBook);
            addedBook.setOwner(owner);

            bookRepo.saveBook(addedBook);
            personService.save(owner);
        }


    }



    @SneakyThrows
    private List<Book> findAllWithFilter(String query){
        List<Book> responseList = new ArrayList<>();
        List<Book> untreatedList = bookRepo.getBooksByQuery(query);
        SettingsForCatalog catalogPageSettings = settingsServiceImpl.getSettings(SettingsForCatalog.class);
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
