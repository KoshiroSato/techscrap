package com.koshirosato.techscrap;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class ArticleServiceTest {
    
    @Mock
    ArticleRepository repository;

    @Mock
    TextVectorizer vectorizer;

    @InjectMocks
    ArticleService service;

    @Test
    void testSaveSkipsIfDuplicate() {
        ArticleEntity entity = new ArticleEntity();
        var exUrl = "http://example.com";

        entity.setUrl(exUrl);

        when(repository.findByUrl(exUrl)).thenReturn(Optional.of(new ArticleEntity()));

        service.save(entity);

        verify(repository, never()).save(any());
    }

    @Test
    void testToggleStar() {
        ArticleEntity entity = new ArticleEntity();
        entity.setStarred(false);
        when(repository.findById(1L)).thenReturn(Optional.of(entity));

        service.toggleStar(1L);

        assertTrue(entity.getStarred());
    }
}
