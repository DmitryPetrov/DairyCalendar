package net.personal.dairycalendar.service;

import lombok.Data;
import net.personal.dairycalendar.storage.entity.AppUserEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Data
public class AppUser implements UserDetails {

    private final String login;
    private final String password;
    private final Set<GrantedAuthority> authorities;

    public AppUser(AppUserEntity entity) {
        this.login = entity.getLogin();
        this.password = entity.getPassword();
        this.authorities = Set.of();
    }

    public AppUser(String login, String password, Collection<? extends GrantedAuthority> authorities) {
        this.login = login;
        this.password = password;
        this.authorities = new HashSet<>(authorities);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return login;
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
