package de.fynnkoch.calorietracker.ingredient;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import de.fynnkoch.calorietracker.exception.ResourceNotFoundException;
import de.fynnkoch.calorietracker.profile.Profile;
import de.fynnkoch.calorietracker.profile.ProfileService;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class IngredientServiceTest {

    @Mock
    private IngredientRepository repository;

    @Mock
    private ProfileService profileService;

    @InjectMocks
    private IngredientService service;

    private Profile profile;
    private Ingredient ingredient;

    @BeforeEach
    void setUp() {
        profile = new Profile("Tom");
        ingredient = new Ingredient(profile, "Milch", 100, 58, 3.5, 3.4, 4.7);
    }

    @Test
    void findAllByProfile_returnsList() {
        when(repository.findAllByProfileId(1L)).thenReturn(List.of(ingredient));
        List<Ingredient> result = service.findAllByProfile(1L);
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Milch");
    }

    @Test
    void findByIdAndProfile_notFound_throwsException() {
        when(repository.findByIdAndProfileId(99L, 1L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.findByIdAndProfile(99L, 1L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void create_savesIngredient() {
        when(profileService.findById(1L)).thenReturn(profile);
        when(repository.save(any(Ingredient.class))).thenReturn(ingredient);

        IngredientDto dto = new IngredientDto(null, "Milch", 100, 58, 3.5, 3.4, 4.7);
        Ingredient result = service.create(1L, dto);
        assertThat(result.getName()).isEqualTo("Milch");
        assertThat(result.getCalories()).isEqualTo(58);
    }

    @Test
    void update_existingIngredient_updatesFields() {
        when(repository.findByIdAndProfileId(1L, 1L)).thenReturn(Optional.of(ingredient));
        when(repository.save(any(Ingredient.class))).thenReturn(ingredient);

        IngredientDto dto = new IngredientDto(null, "Vollmilch", 100, 64, 3.5, 3.3, 4.8);
        service.update(1L, 1L, dto);
        assertThat(ingredient.getName()).isEqualTo("Vollmilch");
        assertThat(ingredient.getCalories()).isEqualTo(64);
    }

    @Test
    void delete_existingIngredient_deletesIt() {
        when(repository.findByIdAndProfileId(1L, 1L)).thenReturn(Optional.of(ingredient));
        service.delete(1L, 1L);
        verify(repository).delete(ingredient);
    }
}
