package com.namespace.service.dto;

import com.namespace.domain.Account;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class UserAdministrationFormAssembler {

    public UserAdministrationForm createUserAdministrationForm(Account account) {
        UserAdministrationForm form = new UserAdministrationForm();
        if (account != null) {
            form.setFirstName(account.getFirstName());
            form.setLastName(account.getLastName());
            form.setEmail(account.getEmail());
            form.setAdmin(account.isAdmin());
            form.setUsername(account.getUser());
            form.setEnabled(account.isEnabled());
            form.setBannedUser(account.isBannedUser());
        }

        return form;
    }

    public UserAdministrationForm createUserAdministrationForm() {
        return new UserAdministrationForm();
    }

    public Map<String, Object> copyNewUserFromUserAdministrationForm(UserAdministrationForm form) {
        HashMap<String, Object> objectsMap = new HashMap<>();

        Account account = fillCommonAccountInformationFromForm(form, new Account());

        account.setUser(form.getUsername());
        account.setPassword(form.getPassword());
        account.setEnabled(form.isEnabled());
        account.setBannedUser(form.isBannedUser());
        account.setAccountNonExpired(true);
        objectsMap.put("account", account);

        return objectsMap;
    }

    public Map<String, Object> updateUserDetailsFromUserAdministrationForm(UserAdministrationForm form,
                                                                           Account accountToFill) {
        HashMap<String, Object> objectsMap = new HashMap<>();


        Account account = fillCommonAccountInformationFromForm(form, accountToFill);

        objectsMap.put("account", account);

        return objectsMap;
    }


    private Account fillCommonAccountInformationFromForm(UserAdministrationForm form,
                                                         Account accountToBeFilled) {
        accountToBeFilled.setFirstName(form.getFirstName());
        accountToBeFilled.setLastName(form.getLastName());
        accountToBeFilled.setEmail(form.getEmail());
        accountToBeFilled.setAdmin(form.isAdmin());
        return accountToBeFilled;
    }
}
