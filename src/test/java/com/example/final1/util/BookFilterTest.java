package com.example.final1.util;

import com.example.final1.util.personalSettings.settings.BookFilter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class BookFilterTest {

    private BookFilter bookFilter;

    @Before
    public void setUp() throws Exception {
        bookFilter = new BookFilter();
        bookFilter.updateFilter(true, true, true, true, "allBook");
    }

    @Test
    public void addNewFilter() {
        Assert.assertTrue(bookFilter.isHaveAFilter());
    }

    @Test
    public void whenWeRefreshFilterIsHaveFilterShouldBeFalse() {
        bookFilter.updateFilter(false, false, false, false, "allBook");
        Assert.assertFalse(bookFilter.isHaveAFilter());
    }

    @Test
    public void setIsAllInFalse() {
        bookFilter.updateFilter(false, false, false, false, "al");
        Assert.assertTrue(bookFilter.isHaveAFilter());
        Assert.assertFalse(bookFilter.isAll());
    }

    @Test
    public void getEmptyFilterListWhenWeRefreshFilter() {
        List<String> filterList = new ArrayList<>();
        bookFilter.updateFilter(false, false, false, false, "al");

        Assert.assertEquals(0, bookFilter.getFilterList().size());
    }

    @Test
    public void getFullFilterListWhenWeRefreshFilter() {
        List<String> filterList = new ArrayList<>();
        bookFilter.updateFilter(false, true, true, false, "al");
        filterList.add("FICTION");
        filterList.add("HISTORY");

        Assert.assertEquals(2, bookFilter.getFilterList().size());
        Assert.assertEquals(filterList, bookFilter.getFilterList());
    }

//    @Test
//    public void isHaveAFilter() {
//    }
//
//    @Test
//    public void isAll() {
//    }todo delete hidden tests
}