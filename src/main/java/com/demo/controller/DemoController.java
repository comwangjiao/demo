package com.demo.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@RestController
public class DemoController {

    @GetMapping(value = "/demo/test")
    public Map<Object, Object> test(Locale locale) {
        var stringStringHashMap = new HashMap<>();
        stringStringHashMap.put("aa", "aaa");
        stringStringHashMap.put("locale", locale.getLanguage());
        return stringStringHashMap;
    }
    @GetMapping(value = "/demo/hello")
    public Map<Object, Object> hello(Locale locale) {
        var stringStringHashMap = new HashMap<>();
        stringStringHashMap.put("hello", "hello");
        stringStringHashMap.put("locale", locale.getLanguage());
        return stringStringHashMap;
    }
}
