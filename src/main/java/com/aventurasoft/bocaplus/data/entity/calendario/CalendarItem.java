package com.aventurasoft.bocaplus.data.entity.calendario;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CalendarItem {
    private String id;
    private String name;

    public CalendarItem(int id, String name)
    {
        this.id = String.valueOf(id);
        this.name = name;
    }
}


