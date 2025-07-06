package com.myster.demo.controller;

import com.myster.demo.vo.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/category-test")
public class CategoryTestController {
    
    @GetMapping("/hello")
    public Result<String> hello() {
        return Result.success("Category Test Controller is working!");
    }
} 