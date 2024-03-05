package com.nikita.taskcreateserviceforcv.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/test")
    public String getData() {
        return "data";
    }

}
