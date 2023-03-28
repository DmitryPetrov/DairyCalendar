package net.personal.dairycalendar.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.personal.dairycalendar.AbstractTest;
import net.personal.dairycalendar.dto.CourseDto;
import net.personal.dairycalendar.dto.IdDto;
import net.personal.dairycalendar.exception.RecordIsNotExistException;
import net.personal.dairycalendar.storage.entity.CourseEntity;
import net.personal.dairycalendar.storage.entity.DayEntity;
import net.personal.dairycalendar.storage.repository.CourseRepository;
import net.personal.dairycalendar.storage.repository.DayRepository;
import net.personal.dairycalendar.storage.repository.TagRepository;
import net.personal.dairycalendar.storage.specification.CourseDaySpecifications;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.RepetitionInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Test CRUD endpoints for courses")
class CourseController_IntegrationTest extends AbstractTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private DayRepository dayRepository;

    @Test
    @WithUserDetails(USER_1_USERNAME)
    @DisplayName("Get course by id")
    void getCourse() throws Exception {
        CourseEntity course = saveCourse("title", USER_1_USERNAME, Set.of(TAG_1_TITLE, TAG_2_TITLE), 1);

        MvcResult result = mockMvc
                .perform(get(CourseController.URL_GET_COURSE, course.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        CourseDto courseDto = objectMapper.readValue(result.getResponse().getContentAsString(), CourseDto.class);
        assertEquals("title", courseDto.getTitle(), "Course title wrong");
        assertEquals("description", courseDto.getDescription(),"Course description wrong");
        assertEquals(1, courseDto.getPosition(), "Course position wrong");
        assertEquals(Set.of(TAG_1_TITLE, TAG_2_TITLE), courseDto.getTags(), "Course tags wrong");
    }

    @RepeatedTest(3)
    @WithUserDetails(USER_1_USERNAME)
    @DisplayName("Add new course")
    void addCourse(RepetitionInfo repetitionInfo) throws Exception {
        CourseDto requestPayload = new CourseDto(
                "new course " + repetitionInfo.getCurrentRepetition(),
                10 + repetitionInfo.getCurrentRepetition(),
                "course description",
                Set.of("new tag", TAG_2_TITLE, TAG_3_TITLE)
        );
        MvcResult result = mockMvc
                .perform(
                        post(CourseController.URL_ADD_NEW_COURSE)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestPayload))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        long newCourseId = objectMapper.readValue(result.getResponse().getContentAsString(), IdDto.class).getId();

        CourseEntity entity = courseRepository
                .findById(newCourseId)
                .orElseThrow(() -> new RecordIsNotExistException(CourseEntity.class, newCourseId));

        assertEquals(USER_1_USERNAME, entity.getUser().getUsername(), "Course was not link to user");
        assertEquals(requestPayload.getTitle(), entity.getTitle(), "Course title wrong");
        assertEquals(requestPayload.getDescription(), entity.getDescription(),"Course description wrong");
        assertEquals(requestPayload.getPosition(), entity.getPosition(), "Course position wrong");
        assertEquals(requestPayload.getTags(), entity.getTags(), "Course tags wrong");
        for (String tag : requestPayload.getTags()) {
            assertEquals(1, tagRepository.findAllByTagIn(Set.of(tag)).size(),
                         "New tags not saved or saved duplicates for old tags");
        }
    }

    @Test
    @WithUserDetails(USER_1_USERNAME)
    @DisplayName("Update course")
    void updateCourse() throws Exception {
        CourseEntity course = saveCourse("title", USER_1_USERNAME, Set.of(TAG_1_TITLE, TAG_2_TITLE));
        Set<String> tagsBeforeUpdate = course.getTags();

        CourseDto requestPayload = new CourseDto(
                course.getTitle() + "_update",
                course.getPosition() + 118,
                course.getDescription() + "_update",
                Set.of("new tag", TAG_1_TITLE, TAG_3_TITLE)
        );
        MvcResult result = mockMvc
                .perform(
                        put(CourseController.URL_UPDATE_COURSE, course.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestPayload))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        long idOfUpdatedCourse = objectMapper.readValue(result.getResponse().getContentAsString(), IdDto.class).getId();
        assertEquals(course.getId(), idOfUpdatedCourse);

        CourseEntity updatedCourse = courseRepository
                .findById(course.getId())
                .orElseThrow(() -> new RecordIsNotExistException(CourseEntity.class, course.getId()));

        assertEquals(requestPayload.getTitle(), updatedCourse.getTitle(), "Course title wrong");
        assertEquals(requestPayload.getDescription(), updatedCourse.getDescription(),"Course description wrong");
        assertEquals(requestPayload.getPosition(), updatedCourse.getPosition(), "Course position wrong");
        assertEquals(requestPayload.getTags(), updatedCourse.getTags(), "Course tags wrong");
        for (String tag : requestPayload.getTags()) {
            assertEquals(1, tagRepository.findAllByTagIn(Set.of(tag)).size(),
                         "New tags not saved or saved duplicates for old tags");
        }
        for (String tag : tagsBeforeUpdate) {
            assertEquals(1, tagRepository.findAllByTagIn(Set.of(tag)).size(), "Tag [" + tag + "] deleted");
        }
    }

    @Test
    @WithUserDetails(USER_1_USERNAME)
    @DisplayName("Remove course")
    void deleteCourse() throws Exception {
        CourseEntity course = saveCourse("title", USER_1_USERNAME, Set.of(TAG_1_TITLE, TAG_2_TITLE));
        saveDay(course.getId(), LocalDate.of(2023, 3, 27), 3);

        Set<String> tagsBeforeDelete = course.getTags();
        assertFalse(course.getTags().isEmpty(), "Before delete course has tags");

        Specification<DayEntity> daysByCourse = Specification
                .where(CourseDaySpecifications.inCourse(Set.of(course.getId())));
        assertFalse(dayRepository.findAll(daysByCourse).isEmpty(), "Before delete course has days");

        MvcResult result = mockMvc
                .perform(delete(CourseController.URL_DELETE_COURSE, course.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        long idOfDeletedCourse = objectMapper.readValue(result.getResponse().getContentAsString(), IdDto.class).getId();
        assertEquals(course.getId(), idOfDeletedCourse, "Course we wanted delete and deleted are same course");
        Optional<CourseEntity> deletedCourse = courseRepository.findById(course.getId());
        assertTrue(deletedCourse.isEmpty(), "DB does not contains deleted course");
        for (String tag : tagsBeforeDelete) {
            assertEquals(1, tagRepository.findAllByTagIn(Set.of(tag)).size(), "Tag [" + tag + "] was deleted");
        }
        assertTrue(dayRepository.findAll(daysByCourse).isEmpty(), "DB contains deleted course days");
    }
}