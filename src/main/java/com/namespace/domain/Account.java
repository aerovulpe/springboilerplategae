package com.namespace.domain;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;

import javax.persistence.Id;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
public class Account {
    private static final String ROLE_USER = "ROLE_USER";
    private static final String ROLE_ADMIN = "ROLE_ADMIN";

    private String firstName;
    private String lastName;
    private String email;
    private boolean admin;
    @Index
    private boolean enabled;
    private boolean bannedUser;
    private boolean accountNonExpired;

    @Id
    @com.googlecode.objectify.annotation.Id
    private String user;
    private String password;

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

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setAccountNonExpired(boolean accountNonExpired) {
        this.accountNonExpired = accountNonExpired;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public boolean isBannedUser() {
        return bannedUser;
    }

    public void setBannedUser(boolean bannedUser) {
        this.bannedUser = bannedUser;
    }

    public Collection<String> getAuthorities() {
        List<String> authorityList = new ArrayList<>();
        authorityList.add(ROLE_USER);
        if (admin) {
            authorityList.add(ROLE_ADMIN);
        }
        return authorityList;
    }

    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    public boolean isAccountNonLocked() {
        return !bannedUser;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Account(String firstName, String lastName, String email, boolean admin, boolean enabled, boolean bannedUser, boolean accountNonExpired, String user, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.admin = admin;
        this.enabled = enabled;
        this.bannedUser = bannedUser;
        this.accountNonExpired = accountNonExpired;
        this.user = user;
        this.password = password;
    }

    public Account(String firstName, String lastName,
                   String email, String user, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.user = user;
        this.password = password;
        enabled = true;
        accountNonExpired = true;
    }

//    public Account(Long id, String firstName, String lastName,
//                   String email, String user) {
//        this(firstName, lastName, email, user, null);
//        this.id = id;
//    }

    public Account() {
    }

    @Override
    public String toString() {
        return "Account{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", admin=" + admin +
                ", enabled=" + enabled +
                ", bannedUser=" + bannedUser +
                ", accountNonExpired=" + accountNonExpired +
                ", user='" + user + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Account account = (Account) o;

        if (isAdmin() != account.isAdmin()) return false;
        if (isEnabled() != account.isEnabled()) return false;
        if (isBannedUser() != account.isBannedUser()) return false;
        if (isAccountNonExpired() != account.isAccountNonExpired()) return false;
        if (!getFirstName().equals(account.getFirstName())) return false;
        if (!getLastName().equals(account.getLastName())) return false;
        if (!getEmail().equals(account.getEmail())) return false;
        if (!getUser().equals(account.getUser())) return false;
        return getPassword() != null ? getPassword().equals(account.getPassword()) : account.getPassword() == null;

    }

    @Override
    public int hashCode() {
        int result = getFirstName().hashCode();
        result = 31 * result + getLastName().hashCode();
        result = 31 * result + getEmail().hashCode();
        result = 31 * result + (isAdmin() ? 1 : 0);
        result = 31 * result + (isEnabled() ? 1 : 0);
        result = 31 * result + (isBannedUser() ? 1 : 0);
        result = 31 * result + (isAccountNonExpired() ? 1 : 0);
        result = 31 * result + getUser().hashCode();
        result = 31 * result + (getPassword() != null ? getPassword().hashCode() : 0);
        return result;
    }
}
