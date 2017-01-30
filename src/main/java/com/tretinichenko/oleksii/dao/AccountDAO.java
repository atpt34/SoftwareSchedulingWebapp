package com.tretinichenko.oleksii.dao;

import java.util.List;

import com.tretinichenko.oleksii.entity.Account;

public interface AccountDAO {

	Account findAccountByName(String userName);
	
	void updateAccount(Account account);
	
	void deleteAccountByName(String userName); 
	
	List<Account> listAllAccounts();
	
}
