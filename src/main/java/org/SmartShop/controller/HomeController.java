package org.SmartShop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.RedirectView;

import java.util.HashMap;
import java.util.Map;

@Controller
public class HomeController {

    @GetMapping("/")
    public RedirectView home() {
        // Redirect to Swagger UI
        return new RedirectView("/swagger-ui.html");
    }

    @GetMapping("/api")
    @ResponseBody
    public Map<String, Object> apiInfo() {
        Map<String, Object> info = new HashMap<>();
        info.put("application", "SmartShop API");
        info.put("version", "1.0.0");
        info.put("status", "running");
        info.put("endpoints", Map.of(
            "swagger", "/swagger-ui.html",
            "api-docs", "/v3/api-docs",
            "auth", "/api/auth",
            "clients", "/api/clients"
        ));
        return info;
    }
}
