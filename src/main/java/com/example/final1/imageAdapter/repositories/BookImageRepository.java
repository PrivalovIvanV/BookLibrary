package com.example.final1.imageAdapter.repositories;

import com.example.final1.imageAdapter.imageModels.BookImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookImageRepository extends JpaRepository<BookImage, Integer> {
}
