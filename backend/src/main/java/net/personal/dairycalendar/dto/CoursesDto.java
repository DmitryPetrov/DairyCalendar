package net.personal.dairycalendar.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CoursesDto {

    private List<CourseDto> courses = List.of();
    private List<DayDescriptionDto> descriptions = List.of();
    private LocalDate fromDate;
    private LocalDate toDate;

}
