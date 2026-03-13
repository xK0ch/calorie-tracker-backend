package de.fynnkoch.calorietracker.profile;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profiles")
public class ProfileController {

    private final ProfileService service;

    public ProfileController(ProfileService service) {
        this.service = service;
    }

    @GetMapping
    public List<ProfileDto> findAll() {
        return service.findAll().stream().map(ProfileDto::from).toList();
    }

    @GetMapping("/{id}")
    public ProfileDto findById(@PathVariable Long id) {
        return ProfileDto.from(service.findById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProfileDto create(@Valid @RequestBody ProfileDto dto) {
        return ProfileDto.from(service.create(dto.name()));
    }

    @PutMapping("/{id}")
    public ProfileDto update(@PathVariable Long id, @Valid @RequestBody ProfileDto dto) {
        return ProfileDto.from(service.update(id, dto.name()));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    @GetMapping("/{id}/settings")
    public SettingsDto getSettings(@PathVariable Long id) {
        Profile profile = service.findById(id);
        return new SettingsDto(profile.getCalorieGoal());
    }

    @PutMapping("/{id}/settings")
    public SettingsDto updateSettings(@PathVariable Long id, @Valid @RequestBody SettingsDto dto) {
        Profile profile = service.updateSettings(id, dto.calorieGoal());
        return new SettingsDto(profile.getCalorieGoal());
    }
}
