package net.personal.dairycalendar.dto.mapper;

import net.personal.dairycalendar.dto.TaskDto;
import net.personal.dairycalendar.storage.entity.TaskEntity;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TaskMapperTest {

    @Test
    void toEntityWithoutTags() {
        TaskDto dto = new TaskDto();
        dto.setTitle("title");
        dto.setDescription("description");
        dto.setPosition(3);
        dto.setPriority(4);
        dto.setDone(true);
        dto.setFinishedAt(LocalDateTime.of(2023, 3, 28, 11, 36, 13));
        dto.setParentId(1233L);
        dto.setTags(Set.of("tag_1", "tag_2"));

        TaskEntity entity = TaskMapper.INSTANCE.toEntityWithoutTags(dto);
        assertEquals(dto.getTitle(), entity.getTitle());
        assertEquals(dto.getDescription(), entity.getDescription());
        assertEquals(dto.getPosition(), entity.getPosition());
        assertEquals(dto.getPriority(), entity.getPriority());
        assertEquals(dto.isDone(), entity.isDone());
        assertEquals(dto.getFinishedAt(), entity.getFinishedAt());
        assertNull(entity.getUser());
        assertNull(entity.getParent());
        assertTrue(entity.getTagCollection().getTags().isEmpty());
    }

    @Test
    void updateEntity() {
        TaskDto dto = new TaskDto();
        dto.setTitle("title");
        dto.setDescription("description");
        dto.setPosition(3);
        dto.setPriority(4);
        dto.setDone(true);
        dto.setFinishedAt(LocalDateTime.of(2023, 3, 28, 11, 36, 13));
        dto.setParentId(1233L);
        dto.setTags(Set.of("tag_1", "tag_2"));
        TaskEntity entity = new TaskEntity();

        TaskMapper.INSTANCE.updateEntity(dto, entity);

        assertEquals(dto.getTitle(), entity.getTitle());
        assertEquals(dto.getDescription(), entity.getDescription());
        assertEquals(dto.getPosition(), entity.getPosition());
        assertEquals(dto.getPriority(), entity.getPriority());
        assertEquals(dto.isDone(), entity.isDone());
        assertNull(entity.getFinishedAt());
        assertNull(entity.getUser());
        assertNull(entity.getParent());
        assertTrue(entity.getTagCollection().getTags().isEmpty());
    }
}