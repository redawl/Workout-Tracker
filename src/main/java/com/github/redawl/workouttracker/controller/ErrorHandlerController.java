package com.github.redawl.workouttracker.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;

@Controller
public class ErrorHandlerController implements ErrorController {
    @GetMapping("/error")
    public String error(){
        return "forward:/";
    }

    @RequestMapping(method = {RequestMethod.DELETE, RequestMethod.PUT, RequestMethod.POST}, path = "/error")
    public void errorOther(HttpServletRequest request, HttpServletResponse response, Exception exception) throws IOException {
        if(exception.getMessage() != null) {
            System.out.println(exception.getMessage());
            exception.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
