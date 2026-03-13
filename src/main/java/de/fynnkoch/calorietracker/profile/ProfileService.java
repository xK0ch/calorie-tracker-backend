package de.fynnkoch.calorietracker.profile;

import de.fynnkoch.calorietracker.exception.ResourceNotFoundException;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProfileService {

    private final ProfileRepository repository;

    public ProfileService(ProfileRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<Profile> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Profile findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found: " + id));
    }

    public Profile create(String name) {
        return repository.save(new Profile(name));
    }

    public Profile update(Long id, String name) {
        Profile profile = findById(id);
        profile.setName(name);
        return repository.save(profile);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Profile not found: " + id);
        }
        repository.deleteById(id);
    }

    public Profile updateSettings(Long id, int calorieGoal) {
        Profile profile = findById(id);
        profile.setCalorieGoal(calorieGoal);
        return repository.save(profile);
    }
}
