package de.fynnkoch.calorietracker.profile;

import jakarta.validation.constraints.Min;

public record SettingsDto(
        @Min(0) int calorieGoal
) {}
