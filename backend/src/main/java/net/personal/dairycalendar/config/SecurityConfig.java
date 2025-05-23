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

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain configure(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf().disable()
                .cors()
                .and()
                .authorizeHttpRequests((authorizeHttpRequests) ->
                    authorizeHttpRequests
                            .requestMatchers("/index**", "/static/**", "/login/**", "/*.js", "/*.json", "/*.ico").permitAll()
                            .requestMatchers("/public").permitAll()
                            .requestMatchers("/secured").authenticated()
                            .requestMatchers("/api/**").authenticated()
                            .requestMatchers("/user").hasRole(Role.USER.name())
                            .requestMatchers("/admin").hasRole(Role.ADMIN.name())
                            .anyRequest().denyAll()
                )
                .formLogin()
                    .loginPage("/").permitAll()
                    .loginProcessingUrl("/login/process").permitAll()
                    .successHandler((req, resp, auth) -> System.out.println("===login successful==="))
                    .failureHandler((req, resp, auth) -> {
                        System.out.println("===login unsuccessful===");
                        resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    })
                .and()
                .logout()
                    .logoutUrl("/logout").permitAll()
                    .invalidateHttpSession(true)
                    .clearAuthentication(true)
                    .logoutSuccessHandler((req, resp, auth) -> System.out.println("===logout successful==="))
                .and()
                .httpBasic(withDefaults())
                .exceptionHandling()
                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                .and()
                .build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:3000",//current device front dev
                                                "http://localhost:8888",//current device prod (docker)
                                                "http://localhost:8181",//current device back dev
                                                "http://192.168.0.119:8888",//macbook pro in home net
                                                "http://192.168.0.165:8888",//iphone in home net
                                                "http://192.168.0.233:8888",//DESKTOP_PC in home net
                                                "http://192.168.0.107:3000",
                                                "http://192.168.0.107:8888",
                                                "http://192.168.0.107:8181",
                                                "http://192.168.0.173:3000",
                                                "http://192.168.0.173:8888",
                                                "http://192.168.0.173:8181"));
        configuration.setAllowedMethods(List.of(CorsConfiguration.ALL));
        configuration.setAllowedHeaders(List.of(CorsConfiguration.ALL));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
