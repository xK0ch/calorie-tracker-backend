package de.fynnkoch.calorietracker.dayentry;

import de.fynnkoch.calorietracker.meal.Meal;
import jakarta.persistence.*;

@Entity
@Table(name = "day_entry_meal")
public class DayEntryMeal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "day_entry_id", nullable = false)
    private DayEntry dayEntry;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meal_id", nullable = false)
    private Meal meal;

    @Column(name = "sort_order", nullable = false)
    private int sortOrder;

    protected DayEntryMeal() {}

    public DayEntryMeal(DayEntry dayEntry, Meal meal, int sortOrder) {
        this.dayEntry = dayEntry;
        this.meal = meal;
        this.sortOrder = sortOrder;
    }

    public Long getId() { return id; }
    public DayEntry getDayEntry() { return dayEntry; }
    public Meal getMeal() { return meal; }
    public int getSortOrder() { return sortOrder; }
    public void setSortOrder(int sortOrder) { this.sortOrder = sortOrder; }
}
