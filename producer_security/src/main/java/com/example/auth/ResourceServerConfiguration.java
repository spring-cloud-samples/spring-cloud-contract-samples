package com.example.auth;

import com.example.security.UserAuthenticationConverter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import static org.springframework.util.Assert.notNull;

/**
 * <p>
 * For restricting an access to the rest endpoints.
 * </p>
 */
@Configuration
@EnableResourceServer
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

	/**
	 * <p>
	 * Configures security.
	 * </p>
	 * @param http security to configure
	 * @throws Exception if something unexpected happened
	 */
	@Override
	public void configure(HttpSecurity http) throws Exception {
		notNull(http, "http");
		http.csrf().disable().authorizeRequests().antMatchers("/**").authenticated().and()
				.exceptionHandling().accessDeniedHandler(new OAuth2AccessDeniedHandler());
	}

	/**
	 * <p>
	 * Configures jwt related access token converter to allow enhanced user details to be
	 * converted.
	 * </p>
	 * @param jwtAccessTokenConverter the converter to configure
	 */
	@Autowired
	public void configure(JwtAccessTokenConverter jwtAccessTokenConverter) {
		notNull(jwtAccessTokenConverter, "jwtAccessTokenConverter");
		DefaultAccessTokenConverter defaultAccessTokenConverter = (DefaultAccessTokenConverter) jwtAccessTokenConverter
				.getAccessTokenConverter();
		defaultAccessTokenConverter
				.setUserTokenConverter(new UserAuthenticationConverter());
	}

}
