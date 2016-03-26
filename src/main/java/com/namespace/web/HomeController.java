package com.namespace.web;

import org.pac4j.core.config.Config;
import org.pac4j.core.profile.UserProfile;
import org.pac4j.http.client.indirect.FormClient;
import org.pac4j.jwt.profile.JwtGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    @Autowired
    private Config config;

    public HomeController() {
    }

    @RequestMapping(value = "/", method = GET)
    public String home(HttpServletRequest request, HttpServletResponse response, Map<String, Object> map) {
        UserProfile profile = getProfile(request, response);
        if(profile == null)
            return "redirect:/login";

        logger.info("Welcome home, " + profile.getAttribute("first_name") + "!");
        map.put("profile", profile);
        return "/home/home";
    }

    @RequestMapping(value = "/login", method = POST)
    public String login(HttpServletRequest request, HttpServletResponse response, Map<String, Object> map) {
        UserProfile profile = getProfile(request, response);
        logger.info("Profile: " + profile);
        map.put("profile", profile);
        return "/home/home";
    }

    @RequestMapping(value = "/login", method = GET)
    public String getLoginPage() {
        return "/signin/signin";
    }

    @RequestMapping(value = "/loginfailed", method = GET)
    public String loginError(ModelMap model) {
        model.addAttribute("error", "true");
        return "login/login";
    }

    @RequestMapping(value = "/logout", method = GET)
    public String logout() {
        return "/home/home";
    }

    @RequestMapping("/login/facebook/")
    public String facebook(HttpServletRequest request, HttpServletResponse response, Map<String, Object> map) {
        return protectedIndex(request, response, map);
    }

    @RequestMapping("/login/twitter/")
    public String twitter(HttpServletRequest request, HttpServletResponse response, Map<String, Object> map) {
        return protectedIndex(request, response, map);
    }

    @RequestMapping("/login/form/")
    public String form(HttpServletRequest request, HttpServletResponse response, Map<String, Object> map) {
        return protectedIndex(request, response, map);
    }

    @RequestMapping("/login/oidc/")
    public String oidc(HttpServletRequest request, HttpServletResponse response, Map<String, Object> map) {
        return protectedIndex(request, response, map);
    }


    @RequestMapping(value = "/jwt/", method = POST)
    public String restJwt(HttpServletRequest request, HttpServletResponse response, Map<String, Object> map) {
        return protectedIndex(request, response, map);
    }

    @RequestMapping(value = "/jwt/", method = GET)
    public String jwt(HttpServletRequest request, HttpServletResponse response, Map<String, Object> map) {
        final UserProfile profile = getProfile(request, response);
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
        return "signin/loginForm";
    }

    private String protectedIndex(HttpServletRequest request, HttpServletResponse response, Map<String, Object> map) {
        map.put("profile", getProfile(request, response));
        return "home/home";
    }
}

