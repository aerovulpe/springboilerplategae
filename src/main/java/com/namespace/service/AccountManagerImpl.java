package com.namespace.service;

import com.namespace.domain.Account;
import com.namespace.repository.AccountDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountManagerImpl implements AccountManager {

    private static final Logger logger = LoggerFactory.getLogger(AccountManagerImpl.class);

    @Autowired
    private AccountDAO accountDAO;

    public AccountManagerImpl(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }

    public AccountManagerImpl() {
    }

    @Override
    public boolean updateAccount(Account account) {
        logger.info("updateAccount()");

        if (account == null)
            return false;

        try {
            logger.info("Trying to update the account using  accountDAO.update() ");
            boolean isUpdatedSuccessfully = this.accountDAO.update(account);
            if (isUpdatedSuccessfully) {
                logger.info("This account was updated successfully" + account.toString());
            } else {
                logger.info("This account was not updated successfully" + account.toString());
            }
            return isUpdatedSuccessfully;

        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return false;
    }

    @Override
    public Account getEnabledAccount(String username) {
        return accountDAO.findByUsername(username);
    }

    @Override
    public boolean closeAccount(String username) {
        Account account = accountDAO.findByUsername(username);
        account.setEnabled(false);
        account.setAccountNonExpired(false);
        try {
            return accountDAO.update(account);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Account getAccountByUsername(String username) {
        return accountDAO.findByUsername(username);
    }
}
