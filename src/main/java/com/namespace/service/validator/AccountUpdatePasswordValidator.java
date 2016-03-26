package com.namespace.service.validator;

import com.namespace.service.dto.AccountForm;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class AccountUpdatePasswordValidator extends AccountCommonsValidations implements Validator{

	@Override
	public boolean supports(Class<?> clazz) {
		return AccountForm.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		AccountForm user = (AccountForm) target;
		passwordValidation(errors, user);
	}
}
