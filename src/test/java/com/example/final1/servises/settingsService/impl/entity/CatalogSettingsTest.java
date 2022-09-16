package com.example.final1.servises.settingsService.impl.entity;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CatalogSettingsTest {

    private CatalogSettings catalogSettings;

    @Before
    public void setUp() throws Exception {
        catalogSettings = new CatalogSettings();

    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testNullable() {
        assertTrue( catalogSettings.equals(new CatalogSettings()) );
    }


    @Test
    public void whenWeUpdatePageStatePageIsChecked() {
        catalogSettings = createSettingsWhenPage_3();
        assertEquals(catalogSettings.getLastPage(), 3);
    }

    @Test
    public void whenWeSendBlankSettingsUnUpdateState() {
        CatalogSettings activePage = createSettingsWhenPage_3();
        activePage.update(catalogSettings);
        assertEquals(createSettingsWhenPage_3(), activePage);
    }

    @Test
    public void whenWeUpdateWithNewSearchOldSearchShouldBeDie() {
        CatalogSettings activeSearch = createSettingsWhenSearch_is_Pushkin();
        catalogSettings.update(createSettingsWhenSearch_is_Pushkin());

        assertEquals(catalogSettings.getLastSearch(), activeSearch.getLastSearch());
    }

    @Test
    public void whenWeUpdateWithNewPageOldSearchUnChecked() {
        CatalogSettings activePage = createSettingsWhenPage_3();
        catalogSettings = createSettingsWhenSearch_is_Pushkin();

        assertNotEquals(catalogSettings.getLastSearch(), activePage.getLastSearch());
        assertEquals(catalogSettings.getLastSearch(), "Pushkin");
    }
    @Test
    public void whenWeUpdateWithNewFilterOldSearchUnChecked() {
        CatalogSettings activeFilter = createSettingsWhenNewFilter();
        catalogSettings = createSettingsWhenSearch_is_Pushkin();

        assertNotEquals(catalogSettings.getLastSearch(), activeFilter.getLastSearch());
        assertEquals(catalogSettings.getLastSearch(), "Pushkin");
    }

    @Test
    public void whenWeUpdateWithNewSearchPageShouldBe_0(){
        catalogSettings = createSettingsWhenPage_3();

    }



    private CatalogSettings createSettingsWhenNewFilter(){
        Integer page = null;
        String query = null;
        String isAll = "allBook";
        boolean CS = true;
        boolean FICTION = false;
        boolean HISTORY = true;
        boolean COMICS = true;
        return new CatalogSettings(page, query, isAll,
                CS, FICTION, HISTORY, COMICS);
    }

    private CatalogSettings createSettingsWhenPage_3(){
        Integer page = 3;
        String query = null;
        String isAll = null;
        boolean CS = true;
        boolean FICTION = false;
        boolean HISTORY = true;
        boolean COMICS = true;
        return new CatalogSettings(page, query, isAll,
                CS, FICTION, HISTORY, COMICS);
    }
    private CatalogSettings createSettingsWhenSearch_is_Pushkin(){
        Integer page = null;
        String query = "Pushkin";
        String isAll = null;
        boolean CS = true;
        boolean FICTION = false;
        boolean HISTORY = true;
        boolean COMICS = true;
        return new CatalogSettings(page, query, isAll,
                CS, FICTION, HISTORY, COMICS);
    }
}