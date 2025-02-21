create type measurement_unit as ENUM('G', 'L', 'U');

create table ingredients(
    ingredient_id int primary key,
    ingredient_name varchar(50) not null,
    creation_date_and_last_modification_time timestamp,
    unit_price int,
    unit measurement_unit
);