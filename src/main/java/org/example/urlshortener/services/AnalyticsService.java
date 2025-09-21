package org.example.urlshortener.services;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


import org.apache.coyote.Response;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.bson.Document;
import org.example.urlshortener.config.MongoDBSingleton;

@RestController
@RequestMapping("/api/urls/")
public class AnalyticsService {
    MongoDBSingleton connection = MongoDBSingleton.getMongoDBConnection();
    @GetMapping("analyze/{shortened_url}")
    public ResponseEntity<Integer> createUrl(@PathVariable String shortened_url) {
        Document document = connection.getKey("shortened_url", shortened_url);
        if(document == null) {
            return new ResponseEntity<>(-1, HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(document.getInteger("click_count"), HttpStatus.FOUND);
        }

    }
}
