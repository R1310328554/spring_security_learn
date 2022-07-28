package com.lk.learn.springmvc.demo.controller;

import com.lk.learn.springmvc.demo.domain.User;
import com.lk.learn.springmvc.demo.domain.UserForm;
import com.lk.learn.springmvc.demo.domain.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/valid")
public class ValidatorController {

	@Autowired
	private UserValidator userValidator;

	@InitBinder
	private void initBinder(WebDataBinder binder) {
		binder.addValidators(userValidator);
		// 这里的UserValidator的叠加的作用， User类上面的jsr303 验证注解仍然有效！
		// 它在jsr303后面执行、起作用！
	}

	@RequestMapping(value = { "/index", "" }, method = { RequestMethod.GET })
	public String index(ModelMap m) throws Exception {
		m.addAttribute("user", new UserForm());
		return "initbinder/user.jsp";
	}

	@RequestMapping(value = { "/signup" }, method = { RequestMethod.POST })
	public String signup(@Validated UserForm user, BindingResult br, RedirectAttributes ra) throws Exception {
		// 携带用户录入的信息方便回显
		ra.addFlashAttribute("user", user);
		return "initbinder/user.jsp";
	}


	/*
 	@Valid 是xx jsr232；

	！！！当前controller的@InitBinder绑定的 UserFormValidator必须要能够能够处理 @Valid User user， 否则：
	org.springframework.validation.DataBinder.assertValidators

	{"msg":"Invalid target for Validator [com.lk.learn.springmvc.demo.domain.UserValidator@3b216246]: com.lk.learn.springmvc.demo.domain.User@45634d44","code":100}
		assertValidators:539, DataBinder (org.springframework.validation)
		setValidator:531, DataBinder (org.springframework.validation)
		initBinder:208, ConfigurableWebBindingInitializer (org.springframework.web.bind.support)
		initBinder:47, WebBindingInitializer (org.springframework.web.bind.support)
		createBinder:58, DefaultDataBinderFactory (org.springframework.web.bind.support)
		resolveArgument:157, ModelAttributeMethodProcessor (org.springframework.web.method.annotation)
		resolveArgument:126, HandlerMethodArgumentResolverComposite (org.springframework.web.method.support)
		getMethodArgumentValues:166, InvocableHandlerMethod (org.springframework.web.method.support)
		invokeForRequest:134, InvocableHandlerMethod (org.springframework.web.method.support)
		invokeAndHandle:102, ServletInvocableHandlerMethod (org.springframework.web.servlet.mvc.method.annotation)
		invokeHandlerMethod:895, RequestMappingHandlerAdapter (org.springframework.web.servlet.mvc.method.annotation)
		handleInternal:800, RequestMappingHandlerAdapter (org.springframework.web.servlet.mvc.method.annotation)
		handle:87, AbstractHandlerMethodAdapter (org.springframework.web.servlet.mvc.method)
		doDispatch:1038, DispatcherServlet (org.springframework.web.servlet)
		doService:942, DispatcherServlet (org.springframework.web.servlet)
		processRequest:1005, FrameworkServlet (org.springframework.web.servlet)
		doPost:908, FrameworkServlet (org.springframework.web.servlet)
		service:660, HttpServlet (javax.servlet.http)
		service:882, FrameworkServlet (org.springframework.web.servlet)
		service:741, HttpServlet (javax.servlet.http)
		internalDoFilter:231, ApplicationFilterChain (org.apache.catalina.core)
		doFilter:166, ApplicationFilterChain (org.apache.catalina.core)
		doFilter:53, WsFilter (org.apache.tomcat.websocket.server)
		internalDoFilter:193, ApplicationFilterChain (org.apache.catalina.core)
		doFilter:166, ApplicationFilterChain (org.apache.catalina.core)
		doFilterInternal:99, RequestContextFilter (org.springframework.web.filter)
		doFilter:107, OncePerRequestFilter (org.springframework.web.filter)
		internalDoFilter:193, ApplicationFilterChain (org.apache.catalina.core)
		doFilter:166, ApplicationFilterChain (org.apache.catalina.core)
		doFilterInternal:92, FormContentFilter (org.springframework.web.filter)
		doFilter:107, OncePerRequestFilter (org.springframework.web.filter)
		internalDoFilter:193, ApplicationFilterChain (org.apache.catalina.core)
		doFilter:166, ApplicationFilterChain (org.apache.catalina.core)
		doFilterInternal:93, HiddenHttpMethodFilter (org.springframework.web.filter)
		doFilter:107, OncePerRequestFilter (org.springframework.web.filter)
		internalDoFilter:193, ApplicationFilterChain (org.apache.catalina.core)
		doFilter:166, ApplicationFilterChain (org.apache.catalina.core)
		doFilterInternal:200, CharacterEncodingFilter (org.springframework.web.filter)
		doFilter:107, OncePerRequestFilter (org.springframework.web.filter)
		internalDoFilter:193, ApplicationFilterChain (org.apache.catalina.core)
		doFilter:166, ApplicationFilterChain (org.apache.catalina.core)
		invoke:200, StandardWrapperValve (org.apache.catalina.core)
		invoke:96, StandardContextValve (org.apache.catalina.core)
		invoke:490, AuthenticatorBase (org.apache.catalina.authenticator)
		invoke:139, StandardHostValve (org.apache.catalina.core)
		invoke:92, ErrorReportValve (org.apache.catalina.valves)
		invoke:74, StandardEngineValve (org.apache.catalina.core)
		service:343, CoyoteAdapter (org.apache.catalina.connector)
		service:408, Http11Processor (org.apache.coyote.http11)
		process:66, AbstractProcessorLight (org.apache.coyote)
		process:834, AbstractProtocol$ConnectionHandler (org.apache.coyote)
		doRun:1415, NioEndpoint$SocketProcessor (org.apache.tomcat.util.net)
		run:49, SocketProcessorBase (org.apache.tomcat.util.net)
		runWorker:1149, ThreadPoolExecutor (java.util.concurrent)
		run:624, ThreadPoolExecutor$Worker (java.util.concurrent)
		run:61, TaskThread$WrappingRunnable (org.apache.tomcat.util.threads)
		run:748, Thread (java.lang)

	 */
	@RequestMapping("/validate")
	public String validate(@Valid User user, BindingResult result) {
		System.out.println("ValidatorController.validate	" + "	user = [" + user + "], result = [" + result + "]");
		// 如果有异常信息
		if (result.hasErrors()) {
			// 获取异常信息对象
			List<ObjectError> errors = result.getAllErrors();
			// 将异常信息输出
			for (ObjectError error : errors) {
				System.out.println(error.getDefaultMessage());
			}
		}
		return "index";
	}

