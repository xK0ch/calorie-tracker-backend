package de.fynnkoch.calorietracker.dayentry;

import de.fynnkoch.calorietracker.exception.ResourceNotFoundException;
import de.fynnkoch.calorietracker.meal.Meal;
import de.fynnkoch.calorietracker.meal.MealRepository;
import de.fynnkoch.calorietracker.profile.Profile;
import de.fynnkoch.calorietracker.profile.ProfileService;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DayEntryService {

    private final DayEntryRepository dayEntryRepository;
    private final MealRepository mealRepository;
    private final ProfileService profileService;

    public DayEntryService(DayEntryRepository dayEntryRepository, MealRepository mealRepository,
                           ProfileService profileService) {
        this.dayEntryRepository = dayEntryRepository;
        this.mealRepository = mealRepository;
        this.profileService = profileService;
    }

    @Transactional(readOnly = true)
    public List<DayEntry> findByDateRange(Long profileId, LocalDate from, LocalDate to) {
        return dayEntryRepository.findByProfileIdAndDateBetween(profileId, from, to);
    }

    @Transactional(readOnly = true)
    public DayEntry findByDate(Long profileId, LocalDate date) {
        return dayEntryRepository.findByProfileIdAndDate(profileId, date).orElse(null);
    }

    public DayEntry addMealToDay(Long profileId, LocalDate date, Long mealId) {
        Profile profile = profileService.findById(profileId);
        Meal meal = mealRepository.findByIdAndProfileId(mealId, profileId)
                .orElseThrow(() -> new ResourceNotFoundException("Meal not found: " + mealId));

        DayEntry entry = dayEntryRepository.findByProfileIdAndDate(profileId, date)
                .orElseGet(() -> dayEntryRepository.save(new DayEntry(profile, date)));

        int nextOrder = entry.getMeals().size();
        entry.getMeals().add(new DayEntryMeal(entry, meal, nextOrder));
        return dayEntryRepository.save(entry);
    }

    public void removeMealFromDay(Long profileId, LocalDate date, int index) {
        DayEntry entry = dayEntryRepository.findByProfileIdAndDate(profileId, date)
                .orElseThrow(() -> new ResourceNotFoundException("Day entry not found for date: " + date));

        List<DayEntryMeal> meals = entry.getMeals();
        if (index < 0 || index >= meals.size()) {
            throw new ResourceNotFoundException("Meal index out of range: " + index);
        }

        meals.remove(index);
        for (int i = 0; i < meals.size(); i++) {
            meals.get(i).setSortOrder(i);
        }

        if (meals.isEmpty()) {
            dayEntryRepository.delete(entry);
        } else {
            dayEntryRepository.save(entry);
        }
    }
}
