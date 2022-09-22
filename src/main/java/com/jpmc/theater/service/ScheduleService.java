package com.jpmc.theater.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jpmc.theater.model.Theater;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Duration;
import java.time.LocalDate;
import java.util.concurrent.TimeUnit;

public class ScheduleService {

  private Theater theater;

  public ScheduleService(Theater theater) {
    this.theater = theater;
  }

  public void getConsoleInputForPrintingSchedule() {
    BufferedReader reader = new BufferedReader(new InputStreamReader((System.in)));
    System.out.println("Enter desired schedule format (JSON, PLAINTEXT)");
    try {
      String outputFormat = reader.readLine();
      this.printScheduleWithFormat(outputFormat);
    } catch (IOException e) {
      e.getMessage();
    }
  }

  public void printScheduleWithFormat(String format) {
    if (null != format) {
      if (format.equalsIgnoreCase(ScheduleFormat.Enum.JSON.toString())) {
        printScheduleJSON();
      } else if (format.equalsIgnoreCase(ScheduleFormat.Enum.PLAINTEXT.toString())) {
        printPlainTextScheduleToConsole();
      } else if (!format.equalsIgnoreCase(ScheduleFormat.Enum.JSON.toString())
          && !format.equalsIgnoreCase(ScheduleFormat.Enum.PLAINTEXT.toString())) {
        System.out.println("This is not a valid format");
      }
    } else {
      System.out.println("format input is null");
    }
  }

  private void printPlainTextScheduleToConsole() {
    System.out.println(LocalDate.now());
    System.out.println("===================================================");
    try {
      theater
          .getSchedule()
          .forEach(
              s ->
                  System.out.println(
                      s.getSequenceOfTheDay()
                          + ": "
                          + s.getStartTime()
                          + " "
                          + s.getMovie().getTitle()
                          + " "
                          + humanReadableFormat(s.getMovie().getRunningTime())
                          + " $"
                          + s.getMovie().getFullPriceTicket()));
    } catch (NullPointerException npe) {
      System.out.println(npe.getMessage());
    }
    System.out.println("===================================================");
  }

  private String humanReadableFormat(Duration duration) {
    long hour = duration.toHours();
    long remainingMin = duration.toMinutes() - TimeUnit.HOURS.toMinutes(duration.toHours());

    return String.format(
        "(%s hour%s %s minute%s)",
        hour, handlePlural(hour), remainingMin, handlePlural(remainingMin));
  }

  // (s) postfix should be added to handle plural correctly
  private String handlePlural(long value) {
    if (value == 1) {
      return "";
    } else {
      return "s";
    }
  }

  /**
   * I chose to output the JSON to the console and not a separate file as the previous plain text
   * was put there
   */
  private void printScheduleJSON() {
    ObjectMapper objectMapper = new ObjectMapper();
    try {
      String scheduleJSON =
          objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(theater.getSchedule());
      System.out.println(scheduleJSON);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
  }

  private static class ScheduleFormat {
    enum Enum {
      JSON,
      PLAINTEXT
    }
  }
}
