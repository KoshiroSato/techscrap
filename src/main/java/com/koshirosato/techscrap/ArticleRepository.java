package com.koshirosato.techscrap;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<ArticleEntity, Long>{ 
    Optional<ArticleEntity> findByUrl(String url); 
}
