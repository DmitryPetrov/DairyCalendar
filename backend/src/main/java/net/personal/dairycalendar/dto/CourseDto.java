package net.personal.dairycalendar.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Set;

@Data
public class CourseDto {

    long id;
    @JsonProperty("name")
    String title;
    int position;
    String description;
    Set<String> tags;
    Set<DayDto> days;
}
