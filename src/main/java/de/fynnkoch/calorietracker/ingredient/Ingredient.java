package de.fynnkoch.calorietracker.ingredient;

import de.fynnkoch.calorietracker.profile.Profile;
import jakarta.persistence.*;

@Entity
@Table(name = "ingredient")
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id", nullable = false)
    private Profile profile;

    @Column(nullable = false)
    private String name;

    @Column(name = "reference_amount", nullable = false)
    private double referenceAmount;

    @Column(nullable = false)
    private double calories;

    @Column(nullable = false)
    private double fat;

    @Column(nullable = false)
    private double protein;

    @Column(nullable = false)
    private double carbs;

    protected Ingredient() {}

    public Ingredient(Profile profile, String name, double referenceAmount,
                      double calories, double fat, double protein, double carbs) {
        this.profile = profile;
        this.name = name;
        this.referenceAmount = referenceAmount;
        this.calories = calories;
        this.fat = fat;
        this.protein = protein;
        this.carbs = carbs;
    }

    public Long getId() { return id; }
    public Profile getProfile() { return profile; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public double getReferenceAmount() { return referenceAmount; }
    public void setReferenceAmount(double referenceAmount) { this.referenceAmount = referenceAmount; }
    public double getCalories() { return calories; }
    public void setCalories(double calories) { this.calories = calories; }
    public double getFat() { return fat; }
    public void setFat(double fat) { this.fat = fat; }
    public double getProtein() { return protein; }
    public void setProtein(double protein) { this.protein = protein; }
    public double getCarbs() { return carbs; }
    public void setCarbs(double carbs) { this.carbs = carbs; }
}
