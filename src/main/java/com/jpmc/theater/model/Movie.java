package com.jpmc.theater.model;

import org.javamoney.moneta.Money;

import java.time.Duration;
import java.util.Objects;

public class Movie {

    private String title;
    private String description;
    private Duration runningTime;
    private Money ticketPrice; //todo: have this set up in properties.yml
    private boolean specialCode;  //I personally don't feel this is where specialCode belongs, but to change it means I am changing biz logic

    public Movie(String title, String description, Duration runningTime, Money ticketPrice, boolean specialCode) {
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

    public Money getTicketPrice() {
        return ticketPrice;
    }

    public boolean hasSpecialCode() {
        return specialCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Movie movie = (Movie) o;
        return (movie.ticketPrice.compareTo(ticketPrice)==0)
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