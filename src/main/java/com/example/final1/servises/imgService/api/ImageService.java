package com.example.final1.servises.imgService.api;


import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
    void deleteAvatar(int person_id);
    void addAvatar(MultipartFile file, int person_id);
    
}
