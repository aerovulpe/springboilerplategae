package com.namespace.service;

import com.namespace.domain.Account;
import com.namespace.repository.AccountDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserAdministrationManagerImpl implements UserAdministrationManager {

    private static final Logger logger = LoggerFactory.getLogger(UserAdministrationManagerImpl.class);

    @Autowired
    private AccountDAO accountDAO;

    public UserAdministrationManagerImpl(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }

    public UserAdministrationManagerImpl() {
    }

    @Override
    public void createNewUserAccount(Account account) throws Exception {
        logger.info("createNewUserAccount()");

        try {
            logger.info("Trying to create a new account: " + account.toString());
            this.accountDAO.create(account);
            logger.info("New account created successfully");
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

    }

    @Override
    public List<Account> getEnabledUsers() {

        return accountDAO.findEnabled();
    }

    @Override
    public List<Account> getDisabledUsers() {
    return accountDAO.findDisabled();
    }

    @Override
    public boolean deactivateUserByUsername(String username) {

        Account user = accountDAO.findByUsername(username);
        user.setEnabled(false);
        try {
            return accountDAO.update(user);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean deleteUserByUsername(String username) {
        Account account = accountDAO.findByUsername(username);

        try {
            return accountDAO.remove(account);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Account getUserByUsername(String username) {
        return accountDAO.findByUsername(username);
    }

    @Override
    public boolean updateUserDetails(Account account) {
        try {
            return accountDAO.update(account);
        } catch (Exception e) {
            return false;
        }
    }
}

