package com.lk.learn.config;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 登出处理Handler
 *
 * @author luokai
 * @date 2022年7月8日
 */
@Component
public class TigerLogoutSuccessHandler implements LogoutSuccessHandler {

 	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
		System.out.println("登出成功后的处理Handler, request = [" + request + "], response = [" + response + "], authentication = [" + authentication + "]");
		String loginOutUrl = "/myLogoutSuccessUrl";// 登出之后， 再访问的页面， 需要放行，否则302到 login page
		// 为什么.logoutSuccessUrl("/myLogoutSuccessUrl") 可以无须发行 myLogoutSuccessUrl ？ 这里却不行？ 因为我在.logout()有配置 .permitAll()； 而antMatchers 中没有对/myLogoutSuccessUrl配置permitAll
		if (StringUtils.isBlank(loginOutUrl)) {
			response.setContentType("application/json;charset=UTF-8");
			response.getWriter().write(JSON.toJSONString("退出成功"));
		} else {
			response.sendRedirect(loginOutUrl);
		}
	}

}
