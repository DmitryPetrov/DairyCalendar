package net.personal.dairycalendar;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan("net.personal.dairycalendar.storage.entity")
public class DairyCalendarApplication {

    public static void main(String[] args) {
        SpringApplication.run(DairyCalendarApplication.class, args);
    }

}
