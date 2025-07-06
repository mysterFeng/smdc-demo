package com.myster.demo.controller;

import com.myster.demo.vo.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/v1/simple-test")
public class SimpleTestController {
    
    @GetMapping("/categories")
    public Result<List<String>> getCategories() {
        List<String> categories = Arrays.asList("热菜", "凉菜", "汤类", "主食", "饮品");
        return Result.success(categories);
    }
    
    @GetMapping("/hello")
    public Result<String> hello() {
        return Result.success("Simple Test Controller is working!");
    }
} 