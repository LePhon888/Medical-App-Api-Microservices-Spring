package com.med.service;

import com.med.model.Article;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class WebScrapingService {

    public List<Article> scrapeMedicalNewsConcurrently() {
        String baseUrl = "https://www.medicalnewstoday.com/news";
        List<Article> articles = new ArrayList<>();
        AtomicInteger articleId = new AtomicInteger(1); // Initialize the article ID counter

        try {
            Document document = Jsoup.connect(baseUrl).get();

            Elements liElements = document.select("li.css-kbq0t");

            ExecutorService executorService = Executors.newFixedThreadPool(30);

            List<Future<Article>> futures = new ArrayList<>();

            for (Element li : liElements) {
                String link = li.select("a.css-1mttucs").attr("href");

                String fullUrl = "https://www.medicalnewstoday.com" + link;

                // Create a Callable task for scraping this article
                Callable<Article> scrapingTask = () -> {
                    Document articleDocument = Jsoup.connect(fullUrl).get();

                    Element articleHeader = articleDocument.select("h1").first();
                    Element articleContent = articleDocument.select("div.css-z468a2").first();
                    Element articleImage = articleDocument.select("[data-share-url]").first();
                    Element articleAuthor = articleDocument.select("a.css-1dkklb9").first();
                    Element articleAuthorImage = articleDocument.select("div.css-1edxg1f img").first();
                    Element articleDate = articleDocument.select("section[data-testid=byline] span:contains(on)").first();

                    System.out.println("articleAuthorImage " + articleAuthorImage);
                    if (articleHeader != null &&
                            articleContent != null &&
                            articleImage != null &&
                            articleAuthor != null &&
                            articleDate != null) {
                        String header = articleHeader.text();
                        String content = articleContent.text();
                        String image = articleImage.attr("data-share-url");
                        String author = articleAuthor.text();
                        String authorImage;
                        if (articleAuthorImage != null) {
                            authorImage = articleAuthorImage.attr("src");
                        }
                        else
                            authorImage = null;
                        String date = articleDate.text().trim();
                        return new Article(articleId.getAndIncrement(), header, author, authorImage, date, image, content);
                    }
                    return null;
                };

                // Submit the scraping task and store the Future object
                futures.add(executorService.submit(scrapingTask));
            }

            // Wait for all tasks to complete and collect results
            for (Future<Article> future : futures) {
                Article article = future.get(); // This will block until the task is done
                if (article != null) {
                    articles.add(article);
                }
            }

            executorService.shutdown();

            return articles;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
