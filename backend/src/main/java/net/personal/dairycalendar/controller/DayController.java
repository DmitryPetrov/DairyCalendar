package net.personal.dairycalendar.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.personal.dairycalendar.dto.DayDto;
import net.personal.dairycalendar.service.DayService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class DayController {

    private final DayService dayService;

    @PostMapping(value = "/day")
    public ResponseEntity<Void> addCourse(@RequestBody List<DayDto> dayDto) {
        dayService.save(dayDto);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
