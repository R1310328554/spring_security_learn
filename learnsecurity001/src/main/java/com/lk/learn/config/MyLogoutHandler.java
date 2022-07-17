package com.lk.learn.config;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 登出处理Handler
 *
 * @author CaiRui
 * @date 2019-03-12 08:31
 */
@Component
public class MyLogoutHandler implements LogoutHandler {

 	@Override
	public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
		System.out.println("登出处理Handler, request = [" + request + "], response = [" + response + "], authentication = [" + authentication + "]");
		String loginOutUrl = "/myLogoutSuccessUrl";// 登出之后， 再访问的页面， 需要放行，否则302到 login page
//		if (StringUtils.isBlank(loginOutUrl)) {
//			response.setContentType("application/json;charset=UTF-8");
//			response.getWriter().write(JSON.toJSONString("退出成功"));
//		} else {
//			response.sendRedirect(loginOutUrl);
//		}
	}

}
