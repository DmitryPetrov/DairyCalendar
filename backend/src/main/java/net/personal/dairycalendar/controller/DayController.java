package net.personal.dairycalendar.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.personal.dairycalendar.dto.DayInformationDto;
import net.personal.dairycalendar.service.DayService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class DayController {

    private final DayService dayService;

    private static final String BASE_URL = "/api/day";

    public static final String URL_SAVE_DAY_INFO = BASE_URL;
    @PostMapping(URL_SAVE_DAY_INFO)
    public ResponseEntity<Void> saveDayInfo(@RequestBody DayInformationDto dayInfo) {
        dayService.save(dayInfo);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
