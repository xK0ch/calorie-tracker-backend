package de.fynnkoch.calorietracker.ingredient;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
    List<Ingredient> findAllByProfileId(Long profileId);
    Optional<Ingredient> findByIdAndProfileId(Long id, Long profileId);
}
