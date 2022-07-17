# 端点说明
Spring Security对OAuth2提供了默认可访问端点，即URL
​ ​/oauth/authorize​ ​​:申请授权码code，涉及类​ ​AuthorizationEndpoint​ ​
​ ​/oauth/token​ ​​:获取令牌token，涉及类​ ​TokenEndpoint​ ​
​ ​/oauth/check_token​ ​​:用于资源服务器请求端点来检查令牌是否有效，涉及类​ ​CheckTokenEndpoint​ ​
​ ​/oauth/confirm_access​ ​​:用于确认授权提交，涉及类​ ​WhitelabelApprovalEndpoint​ ​
​ ​/oauth/error​ ​​:授权错误信息，涉及​ ​WhitelabelErrorEndpoint​ ​
​ ​/oauth/token_key​ ​​:提供公有密匙的端点，使用JWT令牌时会使用，涉及类​ ​TokenKeyEndpoint​ ​


# 资源服务器
首先需要搞懂，什么是ResourceServer 资源服务器？  资源服务器需要做什么？ 提供哪些功能？ 
我理解是配置有哪些资源，如何访问，权限如何，资源的访问策略； 
当然，ResourceServer资源服务器需要使用到 认证服务器，因为一般需要事先认证后，才能访问被保护的资源！

参考 https://zhuanlan.zhihu.com/p/164688581  
Spring Security OAuth2 架构上分为Authorization Server认证服务器和Resource Server资源服务器。
我们可以为每一个Resource Server（一个微服务实例）设置一个resourceid。Authorization Server给client第三方客户端授权的时候，可以设置这个client可以访问哪一些Resource Server资源服务，如果没设置，就是对所有的Resource Server都有访问权限。

默认是ResourceServerSecurityConfigurer.resourceId，oauth2-resource 

## @EnableResourceServer 
@EnableResourceServer 引入了ResourceServerConfiguration，它关键就是提供了configure方法；这个configure 是做了一些默认的配置； 这里的configure 是谁调用的？

    @Configuration
    public class ResourceServerConfiguration extends WebSecurityConfigurerAdapter implements Ordered {
	
	    protected void configure(HttpSecurity http) throws Exception {
		
            ResourceServerSecurityConfigurer resources = new ResourceServerSecurityConfigurer();// 创建总的配置器，然后设置它的各属性，比如tokenStore 等，然后交给ResourceServerConfigurer去使用
            ResourceServerTokenServices services = resolveTokenServices();
            if (services != null) {
                resources.tokenServices(services);
            }
            else {
                if (tokenStore != null) {
                    resources.tokenStore(tokenStore);
                }
                else if (endpoints != null) {
                    resources.tokenStore(endpoints.getEndpointsConfigurer().getTokenStore());
                }
            }
            if (eventPublisher != null) {
                resources.eventPublisher(eventPublisher);
            }
            for (ResourceServerConfigurer configurer : configurers) {// 把resources交给ResourceServerConfigurer去使用
                configurer.configure(resources);
            }
            // @formatter:off
            http.authenticationProvider(new AnonymousAuthenticationProvider("default"))
            // N.B. exceptionHandling is duplicated in resources.configure() so that
            // it works
            .exceptionHandling()
                    .accessDeniedHandler(resources.getAccessDeniedHandler()).and()
                    .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                    .csrf().disable();
            // @formatter:on
            http.apply(resources);
            if (endpoints != null) {
                // Assume we are in an Authorization Server
                http.requestMatcher(new NotOAuthRequestMatcher(endpoints.oauth2EndpointHandlerMapping()));
            }
            for (ResourceServerConfigurer configurer : configurers) { // 为什么需要重新配置？
                // Delegates can add authorizeRequests() here
                configurer.configure(http);
            }
            if (configurers.isEmpty()) {
                // Add anyRequest() last as a fall back. Spring Security would
                // replace an existing anyRequest() matcher with this one, so to
                // avoid that we only add it if the user hasn't configured anything.
                http.authorizeRequests().anyRequest().authenticated();	// 默认所有的请求都需要登录之后才能进行！
            }
	    }
    }



初始化是在 WebSecurityConfigurerAdapter#init 

	public void init(final WebSecurity web) throws Exception {
		final HttpSecurity http = getHttp();	// getHttp 方法就会获取所有的
		web.addSecurityFilterChainBuilder(http).postBuildAction(new Runnable() {
			public void run() {
				FilterSecurityInterceptor securityInterceptor = http
						.getSharedObject(FilterSecurityInterceptor.class);
				web.securityInterceptor(securityInterceptor);
			}
		});
	}
	
	
	
