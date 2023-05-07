package net.personal.dairycalendar.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.personal.dairycalendar.AbstractTest;
import net.personal.dairycalendar.dto.IdDto;
import net.personal.dairycalendar.dto.TaskDto;
import net.personal.dairycalendar.exception.RecordIsNotExistException;
import net.personal.dairycalendar.storage.entity.CourseEntity;
import net.personal.dairycalendar.storage.entity.TaskEntity;
import net.personal.dairycalendar.storage.repository.TagRepository;
import net.personal.dairycalendar.storage.repository.TaskRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.RepetitionInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Test CRUD endpoints for tasks")
class TaskController_IntegrationTest extends AbstractTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private TagRepository tagRepository;

    @Test
    @WithUserDetails(USER_1_USERNAME)
    @DisplayName("Add new root task")
    void addRootTask() throws Exception {
        TaskDto payload = new TaskDto();
        payload.setTitle("title");
        payload.setDescription("description");
        payload.setPosition(1);
        payload.setPriority(1);
        payload.setDone(true);
        payload.setFinishedAt(LocalDateTime.of(2023, 3, 28, 11, 36, 13));
        payload.setParentId(null);
        payload.setTags(Set.of(TAG_1_TITLE, TAG_2_TITLE));

        MvcResult result = mockMvc
                .perform(
                        post(TaskController.URL_ADD_NEW_TASK)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(payload))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        long newTaskId = objectMapper.readValue(result.getResponse().getContentAsString(), IdDto.class).getId();

        TaskEntity entity = taskRepository
                .findById(newTaskId)
                .orElseThrow(() -> new RecordIsNotExistException(TaskEntity.class, newTaskId));

        assertEquals(USER_1_USERNAME, entity.getUser().getUsername(), "Task was not link to user");
        assertEquals(payload.getTitle(), entity.getTitle(), "Task title wrong");
        assertEquals(payload.getDescription(), entity.getDescription(),"Task description wrong");
        assertEquals(payload.getPosition(), entity.getPosition(), "Task position wrong");
        assertEquals(payload.getPriority(), entity.getPriority(), "Task priority wrong");
        assertEquals(payload.isDone(), entity.isDone(), "Task done flag wrong");
        assertNull(entity.getParent(), "Task parent wrong");
        assertEquals(payload.getTags(), entity.getTags(), "Task tags wrong");
        for (String tag : payload.getTags()) {
            assertEquals(1, tagRepository.findAllByTagIn(Set.of(tag)).size(),
                         "New tags not saved or saved duplicates for old tags");
        }
    }

    @RepeatedTest(3)
    @WithUserDetails(USER_1_USERNAME)
    @DisplayName("Add new child task")
    void addChildTask(RepetitionInfo repetitionInfo) throws Exception {
        TaskEntity task = saveTask(USER_1_USERNAME, Set.of(TAG_1_TITLE, TAG_2_TITLE), false, null, null);
        TaskDto payload = new TaskDto();
        payload.setTitle("title " + repetitionInfo.getCurrentRepetition());
        payload.setDescription("description");
        payload.setPosition(repetitionInfo.getCurrentRepetition());
        payload.setPriority(repetitionInfo.getCurrentRepetition());
        payload.setDone(true);
        payload.setFinishedAt(LocalDateTime.of(2023, 3, 28, 11, 36, 13));
        payload.setParentId(task.getId());
        payload.setTags(Set.of(TAG_1_TITLE, TAG_2_TITLE));

        MvcResult result = mockMvc
                .perform(
                        post(TaskController.URL_ADD_NEW_TASK)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(payload))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        long newTaskId = objectMapper.readValue(result.getResponse().getContentAsString(), IdDto.class).getId();

        TaskEntity entity = taskRepository
                .findById(newTaskId)
                .orElseThrow(() -> new RecordIsNotExistException(TaskEntity.class, newTaskId));

        assertEquals(USER_1_USERNAME, entity.getUser().getUsername(), "Task was not link to user");
        assertEquals(payload.getTitle(), entity.getTitle(), "Task title wrong");
        assertEquals(payload.getDescription(), entity.getDescription(),"Task description wrong");
        assertEquals(payload.getPosition(), entity.getPosition(), "Task position wrong");
        assertEquals(payload.getPriority(), entity.getPriority(), "Task priority wrong");
        assertEquals(payload.isDone(), entity.isDone(), "Task done flag wrong");
        assertEquals(task.getId(), entity.getParent().getId(),"Task parent wrong");
        assertEquals(payload.getTags(), entity.getTags(), "Task tags wrong");
        for (String tag : payload.getTags()) {
            assertEquals(1, tagRepository.findAllByTagIn(Set.of(tag)).size(),
                         "New tags not saved or saved duplicates for old tags");
        }
    }

    @Test
    @WithUserDetails(USER_1_USERNAME)
    @DisplayName("Update task")
    void updateTask() throws Exception {
        TaskEntity parent = saveTask(USER_1_USERNAME, Set.of(TAG_1_TITLE, TAG_2_TITLE), false, null, null);
        TaskEntity task = saveTask(USER_1_USERNAME, Set.of(TAG_1_TITLE, TAG_2_TITLE), false, null, parent);
        TaskDto payload = new TaskDto();
        payload.setTitle("title update");
        payload.setDescription("description update");
        payload.setPosition(111);
        payload.setPriority(111);
        payload.setDone(true);
        payload.setFinishedAt(null);
        payload.setParentId(parent.getId());
        payload.setTags(Set.of(TAG_2_TITLE, TAG_3_TITLE));
        Set<String> tagsBeforeUpdate = task.getTags();

        MvcResult result = mockMvc
                .perform(
                        put(TaskController.URL_UPDATE_TASK, task.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(payload))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        long idOfUpdatedTask = objectMapper.readValue(result.getResponse().getContentAsString(), IdDto.class).getId();
        assertEquals(task.getId(), idOfUpdatedTask);

        TaskEntity entity = taskRepository
                .findById(task.getId())
                .orElseThrow(() -> new RecordIsNotExistException(CourseEntity.class, task.getId()));

        assertEquals(USER_1_USERNAME, entity.getUser().getUsername(), "Task was not link to user");
        assertEquals(payload.getTitle(), entity.getTitle(), "Task title wrong");
        assertEquals(payload.getDescription(), entity.getDescription(),"Task description wrong");
        assertEquals(payload.getPosition(), entity.getPosition(), "Task position wrong");
        assertEquals(payload.getPriority(), entity.getPriority(), "Task priority wrong");
        assertEquals(payload.isDone(), entity.isDone(), "Task done flag wrong");
        assertNotNull(entity.getFinishedAt(), "Task not finished");
        assertEquals(entity.getParent().getId(), parent.getId(), "Task parent wrong");
        assertEquals(payload.getTags(), entity.getTags(), "Task tags wrong");
        for (String tag : payload.getTags()) {
            assertEquals(1, tagRepository.findAllByTagIn(Set.of(tag)).size(),
                         "New tags not saved or saved duplicates for old tags");
        }
        for (String tag : tagsBeforeUpdate) {
            assertEquals(1, tagRepository.findAllByTagIn(Set.of(tag)).size(), "Tag [" + tag + "] deleted");
        }
    }

    @Test
    @WithUserDetails(USER_1_USERNAME)
    @DisplayName("Close task")
    void closeTask() throws Exception {
        TaskEntity task = saveTask(USER_1_USERNAME, Set.of(TAG_1_TITLE, TAG_2_TITLE), false, null, null);

        MvcResult result = mockMvc
                .perform(put(TaskController.URL_CLOSE_TASK, task.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        long idOfUpdatedTask = objectMapper.readValue(result.getResponse().getContentAsString(), IdDto.class).getId();
        assertEquals(task.getId(), idOfUpdatedTask);

        TaskEntity entity = taskRepository
                .findById(task.getId())
                .orElseThrow(() -> new RecordIsNotExistException(CourseEntity.class, task.getId()));

        assertNotNull(entity.getFinishedAt(), "Task not finished");
    }

    @Test
    @WithUserDetails(USER_1_USERNAME)
    @DisplayName("Remove task")
    void deleteTask() throws Exception {
        TaskEntity task = saveTask(USER_1_USERNAME, Set.of(TAG_1_TITLE, TAG_2_TITLE), false, null, null);

        Set<String> tagsBeforeDelete = task.getTags();
        assertFalse(task.getTags().isEmpty(), "Before delete task has tags");

        MvcResult result = mockMvc
                .perform(delete(TaskController.URL_DELETE_TASK, task.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        long idOfDeletedTask = objectMapper.readValue(result.getResponse().getContentAsString(), IdDto.class).getId();
        assertEquals(task.getId(), idOfDeletedTask, "Task we wanted delete and deleted are same task");
        Optional<TaskEntity> taskEntity = taskRepository.findById(task.getId());
        assertTrue(taskEntity.isEmpty(), "DB does not contains deleted task");
        for (String tag : tagsBeforeDelete) {
            assertEquals(1, tagRepository.findAllByTagIn(Set.of(tag)).size(), "Tag [" + tag + "] was deleted");
        }
    }

    @Test
    @WithUserDetails(USER_1_USERNAME)
    @DisplayName("Get task")
    void getTask() throws Exception {
        TaskEntity root = saveTask(USER_1_USERNAME, Set.of(TAG_1_TITLE, TAG_2_TITLE), false, null, null);
        TaskEntity task = saveTask(USER_1_USERNAME, Set.of(TAG_1_TITLE, TAG_2_TITLE, TAG_3_TITLE), false, null, root);
        TaskEntity child1 = saveTask(USER_1_USERNAME, Set.of(TAG_4_TITLE, TAG_2_TITLE, TAG_3_TITLE), false, null, task);
        TaskEntity child2 = saveTask(USER_1_USERNAME, Set.of(TAG_5_TITLE, TAG_2_TITLE, TAG_3_TITLE), false, null, task);

        MvcResult result = mockMvc
                .perform(get(TaskController.URL_GET_TASK, task.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        TaskDto payload = objectMapper.readValue(result.getResponse().getContentAsString(), TaskDto.class);

        assertEquals(payload.getId(), task.getId(), "Task id wrong");
        assertEquals(payload.getDescription(), task.getDescription(),"Task description wrong");
        assertEquals(payload.getTags(), task.getTags(), "Task tags wrong");
        assertEquals(payload.getParent().getId(), root.getId(),"Parent task wrong");
        assertTrue(payload.getChildren().stream().anyMatch(child -> child.getId() == child1.getId()),
                   "Child task1 absent");
        assertTrue(payload.getChildren().stream().anyMatch(child -> child.getId() == child2.getId()),
                   "Child task2 absent");
    }
}