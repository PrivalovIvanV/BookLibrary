package com.example.final1.servises;

import com.example.final1.models.Book;
import com.example.final1.repositories.BookRepo;
import com.example.final1.util.BookFilter;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.junit.Assert.*;

@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
public class BookServiceTest {

    @Autowired
    BookService bookService;
    @Autowired
    BookRepo bookRepo;
    @MockBean
    BookFilter bookFilter;


    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void findAll() {
        int trueSize = bookRepo.findAll().size();
        Assert.assertEquals(trueSize, bookService.findAll().size());
    }

    @Test
    public void findByQuery() {
        List<Book> testList = bookService.findAll("пуш");
        Assert.assertTrue(testList.size() != 0);
        //наша библиотека специализируется на класической литертуре, поэтому Пушкин есть всегда)
        testList.forEach(book -> {
            if (!(book.getAuthor().toLowerCase(Locale.ROOT).contains("пуш")
                    ||
                book.getTitle().toLowerCase(Locale.ROOT).contains("пуш"))
            ){
                Assert.assertTrue(false);
                log.error("При попытке запроса книг по ключевому слову, в списке оказалось слово не подходящее {} {}", book.getAuthor(), book.getTitle());
            }//Если в нашем листе мы найдем хоть один не подходящий вариант, то выкидываем его
        });
        Assert.assertTrue(true);
    }

    @Test
    public void countOfPageShouldBe15WithoutFilters() {
        int size = bookService.findAll("а",0).size();
        Assert.assertEquals(15, size);

    }

    @Test
    public void countOfPageShouldBe15WithFilters() {
        List<String> filterList = new ArrayList<>();
        filterList.add("FICTION");
        filterList.add("HISTORY");
        filterList.add("CS");

        Mockito.when(bookFilter.isHaveAFilter()).thenReturn(true);
        Mockito.when(bookFilter.getFilterList()).thenReturn(filterList);

        int size = bookService.findAll("", 0).size();

        Assert.assertEquals(15, size);

    }

    @Test
    public void when_we_request_unknown_page_return() {
        int size = bookService.findAll("", 1000000000).size();
        Assert.assertEquals(0, size);

    }

}
