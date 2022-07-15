package com.okta.spring.AuthorizationServerApplication.cfg.refresh;

import java.util.Date;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import cn.hutool.cache.Cache;
import cn.hutool.cache.CacheUtil;
import cn.hutool.core.util.StrUtil;

/**
 * 延长Token的有效期，达到类似Session的效果
 * @author yuxuan
 *
 */
@Component
public class OauthTokenInterceptor implements HandlerInterceptor {

	@Autowired
	private TokenStore tokenStore;
	//OAUTH 缓存 默认为3分钟
	private Cache<String, Integer> timedCache = CacheUtil.newTimedCache(1000*60*3);

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		String authorization = request.getHeader("Authorization");
		if(StrUtil.isNotEmpty(authorization)) {
			authorization = authorization.substring(authorization.indexOf("Bearer")+6).trim();
			if(timedCache.containsKey(authorization)) {
				int count = timedCache.get(authorization);
				if(count % 30 == 0) { // 每30次操作 就自动续期，但是，感觉还是不好 todo
					DefaultOAuth2AccessToken token = (DefaultOAuth2AccessToken) tokenStore.readAccessToken(authorization);
					if(!Objects.isNull(token) && token.isExpired()) {
						//已经过期了，直接返回
						return HandlerInterceptor.super.preHandle(request, response, handler);
					}
					Date expiration = token.getExpiration();
					//给Token增加3个小时的延长期限
					expiration.setTime(expiration.getTime() + (60 * 1000 * 60  * 3));
//					expiration.setTime(expiration.getTime() - (60 * 1000 * 60  * 300));
					token.setExpiration(expiration);
					OAuth2Authentication authentication = tokenStore.readAuthentication(token);
					tokenStore.storeAccessToken(token, authentication);
					timedCache.remove(authorization); //移除掉
				}
				//次数+1
				timedCache.put(authorization, timedCache.get(authorization)+1);
			}else {
				timedCache.put(authorization, 1); //第一次赋值为1
			}
		}

		return HandlerInterceptor.super.preHandle(request, response, handler);
	}
}
