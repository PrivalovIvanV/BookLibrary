package com.example.final1.servises.personService.impl.entity;


import com.example.final1.servises.imgService.impl.entity.PersonImage;
import com.example.final1.servises.bookService.impl.entity.Book;
import com.example.final1.util.UserRole;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

@Data
@Entity
@Table(name = "Person")
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Person{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @NotBlank(message = "Заполни поле, педрила!")
    @Column(name = "first_name")
    @Size(max = 15, message = "Ебать у тебя имя огромное")
    String first_name;


    @Column(name = "last_name")
    @Size(max = 15, message = "Фамилия как у хача")
    String last_name;


    @Column(name = "email")
    @NotBlank(message = "Заполни поле, педрила!")
    @Email(message = "Сука, попросил же ввести почту, а не вьебаться головой в класиатуру")
    String email;


    @Column(name = "password")
    @NotBlank(message = "пароль не может быть пустым")
    String password;


    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    UserRole personRole;
    @Column(name = "account_created")
    Date account_created;
    @Column(name = "last_active")
    Timestamp last_active;
    @Column(name = "isNotBanned")
    boolean isNotBanned;

    @OneToMany(mappedBy = "owner")
    List<Book> personLibrary;

    @OneToOne(mappedBy = "user")
    PersonImage avatar;


    public void addBook(Book book){
        personLibrary.add(book);
    }

    public Person(Person p) {
        this.id = p.getId();
        this.first_name = p.getFirst_name();
        this.last_name = p.getLast_name();
        this.email = p.getEmail();
        this.password = "*********";
    }

    public boolean isOwnerThisBook(int bookId){
        if (personLibrary == null) return false;
        return personLibrary.stream().anyMatch(book -> book.getId() == bookId);
    }

    public String getFullName(){return first_name + " " + last_name;}

}
