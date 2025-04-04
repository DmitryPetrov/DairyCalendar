package net.personal.dairycalendar.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DayDescriptionDto {

    private LocalDate date = LocalDate.now();
    private String description;
}
