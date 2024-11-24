**package org.example.lambda1 deployment and testing**

Build the Project: mvn clean package

**Deploy to AWS Lambda**

**Create Function One** Upload the JAR to AWS Lambda and Set the handler to: **org.example.lambda1.FunctionOneHandler**

**Create Function Two** Upload the JAR to AWS Lambda and Set the handler to: **org.example.lambda1.FunctionTwoHandler**

**Setup & Create API Gateway**

For Function One, create a route like /functionOne and map it to the first Lambda function.

For Function Two, create a route like /functionTwo and map it to the second Lambda function.

**Test the Functions**

**Test FunctionOneHandler**

curl -X POST -d "Hello Function One" https://api-gateway-endpoint.com/functionOne

**Expected Response**

Function One processed the request: Hello Function One

**Test FunctionTwoHandler**

curl -X POST -d "Hello Function Two" https://api-gateway-endpoint.com/functionTwo

**Expected Response**

Function Two processed the input: Hello Function Two and External API Response: {JSON response from the external API}

**Notes**

Independent Handlers: Each Lambda function handler is self-contained and independent.
API Gateway Routes: The API Gateway directly invokes the correct Lambda function based on the route configuration.
Single JAR Deployment: Both functions are in the same JAR, simplifying the deployment process.

**-------------------------------------------------------------------------------------------------------------**

**package org.example.lambda2 deployment and testing**

Build the Project: mvn clean package

**Deploy to AWS Lambda**

Upload the generated JAR to AWS Lambda and Set the handler as: **org.example.lambda2.MainHandler**

**Setup & Create an API Gateway 2 Routes**

/functionOne: POST method linked to the Lambda function.
/functionTwo: POST method linked to the Lambda function.

**Test the Endpoints**

**Test /functionOne**

curl -X POST -d "Hello from Function One" https://api-gateway-endpoint.com/functionOne

**Expected Output**

Function One processed: Hello from Function One

**Test /functionTwo** 

curl -X POST -d "Hello from Function Two" https://api-gateway-endpoint.com/functionTwo

**Expected Output**

Function Two processed: Hello from Function Two and External API Response: {JSON response from the external API}

**Notes**

No Framework Dependency: This implementation uses plain Java 21 with minimal dependencies.
HTTP Client with Java 21: It demonstrates how to perform secure HTTP requests using HttpClient.
Single Lambda Deployment: The MainHandler routes requests based on paths, so only one AWS Lambda function is needed.

**-------------------------------------------------------------------------------------------------------------**

**package org.example.lambda3 deployment and testing**

mvn clean package

**Upload the JAR to AWS Lambda for each function**

Save MongoDB: **org.example.lambda3.FunctionSaveToMongoHandler**

Fetch MongoDB: **org.example.lambda3.FunctionFetchFromMongoHandler**

Update MongoDB: **org.example.lambda3.FunctionUpdateMongoHandler**

Upload S3: **org.example.lambda3.FunctionUploadToS3Handler**

Download S3: **org.example.lambda3.FunctionDownloadFromS3Handler**

**-------------------------------------------------------------------------------------------------------------**

**package org.example.lambda4 deployment and testing**

mvn clean package

Deploy to AWS Lambda

Create Function One  Use the same JAR file to AWS Lambda & Set the handler to : **org.example.lambda4.FunctionOneHandler**

Create Function Two Use the same JAR file for deployment & Set the handler to:  **org.example.lambda4.FunctionTwoHandler**

**Create API Gateway**  

Map /functionOne to Function One Lambda.

Map /functionTwo to Function Two Lambda.

**Test the Functions  FunctionOneHandler  & FunctionTwoHandler**

curl -X GET "https://api-gateway-endpoint.com/functionOne?key=myKey"

curl -X POST -H "Content-Type: text/plain" -d "$(base64 myfile.pdf)" "https://api-gateway-endpoint.com/functionTwo?fileName=myFile.pdf"


**Note**

This setup ensures a clean and efficient deployment of multiple AWS Lambda functions with DynamoDB and S3 integration, all packaged in the same JAR file.






