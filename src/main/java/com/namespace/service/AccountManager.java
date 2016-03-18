package com.namespace.service;

import com.namespace.domain.Account;
import com.namespace.domain.UserGAE;

public interface AccountManager extends CurrentUserManager {
    boolean updateAccount(Account account);

    Account getEnabledAccount();

    Account getAccountByUsername(String username);

    Account getAccountByUser(UserGAE user);

    boolean updateUser(UserGAE user);

    boolean closeEnabledAccount();
}
