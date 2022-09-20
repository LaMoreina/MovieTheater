package com.jpmc.theater.model;

import java.util.UUID;


public class Ticket {

    private UUID ticketId;
    private double priceChargedAfterAnyDiscounts;
    private Showing showing;

    public Ticket(Showing showing, double priceChargedAfterAnyDiscounts) {
        this.showing = showing;
        this.priceChargedAfterAnyDiscounts = priceChargedAfterAnyDiscounts;
    }

    public UUID createTicket(Showing showing, double ticketPrice) {
        this.ticketId = UUID.randomUUID();
        this.showing = showing;
        this.priceChargedAfterAnyDiscounts = ticketPrice;

        return ticketId;
    }

    public UUID getTicketId() {
        return ticketId;
    }

    public double getPriceChargedAfterAnyDiscounts() {
        return priceChargedAfterAnyDiscounts;
    }

    public Showing getShowing() {
        return showing;
    }

}
