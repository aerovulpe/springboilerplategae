package com.namespace.repository;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.Objectify;
import com.namespace.domain.Account;
import com.namespace.domain.UserGAE;
import com.namespace.repository.mock.AccountDAOMock;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class AccountDAOTest extends TestBase{

	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(AccountDAOTest.class);

	private AccountDAOImpl dao;
	
	@Override
	@Before
	public void setUp(){
		super.setUp();
		
		this.objectifyFactory.register(UserGAE.class);
		this.objectifyFactory.register(Account.class);
		
		dao = new AccountDAOMock(super.objectifyFactory);
	}
	
	@Test
	public void findAll_BoundaryConditions(){
		assertTrue(true);
	}
	
	@Test
	public void findAll_RightResults() throws InterruptedException{
		List<Account> accountListToPersist = generateAccountsAndPersistThem();

		List<Account> accountFromDatastoreList = dao.findAll();
		
		assertEquals(accountFromDatastoreList.size(), accountListToPersist.size());

		compareIfList1ContainsList2Objects(accountFromDatastoreList, accountListToPersist);
	}

	@Test
	public void findByUsername_BoundaryConditions(){
		assertNull(this.dao.findByUsername(null));
		
		generateAccountsAndPersistThem();
		assertNull(this.dao.findByUsername(""));
		assertNull(this.dao.findByUsername("xyz"));
		assertNull(this.dao.findByUsername("$$�!%&%/%&)=&$?*^�"));
	}
	
	@Test
	public void findByUsername_RightResults(){
		
		Objectify ofy = this.objectifyFactory.begin();
		UserGAE user = new UserGAE("user3", "33333", false);
		ofy.save().entity(user).now();
		Key<UserGAE> userKey = Key.create(UserGAE.class, user.getUsername());

		Account account = new Account("David", "D.", "example@example.com", userKey);

		ofy.save().entity(account).now();
		
		/*
		 * It must work just if Account is at the same UserGAE entity group
		 */
		Account accountFromDatastore = ofy.load().type(Account.class).ancestor(userKey).first().now();
//		Account accountFromDatastore = ofy.query(Account.class).filter("user", userKey).get(); //without @Parent in Account.java

		assertEquals(accountFromDatastore, account);
	}

	@Test public void create_BoundaryConditions(){
		Account account = null;
		try {
			this.dao.create(account);
			fail("You have been persisted a null object!");
		} catch (Exception e) {
			assertTrue(true);
		}
	}
	
	@Test
	public void create_RightResults(){
		List<Account> accountListToPersist = generateAccountsAndPersistThem();
		List<Account> accountFromDatastoreList = this.objectifyFactory.begin()
									.load().type(Account.class).list();
		compareIfList1ContainsList2Objects(accountFromDatastoreList, 
				accountListToPersist);
		assertEquals(accountFromDatastoreList.size(), accountListToPersist.size());
	}
	
	@Test
	public void update_BoundaryConditions(){
		assertFalse(this.dao.update(null));
//		try {
//			;
//			fail("You have been persisted a null object!");
//		} catch (Exception e) {
//			assertTrue(true);
//		} 
	}
	
	@Test
	public void update_RightResults(){
		generateAccountsAndPersistThem();
		Objectify ofy = this.objectifyFactory.begin();
		Account account = ofy.load().type(Account.class).first().now();
        logger.warn("LOOK MUM! I'M FAMOUS!!! **********************:" + account.toString());
		try {
			this.dao.update(account);
		} catch (Exception e) {
			fail();
		}finally{
			assertTrue(true);
		}
		Key<UserGAE> userKey = Key.create(UserGAE.class, "user");
		Account updatedAccount = ofy.load().type(Account.class).first().now();
//		Account updatedAccount = ofy.get(Account.class, account.getId());
		assertEquals(updatedAccount, account);
	}
	
	private void compareIfList1ContainsList2Objects(
			List<Account> list1, List<Account> list2) {
		for (Account account : list2) {
			assertTrue(list1.contains(account));
		}
	}
	
	private List<Account> generateAccountsAndPersistThem(){
		List<Account> accountListToPersist = generateAccountList();
		persistAccountList(accountListToPersist);
		return accountListToPersist;
		
	}
	
	private void persistAccountList(List<Account> list){
		Objectify ofy = this.objectifyFactory.begin();
		for (Account account : list) {
			ofy.save().entities(account).now();
		}
	}
	
	private List<Account> generateAccountList(){
		UserGAE user = new UserGAE("user", "12345", true);
		Key<UserGAE> userKey = Key.create(UserGAE.class, user.getUsername());
		Account account1 = new Account("David", "D.", "example@example.com", userKey);
		
		Account account2 = new Account("David2", "D.", "example2@example.com", userKey);
		List<Account> accountList = new ArrayList<>();
		accountList.add(account1);
		accountList.add(account2);
		return accountList;
	}
	
}
