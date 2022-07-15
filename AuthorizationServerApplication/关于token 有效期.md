
如果使用默认的 org.springframework.security.oauth2.provider.token.DefaultTokenServices.createAccessToken(org.springframework.security.oauth2.provider.OAuth2Authentication)，
那么有效期是：

	private int refreshTokenValiditySeconds = 60 * 60 * 24 * 30; // default 30 days.
	private int accessTokenValiditySeconds = 60 * 60 * 12; // default 12 hours.

如果使用了其他的TokenServices或 TokenStore，那么需要记得先清除缓存。 比如 RedisTokenStore其本身不会过期的（其实是使用默认值）
此时， com.okta.spring.AuthorizationServerApplication.AuthServerConfig.configure( ClientDetailsServiceConfigurer) 的设置不会立即生效，最好先手动清除token缓存！

另外， jwt token 可以自行设置有效时间，此时不受RedisTokenStore 控制。

   	public OAuth2AccessToken createAccessToken(OAuth2Authentication authentication) throws AuthenticationException {

		OAuth2AccessToken existingAccessToken = tokenStore.getAccessToken(authentication);
		OAuth2RefreshToken refreshToken = null;
		if (existingAccessToken != null) {
			if (existingAccessToken.isExpired()) {
				if (existingAccessToken.getRefreshToken() != null) {
					refreshToken = existingAccessToken.getRefreshToken();
					// The token store could remove the refresh token when the
					// access token is removed, but we want to
					// be sure...
					tokenStore.removeRefreshToken(refreshToken);
				}
				tokenStore.removeAccessToken(existingAccessToken);
			}

可以看到，如果existingAccessToken 已经过期， 而 refreshToken 还在， 那么会尝试删除 refreshToken

测试发现， redis中的accessToken、refreshToken 都是有过期时间的，如果过期，会自动删除， 这个时候获取到的 existingAccessToken 就是null了！

对于RedisTokenStore无须通过认证服务器删除！ 对于其他的，不清楚！


