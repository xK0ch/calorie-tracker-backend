package de.fynnkoch.calorietracker.dayentry;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import de.fynnkoch.calorietracker.exception.ResourceNotFoundException;
import de.fynnkoch.calorietracker.meal.Meal;
import de.fynnkoch.calorietracker.meal.MealRepository;
import de.fynnkoch.calorietracker.profile.Profile;
import de.fynnkoch.calorietracker.profile.ProfileService;
import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DayEntryServiceTest {

    @Mock
    private DayEntryRepository dayEntryRepository;

    @Mock
    private MealRepository mealRepository;

    @Mock
    private ProfileService profileService;

    @InjectMocks
    private DayEntryService service;

    private Profile profile;
    private Meal meal;
    private final LocalDate testDate = LocalDate.of(2026, 3, 11);

    @BeforeEach
    void setUp() {
        profile = new Profile("Tom");
        meal = new Meal(profile, "Frühstück");
    }

    @Test
    void addMealToDay_newDay_createsEntryAndAddsMeal() {
        when(profileService.findById(1L)).thenReturn(profile);
        when(mealRepository.findByIdAndProfileId(1L, 1L)).thenReturn(Optional.of(meal));
        when(dayEntryRepository.findByProfileIdAndDate(1L, testDate)).thenReturn(Optional.empty());

        DayEntry newEntry = new DayEntry(profile, testDate);
        when(dayEntryRepository.save(any(DayEntry.class))).thenReturn(newEntry);

        DayEntry result = service.addMealToDay(1L, testDate, 1L);
        assertThat(result.getMeals()).hasSize(1);
    }

    @Test
    void addMealToDay_unknownMeal_throwsException() {
        when(profileService.findById(1L)).thenReturn(profile);
        when(mealRepository.findByIdAndProfileId(99L, 1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.addMealToDay(1L, testDate, 99L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void removeMealFromDay_invalidIndex_throwsException() {
        DayEntry entry = new DayEntry(profile, testDate);
        when(dayEntryRepository.findByProfileIdAndDate(1L, testDate)).thenReturn(Optional.of(entry));

        assertThatThrownBy(() -> service.removeMealFromDay(1L, testDate, 5))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void removeMealFromDay_noEntry_throwsException() {
        when(dayEntryRepository.findByProfileIdAndDate(1L, testDate)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.removeMealFromDay(1L, testDate, 0))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}
