package net.personal.dairycalendar.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.personal.dairycalendar.dto.DayDto;
import net.personal.dairycalendar.dto.LoginDto;
import net.personal.dairycalendar.service.DayService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class DayController {

    private final DayService dayService;

    @PostMapping(value = "api/day")
    public ResponseEntity<Void> addCourse(@RequestBody List<DayDto> dayDto) {
        dayService.save(dayDto);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

/*    @PostMapping("/login/process")
    public ResponseEntity<Void> gq(@RequestBody LoginDto loginDto) {
        System.out.println("==========================");
        System.out.println("login");
        System.out.println(loginDto);
        System.out.println("==========================");
        return ResponseEntity.ok().build();
    }*/
    @PostMapping("/login/success")
    public ResponseEntity<Void> ghgq(@RequestBody LoginDto loginDto) {
        System.out.println("==========================");
        System.out.println("login/success");
        System.out.println("==========================");
        return ResponseEntity.ok().build();
    }
    @PostMapping("/login/failure")
    public ResponseEntity<Void> gqhj(@RequestBody LoginDto loginDto) {
        System.out.println("==========================");
        System.out.println("login/failure");
        System.out.println("==========================");
        return ResponseEntity.ok().build();
    }

    @GetMapping("/public")
    public ResponseEntity<String> q() {
        return ResponseEntity.ok().body("public");
    }

    @GetMapping("/secured")
    public ResponseEntity<String> qkl() {
        return ResponseEntity.ok().body("secured");
    }

    @GetMapping("/user")
    public ResponseEntity<String> q2() {
        return ResponseEntity.ok().body("user");
    }

    @GetMapping("/admin")
    public ResponseEntity<String> q22() {
        return ResponseEntity.ok().body("admin");
    }
}
