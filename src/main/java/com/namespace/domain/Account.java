package com.namespace.domain;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Parent;

import javax.persistence.Id;

@Entity
public class Account {

    @Id
    @com.googlecode.objectify.annotation.Id
    private Long id;
    private String firstName;
    private String lastName;
    private String email;

    @Parent
    private Key<UserGAE> user;

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

    public Key<UserGAE> getUser() {
        return user;
    }

    public Long getId() {
        return id;
    }

    public void setUser(Key<UserGAE> user) {
        this.user = user;
    }

    public Account(String firstName, String lastName,
                   String email, Key<UserGAE> user) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.user = user;
    }

    public Account(Long id, String firstName, String lastName,
                   String email, Key<UserGAE> user) {
        this(firstName, lastName, email, user);
        this.id = id;
    }

    public Account() {
    }

    @Override
    public String toString() {
        return "Account [id=" + id + ", firstName=" + firstName + ", lastName="
                + lastName + ", email=" + email + ", user=" + user + "]";
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        Account account = (Account) object;

        return getId() != null ? getId().equals(account.getId()) : account.getId() == null &&
                getFirstName().equals(account.getFirstName()) &&
                getLastName().equals(account.getLastName()) &&
                        getEmail().equals(account.getEmail()) &&
                        (getUser() != null ? getUser().equals(account.getUser()) :
                                account.getUser() == null);
    }

    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + getFirstName().hashCode();
        result = 31 * result + getLastName().hashCode();
        result = 31 * result + getEmail().hashCode();
        result = 31 * result + (getUser() != null ? getUser().hashCode() : 0);
        return result;
    }
}
