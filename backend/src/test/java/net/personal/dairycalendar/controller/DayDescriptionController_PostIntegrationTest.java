package net.personal.dairycalendar.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.personal.dairycalendar.AbstractTest;
import net.personal.dairycalendar.dto.DayDescriptionDto;
import net.personal.dairycalendar.dto.DayInformationDto;
import net.personal.dairycalendar.storage.entity.DayDescription;
import net.personal.dairycalendar.storage.repository.DayDescriptionRepository;
import net.personal.dairycalendar.storage.specification.DayDescriptionSpecifications;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.RepetitionInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Test get list of courses")
class DayDescriptionController_PostIntegrationTest extends AbstractTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private DayDescriptionRepository dayDescriptionRepository;

    @RepeatedTest(3)
    @WithUserDetails(USER_1_USERNAME)
    @DisplayName("Add new day description")
    void addDayDescription(RepetitionInfo repetitionInfo) throws Exception {
        LocalDate date = LocalDate.of(2025, 3, repetitionInfo.getCurrentRepetition());
        String descriptionText = "description " + repetitionInfo.getCurrentRepetition();
        DayDescriptionDto dayDescriptionDto = new DayDescriptionDto(date, descriptionText);
        DayInformationDto requestPayload = new DayInformationDto(List.of(), List.of(dayDescriptionDto));
        mockMvc
                .perform(
                        post(DayController.URL_SAVE_DAY_INFO)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestPayload))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        Specification<DayDescription> specification = Specification
                .where(DayDescriptionSpecifications.byUser(loadUser(USER_1_USERNAME).getId())
                .and(DayDescriptionSpecifications.inPeriod(LocalDate.of(2025, 3, 1), date)));
        DayDescription dayDescription = dayDescriptionRepository.findAll(specification)
                .stream()
                .filter(description -> Objects.equals(description.getDate(), date))
                .findFirst()
                .orElseThrow();

        assertEquals(dayDescription.getDescription(), descriptionText, "Description text wrong");
    }

    DayDescriptionDto getDayDescriptionDto(int i, int j) {
        LocalDate date = getDate(i, j);
        String descriptionText = getDescription(i, j);
        return new DayDescriptionDto(date, descriptionText);
    }

    LocalDate getDate(int i, int j) {
        return LocalDate.of(2025, 2, i + j);
    }

    String getDescription(int i, int j) {
        return "description " + i + j;
    }

    @RepeatedTest(3)
    @WithUserDetails(USER_1_USERNAME)
    @DisplayName("Add several day descriptions")
    void addDayDescriptions(RepetitionInfo rInfo) throws Exception {
        List<Integer> steps = List.of(0, 3, 6);
        List<DayDescriptionDto> descriptionDtoList = steps.stream()
                .map(j -> this.getDayDescriptionDto(rInfo.getCurrentRepetition(), j))
                .collect(Collectors.toList());
        DayInformationDto requestPayload = new DayInformationDto(List.of(), descriptionDtoList);
        mockMvc
                .perform(
                        post(DayController.URL_SAVE_DAY_INFO)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestPayload))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        LocalDate from = LocalDate.of(2025, 2, 1);
        LocalDate to = LocalDate.of(2025, 2, 28);
        Specification<DayDescription> specification = Specification
                .where(DayDescriptionSpecifications.byUser(loadUser(USER_1_USERNAME).getId())
                .and(DayDescriptionSpecifications.inPeriod(from, to)));
        List<DayDescription> all = dayDescriptionRepository.findAll(specification);

        for (Integer j : steps) {
            String descriptionText = all.stream()
                    .filter(entity -> Objects.equals(getDate(rInfo.getCurrentRepetition(), j), entity.getDate()))
                    .findFirst()
                    .orElseThrow()
                    .getDescription();
            assertEquals(descriptionText, getDescription(rInfo.getCurrentRepetition(), j));
        }
    }
}