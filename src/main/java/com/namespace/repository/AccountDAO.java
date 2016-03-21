package com.namespace.repository;

import com.namespace.domain.Account;

import java.util.List;

public interface AccountDAO extends GenericDAO<Account> {

    List<Account> findAll();

    List<Account> findEnabled();

    List<Account> findDisabled();

    Account findByUsername(String username);
}