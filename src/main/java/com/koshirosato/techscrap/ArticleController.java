package com.koshirosato.techscrap;

import org.springframework.ui.Model;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ArticleController {
    
    private final ArticleService service;

    public ArticleController(ArticleService service) {
        this.service = service;
    }

    @GetMapping("/")
    public String index(@RequestParam(name = "q", required = false) String keyword, Model model) {
        model.addAttribute("articles", service.search(keyword));
        model.addAttribute("keyword", keyword);
        return "index";
    }

    @GetMapping("/form")
    public String form(Model model) {
        model.addAttribute("article", new ArticleEntity());
        return "form";
    }

    @PostMapping("/submit")
    public String submit(@ModelAttribute ArticleEntity entity, Model model) {
        try {
            String ogImageUrl = OGPUtils.fetchOGImage(entity.getUrl());
            entity.setOgImageUrl(ogImageUrl);
            service.save(entity);
            return "redirect:/";
        } catch (Exception e) {
            model.addAttribute("article", entity);
            model.addAttribute("errorMessage", "無効なURLです。正しいURLを入力してください。");
            return "form";
        }
    }

    @PostMapping("/delete/{id}")
    public String deleteArticle(@PathVariable Long id) {
        service.deleteArticleById(id);
        return "redirect:/";
    }

    @GetMapping("/starred")
    public String starred(Model model) {
        model.addAttribute("articles", service.findStarred());
        return "index";
    }

    @PostMapping("/star/{id}")
    public String toggleStar(@PathVariable Long id, @RequestHeader("Referer") String referer) {
        service.toggleStar(id);
        return "redirect:" + referer;
    }

    @GetMapping("/recommend")
    public String recommend(Model model) {
        List<ArticleEntity> recommended = service.recommendSimilarArticles(5);
        model.addAttribute("articles", recommended);
        return "index";
    }
}
