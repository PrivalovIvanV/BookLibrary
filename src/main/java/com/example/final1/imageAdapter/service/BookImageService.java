package com.example.final1.imageAdapter.service;

import com.example.final1.imageAdapter.imageModels.BookImage;
import com.example.final1.imageAdapter.imageModels.PersonImage;
import com.example.final1.imageAdapter.repositories.BookImageRepository;
import com.example.final1.models.Book;
import com.example.final1.servises.BookService;
import com.example.final1.servises.PersonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Slf4j
@Service
@RequiredArgsConstructor
public class BookImageService {

    private final BookImageRepository bookRepository;
    private final JdbcTemplate jdbcTemplate;
    private final BookService bookService;



    @Transactional
    public void saveBookImage(BookImage bookImage, int book_id){
        Optional<Book> optionalBook = bookService.findById(book_id);
        Book originalBook;
        if (optionalBook.isPresent()){
            originalBook = optionalBook.get();
            originalBook.setBookImage(bookImage);
            bookImage.setBook(originalBook);
            bookService.save(originalBook);
        }
        bookRepository.save(bookImage);}


    public BookImage getImageByBookId(int book_id){
        BookImage image = jdbcTemplate.query("select * from book_images where book_id = ?",
                new Object[]{book_id},
                new BeanPropertyRowMapper<>(BookImage.class)
        ).stream().findAny().orElse(null);
        return image;
    }
}
