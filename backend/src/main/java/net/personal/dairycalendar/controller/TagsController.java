package net.personal.dairycalendar.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.personal.dairycalendar.service.TagService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class TagsController {

    private final TagService service;
    private static final String BASE_URL = "/api/tag";

    public static final String  URL_GET_TAGS = BASE_URL;
    @GetMapping(value = URL_GET_TAGS)
    public ResponseEntity<List<String>> getTags() {
        List<String> tags = service.getTags();
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(tags);
    }

}
