package de.fynnkoch.calorietracker.meal;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MealRepository extends JpaRepository<Meal, Long> {

    @Query("SELECT DISTINCT m FROM Meal m LEFT JOIN FETCH m.ingredients mi LEFT JOIN FETCH mi.ingredient WHERE m.profile.id = :profileId")
    List<Meal> findAllByProfileIdWithIngredients(Long profileId);

    @Query("SELECT m FROM Meal m LEFT JOIN FETCH m.ingredients mi LEFT JOIN FETCH mi.ingredient WHERE m.id = :id AND m.profile.id = :profileId")
    Optional<Meal> findByIdAndProfileIdWithIngredients(Long id, Long profileId);

    Optional<Meal> findByIdAndProfileId(Long id, Long profileId);
}
