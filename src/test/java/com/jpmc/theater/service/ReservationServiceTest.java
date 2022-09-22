package com.jpmc.theater.service;

import com.jpmc.theater.model.Movie;
import com.jpmc.theater.model.Reservation;
import com.jpmc.theater.model.Showing;
import com.jpmc.theater.model.Theater;
import com.jpmc.theater.utils.TestUtils;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.lang.reflect.Field;
import java.math.RoundingMode;
import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ReservationServiceTest {
  private Theater theater;
  private ReservationService reservationService;
  private TestUtils testUtils = new TestUtils();

  private static final CurrencyUnit USD = CurrencyUnit.USD;
  private String customerName = "Jane Doe";

  private Money testTicketFullPrice = Money.of(USD, 12.5);
  private Money freeTicketPrice = Money.of(USD, 0);
  private Money expectedTestTicketPriceWithFirstInSequenceDiscount = testTicketFullPrice.minus(3d);
  private Money expectedTestTicketPriceWithSecondInSequenceDiscount = testTicketFullPrice.minus(2d);
  private Money expectedTestTicketPriceWithDateDiscount = testTicketFullPrice.minus(1d);
  private Money expectedTestTicketPriceWithSpecialDiscount =
      testTicketFullPrice.minus(testTicketFullPrice.multipliedBy(0.2d, RoundingMode.DOWN));
  private Money expectedTestTicketPriceWithMatineeDiscount =
      testTicketFullPrice.minus(testTicketFullPrice.multipliedBy(0.25d, RoundingMode.DOWN));

  @BeforeEach
  public void setUp() {
    reservationService = testUtils.setUpReservationService();
  }

  @AfterEach
  public void restoreTheaterSeatingCapacity() throws NoSuchFieldException, IllegalAccessException {
    Field field = Theater.class.getDeclaredField("currentSeatingCapacity");
    field.setAccessible(true);
    field.set(theater.getCurrentSeatingCapacity(), 25);
  }

  @Test
  public void
      createReservationByShowNumber_specialMovieNoOtherEligibleDiscountSingleTicket_reservationCreatedAndSpecialDiscountApplied() {
    int specialMovieNoOtherDiscountShowNumber = 5;
    int howManyTickets = 1;

    Reservation reservation =
        reservationService.createReservationByShowNumber(
            customerName, specialMovieNoOtherDiscountShowNumber, howManyTickets);

    assertDiscountAndReservationDetails(
        howManyTickets, reservation, expectedTestTicketPriceWithSpecialDiscount);
  }

  @Test
  public void
      createReservationByShowNumber_firstMovieNoOtherEligibleDiscountTwoTickets_reservationCreatedAndThreeDollarDiscountApplied() {
    int firstShowNumber = 0;
    int howManyTickets = 2;

    Reservation reservation =
        reservationService.createReservationByShowNumber(
            customerName, firstShowNumber, howManyTickets);

    assertDiscountAndReservationDetails(
        howManyTickets, reservation, expectedTestTicketPriceWithFirstInSequenceDiscount);
  }

  @Test
  public void
      createReservationByShowNumber_secondMovieBeforeMatineeWindowTenTickets_reservationCreatedAndTwoDollarDiscountApplied() {
    int secondShowNumber = 1;
    int howManyTickets = 10;

    Reservation reservation =
        reservationService.createReservationByShowNumber(
            customerName, secondShowNumber, howManyTickets);

    assertDiscountAndReservationDetails(
        howManyTickets, reservation, expectedTestTicketPriceWithSecondInSequenceDiscount);
  }

  @Test
  public void createReservationByShowNumber_seventhOfMonthOneTicket_DateDiscountApplied() {
    int showNumberForSeventhOfMonthMovie = 9;
    int howManyTickets = 1;

    Reservation reservation =
        reservationService.createReservationByShowNumber(
            customerName, showNumberForSeventhOfMonthMovie, howManyTickets);

    assertDiscountAndReservationDetails(
        howManyTickets, reservation, expectedTestTicketPriceWithDateDiscount);
  }

  @Test
  public void createReservationByShowNumber_theaterCapacityReached_returnsNull() {
    int showNumber = 0;
    int howManyTickets = 26; // current seat capacity is hardcoded at 25

    Reservation reservation =
        reservationService.createReservationByShowNumber(customerName, showNumber, howManyTickets);

    assertNull(reservationService.getDiscountedTicketPrice());
    assertNull(reservation);
  }

  @Test
  public void
      createReservationByShowNumber_matineeAndSpecialMovieOnSeventh_matineeDiscountApplied() {
    int matineeAndSpecialMovieOnSeventhShowNumber = 11;
    int howManyTickets = 12;

    Reservation reservation =
        reservationService.createReservationByShowNumber(
            customerName, matineeAndSpecialMovieOnSeventhShowNumber, howManyTickets);

    assertDiscountAndReservationDetails(
        howManyTickets, reservation, expectedTestTicketPriceWithMatineeDiscount);
  }

  @Test
  public void
      createReservationByShowNumber_twelfthMovieNotSpecialNotMatineeWindowSixthDayOfMonth_NoDiscountApplied() {
    int noDiscountShowNumberOnSixth = 12;
    int howManyTickets = 22;

    Reservation reservation =
        reservationService.createReservationByShowNumber(
            customerName, noDiscountShowNumberOnSixth, howManyTickets);

    assertDiscountAndReservationDetails(howManyTickets, reservation, testTicketFullPrice);
  }

  @Test
  public void
      createReservationByShowNumber_MovieNotSpecialNotMatineeWindowEighthDayOfMonth_NoDiscountApplied() {
    int noDiscountShowNumberOnEighth = 8;
    int howManyTickets = 22;

    Reservation reservation =
        reservationService.createReservationByShowNumber(
            customerName, noDiscountShowNumberOnEighth, howManyTickets);

    assertDiscountAndReservationDetails(howManyTickets, reservation, testTicketFullPrice);
  }

  @Test
  public void
      createReservationByShowNumber_cheapTicketMatineeWithSeventhOfMonthDiscount_negativeDiscountTicketIsFree() {
    int cheapTicketOnSeventhShowNumber = 6;
    int howManyTickets = 1;

    Reservation reservation =
        reservationService.createReservationByShowNumber(
            customerName, cheapTicketOnSeventhShowNumber, howManyTickets);

    assertDiscountAndReservationDetails(howManyTickets, reservation, freeTicketPrice);
  }

  @Test
  public void createReservationByShowNumber_ShowingHasNullFields_ExceptionCaught() {
    Movie movie = new Movie("Turning Red", "", Duration.ofMinutes(85), testTicketFullPrice, false);
    List<Showing> schedule = List.of(new Showing(movie, 0, null));
    theater = new Theater(schedule);
    reservationService = new ReservationService(theater);

    assertThrows(
        NullPointerException.class,
        () -> reservationService.createReservationByShowNumber(customerName, 0, 1));
  }

  private void assertDiscountAndReservationDetails(
      int howManyTickets, Reservation reservation, Money testTicketPriceWithDiscountUnderTest) {
    assertEquals(
        testTicketPriceWithDiscountUnderTest, reservationService.getDiscountedTicketPrice());
    assertEquals(howManyTickets, reservation.getTickets().size());
    assertEquals(
        testTicketPriceWithDiscountUnderTest.multipliedBy(howManyTickets),
        reservation.getTotalFee());
    assertEquals(customerName, reservation.getCustomer().getName());
  }
}
