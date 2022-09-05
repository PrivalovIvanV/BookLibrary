package com.example.final1.servises;


import com.example.final1.imageAdapter.imageModels.PersonImage;
import com.example.final1.imageAdapter.service.ImageService;
import com.example.final1.models.Book;
import com.example.final1.models.Person;
import com.example.final1.repositories.PersonRepo;
import com.example.final1.security.PersonDetails;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PersonService implements UserDetailsService {

    private final JdbcTemplate jdbcTemplate;
    private final PersonRepo repo;
    private final ImageService imageService;


    public boolean isAuth(){
        int currentId = getCurrentUserID();
        if ( currentId != -1){
            return true;
        }
        return false;
    }           //проверка на авторизацию пользователя в системе
    public Person getCurrentUser(){
        int currentId = getCurrentUserID();
        if ( currentId != -1){
            return repo.findById(currentId).get();
        }
        return null;
    }    //получение текущего пользователя
    public boolean userHaveAvatar(){
        PersonImage image = imageService.getImageByPersonId(getCurrentUserID());
        System.out.println(image);
        if (image != null) return true;
        return false;
    }


    @Transactional
    public void UpdatePerson(Person person){
        jdbcTemplate.update("update Person set first_name=?, last_name=? where email = ?",
                person.getFirst_name(),
                person.getLast_name(),
                person.getEmail()
        );
    }
    @SneakyThrows
    @Transactional
    public void addAvatar(MultipartFile file){
        PersonImage image;
        if (file.getSize() != 0) {
            Person person = getCurrentUser();
            image = toImageEntity(file);
            image.setUser(person);
            person.setAvatar(image);
            System.out.println(userHaveAvatar());


            if (userHaveAvatar()){
                int image_id = imageService.getImageByPersonId(getCurrentUserID()).getId();
                image.setId(image_id);
                imageService.savePersonImage(image);
            } else imageService.savePersonImage(image);

            repo.save(person);
        }
    }

    @Transactional
    public void save(Person person){ repo.save(person); };


    @Transactional
    public void deleteAvatar(){
        Person person = repo.findById(getCurrentUserID()).get();
        person.setAvatar(null);
        repo.save(person);

        jdbcTemplate.update("delete from avatar_images where person_id = ?", getCurrentUserID());
    }


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Person> user = repo.findPersonByEmail(email); //пытаемся получить из БД человека с Таким Email-ом

        if(user.isEmpty()) {
            log.info("Попытка войти в систему под не зарегистрированной почтой {}", email);
            throw new UsernameNotFoundException("Пользователь с такой почтой не зарегистрирован");
        }
        return new PersonDetails(user.get());
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
    private static int getCurrentUserID(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        try {
            Person user = ((PersonDetails) authentication.getPrincipal()).getPerson();
            return user.getId();
        }catch (Exception e){
            return -1;
        }
    }
}
