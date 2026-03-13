package de.fynnkoch.calorietracker.ingredient;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record IngredientDto(
        Long id,
        @NotBlank String name,
        @Positive double referenceAmount,
        double calories,
        double fat,
        double protein,
        double carbs
) {
    public static IngredientDto from(Ingredient ingredient) {
        return new IngredientDto(
                ingredient.getId(),
                ingredient.getName(),
                ingredient.getReferenceAmount(),
                ingredient.getCalories(),
                ingredient.getFat(),
                ingredient.getProtein(),
                ingredient.getCarbs()
        );
    }
}
