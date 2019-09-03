# Difftool

**`difftool`**  is a Java and Spring boot application that will let you know information about two strings

## Starting the application

You can use the following command to start the application from the root folder
```bash
./gradlew bootRun
```

To submit the first string you wish to compare, you send a POST Request to 
```http request
http://localhost:8080/v1/diff/{id}/left
```
where the id parameter is the identifier for this operation and the body of the Request looks like this: 
```http request
{"base64encoded":"dGVtcDI="}
```
The value of base64encoded should be a base64encoded string. Remember to set your Headers correctly
```http request
Content-Type →application/json;charset=UTF-8
```

To submit the second string you follow the same steps as before, but instead of hitting the left endpoint to hit the right
```http request
http://localhost:8080/v1/diff/{id}/right
```
The id of the first operation to the left must match the id of the second operation to the right

## Checking the result
To find out the result of the diff you hit the following endpoint with a GET Request
```http request
http://localhost:8080/v1/diff/{id}/
```
The id must match the one of the first two operation

### Unit Testing 
To run the unit tests you can run the following command
```bash
./gradlew test
```

### Integration Testing
To run the integration tests you can run the following command
```bash
./gradlew integration
```
