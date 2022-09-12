package com.example.final1.servises.settingsService.api;

import com.example.final1.servises.settingsService.impl.entity.Settings;
import com.example.final1.servises.settingsService.util.FilterNotFoundException;


public interface SettingsService {

    <T extends Settings> void addSettings(T settings);
    <T extends Settings> T getSettings(Class<T> clazz) throws FilterNotFoundException;
    <T extends Settings> boolean isPresent(Class<T> clazz);

}
