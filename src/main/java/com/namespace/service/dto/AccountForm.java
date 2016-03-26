package com.namespace.service.dto;

import com.namespace.domain.Account;

import javax.validation.constraints.NotNull;

public class AccountForm {

    private String firstName;
    private String lastName;
    private String email;
    private boolean admin;
    private boolean enabled;
    private boolean bannedUser;
    private boolean accountNonExpired;
    private String username;
    private String password;
    private String passwordConfirmation;
    private String oldPassword;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isBannedUser() {
        return bannedUser;
    }

    public void setBannedUser(boolean bannedUser) {
        this.bannedUser = bannedUser;
    }

    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    public void setAccountNonExpired(boolean accountNonExpired) {
        this.accountNonExpired = accountNonExpired;
    }

    public String getPasswordConfirmation() {
        return passwordConfirmation;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public void setPasswordConfirmation(String passwordConfirmation) {
        this.passwordConfirmation = passwordConfirmation;
    }

    public AccountForm(@NotNull String username, @NotNull String firstName,
                       @NotNull String lastName, @NotNull String email, boolean admin,
                       String password, boolean enabled, boolean bannedUser, boolean accountNonExpired) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.admin = admin;
        this.password = password;
        this.enabled = enabled;
        this.bannedUser = bannedUser;
        this.accountNonExpired = accountNonExpired;
    }

    public AccountForm() {
    }

    public AccountForm(Account account) {
        this(account.getUsername(), account.getFirstName(), account.getLastName(), account.getEmail(), account.isAdmin(),
                account.getPassword(), account.isEnabled(), account.isBannedUser(), account.isAccountNonExpired());
    }

    @Override
    public String toString() {
        return "AccountForm{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", admin=" + admin +
                ", enabled=" + enabled +
                ", bannedUser=" + bannedUser +
                ", accountNonExpired=" + accountNonExpired +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", passwordConfirmation='" + passwordConfirmation + '\'' +
                '}';
    }
}
