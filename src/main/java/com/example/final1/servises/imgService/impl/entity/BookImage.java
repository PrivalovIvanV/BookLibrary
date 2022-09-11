package com.example.final1.servises.imgService.impl.entity;

import com.example.final1.servises.bookService.impl.entity.Book;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.*;
@Entity
@Table(name = "book_images")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookImage {


        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id")
        private int id;
        @Column(name = "name")
        private String name;
        @Column(name = "original_file_name")
        private String originalFileName;
        @Column(name = "size")
        private Long size;
        @Column(name = "content_type")
        private String contentType;

        @Lob
        @Type(type = "org.hibernate.type.ImageType")
        private byte[] bytes;

        @OneToOne(cascade = CascadeType.ALL)
        @JoinColumn(name = "book_id", referencedColumnName = "id")
        private Book book;


        @Override
        public String toString() {
            return "PersonImage{" +
                    "size=" + size +
                    ", contentType='" + contentType + '\'' +
                    '}';
        }


}
