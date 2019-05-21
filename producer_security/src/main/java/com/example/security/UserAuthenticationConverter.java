package com.example.security;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import static org.springframework.util.Assert.notNull;

/**
 * <p>
 * Utility interface for converting a user authentication to and from a map.
 * </p>
 */
public class UserAuthenticationConverter implements
		org.springframework.security.oauth2.provider.token.UserAuthenticationConverter {

	private static final String USERDETAILS = "user_details";

	private static final String PROP_USERNAME = "username";

	private static final String PROP_AGE = "age";

	private static final String DEFAULT_VALUE = "";

	private static UserDetails extractUserDetails(Map<String, Object> userDetailsMap) {
		return UserDetails.builder().username(
				(String) userDetailsMap.getOrDefault(PROP_USERNAME, DEFAULT_VALUE))
				.age(Integer.valueOf(
						String.valueOf(userDetailsMap.getOrDefault(PROP_AGE, -1))))
				.build();
	}

	private static Map<String, Object> extractMap(UserDetails userDetails) {
		Map<String, Object> result = new HashMap<>();
		result.put(PROP_USERNAME, userDetails.getUsername());
		result.put(PROP_AGE, userDetails.getAge());
		return result;
	}

	private static List<GrantedAuthority> extractGrantedAuthorities(
			Collection<String> authorities) {
		return AuthorityUtils.commaSeparatedStringToAuthorityList(
				StringUtils.collectionToCommaDelimitedString(authorities));
	}

	/**
	 * <p>
	 * Extract information about the user to be used in an access token (i.e. for resource
	 * servers).
	 * </p>
	 * @param authentication an authentication representing a user.
	 * @return a map of key values representing the unique information about the user.
	 * @throws NullPointerException if authentication is {@code null}
	 */
	@Override
	public Map<String, ?> convertUserAuthentication(Authentication authentication) {
		notNull(authentication, "authentication");
		Map<String, Object> result = new HashMap<>();
		result.put(USERNAME, authentication.getName());
		result.put(AUTHORITIES,
				AuthorityUtils.authorityListToSet(authentication.getAuthorities()));
		result.put(USERDETAILS, extractMap((UserDetails) authentication.getPrincipal()));
		return result;
	}

	/**
	 * <p>
	 * Inverse of {@link #convertUserAuthentication(Authentication)}. Extracts an
	 * Authentication from a map.
	 * </p>
	 * @param authenticationAsMap a map of user information.
	 * @return an Authentication representing the user or null if there is none.
	 * @throws NullPointerException if authenticationAsMap is {@code null}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public Authentication extractAuthentication(
			final Map<String, ?> authenticationAsMap) {
		notNull(authenticationAsMap, "authenticationAsMap");
		String userName = (String) authenticationAsMap.get(USERNAME);
		Map<String, Object> userDetailsMap = (Map<String, Object>) authenticationAsMap
				.get(USERDETAILS);
		if (userName == null && userDetailsMap == null) {
			return null;
		}
		Assert.hasText(userName, "userName");
		notNull(userDetailsMap, "userDetailsMap");
		List<String> authorities = (List<String>) authenticationAsMap.get(AUTHORITIES);
		notNull(authorities, "authorities");
		UserDetails userDetails = extractUserDetails(userDetailsMap);
		List<GrantedAuthority> grantedAuthorities = extractGrantedAuthorities(
				authorities);
		return new UsernamePasswordAuthenticationToken(userDetails, null,
				grantedAuthorities);
	}

}
