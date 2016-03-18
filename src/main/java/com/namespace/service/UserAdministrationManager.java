package com.namespace.service;

import com.namespace.domain.Account;
import com.namespace.domain.UserGAE;
import com.namespace.util.Pair;

import java.util.List;

public interface UserAdministrationManager {

    void createNewUserAccount(UserGAE user, Account account);

    List<Pair<Account, UserGAE>> getEnabledUsers();

    List<Pair<Account, UserGAE>> getDisabledUsers();

    boolean deactivateUserByUsername(String username);

    boolean deleteUserByUsername(String username);

    UserGAE getUserByUsername(String username);

    boolean updateUserDetails(UserGAE user, Account account);

    boolean updateUser(UserGAE user);
}