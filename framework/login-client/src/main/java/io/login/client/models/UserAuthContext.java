package io.login.client.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@NoArgsConstructor
@Getter
public class UserAuthContext implements UserDetails {

    private UserAccount userRequest;
    private UserDetails userDetails;

    public UserAuthContext(UserAccount userRequest, UserDetails userDetails) {
        this.userRequest = userRequest;
        this.userDetails = userDetails;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        if(this.userDetails==null)
            return null;
            return userDetails.getPassword();
    }

    @Override
    public String getUsername()
    {
        return userRequest.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
