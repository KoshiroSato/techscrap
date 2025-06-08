package com.koshirosato.techscrap;

import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ArticleController {
    private final ArticleService service;

    public ArticleController(ArticleService service) {
        this.service = service;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("articles", service.findAll());
        return "index";
    }

    @GetMapping("/form")
    public String form(Model model) {
        model.addAttribute("article", new ArticleEntity());
        return "form";
    }

    @PostMapping("/submit")
    public String submit(@ModelAttribute ArticleEntity entity) {
        service.save(entity);
        return "redirect:/";
    }
}
