package net.personal.dairycalendar.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.personal.dairycalendar.storage.entity.AppUserEntity;
import net.personal.dairycalendar.storage.repository.AppUserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final AppUserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) {
        System.out.println("CustomUserDetailsService.loadUserByUsername:" + username);

        return userRepository
                .findByUsername(username)
                .map(AppUserEntity::toUser)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "There is no entity [" + AppUserEntity.class + "] " +
                                "with field [username] has value [" + username + "] in database"));
    }
}