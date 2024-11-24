package org.example.lambda3;


import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

import java.io.InputStream;

public class FunctionDownloadFromS3Handler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    private final AWSS3Client s3Client = new AWSS3Client();

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {
        String fileName = request.getQueryStringParameters().get("fileName");
        InputStream fileContent = s3Client.downloadFile("my-bucket", fileName);

        return new APIGatewayProxyResponseEvent().withStatusCode(200).withBody(fileContent.toString());
    }
}
