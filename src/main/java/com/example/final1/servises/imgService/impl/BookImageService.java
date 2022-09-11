package com.example.final1.servises.imgService.impl;

import com.example.final1.servises.imgService.impl.entity.BookImage;
import com.example.final1.servises.imgService.impl.repo.BookImageRepository;
import com.example.final1.servises.bookService.impl.entity.Book;
import com.example.final1.servises.bookService.impl.BookServiceImpl;
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
    private final BookServiceImpl bookServiceImpl;



    @Transactional
    public void saveBookImage(BookImage bookImage, int book_id){
        Book book = bookServiceImpl.findById(book_id);
        book.setBookImage(bookImage);
        bookImage.setBook(book);

        bookServiceImpl.save(book);
        bookRepository.save(bookImage);
    }


    public BookImage getImageByBookId(int book_id){
        BookImage image = jdbcTemplate.query("select * from book_images where book_id = ?",
                new Object[]{book_id},
                new BeanPropertyRowMapper<>(BookImage.class)
        ).stream().findAny().orElse(null);
        return image;
    }
}
