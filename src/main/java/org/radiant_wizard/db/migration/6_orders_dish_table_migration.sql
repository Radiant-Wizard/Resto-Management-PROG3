CREATE TABLE if not exists order_dish(
    order_dish_id BIGSERIAL PRIMARY KEY,
    dish_id BIGINT references dishes(dish_id) NOT NULL,
    order_id BIGINT references orders(order_id) NOT NULL,
    ordered_dish_date TIMESTAMP without time zone DEFAULT CURRENT_TIMESTAMP,
    ordered_dish_quantity INT NOT NULL
);


