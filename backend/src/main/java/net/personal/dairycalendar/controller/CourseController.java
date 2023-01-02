package net.personal.dairycalendar.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.personal.dairycalendar.dto.CourseDto;
import net.personal.dairycalendar.dto.CoursesDto;
import net.personal.dairycalendar.dto.IdDto;
import net.personal.dairycalendar.dto.mapper.CourseMapper;
import net.personal.dairycalendar.service.CourseService;
import net.personal.dairycalendar.service.TagService;
import net.personal.dairycalendar.storage.entity.CourseEntity;
import net.personal.dairycalendar.storage.repository.CourseRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CourseController {
    private final CourseService courseService;
    private final CourseRepository courseRepository;
    private final TagService tagService;
    private final CourseMapper courseMapper;

    @GetMapping(value = "/course")
    public ResponseEntity<CoursesDto> getUsersCourses(
            @RequestParam(required = false) LocalDate fromDate,
            @RequestParam(required = false) LocalDate toDate,
            @RequestParam(required = false) Set<String> tags
    ) {
        if (toDate == null) {
            toDate = LocalDate.now().plusDays(1);
        }
        if (fromDate == null) {
            fromDate = toDate.minusDays(8);
        }
        if (tags == null) {
            tags = Set.of();
        }
        List<CourseDto> result = courseService.getCoursesForCurrentUser(fromDate, toDate, tags);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new CoursesDto(result, fromDate, toDate));
    }

    @GetMapping(value = "/courseList")
    public ResponseEntity<List<CourseDto>> getUsersCoursesList() {
        List<CourseDto> result = courseService.getCoursesForCurrentUser();
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(result);
    }

    @PostMapping(value = "/course")
    public ResponseEntity<IdDto> addCourse(@RequestBody CourseDto courseDto) {
        CourseEntity entity = courseMapper.toEntity(courseDto);
        entity.setTags(tagService.getTags(courseDto.getTags()));
        courseRepository.save(entity);
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new IdDto(entity.getId()));
    }


}
