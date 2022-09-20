package com.jpmc.theater.service;

import com.jpmc.theater.model.*;

import java.time.LocalTime;
import java.util.ArrayList;


public class ReservationService implements ReservationServiceImpl {
    private Theater theater;

    public ReservationService (Theater theater){
        this.theater = theater;
    }

    /**
     * Method to create a reservation by the show number--its index within the list of showings that comprise the schedule.
     * If the theater does not have enough seating available to reserve the amount requested, the method currently returns null.
     *
     * @param customerName
     * @param showNumber
     * @param howManyTickets
     *
     * Reservations for multiple tickets are under one customer and every customer has a unique UUID
     * */
    public Reservation createReservationByShowNumber(String customerName, int showNumber, int howManyTickets) {

        if (theater.getCurrentSeatingCapacity() - howManyTickets >= 0) {
            ArrayList<Ticket> reservationTickets = new ArrayList<>();
            Showing showing = theater.getSchedule().get(showNumber);
            Customer customer = new Customer(customerName);

            //calculate discount and make sure no negative balance occurs such that the theater never "owes" the customer money
            double calculateTicketPriceWithAnyDiscount = showing.getMovie().getTicketPrice() - getDiscount(showing);
            calculateTicketPriceWithAnyDiscount = (calculateTicketPriceWithAnyDiscount) >= 0 ? calculateTicketPriceWithAnyDiscount : 0;

            for (int i = 0; i < howManyTickets; i++) {
                Ticket ticket = new Ticket(showing, calculateTicketPriceWithAnyDiscount);
                reservationTickets.add(ticket);
            }
            return new Reservation(customer, showing, reservationTickets);
        }
        return null; //todo: update with error handling
    }


    /**
     * Calculates the possible discounts eligible for a given showing and outputs the largest
     * possible discount. Only one discount may be applied at a time.  In this implementation
     * there are four possible discounts:
     *      specialDiscount - 20% discount for special movie
     *      sequenceDiscount - $3 discount for 1st show or $2 discount for 2nd show
     *      matineeDiscount
     *      dateDiscount - $1 discount for showings on the 7th of the month
     *
     * @param showing
     *
     * Based on the requirements to discount ticket purchases between 11am - 4pm,
     * the matinee discount time is defined by this window.
     * Seconds seemed unnecessarily granular so I omitted them.
     * If the showing is equal to the ends of the window, I gave them a discount.
     * I would usually double check the requirements with someone in product.
     * This ultimately means that you can have a ticket that starts at 4:00:59
     * and still get a discount.*/
    private double getDiscount(Showing showing) {

        double specialDiscount = 0;
        double sequenceDiscount = 0;
        double matineeDiscount = 0;
        double dateDiscount = 0;

        int showSequence = showing.getSequenceOfTheDay();
        int dayOfMonth = showing.getStartTime().getDayOfMonth();

        int hourOfShowStart = showing.getStartTime().getHour();
        int minuteOfShowStart = showing.getStartTime().getMinute();
        LocalTime showingStartTime = LocalTime.of(hourOfShowStart, minuteOfShowStart);

        LocalTime matineeWindowStartTime = LocalTime.of(11, 0);
        LocalTime matineeWindowCloseTime = LocalTime.of(16, 0);

        if ((showingStartTime.isAfter(matineeWindowStartTime) && showingStartTime.isBefore(matineeWindowCloseTime))
                || (showingStartTime.equals(matineeWindowStartTime) || showingStartTime.equals(matineeWindowCloseTime))) {
            matineeDiscount = showing.getMovieFee() * 0.25; // 25% discount for matinees
        }
        if (7 == dayOfMonth) {
            dateDiscount = 1;
        }
        if (showing.getMovie().hasSpecialCode()) {
            specialDiscount = showing.getMovieFee() * 0.2;
        }
        if (showSequence == 1) {
            sequenceDiscount = 3;
        } else if (showSequence == 2) {
            sequenceDiscount = 2;
        }
        return Math.max(specialDiscount, Math.max(sequenceDiscount, Math.max( matineeDiscount, dateDiscount)));
    }

    public double getCalculatedDiscount(Showing showing) {
        return getDiscount(showing);
    }
}
