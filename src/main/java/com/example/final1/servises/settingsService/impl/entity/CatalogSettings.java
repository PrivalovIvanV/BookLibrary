package com.example.final1.servises.settingsService.impl.entity;


import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Getter
@Component
@NoArgsConstructor
public class CatalogSettings implements Settings {

    private boolean haveAFilter = false;

    private boolean CS;
    private boolean FICTION;
    private boolean HISTORY;
    private boolean COMICS;
    private boolean isAll;
    private String isAllStr;
    private String lastSearch = "";
    private int lastPage = 0;

    public CatalogSettings(Integer page, String query, String isAll,
                           boolean CS, boolean FICTION, boolean HISTORY, boolean COMICS) {
        update(page, query, isAll, CS, FICTION, HISTORY, COMICS);
    }


    public void update(Integer page, String query, String isAll,
                       boolean CS, boolean FICTION, boolean HISTORY, boolean COMICS){
        isAllStr = isAll;
        updatePageStatus(page, query, isAll);

        if (isAll != null) {
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

        update(settings.getLastPage(),
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
        if (page != null) {
            lastPage = page.intValue();
        }
        if (query != null) {
            lastSearch = query;
        }
        if (query != null && page == null || query == null && page == null && isAll != null) {
            lastPage = 0;
        }     //Если у нас новый поисковый запрос, то lastPage сбрасывается
        if (query != null && isAll != null) {
            lastSearch = "";
        }
    }



}
