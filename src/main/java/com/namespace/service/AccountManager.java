package com.namespace.service;

import com.namespace.domain.Account;
import com.namespace.service.dto.AccountForm;
import org.springframework.validation.BindingResult;

import java.util.List;

public interface AccountManager {
    boolean updateAccount(Account account);

    Account getEnabledAccount(String username);

    Account getAccountByUsername(String username);

    void createNewAccount(Account account) throws Exception;

    List<Account> getEnabledAccounts();

    List<Account> getDisabledAccounts();

    boolean deactivateAccountByUsername(String username);

    Account closeAccount(String username);

    Account deleteAccountByUsername(String username);

    Account createNewAccount(AccountForm model, BindingResult result) throws Exception;

    Account updateAccount(String username, boolean details, AccountForm model, BindingResult result);
}
