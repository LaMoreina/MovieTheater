package com.jpmc.theater.model;

import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.junit.jupiter.api.Test;

import java.time.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReservationTest {

    private Reservation reservation;

    @Test
    public void testGetTotalFeeCalculateCorrectTotalFeeBasedOnNumberOfTickets() {

        Money testTicketFullPrice = Money.of(CurrencyUnit.USD, 50);
        List<Ticket> threeTickets = List.of(
                new Ticket(null, testTicketFullPrice),
                new Ticket(null, testTicketFullPrice),
                new Ticket(null, testTicketFullPrice));

        reservation = new Reservation(new Customer("Joan Jett"), null, threeTickets, testTicketFullPrice);

        assertEquals(testTicketFullPrice.multipliedBy(threeTickets.size()), reservation.getTotalFee());

    }
}
