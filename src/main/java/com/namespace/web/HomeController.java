package com.namespace.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.googlecode.objectify.Key;
import com.namespace.domain.Account;
import com.namespace.domain.UserGAE;
import com.namespace.repository.AccountDAO;
import com.namespace.repository.UserGaeDAO;
import com.namespace.service.AbstractCurrentUserManager;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController extends AbstractCurrentUserManager {

    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    @Autowired
    private UserGaeDAO userGaeDAO;
    @Autowired
    private AccountDAO accountDAO;

    public HomeController() {
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String home() {
        logger.info("Welcome home!");
        return "home";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login() {

        return "login";

    }

    @RequestMapping(value = "/loginfailed", method = RequestMethod.GET)
    public String loginError(ModelMap model) {
        model.addAttribute("error", "true");
        return "login";
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout() {
        return "home";
    }

    @RequestMapping(value = "/createDefaultUsers", method = RequestMethod.GET)
    public String createDefaultUsers(ModelMap model) {
        try {
            logger.info("Creating default accounts...");

            UserGAE firstAdminUser = new UserGAE("admin", "admin", true, true, false, true);
            UserGAE firstNonAdminUser = new UserGAE("user", "user", false, true, false, true);
            Key<UserGAE> userAdminKey = Key.create(UserGAE.class, firstAdminUser.getUsername());
            Key<UserGAE> userNonAdminKey = Key.create(UserGAE.class, firstNonAdminUser.getUsername());
            Account accountAdmin = new Account("John", "Doe", "example@example.com", userAdminKey);
            Account accountNonAdmin = new Account("User1", "User1", "example1@example.com", userNonAdminKey);

            if (accountDAO.findByUsername("user") == null) {
                this.accountDAO.create(accountAdmin);
                this.accountDAO.create(accountNonAdmin);
            }

            if (userGaeDAO.findByUsername("user") == null) {
                this.userGaeDAO.create(firstAdminUser);
                this.userGaeDAO.create(firstNonAdminUser);
            }

            model.addAttribute("Error", "true");
        } catch (Exception ex) {
            model.addAttribute("Error", "true");
        }
        return "home";
    }
}

