package com.example.final1.util;

import com.example.final1.servises.settingsService.impl.entity.CatalogSettings;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class CatalogSettingsTest {

    private CatalogSettings catalogSettings;

    @Before
    public void setUp() throws Exception {
        catalogSettings = new CatalogSettings();
     //   catalogPageStatus.updateFilter(true, true, true, true, "allBook");
    }

    @Test
    public void addNewFilter() {
        Assert.assertTrue(catalogSettings.isHaveAFilter());
    }

    @Test
    public void whenWeRefreshFilterIsHaveFilterShouldBeFalse() {
     //   catalogPageStatus.updateFilter(false, false, false, false, "allBook");
        Assert.assertFalse(catalogSettings.isHaveAFilter());
    }

    @Test
    public void setIsAllInFalse() {
    //    catalogPageStatus.updateFilter(false, false, false, false, "al");
        Assert.assertTrue(catalogSettings.isHaveAFilter());
        Assert.assertFalse(catalogSettings.isAll());
    }

    @Test
    public void getEmptyFilterListWhenWeRefreshFilter() {
        List<String> filterList = new ArrayList<>();
    //    catalogPageStatus.updateFilter(false, false, false, false, "al");

        Assert.assertEquals(0, catalogSettings.getFilterList().size());
    }

    @Test
    public void getFullFilterListWhenWeRefreshFilter() {
        List<String> filterList = new ArrayList<>();
   //     catalogPageStatus.updateFilter(false, true, true, false, "al");
        filterList.add("FICTION");
        filterList.add("HISTORY");

        Assert.assertEquals(2, catalogSettings.getFilterList().size());
        Assert.assertEquals(filterList, catalogSettings.getFilterList());
    }

//    @Test
//    public void isHaveAFilter() {
//    }
//
//    @Test
//    public void isAll() {
//    }todo delete hidden tests
}