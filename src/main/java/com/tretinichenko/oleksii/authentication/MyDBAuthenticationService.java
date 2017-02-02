package com.tretinichenko.oleksii.authentication;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.tretinichenko.oleksii.dao.AccountDAO;
import com.tretinichenko.oleksii.entity.Account;
import com.tretinichenko.oleksii.entity.UserRole;

@Service
public class MyDBAuthenticationService implements UserDetailsService {

	@Autowired
	AccountDAO accountDAO;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		Account account = accountDAO.findAccountByName(username);
		
		if (account == null) {
			throw new UsernameNotFoundException("User " + username + " was not found in the database");
		}
				
		List<GrantedAuthority> grantList = new ArrayList<GrantedAuthority>();
		UserRole userRole = account.getUserRole();
		if (userRole != null) {
			grantList.add(new SimpleGrantedAuthority("ROLE_" + userRole));
		}

		UserDetails userDetails = (UserDetails) new User(account.getUserName(), account.getPassword(), grantList);
		return userDetails;
	}

}
