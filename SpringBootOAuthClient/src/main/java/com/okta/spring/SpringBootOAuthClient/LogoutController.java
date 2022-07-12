
package com.okta.spring.SpringBootOAuthClient;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping("/")
public class LogoutController {

    /*
        注意， F5 是重复上一个请求，包括参数、请求方法 等；如果上一个请求是 post， 重复也是post！
        浏览器地址栏 输入地址后 + enter，发起的请求， 永远都是 get 请求！
     */
    @RequestMapping("/logout")
//    @ResponseBody
    public String  logout(@CookieValue(value = "aimee-test-token", required = false)String access_token
            , @CookieValue(value = "thirdType", required = false)String thirdType, HttpServletResponse response) {
        System.out.println("logout      access_token = [" + access_token + "], thirdType = [" + thirdType + "], response = [" + response + "]");
        return "myLogout.html";
    }

    @RequestMapping("/myLogoutSuccessUrl")
    public String  myLogoutSuccessUrl(@CookieValue(value = "aimee-test-token", required = false)String access_token
            , @CookieValue(value = "thirdType", required = false)String thirdType, HttpServletResponse response) {
        System.out.println("myLogoutSuccessUrl      access_token = [" + access_token + "], thirdType = [" + thirdType + "], response = [" + response + "]");
        return "myLogoutSuccessUrl.html";
    }


    @RequestMapping("/logoutUrl")
    public String logoutUrl( ) throws IOException {
        System.out.println("LoginController.logoutUrl");
        return "logoutUrl";
    }
    @RequestMapping("/callback")
    public String wbcallback(@RequestParam("code") String code) throws IOException {
        return "callback";
    }

}
