package com.namespace.service;

import com.namespace.domain.Account;

public interface AccountManager {
    boolean updateAccount(Account username);

    Account getEnabledAccount(String username);

    Account getAccountByUsername(String username);

    boolean closeAccount(String username);
}
