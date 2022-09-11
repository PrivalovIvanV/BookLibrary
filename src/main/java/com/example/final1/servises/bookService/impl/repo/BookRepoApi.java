package com.example.final1.servises.bookService.impl.repo;

import com.example.final1.servises.bookService.impl.entity.Book;
import com.example.final1.servises.bookService.impl.util.BookNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class BookRepoApi {
    private final BookRepo bookRepo;

    public List<Book> getBooksByQuery(String q){
        return bookRepo.findByTitleContainsIgnoreCaseOrAuthorContainsIgnoreCase(q, q);
    }


    public List<Book> findAll(){
        return bookRepo.findAll();
    }

    public void saveBook(Book book){
        bookRepo.save(book);
    }

    public Book getBook(int id){
        Optional<Book> book = bookRepo.getBookById(id);
        if (!book.isPresent()) throw new BookNotFoundException();
        return book.get();
    }
}
