package com.jpmc.theater.model;

import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MovieTest {

  @Test
  public void testOverridenEquals_equal() {
    Movie movie1 =
        new Movie(
            "title", "description", Duration.ofMinutes(99), Money.of(CurrencyUnit.USD, 100), true);
    Movie movie2 =
        new Movie(
            "title", "description", Duration.ofMinutes(99), Money.of(CurrencyUnit.USD, 100), true);

    assertTrue(movie1.equals(movie2));
  }

  @Test
  public void testOverridenEquals_notEqual() {
    Movie movie1 =
        new Movie(
            "title", "description1", Duration.ofMinutes(99), Money.of(CurrencyUnit.USD, 100), true);
    Movie movie2 =
        new Movie(
            "title", "description2", Duration.ofMinutes(99), Money.of(CurrencyUnit.USD, 100), true);

    assertFalse(movie1.equals(movie2));
  }
}
