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

    public void save(ArticleEntity entity) {
        // 記事タイトル自動取得
        if (entity.getTitle() == null || entity.getTitle().isEmpty()) {
            try {
                String title = Jsoup.connect(entity.getUrl())
                    .userAgent("Mozilla")
                    .get()
                    .title();
                entity.setTitle(title);
            } catch (IOException e) {
                entity.setTitle("タイトル取得失敗");
            }
        }
        // 重複した記事は登録できない
        if (repository.findByUrl(entity.getUrl()).isPresent()) {
            return;
        }
        
        repository.save(entity);
    }

    public void deleteArticleById(Long id) {
        repository.deleteById(id);
    }
}
