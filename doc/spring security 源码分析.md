# spring_security_learn



默认的Spring Security是怎么样的：

    @SuppressWarnings({ "rawtypes", "unchecked" })
	protected final HttpSecurity getHttp() throws Exception {
		if (http != null) {
			return http;
		}

		DefaultAuthenticationEventPublisher eventPublisher = objectPostProcessor
				.postProcess(new DefaultAuthenticationEventPublisher());
		localConfigureAuthenticationBldr.authenticationEventPublisher(eventPublisher);

		AuthenticationManager authenticationManager = authenticationManager();
		authenticationBuilder.parentAuthenticationManager(authenticationManager);
		authenticationBuilder.authenticationEventPublisher(eventPublisher);
		Map<Class<? extends Object>, Object> sharedObjects = createSharedObjects();

		http = new HttpSecurity(objectPostProcessor, authenticationBuilder,
				sharedObjects);
		if (!disableDefaults) {
			// @formatter:off
			http
				.csrf().and()
				.addFilter(new WebAsyncManagerIntegrationFilter())
				.exceptionHandling().and()
				.headers().and()
				.sessionManagement().and()
				.securityContext().and()
				.requestCache().and()
				.anonymous().and()
				.servletApi().and()
				.apply(new DefaultLoginPageConfigurer<>()).and()
				.logout();
			// @formatter:on
			ClassLoader classLoader = this.context.getClassLoader();
			List<AbstractHttpConfigurer> defaultHttpConfigurers =
					SpringFactoriesLoader.loadFactories(AbstractHttpConfigurer.class, classLoader);

			for (AbstractHttpConfigurer configurer : defaultHttpConfigurers) {
				http.apply(configurer);
			}
		}
		configure(http);
		return http;
	}
	


AutoXxxConfiguration 就是说.. 

XxxConfiguration 就是说，组装了一些bean， 但是没有实例化、没有启用
EnableXxxConfiguration 就是说..  Enable XxxConfiguration 就是实例化 并启用



SpringSecurity 本质是一个过滤器链：从启动时可以获取到过滤器链：


org.springframework.security.web.context.request.async.WebAsyncManagerIntegrationFilter
org.springframework.security.web.context.SecurityContextPersistenceFilter 
org.springframework.security.web.header.HeaderWriterFilter
org.springframework.security.web.csrf.CsrfFilter
org.springframework.security.web.authentication.logout.LogoutFilter 
org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter 
org.springframework.security.web.authentication.ui.DefaultLoginPageGeneratingFilter 
org.springframework.security.web.authentication.ui.DefaultLogoutPageGeneratingFilter
org.springframework.security.web.savedrequest.RequestCacheAwareFilter
org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestFilter
org.springframework.security.web.authentication.AnonymousAuthenticationFilter 
org.springframework.security.web.session.SessionManagementFilter 
org.springframework.security.web.access.ExceptionTranslationFilter 
org.springframework.security.web.access.intercept.FilterSecurityInterceptor

1.4.1、代码底层流程：重点看三个过滤器：
1 FilterSecurityInterceptor：是一个方法级的权限过滤器, 基本位于过滤链的最底部。
super.beforeInvocation(fi) 表示查看之前的 filter 是否通过。
fi.getChain().doFilter(fi.getRequest(), fi.getResponse());表示真正的调用后台的服务。

2 ExceptionTranslationFilter：是个异常过滤器，用来处理在认证授权过程中抛出的异常

3 UsernamePasswordAuthenticationFilter ：对/login 的 POST 请求做拦截，校验表单中用户名，密码。



1.6、SpringSecurity 原理总结
1.6.1、SpringSecurity 的过滤器介绍
SpringSecurity 采用的是责任链的设计模式，它有一条很长的过滤器链，现在对这条过滤器链的15个过滤器进行说明：

WebAsyncManagerIntegrationFilter：将 Security 上下文与 Spring Web 中用于处理异步请求映射的 WebAsyncManager 进行集成。
SecurityContextPersistenceFilter：在每次请求处理之前将该请求相关的安全上下文信息加载到 SecurityContextHolder 中，然后在该次请求处理完成之后，将SecurityContextHolder 中关于这次请求的信息存储到一个“仓储”中，然后将SecurityContextHolder 中的信息清除，例如在 Session 中维护一个用户的安全信息就是这个过滤器处理的。
HeaderWriterFilter：用于将头信息加入响应中。
CsrfFilter：用于处理跨站请求伪造。
LogoutFilter：用于处理退出登录。
UsernamePasswordAuthenticationFilter：用于处理基于表单的登录请求，从表单中获取用户名和密码。默认情况下处理来自 /login 的请求。从表单中获取用户名和密码时，默认使用的表单 name 值为 username 和 password，这两个值可以通过设置这个过滤器的 usernameParameter 和 passwordParameter 两个参数的值进行修改。
DefaultLoginPageGeneratingFilter：如果没有配置登录页面，那系统初始化时就会配置这个过滤器，并且用于在需要进行登录时生成一个登录表单页面。
BasicAuthenticationFilter：检测和处理 http basic 认证。
RequestCacheAwareFilter：用来处理请求的缓存
SecurityContextHolderAwareRequestFilter：主要是包装请求对象 request。
AnonymousAuthenticationFilter：检测 SecurityContextHolder 中是否存在Authentication 对象，如果不存在为其提供一个匿名 Authentication。
SessionManagementFilter：管理 session 的过滤器
ExceptionTranslationFilter：处理 AccessDeniedException 和AuthenticationException 异常。
FilterSecurityInterceptor：可以看做过滤器链的出口。
RememberMeAuthenticationFilter：当用户没有登录而直接访问资源时, 从 cookie 里找出用户的信息, 如果 Spring Security 能够识别出用户提供的 remember me cookie, 用户将不必填写用户名和密码, 而是直接登录进入系统，该过滤器默认不开启。
1.6.2、SpringSecurity 基本流程
Spring Security 采取过滤链实现认证与授权，只有当前过滤器通过，才能进入下一个过滤器：
————————————————
版权声明：本文为CSDN博主「prefect_start」的原创文章，遵循CC 4.0 BY-SA版权协议，转载请附上原文出处链接及本声明。
原文链接：https://blog.csdn.net/prefect_start/article/details/125356882











