package com.example.final1.servises.settingsService.impl.entity;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
        update(page, query, isAll, CS, FICTION, HISTORY, COMICS);
    }


    private void update(Integer page, String query, String isAll,
                        boolean CS, boolean FICTION, boolean HISTORY, boolean COMICS){
        this.lastPageInteger = page;

        updatePageStatus(page, query, isAll);
        if (isAll != null) {
            this.isAllStr = isAll;
            this.CS = CS;
            this.FICTION = FICTION;
            this.HISTORY = HISTORY;
            this.COMICS = COMICS;
            haveAFilter = true;
            if (isAll.equals("allBook")){
                if (!CS && !FICTION && !HISTORY && !COMICS) haveAFilter = false;
                this.isAll = true;
            } else {
                this.isAll = false;
            }

        }



    }

    @Override
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

    private void updatePageStatus(Integer page, String query, String isAll){

        if (page == null && isAll == null && (query != null && query.equals(""))) lastSearch = "";
        if (page != null) lastPage = page;
        if ((query != null) && !query.equals("")) lastSearch = query;
        if (((query != null) && !query.equals("")) && page == null) lastPage = 0;
        if (((query != null) && !query.equals("")) && isAll != null) lastSearch = "";
        if ((query == null || query.equals("")) && page == null && isAll != null) lastPage = 0;

    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CatalogSettings that = (CatalogSettings) o;
        return haveAFilter == that.haveAFilter && CS == that.CS && FICTION == that.FICTION && HISTORY == that.HISTORY && COMICS == that.COMICS && isAll == that.isAll && lastPage == that.lastPage && Objects.equals(lastSearch, that.lastSearch);
    }

    @Override
    public int hashCode() {
        return Objects.hash(haveAFilter, CS, FICTION, HISTORY, COMICS, isAll, lastSearch, lastPage);
    }


}
