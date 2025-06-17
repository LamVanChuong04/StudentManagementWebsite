package com.demo.e_commerce.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;



@Controller
@RequestMapping("/admin")
public class AdminController {
    
    @GetMapping("/dashboard")
    public String adminIndex() {
        return "admin/dashboard";
    }
    @GetMapping("/user")
    public String adminUser() {
        return "admin/user";
    }
    @GetMapping("/product")
    public String adminProduct() {
        return "admin/product";
    }
    @GetMapping("/order")
    public String adminOrder() {
        return "admin/order";
    }

    
}
