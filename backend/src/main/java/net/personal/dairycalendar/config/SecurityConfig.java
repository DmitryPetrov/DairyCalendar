package net.personal.dairycalendar.config;

import jakarta.servlet.http.HttpServletResponse;
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

import java.util.Arrays;

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
/*                .authorizeHttpRequests()
                    .requestMatchers("/index**", "/static/**", "/*.js", "/*.json", "/*.ico").permitAll()
                    .requestMatchers("/public").permitAll()
                    .requestMatchers("/secured").authenticated()
                    .requestMatchers("/api/**").authenticated()
                    .requestMatchers("/user").hasRole("USER")
                    .requestMatchers("/admin").hasRole("ADMIN")
                    .anyRequest().denyAll()
                .and()*/
                .authorizeHttpRequests((authorizeHttpRequests) ->
                    authorizeHttpRequests
                            .requestMatchers("/index**", "/static/**", "/*.js", "/*.json", "/*.ico").permitAll()
                            .requestMatchers("/public").permitAll()
                            .requestMatchers("/secured").authenticated()
                            .requestMatchers("/api/**").authenticated()
                            .requestMatchers("/user").hasRole("USER")
                            .requestMatchers("/admin").hasRole("ADMIN")
                            .anyRequest().denyAll()
                )
                .formLogin()
                    .loginPage("/").permitAll()
                    .loginProcessingUrl("/login/process").permitAll()
                    .successHandler((request, response, authentication) -> {
                        System.out.println("=====================auth suxxess=====================");
                    })
                    .failureHandler((request, response, authentication) -> {
                        System.out.println("=====================auth NOT suxxess=====================");
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    })
                .and()
                .logout()
                    .invalidateHttpSession(true)
                    .clearAuthentication(true)
                    .logoutSuccessHandler((request, response, authentication) -> {
                        System.out.println("=====================logout suxxess=====================");
                    })
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                .and()
                .build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
        configuration.setAllowedMethods(Arrays.asList("GET","POST"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
