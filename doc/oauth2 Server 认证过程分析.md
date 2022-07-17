

OAuth2AuthenticationProcessingFilter


首先使用tokenExtractor 从request 的头或cookie抽取token（通常是 bearer格式）；如果有，说明已经认证过，  
否则看是否是认证请求，是则从中获取认证信息，比如用户名密码、client id、secret等，进行认证

否则302到 认证页面!

 
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException,
			ServletException {

		final boolean debug = logger.isDebugEnabled();
		final HttpServletRequest request = (HttpServletRequest) req;
		final HttpServletResponse response = (HttpServletResponse) res;

		try {

			Authentication authentication = tokenExtractor.extract(request);
			
			if (authentication == null) {
				if (stateless && isAuthenticated()) {
					if (debug) {
						logger.debug("Clearing security context.");
					}
					SecurityContextHolder.clearContext();
				}
				if (debug) {
					logger.debug("No token in request, will continue chain.");
				}
			}
			else {
				request.setAttribute(OAuth2AuthenticationDetails.ACCESS_TOKEN_VALUE, authentication.getPrincipal());
				if (authentication instanceof AbstractAuthenticationToken) {
					AbstractAuthenticationToken needsDetails = (AbstractAuthenticationToken) authentication;
					needsDetails.setDetails(authenticationDetailsSource.buildDetails(request));
				}
				Authentication authResult = authenticationManager.authenticate(authentication);

				if (debug) {
					logger.debug("Authentication success: " + authResult);
				}

				eventPublisher.publishAuthenticationSuccess(authResult);
				SecurityContextHolder.getContext().setAuthentication(authResult);

			}
		}
		catch (OAuth2Exception failed) {
			SecurityContextHolder.clearContext();

			if (debug) {
				logger.debug("Authentication request failed: " + failed);
			}
			eventPublisher.publishAuthenticationFailure(new BadCredentialsException(failed.getMessage(), failed),
					new PreAuthenticatedAuthenticationToken("access-token", "N/A"));

			authenticationEntryPoint.commence(request, response,
					new InsufficientAuthenticationException(failed.getMessage(), failed));

			return;
		}

		chain.doFilter(request, response);
	}
	
认证工作是：
Authentication authResult = authenticationManager.authenticate(authentication);

具体是 ProviderManager.authenticate 方法
ProviderManager 会调用 AuthenticationProvider 

AuthenticationProvider 有很多实现。 对于 oauth2， 那就是 OAuth2AuthorizationCodeAuthenticationProvider
或者 OAuth2LoginAuthenticationProvider 


生成 AccessToken的关键代码是在DefaultTokenServices
DefaultTokenServices.createAccessToken(org.springframework.security.oauth2.provider.OAuth2Authentication)

它先获取已有的 AccessToken，然后就； —— 对于jdbc， 如果之前已经有了，就不会走后面的逻辑。
—— 如果之前没有使用tokenEnhancer， 后面也不会用到。 所以，最好先清空相关oauth2表！

否则会重新生成token，生成过程中会用到 tokenEnhancer ！



