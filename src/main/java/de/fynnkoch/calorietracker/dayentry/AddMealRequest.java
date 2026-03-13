package de.fynnkoch.calorietracker.dayentry;

import jakarta.validation.constraints.NotNull;

public record AddMealRequest(
        @NotNull Long mealId
) {}
