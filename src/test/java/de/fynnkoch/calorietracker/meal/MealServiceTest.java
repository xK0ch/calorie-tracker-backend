package de.fynnkoch.calorietracker.meal;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import de.fynnkoch.calorietracker.exception.ResourceNotFoundException;
import de.fynnkoch.calorietracker.ingredient.Ingredient;
import de.fynnkoch.calorietracker.ingredient.IngredientRepository;
import de.fynnkoch.calorietracker.profile.Profile;
import de.fynnkoch.calorietracker.profile.ProfileService;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MealServiceTest {

    @Mock
    private MealRepository mealRepository;

    @Mock
    private IngredientRepository ingredientRepository;

    @Mock
    private ProfileService profileService;

    @InjectMocks
    private MealService service;

    private Profile profile;
    private Ingredient ingredient;

    @BeforeEach
    void setUp() {
        profile = new Profile("Tom");
        ingredient = new Ingredient(profile, "Reis", 100, 130, 0.3, 2.7, 28);
    }

    @Test
    void create_withIngredients_savesMeal() {
        when(profileService.findById(1L)).thenReturn(profile);
        when(ingredientRepository.findByIdAndProfileId(1L, 1L)).thenReturn(Optional.of(ingredient));
        when(mealRepository.save(any(Meal.class))).thenAnswer(inv -> inv.getArgument(0));

        MealDto dto = new MealDto(null, "Reispfanne", List.of(new MealIngredientDto(1L, 200)));
        Meal result = service.create(1L, dto);

        assertThat(result.getName()).isEqualTo("Reispfanne");
        assertThat(result.getIngredients()).hasSize(1);
        assertThat(result.getIngredients().get(0).getAmount()).isEqualTo(200);
    }

    @Test
    void create_withUnknownIngredient_throwsException() {
        when(profileService.findById(1L)).thenReturn(profile);
        when(ingredientRepository.findByIdAndProfileId(99L, 1L)).thenReturn(Optional.empty());

        MealDto dto = new MealDto(null, "Test", List.of(new MealIngredientDto(99L, 100)));
        assertThatThrownBy(() -> service.create(1L, dto))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void delete_existingMeal_deletesIt() {
        Meal meal = new Meal(profile, "Test");
        when(mealRepository.findByIdAndProfileId(1L, 1L)).thenReturn(Optional.of(meal));
        service.delete(1L, 1L);
        verify(mealRepository).delete(meal);
    }

    @Test
    void delete_nonExistingMeal_throwsException() {
        when(mealRepository.findByIdAndProfileId(99L, 1L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.delete(99L, 1L))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}
