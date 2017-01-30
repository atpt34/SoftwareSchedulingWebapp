package com.tretinichenko.oleksii.service.impl;

import java.util.List;

import com.tretinichenko.oleksii.dao.AccountDAO;
import com.tretinichenko.oleksii.entity.Account;
import com.tretinichenko.oleksii.service.AccountService;

public class AccountServiceImpl implements AccountService {
	
	private AccountDAO accountDAO;

	@Override
	public Account findAccountByName(String userName) {
		return accountDAO.findAccountByName(userName);
	}

	@Override
	public void updateAccount(Account account) {
		accountDAO.updateAccount(account);		
	}

	@Override
	public void deleteAccountByName(String userName) {
		accountDAO.deleteAccountByName(userName);
	}

	@Override
	public List<Account> listAllAccounts() {
		return accountDAO.listAllAccounts();
	}

}
