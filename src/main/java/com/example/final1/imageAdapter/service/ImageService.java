package com.example.final1.imageAdapter.service;



import com.example.final1.imageAdapter.imageModels.PersonImage;
import com.example.final1.imageAdapter.repositories.PersonImageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageService {

    private final PersonImageRepository imageRepository;
    private final JdbcTemplate jdbcTemplate;



    @Transactional
    public void savePersonImage(PersonImage personImage){ imageRepository.save(personImage);}


    public PersonImage getImageByPersonId(int person_id){
        PersonImage image = jdbcTemplate.query("select * from avatar_images where person_id = ?",
                new Object[]{person_id},
                new BeanPropertyRowMapper<>(PersonImage.class)
        ).stream().findAny().orElse(null);
        return image;
    }



}
