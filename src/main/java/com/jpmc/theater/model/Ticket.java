package com.jpmc.theater.model;

import org.joda.money.Money;

import java.util.UUID;


public class Ticket {

    private UUID ticketId;
    private Showing showing;
    private Money priceChargedAfterAnyDiscounts;
    //todo: should a ticket contain info on which discount received or original movie pricing?
    //private Money originalMoviePrice;
    //DiscountType discountType?

    public Ticket(Showing showing, Money priceChargedAfterAnyDiscounts) {
        this.showing = showing;
        this.priceChargedAfterAnyDiscounts = priceChargedAfterAnyDiscounts;
    }

    public UUID createTicket(Showing showing, Money priceChargedAfterAnyDiscounts) {
        this.ticketId = UUID.randomUUID();
        this.showing = showing;
        this.priceChargedAfterAnyDiscounts = priceChargedAfterAnyDiscounts;

        return ticketId;
    }

    public UUID getTicketId() {
        return ticketId;
    }

    public Money getPriceChargedAfterAnyDiscounts() {
        return priceChargedAfterAnyDiscounts;
    }

    public Showing getShowing() {
        return showing;
    }

}
