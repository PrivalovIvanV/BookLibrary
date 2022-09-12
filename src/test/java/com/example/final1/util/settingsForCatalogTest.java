package com.example.final1.util;

import com.example.final1.servises.settingsService.impl.entity.SettingsForCatalog;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class settingsForCatalogTest {

    private SettingsForCatalog settingsForCatalog;

    @Before
    public void setUp() throws Exception {
        settingsForCatalog = new SettingsForCatalog();
     //   catalogPageStatus.updateFilter(true, true, true, true, "allBook");
    }

    @Test
    public void addNewFilter() {
        Assert.assertTrue(settingsForCatalog.isHaveAFilter());
    }

    @Test
    public void whenWeRefreshFilterIsHaveFilterShouldBeFalse() {
     //   catalogPageStatus.updateFilter(false, false, false, false, "allBook");
        Assert.assertFalse(settingsForCatalog.isHaveAFilter());
    }

    @Test
    public void setIsAllInFalse() {
    //    catalogPageStatus.updateFilter(false, false, false, false, "al");
        Assert.assertTrue(settingsForCatalog.isHaveAFilter());
        Assert.assertFalse(settingsForCatalog.isAll());
    }

    @Test
    public void getEmptyFilterListWhenWeRefreshFilter() {
        List<String> filterList = new ArrayList<>();
    //    catalogPageStatus.updateFilter(false, false, false, false, "al");

        Assert.assertEquals(0, settingsForCatalog.getFilterList().size());
    }

    @Test
    public void getFullFilterListWhenWeRefreshFilter() {
        List<String> filterList = new ArrayList<>();
   //     catalogPageStatus.updateFilter(false, true, true, false, "al");
        filterList.add("FICTION");
        filterList.add("HISTORY");

        Assert.assertEquals(2, settingsForCatalog.getFilterList().size());
        Assert.assertEquals(filterList, settingsForCatalog.getFilterList());
    }

//    @Test
//    public void isHaveAFilter() {
//    }
//
//    @Test
//    public void isAll() {
//    }todo delete hidden tests
}