	@RequestMapping(value = "/addUser")
	public String add() {
		return "addUser";
	}

	@RequestMapping(value = "/getUser", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public User get() {
		User user = new User();
		user.setId(123);
		user.setName("lk");
		user.setBornDate(new Date());
		user.setBornDate2(new Date().getTime());
		user.setBornDate3(LocalDateTime.now());
		user.setBornDate4(new Date());
		user.setHiredate(new Date());
		user.setEmail("asdf@qq.com");
		user.setBalance(123456789.012345);
		user.setBalance2("123456789.012345");
		user.setBalance3(123456789.012345);
		user.setNum4(123456789.012345);
		user.setMoney(3210123.456789);
		user.setDiscount(3210123.456789);
		user.setTotal(210123789);
		return user;
	}

	/*
		http://192.168.1.103:8080/valid/date1?date=2011-01-11
	 */
	@GetMapping("/date1")
	public String datest(@DateTimeFormat(iso= DateTimeFormat.ISO.DATE) Date date){
		System.out.println(date);
		return "index";
	}

	/*
		http://192.168.1.103:8080/valid/date2?date=2011/01/11
	 */
	@GetMapping("/date2")
	public String datest2(@DateTimeFormat(pattern = "yyyy/MM/dd") Date date){
		System.out.println(date);
		return "index";
	}


}
