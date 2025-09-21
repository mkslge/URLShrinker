package org.example.urlshortener.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Updates;
import org.bson.Document;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.eq;

public class MongoDBSingleton {
    static MongoDBSingleton instance;
    private MongoDatabase database;
    private MongoCollection<Document> collection;


    private MongoDBSingleton() {
        String connectionString = AppConfig.getProperty("database.url");
        MongoClient mongoClient = MongoClients.create(connectionString);
        database = mongoClient.getDatabase(AppConfig.getProperty("database.name"));
        collection =  database.getCollection(AppConfig.getProperty("database.collection"));



    }

    public static MongoDBSingleton getMongoDBConnection() {
        if(instance == null) {
            instance = new MongoDBSingleton();
        }
        return instance;
    }

    public MongoDatabase getDatabase() {
        return this.database;
    }

    public MongoCollection<Document> getDocumentCollection() {
        return this.collection;
    }

    public boolean contains(String key, String value) {
        return collection.find(eq(key, value.trim())).iterator().hasNext();
    }

    public Document getKey(String field, String key) {
        return collection.find(eq(field, key.trim())).first();

    }

    public String getShortenedUrl(String originalUrl) {
        Document document = this.getKey("original_url", originalUrl);
        return document.getString("shortened_url");
    }

    public String getOriginalUrl(String shortenedUrl) {
        Document document = this.getKey("shortened_url", shortenedUrl);
        return document.getString("original_url");
    }

    public int getOriginalCount(String shortenedUrl) {
        Document document = this.getKey("shortened_url", shortenedUrl);
        return document.getInteger("click_count");
    }

    public void incrementUrlClickCount(String shortenedUrl) {
        collection.updateOne(eq("shortened_url", shortenedUrl), Updates.inc("click_count", 1));
    }
}
