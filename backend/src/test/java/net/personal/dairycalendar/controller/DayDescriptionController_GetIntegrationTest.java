package net.personal.dairycalendar.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.personal.dairycalendar.AbstractTest;
import net.personal.dairycalendar.dto.CoursesDto;
import net.personal.dairycalendar.dto.DayDescriptionDto;
import net.personal.dairycalendar.storage.entity.DayDescription;
import net.personal.dairycalendar.storage.repository.DayDescriptionRepository;
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
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Test get list of courses")
class DayDescriptionController_GetIntegrationTest extends AbstractTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private DayDescriptionRepository dayDescriptionRepository;

    private DayDescription dayDescription1;
    private DayDescription dayDescription2;
    private DayDescription dayDescription3;
    private DayDescription dayDescription4;
    private DayDescription dayDescription5;
    @BeforeEach
    void addCoursesToDataBase() {
        dayDescription1 = saveDayDescription(USER_1_USERNAME, LocalDate.of(2025, 4, 1), "description 1");
        dayDescription2 = saveDayDescription(USER_1_USERNAME, LocalDate.of(2025, 4, 2), "description 2");
        dayDescription3 = saveDayDescription(USER_1_USERNAME, LocalDate.of(2025, 4, 3), "description 3");
        dayDescription4 = saveDayDescription(USER_1_USERNAME, LocalDate.of(2025, 4, 4), "description 4");
        dayDescription5 = saveDayDescription(USER_1_USERNAME, LocalDate.of(2025, 4, 5), "description 5");
    }

    @Test
    @WithUserDetails(USER_1_USERNAME)
    @DisplayName("Get days descriptions by dates")
    void getDayDescriptions() throws Exception {
        MvcResult result = mockMvc
                .perform(get(CourseController.URL_GET_COURSE_LIST)
                                 .param("fromDate", dayDescription1.getDate().toString())
                                 .param("toDate", dayDescription5.getDate().toString()))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        CoursesDto payload = objectMapper.readValue(result.getResponse().getContentAsString(), CoursesDto.class);

        assertEquals(dayDescription1.getDate(), payload.getFromDate());
        assertEquals(dayDescription5.getDate(), payload.getToDate());
        assertTrue(payload.getDescriptions().size() == 5, "Descriptions count wrong");
        assertEquals(fingDayDescriptionByDate(payload, dayDescription1.getDate()), dayDescription1.getDescription(), "Wrong description");
        assertEquals(fingDayDescriptionByDate(payload, dayDescription2.getDate()), dayDescription2.getDescription(), "Wrong description");
        assertEquals(fingDayDescriptionByDate(payload, dayDescription3.getDate()), dayDescription3.getDescription(), "Wrong description");
        assertEquals(fingDayDescriptionByDate(payload, dayDescription4.getDate()), dayDescription4.getDescription(), "Wrong description");
        assertEquals(fingDayDescriptionByDate(payload, dayDescription5.getDate()), dayDescription5.getDescription(), "Wrong description");
    }

    String fingDayDescriptionByDate(CoursesDto payload, LocalDate date) {
        return payload.getDescriptions()
                .stream()
                .filter(dto -> Objects.equals(dto.getDate(), date))
                .findFirst()
                .map(DayDescriptionDto::getDescription)
                .orElseThrow();
    }

}