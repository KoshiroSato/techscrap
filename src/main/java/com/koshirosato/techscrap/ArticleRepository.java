package com.koshirosato.techscrap;

import java.util.Optional;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ArticleRepository extends JpaRepository<ArticleEntity, Long>{ 
    Optional<ArticleEntity> findByUrl(String url);

    @Query("SELECT a FROM ArticleEntity a WHERE " + 
           "LOWER(a.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " + 
           "LOWER(a.url) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " + 
           "LOWER(a.memo) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<ArticleEntity> search(@Param("keyword") String keyword);

    List<ArticleEntity> findByStarredTrue();
}
