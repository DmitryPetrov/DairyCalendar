package net.personal.dairycalendar.dto.mapper;

import net.personal.dairycalendar.dto.TaskDto;
import net.personal.dairycalendar.storage.entity.TaskEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    TaskMapper INSTANCE = Mappers.getMapper(TaskMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "tagCollection", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "parent", ignore = true)
    TaskEntity toEntityWithoutTags(TaskDto dto);

    @Mapping(target = "parentId", source = "parent.id")
    TaskDto toDtoWithoutTags(TaskEntity entity);

    default TaskDto toDto(TaskEntity entity) {
        TaskDto taskDto = toDtoWithoutTags(entity);
        taskDto.addTags(entity.getTags());
        return taskDto;
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tagCollection", ignore = true)
    void updateEntity(TaskDto dto, @MappingTarget TaskEntity entity);

}
