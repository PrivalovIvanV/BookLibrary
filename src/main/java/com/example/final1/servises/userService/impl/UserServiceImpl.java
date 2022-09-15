package com.example.final1.servises.userService.impl;

import com.example.final1.security.PersonDetails;
import com.example.final1.servises.bookService.api.BookService;
import com.example.final1.servises.bookService.impl.entity.Book;
import com.example.final1.servises.imgService.api.ImageService;
import com.example.final1.servises.personService.api.PersonService;
import com.example.final1.servises.personService.impl.entity.Person;
import com.example.final1.servises.settingsService.api.SettingsService;
import com.example.final1.servises.settingsService.impl.entity.CatalogSettings;
import com.example.final1.servises.settingsService.impl.entity.Settings;
import com.example.final1.servises.settingsService.util.FilterNotFoundException;
import com.example.final1.servises.userService.api.UserService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final PersonService personService;
    private final BookService bookService;
    private final SettingsService settingsService;
    private final ImageService imageService;

    @Override
    public List<Book> getAll() {
        CatalogSettings filter = settingsService.getSettings(CatalogSettings.class);
        String query = filter.getLastSearch();

        if (filter.isHaveAFilter()){
            return sorted(query);
        }else
            return bookService.findAll(query);
    }

    @Override
    public List<Book> getUserLibrary() {
        int id = getCurrentUserID();
        return bookService.getAllByPersonId(id);
    }


    @Override
    public void addOwner(int book_id) {
        if (isAuth()){
            int id = getCurrentUserID();
            bookService.addOwner(book_id, id);
        }
    }

    @Override
    public boolean isOwner(int book_id) {
        if (isAuth()){
            List<Book> library = getUserLibrary();
            return library.stream().anyMatch(book -> book.getId() == book_id);
        }else return false;

    }

    @Override
    public Book getBook(int id) {
        return bookService.get(id);
    }

    @Override
    public Person getUser() {
        int id = getCurrentUserID();
        if (isAuth()){
            return personService.get(id);
        } else return null;
    }

    @Override
    public boolean isAuth() {
        int currentId = getCurrentUserID();
        return currentId != -1;
    }

    @Override
    public void addAvatar(MultipartFile avatar) {
        int id = getCurrentUserID();
        imageService.addAvatar(avatar, id);
    }

    @Override
    public void removeAvatar() {
        int id = getCurrentUserID();
        imageService.deleteAvatar(id);
    }

    @Override
    public <T extends Settings> void addSettings(T settings) {
        settingsService.addSettings(settings);
    }

    @Override
    public <T extends Settings> T getSettings(Class<T> clazz) throws FilterNotFoundException {
        return settingsService.getSettings(clazz);
    }

    @Override
    public <T extends Settings> boolean isPresent(Class<T> clazz) {
        return settingsService.isPresent(clazz);
    }

    @SneakyThrows
    private List<Book> sorted(String query){
        List<Book> responseList = new ArrayList<>();
        List<Book> untreatedList = bookService.findAll(query);

        CatalogSettings catalogSettings = settingsService.getSettings(CatalogSettings.class);
        List<String> filterList = catalogSettings.getContent();


        if (filterList.isEmpty()) {
            responseList = untreatedList;
        } else {
            for (String filter : filterList) {
                for (Book book : untreatedList){
                    if (book.getBook_genres().equals(filter))
                        responseList.add(book);
                }
            }
        }

        if (!catalogSettings.isAll()){
            responseList = responseList.stream().filter(Book::isAccess).collect(Collectors.toList());
        }
        log.info("Количество книг до и после сортировки сортировки: {} и {}, isAll = {}",
                untreatedList.size(), responseList.size(), catalogSettings.isAll());
        return responseList;
    }

    private int getCurrentUserID(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        try {
            Person user = ((PersonDetails) authentication.getPrincipal()).getPerson();
            return user.getId();
        }catch (Exception e){
            return -1;
        }
    }   //получение текущего пользователя
}
