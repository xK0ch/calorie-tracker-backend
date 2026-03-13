package de.fynnkoch.calorietracker.dayentry;

import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profiles/{profileId}/days")
public class DayEntryController {

    private final DayEntryService service;

    public DayEntryController(DayEntryService service) {
        this.service = service;
    }

    @GetMapping
    public List<DayEntryDto> findByDateRange(
            @PathVariable Long profileId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return service.findByDateRange(profileId, from, to).stream()
                .map(DayEntryDto::from).toList();
    }

    @GetMapping("/{date}")
    public DayEntryDto findByDate(
            @PathVariable Long profileId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        DayEntry entry = service.findByDate(profileId, date);
        if (entry == null) {
            return new DayEntryDto(date, Collections.emptyList());
        }
        return DayEntryDto.from(entry);
    }

    @PostMapping("/{date}/meals")
    @ResponseStatus(HttpStatus.CREATED)
    public DayEntryDto addMeal(
            @PathVariable Long profileId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @Valid @RequestBody AddMealRequest request) {
        return DayEntryDto.from(service.addMealToDay(profileId, date, request.mealId()));
    }

    @DeleteMapping("/{date}/meals/{index}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeMeal(
            @PathVariable Long profileId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @PathVariable int index) {
        service.removeMealFromDay(profileId, date, index);
    }
}
