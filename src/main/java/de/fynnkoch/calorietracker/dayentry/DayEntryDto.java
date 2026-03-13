package de.fynnkoch.calorietracker.dayentry;

import java.time.LocalDate;
import java.util.List;

public record DayEntryDto(
        LocalDate date,
        List<Long> mealIds
) {
    public static DayEntryDto from(DayEntry entry) {
        return new DayEntryDto(
                entry.getDate(),
                entry.getMeals().stream().map(dm -> dm.getMeal().getId()).toList()
        );
    }
}
