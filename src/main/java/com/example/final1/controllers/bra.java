package com.example.final1.controllers;

import com.example.final1.servises.imgService.impl.entity.BookImage;
import com.example.final1.servises.imgService.impl.BookImageService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
@RequestMapping("/addImageForBook")
public class bra {

    private final BookImageService bookImageService;
    private static int num = 87;

    @GetMapping
    public String home(Model model){
        model.addAttribute("num", num++);
        return "e/create";
    }

    @SneakyThrows
    @PostMapping
    public String addImage(@RequestParam("img") MultipartFile file1,
                           @RequestParam("book_id") int book_id,
                           Model model){
        if (file1.getSize() != 0){
            BookImage bookImage = toImageEntity(file1);
            bookImageService.saveBookImage(bookImage, book_id);
        }
        model.addAttribute("num", num++);
        return "e/create";
    }


    private BookImage toImageEntity(MultipartFile file) throws IOException {
        BookImage image = new BookImage();
        image.setName(file.getName());
        image.setOriginalFileName(file.getOriginalFilename());
        image.setContentType(file.getContentType());
        image.setSize(file.getSize());
        image.setBytes(file.getBytes());
        return image;
    }
}
