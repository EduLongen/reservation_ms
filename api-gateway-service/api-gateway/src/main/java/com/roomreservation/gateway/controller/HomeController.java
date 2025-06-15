package com.roomreservation.gateway.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Controller
@Tag(name = "Home", description = "Home page controller")
public class HomeController {

    @GetMapping("/")
    @Operation(summary = "Redirect to reservations page", description = "Redirects the user to the reservations page")
    public String home() {
        return "redirect:/reservations";
    }
} 