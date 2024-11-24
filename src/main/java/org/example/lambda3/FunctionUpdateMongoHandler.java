package org.example.lambda3;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import org.bson.Document;

public class FunctionUpdateMongoHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    private final MongoDBClient mongoDBClient = new MongoDBClient("mongodb+srv://<username>:<password>@cluster0.mongodb.net", "mydb");

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {
        String id = request.getQueryStringParameters().get("id");
        Document updates = Document.parse(request.getBody());
        mongoDBClient.update("mycollection", id, updates);

        return new APIGatewayProxyResponseEvent().withStatusCode(200).withBody("Data updated in MongoDB");
    }
}
