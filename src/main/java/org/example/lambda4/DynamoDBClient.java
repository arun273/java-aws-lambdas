package org.example.lambda4;


import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest;
import software.amazon.awssdk.services.dynamodb.model.GetItemResponse;
import software.amazon.awssdk.services.dynamodb.model.ResourceNotFoundException;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.Map;

public class DynamoDBClient {
    private final DynamoDbClient dynamoDbClient = DynamoDbClient.create();

    public Map<String, AttributeValue> getItem(String tableName, String keyName, String keyValue) {
        try {
            GetItemRequest request = GetItemRequest.builder()
                    .tableName(tableName)
                    .key(Map.of(keyName, AttributeValue.builder().s(keyValue).build()))
                    .build();

            GetItemResponse response = dynamoDbClient.getItem(request);
            return response.item();
        } catch (ResourceNotFoundException e) {
            throw new RuntimeException("DynamoDB table not found: " + e.getMessage());
        }
    }
}
