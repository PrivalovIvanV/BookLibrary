package com.example.final1.servises.imgService.api;

import com.example.final1.servises.imgService.impl.entity.Image;
import com.example.final1.servises.imgService.impl.entity.PersonImage;

public interface ImageService {
    <T extends Image> void save(T personImage, int id);
    
}
