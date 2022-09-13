package com.example.final1.servises.imgService;


import com.example.final1.servises.imgService.impl.entity.BookImage;
import com.example.final1.servises.imgService.impl.entity.PersonImage;
import com.example.final1.servises.imgService.impl.BookImageService;
import com.example.final1.servises.imgService.impl.AvatarService;
import com.example.final1.servises.personService.impl.entity.Person;
import com.example.final1.security.PersonDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.ByteArrayInputStream;

@org.springframework.web.bind.annotation.RestController
@RequiredArgsConstructor
public class RestController {

    private final AvatarService avatarService;
    private final JdbcTemplate jdbcTemplate;
    private final BookImageService bookService;

    @GetMapping("/personAvatar")
    private ResponseEntity<?> getImageById() {
        int person_id = getCurrentUserID();
        PersonImage image = avatarService.getById(person_id);

        if ( image != null) {         //если аватарка у пользователя есть, то мы ее покажем
            return ResponseEntity.ok()
                    .header("fileName", image.getOriginalFileName())
                    .contentType(MediaType.valueOf(image.getContentType()))
                    .contentLength(image.getSize())
                    .body(new InputStreamResource(new ByteArrayInputStream(image.getBytes())));
        }

        PersonImage defaultIm = jdbcTemplate.query("select * from avatar_images where is_default = true",
                        new Object[]{},
                        new BeanPropertyRowMapper<>(PersonImage.class))
                .stream().findAny().get();


        return ResponseEntity.ok()   //А если все таки нету, то покажем дефолтную
                .header("fileName", defaultIm.getOriginalFileName())
                .contentType(MediaType.valueOf(defaultIm.getContentType()))
                .contentLength(defaultIm.getSize())
                .body(new InputStreamResource(new ByteArrayInputStream(defaultIm.getBytes())));
    }


    @GetMapping("/BookAvatar/{id}")
    private ResponseEntity<?> getBookImageById(@PathVariable("id") int book_id) {
        BookImage image = bookService.getById(book_id);

        if ( image != null) {         //если аватарка у пользователя есть, то мы ее покаем
            return ResponseEntity.ok()
                    .header("fileName", image.getOriginalFileName())
                    .contentType(MediaType.valueOf(image.getContentType()))
                    .contentLength(image.getSize())
                    .body(new InputStreamResource(new ByteArrayInputStream(image.getBytes())));
        }


        BookImage defaultIm = jdbcTemplate.query("select * from book_images where is_default = true",
                        new Object[]{},
                        new BeanPropertyRowMapper<>(BookImage.class))
                .stream().findAny().get();


        return ResponseEntity.ok()   //А если все таки нет, то покажем дефолтную
                .header("fileName", defaultIm.getOriginalFileName())
                .contentType(MediaType.valueOf(defaultIm.getContentType()))
                .contentLength(defaultIm.getSize())
                .body(new InputStreamResource(new ByteArrayInputStream(defaultIm.getBytes())));
    }

    private static int getCurrentUserID() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        try {
            Person user = ((PersonDetails) authentication.getPrincipal()).getPerson();
            return user.getId();
        } catch (Exception e) {
            return -1;
        }
    }
}
