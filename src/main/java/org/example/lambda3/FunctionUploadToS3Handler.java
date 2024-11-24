package org.example.lambda3;


import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

import java.util.Base64;

public class FunctionUploadToS3Handler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    private final AWSS3Client s3Client = new AWSS3Client();

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {
        String fileName = request.getQueryStringParameters().get("fileName");
        byte[] fileContent = Base64.getDecoder().decode(request.getBody());
        s3Client.uploadFile("my-bucket", fileName, fileContent);

        return new APIGatewayProxyResponseEvent().withStatusCode(200).withBody("File uploaded to S3");
    }
}
