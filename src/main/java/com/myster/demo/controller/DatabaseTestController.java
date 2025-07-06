package com.myster.demo.controller;

import com.myster.demo.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/v1/db-test")
public class DatabaseTestController {
    
    @Autowired
    private DataSource dataSource;
    
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    @GetMapping("/connection")
    public Result<Map<String, Object>> testConnection() {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 测试数据源连接
            result.put("dataSourceClass", dataSource.getClass().getName());
            result.put("connectionValid", dataSource.getConnection().isValid(5));
            
            // 测试简单查询
            Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM categories", Integer.class);
            result.put("categoriesCount", count);
            
            result.put("status", "success");
            result.put("message", "数据库连接正常");
            
        } catch (Exception e) {
            result.put("status", "error");
            result.put("message", "数据库连接失败: " + e.getMessage());
            result.put("exception", e.getClass().getName());
        }
        
        return Result.success(result);
    }
    
    @GetMapping("/hello")
    public Result<String> hello() {
        return Result.success("Database Test Controller is working!");
    }
} 