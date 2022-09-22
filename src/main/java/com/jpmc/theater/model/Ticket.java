package com.jpmc.theater.model;

import org.joda.money.Money;

import java.util.UUID;


public class Ticket {

    private UUID ticketId;
    private Showing showing;
    private Money priceChargedAfterAnyDiscounts;

    public Ticket(Showing showing, Money priceChargedAfterAnyDiscounts) {
        this.ticketId = UUID.randomUUID();
        this.showing = showing;
        this.priceChargedAfterAnyDiscounts = priceChargedAfterAnyDiscounts;
    }

}
