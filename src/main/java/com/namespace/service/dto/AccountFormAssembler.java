package com.namespace.service.dto;

import org.springframework.stereotype.Component;

import com.namespace.domain.Account;

@Component
public class AccountFormAssembler {
	
	public AccountControllerForm createAccountControllerForm (Account account){
		return new AccountControllerForm(account);
	}
	
	/**
	 * Account details
	 */
	public AccountDetailsForm createAccountDetailsForm(Account account){
		AccountDetailsForm form = new AccountDetailsForm();
		form.setFirstName(account.getFirstName());
		form.setLastName(account.getLastName());
		form.setEmail(account.getEmail());
		return form; 
	}
	
	public Account copyAccountDetailsFormtoAccount(AccountDetailsForm accountDetailsForm, Account account){
		account.setFirstName(accountDetailsForm.getFirstName());
		account.setLastName(accountDetailsForm.getLastName());
		account.setEmail(accountDetailsForm.getEmail());
		
		return account;
	}

    public UserPasswordForm createUserPasswordForm(){
		return new UserPasswordForm();
    }
    
	public Account copyUserPasswordFormToAccount(UserPasswordForm userPasswordForm, Account account){
		account.setPassword(userPasswordForm.getNewPassword());
		return account;
	}
	

}
