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
