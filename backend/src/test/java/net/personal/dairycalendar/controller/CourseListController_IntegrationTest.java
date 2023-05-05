package net.personal.dairycalendar.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.personal.dairycalendar.AbstractTest;
import net.personal.dairycalendar.dto.CourseDto;
import net.personal.dairycalendar.dto.CoursesDto;
import net.personal.dairycalendar.dto.DayDto;
import net.personal.dairycalendar.storage.entity.CourseEntity;
import net.personal.dairycalendar.storage.entity.DayEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Test get list of courses")
class CourseListController_IntegrationTest extends AbstractTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    private CourseEntity course1;
    private CourseEntity course2;
    private CourseEntity course3;
    private CourseEntity course4;
    private CourseEntity course5;
    @BeforeEach
    void addCoursesToDataBase() {
        course1 = saveCourse("title" + getRandom(), USER_1_USERNAME, Set.of(TAG_1_TITLE, TAG_2_TITLE));
        course2 = saveCourse("title" + getRandom(), USER_1_USERNAME, Set.of(TAG_2_TITLE, TAG_3_TITLE));
        course3 = saveCourse("title" + getRandom(), USER_1_USERNAME, Set.of(TAG_3_TITLE, TAG_4_TITLE));
        course4 = saveCourse("title" + getRandom(), USER_1_USERNAME, Set.of(TAG_4_TITLE, TAG_5_TITLE));
        course5 = saveCourse("title" + getRandom(), USER_1_USERNAME, Set.of(TAG_5_TITLE, TAG_1_TITLE));
    }

    @Test
    @WithUserDetails(USER_1_USERNAME)
    @DisplayName("Get courses without filters")
    void getUsersCourses() throws Exception {
        MvcResult result = mockMvc
                .perform(get(CourseController.URL_GET_COURSE_LIST))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        CoursesDto payload = objectMapper.readValue(result.getResponse().getContentAsString(), CoursesDto.class);

        assertEquals(LocalDate.now(), payload.getToDate());
        assertEquals(LocalDate.now().minusDays(8), payload.getFromDate());
        Set<Long> coursesId = payload.getCourses()
                .stream()
                .map(CourseDto::getId)
                .collect(Collectors.toSet());
        assertTrue(coursesId.contains(course1.getId()), "Response does not contains course1");
        assertTrue(coursesId.contains(course2.getId()), "Response does not contains course2");
        assertTrue(coursesId.contains(course3.getId()), "Response does not contains course3");
        assertTrue(coursesId.contains(course4.getId()), "Response does not contains course4");
        assertTrue(coursesId.contains(course5.getId()), "Response does not contains course5");
    }

    @Test
    @WithUserDetails(USER_1_USERNAME)
    @DisplayName("Get courses by date")
    void getUsersCoursesByDate() throws Exception {
        DayEntity dayEntity1 = saveDay(course1.getId(), LocalDate.of(2023, 3, 25), 3);
        DayEntity dayEntity2 = saveDay(course1.getId(), LocalDate.of(2023, 3, 26), 3);
        DayEntity dayEntity3 = saveDay(course1.getId(), LocalDate.of(2023, 3, 27), 3);
        DayEntity dayEntity4 = saveDay(course1.getId(), LocalDate.of(2023, 3, 28), 3);
        DayEntity dayEntity5 = saveDay(course1.getId(), LocalDate.of(2023, 3, 29), 3);
        DayEntity dayEntity6 = saveDay(course1.getId(), LocalDate.of(2023, 3, 30), 3);
        DayEntity dayEntity7 = saveDay(course1.getId(), LocalDate.of(2023, 3, 31), 3);

        MvcResult result = mockMvc
                .perform(get(CourseController.URL_GET_COURSE_LIST)
                                 .param("fromDate", dayEntity2.getDate().toString())
                                 .param("toDate", dayEntity6.getDate().toString()))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        CoursesDto payload = objectMapper.readValue(result.getResponse().getContentAsString(), CoursesDto.class);

        assertEquals(dayEntity2.getDate(), payload.getFromDate());
        assertEquals(dayEntity6.getDate(), payload.getToDate());
        Set<LocalDate> dates = payload.getCourses()
                .stream()
                .filter(courseDto -> courseDto.getId() == course1.getId())
                .findFirst()
                .orElseThrow()
                .getDays()
                .stream()
                .map(DayDto::getDate)
                .collect(Collectors.toSet());
        assertFalse(dates.contains(dayEntity1.getDate()), "Response contains day1");
        assertTrue(dates.contains(dayEntity2.getDate()), "Response does not contains day2");
        assertTrue(dates.contains(dayEntity3.getDate()), "Response does not contains day3");
        assertTrue(dates.contains(dayEntity4.getDate()), "Response does not contains day4");
        assertTrue(dates.contains(dayEntity5.getDate()), "Response does not contains day5");
        assertTrue(dates.contains(dayEntity6.getDate()), "Response does not contains day6");
        assertFalse(dates.contains(dayEntity7.getDate()), "Response contains course7");
    }

    @Test
    @WithUserDetails(USER_1_USERNAME)
    @DisplayName("Get courses by id")
    void getUsersCoursesById() throws Exception {
        MvcResult result = mockMvc
                .perform(get(CourseController.URL_GET_COURSE_LIST)
                                 .param("courses", String.valueOf(course1.getId()))
                                 .param("courses", String.valueOf(course2.getId()))
                                 .param("courses", String.valueOf(course3.getId()))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        Set<Long> coursesId = objectMapper
                .readValue(result.getResponse().getContentAsString(), CoursesDto.class)
                .getCourses()
                .stream()
                .map(CourseDto::getId)
                .collect(Collectors.toSet());
        assertTrue(coursesId.contains(course1.getId()), "Response does not contains course1");
        assertTrue(coursesId.contains(course2.getId()), "Response does not contains course2");
        assertTrue(coursesId.contains(course3.getId()), "Response does not contains course3");
        assertFalse(coursesId.contains(course4.getId()), "Response contains course4");
        assertFalse(coursesId.contains(course5.getId()), "Response contains course5");
    }

    @Test
    @WithUserDetails(USER_1_USERNAME)
    @DisplayName("Get courses by tags")
    void getTasksByTags() throws Exception {
        MvcResult result = mockMvc
                .perform(get(CourseController.URL_GET_COURSE_LIST)
                                 .param("tags", TAG_2_TITLE)
                                 .param("tags", TAG_3_TITLE))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        Set<Long> coursesId = objectMapper
                .readValue(result.getResponse().getContentAsString(), CoursesDto.class)
                .getCourses()
                .stream()
                .map(CourseDto::getId)
                .collect(Collectors.toSet());
        assertTrue(coursesId.contains(course1.getId()), "Response does not contains course1");
        assertTrue(coursesId.contains(course2.getId()), "Response does not contains course2");
        assertTrue(coursesId.contains(course3.getId()), "Response does not contains course3");
        assertFalse(coursesId.contains(course4.getId()), "Response contains course4");
        assertFalse(coursesId.contains(course5.getId()), "Response contains course5");
    }

}