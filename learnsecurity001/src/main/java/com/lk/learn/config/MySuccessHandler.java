package com.lk.learn.config;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 *  登录成功处理Handler
 * @author luoaki
 */
@Component
public class MySuccessHandler implements AuthenticationSuccessHandler {
//public class MySuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
//public class MySuccessHandler extends ForwardAuthenticationSuccessHandler {

 	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
		System.out.println("登录成功处理Handler, request = [" + request + "], response = [" + response + "], authentication = [" + authentication + "]");
		String loginOutUrl = "/test"; // /test请求 无须发行！; —— 登录成功之后， 访问什么路径、页面都可以的！
		if (StringUtils.isBlank(loginOutUrl)) {
			response.setContentType("application/json;charset=UTF-8");
			response.getWriter().write(JSON.toJSONString("登录成功"));
		} else {
			response.sendRedirect(loginOutUrl);
		}
	}

}
