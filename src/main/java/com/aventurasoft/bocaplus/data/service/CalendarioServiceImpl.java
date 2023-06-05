package com.aventurasoft.bocaplus.data.service;

import lombok.extern.log4j.Log4j2;
import org.dmfs.rfc5545.DateTime;
import org.dmfs.rfc5545.recur.InvalidRecurrenceRuleException;
import org.dmfs.rfc5545.recur.RecurrenceRule;
import org.dmfs.rfc5545.recur.RecurrenceRuleIterator;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@Log4j2
public class CalendarioServiceImpl implements CalendarioService {
    @Override
    public boolean isDatePositive(LocalDate checkDate, String rruleExpression, LocalDate startDate, LocalDate endDate) {
        try {

            RecurrenceRule rule = new RecurrenceRule(rruleExpression);


            DateTime startDateTime;
            DateTime endDateTime;

            if (startDate != null)
                startDateTime = getDateTimeFromLocalDate(startDate);
            else
                startDateTime = getDateTimeFromLocalDate(LocalDate.now());

            if (endDate != null)
                endDateTime = getDateTimeFromLocalDate(endDate);
            else
                endDateTime = null;


            DateTime targetDateTime = getDateTimeFromLocalDate(checkDate);

            if (endDateTime != null && endDateTime.before(targetDateTime))
                return false;

            boolean inPeriod = (startDateTime.before(targetDateTime) );

            RecurrenceRuleIterator iterator = rule.iterator(startDateTime);
            while (iterator.hasNext() && inPeriod)
            {
                DateTime instance = iterator.nextDateTime();
                LocalDate instanceDate = LocalDate.of(instance.getYear(), instance.getMonth()+1, instance.getDayOfMonth());
                if (instanceDate.isEqual(checkDate)) return true;
                inPeriod = (instance.before(targetDateTime));
            }
            return false;
        } catch ( InvalidRecurrenceRuleException e)
        {
            log.error(e.getMessage());
            return false;
        }

    }

    private DateTime getDateTimeFromLocalDate(LocalDate localDate)
    {
        return new DateTime(localDate.getYear(), localDate.getMonthValue()-1, localDate.getDayOfMonth());
    }




}
