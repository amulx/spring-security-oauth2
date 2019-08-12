package com.amu.demo.config;

import com.amu.demo.config.service.MyUserDetailsService;
import com.amu.demo.filter.LoginFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@EnableWebSecurity
public class WebConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable() // 将防护跨站请求伪造的功能置为不可用
                .formLogin()
                .loginPage("/login") //自定义登录界面
                .loginProcessingUrl("/login") //指定提交地址
                .defaultSuccessUrl("/main") //指定认证成功跳转界面
                //.failureForwardUrl("/error.html") //指定认证失败跳转界面(注: 转发需要与提交登录请求方式一致)
                .failureUrl("/error") //指定认证失败跳转界面(注: 重定向需要对应的方法为 GET 方式)
                .usernameParameter("username") //username
                .passwordParameter("password") //password
                .successHandler(new AuthenticationSuccessHandler() {
                    @Override
                    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
                        System.out.println("************ 身份认证成功 *************************");
                        httpServletResponse.setStatus(HttpStatus.OK.value());
                        httpServletResponse.sendRedirect("/main");
                    }
                })
                .failureHandler(new AuthenticationFailureHandler() {
                    @Override
                    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
                        System.out.println("************ 身份认证失败 *************************");
                        httpServletResponse.sendError(HttpStatus.UNAUTHORIZED.value(),HttpStatus.UNAUTHORIZED.getReasonPhrase());
                    }
                })
                .permitAll()
                .and()
                .logout()
                .logoutUrl("/logout.action") //指定登出的url, controller里面不用写对应的方法
                .logoutSuccessUrl("/login") //登出成功跳转的界面
                .permitAll()
                .and()
                .authorizeRequests()
                .antMatchers("/register*").permitAll() //设置不需要认证的
                .mvcMatchers("/main").hasAnyRole("admin")
                .anyRequest().authenticated() //其他的全部需要认证
                .and()
                .exceptionHandling()
                .accessDeniedPage("/error") //配置权限失败跳转界面 (注: url配置不会被springmvc异常处理拦截, 但是注解配置springmvc异常机制可以拦截到)
//                .and()
//                .addFilterBefore(new LoginFilter(), UsernamePasswordAuthenticationFilter.class)
        ;
    }

    @Autowired
    private MyUserDetailsService userDetailsService;

    protected void configure(AuthenticationManagerBuilder auth) throws Exception{
        auth.userDetailsService(userDetailsService);
        // auth.inMemoryAuthentication().withUser("user").password(passwordEncoder().encode("123456")).roles("admin");
    }

    public void configure(WebSecurity webSecurity) throws Exception{
        webSecurity.ignoring().antMatchers("/captcha");
    }
}
