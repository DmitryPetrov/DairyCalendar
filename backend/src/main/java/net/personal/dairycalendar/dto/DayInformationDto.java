package net.personal.dairycalendar.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DayInformationDto {

    private List<DayDto> activities = new ArrayList<>();
    private List<DayDescriptionDto> descriptions = new ArrayList<>();
}
