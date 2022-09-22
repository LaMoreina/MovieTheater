package com.jpmc.theater.model;

import org.joda.money.Money;

import java.util.List;
import java.util.UUID;

public class Reservation {

    private UUID reservationId;
    private Customer customer;
    private Showing showing;
    private List<Ticket> tickets;
    private Money calculatedTicketPrice;
    private Money totalFee;

    public Reservation(Customer customer, Showing showing, List<Ticket> tickets, Money calculatedTicketPrice) {
        this.reservationId = UUID.randomUUID();
        this.customer = customer;
        this.showing = showing;
        this.tickets = tickets;
        this.calculatedTicketPrice = calculatedTicketPrice;
        this.totalFee = calculateTotalFee();
    }

    private Money calculateTotalFee() {
        return calculatedTicketPrice.multipliedBy(tickets.size());
    }

    public Money getTotalFee() {
        return totalFee;
    }

    public Customer getCustomer() {
        return customer;
    }

    public List<Ticket> getTickets() {
        return tickets;
    }
}