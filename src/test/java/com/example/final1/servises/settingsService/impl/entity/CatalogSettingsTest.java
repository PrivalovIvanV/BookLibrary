package com.example.final1.servises.settingsService.impl.entity;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class CatalogSettingsTest {

    private CatalogSettings catalogSettings;

    @Before
    public void setUp() throws Exception {
        catalogSettings = new CatalogSettings();

    }

//    @After
//    public void tearDown() throws Exception {
//    }

    @Test
    public void testNullable() {
        assertTrue( catalogSettings.equals(new CatalogSettings()) );
    }


    @Test
    public void whenWeUpdatePageStatePageIsChecked() {
        catalogSettings = createSettingsWhenPage_is(3);
        assertEquals(catalogSettings.getLastPage(), 3);
    }

    @Test
    public void whenWeSendBlankSettingsUnUpdateState() {
        CatalogSettings activePage = createSettingsWhenPage_is(3);
        activePage.update(catalogSettings);
        assertEquals(createSettingsWhenPage_is(3), activePage);
    }

    @Test
    public void whenWeUpdateWithNewSearchOldSearchShouldBeDie() {
        CatalogSettings activeSearch = createSettingsWhenSearch_is_Pushkin();
        catalogSettings.update(createSettingsWhenSearch_is_Pushkin());

        assertEquals(catalogSettings.getLastSearch(), activeSearch.getLastSearch());
    }

    @Test
    public void whenWeUpdateWithNewPageOldSearchUnChecked() {
        CatalogSettings activePage = createSettingsWhenPage_is(3);
        catalogSettings = createSettingsWhenSearch_is_Pushkin();

        assertNotEquals(catalogSettings.getLastSearch(), activePage.getLastSearch());
        assertEquals(catalogSettings.getLastSearch(), "Pushkin");
    }
    @Test
    public void whenWeUpdateWithNewFilterOldSearchUnChecked() {
        CatalogSettings activeFilter = createSettingsWhenNewFilterIsAll_true();
        catalogSettings = createSettingsWhenSearch_is_Pushkin();

        assertNotEquals(catalogSettings.getLastSearch(), activeFilter.getLastSearch());
        assertEquals(catalogSettings.getLastSearch(), "Pushkin");
    }

    @Test
    public void whenWeUpdateWithNewSearchPageShouldBe_0(){
        catalogSettings = createSettingsWhenPage_is(3);
        catalogSettings.update(createSettingsWhenSearch_is_Pushkin());
        assertEquals(catalogSettings.getLastPage(), 0);

    }
    @Test
    public void whenWeUpdateWithNewSearch_isHaveFilter_should_be_false(){
        catalogSettings = createSettingsWhenSearch_is_Pushkin();
        catalogSettings.update(createSettingsWhenSearch_is_Pushkin());
        assertEquals(catalogSettings.isHaveAFilter(), false);

    }
    @Test
    public void whenWeUpdateWithNewFilter_isHaveFilter_should_be_true(){
        catalogSettings = createSettingsWhenNewFilterIsAll_true();
        assertEquals(catalogSettings.isHaveAFilter(), true);

    }
    @Test
    public void shouldReturnEmptyList(){
        catalogSettings = createSettingsWhenSearch_is_Pushkin();
        assertTrue(catalogSettings.getContent().isEmpty());
    }
    @Test
    public void shouldReturnNotEmptyList(){
        catalogSettings = createSettingsWhenNewFilterIsAll_true();
        assertFalse(catalogSettings.getContent().isEmpty());
    }
    @Test
    public void shouldReturn_isAll_true(){
        catalogSettings = createSettingsWhenNewFilterIsAll_true();
        assertTrue(catalogSettings.isAll());
    }
    @Test
    public void shouldReturn_isAll_false(){
        catalogSettings = createSettingsWhenNewFilterIsAll_false();
        assertFalse(catalogSettings.isAll());
    }

    @Test
    public void testHashCode_should_return_true(){
        catalogSettings = createSettingsWhenSearch_is_Pushkin();
        assertEquals(catalogSettings.hashCode(), createSettingsWhenSearch_is_Pushkin().hashCode());
    }
    @Test
    public void testHashCode_should_return_false(){
        catalogSettings = createSettingsWhenSearch_is_Pushkin();
        catalogSettings.update(createSettingsWhenPage_is(2));
        assertNotEquals(catalogSettings.hashCode(), createSettingsWhenSearch_is_Pushkin().hashCode());
    }



    private CatalogSettings createSettingsWhenNewFilterIsAll_true(){
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
    private CatalogSettings createSettingsWhenNewFilterIsAll_false(){
        Integer page = null;
        String query = null;
        String isAll = "isStock";
        boolean CS = true;
        boolean FICTION = false;
        boolean HISTORY = true;
        boolean COMICS = true;
        return new CatalogSettings(page, query, isAll,
                CS, FICTION, HISTORY, COMICS);
    }

    private CatalogSettings createSettingsWhenPage_is(int page){
        Integer myPage = page;
        String query = null;
        String isAll = null;
        boolean CS = true;
        boolean FICTION = false;
        boolean HISTORY = true;
        boolean COMICS = true;
        return new CatalogSettings(myPage, query, isAll,
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