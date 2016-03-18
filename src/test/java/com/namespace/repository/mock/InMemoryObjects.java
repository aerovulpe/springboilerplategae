package com.namespace.repository.mock;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.Objectify;
import com.namespace.domain.Account;
import com.namespace.domain.UserGAE;

import java.util.ArrayList;
import java.util.List;

public class InMemoryObjects implements IDaoMocks {

	/*
	 * Begin singleton stuffs
	 */
	
	private static InMemoryObjects INSTANCE = null;
	
	private InMemoryObjects() {}

    private synchronized static void createInstance() {
        if (INSTANCE == null) { 
            INSTANCE = new InMemoryObjects();
        }
    }	
	
	public static InMemoryObjects getInstance(){
        if (INSTANCE == null) 
        	createInstance();
        
        return INSTANCE;
	}

	public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException(); 
	}
	/*End singleton stuffs*/
	
	
	/*
	 * InMemoryObjects
	 */
	private List<Account> accounts = new ArrayList<Account>();
	private List<UserGAE> users = new ArrayList<UserGAE>();

	public List<Account> getAccounts() {
		return accounts;
	}

	public void setAccounts(List<Account> accounts) {
		if(accounts != null){
			this.accounts = accounts;
		}else{
			this.accounts = new ArrayList<Account>();
		}
	}

	public List<UserGAE> getUsers() {
		return users;
	}

	public void setUsers(List<UserGAE> users) {
		if(users != null){
			this.users = users;
		}else{
			this.users = new ArrayList<UserGAE>();
		}
	}

	@Override
	public void datastoreWithNoEntities(Objectify ofy) {
		accounts = new ArrayList<Account>();
		users = new ArrayList<UserGAE>();
	}

	@Override
	public void datastoreWithOneEntity(Objectify ofy) {
		users = new ArrayList<UserGAE>();
		accounts = new ArrayList<Account>();

		UserGAE user = new UserGAE("user", "12345", true);
		Key<UserGAE> userKey = Key.create(UserGAE.class, "user");
		users.add(user);
		ofy.save().entities(user).now();
		
		Account account = new Account("David", "D.", "example@example.com", userKey);
		accounts.add(account);
		ofy.save().entities(account).now();
	}

	@Override
	public void datastoreWithManyEntities(Objectify ofy) {
		
		users = new ArrayList<UserGAE>();
		accounts = new ArrayList<Account>();

		UserGAE user = new UserGAE("user", "12345", true);
		Key<UserGAE> userKey = Key.create(UserGAE.class, "user");
		users.add(user);
		ofy.save().entities(user).now();
		
		Account account = new Account("David", "D.", "example@example.com", userKey);
		accounts.add(account);
		ofy.save().entities(account).now();
		
		UserGAE user2 = new UserGAE("user2", "12345", false);
		users.add(user2);
		ofy.save().entities(user2).now();

		Account account2 = new Account("David", "D.", "example@example.com", userKey);
		accounts.add(account2);
		ofy.save().entities(account2).now();
		
	}

	
}
