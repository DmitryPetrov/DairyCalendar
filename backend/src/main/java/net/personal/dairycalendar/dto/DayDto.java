package net.personal.dairycalendar.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class DayDto {

    long courseId;
    String courseName;
    LocalDate date;
    int assessment;
}
