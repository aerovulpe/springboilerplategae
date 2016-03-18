package com.namespace.web;

import com.namespace.domain.Account;
import com.namespace.domain.UserGAE;
import com.namespace.service.AccountManager;
import com.namespace.service.UserAdministrationManager;
import com.namespace.service.dto.EnabledUserForm;
import com.namespace.service.dto.UserAdministrationForm;
import com.namespace.service.dto.UserAdministrationFormAssembler;
import com.namespace.service.validator.UserAdministrationDetailsValidator;
import com.namespace.service.validator.UserAdministrationPasswordValidator;
import com.namespace.service.validator.UserAdministrationValidator;
import com.namespace.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;

@Controller
@Secured({"ROLE_ADMIN"})
public class UsersController {

    private static final Logger logger = LoggerFactory.getLogger(UsersController.class);

    @Autowired
    private UserAdministrationFormAssembler userAdministrationFormAssembler;
    @Autowired
    private UserAdministrationValidator userAdministrationValidator;
    @Autowired
    private UserAdministrationDetailsValidator userAdministrationDetailsValidator;
    @Autowired
    private UserAdministrationPasswordValidator userAdministrationPasswordValidator;
    @Autowired
    private UserAdministrationManager userAdministrationManager;
    @Autowired
    private AccountManager accountManager;

    public UsersController(UserAdministrationFormAssembler userAdministrationFormAssembler,
                           UserAdministrationValidator userAdministrationValidator,
                           UserAdministrationManager userAdministrationManager) {
        this.userAdministrationFormAssembler = userAdministrationFormAssembler;
        this.userAdministrationValidator = userAdministrationValidator;
        this.userAdministrationManager = userAdministrationManager;
    }

    public UsersController() {
    }

    /**
     * New user form
     */
    @RequestMapping(value = "/newUser", method = RequestMethod.GET)
    public ModelAndView addNewUserHome() {

        UserAdministrationForm model = new UserAdministrationForm();
        return new ModelAndView("users/addNewUser", "user", model);
    }

    @RequestMapping(value = "createUser", method = RequestMethod.POST)
    public String createNewUser(@ModelAttribute("user") UserAdministrationForm model,
                                BindingResult result) {

        userAdministrationValidator.validate(model, result);

        if (result.hasErrors()) {
            return "users/addNewUser";
        } else {
            HashMap<String, Object> domainModelMap;
            domainModelMap = (HashMap<String, Object>) userAdministrationFormAssembler
                    .copyNewUserFromUserAdministrationForm(model);
            UserGAE user = (UserGAE) domainModelMap.get("user");
            user.setEnabled(true);
            user.setBannedUser(false);
            user.setAccountNonExpired(true);
            Account account = (Account) domainModelMap.get("account");

            userAdministrationManager.createNewUserAccount(user, account);

            return "redirect:updateUser/" + user.getUsername() + "/";
        }
    }


    /**
     * Update user form
     */
    @RequestMapping(value = "/updateUser/{username}/", method = RequestMethod.GET)
    public ModelAndView updateUserHome(@PathVariable String username) {

        UserGAE user = userAdministrationManager.getUserByUsername(username);
        Account account = accountManager.getAccountByUsername(username);

        UserAdministrationForm userAdministrationModel = userAdministrationFormAssembler
                .createUserAdministrationForm(user, account);

        ModelAndView mv = new ModelAndView("users/updateUser");
        mv.addObject("userDetailsModel", userAdministrationModel);

        UserAdministrationForm userEmptyModel = new UserAdministrationForm();
        userEmptyModel.setUsername(username);

        mv.addObject("userPasswordModel", userAdministrationModel);
        mv.addObject("enableUserModel", userEmptyModel);
        mv.addObject("deleteBanUserModel", userEmptyModel);

        return mv;
    }

    @RequestMapping(value = "/updateUser/{username}/updateUserDetails", method = RequestMethod.POST)
    public String updateUserDetails(@PathVariable String username,
                                    @ModelAttribute("userDetailsModel") UserAdministrationForm model, BindingResult result) {

        logger.info("updateUserDetails()");

        userAdministrationDetailsValidator.validate(model, result);

        if (result.hasErrors()) {
            return "updateUser/" + username + "/";
        } else {
            HashMap<String, Object> domainModelMap;
            domainModelMap = (HashMap<String, Object>) userAdministrationFormAssembler
                    .updateUserDetailsFromUserAdministrationForm(model,
                            userAdministrationManager.getUserByUsername(username),
                            accountManager.getAccountByUsername(username));

            UserGAE user = (UserGAE) domainModelMap.get("user");
            Account account = (Account) domainModelMap.get("account");

            userAdministrationManager.updateUserDetails(user, account);

            return "redirect:./";
        }
    }

    @RequestMapping(value = "/updateUser/{username}/updatePasswordAdministrationForm", method = RequestMethod.POST)
    public String updatePassword(@PathVariable String username, @ModelAttribute("userPasswordModel") UserAdministrationForm model,
                                 BindingResult result) {

        logger.info("updating password");

        this.userAdministrationPasswordValidator.validate(model, result);

        if (result.hasErrors()) {
            logger.info("validation error!");
            return "../../updateUser/" + username + "/";
        } else {
            UserGAE user = userAdministrationManager.getUserByUsername(username);
            user.setPassword(model.getPassword());

            userAdministrationManager.updateUser(user);

            return "redirect:./";
        }
    }


    /**
     * Enabled users
     */
    @RequestMapping(value = "/enabledUsersList", method = RequestMethod.GET)
    public ModelAndView enabledUserListHome() {
        List<Pair<Account, UserGAE>> enabledUsers = userAdministrationManager.getEnabledUsers();

        ModelAndView mv = new ModelAndView("users/listEnabledUser");
        mv.addObject("usersList", enabledUsers);
        mv.addObject("enabledUsersToDeactivateModel", new EnabledUserForm());

        return mv;
    }

    @RequestMapping(value = "deactivateUsers", method = RequestMethod.POST)
    public String deactivateUsers(@ModelAttribute("enabledUsersToDeactivateModel") EnabledUserForm model,
                                  BindingResult result) {

        logger.info("Enabled users (account IDs) to be deactivated: " + model);

        List<String> deactivatedUsers = model.getDeactivate();

        logger.info("deactivatedUsers: " + deactivatedUsers);

        if (deactivatedUsers != null) {
            for (String username : deactivatedUsers) {
                userAdministrationManager.deactivateUserByUsername(username);
            }
        }

        return "redirect:enabledUsersList";
    }


    /**
     * Disabled users
     */
    @RequestMapping(value = "/disabledUsersList", method = RequestMethod.GET)
    public ModelAndView disabledUserListHome() {

        List<Pair<Account, UserGAE>> disabledUsers = userAdministrationManager.getDisabledUsers();

        ModelAndView mv = new ModelAndView("users/listDisabledUser");
        mv.addObject("usersList", disabledUsers);
        mv.addObject("enabledUsersToDeleteModel", new EnabledUserForm());

        return mv;
    }

    @RequestMapping(value = "deleteUsers", method = RequestMethod.POST)
    public String deleteUsers(@ModelAttribute("enabledUsersToDeleteModel") EnabledUserForm model,
                              BindingResult result) {

        logger.info("Enabled users (account IDs) to be deleted: " + model);

        List<String> deletedUsers = model.getDeactivate();

        logger.info("deactivatedUsers: " + deletedUsers);

        if (deletedUsers != null) {
            for (String username : deletedUsers) {
                userAdministrationManager.deleteUserByUsername(username);
            }
        }

        return "redirect:disabledUsersList";
    }
}
