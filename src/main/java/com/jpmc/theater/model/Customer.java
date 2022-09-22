package com.jpmc.theater.model;

import java.util.UUID;

public class Customer {

  private String name;
  private UUID id;

  /** @param name customer name */
  public Customer(String name) {
    this.id = UUID.randomUUID();
    this.name = name;
  }

  public String getName() {
    return name;
  }
}
