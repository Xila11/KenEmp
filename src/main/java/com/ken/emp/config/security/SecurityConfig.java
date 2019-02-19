package com.ken.emp.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import com.ken.emp.config.RestServiceConfig;

/**
 * This class implements application security configurations
 * 
 * @author cmenerip
 *
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private AuthenticationEntryPoint authEntryPoint;

	/**
	 * configure HTTP security depending on the URL and HTTP methods
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// Basic authentication for employee delete function only. using in memory user and password given below.
		// password == "password"
		http.csrf().disable().authorizeRequests()
				.antMatchers(HttpMethod.DELETE, RestServiceConfig.appBasePath + "/employees").authenticated().and()
				.httpBasic().authenticationEntryPoint(authEntryPoint);
	}

	/**
	 * provides authentication credentials and roles
	 */
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication().withUser("dilan")
				.password("$2a$04$AjFEmZeX7mN8zSn57PUEZeJgBeoKMvwteZMBiP57Jb4AGFsUORmLC").roles("ADMIN");
	}

}
