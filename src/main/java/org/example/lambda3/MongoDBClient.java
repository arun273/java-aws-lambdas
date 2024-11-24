package org.example.lambda3;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class MongoDBClient {

    private final MongoDatabase database;

    public MongoDBClient(String connectionStringValue , String dbName) {
      // Create a ConnectionString object
        ConnectionString connectionString = new ConnectionString(connectionStringValue);
        MongoClient mongoClient = MongoClients.create(MongoClientSettings.builder().applyConnectionString(connectionString).build());
        database = mongoClient.getDatabase(dbName);
    }

    public void save(String collectionName, Document document) {
        MongoCollection<Document> collection = database.getCollection(collectionName);
        collection.insertOne(document);
    }

    public Document fetch(String collectionName, String id) {
        MongoCollection<Document> collection = database.getCollection(collectionName);
        return collection.find(new Document("_id", id)).first();
    }

    public void update(String collectionName, String id, Document updates) {
        MongoCollection<Document> collection = database.getCollection(collectionName);
        collection.updateOne(new Document("_id", id), new Document("$set", updates));
    }
}
