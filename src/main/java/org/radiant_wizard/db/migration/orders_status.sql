CREATE TABLE if not exists order_status(
    order_status_id BIGSERIAL PRIMARY KEY,
    order_id BIGINT references orders(order_id),
    order_creation_date TIMESTAMP without time zone
);

