package com.example.final1.servises;

import com.example.final1.models.Person;
import com.example.final1.security.PersonDetails;
import com.example.final1.util.personalSettings.PersonalSettings;
import com.example.final1.util.personalSettings.settings.BookFilter;
import com.example.final1.util.personalSettings.settings.PageStatus;
import com.example.final1.util.personalSettings.settings.Settings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
public class SettingsService {

    private Map<Long, PersonalSettings> cashedSettings = new HashMap<>();
    private Authentication authentication;

    public BookFilter addCatalogFilter(Integer page, String query, String isAll,
                                 boolean CS, boolean FICTION, boolean HISTORY, boolean COMICS){
        boolean lastPageIsChanged = false;
        boolean lastSearchIsChanged = false;
        BookFilter bookFilter;
        int lastPage = 0;
        String lastSearch = "";

        if (page != null) {
            lastPage = page.intValue();
            lastPageIsChanged = true;
        }
        if (query != null) {
            lastSearchIsChanged = true;
            lastSearch = query;
        }
        if (query != null && page == null) {
            lastPage = 0;
            lastPageIsChanged = true;
        }     //Если у нас новый поисковой запрос, то lastPage сбрасывается
        if (query != null && isAll != null) {
            lastSearch = "";
            lastSearchIsChanged = true;
        };
        if (query == null && page == null && isAll != null) {
            lastPage = 0;
            lastPageIsChanged = true;
        };


        ///Сейчас мы должны проверить есть ли у нас закэшированные настройки для конкретного пользователя
        if (cashedSettings.containsKey(this.getKeyCurrentUser())){
        PersonalSettings listSettings = cashedSettings.get(getKeyCurrentUser());

            try {//это мы попробовали добавить BookFilter
                bookFilter = (BookFilter) this.get("BookFilter");
                bookFilter.updateFilter(CS, FICTION, HISTORY, COMICS, isAll);
            } catch (Exception e) {
                bookFilter = new BookFilter();
                bookFilter.updateFilter(CS, FICTION, HISTORY, COMICS, isAll);
                listSettings.pull(bookFilter);
            }



        } else {
            PersonalSettings personalSettings = new PersonalSettings();
            bookFilter = new BookFilter();
            bookFilter.updateFilter(CS, FICTION, HISTORY, COMICS, isAll);
            personalSettings.pull(bookFilter);
            cashedSettings.put(getKeyCurrentUser(), personalSettings);
        }
        PersonalSettings listSettings = cashedSettings.get(getKeyCurrentUser());
        PageStatus pageStatus = listSettings.getPageStatus();
        if (lastPageIsChanged) pageStatus.setLastPage(lastPage);
        if (lastSearchIsChanged) pageStatus.setLastSearch(lastSearch);


        return bookFilter;
    }

    public Settings get(String name) throws Exception{
        Map<String, Settings> map = cashedSettings.get(getKeyCurrentUser()).get();
        log.warn("Уникальный идентификатор равен {}", getKeyCurrentUser());
        if (map.containsKey(name.toLowerCase())) return map.get(name.toLowerCase());
        throw new Exception();//todo доделать исключение
    }

    private long getKeyCurrentUser(){
        authentication = SecurityContextHolder.getContext().getAuthentication();
        try {
            Person user = ((PersonDetails) authentication.getPrincipal()).getPerson();
            log.warn("{}", user.getId());
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
        log.info("Преобразовали {} в {}", ipWithColon, ip);
        return ip;
    }
}
