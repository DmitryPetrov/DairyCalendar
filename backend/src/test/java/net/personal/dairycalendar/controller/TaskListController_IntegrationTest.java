package net.personal.dairycalendar.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import net.personal.dairycalendar.AbstractTest;
import net.personal.dairycalendar.dto.TaskDto;
import net.personal.dairycalendar.storage.entity.TaskEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Test list of tasks")
class TaskListController_IntegrationTest extends AbstractTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    private TaskEntity task1;
    private TaskEntity task2;
    private TaskEntity task3;
    private TaskEntity task4;
    private TaskEntity task5;

    @BeforeEach
    void addTasksInDataBase() {
        task1 = saveTask("123", USER_1_USERNAME, Set.of(TAG_4_TITLE, TAG_3_TITLE), true, LocalDateTime.of(2023, 3, 28, 11, 36, 13), null);
        task2 = saveTask("123qwe", USER_1_USERNAME, Set.of(TAG_3_TITLE, TAG_5_TITLE), true, LocalDateTime.of(2023, 3, 28, 11, 36, 13), null);
        task3 = saveTask("asd123", USER_1_USERNAME, Set.of(TAG_1_TITLE), false, LocalDateTime.of(2023, 3, 28, 11, 36, 13), null);
        task4 = saveTask("zxc1_23", USER_1_USERNAME, Set.of(TAG_2_TITLE), false, LocalDateTime.of(2023, 3, 28, 11, 36, 13), null);
        task5 = saveTask("777_12_3", USER_1_USERNAME, Set.of(TAG_3_TITLE), false, null, null);
    }

    @Test
    @WithUserDetails(USER_1_USERNAME)
    @DisplayName("Find task by tags")
    void getTasksByTags() throws Exception {
        MvcResult result = mockMvc
                .perform(get(TaskController.URL_GET_ALL_TASKS).param("tags", TAG_3_TITLE))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        CollectionType listType = objectMapper
                .getTypeFactory()
                .constructCollectionType(List.class, TaskDto.class);
        List<TaskDto> payload = objectMapper.readValue(result.getResponse().getContentAsString(), listType);
        Set<Long> taskIdSet = payload.stream()
                .map(TaskDto::getId)
                .collect(Collectors.toSet());

        assertTrue(taskIdSet.contains(task1.getId()), "Response does not contains task1");
        assertTrue(taskIdSet.contains(task2.getId()), "Response does not contains task2");
        assertTrue(taskIdSet.contains(task5.getId()), "Response does not contains task5");
        assertFalse(taskIdSet.contains(task3.getId()), "Response contains task3");
        assertFalse(taskIdSet.contains(task4.getId()), "Response contains task4");
    }

    @Test
    @WithUserDetails(USER_1_USERNAME)
    @DisplayName("Find task by no tags")
    void getTasksByNoTags() throws Exception {
        MvcResult result = mockMvc
                .perform(get(TaskController.URL_GET_ALL_TASKS).param("noTags", TAG_3_TITLE))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        CollectionType listType = objectMapper
                .getTypeFactory()
                .constructCollectionType(List.class, TaskDto.class);
        List<TaskDto> payload = objectMapper.readValue(result.getResponse().getContentAsString(), listType);
        Set<Long> taskIdSet = payload.stream()
                .map(TaskDto::getId)
                .collect(Collectors.toSet());

        assertFalse(taskIdSet.contains(task1.getId()), "Response contains task1");
        assertFalse(taskIdSet.contains(task2.getId()), "Response contains task2");
        assertFalse(taskIdSet.contains(task5.getId()), "Response contains task5");
        assertTrue(taskIdSet.contains(task3.getId()), "Response does not contains task3");
        assertTrue(taskIdSet.contains(task4.getId()), "Response does not contains task4");
    }

    @Test
    @WithUserDetails(USER_1_USERNAME)
    @DisplayName("Find completed tasks")
    void getCompletedTasks() throws Exception {
        MvcResult result = mockMvc
                .perform(get(TaskController.URL_GET_ALL_TASKS).param("done", Boolean.TRUE.toString()))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        CollectionType listType = objectMapper
                .getTypeFactory()
                .constructCollectionType(List.class, TaskDto.class);
        List<TaskDto> payload = objectMapper.readValue(result.getResponse().getContentAsString(), listType);
        Set<Long> taskIdSet = payload.stream()
                .map(TaskDto::getId)
                .collect(Collectors.toSet());

        assertTrue(taskIdSet.contains(task1.getId()), "Response does not contains task1");
        assertTrue(taskIdSet.contains(task2.getId()), "Response does not contains task2");
        assertFalse(taskIdSet.contains(task3.getId()), "Response contains task3");
        assertFalse(taskIdSet.contains(task4.getId()), "Response contains task4");
        assertFalse(taskIdSet.contains(task5.getId()), "Response contains task5");
    }

    @Test
    @WithUserDetails(USER_1_USERNAME)
    @DisplayName("Find not completed tasks")
    void getNotCompletedTasks() throws Exception {
        MvcResult result = mockMvc
                .perform(get(TaskController.URL_GET_ALL_TASKS).param("done", Boolean.FALSE.toString()))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        CollectionType listType = objectMapper
                .getTypeFactory()
                .constructCollectionType(List.class, TaskDto.class);
        List<TaskDto> payload = objectMapper.readValue(result.getResponse().getContentAsString(), listType);
        Set<Long> taskIdSet = payload.stream()
                .map(TaskDto::getId)
                .collect(Collectors.toSet());

        assertFalse(taskIdSet.contains(task1.getId()), "Response contains task1");
        assertFalse(taskIdSet.contains(task2.getId()), "Response contains task2");
        assertTrue(taskIdSet.contains(task3.getId()), "Response does not contains task3");
        assertTrue(taskIdSet.contains(task4.getId()), "Response does not contains task4");
        assertTrue(taskIdSet.contains(task5.getId()), "Response does not contains task5");
    }

    @Test
    @WithUserDetails(USER_1_USERNAME)
    @DisplayName("Find closed tasks")
    void getClosedTasks() throws Exception {
        MvcResult result = mockMvc
                .perform(get(TaskController.URL_GET_ALL_TASKS).param("closed", Boolean.TRUE.toString()))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        CollectionType listType = objectMapper
                .getTypeFactory()
                .constructCollectionType(List.class, TaskDto.class);
        List<TaskDto> payload = objectMapper.readValue(result.getResponse().getContentAsString(), listType);
        Set<Long> taskIdSet = payload.stream()
                .map(TaskDto::getId)
                .collect(Collectors.toSet());

        assertTrue(taskIdSet.contains(task1.getId()), "Response does not contains task1");
        assertTrue(taskIdSet.contains(task2.getId()), "Response does not contains task2");
        assertTrue(taskIdSet.contains(task3.getId()), "Response does not contains task3");
        assertTrue(taskIdSet.contains(task4.getId()), "Response does not contains task4");
        assertFalse(taskIdSet.contains(task5.getId()), "Response contains task5");
    }

    @Test
    @WithUserDetails(USER_1_USERNAME)
    @DisplayName("Find not closed tasks")
    void getNotClosedTasks() throws Exception {
        MvcResult result = mockMvc
                .perform(get(TaskController.URL_GET_ALL_TASKS).param("closed", Boolean.FALSE.toString()))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        CollectionType listType = objectMapper
                .getTypeFactory()
                .constructCollectionType(List.class, TaskDto.class);
        List<TaskDto> payload = objectMapper.readValue(result.getResponse().getContentAsString(), listType);
        Set<Long> taskIdSet = payload.stream()
                .map(TaskDto::getId)
                .collect(Collectors.toSet());

        assertFalse(taskIdSet.contains(task1.getId()), "Response contains task1");
        assertFalse(taskIdSet.contains(task2.getId()), "Response contains task2");
        assertFalse(taskIdSet.contains(task3.getId()), "Response contains task3");
        assertFalse(taskIdSet.contains(task4.getId()), "Response contains task4");
        assertTrue(taskIdSet.contains(task5.getId()), "Response does not contains task5");
    }

    @Test
    @WithUserDetails(USER_1_USERNAME)
    @DisplayName("Find tasks by title")
    void getTasksByTitle() throws Exception {
        MvcResult result = mockMvc
                .perform(get(TaskController.URL_GET_ALL_TASKS).param("title", "23"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        CollectionType listType = objectMapper
                .getTypeFactory()
                .constructCollectionType(List.class, TaskDto.class);
        List<TaskDto> payload = objectMapper.readValue(result.getResponse().getContentAsString(), listType);
        Set<Long> taskIdSet = payload.stream()
                .map(TaskDto::getId)
                .collect(Collectors.toSet());

        assertTrue(taskIdSet.contains(task1.getId()), "Response does not contains task1");
        assertTrue(taskIdSet.contains(task2.getId()), "Response does not contains task2");
        assertTrue(taskIdSet.contains(task3.getId()), "Response does not contains task3");
        assertTrue(taskIdSet.contains(task4.getId()), "Response does not contains task4");
        assertFalse(taskIdSet.contains(task5.getId()), "Response contains task5");
    }

    @Test
    @WithUserDetails(USER_1_USERNAME)
    @DisplayName("Find tasks by multiply filters")
    void getTasksByMultiplyFilters() throws Exception {
        MvcResult result = mockMvc
                .perform(get(TaskController.URL_GET_ALL_TASKS)
                                 .param("tags", TAG_3_TITLE)
                                 .param("noTags", TAG_4_TITLE)
                                 .param("title", "77")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        CollectionType listType = objectMapper
                .getTypeFactory()
                .constructCollectionType(List.class, TaskDto.class);
        List<TaskDto> payload = objectMapper.readValue(result.getResponse().getContentAsString(), listType);
        Set<Long> taskIdSet = payload.stream()
                .map(TaskDto::getId)
                .collect(Collectors.toSet());

        assertFalse(taskIdSet.contains(task1.getId()), "Response contains task1");
        assertFalse(taskIdSet.contains(task2.getId()), "Response contains task2");
        assertFalse(taskIdSet.contains(task3.getId()), "Response contains task3");
        assertFalse(taskIdSet.contains(task4.getId()), "Response contains task4");
        assertTrue(taskIdSet.contains(task5.getId()), "Response does not contains task5");
    }

    @Test
    @WithUserDetails(USER_1_USERNAME)
    @DisplayName("Find tasks without filters")
    void getTasksWithoutFilters() throws Exception {
        MvcResult result = mockMvc
                .perform(get(TaskController.URL_GET_ALL_TASKS))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        CollectionType listType = objectMapper
                .getTypeFactory()
                .constructCollectionType(List.class, TaskDto.class);
        List<TaskDto> payload = objectMapper.readValue(result.getResponse().getContentAsString(), listType);
        Set<Long> taskIdSet = payload.stream()
                .map(TaskDto::getId)
                .collect(Collectors.toSet());

        assertTrue(taskIdSet.contains(task1.getId()), "Response does not contains task1");
        assertTrue(taskIdSet.contains(task2.getId()), "Response does not contains task2");
        assertTrue(taskIdSet.contains(task3.getId()), "Response does not contains task3");
        assertTrue(taskIdSet.contains(task4.getId()), "Response does not contains task4");
        assertTrue(taskIdSet.contains(task5.getId()), "Response does not contains task5");
    }

}