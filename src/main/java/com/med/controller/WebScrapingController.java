package com.med.controller;

import com.med.model.Article;
import com.med.service.WebScrapingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class WebScrapingController {

    @Autowired
    private WebScrapingService scrapingService;

    @GetMapping("/scrape")
    public ResponseEntity<List<Article>> scrapeMedicalNews() throws IOException {
        List<Article> articles = scrapingService.scrapeMedicalNewsConcurrently();
        return new ResponseEntity<>(articles, HttpStatus.OK);
    }
}
