package com.namespace.web;

import com.namespace.domain.Account;
import com.namespace.service.AccountManager;
import com.namespace.service.dto.AccountDetailsForm;
import com.namespace.service.dto.AccountFormAssembler;
import com.namespace.service.validator.AccountDetailsValidator;
import com.namespace.util.Protected;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class AccountController {

	private static final Logger logger = LoggerFactory.getLogger(AccountController.class);

	@Autowired private AccountFormAssembler accountFormAssembler;
	@Autowired private AccountManager accountManager;
	@Autowired private AccountDetailsValidator accountDetailsValidator;

	public AccountController() {
	}

	public AccountController(AccountManager accountManager, 
			AccountFormAssembler accountFormAssembler,
			AccountDetailsValidator accountDetailsValidator) {
		this.accountManager = accountManager;
		this.accountFormAssembler = accountFormAssembler;
		this.accountDetailsValidator = accountDetailsValidator;
	}
	
	@RequestMapping(value="/account", method=RequestMethod.GET)
	public ModelAndView accountHome(HttpServletRequest request, HttpServletResponse response) {
		Account enabledAccount = accountManager.getEnabledAccount(Protected.getProfile(request, response).getId());
		logger.info("Sending the enabled account for the view: " + enabledAccount);
		
		AccountDetailsForm model = accountFormAssembler.createAccountDetailsForm(enabledAccount);
		
		return new ModelAndView("account/account", "account", model);
	}

	@RequestMapping(value="updateAccount", method=RequestMethod.POST)
	public String updateAccount(HttpServletRequest request, HttpServletResponse response,
                                @ModelAttribute("account") AccountDetailsForm model, BindingResult result){
		
		if(model == null)
			throw new NullPointerException("The AccountDetailsFormModel cannot be null at " + AccountController.class.toString() + "updateAccount()");
		
		this.accountDetailsValidator.validate(model, result);

		if(result.hasErrors()){
			return "account/account";
		}else{
			Account account = accountFormAssembler.copyAccountDetailsFormtoAccount(
									model, this.accountManager.getEnabledAccount(Protected.getProfile(request, response).getId()));
			this.accountManager.updateAccount(account);
			return "redirect:account";
		}
	}
	
	
	
}

