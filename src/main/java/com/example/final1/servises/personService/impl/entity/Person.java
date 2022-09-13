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

    @NotBlank(message = "Поле не может пустовать")
    @Column(name = "first_name")
    @Size(max = 15, message = "Имя превышает 15 символов")
    String first_name;


    @Column(name = "last_name")
    @Size(max = 15, message = "Значение не должно превышать 15 символов")
    String last_name;


    @Column(name = "email")
    @NotBlank(message = "Пожалуйста, заполните поле")
    @Email(message = "Не корректная почта")
    String email;


    @Column(name = "password")
    @NotBlank(message = "Обязательно для заполнения")
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

    public boolean isOwner(int bookId){
        if (personLibrary == null) return false;
        return personLibrary.stream().anyMatch(book -> book.getId() == bookId);
    }

}
