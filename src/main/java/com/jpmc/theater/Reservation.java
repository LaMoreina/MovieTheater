package com.jpmc.theater;

public class Reservation {
    private Customer customer;
    private Showing showing;
    private int numberOfTickets;
    private int reservationId;
    private int audienceCount; //todo: remove this variable

    public Reservation(Customer customer, Showing showing, int audienceCount) {
        this.customer = customer;
        this.showing = showing;
        this.audienceCount = audienceCount;
    }

    //todo: move reserve method from Theater to here

    public double totalFee() {
        return showing.getMovieFee() * audienceCount;
    }
}