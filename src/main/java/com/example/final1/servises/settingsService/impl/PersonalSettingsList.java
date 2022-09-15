package com.example.final1.servises.settingsService.impl;

import com.example.final1.servises.settingsService.impl.entity.Settings;
import com.example.final1.servises.settingsService.impl.entity.CatalogSettings;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Getter
public class PersonalSettingsList {


    private final Map<String, Settings> listSettings;

    public PersonalSettingsList() {
        listSettings = new HashMap<>();
    }
    
    public void update(Settings settings){
        String key = parseClassName(settings);
        log.info("Попытка обновить настройки {}", key);

        if (listSettings.containsKey(key)){
            Settings setting = listSettings.get(key);
            setting.update(settings);
        } else {
            listSettings.put(key, settings);
            log.info("Из-за отсутствия у пользователя {}, мы ее добавили", key);
        }
    }


    private String parseClassName(Settings text){
        Pattern classNamePattern = Pattern.compile("[.]([A-Z]\\w+)");
        Matcher matcher = classNamePattern.matcher(text.getClass().toString());
        matcher.find();
        return matcher.group(1);
    }

}
