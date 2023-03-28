package net.personal.dairycalendar.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.personal.dairycalendar.dto.IdDto;
import net.personal.dairycalendar.dto.TaskDto;
import net.personal.dairycalendar.service.TaskService;
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

import java.util.List;
import java.util.Set;

@Slf4j
@RestController
@RequiredArgsConstructor
public class TaskController {
    private final TaskService service;
    private static final String BASE_URL = "/api/task";

    public static final String URL_GET_ALL_TASKS = BASE_URL;
    @GetMapping(URL_GET_ALL_TASKS)
    public ResponseEntity<List<TaskDto>> getUsersTasks(@RequestParam(required = false) Set<String> tags) {
        if (tags == null) {
            tags = Set.of();
        }
        List<TaskDto> tasks = service.getTasksForCurrentUser(tags);
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(tasks);
    }

    public static final String URL_ADD_NEW_TASK = BASE_URL;
    @PostMapping(URL_ADD_NEW_TASK)
    public ResponseEntity<IdDto> add(@RequestBody TaskDto payload) {
        long id = service.addTask(payload);
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new IdDto(id));
    }

    public static final String URL_GET_TASK = BASE_URL + "/{id}";
    @GetMapping(URL_GET_TASK)
    public ResponseEntity<TaskDto> getCourse(@PathVariable long id) {
        TaskDto task = service.getTask(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(task);
    }

    public static final String URL_UPDATE_TASK = BASE_URL + "/{id}";
    @PutMapping(URL_UPDATE_TASK)
    public ResponseEntity<IdDto> updateCourse(@PathVariable long id, @RequestBody TaskDto dto) {
        service.updateTask(id, dto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new IdDto(id));
    }

    public static final String URL_DELETE_TASK = BASE_URL + "/{id}";
    @DeleteMapping(URL_DELETE_TASK)
    public ResponseEntity<IdDto> deleteCourse(@PathVariable long id) {
        service.deleteTask(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new IdDto(id));
    }

}
