package net.personal.dairycalendar.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
public class CourseDto {

    long id;
    @JsonProperty("name")
    String title;
    int position;
    String description;
    Set<String> tags;
    Set<DayDto> days;
    boolean paused;

    public CourseDto(String title, int position, String description, Set<String> tags) {
        this.title = title;
        this.position = position;
        this.description = description;
        this.tags = tags;
    }

    public CourseDto(String title, int position, String description, Set<String> tags, boolean paused) {
        this.title = title;
        this.position = position;
        this.description = description;
        this.tags = tags;
        this.paused = paused;
    }
}
