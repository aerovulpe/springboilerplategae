package com.namespace.repository;

import com.namespace.domain.Account;
import com.namespace.domain.UserGAE;

import java.util.List;

public interface UserGaeDAO extends GenericDAO<UserGAE>{

	List<UserGAE> findAll();

	List<UserGAE> findAllEnabledUsers(boolean isEnabled);

	UserGAE findByUsername(String username);
	
	UserGAE findByAccount(Account account);

	void createUserAccount(UserGAE user, Account account);
}