package com.namespace.service;

import com.googlecode.objectify.Key;
import com.namespace.domain.Account;
import com.namespace.domain.UserGAE;
import com.namespace.repository.AccountDAO;
import com.namespace.repository.UserGaeDAO;
import com.namespace.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Secured({"ROLE_ADMIN"})
public class UserAdministrationManagerImpl extends AbstractCurrentUserManager implements UserAdministrationManager {

    private static final Logger logger = LoggerFactory.getLogger(UserAdministrationManagerImpl.class);

    @Autowired
    private UserGaeDAO userGaeDAO;
    @Autowired
    private AccountDAO accountDAO;

    public UserAdministrationManagerImpl(UserGaeDAO userGaeDAO,
                                         AccountDAO accountDAO) {
        this.userGaeDAO = userGaeDAO;
        this.accountDAO = accountDAO;
    }

    public UserAdministrationManagerImpl() {
    }

    @Override
    public void createNewUserAccount(UserGAE user, Account account) {
        logger.info("createNewUserAccount()");

        try {
            logger.info("Trying to create a new user: " + user.toString());
            userGaeDAO.create(user);
            logger.info("New user created successfully");
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        Key<UserGAE> userKey = Key.create(UserGAE.class, user.getUsername());
        account.setUser(userKey);
        try {
            logger.info("Trying to create a new account: " + account.toString());
            this.accountDAO.create(account);
            logger.info("New account created successfully");
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

    }

    @Override
    public List<Pair<Account, UserGAE>> getEnabledUsers() {
        List<UserGAE> enabledUsers = userGaeDAO.findAllEnabledUsers(true);

        List<Pair<Account, UserGAE>> enabledAccounts = new ArrayList<>();
        for (UserGAE enabledUser : enabledUsers) {
            enabledAccounts.add(new Pair<>(accountDAO.findByUsername(enabledUser.getUsername()), enabledUser));
        }

        return enabledAccounts;
    }

    @Override
    public List<Pair<Account, UserGAE>> getDisabledUsers() {
        List<UserGAE> noEnabledUsers = userGaeDAO.findAllEnabledUsers(false);

        List<Pair<Account, UserGAE>> noEnabledAccounts = new ArrayList<>();
        for (UserGAE enabledUser : noEnabledUsers) {
            noEnabledAccounts.add(new Pair<>(accountDAO.findByUsername(enabledUser.getUsername()), enabledUser));
        }

        return noEnabledAccounts;
    }

    @Override
    public boolean deactivateUserByUsername(String username) {

        UserGAE user = userGaeDAO.findByUsername(username);
        user.setEnabled(false);
        try {
            return userGaeDAO.update(user);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean deleteUserByUsername(String username) {
        UserGAE user = userGaeDAO.findByUsername(username);
        Account account = accountDAO.findByUsername(username);

        try {
            return userGaeDAO.remove(user) && accountDAO.remove(account);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public UserGAE getUserByUsername(String username) {
        return userGaeDAO.findByUsername(username);
    }

    @Override
    public boolean updateUserDetails(UserGAE user, Account account) {
        try {
            return updateUser(user) && accountDAO.update(account);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean updateUser(UserGAE user) {

        try {
            return userGaeDAO.update(user);
        } catch (Exception e) {
            return false;
        }
    }
}

