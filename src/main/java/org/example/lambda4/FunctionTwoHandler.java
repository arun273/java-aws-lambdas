package org.example.lambda4;


import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

import java.util.Base64;

public class FunctionTwoHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private final S3Uploader s3Uploader = new S3Uploader();

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {
        String fileName = request.getQueryStringParameters().get("fileName");
        String base64Content = request.getBody();
        byte[] fileContent = Base64.getDecoder().decode(base64Content);

        s3Uploader.uploadFile("my-bucket", fileName, fileContent);

        return new APIGatewayProxyResponseEvent()
                .withStatusCode(200)
                .withBody("File uploaded to S3 with key: " + fileName);
    }
}
