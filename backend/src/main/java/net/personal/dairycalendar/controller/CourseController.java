package net.personal.dairycalendar.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.personal.dairycalendar.dto.CourseDto;
import net.personal.dairycalendar.dto.CoursesDto;
import net.personal.dairycalendar.dto.IdDto;
import net.personal.dairycalendar.service.CourseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CourseController {
    private final CourseService service;
    private static final String BASE_URL = "/api/course";

    public static final String URL_GET_COURSE_LIST = BASE_URL;
    @GetMapping(URL_GET_COURSE_LIST)
    public ResponseEntity<CoursesDto> getUsersCourses(
            @RequestParam(required = false) LocalDate fromDate,
            @RequestParam(required = false) LocalDate toDate,
            @RequestParam(required = false) Set<String> tags,
            @RequestParam(required = false) Set<Long> courses
    ) {
        if (toDate == null) {
            toDate = LocalDate.now();
        }
        if (fromDate == null) {
            fromDate = toDate.minusDays(8);
        }
        if (tags == null) {
            tags = Set.of();
        }
        if (courses == null) {
            courses = Set.of();
        }
        List<CourseDto> result = service.getCoursesForCurrentUser(fromDate, toDate, tags, courses);
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new CoursesDto(result, fromDate, toDate));
    }

    public static final String  URL_ADD_NEW_COURSE = BASE_URL;
    @PostMapping(URL_ADD_NEW_COURSE)
    public ResponseEntity<IdDto> addCourse(@RequestBody CourseDto courseDto) {
        long id = service.addCourse(courseDto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new IdDto(id));
    }

    public static final String  URL_GET_COURSE = BASE_URL + "/{id}";
    @GetMapping(URL_GET_COURSE)
    public ResponseEntity<CourseDto> getCourse(@PathVariable long id) {
        CourseDto courseDto = service.getCourse(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(courseDto);
    }

    public static final String  URL_UPDATE_COURSE = BASE_URL + "/{id}";
    @PutMapping(URL_UPDATE_COURSE)
    public ResponseEntity<IdDto> updateCourse(@PathVariable long id, @RequestBody CourseDto courseDto) {
        service.updateCourse(id, courseDto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new IdDto(id));
    }

    public static final String  URL_DELETE_COURSE = BASE_URL + "/{id}";
    @DeleteMapping(URL_DELETE_COURSE)
    public ResponseEntity<IdDto> deleteCourse(@PathVariable long id) {
        service.deleteCourse(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new IdDto(id));
    }

}
