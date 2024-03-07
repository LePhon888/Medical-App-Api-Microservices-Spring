package com.med.service;

import com.med.model.Article;
import com.med.model.Category;
import com.med.model.Post;
import com.med.repository.CategoryRepository;
import com.med.repository.PostRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;


@Service
public class WebScrapingService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private PostRepository postRepository;
    public List<Article> scrapeMedicalNewsConcurrently() {
        String baseUrl = "https://hellobacsi.com/categories/";
        System.out.println("baseUrl " + baseUrl);

        List<Article> articles = new ArrayList<>();
        try {
            Document document = Jsoup.connect(baseUrl).get();
            Elements liElements = document.select("div.sc-cb991c61-5");
            System.out.println("liElements " + liElements);

            for (Element li : liElements) {
                String link = li.select("a.sc-fatcLD").attr("href");
                String categoryName = li.select("p.category_name").text();

                Category cat = new Category();
                cat.setName(categoryName);
                categoryRepository.save(cat);

                articles = scrapeItem(link, cat);
            }
            return articles;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public List<Article> scrapeItem(String slug, Category cat) {
        String baseUrl = "https://hellobacsi.com/" + slug;
        List<Article> articles = new ArrayList<>();
        AtomicInteger articleId = new AtomicInteger(1);

        try {
            Document document = Jsoup.connect(baseUrl).get();
            Elements liElements = document.select("article._6lkfLYy");
            List<Future<Article>> futures = new ArrayList<>();

            ExecutorService executorService = Executors.newFixedThreadPool(30);

            for (Element li : liElements) {
                String link = li.select("a.HgbW6-m").attr("href");

                String fullUrl = baseUrl + link;

                Callable<Article> scrapingTask = () -> {
                    Document articleDocument = Jsoup.connect(fullUrl).get();

                    Element articleHeader = articleDocument.select("h1.xzO2I0N").first();
                    Element articleContent = articleDocument.select("div.unique-content-wrapper").first();
                    Element articleImage = articleDocument.select("img.alignnone").first();
                    Element articleAuthor = articleDocument.select("span.mantine-sjk8hh").first();
                    Element articleAuthorImage = articleDocument.select("img.mantine-9rx0rd").first();
                    Element articleDate = articleDocument.select("span.mantine-sjk8hh").first();
                    replaceATagsWithPTags(articleContent);

                    if (articleHeader != null &&
                            articleContent != null &&
                            articleImage != null &&
                            articleAuthor != null &&
                            articleDate != null) {
                        String header = articleHeader.text();
                        String content = articleContent.html();
                        String image = articleImage.attr("src");
                        String author = articleAuthor.text();
                        String authorImage;
                        if (articleAuthorImage != null) {
                            authorImage = articleAuthorImage.attr("src");
                        } else
                            authorImage = null;
                        String date = articleDate.text().trim();
                        Post newPost = new Post();
                        newPost.setHeader(header);
                        newPost.setContent(content);
                        newPost.setAuthor(author);
                        newPost.setCreatedDate(date);
                        newPost.setImage(image);
                        newPost.setCategory(cat);
                        this.postRepository.save(newPost);
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
    private void replaceATagsWithPTags(Element element) {
        Elements aTags = element.select("a");

        for (Element aTag : aTags) {
            aTag.select("br").remove();

            Element pTag = new Element("span").text(aTag.text());

            aTag.replaceWith(pTag);
        }
    }
}
