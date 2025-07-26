package com.example.api.authorization.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class SimpleController {

    @GetMapping("/hello")
    public String helloWorld() {
        return "Hello, World! This is a simple controller endpoint.";
    }

    @GetMapping("/status")
    public String getStatus() {
        return "Application is running normally.";
    }
}