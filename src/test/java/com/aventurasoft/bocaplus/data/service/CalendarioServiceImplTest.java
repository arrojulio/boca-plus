package com.aventurasoft.bocaplus.data.service;


import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Log4j2
public class CalendarioServiceImplTest {

    @Autowired
    CalendarioService calendarioService;

    @Test
    public void testIsDatePositive() {
        LocalDate checkDate = LocalDate.of(2020, 12,8);
        LocalDate startDate = LocalDate.of(2020,12,1);
        String rrule = "FREQ=WEEKLY;BYDAY=MO,TU,WE;INTERVAL=1";

        boolean result = calendarioService.isDatePositive(checkDate, rrule, startDate, null);

        assertTrue(result);


    }

    @Test
    public void testYearlyOnMultipleMonth()
    {
        String rrule = "FREQ=YEARLY;BYMONTH=11,12;BYMONTHDAY=8,9";
        LocalDate startDate = LocalDate.of(2020,11,1);

        List<LocalDate> checkDates = new ArrayList<>();
        checkDates.add(LocalDate.of(2020,11,8));
        checkDates.add(LocalDate.of(2020,12,8));
        checkDates.add(LocalDate.of(2020,12,9));


        boolean result = true;
        for (LocalDate checkDate : checkDates) {
            log.info("test: " + checkDate + " on " + rrule);
            assertTrue(calendarioService.isDatePositive(checkDate, rrule, startDate, null));
        }

        List<LocalDate> checkFalseDates = new ArrayList<>();
        checkFalseDates.add(LocalDate.of(2020,12,7));

        for (LocalDate checkDate : checkFalseDates) {
            log.info("test false: " + checkDate + " on " + rrule);
            assertFalse(calendarioService.isDatePositive(checkDate, rrule, startDate, null));
        }
    }

    @Test
    public void testAllDecember()
    {
        String rrule = "FREQ=YEARLY;BYDAY=SU;BYSETPOS=;BYMONTH=12";
        LocalDate startDate = LocalDate.of(2020,12,1);
        LocalDate iterator = LocalDate.of(2020,12,1);
        LocalDate endDate = LocalDate.of(2021,1,1);
        log.info("Test rule: " + rrule);
        while (iterator.isBefore(endDate))
        {
            log.info("Test " + iterator + " : " + calendarioService.isDatePositive(iterator, rrule, startDate, null));
            iterator= iterator.plusDays(1);
        }

        assertTrue(true);

    }
}