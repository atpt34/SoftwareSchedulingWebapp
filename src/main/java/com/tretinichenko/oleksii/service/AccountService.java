package com.tretinichenko.oleksii.service;

import java.util.List;

import com.tretinichenko.oleksii.entity.Account;

public interface AccountService {

	Account findAccountByName(String userName);
	
	void updateAccount(Account account);
	
	void deleteAccountByName(String userName); 
	
	List<Account> listAllAccounts();
	
}
