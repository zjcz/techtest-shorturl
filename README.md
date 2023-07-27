# Tech Test-Short Url Webapp

## Introduction
This project is my solution to a tech test for a software developer interview.

#### Brief

ShortLink is a URL shortening service where you enter a URL such as https://codesubmit.io/library/react and it returns a short URL such as http://short.est/GeAi9K.

#### Tasks

-   Implement assignment using:
    -   Language: **Java**
    -   Framework: **Spring**
    -   Two API endpoints are required
        -   /encode - Encodes a URL to a shortened URL
        -   /decode - Decodes a shortened URL to its original URL.
        -   Both endpoints should return JSON
    -   Additionally, going directly to the shortened URL in a browser should redirect to the original URL.
-   There is no restriction on how your encode/decode algorithm should work. You just need to make sure that a URL can be encoded to a short URL and the short URL can be decoded to the original URL, and that the short URL is accessible and redirects to the original URL. **You can choose to either persist the short URLs to a database of your choice or keep them in memory.**
-   Provide detailed instructions on how to run your assignment in a separate markdown file
-   Provide API tests for all endpoints

#### Evaluation Criteria

-   **Java** best practices
-   API implemented featuring a /encode and /decode endpoint
-   Surfing to the short URL redirects to the original URL
-   Show us your work through your commit history
-   Completeness: did you complete the features? Are all the tests running?
-   Correctness: does the functionality act in sensible, thought-out ways?
-   Maintainability: is it written in a clean, maintainable way?

## Implementation
The requirements seemed reasonably straight forward, the only part I was unsure about was how to generate the short code for the url.  A quick search found https://stackoverflow.com/questions/742013/how-do-i-create-a-url-shortener, which provided code to encode and decode a number, which could be the id column of a database record.  The idea was the url was added to the database and the identifier generated could be encoded and used as the short code.  When a user requested the url for the short code, it could be decoded back to a numeric identifier and used to lookup the record in the database.

I decided to use this idea but with one small change.  I would encode the identifier and save the short code to the database.  When the user requested the url for the short code I could lookup the record using the short code without the need to decode it.  This would make the initial adding of the url slower as it would need to be saved to the database twice, but it would be quicker to lookup the url when required as no decoding would be needed.

My first step was to create the web app using the Spring Initializr tool.  This included the Web, JPA and H2 modules.  I decided to use H2 as the backend database for this project as it is easy to setup and does not rely on a 3rd party database system being installed on the machine.  The downside of this is all the data is lost everytime the webapp is restarted (this can be prevented in config but for the purposes of this simple project we will leave it as is).

Next, I added the repository and service layer. The repository simply inherits from the CrudRepository interface and lets Spring Data JPA work its magic.  The service layer provides a wrapper around the repository, adding in validation and handles the generation of the shortUrl code.

Then I added 2 controllers, one to handle the API calls and one to resolve the shortcode and redirect to the desired url.  These are light on functionality, simply validating the input and calling the service layer to process and produce a response.

To make the application production ready I have added the [Spring Boot Actuator Module](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#actuator.enabling), with the default config settings.  The application logs data using the [Commons Logging Module](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#features.logging) as adopted by Spring Boot.  Again this uses the default settings so will only log to the console but for full production use logging to file(s) would probably be desired.

One feature of this solution which limits the production readiness is only a single instance of the application can be run, as each instance would store urls in its own H2 in-memory database, so a url added to one instance would not be available on another.  However, as the brief states it is acceptable to store data in memory I feel this is an acceptable tradeoff under the circumstances.  Also, this problem could be resolved by either configuring the connection string of each instance to connect to the H2 database of the primary instance (See [Access the Same In-Memory H2 Database in Multiple Spring Boot Applications](https://www.baeldung.com/spring-boot-access-h2-database-multiple-apps)), or configure a standalone H2 database and have all instances connect to that.

## Installation and Running

### Prerequisites
Before cloning and running the application, ensure you have the following installed on your system:
- Java Development Kit (JDK) 17 or higher
- Git

### Get the Code
Clone the repository and navigate into the project directory:

```bash
git clone https://github.com/zjcz/techtest-shorturl.git
cd techtest-shorturl
```

### Build using Gradle
To build and run the application using Gradle, run the following command:

```bash
./gradlew bootRun
```

## Accessing the Application
Once the application is running, you can access the API using tools like Postman, cURL, etc

The default url for the application is:

```
http://localhost:8080
```

### API Endpoints
The application has 2 API endpoints, one to encode a url and one to decode a short code.
-   /encode - Encodes a URL to a shortened URL
-   /decode - Decodes a shortened URL to its original URL.

#### /encode
This endpoint takes a url as a parameter and returns a json object containing the short code for the url.  The url must be a valid url and must start with either http:// or https://.  If the url is not valid a 400 Bad Request response is returned.

For example, the following curl command will return a short code for the url https://www.example.com:

```bash
curl -X POST -H "Content-Type: application/json" -d '{"url": "https://www.example.com"}' http://localhost:8080/encode
```
This will send a POST request to the /encode endpoint with a JSON payload containing the url parameter set to https://www.example.com.  The following json response will be returned (note: the shortUrl value may differ depending on the number of short urls you have stored in the database):

```json
{
    "url":"https://www.example.com",
    "shortUrl":"aaaaaae"
}
```

#### /decode
This endpoint takes a short code as a parameter and returns a json object containing the url for the short code.  If the short code is not valid a 404 Not Found response is returned.

The following curl command will return the url for short code 'aaaaaae':

```bash
curl -X GET http://localhost:8080/decode/aaaaaae
```
This will send a GET request to the /decode endpoint with the short code of 'aaaaaae' as a query string parameter.  The following json response will be returned:

```json
{
    "url":"https://www.example.com",
    "shortUrl":"aaaaaae"
}
```

### Browser Access
To access a short url in a browser, simply navigate to the url in the browser.  For example, if the short code is 'aaaaaae' then navigate to http://localhost:8080/aaaaaae.  This will redirect you to the original url.
