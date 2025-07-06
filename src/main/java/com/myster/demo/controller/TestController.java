package com.myster.demo.controller;

import com.myster.demo.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/test")
@Slf4j
public class TestController {
    
    @GetMapping("/categories")
    public Result<String> testCategories() {
        return Result.success("分类接口测试成功");
    }
    
    @GetMapping("/dishes")
    public Result<String> testDishes() {
        return Result.success("菜品接口测试成功");
    }
} 