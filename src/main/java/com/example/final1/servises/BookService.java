package com.example.final1.servises;




import com.example.final1.models.Book;
import com.example.final1.models.Person;
import com.example.final1.repositories.BookRepo;
import com.example.final1.util.BookFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BookService {

    private final JdbcTemplate jdbcTemplate;
    private final BookFilter bookFilter;
    private final BookRepo bookRepo;
    private final PersonService personService;

    public List<Book> findAll(){ return bookRepo.findAll();}
    public List<Book> findAll(String q){ return bookRepo.findByTitleContainsIgnoreCaseOrAuthorContainsIgnoreCase(q, q);}
    public List<Book> findAll(String q, int page){

        if (bookFilter.isHaveAFilter()){
            List<Book> unSortedList = findAllWithFilter(q);

            int size = unSortedList.size();
            int countOfPage = size/15;
            if (size%15 != 0) countOfPage++;
            log.warn("Мы вычислили что из массива в {} элементов можно сделать {} страниц", size, countOfPage);

            //////Мы приготовили все и нам остлось только раскидать большое количество книг по 15 штук на страничку

            //List<List<Book>> containerWithPages = new ArrayList<>();
            if (page >= countOfPage) return new ArrayList<>();//если пользователь запросит не возможную страницу, то мы эту ошибку перехватим

            //List<Book> finalList = new ArrayList<>();
            if (page == ( countOfPage - 1)) {
                return unSortedList.subList(page * 15, size);
            }else return unSortedList.subList(page*15, ( page + 1 ) * 15 - 1);

        }else

        return bookRepo.findAllByTitleContainsIgnoreCaseOrAuthorContainsIgnoreCase(q, q, PageRequest.of(page, 15)).getContent();
    }
    public List<Book> findAllForPerson(int id){
        int idCurrentUser = id;
        return jdbcTemplate.query("select * from book where person_id = ?",
                new Object[]{idCurrentUser},
                new BeanPropertyRowMapper<>(Book.class));

    }
    public Optional<Book> findById(int id){ return bookRepo.findById(id);}

    public void save(Book book){bookRepo.save(book);}


    @Transactional
    public void addBookOwner(int bookId, int personId){
        Person person = personService.getCurrentUser();
        Optional<Book> addedBook = bookRepo.findById(bookId);

        if (addedBook.isPresent() && addedBook.get().isAccess()){
            Book currentBook = addedBook.get();
            person.addBook(addedBook.get());

            currentBook.setOwner(person);
            currentBook.setAccess(false);
            currentBook.setTaken_data(new Date(System.currentTimeMillis()));

            bookRepo.save(addedBook.get());
            personService.save(person);
        }


    }



    public List<Book> findAllWithFilter(String q){
        List<Book> finalList = new ArrayList<>();
        List<Book> untreatedList = bookRepo.findByTitleContainsIgnoreCaseOrAuthorContainsIgnoreCase(q, q);
        List<String> filterList = bookFilter.getFilterList();
        log.warn("Количество книг до сортировки: {}", untreatedList.size());
        if (filterList.size() != 0) {
            for (String filter : filterList) {
                for (Book book : untreatedList){
                    if (book.getBook_genres().equals(filter))
                        finalList.add(book);
                }

            }
        } else finalList = untreatedList;
        log.warn("Количество книг после первого цикла сортировки: {}", finalList.size());

        if (!bookFilter.isAll()){
            finalList = finalList.stream().filter(book -> book.isAccess()).collect(Collectors.toList());
        }
        log.warn("Количество книг после второго цикла сортировки: {}, isAll = {}", finalList.size(), bookFilter.isAll());
        /////////////////////////////////////////////////////////////////////////////////////////////////////
        ///////////На этом моменте мы закончили с сортировкой всех книг по нужным нам фильтрам///////////////
        /////////////////////////////////////////////////////////////////////////////////////////////////////

        return finalList;
    }
}
