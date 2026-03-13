package de.fynnkoch.calorietracker.profile;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ProfileDto(
        Long id,
        @NotBlank @Size(max = 100) String name,
        int calorieGoal
) {
    public static ProfileDto from(Profile profile) {
        return new ProfileDto(profile.getId(), profile.getName(), profile.getCalorieGoal());
    }
}
