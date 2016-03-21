package com.namespace.web;

import com.namespace.repository.AccountDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {

    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

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
}

