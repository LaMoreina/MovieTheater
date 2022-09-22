package com.jpmc.theater.utils;

import com.jpmc.theater.model.*;
import com.jpmc.theater.service.ReservationService;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;

import java.math.RoundingMode;
import java.time.*;
import java.util.List;
import java.util.UUID;

import static org.joda.money.CurrencyUnit.USD;

public class TestUtils {


    public ReservationService setUpReservationService() {

        Money cheapTicketFullPrice = Money.of(USD, .75);
        Money testTicketFullPrice = Money.of(USD, 12.5);

        LocalDate eighthOfMonth = LocalDate.of(1990, Month.JANUARY, 8);
        LocalDate seventhOfMonth = LocalDate.of(1990, Month.AUGUST, 7);
        LocalDate sixthOfMonth = LocalDate.of(1990, Month.AUGUST, 6);

        LocalTime beforeMatineeWindow = LocalTime.of(10, 59);
        LocalTime beginMatineeWindow = LocalTime.of(11, 0);
        LocalTime inMatineeWindow = LocalTime.of(13, 13);
        LocalTime nearEndMatineeWindow = LocalTime.of(15, 59);
        LocalTime endMatineeWindow = LocalTime.of(16, 1);

        Movie spiderMan = new Movie("Spider-Man: No Way Home", "",
                Duration.ofMinutes(90), testTicketFullPrice, true);
        Movie turningRed = new Movie("Turning Red", "",
                Duration.ofMinutes(85), testTicketFullPrice, false);
        Movie theCheapTicketMovie = new Movie("The Batman with George Clooney", "Uma Thurman is in it.",
                Duration.ofMinutes(95), cheapTicketFullPrice, true);
        Movie seventhOfMonthMovie = new Movie("The Batman", "",
                Duration.ofMinutes(95), testTicketFullPrice, false);
        Movie seventhOfMonthAndSpecialMovie = new Movie("The Batman", "The one directed by Chris Nolan",
                Duration.ofMinutes(95), testTicketFullPrice, true);
        Movie matineeAndSpecialMovieOnSeventh = new Movie("The Batman", "",
                Duration.ofMinutes(95), testTicketFullPrice, true);
        Movie twelfthMovieNotSpecialNotMatineeWindowSixthDayOfMonth = new Movie("The Crows Have Eyes", "The Crowening!",
                Duration.ofMinutes(9), testTicketFullPrice, false);
        Movie eighthMovieNotSpecialNotMatineeWindowEighthDayOfMonth = new Movie("The Crows Have Eyes", "The Crowening!",
                Duration.ofMinutes(9), testTicketFullPrice, false);
        List<Showing> schedule = List.of(
                new Showing(turningRed, 0, LocalDateTime.of(sixthOfMonth, LocalTime.of(9, 0))),
                new Showing(turningRed, 1, LocalDateTime.of(sixthOfMonth, beforeMatineeWindow)),
                new Showing(theCheapTicketMovie, 2, LocalDateTime.of(sixthOfMonth, beginMatineeWindow)),
                new Showing(spiderMan, 3, LocalDateTime.of(sixthOfMonth, inMatineeWindow)),
                new Showing(turningRed, 4, LocalDateTime.of(sixthOfMonth, inMatineeWindow)),
                new Showing(spiderMan, 5, LocalDateTime.of(sixthOfMonth, endMatineeWindow)),
                new Showing(theCheapTicketMovie, 6, LocalDateTime.of(seventhOfMonth, inMatineeWindow)),
                new Showing(turningRed, 7, LocalDateTime.of(sixthOfMonth, LocalTime.of(19, 30))),
                new Showing(eighthMovieNotSpecialNotMatineeWindowEighthDayOfMonth, 8, LocalDateTime.of(eighthOfMonth, LocalTime.of(21, 10))),
                new Showing(seventhOfMonthMovie, 9, LocalDateTime.of(seventhOfMonth, LocalTime.of(23, 0))),
                new Showing(seventhOfMonthAndSpecialMovie, 10, LocalDateTime.of(seventhOfMonth, LocalTime.of(23, 0))),
                new Showing(matineeAndSpecialMovieOnSeventh, 11, LocalDateTime.of(seventhOfMonth, nearEndMatineeWindow)),
                new Showing(twelfthMovieNotSpecialNotMatineeWindowSixthDayOfMonth, 12, LocalDateTime.of(sixthOfMonth, endMatineeWindow))
        );
        Theater theater = new Theater(schedule);
        return new ReservationService(theater);
    }
    
}
