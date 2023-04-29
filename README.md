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
+ JUnit and Mockito
+ Open API Swagger
+ Maven

## Installation and Running

To install and run your API, use the following commands:

- mvn clean install
- mvn spring-boot:run
- Due to permission issues, the files folder in C should be created like C:/files/ 


### API Endpoints

| HTTP Method | Endpoint                  | Description |
|-------------|---------------------------|-------------|
| POST        | /users/register           | User registration with JSON body {"username" :"username","password":"password","role":"role"}. |
| POST        | /users/login              | User login with JSON body {"username" :"username","password":"password"} (returns JWT token). |
| POST        | /files/upload             | File upload with content-type multipart/form-data and body key file with value as the file. |
| GET         | /files/list               | File listing. |
| GET         | /files/content/{id}       | File content retrieval where {id} is the ID of the file (returns file as byteArray). |
| DELETE      | /files/delete/{id}        | File deletion where {id} is the ID of the file. |
| PUT         | /files/update/{id}        | File update with content-type multipart/form-data and body key file with value as the updated file, where {id} is the ID of the file. |

### Note

To access file operations, you need to add the JWT token received from the /users/login endpoint to the authorization header.


## Documentation

You can access your API's Swagger documentation and postman collection information from the URLs below.

- http://{host}:8080/api-docs/swagger-ui/index.html#/
- src/test/resources/ETSTUR_FILE_API.postman_collection.json