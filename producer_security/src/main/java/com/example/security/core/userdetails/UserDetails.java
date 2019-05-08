package com.example.security.core.userdetails;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

/**
 * <p>Data transfer object which represents a {@link UserDetails} object.</p>
 */
@Getter
@Builder
@ToString
@EqualsAndHashCode
public class UserDetails implements org.springframework.security.core.userdetails.UserDetails {

    private static final long serialVersionUID = 4376646321634775767L;

    @NonNull
    private String username;
    @NonNull
    private Integer age;

    private static String ROLE = "role";

    /**
     * <p>Returns the authorities granted to the user.</p>
     *
     * @return the authorities.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return AuthorityUtils.createAuthorityList(ROLE);
    }

    /**
     * <p>Returns the password used to authenticate the user.</p>
     *
     * @return the password.
     */
    @Override
    public String getPassword() {
        return null;
    }

    /**
     * <p>Indicates whether the user's account has expired. An expired account cannot be
     * authenticated.</p>
     *
     * @return <code>true</code> if the user's account is valid (ie non-expired),
     *         <code>false</code> if no longer valid (ie expired).
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * <p>Indicates whether the user is locked or unlocked. A locked user cannot be
     * authenticated.</p>
     *
     * @return <code>true</code> if the user is not locked, <code>false</code> otherwise.
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * <p>Indicates whether the user's credentials (password) has expired. Expired
     * credentials prevent authentication.</p>
     *
     * @return <code>true</code> if the user's credentials are valid (ie non-expired),
     *         <code>false</code> if no longer valid (ie expired).
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * <p>Indicates whether the user is enabled or disabled. A disabled user cannot be
     * authenticated.</p>
     *
     * @return <code>true</code> if the user is enabled, <code>false</code> otherwise.
     */
    @Override
    public boolean isEnabled() {
        return true;
    }

}
