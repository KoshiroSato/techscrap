package com.koshirosato.techscrap;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class ArticleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    
    @Column(unique = true)
    private String url;
    private String memo;

    @Column(length = 4096)
    private String ogImageUrl;

    @Column(nullable = false)
    private boolean starred = false;

    // Getter / Setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }
    public String getMemo() { return memo; }
    public void setMemo(String memo) { this.memo = memo; }
    public String getOgImageUrl() { return ogImageUrl; }
    public void setOgImageUrl(String ogImageUrl) { this.ogImageUrl = ogImageUrl; }
    public boolean getStarred() { return starred; }
    public void setStarred(boolean starred) { this.starred = starred; }
}
