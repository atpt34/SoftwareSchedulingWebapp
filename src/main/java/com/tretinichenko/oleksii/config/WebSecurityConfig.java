package com.tretinichenko.oleksii.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import com.tretinichenko.oleksii.authentication.MyDBAuthenticationService;
import com.tretinichenko.oleksii.entity.UserRole;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
//	@Autowired
//	MyDBAuthenticationService myDBAuthenticationService;
	
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		
		// accounts in memory
//		auth.inMemoryAuthentication().withUser("").password("").roles(UserRole.EMPLOYEE);
		// 'ROLE_' appended automatically ! 
		auth.inMemoryAuthentication()
			.withUser("employee").password("employee").roles("EMPLOYEE");
		auth.inMemoryAuthentication()
			.withUser("manager").password("manager").roles("MANAGER");
		auth.inMemoryAuthentication()
			.withUser("admin").password("admin").roles("ADMIN");
		
		// accounts in database:
//		auth.userDetailsService(myDBAuthenticationService);
	}

	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {
		
		httpSecurity.csrf().disable();
		
		// the pages does not require login
		httpSecurity.authorizeRequests().
			antMatchers("/", "/index", "/welcome", 
					"/login", "/home", "/logout").permitAll();
		
		// requires EMPLOYEE role:
		httpSecurity.authorizeRequests().antMatchers("/employee", "/employee/*").access("hasRole('ROLE_EMPLOYEE')");
		
		// requires EMPLOYEE role:
		httpSecurity.authorizeRequests().antMatchers("/manager", "/manager/*").access("hasRole('ROLE_MANAGER')");
				
		// requires EMPLOYEE role:
		httpSecurity.authorizeRequests().antMatchers("/admin", "/admin/*").access("hasRole('ROLE_ADMIN')");
		
		// exception handling page
		// AccessDeniedException will throw
		httpSecurity.authorizeRequests().and().exceptionHandling().accessDeniedPage("/403");
		
		// config for login form
		httpSecurity.authorizeRequests().and().formLogin()
			// submit url for login page
			.loginProcessingUrl("/security_check") // submit url
			.loginPage("/login")
			.defaultSuccessUrl("/accountInfo")
			.failureUrl("/login?error=true")
			.usernameParameter("username")
			.passwordParameter("password")
			// config for logout page
			.and().logout().logoutUrl("/logout")
			.logoutSuccessUrl("/logoutSuccessful");
		
	}
}
