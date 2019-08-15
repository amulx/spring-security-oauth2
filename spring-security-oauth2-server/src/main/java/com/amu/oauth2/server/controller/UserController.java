package com.amu.oauth2.server.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;

@Controller
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    @GetMapping("/user")
    public Principal  user(Principal user){
        return user;
    }

    @RequestMapping("/oauth/exit")
    public void exit(HttpServletRequest request, HttpServletResponse response){
        logger.info("退出：");
        new SecurityContextLogoutHandler().logout(request,null,null);

        try {
            response.sendRedirect(request.getHeader("referer"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
