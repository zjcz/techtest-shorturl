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

### Implementation
The requirements seemed reasonably straight forward, the only part I was unsure about was how to generate the short code for the url.  A quick search found https://stackoverflow.com/questions/742013/how-do-i-create-a-url-shortener, which provided code to encode and decode a number, which could be the id column of a database record.  The idea was the url was added to the database and the identifier generated could be encoded and used as the short code.  When a user requested the url for the short code, it could be decoded back to a numeric identifier and used to lookup the record in the database.

I decided to use this idea but with one small change.  I would encode the identifier and save the short code to the database.  When the user requested the url for the short code I could lookup the record using the short code without the need to decode it.  This would make the initial adding of the url slower as it would need to be saved to the database twice, but it would be quicker to lookup the url when required as no decoding would be needed.

My first step was to create the web app using the Spring Initializr tool.  This included the Web, JPA and H2 modules.  I decided to use H2 as the backend database for this project as it is easy to setup and does not rely on a 3rd party database system being installed on the machine.  The downside of this is all the data is lost everytime the webapp is restarted (this can be prevented in config but for the purposes of this simple project we will leave it as is).

Next, I added the repository and service layer. The repository simply inherits from the CrudRepository interface and lets Spring Data JPA work its magic.  The service layer provides a wrapper around the repository, adding in validation and handles the generation of the shortUrl code.

Then I added 2 controllers, one to handle the API calls and one to resolve the shortcode and redirect to the desired url.  These are light on functionality, simply validating the input and calling the service layer to process and produce a response.
