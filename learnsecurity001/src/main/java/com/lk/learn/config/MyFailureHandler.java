package com.lk.learn.config;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 *  登出失败处理Handler
 * @author luoaki
 */
@Component
public class MyFailureHandler implements AuthenticationFailureHandler {

 	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
		System.out.println("登出失败处理Handler, request = [" + request + "], response = [" + response + "], exception = [" + exception + "]");
		String loginOutUrl = "/test"; // /test请求 需要发行！;
		/*
		if (StringUtils.isBlank(loginOutUrl)) {
			response.setContentType("application/json;charset=UTF-8");
			response.getWriter().write(JSON.toJSONString("登出失败"));
		} else {
			response.sendRedirect(loginOutUrl);
		}
		*/
		response.setContentType("application/json;charset=UTF-8");
		response.getWriter().write(JSON.toJSONString("登入失败"));
	}

}
