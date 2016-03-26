package com.namespace.service.validator;

import com.namespace.service.dto.AccountForm;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public abstract class AccountCommonsValidations implements Validator{

    void commonValidations(Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "firstName", "firstName_required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "lastName", "lastName_required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "email_required");
    }

    void passwordValidation(Errors errors, AccountForm account) {
        ValidationUtils.rejectIfEmpty(errors, "password", "password_empty");
        ValidationUtils.rejectIfEmpty(errors, "passwordConfirmation", "password_empty");
        if (!account.getPassword().equals(account.getPasswordConfirmation())) {
            errors.rejectValue("password", "password_does_not_match");
            errors.rejectValue("passwordConfirmation", "password_does_not_match");
        }else if(account.getPassword().equals(account.getOldPassword())){
            errors.rejectValue("password", "password_must_be_new");
            errors.rejectValue("passwordConfirmation", "password_must_be_new");
        }
    }
}