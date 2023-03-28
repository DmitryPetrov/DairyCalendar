package net.personal.dairycalendar.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.personal.dairycalendar.dto.TaskDto;
import net.personal.dairycalendar.dto.mapper.TaskMapper;
import net.personal.dairycalendar.exception.RecordIsNotExistException;
import net.personal.dairycalendar.storage.entity.AppUserEntity;
import net.personal.dairycalendar.storage.entity.TaskEntity;
import net.personal.dairycalendar.storage.repository.AppUserRepository;
import net.personal.dairycalendar.storage.repository.TaskRepository;
import net.personal.dairycalendar.storage.specification.TaskSpecifications;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final AuthenticationService authenticationService;
    private final TagService tagService;
    private final TaskMapper taskMapper;
    private final AppUserRepository appUserRepository;

    @Transactional(readOnly = true)
    public List<TaskDto> getTasksForCurrentUser(Set<String> tags) {
        return taskRepository
                .findAll(Specification.where(TaskSpecifications.byUser(authenticationService.getCurrentUser().getId())))
                .stream()
                .map(taskMapper::toDto)
                .collect(Collectors.toList());
    }

    public TaskDto getTask(long id) {
        //todo check access
        return taskRepository
                .findById(id)
                .map(taskMapper::toDto)
                .orElseThrow(() -> new RecordIsNotExistException(TaskEntity.class, id));
    }

    @Transactional
    public long addTask(TaskDto dto) {
        TaskEntity taskEntity = taskMapper.toEntityWithoutTags(dto);
        tagService.updateTagCollection(taskEntity, dto.getTags());
        long currentUserId = authenticationService.getCurrentUser().getId();
        if (Objects.nonNull(dto.getParentId())) {
            TaskEntity parent = taskRepository.findById(dto.getParentId())
                    .orElseThrow(() -> new RecordIsNotExistException(TaskEntity.class, dto.getParentId()));
            taskEntity.setParent(parent);
        }
        taskEntity.setUser(appUserRepository
                               .findById(currentUserId)
                               .orElseThrow(() -> new RecordIsNotExistException(AppUserEntity.class, currentUserId)));
        return taskRepository.save(taskEntity).getId();
    }

    @Transactional
    public void updateTask(long id, TaskDto dto) {
        TaskEntity entity = taskRepository
                .findById(id)
                .orElseThrow(() -> new RecordIsNotExistException(TaskEntity.class, id));
        taskMapper.updateEntity(dto, entity);
        tagService.updateTagCollection(entity, dto.getTags());
        taskRepository.save(entity);
    }

    public void deleteTask(long id) {
        taskRepository.deleteById(id);
    }
}
