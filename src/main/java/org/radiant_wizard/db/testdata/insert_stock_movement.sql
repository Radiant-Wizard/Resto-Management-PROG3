INSERT INTO stock_movement (stock_movement_id, ingredient_id, movement_type, quantity, unit, movement_date)
VALUES
    (1, 18, 'ENTRY', 100.00, 'U', '2025-02-01 08:00:00'),
    (2, 11, 'ENTRY', 50.00, 'U', '2025-02-01 08:00:00'),
    (3, 16, 'ENTRY', 10000.00, 'G', '2025-02-01 08:00:00'),
    (4, 17, 'ENTRY', 20.00, 'L', '2025-02-01 08:00:00'),
    (5, 18, 'EXIT', 10.00, 'U', '2025-02-02 10:00:00'),
    (6, 18, 'EXIT', 10.00, 'U', '2025-02-02 15:00:00'),
    (7, 11, 'EXIT', 20.00, 'U', '2025-02-05 16:00:00');
