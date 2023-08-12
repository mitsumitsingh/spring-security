## Session management for the stateless services using spring-session and redis.

Problem - If a user is loggedIn, But due to some reason we have started our application. 
          In these scenarios we will lose our session, and we need to log-in again. 
          To resolve this we can store session in redis and everytime application will get session from the redis.

Implementation :-
We need below dependencies
```
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-redis</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-security</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.session</groupId>
        <artifactId>spring-session-data-redis</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-docker-compose</artifactId>
    </dependency>
```

Add docker compose for the redis
```dockerfile
version: '3.1'
services:
  redis:
    image: "redis:alpine"
    ports:
      - "6379:6379"
```
Now all the sessions will be stored in redis, and our application will not face the same issue again. 

To replicate the scenario we can add a rest controller

```java
package dev.sumit.springsecurity.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class HomeController {

    private final String VIEW_COUNT = "VIEW_COUNT";

    @GetMapping("/")
    public String hello(Principal principal, HttpSession httpSession){
        incrementViewCount(httpSession, VIEW_COUNT);
        return "Welcome " + principal.getName() + " to spring session";
    }

    @GetMapping("/count")
    private String count(HttpSession session){
        return "Home controller " + session.getAttribute(VIEW_COUNT);
    }

    private void incrementViewCount(HttpSession httpSession, String attr){
        int homeViewCount = httpSession.getAttribute(attr) == null ? 0 : (int)httpSession.getAttribute(attr);
        httpSession.setAttribute(attr, homeViewCount + 1);
    }
}

```

Now we can check by hitting the home controller api and then after restarting the application also we will be able to hit the api with the previous session.

To check the sessions are stored in redis or not.

You can use the below commands
- docker exec -it NAMES redis-cli
Example :
- docker exec -it spring-security-redis-1 redis-cli

you can get the NAMES using the below command 
- docker ps