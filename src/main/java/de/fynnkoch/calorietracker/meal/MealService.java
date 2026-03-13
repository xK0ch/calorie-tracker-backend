package de.fynnkoch.calorietracker.meal;

import de.fynnkoch.calorietracker.exception.ResourceNotFoundException;
import de.fynnkoch.calorietracker.ingredient.Ingredient;
import de.fynnkoch.calorietracker.ingredient.IngredientRepository;
import de.fynnkoch.calorietracker.profile.Profile;
import de.fynnkoch.calorietracker.profile.ProfileService;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MealService {

    private final MealRepository mealRepository;
    private final IngredientRepository ingredientRepository;
    private final ProfileService profileService;

    public MealService(MealRepository mealRepository, IngredientRepository ingredientRepository,
                       ProfileService profileService) {
        this.mealRepository = mealRepository;
        this.ingredientRepository = ingredientRepository;
        this.profileService = profileService;
    }

    @Transactional(readOnly = true)
    public List<Meal> findAllByProfile(Long profileId) {
        return mealRepository.findAllByProfileIdWithIngredients(profileId);
    }

    @Transactional(readOnly = true)
    public Meal findByIdAndProfile(Long id, Long profileId) {
        return mealRepository.findByIdAndProfileIdWithIngredients(id, profileId)
                .orElseThrow(() -> new ResourceNotFoundException("Meal not found: " + id));
    }

    public Meal create(Long profileId, MealDto dto) {
        Profile profile = profileService.findById(profileId);
        Meal meal = new Meal(profile, dto.name());
        addIngredients(meal, profileId, dto.ingredients());
        return mealRepository.save(meal);
    }

    public Meal update(Long id, Long profileId, MealDto dto) {
        Meal meal = mealRepository.findByIdAndProfileId(id, profileId)
                .orElseThrow(() -> new ResourceNotFoundException("Meal not found: " + id));
        meal.setName(dto.name());
        meal.getIngredients().clear();
        addIngredients(meal, profileId, dto.ingredients());
        return mealRepository.save(meal);
    }

    public void delete(Long id, Long profileId) {
        Meal meal = mealRepository.findByIdAndProfileId(id, profileId)
                .orElseThrow(() -> new ResourceNotFoundException("Meal not found: " + id));
        mealRepository.delete(meal);
    }

    private void addIngredients(Meal meal, Long profileId, List<MealIngredientDto> dtos) {
        if (dtos == null) return;
        for (MealIngredientDto dto : dtos) {
            Ingredient ingredient = ingredientRepository.findByIdAndProfileId(dto.ingredientId(), profileId)
                    .orElseThrow(() -> new ResourceNotFoundException("Ingredient not found: " + dto.ingredientId()));
            meal.getIngredients().add(new MealIngredient(meal, ingredient, dto.amount()));
        }
    }
}
