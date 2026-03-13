package de.fynnkoch.calorietracker.meal;

import jakarta.validation.constraints.Positive;

public record MealIngredientDto(
        Long ingredientId,
        @Positive double amount
) {
    public static MealIngredientDto from(MealIngredient mi) {
        return new MealIngredientDto(mi.getIngredient().getId(), mi.getAmount());
    }
}
