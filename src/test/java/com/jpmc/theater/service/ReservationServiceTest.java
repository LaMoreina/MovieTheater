package com.jpmc.theater.service;

import static org.junit.jupiter.api.Assertions.*;

import com.jpmc.theater.model.*;
import org.javamoney.moneta.Money;
import org.junit.jupiter.api.Test;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;


class ReservationServiceTest {
    private Theater theater;
    private ReservationService reservationService;
    private static final CurrencyUnit USD = Monetary.getCurrency("USD");


    public void setUp() {
        LocalDateProvider provider = new LocalDateProvider();
            Movie spiderMan = new Movie("Spider-Man: No Way Home", "", Duration.ofMinutes(90), Money.of(12.5, USD), true);
            Movie turningRed = new Movie("Turning Red","",  Duration.ofMinutes(85), Money.of(11, USD), false);
            Movie theBatMan = new Movie("The Batman", "", Duration.ofMinutes(95), Money.of(9, USD), false);
            List<Showing> schedule = List.of(
                    new Showing(turningRed, 1, LocalDateTime.of(provider.currentDate(), LocalTime.of(9, 0))),
                    new Showing(spiderMan, 2, LocalDateTime.of(provider.currentDate(), LocalTime.of(11, 0))),
                    new Showing(theBatMan, 3, LocalDateTime.of(provider.currentDate(), LocalTime.of(12, 50))),
                    new Showing(turningRed, 4, LocalDateTime.of(provider.currentDate(), LocalTime.of(14, 30))),
                    new Showing(spiderMan, 5, LocalDateTime.of(provider.currentDate(), LocalTime.of(16, 10))),
                    new Showing(theBatMan, 6, LocalDateTime.of(provider.currentDate(), LocalTime.of(17, 50))),
                    new Showing(turningRed, 7, LocalDateTime.of(provider.currentDate(), LocalTime.of(19, 30))),
                    new Showing(spiderMan, 8, LocalDateTime.of(provider.currentDate(), LocalTime.of(21, 10))),
                    new Showing(theBatMan, 9, LocalDateTime.of(provider.currentDate(), LocalTime.of(23, 0)))
            );
        theater = new Theater(provider, schedule);
        reservationService = new ReservationService(theater);
    }

    @Test
    public void createReservationByShowNumber_validShowNumberTwoTickets_() {
        setUp();
        String customerName = "Jane Doe";
        int showNumber = 3;
        int howManyTickets = 2;

        Reservation reservation = reservationService.createReservationByShowNumber(customerName, showNumber, howManyTickets);

        assertEquals(2, reservation.getTickets().size());
        assertTrue(Money.of(22, USD).isEqualTo(reservation.getTotalFee()));
        assertEquals("Jane Doe", reservation.getCustomer().getName());
    }

    @Test
    public void getCalculatedDiscount_happyPath() {
        setUp();
    }

}