package com.demo.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@RestController
public class DemoController {


    @GetMapping(value = "/demo/ping")
    public Map<Object, Object> ping(Locale locale) {
        var stringStringHashMap = new HashMap<>();
        stringStringHashMap.put("locale", locale.getLanguage());
        stringStringHashMap.put("test", locale.getLanguage());
        stringStringHashMap.put("date", System.currentTimeMillis());
        return stringStringHashMap;
    }
}
