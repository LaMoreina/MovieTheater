package com.jpmc.theater.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Theater {

    private List<Showing> schedule;
    private static final int MAXIMUM_SEATING_CAPACITY = 25; //todo: put this in properties.yml
    private static int currentSeatingCapacity = 25; //todo: put this in properties.yml

    public Theater(List<Showing> schedule) {
        this.schedule = schedule;
    }

    public List<Showing> getSchedule() {
        return schedule;
    }

    public void setSchedule(List<Showing> schedule) {
        this.schedule = schedule;
    }

    public static int getCurrentSeatingCapacity() {
        return currentSeatingCapacity;
    }

    public static void setCurrentSeatingCapacity(int currentSeatingCapacity) {
        Theater.currentSeatingCapacity = currentSeatingCapacity;
    }

    public void printSchedule() {
        System.out.println(LocalDate.now());
        System.out.println("===================================================");
        schedule.forEach(s ->
                System.out.println(s.getSequenceOfTheDay() + ": " + s.getStartTime() + " " + s.getMovie().getTitle() + " " + humanReadableFormat(s.getMovie().getRunningTime()) + " $" + s.getMovie().getFullPriceTicket())
        );
        System.out.println("===================================================");
    }

    public String humanReadableFormat(Duration duration) {
        long hour = duration.toHours();
        long remainingMin = duration.toMinutes() - TimeUnit.HOURS.toMinutes(duration.toHours());

        return String.format("(%s hour%s %s minute%s)", hour, handlePlural(hour), remainingMin, handlePlural(remainingMin));
    }

    // (s) postfix should be added to handle plural correctly
    private String handlePlural(long value) {
        if (value == 1) {
            return "";
        }
        else {
            return "s";
        }
    }

    /** I chose to output the JSON to the console and not a separate file
    File file = new File("./schedule.json");
    scheduleJSON = objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, theater.schedule); */
    private void printScheduleJSON() {
//        Theater theater = new Theater(LocalDateProvider.singleton());
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String scheduleJSON = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(this.schedule);
            System.out.println(scheduleJSON);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public void printScheduleWithFormat(String format) {
        if(format.equalsIgnoreCase(ScheduleFormat.Enum.JSON.toString())) {
            printScheduleJSON();
        } else if (format.equalsIgnoreCase(ScheduleFormat.Enum.PLAINTEXT.toString())) {
            printSchedule();
        }
    }

    public static class ScheduleFormat {
        enum Enum {
            JSON, PLAINTEXT
        }
    }

    public static void main(String[] args) {
        CurrencyUnit usd = CurrencyUnit.USD;
        Movie spiderMan = new Movie("Spider-Man: No Way Home", "", Duration.ofMinutes(90), Money.of(usd, 12.5), true);
        Movie turningRed = new Movie("Turning Red","",  Duration.ofMinutes(85), Money.of(usd, 11), false);
        Movie theBatMan = new Movie("The Batman", "", Duration.ofMinutes(95), Money.of(usd, 9), false);
        List<Showing> schedule = new ArrayList<>();
        schedule = List.of(
            new Showing(turningRed, 1, LocalDateTime.of(LocalDate.now(), LocalTime.of(9, 0))),
            new Showing(spiderMan, 2, LocalDateTime.of(LocalDate.now(), LocalTime.of(11, 0))),
            new Showing(theBatMan, 3, LocalDateTime.of(LocalDate.now(), LocalTime.of(12, 50))),
            new Showing(turningRed, 4, LocalDateTime.of(LocalDate.now(), LocalTime.of(14, 30))),
            new Showing(spiderMan, 5, LocalDateTime.of(LocalDate.now(), LocalTime.of(16, 10))),
            new Showing(theBatMan, 6, LocalDateTime.of(LocalDate.now(), LocalTime.of(17, 50))),
            new Showing(turningRed, 7, LocalDateTime.of(LocalDate.now(), LocalTime.of(19, 30))),
            new Showing(spiderMan, 8, LocalDateTime.of(LocalDate.now(), LocalTime.of(21, 10))),
            new Showing(theBatMan, 9, LocalDateTime.of(LocalDate.now(), LocalTime.of(23, 0)))
        );
        Theater theater = new Theater(schedule);

        BufferedReader reader  = new BufferedReader(new InputStreamReader((System.in)));
        System.out.println("Enter desired schedule format (JSON, PLAINTEXT)");
        try {
            String outputFormat = reader.readLine();
            theater.printScheduleWithFormat(outputFormat);
        } catch(IOException e) {
            e.getMessage();
        }
    }
}
