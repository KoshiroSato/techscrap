package com.koshirosato.techscrap;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class ArticleRepositoryTest {
    
    @Autowired
    private ArticleRepository repository;

    @Test
    void testSaveAndFind() {
        var exUrl = "http://example.com";

        ArticleEntity entity = new ArticleEntity();
        entity.setTitle("test title");
        entity.setUrl(exUrl);
        entity.setMemo("test memo");
        repository.save(entity);

        Optional<ArticleEntity> result = repository.findByUrl(exUrl);
        assertTrue(result.isPresent());
    }
}
