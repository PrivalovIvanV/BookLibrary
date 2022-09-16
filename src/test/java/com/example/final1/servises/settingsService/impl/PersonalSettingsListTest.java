package com.example.final1.servises.settingsService.impl;

import com.example.final1.servises.settingsService.api.CantParseException;
import com.example.final1.servises.settingsService.impl.entity.CatalogSettings;
import com.example.final1.servises.settingsService.impl.entity.Settings;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PersonalSettingsListTest {

    private PersonalSettingsList settingsList;

    @Before
    public void setUp() throws Exception {
        settingsList = new PersonalSettingsList();
    }

    @Test
    public void startTest() {
        assertTrue(settingsList.getListSettings().isEmpty());
    }

    @Test
    public void shouldReturn_1() {
        settingsList.update(new CatalogSettings());
        settingsList.update(new CatalogSettings());
        settingsList.update(new CatalogSettings());
        assertEquals(settingsList.getListSettings().size(), 1);
    }


    @Test
    public void addNewSettingsAndCheckInList() {
        settingsList.update(new CatalogSettings());

        assertTrue(settingsList.getListSettings().containsKey("CatalogSettings"));
    }
    @Test(expected = CantParseException.class)
    public void shouldTrowException() {
        settingsList.update(new wrongClassName());
    }
}

class wrongClassName implements Settings{

    @Override
    public void update(Settings settings) {

    }

    @Override
    public <T> T getContent() {
        return null;
    }
}