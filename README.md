# Sample Spring Cloud Gateway Project

The most basic spring boot microservice

Build Tool: Gradle
Language: Kotlin
Spring Boot Version: 2.4
Tags: Gateway,OpenApi,Actuator,JIB,ktlint,aciidoc

## How to run?

```
gradle bootRun
````
Server is running so actuator endpoints are responsive.
```
GET http://localhost:8080/actuator/info 
```

```json
{"git":{"branch":"master","commit":{"id":"1adc0ab","time":"2021-02-20T18:33:37Z"}},"build":{"operatingSystem":"Mac OS X (10.16)","artifact":"spring-boot-gateway","by":"canyaman","group":"me.yaman.can","basePackage":"me.yaman.can.demo","version":"0.0.1-SNAPSHOT","continuousIntegration":"false","build":"0","machine":"Can-MacBook-Pro","name":"spring-boot-gateway","time":"2021-02-20T19:12:18.116Z"}}
```

```
GET http://localhost:8080/actuator/health
```
```json
{"status":"UP"}
```

## Hot to develop?
First of all, build project after every file changes.
As a background task run the task on the console
```
gradle build --continuous
```

While continuous build running, run the application (console or IDE)
```
gradle bootRun
```
Then you can utilize live reload feature 

## How to validate?

```
gradle test
gradle ktlint
```

## How to deploy?

Build local docker registery container image
```
gradle jibDockerBuild
```
Then run application inside container
```
docker run -p 80:8080 spring-boot-gateway
```

```
GET http://localhost/actuator/info   
GET http://localhost/actuator/health  
```

## Documentation

Project is structered as to generate microservice documentation. OpenApi v3 former swagger doc is also included. 
Swagger-UI is awesome tool but not satisfy all requirements. You can enrich openapi doc output via annotation but I don't perefer that. 
These annotations contradict the some principles. 
Without these annotations function name and functional annotation should express the meaning 
beacuse of that I need another level of layer for the documentation. 
In addition to that I don't want to write down the same info again and again in another format. 
At the end code generated openapi doc file converted to asciidoc and enriched by document generation step. 

## Accessing documentation

You can upload document at the central place like confluence but confluence gradle plugin and 
cwiki converter has some issue just because of that I prefer self contained version.
You can access api doc from swagger doc desctiontion field link or just *http://localhost:8080/asciidoc/index.html* link.
