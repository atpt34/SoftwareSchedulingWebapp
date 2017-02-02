package com.tretinichenko.oleksii.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tretinichenko.oleksii.dao.AccountDAO;
import com.tretinichenko.oleksii.entity.Account;
import com.tretinichenko.oleksii.entity.UserRole;


@Service
@Transactional
public class AccountDAOImpl extends JdbcDaoSupport implements AccountDAO {

	private static final String SELECT_ACCOUNT_BY_USERNAME = 
			"SELECT * FROM Account WHERE userName = ?";
	
	private static final String SELECT_ALL_ACCOUNTS = 
			"SELECT * FROM Account";
	
	private static final String UPDATE_ACCOUNT = 
			"UPDATE Account "
			+ "SET password = ?, userRole = ?, employeeId = ? "
			+ "WHERE userName = ?";
	
	private static final String INSERT_ACCOUNT = 
			"INSERT INTO "
			+ "Account(userName, password, userRole, employeeId) "
			+ "VALUES(?, ?, ?, ?)";
	
	private static final String DELETE_ACCOUNT_BY_USERNAME = 
			"DELETE FROM Account WHERE userName = ?";
	
	@Autowired
	public AccountDAOImpl(DataSource dataSource){
		this.setDataSource(dataSource);
	}
	
	@Override
	public Account findAccountByName(String userName) {
		return this.getJdbcTemplate()
				.queryForObject(SELECT_ACCOUNT_BY_USERNAME, 
						new AccountRowMapper(), userName);
	}

	@Override
	public void saveAccount(Account account) {
		this.getJdbcTemplate().update(INSERT_ACCOUNT, 
				account.getUserName(), account.getPassword(), 
				account.getUserRole(), account.getEmployeeId());	
	}
	
	@Override
	public void updateAccount(Account account) {
		this.getJdbcTemplate().update(UPDATE_ACCOUNT, 
				account.getPassword(), account.getUserRole(), 
				account.getEmployeeId(), account.getUserName());
	}

	@Override
	public void deleteAccountByName(String userName) {
		this.getJdbcTemplate().update(DELETE_ACCOUNT_BY_USERNAME, userName);
	}

	@Override
	public List<Account> listAllAccounts() {

		return this.getJdbcTemplate().query(SELECT_ALL_ACCOUNTS, 
				new AccountRowMapper());
	}

	private static final class AccountRowMapper implements RowMapper<Account> {
		public Account mapRow(ResultSet rs, int rowNum) throws SQLException {
			String userName = rs.getString("userName");
			String password = rs.getString("password");
			int employeeId = rs.getInt("employeeId");
			UserRole userRole = UserRole.getUserRole(rs.getString("userRole"));
			return new Account(userName, password, userRole, employeeId);
		}
	}


}
