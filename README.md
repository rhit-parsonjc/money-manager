# Money Manager

## Resources Used

### Spring Boot Basics

- Thanks to [Chinthaka Dinadasa](https://www.javatodev.com/spring-boot-mysql/) for describing how to connect MySQL to Spring Boot and how to set up a REST API in Spring Boot.
- Also thanks to [Samson Alfred's dream-shops repository](https://github.com/dailycodework/dream-shops) for Spring Boot details.
- Thanks to [baledung](https://www.baeldung.com/spring-request-param) for describing how to use the @RequestParam annotation.
- Thanks to [Attila Fejér](https://www.baeldung.com/jpa-many-to-many) for describing how to use the @ManyToMany annotation
- Thanks to [Teddy Smith](https://github.com/teddysmithdev/pokemon-review-springboot/) for showing how to use Spring
- Thanks to [Vinayak Shedgeri's answer](https://stackoverflow.com/questions/37774282/intellij-cannot-resolve-symbol-springframework) for noting the Refresh button for Maven.
- Thanks to [uvsmtid](https://stackoverflow.com/questions/45718145/intellij-errorjava-java-lang-exceptionininitializererror) for identifying a Java version discrepancy as the reason for an error

### Images in Spring Boot

- Thanks to [Lokesh Gupta](https://howtodoinjava.com/spring-boot/spring-boot-file-upload-rest-api/) for a bit about how to upload images to a REST API.
- Thanks to [Spring](https://spring.io/guides/gs/uploading-files) for some assistance with uploading images, but not directly.
- Thanks to [bezdoker](https://www.bezkoder.com/spring-boot-upload-file-database/) for describing how to upload images to a REST API. This is the one that is closest to my implementation, as well as Simpson Alfred's.

### Lombok Annotations in IntelliJ

- Thanks to [Abhishek Sah's second workaround](https://intellij-support.jetbrains.com/hc/en-us/community/posts/23064675521682/comments/23172277328018) to fix the Lombok issue in IntelliJ. For some reason, applying it seemed to fix the Lombok compilation issue, yet undoing the fix has not caused it to come up again.

### CORS

- I looked at these Stack overflow pages to identify that the issue was a CORS issue and would not be trivial to fix:
- [Why does my JavaScript code receive a "No 'Access-Control-Allow-Origin' header is present on the requested resource" error, while Postman does not?](https://stackoverflow.com/questions/20035101/why-does-my-javascript-code-receive-a-no-access-control-allow-origin-header-i)
- [No 'Access-Control-Allow-Origin' header is present on the requested resource—when trying to get data from a REST API](https://stackoverflow.com/questions/43871637/no-access-control-allow-origin-header-is-present-on-the-requested-resource-whe)
- These resources describe the basics of CORS:
- [CORS in 100 Seconds](https://www.youtube.com/watch?v=4KHiSt0oLJ0)
- [Learn CORS In 6 Minutes](https://www.youtube.com/watch?v=PNtFSVU-YTI)
- Thanks to [Sébastien Deleuze](https://spring.io/blog/2015/06/08/cors-support-in-spring-framework) for the solution.

### Spring Data Stack Overflow

- Thanks to [Andrey B. Panfilov](https://stackoverflow.com/questions/73078524/java-lang-stackoverflowerror-while-saving-jpa-entities) for addressing a Stack Overflow exception.
- Thanks to [Grzegorz Oledzki and damian](https://stackoverflow.com/questions/1082095/how-to-remove-entity-with-manytomany-relationship-in-jpa-and-corresponding-join?rq=3) for handling a Many-to-Many relationship.
- Thanks to [Failed to load driver class com.mysql.jdbc.Driver](https://stackoverflow.com/questions/52804228/failed-to-load-driver-class-com-mysql-jdbc-driver) and [Failed to load driver class org.h2.Driver from HikariConfig class classloader jdk.internal.loader.ClassLoaders$AppClassLoader](https://stackoverflow.com/questions/75568456/failed-to-load-driver-class-org-h2-driver-from-hikariconfig-class-classloader-jd/75568519) for suggesting that missing the MySQL dependency caused the error
- Thanks to [Executing an update/delete query; nested exception is javax.persistence.TransactionRequiredException: Executing an update/delete query](https://stackoverflow.com/questions/68662951/executing-an-update-delete-query-nested-exception-is-javax-persistence-transact) for noting the @Transactional annotation

### Fonts

- Thanks to Brenda Gallo for the font [Happy Monkey](https://fonts.google.com/specimen/Happy+Monkey), licensed under [SIL Open Font License, version 1.1](https://openfontlicense.org/open-font-license-official-text/).
- Thanks to Pablo Impallari for the font [Libre Baskerville](https://fonts.google.com/specimen/Libre+Baskerville), licensed under [SIL Open Font License, version 1.1](https://openfontlicense.org/open-font-license-official-text/).
- Thanks to Dalton Maag for the font [Ubuntu](https://fonts.google.com/specimen/Ubuntu), licensed under [UBUNTU Font License Version 1.0](https://ubuntu.com/legal/font-licence).

### HTTP Status Codes

- Thanks to [HTTP response status codes](https://developer.mozilla.org/en-US/docs/Web/HTTP/Status) for describing the meaning of HTTP status codes.

### File Uploading

- Thanks to [how to upload a file to my server using html](https://stackoverflow.com/questions/5628011/how-to-upload-a-file-to-my-server-using-html) for setting up a file upload form
- Thanks to [API4AI](https://medium.com/@API4AI/post-a-file-via-http-request-the-ultimate-guide-b23fb70a3f73) for describing how to use axios to upload a file.

### Docker

- Thanks to vladimirvaca's [dockerize-vue-3 repository](https://github.com/vladimirvaca/dockerize-vue-3/tree/main) for providing an example of how to use Vue with Docker.
- Thanks to [Deploy Spring Boot and MySQL using Docker compose](https://stackoverflow.com/questions/78208727/deploy-spring-boot-and-mysql-using-docker-compose) for detailing how to use Docker Compose with Spring Boot and MySQL
- Also see [Communications link failure , Spring Boot + MySql +Docker + Hibernate](https://stackoverflow.com/questions/58880998/communications-link-failure-spring-boot-mysql-docker-hibernate?rq=1) for more information. I don't know if `docker-compose down --rmi all` was necessary.
- There are other webpages I looked at when configuring Docker, but these three are probably the most helpful ones.
- Also thanks to [Zeitounator] (https://stackoverflow.com/questions/59838692/mysql-root-password-is-set-but-getting-access-denied-for-user-rootlocalhost) for noting that the volume needs to be deleted to fix a database issue and describing how to do so.
- According to [super](https://stackoverflow.com/questions/68151318/make-an-api-call-from-one-container-to-another/68151409), to reach another container, you can use the container name as the hostname. However, thanks to David Maze for ["Unknown host" error calling containerized backend from frontend](https://stackoverflow.com/questions/77060233/unknown-host-error-calling-containerized-backend-from-frontend), which describes in detail the issue with this in the browser and how to use an HTTP reverse proxy to fix it, and for [docker-frontend-hostnames](https://github.com/dmaze/docker-frontend-hostnames), which provides sample code.

### Spring Security

- Thanks to [Geeks for Geeks](https://www.geeksforgeeks.org/spring-boot-3-0-jwt-authentication-with-spring-security-using-mysql-database/) for explaining Spring Security
- Thanks to [FreeCodeCamp](https://www.youtube.com/watch?v=oGhc5Z-WJSw) for demonstrating how to use Spring Boot, but primarily for Spring Security
- Thanks to [DaoAuthenticationException](https://docs.spring.io/spring-security/site/docs/current/api/org/springframework/security/authentication/dao/DaoAuthenticationProvider.html) for fixing the DaoAuthenticationProvider deprecation issue
- Thanks to [JWT Decoder](https://jwt.io/) for viewing JWT contents

### IntelliJ

- Thanks to [StackOverflow](https://stackoverflow.com/questions/75689637/idea-intellij-errorjava-error-release-version-20-not-supported-maven-compil) for fixing the Java version issue

### Spring Testing

- Thanks to [Abderrahim Azhrioun](https://www.baeldung.com/spring-boot-h2-jdbcsqlsyntaxerrorexception-expected-identifier) for noting that the H2 issue could be due to a reserved word. According to [H2 Documentation](https://h2database.com/html/advanced.html#keywords), these keywords are YEAR, MONTH, and DAY.
- Thanks to [W3Schools](https://www.w3schools.com/java/java_advanced_sorting.asp) for explaining how to use Java sorting
- Thanks to [Ali Bouali](https://www.youtube.com/watch?v=uGZQdD9IpQc) for explaining Spring testing

## Random Value

- [Random.org](https://www.random.org/) was used to generate the root password for the MySQL Database and the JWT secret

## Images Used

The following images are used to test the file UI.

- [receipt-6404855_1280](https://pixabay.com/illustrations/receipt-money-payment-card-6404855/): Image by [Mudassar Iqbal](https://pixabay.com/users/kreatikar-8562930/?utm_source=link-attribution&utm_medium=referral&utm_campaign=image&utm_content=6404855) from [Pixabay](https://pixabay.com//?utm_source=link-attribution&utm_medium=referral&utm_campaign=image&utm_content=6404855)
- [receipts-4542292_1280](https://pixabay.com/photos/receipts-tax-office-bank-notes-4542292/): Image by [Ioannis Karathanasis](https://pixabay.com/users/panals-3111125/?utm_source=link-attribution&utm_medium=referral&utm_campaign=image&utm_content=4542292) from [Pixabay](https://pixabay.com//?utm_source=link-attribution&utm_medium=referral&utm_campaign=image&utm_content=4542292)
