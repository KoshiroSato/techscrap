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
        var ex_url = "http://example.com";

        ArticleEntity article = new ArticleEntity();
        article.setTitle("test title");
        article.setUrl(ex_url);
        article.setMemo("test memo");
        repository.save(article);

        Optional<ArticleEntity> result = repository.findByUrl(ex_url);
        assertTrue(result.isPresent());
    }
}
