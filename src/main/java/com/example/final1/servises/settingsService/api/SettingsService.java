package com.example.final1.servises.settingsService.api;

import com.example.final1.servises.settingsService.impl.entity.Settings;
import com.example.final1.servises.settingsService.util.FilterNotFoundException;
import org.springframework.stereotype.Component;


public interface SettingsService {

    void addSettings(Settings settings);
    Settings getSettingsByName(String nameSettings) throws FilterNotFoundException;
    boolean isSettingsPresent(String nameSettings);

}
