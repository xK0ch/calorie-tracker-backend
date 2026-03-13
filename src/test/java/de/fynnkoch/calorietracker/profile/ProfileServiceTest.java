package de.fynnkoch.calorietracker.profile;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import de.fynnkoch.calorietracker.exception.ResourceNotFoundException;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProfileServiceTest {

    @Mock
    private ProfileRepository repository;

    @InjectMocks
    private ProfileService service;

    private Profile testProfile;

    @BeforeEach
    void setUp() {
        testProfile = new Profile("Tom");
    }

    @Test
    void findAll_returnsAllProfiles() {
        when(repository.findAll()).thenReturn(List.of(testProfile));
        List<Profile> result = service.findAll();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Tom");
    }

    @Test
    void findById_existingProfile_returnsProfile() {
        when(repository.findById(1L)).thenReturn(Optional.of(testProfile));
        Profile result = service.findById(1L);
        assertThat(result.getName()).isEqualTo("Tom");
    }

    @Test
    void findById_nonExistingProfile_throwsException() {
        when(repository.findById(99L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.findById(99L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void create_savesProfile() {
        when(repository.save(any(Profile.class))).thenReturn(testProfile);
        Profile result = service.create("Tom");
        assertThat(result.getName()).isEqualTo("Tom");
        verify(repository).save(any(Profile.class));
    }

    @Test
    void update_existingProfile_updatesName() {
        when(repository.findById(1L)).thenReturn(Optional.of(testProfile));
        when(repository.save(any(Profile.class))).thenReturn(testProfile);
        service.update(1L, "Thomas");
        assertThat(testProfile.getName()).isEqualTo("Thomas");
    }

    @Test
    void delete_nonExistingProfile_throwsException() {
        when(repository.existsById(99L)).thenReturn(false);
        assertThatThrownBy(() -> service.delete(99L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void updateSettings_updatesCalorieGoal() {
        when(repository.findById(1L)).thenReturn(Optional.of(testProfile));
        when(repository.save(any(Profile.class))).thenReturn(testProfile);
        service.updateSettings(1L, 1800);
        assertThat(testProfile.getCalorieGoal()).isEqualTo(1800);
    }
}
