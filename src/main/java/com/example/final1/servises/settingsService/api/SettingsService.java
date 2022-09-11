package com.example.final1.servises.settingsService.api;

import com.example.final1.servises.settingsService.impl.entity.Settings;
import org.springframework.stereotype.Component;


public interface SettingsService {

    void addSettings(Settings settings);
    Settings getSettingsByName(String nameSettings);

}
