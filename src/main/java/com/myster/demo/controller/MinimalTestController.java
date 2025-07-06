package com.myster.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/v1/minimal-test")
public class MinimalTestController {
    
    @GetMapping("/hello")
    public Map<String, Object> hello() {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "success");
        result.put("data", "Minimal Test Controller is working!");
        result.put("timestamp", System.currentTimeMillis());
        return result;
    }
    
    @GetMapping("/categories")
    public Map<String, Object> getCategories() {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "success");
        result.put("data", new String[]{"热菜", "凉菜", "汤类", "主食", "饮品"});
        result.put("timestamp", System.currentTimeMillis());
        return result;
    }
} 