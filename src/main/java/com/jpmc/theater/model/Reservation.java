package com.jpmc.theater.model;

import org.joda.money.Money;

import java.util.List;
import java.util.UUID;

//todo: use lombok to simplify POJO classes
//todo: checkout jackson version of UUID and compare other ID generating libraries
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

    public UUID getReservationId() {
        return reservationId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Showing getShowing() {
        return showing;
    }

    public void setShowing(Showing showing) {
        this.showing = showing;
    }

    public List<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
    }
}