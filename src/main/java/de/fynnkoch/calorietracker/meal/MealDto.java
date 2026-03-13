package de.fynnkoch.calorietracker.meal;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import java.util.List;

public record MealDto(
        Long id,
        @NotBlank String name,
        @Valid List<MealIngredientDto> ingredients
) {
    public static MealDto from(Meal meal) {
        return new MealDto(
                meal.getId(),
                meal.getName(),
                meal.getIngredients().stream().map(MealIngredientDto::from).toList()
        );
    }
}
