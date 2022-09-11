package com.example.final1.servises.settingsService.impl;

import com.example.final1.servises.settingsService.impl.entity.Settings;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Getter
public class PersonalSettings {


    private Map<String, Settings> listSettings;

    public PersonalSettings() {
        listSettings = new HashMap<>();
    }



    public void update(Settings settings){
        String key = parseClassName(settings);

        if (listSettings.containsKey(key)){
            Settings setting = listSettings.get(key);
            setting.update(settings);
        } else {
            listSettings.put(key, settings);
            log.info("Для текущего пользователя была добавлена настройка {}", key);
        }
    }


    private String parseClassName(Settings text){
        Pattern classNamePattern = Pattern.compile("[.]([A-Z]\\w+)");
        Matcher matcher = classNamePattern.matcher(text.getClass().toString());
        matcher.find();
        return matcher.group(1).toLowerCase();
    }
//    public PageStatus getPageStatus(){
//        return (PageStatus) listSettings.get("pagestatus");
//    }
}
