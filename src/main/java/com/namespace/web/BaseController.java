package com.namespace.web;

import org.pac4j.core.context.J2EContext;
import org.pac4j.core.context.WebContext;
import org.pac4j.core.profile.ProfileManager;
import org.pac4j.core.profile.UserProfile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Aaron on 20/03/2016.
 */
public abstract class BaseController {

    UserProfile getProfile(HttpServletRequest request, HttpServletResponse response) {
        final WebContext context = new J2EContext(request, response);
        final ProfileManager manager = new ProfileManager(context);
        return manager.get(true);
    }
}
