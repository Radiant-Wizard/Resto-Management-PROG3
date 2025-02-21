CREATE TABLE dish_ingredients (
    dish_id INT REFERENCES dishes(dish_id) ON DELETE CASCADE,
    ingredient_id REFERENCES ingredients(ingredient_id) ON DELETE CASCADE,
    PRIMARY KEY (dish_id, ingredient_id),
    quantity int not null
);

