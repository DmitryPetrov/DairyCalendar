package net.personal.dairycalendar.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDate;

@Data
public class DayDto {

    long courseId;
    @JsonProperty("courseName")
    String courseTitle;
    LocalDate date;
    int assessment;
}
