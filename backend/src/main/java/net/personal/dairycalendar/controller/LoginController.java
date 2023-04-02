package net.personal.dairycalendar.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class LoginController {
    private static final String BASE_URL = "/api/login";

    public static final String  URL_CHECK = BASE_URL + "/check";

    @GetMapping(URL_CHECK)
    public ResponseEntity<Void> getCourse() {
        return ResponseEntity.ok().build();
    }

}
