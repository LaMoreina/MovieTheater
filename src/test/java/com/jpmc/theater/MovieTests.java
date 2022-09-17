package com.jpmc.theater;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MovieTests {

    //todo: set up default movie to use in testing to avoid redundancy
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


}
