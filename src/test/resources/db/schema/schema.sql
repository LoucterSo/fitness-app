CREATE TABLE IF NOT EXISTS users (
  user_id BIGSERIAL PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  email VARCHAR(255) NOT NULL,
  age INTEGER NOT NULL,
  height_in_cm FLOAT(53) NOT NULL,
  weight_in_kg FLOAT(53) NOT NULL,
  goal VARCHAR(255) NOT NULL CHECK (goal IN ('WEIGHT_MAINTENANCE','WEIGHT_LOSS','BULKING')),
  sex VARCHAR(255) NOT NULL CHECK (sex IN ('MAN','WOMAN'))
);

CREATE TABLE IF NOT EXISTS dish (
  dish_id BIGSERIAL PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  carbs INTEGER NOT NULL,
  fats INTEGER NOT NULL,
  proteins INTEGER NOT NULL,
  calories_per_serving INTEGER NOT NULL
);

CREATE TABLE IF NOT EXISTS meal (
  meal_id BIGSERIAL PRIMARY KEY,
  user_id BIGSERIAL,
  name VARCHAR(255) NOT NULL,
  created_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS meal_dish (
  dish_id BIGSERIAL NOT NULL,
  meal_id BIGSERIAL NOT NULL
);

ALTER TABLE IF EXISTS meal
    ADD CONSTRAINT fk_meal_to_users FOREIGN KEY (user_id)
        REFERENCES users;
ALTER TABLE IF EXISTS meal_dish
    ADD CONSTRAINT fk_meal_dish_to_dish FOREIGN KEY (dish_id)
        REFERENCES dish;
ALTER TABLE IF EXISTS meal_dish
    ADD CONSTRAINT fk_meal_dish_to_meal FOREIGN KEY (meal_id)
        REFERENCES meal;