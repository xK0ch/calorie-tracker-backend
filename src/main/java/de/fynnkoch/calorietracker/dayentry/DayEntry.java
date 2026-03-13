package de.fynnkoch.calorietracker.dayentry;

import de.fynnkoch.calorietracker.profile.Profile;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "day_entry", uniqueConstraints = @UniqueConstraint(columnNames = {"profile_id", "date"}))
public class DayEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id", nullable = false)
    private Profile profile;

    @Column(nullable = false)
    private LocalDate date;

    @OneToMany(mappedBy = "dayEntry", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("sortOrder ASC")
    private List<DayEntryMeal> meals = new ArrayList<>();

    protected DayEntry() {}

    public DayEntry(Profile profile, LocalDate date) {
        this.profile = profile;
        this.date = date;
    }

    public Long getId() { return id; }
    public Profile getProfile() { return profile; }
    public LocalDate getDate() { return date; }
    public List<DayEntryMeal> getMeals() { return meals; }
}
