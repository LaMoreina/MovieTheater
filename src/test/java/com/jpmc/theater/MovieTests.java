package com.jpmc.theater;

import org.junit.jupiter.api.Test;

import java.time.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MovieTests {

    //todo: set up default movie to use in testing to avoid redundancy
    //todo: set up time global vars to test
    //todo: edge cases with negative values need to be handled in logic!

    @Test
    void calculateTicketPrice_specialMovieWith20PercentDiscount_SpecialDiscountApplied() {
        Movie spiderMan = new Movie("Spider-Man: No Way Home", Duration.ofMinutes(90),12.5, 1);
        Showing showing = new Showing(spiderMan, 5, LocalDateTime.of(LocalDate.now(), LocalTime.now()));
        assertEquals(10, spiderMan.calculateTicketPrice(showing));
    }

    @Test
    void calculateTicketPrice_firstMovieNotSpecialMovie_threeDollarDiscountApplied() {
        Movie spiderMan = new Movie("Spider-Man: No Way Home", Duration.ofMinutes(90),12.5, 0);
        Showing showing = new Showing(spiderMan, 1, LocalDateTime.of(LocalDate.now(), LocalTime.now()));
        assertEquals(9.5, spiderMan.calculateTicketPrice(showing));
    }

    @Test
    void calculateTicketPrice_firstMovieAndSpecialMovieExpensiveTicket_SpecialDiscountApplied() {
        Movie spiderMan = new Movie("Spider-Man: No Way Home", Duration.ofMinutes(90),1200, 1);
        Showing showing = new Showing(spiderMan, 1, LocalDateTime.of(LocalDate.now(), LocalTime.now()));
        assertEquals((1200 * .8), spiderMan.calculateTicketPrice(showing));
    }

    @Test
    void calculateTicketPrice_firstMovieAndSpecialMovieInexpensiveTicket_SequenceDiscountApplied() {
        Movie spiderMan = new Movie("Spider-Man: No Way Home", Duration.ofMinutes(90),10, 1);
        Showing showing = new Showing(spiderMan, 1, LocalDateTime.of(LocalDate.now(), LocalTime.now()));
        assertEquals(7, spiderMan.calculateTicketPrice(showing));
    }

    @Test
    void calculateTicketPrice_secondMovieAndSpecialMovieInexpensiveTicket_SequenceDiscountApplied() {
        Movie spiderMan = new Movie("Spider-Man: No Way Home", Duration.ofMinutes(90),10, 1);
        Showing showing = new Showing(spiderMan, 2, LocalDateTime.of(LocalDate.now(), LocalTime.now()));
        assertEquals(8, spiderMan.calculateTicketPrice(showing));
    }

    @Test
    void calculateTicketPrice_secondMovieAndSpecialMovieExpensiveTicket_SpecialDiscountApplied() {
        Movie spiderMan = new Movie("Spider-Man: No Way Home", Duration.ofMinutes(90),1200, 1);
        Showing showing = new Showing(spiderMan, 2, LocalDateTime.of(LocalDate.now(), LocalTime.now()));
        assertEquals((1200 * .8), spiderMan.calculateTicketPrice(showing));
    }
//------------New Rules Applied------------------------------------
    @Test
    void calculateTicketPrice_firstMovieAndSpecialMovieExpensiveTicketMatineeWindow_MatineeDiscountApplied() {

        LocalDate todaysDate = LocalDate.now();
        LocalTime time = LocalTime.of(11, 00);
        LocalDateTime localDateTime = LocalDateTime.of(todaysDate, time);

        Movie spiderMan = new Movie("Spider-Man: No Way Home", Duration.ofMinutes(90),1200, 1);
        Showing showing = new Showing(spiderMan, 1, localDateTime);
        assertEquals((1200 * .75), spiderMan.calculateTicketPrice(showing));
    }

    @Test
    void calculateTicketPrice_firstMovieAndSpecialMovieInexpensiveTicketMatineeWindow_SequenceDiscountApplied() {

        LocalDate todaysDate = LocalDate.now();
        LocalTime time = LocalTime.of(11, 00);
        LocalDateTime localDateTime = LocalDateTime.of(todaysDate, time);

        Movie spiderMan = new Movie("Spider-Man: No Way Home", Duration.ofMinutes(90),10, 1);
        Showing showing = new Showing(spiderMan, 1, localDateTime);
        assertEquals(7, spiderMan.calculateTicketPrice(showing));
    }

    @Test
    void calculateTicketPrice_EightMovieNotSpecialNotMatineeWindowSeventhDayOfMonth_DateDiscountApplied() {
        LocalDate todaysDate = LocalDate.of(2080, Month.AUGUST, 7);

        LocalTime time = LocalTime.of(10, 59);
        LocalDateTime localDateTime = LocalDateTime.of(todaysDate, time);

        Movie spiderMan = new Movie("Spider-Man: No Way Home", Duration.ofMinutes(90),10, 0);
        Showing showing = new Showing(spiderMan, 8, localDateTime);
        assertEquals(9, spiderMan.calculateTicketPrice(showing));
    }

    @Test
    void calculateTicketPrice_EightMovieSpecialNotMatineeWindowSeventhDayOfMonth_SpecialDiscountApplied() {
        LocalDate todaysDate = LocalDate.of(2080, Month.AUGUST, 7);

        LocalTime time = LocalTime.of(10, 59);
        LocalDateTime localDateTime = LocalDateTime.of(todaysDate, time);

        Movie spiderMan = new Movie("Spider-Man: No Way Home", Duration.ofMinutes(90),10, 1);
        Showing showing = new Showing(spiderMan, 8, localDateTime);
        assertEquals(8, spiderMan.calculateTicketPrice(showing));
    }

    @Test
    void calculateTicketPrice_EightMovieSpecialNotMatineeWindowSeventhDayOfMonthInexpensiveTicket_DateDiscountApplied() {
        LocalDate todaysDate = LocalDate.of(2080, Month.AUGUST, 7);

        LocalTime time = LocalTime.of(10, 59);
        LocalDateTime localDateTime = LocalDateTime.of(todaysDate, time);

        Movie spiderMan = new Movie("Spider-Man: No Way Home", Duration.ofMinutes(90),3, 1);
        Showing showing = new Showing(spiderMan, 8, localDateTime);
        assertEquals(2, spiderMan.calculateTicketPrice(showing));
    }

    @Test
    void calculateTicketPrice_EightMovieNotSpecialNotMatineeWindowSixthDayOfMonth_NoDiscountApplied() {
        LocalDate todaysDate = LocalDate.of(2080, Month.AUGUST, 6);

        LocalTime time = LocalTime.of(10, 59);
        LocalDateTime localDateTime = LocalDateTime.of(todaysDate, time);

        Movie spiderMan = new Movie("Spider-Man: No Way Home", Duration.ofMinutes(90),10, 0);
        Showing showing = new Showing(spiderMan, 8, localDateTime);
        assertEquals(10, spiderMan.calculateTicketPrice(showing));
    }
}
