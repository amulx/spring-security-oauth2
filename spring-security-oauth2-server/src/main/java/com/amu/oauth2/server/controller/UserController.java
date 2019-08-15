package com.amu.oauth2.server.controller;

import com.amu.oauth2.server.response.*;
import com.amu.oauth2.server.service.AuthService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RestController
@RequestMapping("/")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    AuthService authService;

    @Value("${auth.clientId}")
    String clientId;

    @Value("${auth.clientSecret}")
    String clientSecret;

    @Value("${auth.cookieDomain}")
    String cookieDomain;

    @Value("${auth.cookieMaxAge}")
    int cookieMaxAge;
    /**
     *
     * @return
     */
    @PostMapping("/userlogin")
    public LoginResult login(LoginRequest loginRequest) throws Exception {
        if (loginRequest == null || StringUtils.isEmpty(loginRequest.getUsername())){
            throw new Exception("抛出异常");
        }
        // 密码校验
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();

        // 申请令牌
        AuthToken authToken = authService.login(username,password,clientId,clientSecret);
        // 用户身份令牌
        String access_token = authToken.getAccess_token();

        // 将令牌存储到cookies
        this.saveCookie(access_token);
        return new LoginResult(CommonCode.SUCCESS,access_token);
    }

    //将令牌存储到cookie
    private void saveCookie(String token){
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        //HttpServletResponse response,String domain,String path, String name, String value, int maxAge,boolean httpOnly
        CookieUtil.addCookie(response,cookieDomain,"/","uid",token,cookieMaxAge,false);

    }

    private void clearCookie(String token){
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        CookieUtil.addCookie(response,cookieDomain,"/","uid",token,0,false);
    }

    /**
     * 退出
     * @return
     */
    @PostMapping("/userlogout")
    public ResponseResult logout(){
        // 取出cookie中的用户身份令牌
        String uid = getTokenFormCookie();
        // 删除redis中的token
        boolean result = authService.delToken(uid);
        // 清除cookie
        this.clearCookie(uid);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    /**
     * 取出cookie中的身份令牌
     * @return
     */
    private String getTokenFormCookie(){
        HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
        Map<String,String> map = CookieUtil.readCookie(request,"uid");
        if (map !=null && map.get("uid")!=null){
            String uid = map.get("uid");
            return uid;
        }
        return null;
    }

    /**
     * 获取当前用户信息
     * @return
     */
    @GetMapping("/userjwt")
    public JwtResult userjwt(){
        String uid = getTokenFormCookie();
        if (uid == null){
            return new JwtResult(CommonCode.FAIL,null);
        }
        AuthToken userToken = authService.getUserToken(uid);
        if (userToken!=null){
            String jwt_token = userToken.getJwt_token();
            return new JwtResult(CommonCode.SUCCESS,jwt_token);
        }
        return null;
    }
}
