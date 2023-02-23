package net.personal.dairycalendar.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

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
@DisplayName("Тестирование CRUD методов для курсов")
class CourseController_IntegrationTest {

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
    @WithUserDetails("user_1")
    @DisplayName("Запрос курса по id")
    void getCourse() throws Exception {
        String courseId = "1";
        MvcResult result = mockMvc
                .perform(get(CourseController.URL_GET_COURSE, courseId))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        CourseDto courseDto = objectMapper.readValue(result.getResponse().getContentAsString(), CourseDto.class);
        assertEquals("course_1", courseDto.getTitle());
        assertEquals("description", courseDto.getDescription());
        assertEquals(1, courseDto.getPosition());
        assertEquals(2, courseDto.getTags().size());
        assertEquals(Set.of("tag_1", "tag_2"), courseDto.getTags());
    }

    @Test
    @WithUserDetails("user_1")
    @DisplayName("Добавление нового курса")
    void addCourse() throws Exception {
        CourseDto requestPayload = new CourseDto(
                "new course title",
                118,
                "course description",
                Set.of("new tag", "tag_2", "tag_3")
        );
        MvcResult result = mockMvc
                .perform(
                        post(CourseController.URL_GET_ALL_COURSES)
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
        //check course saved
        assertEquals(requestPayload.getTitle(), entity.getTitle());
        assertEquals(requestPayload.getDescription(), entity.getDescription());
        assertEquals(requestPayload.getPosition(), entity.getPosition());
        assertEquals(requestPayload.getTags(), entity.getTags());

        //check only new tag saved
        //check there are no duplicates for old tags
        for (String tag : requestPayload.getTags()) {
            assertEquals(1, tagRepository.findAllByTagIn(Set.of(tag)).size());
        }
    }

    @Test
    @WithUserDetails("user_1")
    @DisplayName("Обновление курса по id")
    void updateCourse() throws Exception {
        long courseId = 2;
        CourseEntity beforeUpdate = courseRepository
                .findById(courseId)
                .orElseThrow(() -> new RecordIsNotExistException(CourseEntity.class, courseId));
        Set<String> tagsBeforeUpdate = beforeUpdate.getTags();

        String newTag = "new tag";
        String existedTagAssignedToCourse = "tag_2";
        String existedTagNotAssignedToCourse = "tag_1";
        CourseDto requestPayload = new CourseDto(
                beforeUpdate.getTitle() + "_update",
                beforeUpdate.getPosition() + 118,
                beforeUpdate.getDescription() + "_update",
                Set.of(newTag, existedTagAssignedToCourse, existedTagNotAssignedToCourse)
        );
        MvcResult result = mockMvc
                .perform(
                        put(CourseController.URL_UPDATE_COURSE, courseId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestPayload))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        long idOfUpdatedCourse = objectMapper.readValue(result.getResponse().getContentAsString(), IdDto.class).getId();
        assertEquals(courseId, idOfUpdatedCourse);

        CourseEntity entity = courseRepository
                .findById(courseId)
                .orElseThrow(() -> new RecordIsNotExistException(CourseEntity.class, courseId));
        //check course saved
        assertEquals(requestPayload.getTitle(), entity.getTitle());
        assertEquals(requestPayload.getDescription(), entity.getDescription());
        assertEquals(requestPayload.getPosition(), entity.getPosition());
        assertEquals(requestPayload.getTags(), entity.getTags());

        //check only new tag saved
        //check there are no duplicates for old tags
        for (String tag : requestPayload.getTags()) {
            assertEquals(1, tagRepository.findAllByTagIn(Set.of(tag)).size());
        }

        //check old tags did not delete
        for (String tag : tagsBeforeUpdate) {
            assertEquals(1, tagRepository.findAllByTagIn(Set.of(tag)).size());
        }
    }

    @Test
    @WithUserDetails("user_1")
    @DisplayName("Удаление курса по id")
    void deleteCourse() throws Exception {
        long courseId = 3;
        CourseEntity course = courseRepository
                .findById(courseId)
                .orElseThrow(() -> new RecordIsNotExistException(CourseEntity.class, courseId));

        //check course has tags
        Set<String> tagsBeforeDelete = course.getTags();
        assertFalse(course.getTags().isEmpty());

        //check user has days
        Specification<DayEntity> daysByCourse = Specification.where(CourseDaySpecifications.inCourse(Set.of(courseId)));
        assertFalse(dayRepository.findAll(daysByCourse).isEmpty());

        MvcResult result = mockMvc
                .perform(delete(CourseController.URL_UPDATE_COURSE, courseId))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        long idOfDeletedCourse = objectMapper.readValue(result.getResponse().getContentAsString(), IdDto.class).getId();
        assertEquals(courseId, idOfDeletedCourse);

        Optional<CourseEntity> deletedCourse = courseRepository.findById(courseId);
        assertTrue(deletedCourse.isEmpty());

        //check tags did not delete
        for (String tag : tagsBeforeDelete) {
            assertEquals(1, tagRepository.findAllByTagIn(Set.of(tag)).size());
        }

        //check days deleted
        assertTrue(dayRepository.findAll(daysByCourse).isEmpty());
    }
}