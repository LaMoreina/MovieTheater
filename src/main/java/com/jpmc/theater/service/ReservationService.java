package com.jpmc.theater.service;

import com.jpmc.theater.model.*;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.joda.money.MoneyUtils;

import java.math.RoundingMode;
import java.time.DateTimeException;
import java.time.LocalTime;
import java.util.ArrayList;

public class ReservationService {
  private Theater theater;
  private static final CurrencyUnit USD = CurrencyUnit.of("USD");
  private Money calculatedDiscount = Money.of(USD, 0);
  private Money discountedTicketPrice;
  private int numberOfAvailableSeats = Theater.getCurrentSeatingCapacity();

  private Money zeroDollars = Money.of(USD, 0);

  public ReservationService(Theater theater) {
    this.theater = theater;
  }

  public Money getDiscountedTicketPrice() {
    return discountedTicketPrice;
  }

  /**
   * Method to create a reservation by the show number--its index within the list of showings that
   * comprise the schedule. If the theater does not have enough seating available to reserve the
   * amount requested, the method currently returns null.
   *
   * @param customerName
   * @param showNumber
   * @param howManyTickets
   *     <p>Reservations for multiple tickets are under one customer and every customer has a unique
   *     UUID
   */
  public Reservation createReservationByShowNumber(
      String customerName, int showNumber, int howManyTickets) {

    if (theater.getCurrentSeatingCapacity() - howManyTickets >= 0) {
      ArrayList<Ticket> reservationTickets = new ArrayList<>();
      Customer customer = new Customer(customerName);

      Showing showing = theater.getSchedule().get(showNumber);
      calculatedDiscount = calculateHighestEligibleTicketDiscount(showing);
      discountedTicketPrice = showing.getMovie().getFullPriceTicket().minus(calculatedDiscount);
      // make sure no negative balance occurs such that the theater never "owes" the customer money
      discountedTicketPrice =
          (discountedTicketPrice).isGreaterThan(zeroDollars) ? discountedTicketPrice : zeroDollars;

      for (int i = 0; i < howManyTickets; i++) {
        Ticket ticket = new Ticket(showing, discountedTicketPrice);
        reservationTickets.add(ticket);
      }
      numberOfAvailableSeats = numberOfAvailableSeats - howManyTickets;
      Theater.setCurrentSeatingCapacity(numberOfAvailableSeats);
      return new Reservation(customer, showing, reservationTickets, discountedTicketPrice);
    }
    return null; // todo: update with custom error?
  }

  /**
   * Calculates the possible discounts eligible for a given showing and outputs the largest possible
   * discount. Only one discount may be applied at a time. In this implementation there are four
   * possible discounts: specialDiscount - 20% discount for special movie sequenceDiscount - $3
   * discount for 1st show or $2 discount for 2nd show matineeDiscount - 25% discount for showing
   * start times between 11am - 4pm dateDiscount - $1 discount for showings on the 7th of the month
   *
   * @param showing
   *     <p>Based on the requirements to discount ticket purchases for showing start times between
   *     11am - 4pm, the matinee discount time is defined by this window. Seconds seemed
   *     unnecessarily granular so I omitted them. If the showing is equal to the ends of the
   *     window, I gave them a discount. I would usually double check the requirements with someone
   *     in product. This ultimately means that you can have a ticket that starts at 4:00:59 and
   *     still get a discount.
   */
  private Money calculateHighestEligibleTicketDiscount(Showing showing) {
    LocalTime showingStartTime = LocalTime.now();
    int dayOfMonth = -1;

    if (null != showing) {
      try {
        dayOfMonth = showing.getStartTime().getDayOfMonth();
        int hourOfShowStart = showing.getStartTime().getHour();
        int minuteOfShowStart = showing.getStartTime().getMinute();
        showingStartTime = LocalTime.of(hourOfShowStart, minuteOfShowStart);
        }
      catch (NullPointerException npe) {
        System.out.println("Please enter a showing with valid fields:" + npe.getMessage());
        return null;
      }
      catch(DateTimeException dte) {
        System.out.println("DateTimeFormattingException:" + dte.getMessage());
        return null;
      }
      catch (Exception e) {
        System.out.println("Please enter a valid showing:" + e.getMessage());
        return null;
      }
    } else {
      throw new NullPointerException();
    }

    Money specialDiscount = getSpecialDiscount(showing);
    Money sequenceDiscount = getSequenceDiscount(showing.getSequenceOfTheDay());
    Money matineeDiscount = getMatineeDiscount(showingStartTime, showing.getMovie().getFullPriceTicket());
    Money dateDiscount = getDateDiscount(dayOfMonth);

    return MoneyUtils.max(
        specialDiscount,
        MoneyUtils.max(sequenceDiscount, MoneyUtils.max(matineeDiscount, dateDiscount)));
  }

  private Money getSpecialDiscount(Showing showing) {
    Money specialDiscount = zeroDollars;
    if (null != showing.getMovie() && showing.getMovie().hasSpecialCode()) {
      specialDiscount =
          showing.getMovie().getFullPriceTicket().multipliedBy(0.2d, RoundingMode.DOWN);
    }
    return specialDiscount;
  }
  private Money getSequenceDiscount(int showSequence) {
    Money sequenceDiscount = zeroDollars;
    if (showSequence == 0) {
      sequenceDiscount = Money.of(USD, 3);
    } else if (showSequence == 1) {
      sequenceDiscount = Money.of(USD, 2);
    }
    return sequenceDiscount;
  }
  private Money getMatineeDiscount(LocalTime showingStartTime, Money fullPriceTicket) {
    Money matineeDiscount = zeroDollars;
    LocalTime matineeWindowStartTime = LocalTime.of(11, 0);
    LocalTime matineeWindowCloseTime = LocalTime.of(16, 0);

    if ((showingStartTime.isAfter(matineeWindowStartTime)
            && showingStartTime.isBefore(matineeWindowCloseTime))
            || (showingStartTime.equals(matineeWindowStartTime)
            || showingStartTime.equals(matineeWindowCloseTime))) {
      matineeDiscount = fullPriceTicket.multipliedBy(.25d, RoundingMode.DOWN);
    }
    return matineeDiscount;
  }
  private Money getDateDiscount(int dayOfMonth) {
    Money dateDiscount = zeroDollars;
    if (7 == dayOfMonth) {
      dateDiscount = Money.of(USD, 1);
    }
    return dateDiscount;
  }
}
