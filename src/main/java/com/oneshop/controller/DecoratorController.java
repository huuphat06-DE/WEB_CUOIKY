package com.oneshop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Điều hướng template Sitemesh Decorator
 */
@Controller
@RequestMapping("/decorators")
public class DecoratorController {

    @GetMapping("/client")
    public String clientDecorator() {
        return "decorators/client";
    }

    @GetMapping("/admin")
    public String adminDecorator() {
        return "decorators/admin";
    }
}
