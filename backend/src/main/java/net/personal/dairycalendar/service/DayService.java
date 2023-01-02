package net.personal.dairycalendar.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.personal.dairycalendar.dto.DayDto;
import net.personal.dairycalendar.dto.mapper.DayMapper;
import net.personal.dairycalendar.exception.RecordIsNotExistException;
import net.personal.dairycalendar.storage.entity.CourseEntity;
import net.personal.dairycalendar.storage.entity.DayEntity;
import net.personal.dairycalendar.storage.repository.DayRepository;
import net.personal.dairycalendar.storage.repository.CourseRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class DayService {

    private final DayMapper dayMapper;
    private final CourseRepository courseRepository;
    private final DayRepository dayRepository;

    @Transactional
    //TODO перезаписывать дни с одной датой и курсом
    public void save(List<DayDto> dayDtos) {
        Set<Long> coursesId = dayDtos
                .stream()
                .map(DayDto::getCourseId)
                .collect(Collectors.toSet());
        List<CourseEntity> courseEntities = courseRepository.findAllById(coursesId);

        List<DayEntity> days = dayDtos
                .stream()
                .map(dayDto-> {
                    Optional<CourseEntity> course = courseEntities
                            .stream()
                            .filter(entity -> (entity.getId() == dayDto.getCourseId()))
                            .findFirst();
                    if (course.isEmpty()) {
                        return null;
                    }
                    DayEntity dayEntity = dayMapper.toEntity(dayDto);
                    dayEntity.setCourse(course.get());
                    return dayEntity;
                })
                .filter(Objects::nonNull)
                .toList();
        dayRepository.saveAll(days);
    }
}
