package com.jpmc.theater.model;

import java.util.List;

public class Theater {

    private List<Showing> schedule;
    private static final int MAXIMUM_SEATING_CAPACITY = 25; //todo: put this in properties.yml
    private static int currentSeatingCapacity = MAXIMUM_SEATING_CAPACITY; //todo: put this in properties.yml

    public Theater(List<Showing> schedule) {
        this.schedule = schedule;
    }

    public List<Showing> getSchedule() {
        return schedule;
    }

    public static int getCurrentSeatingCapacity() {
        return currentSeatingCapacity;
    }

    public static void setCurrentSeatingCapacity(int currentSeatingCapacity) {
        Theater.currentSeatingCapacity = currentSeatingCapacity;
    }

}
