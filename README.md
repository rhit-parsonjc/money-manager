# Money Manager

## Description

This Money Manager app allows you to keep track of your financial transactions and match them with records for an account at a financial institution.

- Keep track of deposits and withdrawals from an account at a financial institution (bank records).
- Keep track of payments, donations, income, etc. (financial transactions).
- Connect bank records with financial transactions.
- Attach files to a bank record or financial transaction.
- Keep track of the amount of money in an account at a particular date.

## Pages (To Be Updated)

- /: home page, can go to login or register page (visible to everyone)
- /login: login page, can go to register, home, or accounts page (only visible to non-authenticated users)
- /register: register page, can go to login or home page (only visible to non-authenticated users)
- /accounts: accounts page, can go to account or profile page (only visible to authenticated users)
- /profile: profile page, can go to home, accounts, or edit profile page (only visible to authenticated users)
- /profile/edit: profile edit page, can go to profile page (only visible to authenticated users)
- /profile/delete: profile delete page, can go to profile or home page (only visible to authenticated users)
- /accounts/{account id}: account page, can go to accounts, records, or transactions page (only visible to authenticated users)
- /accounts/{account id}/records: records page, can go to record or create record page (only visible to authenticated users)
- /accounts/{account id}/transactions: transactions page, can go to transaction or create transaction page (only visible to authenticated users)
- /accounts/{account id}/create-record: create record page, can go to record page (only visible to authenticated users)
- /accounts/{account id}/create-transaction: create transaction page, can go to transaction page (only visible to authenticated users)
- /accounts/{account id}/records/{record id}: record page, can go to attach transactions, edit record, or delete record page (only visible to authenticated users)
- /accounts/{account id}/records/{record id}/transactions: attach transactions page, can go to record page (only visible to authenticated users)
- /accounts/{account id}/records/{record id}/edit: edit record page, can go to record page (only visible to authenticated users)
- /accounts/{account id}/records/{record id}/delete: delete record page, can go to record page (only visible to authenticated users)
- /accounts/{account id}/transactions/{transaction id}: transaction page, can go to attach records, edit transaction, or delete transaction page (only visible to authenticated users)
- /accounts/{account id}/transactions/{transaction id}/records: attach records page, can go to transaction page (only visible to authenticated users)
- /accounts/{account id}/transactions/{transaction id}/edit: edit transaction page, can go to transaction page (only visible to authenticated users)
- /accounts/{account id}/transactions/{transaction id}/delete: delete transaction page, can go to transaction page (only visible to authenticated users)

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

### CORS (Spring Security)

- Thanks to [Allow CORS with Spring Security 6.1.1 with authenticated requests](https://stackoverflow.com/questions/76682586/allow-cors-with-spring-security-6-1-1-with-authenticated-requests) for helping with CORS setup in Spring Boot with Spring Security
- Also thanks to [How to configure CORS in a Spring Boot + Spring Security application?](https://stackoverflow.com/questions/36968963/how-to-configure-cors-in-a-spring-boot-spring-security-application?rq=1) as well for helping with CORS setup in Spring Boot with Spring Security, primarily Soroosh Khodami's response
- Thanks to Spring documentation for showing how to set up CORS with Spring Security for a [Reactive](https://docs.spring.io/spring-security/reference/reactive/integrations/cors.html) or a [Servlet](https://docs.spring.io/spring-security/reference/servlet/integrations/cors.html) application
- These resources also talk about CORS in general, but did not address Spring Security specifically:
- [XMLHttpRequest blocked by CORS Policy](https://stackoverflow.com/questions/46277295/xmlhttprequest-blocked-by-cors-policy)
- [Response to preflight request doesn't pass access control check - No 'Access-Control-Allow-Origin' header](https://stackoverflow.com/questions/35588699/response-to-preflight-request-doesnt-pass-access-control-check-no-access-con)
- [Cross-Origin Resource Sharing (CORS)](https://developer.mozilla.org/en-US/docs/Web/HTTP/Guides/CORS)

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

### Pinia

- Thanks to [Mikalai Parakhnevich](https://stackoverflow.com/questions/73966166/pinia-vue3-set-on-proxy-trap-when-i-try-to-change-my-state) for noting that my getter and state variable having the same name would cause an issue

### Downloading Images

- Thanks to the following resources for describing how to download files from a REST API:
- [Code Sense](https://medium.com/@codesense/how-to-download-file-on-button-click-in-vue-or-nuxt-in-just-2-steps-a0a013b6bd8b)
- [How to download a file through an API in React?](https://stackoverflow.com/questions/56219640/how-to-download-a-file-through-an-api-in-react?rq=3)
- [handling file download from api call](https://stackoverflow.com/questions/64897447/handling-file-download-from-api-call)
- [trying to download file directly from response of an api](https://stackoverflow.com/questions/75901154/trying-to-download-file-directly-from-response-of-an-api)

### Local Storage

- Thanks to [HTML Web Storage API](https://www.w3schools.com/html/html5_webstorage.asp) and [Window localStorage](https://www.w3schools.com/jsref/prop_win_localstorage.asp) for describing how to use local storage.

### JWT Parsing

- Thanks to [Peheje](https://stackoverflow.com/questions/38552003/how-to-decode-jwt-token-in-javascript-without-using-a-library)'s answer for parsing JWT tokens.

### Verifying Date Amounts

- According to [Subset sum problem](https://en.wikipedia.org/wiki/Subset_sum_problem), trying to figure out whether a subset of some numbers equals a number is NP-complete.

### MySQL

- Thanks to [Bro Code](https://www.youtube.com/watch?v=oPV2sjMG53U) for explaining how to install MySQL.

### HTML/CSS

- Thanks to [W3Schools](https://www.w3schools.com/) for explaining HTML and CSS.

## Random Value

- [Random.org](https://www.random.org/) was used to generate the root password for the MySQL Database and the JWT secret

## Images Used

The following images are used to test the file UI.

- [receipt-6404855_1280](https://pixabay.com/illustrations/receipt-money-payment-card-6404855/): Image by [Mudassar Iqbal](https://pixabay.com/users/kreatikar-8562930/?utm_source=link-attribution&utm_medium=referral&utm_campaign=image&utm_content=6404855) from [Pixabay](https://pixabay.com//?utm_source=link-attribution&utm_medium=referral&utm_campaign=image&utm_content=6404855)
- [receipts-4542292_1280](https://pixabay.com/photos/receipts-tax-office-bank-notes-4542292/): Image by [Ioannis Karathanasis](https://pixabay.com/users/panals-3111125/?utm_source=link-attribution&utm_medium=referral&utm_campaign=image&utm_content=4542292) from [Pixabay](https://pixabay.com//?utm_source=link-attribution&utm_medium=referral&utm_campaign=image&utm_content=4542292)
