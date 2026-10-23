package com.github.redawl.workouttracker.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HomeController {
    @RequestMapping("/")
    public String index(){
        return "index.html";
    }

    @RequestMapping("/auth")
    public String auth(){
        return "index.html";
    }
}
