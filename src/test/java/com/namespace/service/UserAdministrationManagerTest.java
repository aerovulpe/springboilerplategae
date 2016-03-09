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

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class UserAdministrationManagerTest extends TestBase {

    @SuppressWarnings("unused")
    private static final Logger logger = LoggerFactory.getLogger(UserAdministrationManagerTest.class);

    private UserAdministrationManager manager;

    private UserGaeDAO userGaeDAO;
    private AccountDAO accountDAO;

    @Before
    public void setUp() {
//		TestBase daoTestBase = new TestBase();
//		daoTestBase.setUp();
        super.setUp();
//		super.objectifyFactory.register(UserGAE.class);
//		userGaeDAO = new UserGaeDAO(super.objectifyFactory);

        this.userGaeDAO = new UserGaeDAOMock(super.objectifyFactory);
        this.accountDAO = new AccountDAOMock(super.objectifyFactory);

        this.manager = new UserAdministrationManagerImpl(this.userGaeDAO,
                this.accountDAO
        );
    }

//	@Test
//	public void createNewUserAccount_BoundaryConditions(){
//		try {
//			this.manager.createNewUserAccount(null, null);
//			fail();
//		} catch (Exception e) {
//			assertTrue(true);
//		}
//
//		UserGAE user = new UserGAE("user", "12345", true);
//		try {
//			this.manager.createNewUserAccount(user, null);
//			fail();
//		} catch (Exception e) {
//			assertTrue(true);
//		}
//		
//		Account account = new Account(null, "David", "D.", "ME", "example@a.com", null, null, null, null, null, null, AccountType.Diamond, null);
//		try {
//			this.manager.createNewUserAccount(user, account);
//			fail();
//		} catch (Exception e) {
//			assertTrue(true);
//		}
//
//	}

    @Test
    public void createNewUserAccount_RightResults() {
        Objectify ofy = super.objectifyFactory.begin();

        UserGAE user = new UserGAE("user", "12345", true);
        Account account = new Account(null, "David", "D.", "example@example.com", null);

        this.manager.createNewUserAccount(user, account);

        UserGAE userFromDatastore = ofy.load().key(Key.create(UserGAE.class, user.getUsername())).now();
        Account accountFromDatastore = ofy.load().type(Account.class)
                .ancestor(user)
                .first().now();
        assertEquals(user, userFromDatastore);
        assertEquals(account.getUser(), accountFromDatastore.getUser());
    }


    @Test
    public void getEnabledUsers_BoundaryConditions() {
        assertEquals(0, this.manager.getEnabledUsers().size());
    }


    @Test
    public void getEnabledUsers_RightResults() {
//		List<Account> accounts = generateManyAccountsAndPersistThem();
//		
//		List<Account> userAccountList = this.manager.getEnabledUsers();
//
//		compareIfList1ContainsList2Objects(accounts, userAccountList);
//		int enabledUsers = accounts.size() - 1; 
//		assertEquals(enabledUsers, userAccountList.size());

//		throw new UnsupportedOperationException(); 

    }

    @Test
    public void getDisabledUsers_BoundaryConditions() {
        assertEquals(0, this.manager.getDisabledUsers().size());
    }

    @Test
    public void getDisabledUsers_RightResults() {
//		List<Account> accounts = generateManyAccountsAndPersistThem();
//		
//		List<Account> userAccountList = this.manager.getDisabledUsers();
//
//		compareIfList1ContainsList2Objects(accounts, userAccountList);
//		assertEquals(1, userAccountList.size());

//		throw new UnsupportedOperationException(); 

    }

    @SuppressWarnings("unused")
    private List<Account> generateManyAccountsAndPersistThem() {
        Objectify ofy = super.objectifyFactory.begin();

//		List<UserGAE> users = new ArrayList<UserGAE>();
        List<Account> accounts = new ArrayList<Account>();

        UserGAE user1 = new UserGAE("user1", "12345", true, false);
        UserGAE user2 = new UserGAE("user2", "142345", false, true);
        UserGAE user3 = new UserGAE("user3", "012345", false, true);
        Key<UserGAE> userKey1 = Key.create(UserGAE.class, "user1");
        Key<UserGAE> userKey2 = Key.create(UserGAE.class, "user2");
        Key<UserGAE> userKey3 = Key.create(UserGAE.class, "user3");
        ofy.save().entities(user1).now();
        ofy.save().entities(user2).now();
        ofy.save().entities(user3).now();
//		users.add(user2);
//		users.add(user);

        Account account1 = new Account(null, "David", "D.", "example@example.com", userKey1);
        Account account2 = new Account(null, "David", "D.", "example@example.com", userKey2);
        Account account3 = new Account(null, "David", "D.", "example@example.com", userKey3);
        accounts.add(account1);
        accounts.add(account2);
        accounts.add(account3);
        ofy.save().entities(account1).now();
        ofy.save().entities(account2).now();
        ofy.save().entities(account3).now();
        return accounts;
    }

    @SuppressWarnings("unused")
    private void compareIfList1ContainsList2Objects(
            List<Account> list1, List<Account> list2) {
        for (Account account : list2) {
            assertTrue(list1.contains(account));
        }
    }

}
