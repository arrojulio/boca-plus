package com.aventurasoft.bocaplus.data.service;

import java.time.LocalDate;

public interface CalendarioService {
    boolean isDatePositive(LocalDate checkDate, String rruleExpression, LocalDate startDate, LocalDate endDate);


}
