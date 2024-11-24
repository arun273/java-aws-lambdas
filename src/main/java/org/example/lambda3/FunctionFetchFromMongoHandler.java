package org.example.lambda3;


import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

public class FunctionFetchFromMongoHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    private final MongoDBClient mongoDBClient = new MongoDBClient("mongodb+srv://<username>:<password>@cluster0.mongodb.net", "mydb");

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {
        String id = request.getQueryStringParameters().get("id");
        var result = mongoDBClient.fetch("mycollection", id);

        return new APIGatewayProxyResponseEvent().withStatusCode(200).withBody(result.toJson());
    }
}
