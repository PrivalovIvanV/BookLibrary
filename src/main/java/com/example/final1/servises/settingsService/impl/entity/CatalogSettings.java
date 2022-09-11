package com.example.final1.servises.settingsService.impl.entity;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
@Slf4j
@Getter
@NoArgsConstructor
public class CatalogSettings implements Settings {

    private boolean haveAFilter = false;

    private boolean CS;
    private boolean FICTION;
    private boolean HISTORY;
    private boolean COMICS;
    private boolean isAll;
    private Integer lastPageInteger;
    private String isAllStr;
    private String lastSearch = "";
    private int lastPage = 0;

    public CatalogSettings(Integer page, String query, String isAll,
                           boolean CS, boolean FICTION, boolean HISTORY, boolean COMICS) {
        System.out.println();
        System.out.println("2.0 page " + page);
        System.out.println("2.0 q " + query);
        System.out.println("2.0 isAll " + isAll);
        System.out.println("2.0 CS " + CS);
//        log.info("Мы попали в конструктор каталога и query = {}", query);//todo log
        update(page, query, isAll, CS, FICTION, HISTORY, COMICS);
    }


    public void update(Integer page, String query, String isAll,
                       boolean CS, boolean FICTION, boolean HISTORY, boolean COMICS){
        lastPageInteger = page;
        System.out.println();
        System.out.println("3.0 page " + page);
        System.out.println("3.0 q " + query);
        System.out.println("3.0 isAll " + isAll);
        System.out.println("3.0 CS " + CS);

//        log.info("Оказалось, что query = {}", query);//todo log
        updatePageStatus(page, query, isAll);

        if (isAll != null) {
            isAllStr = isAll;
            this.CS = CS;
            this.FICTION = FICTION;
            this.HISTORY = HISTORY;
            this.COMICS = COMICS;
            haveAFilter = true;
            if (isAll.equals("allBook")){
                if (CS == false && FICTION == false && HISTORY == false && COMICS == false) haveAFilter = false;
                this.isAll = true;
            } else {
                this.isAll = false;
            }

        }



    }
    public void update(Settings setting){
        CatalogSettings settings = (CatalogSettings) setting;
        update(settings.getLastPageInteger(),
                settings.getLastSearch(),
                settings.getIsAllStr(),
                settings.isCS(),
                settings.isFICTION(),
                settings.isHISTORY(),
                settings.isCOMICS());
    }


    public List<String> getFilterList(){

        List<String> filterList = new ArrayList<>();

        if (!isHaveAFilter()){
            return filterList;
        }

        if (CS) filterList.add("CS");
        if (FICTION) filterList.add("FICTION");
        if (HISTORY) filterList.add("HISTORY");
        if (COMICS) filterList.add("COMICS");

        return filterList;
    }

    private void updatePageStatus(Integer page, String query, String isAll){
        if (page == null && isAll == null && (query != null && query.equals(""))){
            lastSearch = "";
        }
        if (page != null) {
//            log.info("Данные последней страницы изменились с {}, до {}", lastPage, page.intValue());//todo log
            lastPage = page.intValue();
        }
        if ((query != null) && !query.equals("")) {
            lastSearch = query;
        }
        if (((query != null) && !query.equals("")) && page == null) {
//            log.info("Данные последней страницы изменились с {}, до {}", lastPage, 0);//todo log
            lastPage = 0;
        }     //Если у нас новый поисковый запрос, то lastPage сбрасывается
        if (((query != null) && !query.equals("")) && isAll != null) {
            lastSearch = "";
        }
        if ((query == null || query.equals("")) && page == null && isAll != null){
            lastPage = 0;
        }
    }



}