默认 security框架的配置： 

	protected void configure(HttpSecurity http) throws Exception {
		logger.debug("Using default configure(HttpSecurity). If subclassed this will potentially override subclass configure(HttpSecurity).");

		http
			.authorizeRequests()
				.anyRequest().authenticated()
				.and()
			.formLogin().and()
			.httpBasic(); // 默认是同时支持两者认证方式吗？ form 先？ 然后？ 
	}
	
可以看到同时配置了两者认证方式，那么他们会冲突吗？有先后顺序吗？


其实是由WebSecurityConfiguration触发的。

configure:119, ResourceServerConfiguration (org.springframework.security.oauth2.config.annotation.web.configuration)
getHttp:231, WebSecurityConfigurerAdapter (org.springframework.security.config.annotation.web.configuration)
init:322, WebSecurityConfigurerAdapter (org.springframework.security.config.annotation.web.configuration)
init:92, WebSecurityConfigurerAdapter (org.springframework.security.config.annotation.web.configuration)
init:-1, ResourceServerConfiguration$$EnhancerBySpringCGLIB$$8315267b (org.springframework.security.oauth2.config.annotation.web.configuration)
init:371, AbstractConfiguredSecurityBuilder (org.springframework.security.config.annotation)
doBuild:325, AbstractConfiguredSecurityBuilder (org.springframework.security.config.annotation)
build:41, AbstractSecurityBuilder (org.springframework.security.config.annotation)
springSecurityFilterChain:104, WebSecurityConfiguration (org.springframework.security.config.annotation.web.configuration)
CGLIB$springSecurityFilterChain$0:-1, WebSecurityConfiguration$$EnhancerBySpringCGLIB$$7dcca7f5 (org.springframework.security.config.annotation.web.configuration)
invoke:-1, WebSecurityConfiguration$$EnhancerBySpringCGLIB$$7dcca7f5$$FastClassBySpringCGLIB$$788760dd (org.springframework.security.config.annotation.web.configuration)
invokeSuper:244, MethodProxy (org.springframework.cglib.proxy)
intercept:363, ConfigurationClassEnhancer$BeanMethodInterceptor (org.springframework.context.annotation)
springSecurityFilterChain:-1, WebSecurityConfiguration$$EnhancerBySpringCGLIB$$7dcca7f5 (org.springframework.security.config.annotation.web.configuration)


WebSecurityConfiguration 创建了springSecurityFilterChain 实例，这个实例会去执行WebSecurity 的build 方法，
其中 securityFilterChainBuilders 有两个，configurers有3个实例；

configurers = {LinkedHashMap@6361}  size = 3
 {Class@4251} "class org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerSecurityConfiguration$$EnhancerBySpringCGLIB$$e5a8fcf2" -> {ArrayList@6443}  size = 1
 {Class@4246} "class com.okta.spring.AuthorizationServerApplication.SecurityConfiguration$$EnhancerBySpringCGLIB$$e4b7ecac" -> {ArrayList@6444}  size = 1
 {Class@4252} "class org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfiguration$$EnhancerBySpringCGLIB$$8315267b" -> {ArrayList@6445}  size = 1
 
	@Bean(name = AbstractSecurityWebApplicationInitializer.DEFAULT_FILTER_NAME)
	public Filter springSecurityFilterChain() throws Exception {
		boolean hasConfigurers = webSecurityConfigurers != null
				&& !webSecurityConfigurers.isEmpty();
		if (!hasConfigurers) {
			WebSecurityConfigurerAdapter adapter = objectObjectPostProcessor
					.postProcess(new WebSecurityConfigurerAdapter() {
					});
			webSecurity.apply(adapter);
		}
		return webSecurity.build();
	}

build 调用doBuild，然后会把所有的  然后会把所有的各种 ，configurers init 起来、configure 。 详见：

    AbstractConfiguredSecurityBuilder#doBuild

# 授权服务器


## @EnableAuthorizationServer
    @EnableAuthorizationServer 
    @Import({AuthorizationServerEndpointsConfiguration.class, AuthorizationServerSecurityConfiguration.class})
    public @interface EnableAuthorizationServer {
    
    }


## 过滤器

AuthorizationServerEndpointsConfiguration 就是配置了 AuthorizationServerConfigurer ，然后创建了几个 endpoint：
authorizationEndpoint 即 oauth/authorize
tokenEndpoint 即  /oauth/token
checkTokenEndpoint 即 /oauth/check_token
TokenKeyEndpoint 即 /oauth/token_key	—— OAuth2 token services that produces JWT encoded token values.， 不太懂！！


AuthorizationServerConfigurerAdapter 是什么呢？ 是进行 ClientDetailsServiceConfigurer 、AuthorizationServerSecurityConfigurer 、AuthorizationServerEndpointsConfigurer 的配置


想要 通过AuthorizationServer， 则必须先通过 spring security ！



## 过滤器
TokenEndpointAuthenticationFilter 获取请求中的grant_type 参数， 如果grant_type的值是password ，那么进行用户名密码的方式认证。。 其实就是..password 模式认证。通常不会用到它。

