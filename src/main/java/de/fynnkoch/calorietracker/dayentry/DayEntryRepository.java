package de.fynnkoch.calorietracker.dayentry;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DayEntryRepository extends JpaRepository<DayEntry, Long> {

    @Query("SELECT DISTINCT d FROM DayEntry d LEFT JOIN FETCH d.meals dm LEFT JOIN FETCH dm.meal " +
           "WHERE d.profile.id = :profileId AND d.date BETWEEN :from AND :to")
    List<DayEntry> findByProfileIdAndDateBetween(Long profileId, LocalDate from, LocalDate to);

    @Query("SELECT d FROM DayEntry d LEFT JOIN FETCH d.meals dm LEFT JOIN FETCH dm.meal " +
           "WHERE d.profile.id = :profileId AND d.date = :date")
    Optional<DayEntry> findByProfileIdAndDate(Long profileId, LocalDate date);
}
