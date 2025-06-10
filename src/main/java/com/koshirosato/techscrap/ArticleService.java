package com.koshirosato.techscrap;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.springframework.stereotype.Service;

@Service
public class ArticleService {
    
    private final ArticleRepository repository;
    private final TextVectorizer vectorizer;

    public ArticleService(ArticleRepository repository, TextVectorizer vectorizer) {
        this.repository = repository;
        this.vectorizer = vectorizer;
    }

    public List<ArticleEntity> findAll() {
        return repository.findAll();
    }

    public List<ArticleEntity> search(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return findAll();
        }
        return repository.search(keyword);
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

    public void toggleStar(Long id) {
        ArticleEntity entity = repository.findById(id).orElseThrow();
        entity.setStarred(!entity.getStarred());
        repository.save(entity);
    }

    public List<ArticleEntity> findStarred() {
        return repository.findByStarredTrue();
    }

    public List<ArticleEntity> recommendSimilarArticles(int topN) {
        List<ArticleEntity> starred = repository.findByStarredTrue();
        List<ArticleEntity> unstarred = repository.findByStarredFalse(); 
        
        if (starred.isEmpty()) return Collections.emptyList();

         List<String> starredTexts = starred.stream()
            .map(a -> a.getTitle() + " " + a.getMemo())
            .collect(Collectors.toList());

        String mergedStarredText = String.join(" ", starredTexts);

        List<String> corpus = new ArrayList<>(starredTexts);
        corpus.addAll(unstarred.stream()
            .map(a -> a.getTitle() + " " + a.getMemo())
            .collect(Collectors.toList()));

        Map<String, Double> starredVec = vectorizer.tfidf(mergedStarredText, corpus);

        return unstarred.stream()
            .map(article -> Map.entry(article, vectorizer.cosineSimilarity(
                starredVec, vectorizer.tfidf(article.getTitle() + " " + article.getMemo(), corpus))))
            .sorted((a, b) -> Double.compare(b.getValue(), a.getValue()))
            .limit(topN)
            .map(Map.Entry::getKey)
            .collect(Collectors.toList());
    }
}
