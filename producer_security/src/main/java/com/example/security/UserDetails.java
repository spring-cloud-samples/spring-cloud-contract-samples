package com.example.security;

import java.util.Collection;
import java.util.Objects;

import org.springframework.lang.NonNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

/**
 * <p>
 * Data transfer object which represents a {@link UserDetails} object.
 * </p>
 */
public class UserDetails
		implements org.springframework.security.core.userdetails.UserDetails {

	private static final long serialVersionUID = 4376646321634775767L;

	private static String ROLE = "role";

	@NonNull
	private String username;

	@NonNull
	private Integer age;

	@java.beans.ConstructorProperties({ "username", "age" })
	UserDetails(String username, Integer age) {
		this.username = username;
		this.age = age;
	}

	public static UserDetailsBuilder builder() {
		return new UserDetailsBuilder();
	}

	/**
	 * <p>
	 * Returns the authorities granted to the user.
	 * </p>
	 * @return the authorities.
	 */
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return AuthorityUtils.createAuthorityList(ROLE);
	}

	/**
	 * <p>
	 * Returns the password used to authenticate the user.
	 * </p>
	 * @return the password.
	 */
	@Override
	public String getPassword() {
		return null;
	}

	/**
	 * <p>
	 * Indicates whether the user's account has expired. An expired account cannot be
	 * authenticated.
	 * </p>
	 * @return <code>true</code> if the user's account is valid (ie non-expired),
	 * <code>false</code> if no longer valid (ie expired).
	 */
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	/**
	 * <p>
	 * Indicates whether the user is locked or unlocked. A locked user cannot be
	 * authenticated.
	 * </p>
	 * @return <code>true</code> if the user is not locked, <code>false</code> otherwise.
	 */
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	/**
	 * <p>
	 * Indicates whether the user's credentials (password) has expired. Expired
	 * credentials prevent authentication.
	 * </p>
	 * @return <code>true</code> if the user's credentials are valid (ie non-expired),
	 * <code>false</code> if no longer valid (ie expired).
	 */
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	/**
	 * <p>
	 * Indicates whether the user is enabled or disabled. A disabled user cannot be
	 * authenticated.
	 * </p>
	 * @return <code>true</code> if the user is enabled, <code>false</code> otherwise.
	 */
	@Override
	public boolean isEnabled() {
		return true;
	}

	@NonNull
	public String getUsername() {
		return this.username;
	}

	@NonNull
	public Integer getAge() {
		return this.age;
	}

	public boolean equals(final Object o) {
		if (o == this)
			return true;
		if (!(o instanceof UserDetails))
			return false;
		final UserDetails other = (UserDetails) o;
		if (!other.canEqual((Object) this))
			return false;
		final Object this$username = this.getUsername();
		final Object other$username = other.getUsername();
		if (!Objects.equals(this$username, other$username))
			return false;
		final Object this$age = this.getAge();
		final Object other$age = other.getAge();
		if (!Objects.equals(this$age, other$age))
			return false;
		return true;
	}

	protected boolean canEqual(final Object other) {
		return other instanceof UserDetails;
	}

	public int hashCode() {
		final int PRIME = 59;
		int result = 1;
		final Object $username = this.getUsername();
		result = result * PRIME + ($username == null ? 43 : $username.hashCode());
		final Object $age = this.getAge();
		result = result * PRIME + ($age == null ? 43 : $age.hashCode());
		return result;
	}

	public String toString() {
		return "UserDetails(username=" + this.getUsername() + ", age=" + this.getAge()
				+ ")";
	}

	public static class UserDetailsBuilder {

		private String username;

		private Integer age;

		UserDetailsBuilder() {
		}

		public UserDetails.UserDetailsBuilder username(String username) {
			this.username = username;
			return this;
		}

		public UserDetails.UserDetailsBuilder age(Integer age) {
			this.age = age;
			return this;
		}

		public UserDetails build() {
			return new UserDetails(username, age);
		}

		public String toString() {
			return "UserDetails.UserDetailsBuilder(username=" + this.username + ", age="
					+ this.age + ")";
		}

	}

}
