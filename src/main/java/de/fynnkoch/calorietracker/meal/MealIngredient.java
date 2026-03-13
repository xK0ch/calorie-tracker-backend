package de.fynnkoch.calorietracker.meal;

import de.fynnkoch.calorietracker.ingredient.Ingredient;
import jakarta.persistence.*;

@Entity
@Table(name = "meal_ingredient")
public class MealIngredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meal_id", nullable = false)
    private Meal meal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ingredient_id", nullable = false)
    private Ingredient ingredient;

    @Column(nullable = false)
    private double amount;

    protected MealIngredient() {}

    public MealIngredient(Meal meal, Ingredient ingredient, double amount) {
        this.meal = meal;
        this.ingredient = ingredient;
        this.amount = amount;
    }

    public Long getId() { return id; }
    public Meal getMeal() { return meal; }
    public Ingredient getIngredient() { return ingredient; }
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
}
