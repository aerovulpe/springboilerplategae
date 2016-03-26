package com.namespace.web;

import com.namespace.domain.Account;
import com.namespace.service.AccountManager;
import com.namespace.service.dto.AccountForm;
import com.namespace.service.dto.AccountFormAssembler;
import com.namespace.service.dto.EnabledAccountsForm;
import org.pac4j.core.profile.UserProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@Controller
public class AccountController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(AccountController.class);

    @Autowired
    private AccountManager accountManager;

    public AccountController() {
    }

    public AccountController(AccountManager accountManager) {
        this.accountManager = accountManager;
    }

    /**
     * New create account form
     */
    @RequestMapping(value = "/newAccount", method = GET)
    public ModelAndView getNewAccountForm() {
        return new ModelAndView("account/new", "account", new AccountForm());
    }

    /**
     * New update account form
     */
    @RequestMapping(value = "/updateAccount/{username}/", method = GET)
    public ModelAndView getNewUpdateAccountForm(@PathVariable String username) {

        Account account = accountManager.getAccountByUsername(username);

        AccountForm accountForm = AccountFormAssembler
                .createAccountForm(account);

        ModelAndView mv = new ModelAndView("account/update");
        mv.addObject("accountModel", accountForm);

        return mv;
    }

    @RequestMapping(value = "/accounts", method = POST)
    public String createNewAccount(@ModelAttribute("account") AccountForm model,
                                   BindingResult result) throws Exception {
        return accountManager.createNewAccount(model, result) != null ? "redirect:./" : "account/new";
    }

    @RequestMapping(value = "/accounts/{username}/", method = PUT, params = "details")
    public String updateAccount(@PathVariable String username,
                                @RequestParam(value = "details", required = false, defaultValue = "true") boolean details,
                                @ModelAttribute("accountDetailsModel") AccountForm model, BindingResult result) {
        return accountManager.updateAccount(username, details, model, result) != null ? "redirect:./" :
                "/accounts/" + username + "/";
    }


    /**
     * Enabled accounts
     */
    @RequestMapping(value = "/enabledAccounts", method = GET)
    public ModelAndView enabledAccountsListHome() {
        List<Account> enabledAccounts = accountManager.getEnabledAccounts();

        ModelAndView mv = new ModelAndView("account/listEnabledAccounts");
        mv.addObject("accountsList", enabledAccounts);
        mv.addObject("enabledAccountsModel", new EnabledAccountsForm());

        return mv;
    }

    @RequestMapping(value = "/deactivateAccounts", method = PUT)
    public String deactivateAccounts(@ModelAttribute("enabledAccountsModel") EnabledAccountsForm model) {

        logger.info("Enabled users (account IDs) to be deactivated: " + model);

        List<String> deactivatedAccounts = model.getDeactivate();

        logger.info("deactivatedAccounts: " + deactivatedAccounts);

        if (deactivatedAccounts != null) {
            for (String username : deactivatedAccounts) {
                accountManager.deactivateAccountByUsername(username);
            }
        }

        return "redirect:enabledAccounts";
    }


    /**
     * Disabled accounts
     */
    @RequestMapping(value = "/disabledAccounts", method = GET)
    public ModelAndView disabledAccountListHome() {

        List<Account> disabledAccounts = accountManager.getDisabledAccounts();

        ModelAndView mv = new ModelAndView("account/listDisabledAccounts");
        mv.addObject("disabledAccountsList", disabledAccounts);

        return mv;
    }

    @RequestMapping(value = "/deleteAccounts", method = DELETE)
    public String deleteAccounts(@ModelAttribute("accountsToDeleteModel") EnabledAccountsForm model) {

        logger.info("Accounts to be deleted: " + model);

        List<String> accountsToBeDeleted = model.getDeactivate();

        logger.info("Accounts to be deleted: " + accountsToBeDeleted);

        if (accountsToBeDeleted != null) {
            for (String username : accountsToBeDeleted) {
                accountManager.deleteAccountByUsername(username);
            }
        }

        return "redirect:/disabledAccounts";
    }

    @RequestMapping(value = "/account", method = RequestMethod.GET)
    public ModelAndView accountHome(HttpServletRequest request, HttpServletResponse response) {
        UserProfile profile = getProfile(request, response);
        if (profile == null)
            return new ModelAndView("redirect:/login");

        Account enabledAccount = accountManager.getEnabledAccount(profile.getId());
        logger.info("Sending the enabled account for the view: " + enabledAccount);

        AccountForm model = AccountFormAssembler.createAccountForm(enabledAccount);

        return new ModelAndView("account/account", "account", model);
    }

    @RequestMapping(value = "/account", method = PUT, params = "details")
    public String updateAccount(HttpServletRequest request, HttpServletResponse response,
                                @RequestParam(value = "details", required = false, defaultValue = "true") boolean details,
                                @ModelAttribute("account") AccountForm model, BindingResult result) {

        if (model == null)
            throw new NullPointerException("The AccountDetailsFormModel cannot be null at " +
                    AccountController.class.toString() + "updateAccount()");

        return accountManager.updateAccount(getProfile(request, response).getId(), details, model, result) != null ?
                "redirect:/account" : "account/account";
    }
}
