package com.example.final1.servises.bookService.impl.entity;

import com.example.final1.servises.imgService.impl.entity.BookImage;
import com.example.final1.servises.personService.impl.entity.Person;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.sql.Date;

@Data
@Slf4j
@Entity
@Table(name = "Book")
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @Column(name = "title")
    String title;
    @Column(name = "author")
    String author;
    @Column(name = "description")
    String description;

    @ManyToOne
    @JoinColumn(name = "person_id", referencedColumnName = "id")
    Person owner;

    @Column(name = "taken_data")
    Date taken_data;
    @Column(name = "is_access")
    boolean isAccess;
    @Column(name = "book_genres")
    String book_genres;


    @OneToOne(mappedBy = "book")
    BookImage bookImage;



    public boolean isOverdue(){
        if (taken_data == null) {
            return false;
        }
        if (intervalDate() > 15) return true;
        return false;
   };



    public int intervalDate(){
        if (taken_data == null) return 0;
        Date currentDate = new Date(System.currentTimeMillis());
        Date diff = new Date(currentDate.getTime() - taken_data.getTime());
        return (diff.getDate() -1);
   }


    public Date dateOfReturn(){
        if (taken_data == null) return null;
        Date date = new Date(taken_data.getTime() + 60000*60*24*15);
        return date;
   };

    public void setOwner(Person owner) {
        isAccess = false;
        taken_data = new Date(System.currentTimeMillis());
        this.owner = owner;
    }


}
