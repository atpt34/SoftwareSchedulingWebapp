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
	
	@Autowired
	MyDBAuthenticationService myDBAuthenticationService;
	
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		
		// accounts in memory
		// 'ROLE_' appended automatically ! 
		auth.inMemoryAuthentication()
			.withUser("employee").password("employee")
			.roles(UserRole.EMPLOYEE.toString());
		auth.inMemoryAuthentication()
			.withUser("manager").password("manager")
			.roles(UserRole.MANAGER.toString());
		auth.inMemoryAuthentication()
			.withUser("admin").password("admin")
			.roles(UserRole.ADMINISTRATOR.toString());
		
		// accounts in database:
		auth.userDetailsService(myDBAuthenticationService);
	}

	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {
		
		httpSecurity.csrf().disable();
		
		// the pages does not require login
		httpSecurity.authorizeRequests().
			antMatchers("/", "/index", "/welcome", 
					"/login", "/home", "/logout").permitAll();
		
		// deny anything else
//		httpSecurity.authorizeRequests().
//		antMatchers("/*").denyAll();
		
		// requires any role:
		httpSecurity.authorizeRequests()
				.antMatchers("/accountInfo")
				.access(
				"hasAnyRole('ROLE_EMPLOYEE', 'ROLE_MANAGER', 'ROLE_ADMINISTRATOR')");
		
		// requires EMPLOYEE role:
		httpSecurity.authorizeRequests()
			.antMatchers("/employee", "/employee/*")
			.access("hasRole('ROLE_EMPLOYEE')");
		
		// requires EMPLOYEE role:
		httpSecurity.authorizeRequests()
			.antMatchers("/manager", "/manager/*")
			.access("hasRole('ROLE_MANAGER')");
				
		// requires EMPLOYEE role:
		httpSecurity.authorizeRequests()
			.antMatchers("/admin", "/admin/*")
			.access("hasRole('ROLE_ADMINISTRATOR')");
		
		// exception handling page
		// AccessDeniedException will throw
		httpSecurity.authorizeRequests().and().exceptionHandling()
			.accessDeniedPage("/403");
		
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
