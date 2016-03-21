package com.namespace.web;

import com.namespace.domain.Account;
import com.namespace.service.AccountManager;
import com.namespace.service.UserAdministrationManager;
import com.namespace.service.dto.EnabledUserForm;
import com.namespace.service.dto.UserAdministrationForm;
import com.namespace.service.dto.UserAdministrationFormAssembler;
import com.namespace.service.validator.UserAdministrationDetailsValidator;
import com.namespace.service.validator.UserAdministrationPasswordValidator;
import com.namespace.service.validator.UserAdministrationValidator;
import org.pac4j.core.config.Config;
import org.pac4j.core.context.J2EContext;
import org.pac4j.core.context.WebContext;
import org.pac4j.core.exception.RequiresHttpAction;
import org.pac4j.core.profile.ProfileManager;
import org.pac4j.core.profile.UserProfile;
import org.pac4j.http.client.indirect.FormClient;
import org.pac4j.jwt.profile.JwtGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class UsersController {

    private static final Logger logger = LoggerFactory.getLogger(UsersController.class);

    @Autowired
    private Config config;
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
                                BindingResult result) throws Exception{

        userAdministrationValidator.validate(model, result);

        if (result.hasErrors()) {
            return "users/addNewUser";
        } else {
            HashMap<String, Object> domainModelMap;
            domainModelMap = (HashMap<String, Object>) userAdministrationFormAssembler
                    .copyNewUserFromUserAdministrationForm(model);
            Account account = (Account) domainModelMap.get("account");

            userAdministrationManager.createNewUserAccount(account);

            return "redirect:updateUser/" + account.getUser()+ "/";
        }
    }


    /**
     * Update user form
     */
    @RequestMapping(value = "/updateUser/{username}/", method = RequestMethod.GET)
    public ModelAndView updateUserHome(@PathVariable String username) {

        Account account = accountManager.getAccountByUsername(username);

        UserAdministrationForm userAdministrationModel = userAdministrationFormAssembler
                .createUserAdministrationForm(account);

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
                            userAdministrationManager.getUserByUsername(username));

            Account account = (Account) domainModelMap.get("account");

            userAdministrationManager.updateUserDetails(account);

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
            Account user = userAdministrationManager.getUserByUsername(username);
            user.setPassword(model.getPassword());

            userAdministrationManager.updateUserDetails(user);

            return "redirect:./";
        }
    }


    /**
     * Enabled users
     */
    @RequestMapping(value = "/enabledUsersList", method = RequestMethod.GET)
    public ModelAndView enabledUserListHome() {
        List<Account> enabledUsers = userAdministrationManager.getEnabledUsers();

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

        List<Account> disabledUsers = userAdministrationManager.getDisabledUsers();

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

    @RequestMapping("/")
    public String root(HttpServletRequest request, HttpServletResponse response, Map<String, Object> map) throws RequiresHttpAction {
        return index(request, response, map);
    }

    @RequestMapping("/index.html")
    public String index(HttpServletRequest request, HttpServletResponse response, Map<String, Object> map) throws RequiresHttpAction {
        final WebContext context = new J2EContext(request, response);
        map.put("profile", getProfile(context));
        return "index";
    }

    private UserProfile getProfile(WebContext context) {
        final ProfileManager manager = new ProfileManager(context);
        return manager.get(true);
    }

    @RequestMapping("/facebook/index.html")
    public String facebook(HttpServletRequest request, HttpServletResponse response, Map<String, Object> map) {
        return protectedIndex(request, response, map);
    }

    @RequestMapping("/facebook/notprotected.html")
    public String facebookNotProtected(HttpServletRequest request, HttpServletResponse response, Map<String, Object> map) {
        final WebContext context = new J2EContext(request, response);
        map.put("profile", getProfile(context));
        return "notProtected";
    }

    @RequestMapping("/facebookadmin/index.html")
    public String facebookadmin(HttpServletRequest request, HttpServletResponse response, Map<String, Object> map) {
        return protectedIndex(request, response, map);
    }

    @RequestMapping("/facebookcustom/index.html")
    public String facebookcustom(HttpServletRequest request, HttpServletResponse response, Map<String, Object> map) {
        return protectedIndex(request, response, map);
    }

    @RequestMapping("/twitter/index.html")
    public String twitter(HttpServletRequest request, HttpServletResponse response, Map<String, Object> map) {
        return protectedIndex(request, response, map);
    }

    @RequestMapping("/form/index.html")
    public String form(HttpServletRequest request, HttpServletResponse response, Map<String, Object> map) {
        return protectedIndex(request, response, map);
    }

    @RequestMapping("/basicauth/index.html")
    public String basicauth(HttpServletRequest request, HttpServletResponse response, Map<String, Object> map) {
        return protectedIndex(request, response, map);
    }

    @RequestMapping("/cas/index.html")
    public String cas(HttpServletRequest request, HttpServletResponse response, Map<String, Object> map) {
        return protectedIndex(request, response, map);
    }

    @RequestMapping("/casrest/index.html")
    public String casrest(HttpServletRequest request, HttpServletResponse response, Map<String, Object> map) {
        return protectedIndex(request, response, map);
    }

    @RequestMapping("/oidc/index.html")
    public String oidc(HttpServletRequest request, HttpServletResponse response, Map<String, Object> map) {
        return protectedIndex(request, response, map);
    }

    @RequestMapping("/protected/index.html")
    public String protect(HttpServletRequest request, HttpServletResponse response, Map<String, Object> map) {
        return protectedIndex(request, response, map);
    }

    @RequestMapping("/dba/index.html")
    public String dba(HttpServletRequest request, HttpServletResponse response, Map<String, Object> map) {
        return protectedIndex(request, response, map);
    }

    @RequestMapping("/rest-jwt/index.html")
    public String restJwt(HttpServletRequest request, HttpServletResponse response, Map<String, Object> map) {
        return protectedIndex(request, response, map);
    }

    @RequestMapping("/jwt.html")
    public String jwt(HttpServletRequest request, HttpServletResponse response, Map<String, Object> map) {
        final WebContext context = new J2EContext(request, response);
        final UserProfile profile = getProfile(context);
        final JwtGenerator<UserProfile> generator = new JwtGenerator<>("12345678901234567890123456789012");
        String token = "";
        if (profile != null) {
            token = generator.generate(profile);
        }
        map.put("token", token);
        return "jwt";
    }

    @RequestMapping("/loginForm")
    public String theForm(Map<String, Object> map) {
        final FormClient formClient = (FormClient) config.getClients().findClient("FormClient");
        map.put("callbackUrl", formClient.getCallbackUrl());
        return "form";
    }

    protected String protectedIndex(HttpServletRequest request, HttpServletResponse response, Map<String, Object> map) {
        final WebContext context = new J2EContext(request, response);
        map.put("profile", getProfile(context));
        return "protectedIndex";
    }
}
