package com.example.final1.servises.settingsService.impl.entity;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
@Slf4j
@Getter
@NoArgsConstructor
public class SettingsForCatalog implements Settings {

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

    public SettingsForCatalog(Integer page, String query, String isAll,
                              boolean CS, boolean FICTION, boolean HISTORY, boolean COMICS) {
        update(page, query, isAll, CS, FICTION, HISTORY, COMICS);
    }


    @Override
    public void update(Settings setting){
        SettingsForCatalog settings = (SettingsForCatalog) setting;
        update(settings.getLastPageInteger(),
                settings.getLastSearch(),
                settings.getIsAllStr(),
                settings.isCS(),
                settings.isFICTION(),
                settings.isHISTORY(),
                settings.isCOMICS());
    }

    @Override
    public List<String> getContent(){

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

    private void update(Integer page, String query, String isAll,
                       boolean CS, boolean FICTION, boolean HISTORY, boolean COMICS){
        lastPageInteger = page;

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

    private void updatePageStatus(Integer page, String query, String isAll){

        if (page == null && isAll == null && (query != null && query.equals(""))) lastSearch = "";
        if (page != null) lastPage = page.intValue();
        if ((query != null) && !query.equals("")) lastSearch = query;
        //Если у нас новый поисковый запрос, то lastPage сбрасывается
        if (((query != null) && !query.equals("")) && page == null) lastPage = 0;
        if (((query != null) && !query.equals("")) && isAll != null) lastSearch = "";
        if ((query == null || query.equals("")) && page == null && isAll != null) lastPage = 0;

    }



}
