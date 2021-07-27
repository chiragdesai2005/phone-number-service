# Phone number Service

This is responsible for providing various API methods for the phone numbers service.
Assumption - Customer service will be different service who is reposnible for maintaining customer data. This service purely deal with Phone numbers and related operations.
I have also tried to use R2DBC compare to Spring JPA which does not support entity relationships.

## Prerequisite software
  - Min. JDK v8
  - Maven v3.0+
  - Git

## Key highlights  
  - REST API endpoint using webflux - Reactive library
  - H2 database with reactive crud repository
  - Sluth - trace id logging
  - actuator - health end point (http://localhost:8080/actuator/health)
  - Mvn spotless plugin to check and format the code
  - code coverage plugin.
  - Map struct library is used to map entity and dto classes
  - Open API doc. - http://localhost:8080/v3/api-docs
  - Test cases - Mockito
  - Lombok - Getter/Setter and Builder pattern classes
  - Circuit breaker - Not needed but we can use resilient4j

#### model

This layer is divided into 2 sub parts.
- `dto`: data transfer object represents a downstream system's response schema definition.
- `response`: data transfer object represents a current system's response schema definition.

### Running tests

As part of build process, all the test cases should run and pass in order to build the artifact.

## Running the (local) server

1. Start the application: Before you start the application, you need to configure the following application yaml configuration files.
2. Go to http://localhost:8080/phone/api/v1/

## API documentation

1. It follows the open api specification approach
2. Download latest json format specification using http://localhost:8080/v3/api-docs URL.
