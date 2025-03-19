# Money Manager

## Resources Used

### Spring Boot Basics

- Thanks to [Chinthaka Dinadasa](https://www.javatodev.com/spring-boot-mysql/) for describing how to connect MySQL to Spring Boot and how to set up a REST API in Spring Boot.
- Also thanks to [Simpson Alfred's dream-shops repository](https://github.com/dailycodework/dream-shops) for Spring Boot details.
- Thanks to [baledung](https://www.baeldung.com/spring-request-param) for describing how to use the @RequestParam annotation.
- Thanks to [Attila Fejér](https://www.baeldung.com/jpa-many-to-many) for describing how to use the @ManyToMany annotation

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

### Stack Overflow

- Thanks to [Andrey B. Panfilov](https://stackoverflow.com/questions/73078524/java-lang-stackoverflowerror-while-saving-jpa-entities) for addressing a Stack Overflow exception.
- Thanks to [Grzegorz Oledzki and damian](https://stackoverflow.com/questions/1082095/how-to-remove-entity-with-manytomany-relationship-in-jpa-and-corresponding-join?rq=3) for handling a Many-to-Many relationship.

### Fonts

- Thanks to Brenda Gallo for the font [Happy Monkey](https://fonts.google.com/specimen/Happy+Monkey), licensed under [SIL Open Font License, version 1.1](https://openfontlicense.org/open-font-license-official-text/).
- Thanks to Pablo Impallari for the font [Libre Baskerville](https://fonts.google.com/specimen/Libre+Baskerville), licensed under [SIL Open Font License, version 1.1](https://openfontlicense.org/open-font-license-official-text/).
- Thanks to Dalton Maag for the font [Ubuntu](https://fonts.google.com/specimen/Ubuntu), licensed under [UBUNTU Font License Version 1.0](https://ubuntu.com/legal/font-licence).

### HTTP Status Codes

- Thanks to [HTTP response status codes](https://developer.mozilla.org/en-US/docs/Web/HTTP/Status) for describing the meaning of HTTP status codes.

### File Uploading

- Thanks to [how to upload a file to my server using html](https://stackoverflow.com/questions/5628011/how-to-upload-a-file-to-my-server-using-html) for setting up a file upload form
- Thanks to [API4AI](https://medium.com/@API4AI/post-a-file-via-http-request-the-ultimate-guide-b23fb70a3f73) for describing how to use axios to upload a file.

## Images Used

The following images are used to test the file UI.

- [receipt-6404855_1280](https://pixabay.com/illustrations/receipt-money-payment-card-6404855/): Image by [Mudassar Iqbal](https://pixabay.com/users/kreatikar-8562930/?utm_source=link-attribution&utm_medium=referral&utm_campaign=image&utm_content=6404855) from [Pixabay](https://pixabay.com//?utm_source=link-attribution&utm_medium=referral&utm_campaign=image&utm_content=6404855)
- [receipts-4542292_1280](https://pixabay.com/photos/receipts-tax-office-bank-notes-4542292/): Image by [Ioannis Karathanasis](https://pixabay.com/users/panals-3111125/?utm_source=link-attribution&utm_medium=referral&utm_campaign=image&utm_content=4542292) from [Pixabay](https://pixabay.com//?utm_source=link-attribution&utm_medium=referral&utm_campaign=image&utm_content=4542292)
