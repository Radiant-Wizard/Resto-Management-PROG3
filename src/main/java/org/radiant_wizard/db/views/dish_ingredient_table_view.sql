create view see_dishes as SELECT
       d.dish_name AS dish,
       i.ingredient_id,
       i.ingredient_name AS ingredient,
       i.creation_date_and_last_modification_time as last_modification,
       di.quantity,
       i.unit AS unit,
       (i.unit_price * di.quantity) AS cost_per_ingredient
FROM dish_ingredients di
JOIN dishes d ON di.dish_id = d.dish_id
JOIN ingredients i ON di.ingredient_id = i.ingredient_id;
