package com.koshirosato.techscrap;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.List;

@WebMvcTest(ArticleController.class)
public class ArticleControllerTest {
    
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ArticleService service;

    @Test
    void testIndex() throws Exception {
        when(service.search(any())).thenReturn(List.of());
        mockMvc.perform(get("/"))
            .andExpect(status().isOk())
            .andExpect(view().name("index"));
    }

    @Test
    void testSubmit() throws Exception {
        mockMvc.perform(post("/submit")
                .param("url", "http://example.com")
                .param("title", "test title"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/"));
    }
}
