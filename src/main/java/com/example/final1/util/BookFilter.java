package com.example.final1.util;


import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Getter
@Component
public class BookFilter {

    private boolean haveAFilter = false;

    private boolean CS;
    private boolean FICTION;
    private boolean HISTORY;
    private boolean COMICS;
    private boolean isAll;

    public void updateFilter(boolean CS, boolean FICTION, boolean HISTORY, boolean COMICS, String isAll){
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
    };

//    public void clear(){
//        haveAFilter = false;
//        isAll = true;
//    }

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

//    public void setHaveAFilter(boolean haveAFilter) {
//        this.haveAFilter = haveAFilter;
//    }
}
