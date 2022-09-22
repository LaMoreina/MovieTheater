package com.jpmc.theater;

import com.jpmc.theater.model.Movie;
import com.jpmc.theater.model.Showing;
import com.jpmc.theater.model.Theater;
import com.jpmc.theater.service.ScheduleService;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class TheaterApp {
  public static void main(String[] args) {
    Movie spiderMan =
        new Movie(
            "Spider-Man: No Way Home",
            "",
            Duration.ofMinutes(90),
            Money.of(CurrencyUnit.USD, 12.5),
            true);
    Movie turningRed =
        new Movie("Turning Red", "", Duration.ofMinutes(85), Money.of(CurrencyUnit.USD, 11), false);
    Movie theBatMan =
        new Movie("The Batman", "", Duration.ofMinutes(95), Money.of(CurrencyUnit.USD, 9), false);
    List<Showing> schedule = new ArrayList<>();
    schedule =
        List.of(
            new Showing(turningRed, 1, LocalDateTime.of(LocalDate.now(), LocalTime.of(9, 0))),
            new Showing(spiderMan, 2, LocalDateTime.of(LocalDate.now(), LocalTime.of(11, 0))),
            new Showing(theBatMan, 3, LocalDateTime.of(LocalDate.now(), LocalTime.of(12, 50))),
            new Showing(turningRed, 4, LocalDateTime.of(LocalDate.now(), LocalTime.of(14, 30))),
            new Showing(spiderMan, 5, LocalDateTime.of(LocalDate.now(), LocalTime.of(16, 10))),
            new Showing(theBatMan, 6, LocalDateTime.of(LocalDate.now(), LocalTime.of(17, 50))),
            new Showing(turningRed, 7, LocalDateTime.of(LocalDate.now(), LocalTime.of(19, 30))),
            new Showing(spiderMan, 8, LocalDateTime.of(LocalDate.now(), LocalTime.of(21, 10))),
            new Showing(theBatMan, 9, LocalDateTime.of(LocalDate.now(), LocalTime.of(23, 0))));
    Theater theater = new Theater(schedule);
    ScheduleService scheduleService = new ScheduleService(theater);
    scheduleService.getConsoleInputForPrintingSchedule();
  }
}
