package com.jpmc.theater.service;

import static org.junit.jupiter.api.Assertions.*;

import com.jpmc.theater.model.*;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.junit.jupiter.api.*;

import java.lang.reflect.Field;
import java.math.RoundingMode;
import java.time.*;
import java.util.List;

//todo: set up constant time instance vars to test

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ReservationServiceTest {
    private Theater theater;
    private ReservationService reservationService;

    private static final CurrencyUnit USD = CurrencyUnit.USD;
    private String customerName = "Jane Doe";

    //expect different results for selected discount based on ticket price
    private Money testTicketFullPrice = Money.of(USD, 12.5);
    private Money cheapTicketPrice = Money.of(USD, 1200);

    private Money expectedTestTicketPriceWithFirstInSequenceDiscount = testTicketFullPrice.minus(3d);
    private Money expectedTestTicketPriceWithSecondInSequenceDiscount = testTicketFullPrice.minus(2d);
    private Money expectedTestTicketPriceWithDateDiscount = testTicketFullPrice.minus(1d);
    private Money expectedTestTicketPriceWithSpecialDiscount = testTicketFullPrice.minus(testTicketFullPrice.multipliedBy(0.2d, RoundingMode.DOWN));
    private Money expectedTestTicketPriceWithMatineeDiscount = testTicketFullPrice.minus(testTicketFullPrice.multipliedBy(0.25d, RoundingMode.DOWN));

    private Money expectedCheapTicketPriceWithFirstInSequenceDiscount = cheapTicketPrice.minus(3d);

    private LocalDate seventhOfMonth = LocalDate.of(1990, Month.AUGUST, 7);
    private LocalDate sixthOfMonth = LocalDate.of(1990, Month.AUGUST, 6);

    private LocalTime beforeMatineeWindow = LocalTime.of(10, 59);
    private LocalTime beginMatineeWindow = LocalTime.of(11, 0);
    private LocalTime inMatineeWindow = LocalTime.of(13, 13);
    private LocalTime endMatineeWindow = LocalTime.of(16, 1);


    @BeforeEach
    public void setUp() {
        Movie spiderMan = new Movie("Spider-Man: No Way Home", "", Duration.ofMinutes(90), testTicketFullPrice, true);
        Movie turningRed = new Movie("Turning Red","",  Duration.ofMinutes(85), testTicketFullPrice, false);
        Movie theBatMan = new Movie("The Batman", "", Duration.ofMinutes(95), cheapTicketPrice, true);
        Movie seventhMonthMovie = new Movie("The Batman", "", Duration.ofMinutes(95), testTicketFullPrice, false);
        Movie seventhMonthAndSpecialMovie = new Movie("The Batman", "", Duration.ofMinutes(95), testTicketFullPrice, true);
        Movie matineeAndSpecialMovieOnSeventh = new Movie("The Batman", "", Duration.ofMinutes(95), testTicketFullPrice, true);
        Movie twelfthMovieNotSpecialNotMatineeWindowSixthDayOfMonth = new Movie("The Crows Have Eyes", "The Crowening!", Duration.ofMinutes(9), testTicketFullPrice, false);
        List<Showing> schedule = List.of(
                new Showing(turningRed, 0, LocalDateTime.of(sixthOfMonth, LocalTime.of(9, 0))),
                new Showing(turningRed, 1, LocalDateTime.of(sixthOfMonth, beforeMatineeWindow)),
                new Showing(theBatMan, 2, LocalDateTime.of(sixthOfMonth, beginMatineeWindow)),
                new Showing(spiderMan, 3, LocalDateTime.of(sixthOfMonth, inMatineeWindow)),
                new Showing(turningRed, 4, LocalDateTime.of(sixthOfMonth,inMatineeWindow)),
                new Showing(spiderMan, 5, LocalDateTime.of(sixthOfMonth, inMatineeWindow)),
                new Showing(theBatMan, 6, LocalDateTime.of(sixthOfMonth, inMatineeWindow)),
                new Showing(turningRed, 7, LocalDateTime.of(sixthOfMonth, LocalTime.of(19, 30))),
                new Showing(spiderMan, 8, LocalDateTime.of(sixthOfMonth, LocalTime.of(21, 10))),
                new Showing(seventhMonthMovie, 9, LocalDateTime.of(seventhOfMonth, LocalTime.of(23, 0))),
                new Showing(seventhMonthAndSpecialMovie, 10, LocalDateTime.of(seventhOfMonth, LocalTime.of(23, 0))),
                new Showing(matineeAndSpecialMovieOnSeventh, 11, LocalDateTime.of(seventhOfMonth, LocalTime.of(15, 59))),
                new Showing(twelfthMovieNotSpecialNotMatineeWindowSixthDayOfMonth, 12, LocalDateTime.of(sixthOfMonth, endMatineeWindow))
        );
        theater = new Theater(schedule);
        reservationService = new ReservationService(theater);
    }

    @AfterEach
    public void restoreTheaterSeatingCapacity() throws NoSuchFieldException, IllegalAccessException {
        Field field = Theater.class.getDeclaredField("currentSeatingCapacity");
        field.setAccessible(true);
        field.set(theater.getCurrentSeatingCapacity(), 25);
    }

    @Test
    public void createReservationByShowNumber_specialMovieNoOtherEligibleDiscountSingleTicket_reservationCreatedAndSpecialDiscountApplied() {
        int showNumber = 5;
        int howManyTickets = 1;

        Reservation reservation = reservationService.createReservationByShowNumber(customerName, showNumber, howManyTickets);

        assertDiscountAndReservationDetails(howManyTickets, reservation, expectedTestTicketPriceWithSpecialDiscount);
    }
    @Test
    public void createReservationByShowNumber_firstMovieNoOtherEligibleDiscountTwoTickets_reservationCreatedAndThreeDollarDiscountApplied() {
        int showNumber = 0;
        int howManyTickets = 2;

        Reservation reservation = reservationService.createReservationByShowNumber(customerName, showNumber, howManyTickets);

        assertDiscountAndReservationDetails(howManyTickets, reservation, expectedTestTicketPriceWithFirstInSequenceDiscount);
    }
    @Test
    public void createReservationByShowNumber_secondMovieBeforeMatineeWindowTenTickets_reservationCreatedAndTwoDollarDiscountApplied() {
        int showNumber = 1;
        int howManyTickets = 10;

        Reservation reservation = reservationService.createReservationByShowNumber(customerName, showNumber, howManyTickets);

        assertDiscountAndReservationDetails(howManyTickets, reservation, expectedTestTicketPriceWithSecondInSequenceDiscount);
    }
    @Test
    public void createReservationByShowNumber_seventhOfMonthOneTicket_DateDiscountApplied() {
        int showNumber = 9; //This show is on the 7th of the month
        int howManyTickets = 1;

        Reservation reservation = reservationService.createReservationByShowNumber(customerName, showNumber, howManyTickets);

        assertDiscountAndReservationDetails(howManyTickets, reservation, expectedTestTicketPriceWithDateDiscount);
    }
    @Test
    public void createReservationByShowNumber_theaterCapacityReached_returnsNull() {
        int showNumber = 0;
        int howManyTickets = 26; //current seat capacity is hardcoded at 25

        Reservation reservation = reservationService.createReservationByShowNumber(customerName, showNumber, howManyTickets);

        assertNull(reservationService.getDiscountedTicketPrice());
        assertNull(reservation);
    }
    @Test
    public void createReservationByShowNumber_matineeAndSpecialMovieOnSeventh_matineeDiscountApplied() {
        int showNumber = 11;
        int howManyTickets = 12;

        Reservation reservation = reservationService.createReservationByShowNumber(customerName, showNumber, howManyTickets);

        assertDiscountAndReservationDetails(howManyTickets, reservation, expectedTestTicketPriceWithMatineeDiscount);
    }
    @Test
    public void createReservationByShowNumber_twelfthMovieNotSpecialNotMatineeWindowSixthDayOfMonth_NoDiscountApplied() {
        int showNumber = 12;
        int howManyTickets = 22;

        Reservation reservation = reservationService.createReservationByShowNumber(customerName, showNumber, howManyTickets);

        assertDiscountAndReservationDetails(howManyTickets, reservation, testTicketFullPrice);
    }


    private void assertDiscountAndReservationDetails(int howManyTickets, Reservation reservation, Money testTicketPriceWithDiscountUnderTest) {
        assertEquals(testTicketPriceWithDiscountUnderTest, reservationService.getDiscountedTicketPrice());
        assertEquals(howManyTickets, reservation.getTickets().size());
        assertEquals(testTicketPriceWithDiscountUnderTest.multipliedBy(howManyTickets), reservation.getTotalFee());
        assertEquals(customerName, reservation.getCustomer().getName());
    }
//------------New Rules Applied------------------------------------

//    @Test
//    public void getCalculatedDiscount_firstMovieAndSpecialMovieExpensiveTicketMatineeWindow_MatineeDiscountApplied() {
//        setUp();
//
//    }
//    @Test
//    public void getCalculatedDiscount_firstMovieAndSpecialMovieExpensiveTicketMatineeCloseWindow_MatineeDiscountApplied() {
//        setUp();
//
//    }
//    @Test
//    public void getCalculatedDiscount_firstMovieAndSpecialMovieInexpensiveTicketMatineeWindow_SequenceDiscountApplied() {
//        setUp();
//
//    }
//    @Test
//    public void getCalculatedDiscount_EightMovieNotSpecialNotMatineeWindowSeventhDayOfMonth_DateDiscountApplied() {
//        setUp();
//
//    }
//    @Test
//    public void getCalculatedDiscount_EightMovieSpecialNotMatineeWindowSeventhDayOfMonth_SpecialDiscountApplied() {
//        setUp();
//
//    }
//    @Test
//    public void getCalculatedDiscount_EightMovieSpecialNotMatineeWindowSeventhDayOfMonthInexpensiveTicket_DateDiscountApplied() {
//        setUp();
//
//    }
//    @Test
//    public void getCalculatedDiscount_EightMovieNotSpecialNotMatineeWindowSixthDayOfMonth_NoDiscountApplied() {
//        setUp();
//
//    }
}