package com.jpmc.theater.model;

import org.joda.money.Money;

import java.time.Duration;
import java.util.Objects;

public class Movie {

  private String title;
  private String description;
  private Duration runningTime;
  private Money fullPriceTicket; // todo: have this set up in properties.yml
  private boolean specialCode;

  public Movie(
      String title,
      String description,
      Duration runningTime,
      Money fullPriceTicket,
      boolean specialCode) {
    this.title = title;
    this.description = description;
    this.runningTime = runningTime;
    this.fullPriceTicket = fullPriceTicket;
    this.specialCode = specialCode;
  }

  public String getTitle() {
    return title;
  }

  public Duration getRunningTime() {
    return runningTime;
  }

  public Money getFullPriceTicket() {
    return fullPriceTicket;
  }

  public boolean hasSpecialCode() {
    return specialCode;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Movie movie = (Movie) o;
    return (movie.fullPriceTicket.compareTo(fullPriceTicket) == 0)
        && Objects.equals(title, movie.title)
        && Objects.equals(description, movie.description)
        && Objects.equals(runningTime, movie.runningTime)
        && Objects.equals(specialCode, movie.specialCode);
  }
}
