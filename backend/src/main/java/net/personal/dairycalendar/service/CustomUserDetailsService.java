package net.personal.dairycalendar.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.personal.dairycalendar.storage.repository.AppUserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final AppUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @Override
    public UserDetails loadUserByUsername(String login) {
        System.out.println("CustomUserDetailsService.loadUserByUsername:" + login);
        return new AppUser(
                login,
                passwordEncoder.encode("1"),
                Set.of(new SimpleGrantedAuthority("ROLE_USER"))
        );

/*        return userRepository
                .findByLogin(login)
                .map(AppUser::new)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "There is no entity [" + AppUserEntity.class + "] " +
                                "with field [login] has value [" + login + "] in database"));*/
    }
}