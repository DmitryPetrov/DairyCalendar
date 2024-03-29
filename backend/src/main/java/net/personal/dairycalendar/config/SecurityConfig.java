package net.personal.dairycalendar.config;

import jakarta.servlet.http.HttpServletResponse;
import net.personal.dairycalendar.service.Role;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain configure(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf().disable()
                .cors()
                    .configurationSource(corsConfigurationSource())
                .and()
                .authorizeHttpRequests((authorizeHttpRequests) ->
                    authorizeHttpRequests
                            .requestMatchers("/index**", "/static/**", "/*.js", "/*.json", "/*.ico").permitAll()
                            .requestMatchers("/public").permitAll()
                            .requestMatchers("/secured").authenticated()
                            .requestMatchers("/api/**").authenticated()
                            .requestMatchers("/user").hasRole(Role.USER.name())
                            .requestMatchers("/admin").hasRole(Role.ADMIN.name())
                            .anyRequest().denyAll()
                )
                .formLogin()
                    .loginPage("/").permitAll()
                    .loginProcessingUrl("/api/login/process").permitAll()
                    .successHandler((req, resp, auth) -> System.out.println("===login successful==="))
                    .failureHandler((req, resp, auth) -> {
                        System.out.println("===login unsuccessful===");
                        resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    })
                .and()
                .logout()
                    .logoutUrl("/api/logout").permitAll()
                    .invalidateHttpSession(true)
                    .clearAuthentication(true)
                    .logoutSuccessHandler((req, resp, auth) -> System.out.println("===logout successful==="))
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                .and()
                .build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:3000"));
        configuration.setAllowedMethods(List.of("GET","POST","PUT","DELETE", "*"));
        configuration.setAllowCredentials(true);
        configuration.setAllowedHeaders(List.of("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
