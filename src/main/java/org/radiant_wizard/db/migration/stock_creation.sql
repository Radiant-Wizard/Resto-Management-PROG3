create table stock(
    stock_id BIGSERIAL primary key,
    ingredient_id references ingredients(ingredient_id),
    stock_quantity NUMERIC(10,2),
    unit measurement_unit
)