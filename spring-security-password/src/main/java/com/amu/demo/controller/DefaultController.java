package com.amu.demo.controller;

import com.amu.demo.utils.CaptchaUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.OutputStream;

@Controller
public class DefaultController {
    /**
     * 登录页面
     * @return
     */
    @GetMapping("login")
    public String login(){
        return "login";
    }

    /**
     * 首页
     */
    @GetMapping("main")
    public String main(){
        return "main";
    }

    /**
     * 错误页面
     * @return
     */
    @GetMapping("error")
    public String error(){
        return "error";
    }

    /**
     * 图形验证码
     * @param request
     * @param response
     * @throws Exception
     */
    @GetMapping("captcha")
    public void createCode(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession httpSession = request.getSession();
        Object[] objects = CaptchaUtil.createImage();
        httpSession.setAttribute("imageCode", objects[0]);
        BufferedImage bufferedImage = (BufferedImage) objects[1];
        response.setContentType("image/png");
        OutputStream os = response.getOutputStream();
        ImageIO.write(bufferedImage, "png", os);
    }
}
