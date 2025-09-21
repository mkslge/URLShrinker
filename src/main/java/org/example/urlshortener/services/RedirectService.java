package org.example.urlshortener.services;

import org.apache.coyote.Response;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.bson.Document;
import org.example.urlshortener.config.MongoDBSingleton;


@RestController
@RequestMapping("")
public class RedirectService {
    MongoDBSingleton connection = MongoDBSingleton.getMongoDBConnection();
    private final int REDIRECT_NO_CACHE_STATUS_CODE = 302;

    @GetMapping("/l/{shortened_url}")
    public ResponseEntity<Void> getUrl(@PathVariable String shortened_url) {
        //check if our database contains a link

        HttpHeaders headers = new HttpHeaders();
        Document document = connection.getKey("shortened_url", shortened_url);

        if(document == null) {

            // -> if it does, we return redirect to the associated long link, and increment count
            headers.add("Location", "https://www.google.com");
        } else {
            //otherwise we need to update our analytics and then send the redirection
            String originalUrl = document.getString("original_url");
            connection.incrementUrlClickCount(shortened_url);
            headers.add("Location", originalUrl);
        }

        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }
}
