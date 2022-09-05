package com.example.final1.imageAdapter.repositories;

import com.example.final1.imageAdapter.imageModels.PersonImage;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PersonImageRepository extends JpaRepository<PersonImage, Integer> {

}
