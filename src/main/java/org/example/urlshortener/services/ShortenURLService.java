package org.example.urlshortener.services;

import org.apache.coyote.Response;
import org.bson.Document;
import org.example.urlshortener.config.MongoDBSingleton;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Random;

import static com.mongodb.client.model.Filters.eq;


@RestController
@RequestMapping("/api/urls")
public class ShortenURLService {
    private final static String BASE_62_SET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private final static int URL_LENGTH = 6;
    private static final MongoDBSingleton connection = MongoDBSingleton.getMongoDBConnection();


    @PostMapping("create")
    public ResponseEntity<String> createUrl(@RequestBody Map<String, String> request) {
        String originalUrl = request.get("original_url");
        String shortenedUrl;

        if(connection.contains("original_url", originalUrl)) {
            shortenedUrl = connection.getShortenedUrl(originalUrl);
            return ResponseEntity.status(HttpStatus.CREATED).body(shortenedUrl);
        } else {
            shortenedUrl = ShortenURLService.createUniqueShortURL();
            ShortenURLService.addUrlToDB(shortenedUrl, originalUrl);
        }


        return ResponseEntity.status(HttpStatus.CREATED).body(shortenedUrl);
    }


    private static String createUniqueShortURL() {
        boolean createdUniqueUrl = false;
        String uniqueUrl = "";
        do {
            String url = ShortenURLService.createShortURL();
            if(!connection.contains("shortened_url", url)) {
                uniqueUrl = url;
                createdUniqueUrl = true;
            }
        } while(!createdUniqueUrl);

        return uniqueUrl;
    }

    private static String createShortURL() {
        StringBuilder urlAccumulator = new StringBuilder();
        Random random = new Random();
        for(int i = 0 ;i < URL_LENGTH;i++) {
            char nextChar = BASE_62_SET.charAt(random.nextInt(BASE_62_SET.length()));
            urlAccumulator.append(nextChar);
        }
        return urlAccumulator.toString();
    }

    private static void addUrlToDB(String shortenedUrl, String originalUrl) {
        Document toAdd = new Document("shortened_url", shortenedUrl)
                .append("original_url", originalUrl)
                .append("click_count", 0);
        connection.getDocumentCollection().insertOne(toAdd);
    }

}
