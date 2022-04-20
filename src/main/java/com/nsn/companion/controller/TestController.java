package com.nsn.companion.controller;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api("测试")
public class TestController {

    @GetMapping("/hello")
    public String hello(){
        return "H1ello!";
    }
}
