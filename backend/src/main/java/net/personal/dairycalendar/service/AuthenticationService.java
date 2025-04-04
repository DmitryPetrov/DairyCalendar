package net.personal.dairycalendar.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.personal.dairycalendar.exception.RecordIsNotExistException;
import net.personal.dairycalendar.storage.entity.AppUserEntity;
import net.personal.dairycalendar.storage.repository.AppUserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService implements UserDetailsService {
    private final AppUserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        log.debug("Load user [{}] for authentication", username);
        return userRepository
                .findByUsername(username)
                .map(AppUserEntity::toUser)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "There is no entity [" + AppUserEntity.class + "] " +
                                "with field [username] has value [" + username + "] in database"));
    }

    public AppUser getCurrentUser() {
        log.debug("Get current user");
        return (AppUser) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
    }

    public long getCurrentUserId() {
        log.debug("Get current user id");
        return this.getCurrentUser().getId();
    }

    public AppUserEntity getCurrentUserEntity() {
        return userRepository
                .findById(getCurrentUserId())
                .orElseThrow(() -> new RecordIsNotExistException(AppUserEntity.class, getCurrentUserId()));
    }

    public boolean isAuthenticated() {
        Collection<? extends GrantedAuthority> userAuthorities = SecurityContextHolder.getContext()
                .getAuthentication()
                .getAuthorities();
        Set<GrantedAuthority> roles = Arrays.stream(Role.values())
                .map(Role::getGrantedAuthority)
                .collect(Collectors.toSet());
        for (GrantedAuthority authority: userAuthorities) {
            if (roles.contains(authority)) {
                return true;
            }
        }
        return false;
    }
}