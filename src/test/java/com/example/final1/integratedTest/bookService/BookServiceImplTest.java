package com.example.final1.integratedTest.bookService;

import com.example.final1.servises.bookService.api.BookNotFoundException;
import com.example.final1.servises.bookService.impl.BookServiceImpl;
import com.example.final1.servises.bookService.impl.entity.Book;
import com.example.final1.servises.personService.impl.entity.Person;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;


@SpringBootTest
@ActiveProfiles("test")
class BookServiceImplTest{

    @Autowired BookServiceImpl bookService;

    ///////////////////////////////////////////////////////
    ////////
    ////////    Тут используется не основная база данных
    ////////    в ней 62 книги из которых 6 принадлежат
    ////////    пользователю с id 1
    ////////
    ///////////////////////////////////////////////////////

    @Test
    void should_return_3() {
        List<Book> bookList = bookService.findAll("пе");

        Assert.assertEquals(bookList.size(), 3);

    }
    @Test
    void should_return_62() {
        List<Book> bookList = bookService.findAll("");

        Assert.assertEquals(bookList.size(), 62);

    }

    @Test
    void should_throw_exception(){
        assertAll(
                () -> Assert.assertThrows(BookNotFoundException.class, () -> bookService.get(-1)),
                () -> Assert.assertThrows(BookNotFoundException.class, () -> bookService.get(1000000))
        );
    }
    @Test
    void getAssertBook(){
        Book book = bookService.get(3);

        Assert.assertEquals(book.getId(), 3);
    }

    @Test
    void whenGetBooksByPersonId_should_return_7(){
        List<Book> bookList = bookService.getAllByPersonId(1);

        Assert.assertEquals(bookList.size(), 6);
    }

    @Test
    void whenGetBooksByPersonId_should_return_EmptyList(){
        List<Book> bookList1 = bookService.getAllByPersonId(-1);
        List<Book> bookList2 = bookService.getAllByPersonId(10000);

        assertAll(
                () -> Assert.assertTrue(bookList1.isEmpty()),
                () -> Assert.assertTrue(bookList2.isEmpty())
        );
    }

    @Test
    void testAddOwnerWithId_5(){
        bookService.addOwner(10, 5);

        Book book = bookService.get(10);

        assertAll(
                () -> Assert.assertEquals(book.getOwner().getId(), 5),
                () -> Assert.assertFalse(book.isAccess())
        );

        bookService.removeOwner(10);
    }

    @Test
    void addOwner_For_nonexistentBook(){
        assertAll(
                () -> Assert.assertThrows(BookNotFoundException.class, () -> bookService.addOwner(-1, 5)),
                () -> Assert.assertThrows(BookNotFoundException.class, () -> bookService.addOwner(1000000, 5))
        );
    }

    @Test
    void test_remove_owner_for_book_with_id_3(){
        bookService.removeOwner(3);

        Book book = bookService.get(3);

        assertAll(
                () -> Assert.assertNull(book.getOwner()),
                () -> Assert.assertTrue(book.isAccess())
        );

        bookService.addOwner(3, 1);
    }

    @Test
    void remove_Owner_For_nonexistentBook(){
        assertAll(
                () -> Assert.assertThrows(BookNotFoundException.class, () -> bookService.remove(-1)),
                () -> Assert.assertThrows(BookNotFoundException.class, () -> bookService.remove(1000000))
        );
    }


}