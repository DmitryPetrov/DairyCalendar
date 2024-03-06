package net.personal.dairycalendar.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.personal.dairycalendar.service.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class LoginController {
    private static final String BASE_URL = "/login";

    public static final String  URL_CHECK = BASE_URL + "/check";

    private final AuthenticationService service;

    @GetMapping(URL_CHECK)
    public ResponseEntity<Boolean> isLoggedIn() {
        boolean authenticated = service.isAuthenticated();
        return ResponseEntity.ok().body(authenticated);
    }

}
