package de.fynnkoch.calorietracker.ingredient;

import de.fynnkoch.calorietracker.exception.ResourceNotFoundException;
import de.fynnkoch.calorietracker.profile.Profile;
import de.fynnkoch.calorietracker.profile.ProfileService;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class IngredientService {

    private final IngredientRepository repository;
    private final ProfileService profileService;

    public IngredientService(IngredientRepository repository, ProfileService profileService) {
        this.repository = repository;
        this.profileService = profileService;
    }

    @Transactional(readOnly = true)
    public List<Ingredient> findAllByProfile(Long profileId) {
        return repository.findAllByProfileId(profileId);
    }

    @Transactional(readOnly = true)
    public Ingredient findByIdAndProfile(Long id, Long profileId) {
        return repository.findByIdAndProfileId(id, profileId)
                .orElseThrow(() -> new ResourceNotFoundException("Ingredient not found: " + id));
    }

    public Ingredient create(Long profileId, IngredientDto dto) {
        Profile profile = profileService.findById(profileId);
        Ingredient ingredient = new Ingredient(
                profile, dto.name(), dto.referenceAmount(),
                dto.calories(), dto.fat(), dto.protein(), dto.carbs()
        );
        return repository.save(ingredient);
    }

    public Ingredient update(Long id, Long profileId, IngredientDto dto) {
        Ingredient ingredient = findByIdAndProfile(id, profileId);
        ingredient.setName(dto.name());
        ingredient.setReferenceAmount(dto.referenceAmount());
        ingredient.setCalories(dto.calories());
        ingredient.setFat(dto.fat());
        ingredient.setProtein(dto.protein());
        ingredient.setCarbs(dto.carbs());
        return repository.save(ingredient);
    }

    public void delete(Long id, Long profileId) {
        Ingredient ingredient = findByIdAndProfile(id, profileId);
        repository.delete(ingredient);
    }
}
