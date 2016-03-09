package com.namespace.service;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.Objectify;
import com.namespace.domain.Account;
import com.namespace.domain.UserGAE;
import com.namespace.repository.AccountDAO;
import com.namespace.repository.TestBase;
import com.namespace.repository.UserGaeDAO;
import com.namespace.repository.mock.AccountDAOMock;
import com.namespace.repository.mock.UserGaeDAOMock;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.*;

public class AccountManagerTest extends TestBase{

	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(AccountManagerTest.class);

	private AccountManager manager;
	
	private UserGaeDAO userGaeDAO;
	private AccountDAO accountDAO;
	
	@Before
	public void setUp(){
		super.setUp();
		
		this.userGaeDAO = new UserGaeDAOMock(super.objectifyFactory);
		this.accountDAO = new AccountDAOMock(super.objectifyFactory);

		this.manager = new AccountManagerImpl(userGaeDAO, accountDAO);
	}
	
	@Test
	public void updateUser_BoundaryConditions(){
		
		assertFalse(this.manager.updateUser(null));
		assertFalse(this.manager.updateUser(new UserGAE()));
		Objectify ofy = super.objectifyFactory.begin();
		assertEquals(0, ofy.load().type(UserGAE.class).list().size());
		assertFalse(this.manager.updateUser(new UserGAE("user1", "12345", true)));
	}
	
	@Test
	public void updateUser_RightConditions(){
		Objectify ofy = super.objectifyFactory.begin();
		
		UserGAE user = new UserGAE("user", "12345", true);
		ofy.save().entity(user).now();
		
		user.setPassword("AAAAAAAAAA");
		assertTrue(this.manager.updateUser(user));
		assertEquals(user, ofy.load().key(Key.create(UserGAE.class, user.getUsername())).now());
	}
	
	@Test
	public void updateAccount(){
		Objectify ofy = super.objectifyFactory.begin();
		
		/*
		 * BoundaryConditions
		 */
		assertFalse(this.manager.updateAccount(null));
		assertFalse(this.manager.updateAccount(new Account()));
		
		UserGAE user = new UserGAE("user", "12345", true);
		Account account = new Account(null, "David", "D.", "example@example.com", null);
		assertFalse(this.manager.updateAccount(account));
		
		ofy.save().entities(account).now();
		assertFalse(this.manager.updateAccount(account));

		/*
		 * Right results
		 */
		ofy.save().entities(user).now();
		account.setUser(Key.create(UserGAE.class, user.getUsername()));
		ofy.save().entities(account).now();
		assertTrue(this.manager.updateAccount(account));
		
		Account accountFromDatastore = ofy.load().type(Account.class).ancestor(user).first().now();
		assertEquals(account, accountFromDatastore); 
		
	}
	
	@Test
	public void getEnabledAccount(){
		//TODO: implement this test: getEnabledAccount()
	}
	
	@Test
	public void getQuotaForEnabledAccount(){
		//TODO: implement this test: getEnabledAccount()
	}
}
