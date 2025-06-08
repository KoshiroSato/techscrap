package com.koshirosato.techscrap;

import java.io.IOException;
import java.util.List;

import org.jsoup.Jsoup;
import org.springframework.stereotype.Service;


@Service
public class ArticleService {
    private final ArticleRepository repository;

    public ArticleService(ArticleRepository repository) {
        this.repository = repository;
    }

    public List<ArticleEntity> findAll() {
        return repository.findAll();
    }

    public void save(ArticleEntity article) {
        // 記事タイトル自動取得
        if (article.getTitle() == null || article.getTitle().isEmpty()) {
            try {
                String title = Jsoup.connect(article.getUrl())
                    .userAgent("Mozilla")
                    .get()
                    .title();
                article.setTitle(title);
            } catch (IOException e) {
                article.setTitle("タイトル取得失敗");
            }
        }
        
        repository.save(article);
    }
}
