package de.fynnkoch.calorietracker.meal;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profiles/{profileId}/meals")
public class MealController {

    private final MealService service;

    public MealController(MealService service) {
        this.service = service;
    }

    @GetMapping
    public List<MealDto> findAll(@PathVariable Long profileId) {
        return service.findAllByProfile(profileId).stream().map(MealDto::from).toList();
    }

    @GetMapping("/{id}")
    public MealDto findById(@PathVariable Long profileId, @PathVariable Long id) {
        return MealDto.from(service.findByIdAndProfile(id, profileId));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MealDto create(@PathVariable Long profileId, @Valid @RequestBody MealDto dto) {
        return MealDto.from(service.create(profileId, dto));
    }

    @PutMapping("/{id}")
    public MealDto update(@PathVariable Long profileId, @PathVariable Long id,
                          @Valid @RequestBody MealDto dto) {
        return MealDto.from(service.update(id, profileId, dto));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long profileId, @PathVariable Long id) {
        service.delete(id, profileId);
    }
}
