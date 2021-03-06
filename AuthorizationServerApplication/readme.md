

Shared object 是什么？

几个主要的概念：





首先需要搞懂，什么是ResourceServer 资源服务器？  资源服务器需要做什么？ 提供哪些功能？ 
我理解是配置有哪些资源，如何访问，权限如何，资源的访问策略； 
当然，ResourceServer资源服务器需要使用到 认证服务器，因为一般需要事先认证后，才能访问被保护的资源！

@EnableResourceServer 引入了ResourceServerConfiguration， 关键就是提供了configure方法；这个configure 是做了一些默认的配置； 这里的configure 是谁调用的？

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



是在 org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter#init

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

org.springframework.security.config.annotation.AbstractConfiguredSecurityBuilder#doBuild



@EnableAuthorizationServer 
@Import({AuthorizationServerEndpointsConfiguration.class, AuthorizationServerSecurityConfiguration.class})
public @interface EnableAuthorizationServer {

}
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


OAuth2ClientAuthenticationProcessingFilter 就是OAuth2 client 发起一个http 请求到OAuth2 服务端, 获取AccessToken！ ————  An OAuth2 client filter that can be used to acquire an OAuth2 access token from an authorization server, and load an authentication object into the SecurityContext 

## AuthenticationEntryPoint
什么是认证切人点？ 就是提供了 commence 方法，就是 

	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
	
	}

## 最重要的接口：
AccessTokenProvider#obtainAccessToken方法就是用来获取一个新的 OAuth2AccessToken， 通常会由OAuth2 client 发起一个http 请求到OAuth2 服务端！！关键代码在：OAuth2AccessTokenSupport#retrieveToken


## oauth2客户端在获取access token成功之后，会尝试去获取第三方用户的详细信息： loadUser
OAuth2LoginAuthenticationProvider#authenticate 



## jwtToken 前后端分离方式！

