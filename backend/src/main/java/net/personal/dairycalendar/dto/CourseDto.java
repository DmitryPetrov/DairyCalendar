package net.personal.dairycalendar.dto;

import lombok.Data;

import java.util.Set;

@Data
public class CourseDto {

    long id;
    String name;
    String description;
    Set<String> tags;
    Set<DayDto> days;
}
