package com.amu.demo.filter;

import com.amu.demo.config.service.MyUserDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginFilter extends GenericFilterBean {

    private static final Logger logger = LoggerFactory.getLogger(LoginFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        if ("/login".equals(httpServletRequest.getServletPath())) { //开始登录过程

            String username = httpServletRequest.getParameter("username");
            String password = httpServletRequest.getParameter("password");
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(username, password);

            logger.info("登录的用户名：" + username + "  " + password);

            //模拟数据库查出来的
            User.UserBuilder userBuilder = User.withUsername(username);
            userBuilder.password("123");
            userBuilder.roles("user", "admin");
            UserDetails user = userBuilder.build();

            if (user == null) {
                System.out.println("****** 自定义登录过滤器 该用户不存在 ******");
                httpServletResponse.sendError(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase());
            }
            if (!user.getUsername().equals(authentication.getPrincipal())) {
                System.out.println("****** 自定义登录过滤器 账号有问题 ******");
                httpServletResponse.sendError(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase());
            }
            if (!user.getPassword().equals(authentication.getCredentials())) {
                System.out.println("****** 自定义登录过滤器 密码有问题 ******");
                httpServletResponse.sendError(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase());
            }

            UsernamePasswordAuthenticationToken result = new UsernamePasswordAuthenticationToken(user.getUsername(), authentication.getCredentials(), user.getAuthorities());
            result.setDetails(authentication.getDetails());

            //注: 最重要的一步
            SecurityContextHolder.getContext().setAuthentication(result);

            httpServletResponse.setStatus(HttpStatus.OK.value());
        } else  {
            chain.doFilter(request, response);
        }
    }
}
