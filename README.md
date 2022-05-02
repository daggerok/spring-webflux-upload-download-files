# Spring Boot Webflux files Upload / Download [![tests](https://github.com/daggerok/spring-webflux-upload-download-files/actions/workflows/tests.yml/badge.svg)](https://github.com/daggerok/spring-webflux-upload-download-files/actions/workflows/tests.yml)
This repository demonstrates how to upload and download files using Spring WebFlux

```bash
./mvnw clean ; ./mvnw clean compile spring-boot:start
http --form --multipart --boundary=xoxo post :8080/api/v1/upload file@README.md
http :8080/api/v1/uploads
http -f                                 post :8080/api/v1/download/README.md    > target/README.md
./mvnw spring-boot:stop
```

## RTFM
* [How to stream file from Multipart/form-data in Spring WebFlux](https://errorsfixing.com/how-to-stream-file-from-multipart-form-data-in-spring-webflux/)
* [JDK Version Range](https://github.com/spring-projects/spring-framework/wiki/Spring-Framework-Versions#jdk-version-range)
* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.6.7/maven-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/2.6.7/maven-plugin/reference/html/#build-image)
* [Coroutines section of the Spring Framework Documentation](https://docs.spring.io/spring/docs/5.3.19/spring-framework-reference/languages.html#coroutines)
* [Spring Reactive Web](https://docs.spring.io/spring-boot/docs/2.6.7/reference/htmlsingle/#web.reactive)
* [Spring Configuration Processor](https://docs.spring.io/spring-boot/docs/2.6.7/reference/htmlsingle/#configuration-metadata-annotation-processor)
* [Building a Reactive RESTful Web Service](https://spring.io/guides/gs/reactive-rest-service/)
