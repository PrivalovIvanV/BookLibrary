//package com.example.final1.servises;
//
//import com.example.final1.servises.bookService.impl.entity.Book;
//import com.example.final1.servises.bookService.impl.repo.BookRepo;
//import com.example.final1.servises.bookService.impl.BookServiceImpl;
//import com.example.final1.servises.settingsService.impl.entity.CatalogSettings;
//import lombok.extern.slf4j.Slf4j;
//import org.junit.Assert;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Locale;
//
//@Slf4j
//@SpringBootTest
//@RunWith(SpringRunner.class)
//public class BookServiceImplTest {
//
//    @Autowired
//    BookServiceImpl bookServiceImpl;
//    @Autowired
//    BookRepo bookRepo;
//    @MockBean
//    CatalogSettings catalogSettings;
//
//
//    @Before
//    public void setUp() throws Exception {
//    }
//
//    @Test
//    public void findAll() {
//        int trueSize = bookRepo.findAll().size();
//        Assert.assertEquals(trueSize, bookServiceImpl.findAll().size());
//    }
//
//    @Test
//    public void findByQuery() {
//        List<Book> testList = bookServiceImpl.findAll("пуш");
//        Assert.assertTrue(testList.size() != 0);
//        //наша библиотека специализируется на класической литертуре, поэтому Пушкин есть всегда)
//        testList.forEach(book -> {
//            if (!(book.getAuthor().toLowerCase(Locale.ROOT).contains("пуш")
//                    ||
//                book.getTitle().toLowerCase(Locale.ROOT).contains("пуш"))
//            ){
//                Assert.assertTrue(false);
//                log.error("При попытке запроса книг по ключевому слову, в списке оказалось слово не подходящее {} {}", book.getAuthor(), book.getTitle());
//            }//Если в нашем листе мы найдем хоть один не подходящий вариант, то выкидываем его
//        });
//        Assert.assertTrue(true);
//    }
//
//    @Test
//    public void countOfPageShouldBe15WithoutFilters() {
//        int size = bookServiceImpl.findAll("а",0).size();
//        Assert.assertEquals(15, size);
//
//    }
//
//    @Test
//    public void countOfPageShouldBe15WithFilters() {
//        List<String> filterList = new ArrayList<>();
//        filterList.add("FICTION");
//        filterList.add("HISTORY");
//        filterList.add("CS");
//
//        Mockito.when(catalogSettings.isHaveAFilter()).thenReturn(true);
//        Mockito.when(catalogSettings.getFilterList()).thenReturn(filterList);
//
//        int size = bookServiceImpl.findAll("", 0).size();
//
//        Assert.assertEquals(15, size);
//
//    }
//
//    @Test
//    public void when_we_request_unknown_page_return() {
//        int size = bookServiceImpl.findAll("", 1000000000).size();
//        Assert.assertEquals(0, size);
//
//    }
//
//}
