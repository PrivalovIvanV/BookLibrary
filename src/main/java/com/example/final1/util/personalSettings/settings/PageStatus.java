package com.example.final1.util.personalSettings.settings;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageStatus implements Settings{
    private int lastPage = 0;
    private String lastSearch = "";
}
