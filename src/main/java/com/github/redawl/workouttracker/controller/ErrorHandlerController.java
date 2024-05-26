package com.github.redawl.workouttracker.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class ErrorHandlerController implements ErrorController {
    @GetMapping("/error")
    public String error(){
        return "forward:/";
    }

    @RequestMapping(method = {RequestMethod.DELETE, RequestMethod.PUT, RequestMethod.POST}, path = "/error")
    public String errorOther(HttpServletRequest request, HttpServletResponse response, Exception exception) {
        System.out.println(exception.getMessage());
        exception.printStackTrace();

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        return null;
    }
}
