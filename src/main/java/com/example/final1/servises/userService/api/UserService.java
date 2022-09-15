package com.example.final1.servises.userService.api;

import com.example.final1.servises.bookService.impl.entity.Book;
import com.example.final1.servises.personService.impl.entity.Person;
import com.example.final1.servises.settingsService.api.SettingsService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService extends SettingsService {

    void addOwner(int book_id);
    boolean isOwner(int book_id);
    Book getBook(int id);
    List<Book> getAll();
    List<Book> getUserLibrary();
    Person getUser();
    boolean isAuth();
    void addAvatar(MultipartFile avatar);
    void removeAvatar();

}
