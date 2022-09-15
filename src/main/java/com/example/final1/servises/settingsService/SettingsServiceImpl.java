package com.example.final1.servises.settingsService;

import com.example.final1.security.PersonDetails;
import com.example.final1.servises.personService.impl.entity.Person;
import com.example.final1.servises.settingsService.api.CantParseException;
import com.example.final1.servises.settingsService.api.SettingsService;
import com.example.final1.servises.settingsService.impl.PersonalSettingsList;
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

    private final Map<Long, PersonalSettingsList> cash = new HashMap<>();


    @Override
    public <T extends Settings> void addSettings(T settings){
        getListForCurrentUser().update(settings);
    }


    @Override// у текущего пользователя берем конкретные настройки с именем nameSettings
    public <T extends Settings> T getSettings(Class<T> clazz) throws FilterNotFoundException {
        String key = parseClassName(clazz);
        Map<String, Settings> listSettings =
                getListForCurrentUser().getListSettings();

        if (isPresent(clazz)) {
            return (T) listSettings.get(key);
        } else {
            log.warn("У пользователя с id {}, не оказалось настройки с именем {}", keyCurrentUser(), key);
            throw new FilterNotFoundException(key);
        }
    }

    @Override
    public <T extends Settings> boolean isPresent(Class<T> clazz){
        String key = parseClassName(clazz);
        Map<String, Settings> listSettings =
                getListForCurrentUser().getListSettings();
        if (listSettings.containsKey(key)) return true;
        log.info("Проверка на наличие {} у пользователя", key);
        return false;
    }


    //В этом методе и происходит кэширование настроек для каждого отдельного пользователя
    private PersonalSettingsList getListForCurrentUser(){
        long key = keyCurrentUser();
        if (!cash.containsKey(key)) {
            cash.put(key, new PersonalSettingsList());
        }
        return cash.get(key);

    }


    //метод получающий ключ текущего пользователя
    private long keyCurrentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
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
        if (matcher.find()){
            String ipWithColon = matcher.group(1);
            ipWithColon = ipWithColon.replaceAll("\\D", "");
            return Integer.parseInt(ipWithColon);
        } else {
            throw new CantParseException("Невозможно распарсить строку " + text);
        }
    }

    private <T extends Settings> String parseClassName(Class<T> clazz){
        Pattern classNamePattern = Pattern.compile("[.]([A-Z]\\w+)");
        Matcher matcher = classNamePattern.matcher(clazz.toString());
        if (matcher.find()){
            return matcher.group(1);
        } else throw new CantParseException("Не удалось распарсить " + clazz);
    }


}
