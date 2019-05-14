package com.example.auth;

import static org.springframework.util.Assert.notNull;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import com.example.security.oauth2.provider.token.UserAuthenticationConverter;

/**
 * <p>For restricting an access to the rest endpoints.</p>
 */
/*
 * @Configuration
 * @EnableGlobalMethodSecurity(prePostEnabled = true)
 * @EnableResourceServer
 */
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

    /**
     * <p>Configures security.</p>
     *
     * @param http security to configure
     * @throws Exception if something unexpected happened
     */
    @Override
    public void configure(HttpSecurity http) throws Exception {
        notNull(http, "http");
        http.csrf().disable().authorizeRequests().antMatchers("/**").permitAll();
    }

    /**
     * <p>Configures jwt related access token converter to allow enhanced user details to be converted.</p>
     *
     * @param jwtAccessTokenConverter the converter to configure
     */
    /* @Autowired */
    public void configure(JwtAccessTokenConverter jwtAccessTokenConverter) {
        notNull(jwtAccessTokenConverter, "jwtAccessTokenConverter");
        DefaultAccessTokenConverter defaultAccessTokenConverter =
            (DefaultAccessTokenConverter) jwtAccessTokenConverter.getAccessTokenConverter();
        defaultAccessTokenConverter.setUserTokenConverter(new UserAuthenticationConverter());
    }
}
