package net.personal.dairycalendar.dto.mapper;

import net.personal.dairycalendar.dto.CourseDto;
import net.personal.dairycalendar.storage.entity.CourseEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CourseMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "tagCollection", ignore = true)
    @Mapping(target = "days", ignore = true)
    @Mapping(target = "user", ignore = true)
    CourseEntity toEntity(CourseDto dto);

    @Mapping(target = "days", ignore = true)
    @Mapping(target = "tags", ignore = true)
    CourseDto toDto(CourseEntity entity);

}
