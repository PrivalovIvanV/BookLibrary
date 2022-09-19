package com.example.final1.utitTests.entity;

import com.example.final1.servises.bookService.impl.entity.Book;
import com.example.final1.servises.personService.impl.entity.Person;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Date;

class BookTest {

    Book book;

    @BeforeEach
    void setUp() {
        book = new Book();
    }

    @Test
    void intervalDate_should_be_active(){
        book.setTaken_data(new Date(System.currentTimeMillis()));
        boolean answer = book.isOverdue();

        Assert.assertFalse(answer);
    }

    @Test
    void bookShould_be_overdue() {
        book.setTaken_data(new Date(System.currentTimeMillis() - 1728000000));
        boolean answer = book.isOverdue();

        Assert.assertTrue(answer);
    }

    @Test
    void bookShould_be_false() {
        Assert.assertFalse(book.isOverdue());
    }

    @Test
    void interval_date_should_be_10() {
        book.setTaken_data(new Date(System.currentTimeMillis() - 860000000));

        Assert.assertEquals(book.intervalDate(), 10);
    }

    @Test
    void interval_date_return_null() {
        Assert.assertEquals(book.intervalDate(), 0);
    }

    @Test
    void date_of_return_should_return_10() {
        book.setTaken_data(new Date(System.currentTimeMillis() - 860000000));
        Date dateOfReturn = new Date(book.getTaken_data().getTime() + 60000*60*24*15);

        Assert.assertEquals(book.dateOfReturn(), dateOfReturn);
    }

    @Test
    void date_of_return_should_return_1() {
        Assert.assertNull(book.dateOfReturn());
    }

    @Test
    void setOwner() {
        book.setOwner(new Person());
        Assert.assertFalse(book.isAccess());
    }
}