package com.namespace.service;

import com.namespace.domain.Account;

import java.util.List;

public interface UserAdministrationManager {

    void createNewUserAccount(Account account) throws Exception;

    List<Account> getEnabledUsers();

    List<Account> getDisabledUsers();

    boolean deactivateUserByUsername(String username);

    boolean deleteUserByUsername(String username);

    Account getUserByUsername(String username);

    boolean updateUserDetails(Account account);
}