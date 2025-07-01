package com.alibou.security.test;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/bexsex")
public class TestController {

    @GetMapping
    public String testRequest() {
        return "5 nights with bex";
    }
}
