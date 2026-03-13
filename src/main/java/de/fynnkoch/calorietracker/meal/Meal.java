package de.fynnkoch.calorietracker.meal;

import de.fynnkoch.calorietracker.profile.Profile;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "meal")
public class Meal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id", nullable = false)
    private Profile profile;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "meal", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MealIngredient> ingredients = new ArrayList<>();

    protected Meal() {}

    public Meal(Profile profile, String name) {
        this.profile = profile;
        this.name = name;
    }

    public Long getId() { return id; }
    public Profile getProfile() { return profile; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public List<MealIngredient> getIngredients() { return ingredients; }
}
