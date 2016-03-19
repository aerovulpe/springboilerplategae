package com.namespace.service.mock;

import com.googlecode.objectify.Key;
import com.namespace.domain.Account;
import com.namespace.domain.UserGAE;
import com.namespace.repository.TestBase;
import com.namespace.service.AccountManager;
import com.namespace.service.CurrentUserManager;
import com.namespace.util.SecurityUtil;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.List;

public class AccountManagerMock extends TestBase implements AccountManager, CurrentUserManager{

	private List<Account> accounts = new ArrayList<>();
	private List<UserGAE> users = new ArrayList<>();

	public AccountManagerMock(UserGAE enabledUser) {
		SecurityUtil.authenticateUser(enabledUser);
		
		/*
		 * Without extending TestBase.java and calling super.seUp() the 
		 * com.googlecode.objectify.Key class doesn't work.
		 * It's mandatory. 
		 */
		super.setUp();
//		super.objectifyFactory.register(Quota.class);
//		super.objectifyFactory.register(Scheduler.class);
//		super.objectifyFactory.register(Account.class);
//		super.objectifyFactory.register(UserGAE.class);
//		super.objectifyFactory.register(Product.class);
//		super.objectifyFactory.register(MarketplaceProduct.class);
	}
	
	@Override
	public boolean updateAccount(Account account) {
		for (Account accountInMemory : accounts) {
			if (accountInMemory.getEmail().equals(account.getEmail())) {
				accounts.add(accounts.indexOf(accountInMemory), account);
				return true;
			}
		}
		return false;
	}

	@Override
	public Account getEnabledAccount() {
		return accounts.get(0);
	}

	@Override
	public boolean updateUser(UserGAE user) {
		for (UserGAE userInMemory : users) {
			if (userInMemory.getUsername().equals(user.getUsername())) {
				users.add(users.indexOf(userInMemory), user);
				return true;
			}
		}
		return false;
	}


	@Override
	public UserGAE getEnabledUser() {
		return (UserGAE) SecurityContextHolder.getContext()
														   .getAuthentication()
														   .getPrincipal();
	}
	
	public void createInMemoryDomainObjects(){
		users = new ArrayList<>();
		accounts = new ArrayList<>();

		UserGAE user = new UserGAE("user", "12345", true);
		Key<UserGAE> userKey = Key.create(UserGAE.class, "user");
		users.add(user);
		
		Account account = new Account(1L, "David", "D.", "example@example.com", userKey);
		accounts.add(account);
		
		UserGAE user2 = new UserGAE("user2", "12345", false);
		Key<UserGAE> userKey2 = Key.create(UserGAE.class, "user2");
		users.add(user2);

		Account account2 = new Account(2L, "David", "D.", "example@example.com", userKey2);
		accounts.add(account2);

	}

	@Override
	public boolean closeEnabledAccount() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Account getAccountByUsername(String username) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Account getAccountByUser(UserGAE user) {
		// TODO Auto-generated method stub
		return null;
	}


}
