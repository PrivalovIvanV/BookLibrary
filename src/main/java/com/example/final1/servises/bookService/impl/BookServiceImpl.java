package com.example.final1.servises.bookService.impl;




import com.example.final1.servises.bookService.api.BookNotFoundException;
import com.example.final1.servises.bookService.api.BookServiceExtended;
import com.example.final1.servises.bookService.impl.entity.Book;
import com.example.final1.servises.bookService.impl.repo.BookRepoApi;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BookServiceImpl implements BookServiceExtended {

    private final BookRepoApi bookRepo;
    private final JdbcTemplate jdbcTemplate;




    @Override
    public List<Book> findAll(String query) {
        return bookRepo.getBooksByQuery(query);
    }

    @Override
    public Book get(int book_id) {
        if (!isPresent(book_id)){
            throw new BookNotFoundException("Попытка получить не существующую книгу с id " + book_id);
        } else {
            return bookRepo.getBook(book_id);
        }
    }

    @Override
    public List<Book> getAllByPersonId(int person_id) {
        return jdbcTemplate.query("select * from book where person_id = ?",
                new Object[]{person_id},
                new BeanPropertyRowMapper<>(Book.class));

    }


    @Override
    @Transactional
    public void addOwner(int bookId, int personId){
        if (!isPresent(bookId)) {
            throw new BookNotFoundException("Ошибка добавления владельца книги: книги с id " + bookId + " не существует");
        } else {
            jdbcTemplate.update("update book set person_id = ?, taken_data = now(), is_Access = false where id = ?", personId, bookId);
        }
    }

    @Override
    public void removeOwner(int book_id) {
        if (!isPresent(book_id)){
            throw new BookNotFoundException("Ошибка удаления владельца у книги с ID " + book_id + " : такой книги не существует.");
        } else {
            jdbcTemplate.update("update book set person_id = null, taken_data = null, is_Access = true where id = ?", book_id);
        }
    }


    @Override
    public void create(Book book){
        bookRepo.saveBook(book);
    }

    @Override
    public void update(Book book) {
        //todo Метод для админки
    }

    @Override
    public void remove(int id) {
        if (!isPresent(id)){
            throw new BookNotFoundException("Ошибка удаления книги: книги с ID " + id + " не существует.");
        } else {
            bookRepo.remove(id);
        }
    }




    private boolean isPresent(int id){
        Optional<Book> book =
                jdbcTemplate.query(
                        "select * from book where id = ?",
                        new Object[]{id},
                        new BeanPropertyRowMapper<>(Book.class)
                ).stream().findAny();
        return book.isPresent();
    }
}
