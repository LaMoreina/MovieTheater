package com.jpmc.theater.service;

import static org.junit.jupiter.api.Assertions.*;

import com.jpmc.theater.model.*;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.math.RoundingMode;
import java.time.*;
import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ReservationServiceTest {
    private Theater theater;
    private ReservationService reservationService;

    private static final CurrencyUnit USD = CurrencyUnit.USD;
    private String customerName = "Jane Doe";

    private Money testTicketPrice = Money.of(USD, 12.5);
    private Money expensiveTicketPrice = Money.of(USD, 1200);
    private Money testTicketPriceWithMatineeDiscount = testTicketPrice.multipliedBy(0.75d, RoundingMode.DOWN);
    private Money testTicketPriceWithFirstInSequenceDiscount = testTicketPrice.minus(3d);
    private Money testTicketPriceWithSecondInSequenceDiscount = testTicketPrice.minus(2d);
    private Money testTicketPriceWithDateDiscount = testTicketPrice.minus(1d);
    private Money testTicketPriceWithSpecialDiscount = testTicketPrice.multipliedBy(0.8d, RoundingMode.DOWN);

    private LocalDate SeventhOfMonth = LocalDate.of(1990, Month.AUGUST, 7);

    @BeforeAll
    public void setUp() {
        Movie spiderMan = new Movie("Spider-Man: No Way Home", "", Duration.ofMinutes(90), testTicketPrice, true);
        Movie turningRed = new Movie("Turning Red","",  Duration.ofMinutes(85), testTicketPrice, false);
        Movie theBatMan = new Movie("The Batman", "", Duration.ofMinutes(95), expensiveTicketPrice, true);
        List<Showing> schedule = List.of(
                new Showing(turningRed, 0, LocalDateTime.of(LocalDate.now(), LocalTime.of(9, 0))),
                new Showing(turningRed, 1, LocalDateTime.of(LocalDate.now(), LocalTime.of(10, 59))),//near matinee begin
                new Showing(theBatMan, 2, LocalDateTime.of(LocalDate.now(), LocalTime.of(11, 0))),//being matinee
                new Showing(spiderMan, 3, LocalDateTime.of(LocalDate.now(), LocalTime.of(12, 50))),
                new Showing(turningRed, 4, LocalDateTime.of(LocalDate.now(), LocalTime.of(15, 59))), //near matinee end
                new Showing(spiderMan, 5, LocalDateTime.of(LocalDate.now(), LocalTime.of(16, 10))),
                new Showing(theBatMan, 6, LocalDateTime.of(LocalDate.now(), LocalTime.of(17, 50))),
                new Showing(turningRed, 7, LocalDateTime.of(LocalDate.now(), LocalTime.of(19, 30))),
                new Showing(spiderMan, 8, LocalDateTime.of(LocalDate.now(), LocalTime.of(21, 10))),
                new Showing(turningRed, 9, LocalDateTime.of(SeventhOfMonth, LocalTime.of(23, 0)))
            );
        theater = new Theater(schedule);
        reservationService = new ReservationService(theater);
    }

    @Test
    public void createReservationByShowNumber_specialMovieNoOtherEligibleDiscountSingleTicket_reservationCreatedAndSpecialDiscountApplied() {
        int showNumber = 5;
        int howManyTickets = 1;

        Reservation reservation = reservationService.createReservationByShowNumber(customerName, showNumber, howManyTickets);

        assertEquals(testTicketPriceWithSpecialDiscount, reservationService.getDiscountedTicketPrice());
        assertEquals(howManyTickets, reservation.getTickets().size());
        assertEquals(testTicketPriceWithSpecialDiscount.multipliedBy(howManyTickets), reservation.getTotalFee());
        assertEquals(customerName, reservation.getCustomer().getName());
    }
    @Test
    public void createReservationByShowNumber_firstMovieNoOtherEligibleDiscountTwoTickets_reservationCreatedAndThreeDollarDiscountApplied() {
        int showNumber = 0;
        int howManyTickets = 2;

        Reservation reservation = reservationService.createReservationByShowNumber(customerName, showNumber, howManyTickets);

        assertEquals(testTicketPriceWithFirstInSequenceDiscount, reservationService.getDiscountedTicketPrice());
        assertEquals(howManyTickets, reservation.getTickets().size());
        assertEquals(testTicketPriceWithFirstInSequenceDiscount.multipliedBy(howManyTickets), reservation.getTotalFee());
        assertEquals(customerName, reservation.getCustomer().getName());
    }
    @Test
    public void createReservationByShowNumber_secondMovieBeforeMatineeWindowTenTickets_reservationCreatedAndTwoDollarDiscountApplied() {
        int showNumber = 1;
        int howManyTickets = 10;

        Reservation reservation = reservationService.createReservationByShowNumber(customerName, showNumber, howManyTickets);

        assertEquals(testTicketPriceWithSecondInSequenceDiscount, reservationService.getDiscountedTicketPrice());
        assertEquals(howManyTickets, reservation.getTickets().size());
        assertEquals(testTicketPriceWithSecondInSequenceDiscount.multipliedBy(howManyTickets), reservation.getTotalFee());
        assertEquals(customerName, reservation.getCustomer().getName());
    }
    @Test
    public void createReservationByShowNumber_seventhOfMonthOneTicket_DateDiscountApplied() {
        int showNumber = 9; //This show is on the 7th of the month
        int howManyTickets = 1;

        Reservation reservation = reservationService.createReservationByShowNumber(customerName, showNumber, howManyTickets);

        assertEquals(testTicketPriceWithDateDiscount, reservationService.getDiscountedTicketPrice());
        assertEquals(howManyTickets, reservation.getTickets().size());
        assertEquals(testTicketPriceWithDateDiscount.multipliedBy(howManyTickets), reservation.getTotalFee());
        assertEquals(customerName, reservation.getCustomer().getName());
    }
    @Test
    public void createReservationByShowNumber_firstMovieAndSpecialMovieExpensiveTicket_SpecialDiscountApplied() {
    }
    @Test
    public void getCalculatedDiscount_firstMovieAndSpecialMovieInexpensiveTicket_SequenceDiscountApplied() {
    }
    @Test
    public void getCalculatedDiscount_secondMovieAndSpecialMovieInexpensiveTicket_SequenceDiscountApplied() {
        setUp();

    }
    @Test
    public void getCalculatedDiscount_secondMovieAndSpecialMovieExpensiveTicket_SpecialDiscountApplied() {
        setUp();

    }
//------------New Rules Applied------------------------------------

    @Test
    public void getCalculatedDiscount_firstMovieAndSpecialMovieExpensiveTicketMatineeWindow_MatineeDiscountApplied() {
        setUp();

    }
    @Test
    public void getCalculatedDiscount_firstMovieAndSpecialMovieExpensiveTicketMatineeCloseWindow_MatineeDiscountApplied() {
        setUp();

    }
    @Test
    public void getCalculatedDiscount_firstMovieAndSpecialMovieInexpensiveTicketMatineeWindow_SequenceDiscountApplied() {
        setUp();

    }
    @Test
    public void getCalculatedDiscount_EightMovieNotSpecialNotMatineeWindowSeventhDayOfMonth_DateDiscountApplied() {
        setUp();

    }
    @Test
    public void getCalculatedDiscount_EightMovieSpecialNotMatineeWindowSeventhDayOfMonth_SpecialDiscountApplied() {
        setUp();

    }
    @Test
    public void getCalculatedDiscount_EightMovieSpecialNotMatineeWindowSeventhDayOfMonthInexpensiveTicket_DateDiscountApplied() {
        setUp();

    }
    @Test
    public void getCalculatedDiscount_EightMovieNotSpecialNotMatineeWindowSixthDayOfMonth_NoDiscountApplied() {
        setUp();

    }
}