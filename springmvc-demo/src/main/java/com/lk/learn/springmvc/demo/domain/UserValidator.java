package com.lk.learn.springmvc.demo.domain;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class UserValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		// 只支持User类型对象的校验
		return User.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		User user = (User) target;
		String userName = user.getName();
		if (StringUtils.isEmpty(userName) || userName.length() < 3) {
			// rejectValue的第一个参数必须是 User对象的字段！
			errors.rejectValue("name", "valid.userNameLen",
					new Object[] { "minLength", 3 }, "用户名不能少于{xxx}位");
		}
	}

}
