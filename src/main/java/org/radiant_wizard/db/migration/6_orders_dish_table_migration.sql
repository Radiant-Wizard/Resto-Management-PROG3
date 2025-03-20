CREATE TABLE if not exists order_dish(
    order_dish_id BIGSERIAL PRIMARY KEY,
    dish_id BIGINT references dishes(dish_id) NOT NULL,
    order_id BIGINT references orders(order_id) NOT NULL  ON DELETE CASCADE,
    ordered_dish_quantity INT NOT NULL
);


