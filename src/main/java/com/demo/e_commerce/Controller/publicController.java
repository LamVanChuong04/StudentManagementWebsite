package com.demo.e_commerce.Controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;



@RestController
@RequestMapping("/public")
public class publicController {
    
    @GetMapping("/trangchu")
    public String getMethodName() {
        return "public/index";
    }
    
}
