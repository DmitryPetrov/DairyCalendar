package net.personal.dairycalendar.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.personal.dairycalendar.service.Role;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class SecurityController {

    @GetMapping("/public")
    public ResponseEntity<String> testPermitAllAccess() {
        log.debug("Permit all access: OK");
        return ResponseEntity.ok().body("public");
    }

    @GetMapping("/secured")
    public ResponseEntity<String> testAuthenticatedAccess() {
        log.debug("Authenticated access: OK");
        return ResponseEntity.ok().body("secured");
    }

    @GetMapping("/user")
    public ResponseEntity<String> testHasRoleUserAccess() {
        log.debug("Has role [{}] access: OK", Role.USER.name());
        return ResponseEntity.ok().body("user");
    }

    @GetMapping("/admin")
    public ResponseEntity<String> testHasRoleAdminAccess() {
        log.debug("Has role [{}] access: OK", Role.ADMIN.name());
        return ResponseEntity.ok().body("admin");
    }
}
