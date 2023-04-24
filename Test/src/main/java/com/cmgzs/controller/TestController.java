package com.cmgzs.controller;

import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author huangzhenyu
 * @date 2023/4/25
 */
@RestController
@RequestMapping(value = "/Test")
public class TestController {

    @GetMapping
    public Object GetMapping(@RequestParam Map<String, String> map) {
        System.out.println("GetMapping");
        return map;
    }

    @PostMapping
    public Object PostMapping(@RequestBody Map<String, String> map) {
        System.out.println("GetMapping");
        return map;
    }

    @PutMapping
    public Object PutMapping(@RequestBody Map<String, String> map) {
        System.out.println("GetMapping");
        return map;
    }

    @DeleteMapping
    public Object DeleteMapping(@RequestParam Map<String, String> map) {
        System.out.println("GetMapping");
        return map;
    }
}
