
package com.lk.learn;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.security.Principal;

/**
 * @author luokai 2022年7月9日
 *
 * 访问这里的请求需要携带类似下面的请求头：
 * Authorization: Bearer 068225f6-3f91-4a19-9a11-1244e2587ce3
 *
 */
@RestController
public class UserController {

    // 必须有这个 端点，否则OAuth2客户端： OAuth2AuthenticationException: [invalid_user_info_response] An error occurred while attempting to retrieve the UserInfo Resource: 404 null]
    @GetMapping("/user/me") // 需要返回 application/json， 而不是 text/html
    public Principal user(Principal principal) {
        System.out.println("oauth2的user info 端点， 必须返回json格式 principal： " + principal);
        return principal;
    }


    @RequestMapping("/aa")
    public String aa( ) throws IOException {
        System.out.println("测试页面.aa ， 如果受保护且登录了就能正常访问");
        return "种瓜得瓜：aa";
    }


    @RequestMapping("/a")
    @PreAuthorize("principal.equals('a')") // 额外的权限控制， 只有用户a才能访问
    public String a2( ) throws IOException {
        System.out.println("测试页面.a  xx权限");
        return "种瓜得瓜：a";
    }

    @RequestMapping("/bb")
    @PreAuthorize("principal.equals('b')")// 额外的权限控制， 只有用户b才能访问
    public String bb( ) throws IOException {
        System.out.println("测试页面.bb  xx权限");
        return "种豆得豆：bb";
    }

}
