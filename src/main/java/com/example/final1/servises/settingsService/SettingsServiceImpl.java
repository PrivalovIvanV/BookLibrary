package com.example.final1.servises.settingsService;

import com.example.final1.servises.personService.impl.entity.Person;
import com.example.final1.security.PersonDetails;
import com.example.final1.servises.settingsService.api.SettingsService;
import com.example.final1.servises.settingsService.impl.PersonalSettings;
import com.example.final1.servises.settingsService.impl.entity.Settings;
import com.example.final1.servises.settingsService.util.FilterNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Slf4j
@Component
public class SettingsServiceImpl implements SettingsService {

    private final Map<Long, PersonalSettings> cash = new HashMap<>();
    private Authentication authentication;


    @Override
    public void addSettings(Settings settings){
        getSettingsForCurrentUser().update(settings);
    }


    @Override// у текущего пользователя берем конкретные настройки с именем nameSettings
    public Settings getSettingsByName(String nameSettings) throws FilterNotFoundException {
        Map<String, Settings> listSettings =
                cash.get(keyCurrentUser()).getListSettings();

        if (isSettingsPresent(nameSettings)) {
            return listSettings.get(nameSettings.toLowerCase());
        } else {
            log.warn("У пользователя с id {}, не оказалось настройки с именем {}", keyCurrentUser(), nameSettings);
            throw new FilterNotFoundException(nameSettings);
        }
    }

    public boolean isSettingsPresent(String nameSettings){
        Map<String, Settings> listSettings =
                getSettingsForCurrentUser().getListSettings();
        if (listSettings.containsKey(nameSettings.toLowerCase())) return true;

        return false;
    }


    //В этом методе и происходит кэширование настроек для каждого отдельного пользователя
    public PersonalSettings getSettingsForCurrentUser(){
        long key = keyCurrentUser();
        if (!cash.containsKey(key)) {
            cash.put(key, new PersonalSettings());
        }
        return cash.get(key);

    }


    //метод получающий ключ текущего пользователя
    private long keyCurrentUser(){
        authentication = SecurityContextHolder.getContext().getAuthentication();
        try {
            Person user = ((PersonDetails) authentication.getPrincipal()).getPerson();
            return user.getId();
        }catch (Exception e){
            return parseIp(authentication.getDetails().toString());
        }
    }

    private long parseIp(String text){
        Pattern ipPattern = Pattern.compile("IpAddress=([\\d|:]+)");
        Matcher matcher = ipPattern.matcher(text);
        matcher.find();
        String ipWithColon = matcher.group(1);
        ipWithColon = ipWithColon.replaceAll("\\D", "");
        long ip = Integer.valueOf(ipWithColon);
        return ip;
    }


}
