package com.lk.learn.springmvc.demo.domain;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class UserFormValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		// 只支持User类型对象的校验
		return UserForm.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		UserForm user = (UserForm) target;
		String userName = user.getName();
		if (StringUtils.isEmpty(userName) || userName.length() < 8) {
			errors.rejectValue("userName", "valid.userNameLen",
					new Object[] { "minLength", 8 }, "用户名不能少于{1}位");
		}
	}

}
