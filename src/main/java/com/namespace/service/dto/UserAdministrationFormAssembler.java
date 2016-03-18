package com.namespace.service.dto;

import com.namespace.domain.Account;
import com.namespace.domain.UserGAE;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class UserAdministrationFormAssembler {

    public UserAdministrationForm createUserAdministrationForm(UserGAE user, Account account) {
        UserAdministrationForm form = new UserAdministrationForm();
        if (account != null) {
            form.setFirstName(account.getFirstName());
            form.setLastName(account.getLastName());
            form.setEmail(account.getEmail());
        }

        if (user != null) {
            form.setAdmin(user.isAdmin());
            form.setUsername(user.getUsername());
            form.setEnabled(user.isEnabled());
            form.setBannedUser(user.isBannedUser());
        }

        return form;
    }

    public UserAdministrationForm createUserAdministrationForm() {
        return new UserAdministrationForm();
    }

    public Map<String, Object> copyNewUserFromUserAdministrationForm(UserAdministrationForm form) {
        HashMap<String, Object> objectsMap = new HashMap<String, Object>();

        UserGAE user = fillCommonUserInformationFromForm(form, new UserGAE(form.getUsername()));
        user.setPassword(form.getPassword());
        user.setEnabled(form.isEnabled());
        user.setBannedUser(form.isBannedUser());

        Account account = fillCommonAccountInformationFromForm(form, new Account());

        objectsMap.put("user", user);
        objectsMap.put("account", account);

        return objectsMap;
    }

    public Map<String, Object> updateUserDetailsFromUserAdministrationForm(UserAdministrationForm form,
                                                                           UserGAE userToFill, Account accountToFill) {
        HashMap<String, Object> objectsMap = new HashMap<String, Object>();

        UserGAE user = fillCommonUserInformationFromForm(form, userToFill);

        Account account = fillCommonAccountInformationFromForm(form, accountToFill);

        objectsMap.put("user", user);
        objectsMap.put("account", account);

        return objectsMap;
    }


    private UserGAE fillCommonUserInformationFromForm(UserAdministrationForm form, UserGAE userToBeFilled) {
        userToBeFilled.setAdmin(form.isAdmin());
        return userToBeFilled;
    }


    private Account fillCommonAccountInformationFromForm(UserAdministrationForm form,
                                                         Account accountToBeFilled) {
        accountToBeFilled.setFirstName(form.getFirstName());
        accountToBeFilled.setLastName(form.getLastName());
        accountToBeFilled.setEmail(form.getEmail());
        return accountToBeFilled;
    }
}
