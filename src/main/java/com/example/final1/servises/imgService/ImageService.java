package com.example.final1.servises.imgService;

import com.example.final1.servises.imgService.impl.AvatarService;
import com.example.final1.servises.imgService.impl.entity.PersonImage;
import com.example.final1.servises.personService.impl.entity.Person;
import com.example.final1.servises.personService.impl.repo.PersonRepo;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class ImageService {

    private final AvatarService avatarService;
    private final JdbcTemplate jdbcTemplate;
    private final PersonRepo repo;

    @Transactional
    public void deleteAvatar(int person_id){
        Person person = repo.findById(person_id).get();
        person.setAvatar(null);
        repo.save(person);

        jdbcTemplate.update("delete from avatar_images where person_id = ?", person_id);
    }

    @SneakyThrows
    @Transactional
    public void addAvatar(MultipartFile file, int person_id){
        Person person = repo.findById(person_id).get();
        PersonImage image;
        if (file.getSize() != 0) {
            image = toImageEntity(file);
            image.setUser(person);
            person.setAvatar(image);


            if (isHaveAvatar()){
                int image_id = avatarService.getById(person_id).getId();
                image.setId(image_id);
                avatarService.saveAvatar(image);
            } else avatarService.saveAvatar(image);

            repo.save(person);
        }
    }

    private PersonImage toImageEntity(MultipartFile file) throws IOException {
        PersonImage image = new PersonImage();
        image.setName(file.getName());
        image.setOriginalFileName(file.getOriginalFilename());
        image.setContentType(file.getContentType());
        image.setSize(file.getSize());
        image.setBytes(file.getBytes());
        return image;
    }
}
