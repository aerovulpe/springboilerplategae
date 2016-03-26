package com.namespace.service.validator;

import com.namespace.service.dto.AccountForm;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
public class AccountUpdateDetailsValidator extends AccountCommonsValidations {

	@Override
	public boolean supports(Class<?> clazz) {
		return AccountForm.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		commonValidations(errors);
	}
}
