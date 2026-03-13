CREATE TABLE profile (
    id            BIGSERIAL PRIMARY KEY,
    name          VARCHAR(100) NOT NULL UNIQUE,
    calorie_goal  INTEGER NOT NULL DEFAULT 2000,
    created_at    TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE ingredient (
    id               BIGSERIAL PRIMARY KEY,
    profile_id       BIGINT NOT NULL REFERENCES profile(id) ON DELETE CASCADE,
    name             VARCHAR(255) NOT NULL,
    reference_amount DOUBLE PRECISION NOT NULL,
    calories         DOUBLE PRECISION NOT NULL,
    fat              DOUBLE PRECISION NOT NULL,
    protein          DOUBLE PRECISION NOT NULL,
    carbs            DOUBLE PRECISION NOT NULL
);

CREATE INDEX idx_ingredient_profile ON ingredient(profile_id);

CREATE TABLE meal (
    id         BIGSERIAL PRIMARY KEY,
    profile_id BIGINT NOT NULL REFERENCES profile(id) ON DELETE CASCADE,
    name       VARCHAR(255) NOT NULL
);

CREATE INDEX idx_meal_profile ON meal(profile_id);

CREATE TABLE meal_ingredient (
    id            BIGSERIAL PRIMARY KEY,
    meal_id       BIGINT NOT NULL REFERENCES meal(id) ON DELETE CASCADE,
    ingredient_id BIGINT NOT NULL REFERENCES ingredient(id) ON DELETE CASCADE,
    amount        DOUBLE PRECISION NOT NULL
);

CREATE INDEX idx_meal_ingredient_meal ON meal_ingredient(meal_id);

CREATE TABLE day_entry (
    id         BIGSERIAL PRIMARY KEY,
    profile_id BIGINT NOT NULL REFERENCES profile(id) ON DELETE CASCADE,
    date       DATE NOT NULL,
    UNIQUE(profile_id, date)
);

CREATE INDEX idx_day_entry_profile_date ON day_entry(profile_id, date);

CREATE TABLE day_entry_meal (
    id           BIGSERIAL PRIMARY KEY,
    day_entry_id BIGINT NOT NULL REFERENCES day_entry(id) ON DELETE CASCADE,
    meal_id      BIGINT NOT NULL REFERENCES meal(id) ON DELETE CASCADE,
    sort_order   INTEGER NOT NULL DEFAULT 0
);

CREATE INDEX idx_day_entry_meal_entry ON day_entry_meal(day_entry_id);
