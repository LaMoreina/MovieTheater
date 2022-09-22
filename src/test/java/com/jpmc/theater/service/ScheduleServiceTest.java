package com.jpmc.theater.service;

import com.jpmc.theater.model.Theater;
import com.jpmc.theater.utils.TestUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

class ScheduleServiceTest {

    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    private TestUtils testUtils = new TestUtils();
    private Theater theater = testUtils.setUpTheater();
    private ScheduleService scheduleService = new ScheduleService(theater);
    private String jsonSchedule = testUtils.setJsonSchedule();
    private String plaintextSchedule = testUtils.setPlaintextSchedule();

    //todo: fix I/O issues for cleaner code
    //private File file = FileUtils.getFile("./resources/jsonScheduleOutput");
    //assertEquals(FileUtils.readFileToString(file, "utf-8"), outputStreamCaptor.toString().trim());

    @BeforeEach
    public void setUp() {
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @AfterEach
    public void tearDown() {
        System.setOut(standardOut);
    }

    @Test
    void printScheduleWithFormat_invalidFormatEntered_correctConsoleOutput() {
        scheduleService.printScheduleWithFormat("invalid format");
        assertEquals("This is not a valid format", outputStreamCaptor.toString()
                .trim());
    }
    @Test
    void printScheduleWithFormat_JSONFormatEntered_correctConsoleOutput() {
        scheduleService.printScheduleWithFormat("JSON");
        assertEquals(jsonSchedule, outputStreamCaptor.toString()
                .trim());
    }
    @Test
    void printScheduleWithFormat_plaintextFormatEntered_correctConsoleOutput() {
        scheduleService.printScheduleWithFormat("pLaInTeXt");
        assertEquals(plaintextSchedule, outputStreamCaptor.toString()
                .trim());

    }

}