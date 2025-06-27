package com.demo.controller;


import com.demo.service.ExcelParser;
import com.demo.vo.ExcelParseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@RestController
public class DemoController {

    @Autowired
    private ExcelParser excelParser;

    @GetMapping(value = "/demo/ping")
    public Map<Object, Object> ping(Locale locale) {
        var stringStringHashMap = new HashMap<>();
        stringStringHashMap.put("locale", locale.getLanguage());
        stringStringHashMap.put("test", locale.getLanguage());
        stringStringHashMap.put("test", locale.getLanguage());
        stringStringHashMap.put("date", System.currentTimeMillis());
        stringStringHashMap.put("date", System.currentTimeMillis());
        return stringStringHashMap;
    }

    @PostMapping(value = "/demo/parseExcel")
    public ExcelParseResult parseExcel(@RequestParam("file") MultipartFile file){
        try {
           return excelParser.parseExcel(file.getInputStream(), file.getOriginalFilename());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }
}
