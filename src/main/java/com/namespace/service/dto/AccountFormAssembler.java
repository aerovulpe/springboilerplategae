package com.namespace.service.dto;

import com.namespace.domain.Account;

import javax.validation.constraints.NotNull;

public class AccountFormAssembler {

    public static AccountForm createAccountForm(@NotNull Account account) {
        AccountForm form = new AccountForm();
        extractCommons(form, account);
        form.setPassword(account.getPassword());
        return form;
    }

    public static Account copyNewAccountFromAccountForm(@NotNull AccountForm form) {
        return new Account(form.getFirstName(), form.getLastName(), form.getEmail(), form.getUsername(),
                form.getPassword());
    }

    public static Account updateAccountDetailsFromAccountForm(@NotNull AccountForm form, Account account) {
        extractCommons(form, account);
        return account;
    }

    public static AccountForm createAccountFormAdmin(@NotNull Account account) {
        AccountForm form = new AccountForm();
        extractCommons(form, account);
        form.setAdmin(account.isAdmin());
        form.setEnabled(account.isEnabled());
        form.setBannedUser(account.isBannedUser());
        form.setAccountNonExpired(account.isAccountNonLocked());
        form.setPassword(account.getPassword());
        return form;
    }

    public static Account copyNewAccountFromAccountFormAdmin(@NotNull AccountForm form) {
        return new Account(form.getFirstName(), form.getLastName(), form.getEmail(), form.isAdmin(), form.isEnabled(),
                form.getUsername(), form.getPassword());
    }

    private static void extractCommons(AccountForm form, @NotNull Account account) {
        form.setFirstName(account.getFirstName());
        form.setLastName(account.getLastName());
        form.setEmail(account.getEmail());
    }
}
