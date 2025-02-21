CREATE TABLE dish_ingredients (
    dish_id INT REFERENCES dishes(dish_id) ON DELETE CASCADE,
    ingredient_id REFERENCES ingredients(ingredient_id) ON DELETE CASCADE,
    PRIMARY KEY (dish_id, ingredient_id),
    quantity int not null
);
 -- select id, name, unit_price from dish join dish_ingredient on dish.id = dish_ingredient.id_dish join ingredients on  ingredient.id = dish_ingredient.id_ingredient