AbstractAuthenticationProcessingFilter 下包括几个过滤器， 
ClientCredentialsTokenEndpointFilter、 Credentials 模式认证！ 也就是使用 clientId、 clientSecret 参数直接访问/oauth/token获取token， 而不需要通过 /code 请求！
UsernamePasswordAuthenticationFilter 它匹配 /login/oauth2/code/*， 从request 然后中获取用户名密码参数，然后组装成为authRequest，然后使用AuthenticationManager进行认证：  getAuthenticationManager().authenticate(authRequest);

OAuth2LoginAuthenticationFilter 是oauth2 服务端才有的，相当于是 oauth2 服务端处理用户发起的oatuh2客户端 授权码请求，它生成code、state，给当前过滤器。这个过滤器然后就拿clientId  再次请求oauth2客户端的 /login/oauth2/code/* 端点，然后如果授权成功，就获取到 access token 等信息，然后返回 userInfo 给客户端。

tokenEndpoint..去获取clientSecret，然后 code/state/ clientId/clientSecret 


# OAuth2LoginAuthenticationFilter
这个其实是 OAuth2 服务端 发起一个http 请求到OAuth2 client.. —— 是不是搞反了？   


OAuth2ClientAuthenticationProcessingFilter 就是OAuth2 client 发起一个http 请求到OAuth2 服务端, 获取AccessToken！ ————  An OAuth2 client filter that can be used to acquire an OAuth2 access token from an authorization server, and load an authentication object into the SecurityContext 


## @EnableWebSecurity


    @Import({ WebSecurityConfiguration.class,
            SpringWebMvcImportSelector.class,
            OAuth2ImportSelector.class })
    @EnableGlobalAuthentication
    @Configuration
    public @interface EnableWebSecurity {
    
        /**
         * Controls debugging support for Spring Security. Default is false.
         * @return if true, enables debug support with Spring Security
         */
        boolean debug() default false;
    }


## AuthenticationEntryPoint
什么是认证切人点？ 就是提供了 commence 方法，就是 

	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
	
	}

主要实现：
- LoginUrlAuthenticationEntryPoint
- OAuth2AuthenticationEntryPoint


## 最重要的接口：
AccessTokenProvider#obtainAccessToken方法就是用来获取一个新的 OAuth2AccessToken， 通常会由OAuth2 client 发起一个http 请求到OAuth2 服务端！！关键代码在：OAuth2AccessTokenSupport#retrieveToken



## RequestCache
 * Implements "saved request" logic, allowing a single request to be retrieved and
 * restarted after redirecting to an authentication mechanism.
默认实现是 HttpSessionRequestCache， 即 一个session， 最多只保存一个RequestCache 


### ExceptionTranslationFilter
核心逻辑就是使用 AuthenticationEntryPoint认证，如果失败则使用AccessDeniedHandler处理异常！
    	@Override
    	public void configure(H http) throws Exception {
    		AuthenticationEntryPoint entryPoint = getAuthenticationEntryPoint(http);
    		ExceptionTranslationFilter exceptionTranslationFilter = new ExceptionTranslationFilter(
    				entryPoint, getRequestCache(http));
    		AccessDeniedHandler deniedHandler = getAccessDeniedHandler(http);
    		exceptionTranslationFilter.setAccessDeniedHandler(deniedHandler);
    		exceptionTranslationFilter = postProcess(exceptionTranslationFilter);
    		http.addFilter(exceptionTranslationFilter);
    	}

ExceptionTranslationFilter 的关键

	protected void sendStartAuthentication(HttpServletRequest request,
			HttpServletResponse response, FilterChain chain,
			AuthenticationException reason) throws ServletException, IOException {
		// SEC-112: Clear the SecurityContextHolder's Authentication, as the
		// existing Authentication is no longer considered valid
		SecurityContextHolder.getContext().setAuthentication(null);
		requestCache.saveRequest(request, response);
		logger.debug("Calling Authentication entry point.");
		authenticationEntryPoint.commence(request, response, reason);
	}



## oauth2客户端在获取access token成功之后，会尝试去获取第三方用户的详细信息： loadUser
OAuth2LoginAuthenticationProvider#authenticate 


## clientRegistrationRepository 
clientRegistrationRepository 是哪里创建的？ 是 OAuth2ClientRegistrationRepositoryConfiguration

client provider 呢


从 JwtAccessTokenConverter 可知：
JTI 就是TOKEN_ID
ATI 就是ACCESS_TOKEN_ID

	/**
	 * Field name for token id.
	 */
	public static final String TOKEN_ID = AccessTokenConverter.JTI;

	/**
	 * Field name for access token id.
	 */
	public static final String ACCESS_TOKEN_ID = AccessTokenConverter.ATI;
