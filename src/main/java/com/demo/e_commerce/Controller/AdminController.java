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
    @GetMapping("/dashboard/user")
    public String adminUser() {
        return "admin/user";
    }
    @GetMapping("/dashboard/product")
    public String adminProduct() {
        return "admin/product";
    }
    @GetMapping("/dashboard/order")
    public String adminOrder() {
        return "admin/order";
    }

    
}
