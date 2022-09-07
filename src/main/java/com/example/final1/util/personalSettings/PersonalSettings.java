package com.example.final1.util.personalSettings;

import com.example.final1.models.Person;
import com.example.final1.security.PersonDetails;
import com.example.final1.util.personalSettings.settings.BookFilter;
import com.example.final1.util.personalSettings.settings.PageStatus;
import com.example.final1.util.personalSettings.settings.Settings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
@Slf4j
public class PersonalSettings {


    private Map<String, Settings> listSettings;

    public PersonalSettings() {
        listSettings = new HashMap<>();
        PageStatus pageStatus = new PageStatus(0, "");
        this.pull(pageStatus);
    }

    public Map<String, Settings> get(){return listSettings;}


    public void pull(Settings settings){
        String key = parseClassName(settings);
        listSettings.put(key, settings);
    }


    private String parseClassName(Settings text){
        Pattern classNamePattern = Pattern.compile("[.]([A-Z]\\w+)");
        Matcher matcher = classNamePattern.matcher(text.getClass().toString());
        matcher.find();
        return matcher.group(1).toLowerCase();
    }

    public PageStatus getPageStatus(){
        return (PageStatus) listSettings.get("pagestatus");
    }
}
