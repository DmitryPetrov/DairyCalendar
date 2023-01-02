package net.personal.dairycalendar.dto.mapper;

import net.personal.dairycalendar.dto.DayDto;
import net.personal.dairycalendar.storage.entity.DayEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
public interface DayMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "course", ignore = true)
    DayEntity toEntity(DayDto dto);

    @Mapping(target = "courseId", source = "course.id")
    @Mapping(target = "courseName", source = "course.name")
    DayDto toDto(DayEntity dayEntity);
}
