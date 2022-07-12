
package com.okta.spring.SpringBootOAuthClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Controller
public class LoginController {

    /**
     * / 首页无须映射！ 系统自动跳转到 index 页面！
     */
//    @RequestMapping("/")
//    public String idx( ) throws IOException {
//        System.out.println("LoginController. no 404 啊");
//        return "4000000xxx";
//    }

    @RequestMapping("/login2")
    public String login2( ) throws IOException {
        System.out.println("LoginController.login");
        return "lo";
    }

    @Autowired
    private OAuth2ClientProperties oauth2ClientProperties;

    @Autowired
    ClientRegistrationRepository clientRegistrationRepository;// 这个可以获取

    @GetMapping("/myLogin")
    // 实验证明 无法通过此种方式获取 oauth2 的配置信息
    public String myLogin(Map<String, String> oauth2AuthenticationUrlToClientName, HttpServletRequest request) throws IOException {
        System.out.println("LoginController.myLogin " + clientRegistrationRepository);
        return "myLogin";
    }

    @PostMapping("/myLogin")
    public String myLogin2() throws IOException {
        System.out.println("LoginController.myLogin 2");
        return "myLogin";
    }

    @RequestMapping("/doLogin")
    public String doLogin( ) throws IOException {
        System.out.println("LoginController.doLogin");
        return "lo";
    }

    @RequestMapping("/login")
    public void login(@RequestParam("code") String code, HttpServletResponse response) throws IOException {
        System.out.println("LoginController.login: code = [" + code + "], response = [" + response + "]");
//        IOUtils.toString(null, Charsets.UTF_8);
        // IOUtils.toString(((SimpleClientHttpResponse) response).responseStream, Charsets.UTF_8)
    }

}
