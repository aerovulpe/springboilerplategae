package com.namespace.service.validator;

import com.namespace.service.dto.AccountForm;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

@Component
public class AccountCreationValidator extends AccountCommonsValidations  {

    @Override
    public boolean supports(Class<?> clazz) {
        return AccountForm.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "username_required");
        commonValidations(errors);

        AccountForm user = (AccountForm) target;
        passwordValidation(errors, user);

    }
}
