package com.jpmc.theater;

import java.time.Duration;
import java.time.LocalTime;
import java.util.Objects;

public class Movie {

    private String title;
    private String description;
    private Duration runningTime;
    private double ticketPrice;
    private boolean specialCode;

    public Movie(String title, String description, Duration runningTime, double ticketPrice, boolean specialCode) {
        this.title = title;
        this.description = description;
        this.runningTime = runningTime;
        this.ticketPrice = ticketPrice;
        this.specialCode = specialCode;
    }

    public String getTitle() {
        return title;
    }

    public Duration getRunningTime() {
        return runningTime;
    }

    public double getTicketPrice() {
        return ticketPrice;
    }

    public double calculateTicketPrice(Showing showing) {
        return ticketPrice - getDiscount(showing);
    }

    //todo: edge cases with negative values need to be handled in logic!
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

        /**based on the requirements to discount ticket purchases between 11am - 4pm,
         * the matinee discount time is defined by this window.
         * Seconds seemed unnecessarily granular so I omitted them */
        LocalTime matineeWindowStartTime = LocalTime.of(11, 0);
        LocalTime matineeWindowCloseTime = LocalTime.of(16, 0);

        /** If the showing is equal to the ends of the window, I gave them a discount.
        I would usually double check the requirements with someone in product.
        This ultimately means that you can have a ticket that starts at 4:00:59 and get a discount.*/
        if ((showingStartTime.isAfter(matineeWindowStartTime) && showingStartTime.isBefore(matineeWindowCloseTime))
                || (showingStartTime.equals(matineeWindowStartTime) || showingStartTime.equals(matineeWindowCloseTime))) {
            matineeDiscount = ticketPrice * 0.25; // 25% discount for matinees
        }
        if (7 == dayOfMonth) {
            dateDiscount = 1; // $1 discount for showings on the 7th of the month
        }
        if (specialCode) {
            specialDiscount = ticketPrice * 0.2;  // 20% discount for special movie
        }
        if (showSequence == 1) {
            sequenceDiscount = 3; // $3 discount for 1st show
        } else if (showSequence == 2) {
            sequenceDiscount = 2; // $2 discount for 2nd show
        }
        // biggest discount wins
        return Math.max(specialDiscount, Math.max(sequenceDiscount, Math.max( matineeDiscount, dateDiscount)));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Movie movie = (Movie) o;
        return Double.compare(movie.ticketPrice, ticketPrice) == 0
                && Objects.equals(title, movie.title)
                && Objects.equals(description, movie.description)
                && Objects.equals(runningTime, movie.runningTime)
                && Objects.equals(specialCode, movie.specialCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, description, runningTime, ticketPrice, specialCode);
    }
}