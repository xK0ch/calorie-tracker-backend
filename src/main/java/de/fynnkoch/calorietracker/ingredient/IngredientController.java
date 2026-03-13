package de.fynnkoch.calorietracker.ingredient;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profiles/{profileId}/ingredients")
public class IngredientController {

    private final IngredientService service;

    public IngredientController(IngredientService service) {
        this.service = service;
    }

    @GetMapping
    public List<IngredientDto> findAll(@PathVariable Long profileId) {
        return service.findAllByProfile(profileId).stream().map(IngredientDto::from).toList();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public IngredientDto create(@PathVariable Long profileId, @Valid @RequestBody IngredientDto dto) {
        return IngredientDto.from(service.create(profileId, dto));
    }

    @PutMapping("/{id}")
    public IngredientDto update(@PathVariable Long profileId, @PathVariable Long id,
                                @Valid @RequestBody IngredientDto dto) {
        return IngredientDto.from(service.update(id, profileId, dto));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long profileId, @PathVariable Long id) {
        service.delete(id, profileId);
    }
}
