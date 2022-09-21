package com.jpmc.theater.model;

import org.joda.money.Money;

import java.util.UUID;


public class Ticket {

    private UUID ticketId;
    private Money priceChargedAfterAnyDiscounts;
    private Showing showing;

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

//    private Money convertToDollars(double priceChargedAfterAnyDiscounts) {
//        CurrencyUnit usd = Monetary.getCurrency("USD");
//        return Money.of(priceChargedAfterAnyDiscounts, usd);
//    }
}
