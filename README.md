# FileAPI

FileAPI is an API that allows us to perform file storage, update and list operations.

## Tech Stack 

+ Java11
+ Spring Boot
+ Spring Security and JWT
+ Spring Data JPA
+ Spring Actuator
+ Lombok
+ H2 DB 
+ Open API Swagger
+ Maven

## Installation and Running

To install and run your API, use the following commands:

- mvn clean install
- mvn spring-boot:run

## Functions and Usage

Your API provides the following functions:

- User registration: POST http://localhost:8080/users/register {"username" :"username","password":"password","role":"role"}
- User login: POST http://localhost:8080/users/login {"username" :"username","password":"password"} (returns JWT token)
- File upload: POST http://localhost:8080/files/upload (content-type: multipart/form-data, body: key=file, value=file)
- File listing: GET http://localhost:8080/files/list
- File content retrieval: GET http://localhost:8080/files/content/{id} (returns byteArray)
- File deletion: DELETE http://localhost:8080/files/delete/{id}
- File update: PUT http://localhost:8080/files/update/{id} (content-type: multipart/form-data, body: key=file, value=file)

Note: To access file operations, you need to add Bearer JWT token to the authorization header.

## Documentation

You can access the Swagger documentation of your API from the following URL:

- http://{host}:8080/api-docs/swagger-ui/index.html#/