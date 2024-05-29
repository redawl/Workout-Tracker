package com.github.redawl.workouttracker.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;

@CommonsLog
@Controller
public class ErrorHandlerController implements ErrorController {

    @GetMapping("/error")
    public String error(){
        return "forward:/";
    }

    @RequestMapping(method = {RequestMethod.DELETE, RequestMethod.PUT, RequestMethod.POST}, path = "/error")
    public void errorOther(HttpServletResponse response, Exception exception) throws IOException {
        if(exception.getMessage() != null) {
            log.error(exception);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
