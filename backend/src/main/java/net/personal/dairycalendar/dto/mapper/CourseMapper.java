package net.personal.dairycalendar.dto.mapper;

import net.personal.dairycalendar.dto.CourseDto;
import net.personal.dairycalendar.dto.DayDto;
import net.personal.dairycalendar.storage.entity.CourseEntity;
import net.personal.dairycalendar.storage.entity.DayEntity;
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

    CourseDto toDto(CourseEntity entity);

    @Mapping(target = "courseId", source = "course.id")
    @Mapping(target = "courseTitle", source = "course.title")
    DayDto toDto(DayEntity dayEntity);
}
