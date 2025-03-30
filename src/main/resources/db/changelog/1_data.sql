INSERT INTO aircraft_type
VALUES ('A320', 'Airbus 320'),
       ('B737', 'Boeing 737'),
       ('CCM2', 'Cessna Citation M2'),
       ('E175', 'Embraer 175');

INSERT INTO airline
VALUES ('AF', 'Air France'),
       ('BA', 'British Airways'),
       ('LH', 'Lufthansa'),
       ('NW', 'North Western Airways'),
       ('PA', 'Pan American Airways'),
       ('WI', 'Wizz Air');

INSERT INTO airport
VALUES ('AMS', 'Amsterdam Schiphol Airport'),
       ('JFK', 'John F Kennedy International Airport'),
       ('KBP', 'Boryspil International Airport'),
       ('LAX', 'Los Angeles International Airport'),
       ('LHR', 'London Heathrow Airport'),
       ('LWO', 'Lviv Danylo Halytskyi International Airport'),
       ('MUC', 'Munich Airport');

INSERT INTO meal_type
VALUES ('Chicken'),
       ('Pork'),
       ('Salmon'),
       ('Veal'),
       ('Vegan');

INSERT INTO beverage_type
VALUES ('Burgundie', 'C'),
       ('Chablis', 'C'),
       ('Coffee', 'H'),
       ('Coke', 'C'),
       ('HotWasser', 'H'),
       ('Jagermeister', 'C'),
       ('Jamesson', 'C'),
       ('OrangeJuce', 'C'),
       ('Schnaps', 'C'),
       ('Tea', 'H'),
       ('TomatoJuce', 'C'),
       ('Wasser', 'C');

INSERT INTO aircraft
VALUES ('BA', '25006', 'A320'),
       ('LH', '10234', 'A320'),
       ('LH', '10236', 'B737'),
       ('PA', '1001', 'B737'),
       ('PA', '1002', 'B737'),
       ('BA', '25007', 'CCM2'),
       ('LH', '10237', 'CCM2'),
       ('BA', '25005', 'E175'),
       ('LH', '10235', 'E175');

INSERT INTO flight
VALUES (29, '113', '2025-02-23', '21:30:00', '20:30:00', '2025-02-24', '12:30:00', 'JFK', 'MUC', 'LH', '10234'),
       (30, '320', '2025-02-24', '10:20:00', '09:20:00', '2025-02-24', '10:20:00', 'MUC', 'JFK', 'LH', '10236'),
       (64, '112', '2025-02-23', '17:30:00', '16:30:00', '2025-02-23', '19:30:00', 'MUC', 'JFK', 'BA', '25006'),
       (67, '321', '2025-02-25', '15:30:00', '14:30:00', '2025-02-26', '09:30:00', 'LAX', 'MUC', 'PA', '1001'),
       (68, '330', '2025-02-23', '14:30:00', '13:30:00', '2025-02-23', '16:30:00', 'MUC', 'LAX', 'PA', '1002'),
       (69, '111', '2025-02-23', '10:30:00', '09:30:00', '2025-02-23', '12:30:00', 'MUC', 'LWO', 'BA', '25007'),
       (70, '112', '2025-02-24', '17:30:00', '16:30:00', '2025-02-24', '17:30:00', 'LWO', 'LHR', 'LH', '10237'),
       (71, '121', '2025-03-01', '11:30:00', '10:30:00', '2025-03-01', '15:30:00', 'LHR', 'LWO', 'BA', '25005'),
       (72, '122', '2025-03-01', '22:30:00', '21:30:00', '2025-03-01', '22:30:00', 'LWO', 'MUC', 'LH', '10235');

INSERT INTO meal
VALUES (1, 'Mexican Curry Chicken', 'Chicken'),
       (2, 'Deutche Pork Sausage', 'Pork'),
       (3, 'Grilled Norvegian Salmon ', 'Salmon'),
       (4, 'Spanish Veal Stake', 'Veal'),
       (5, 'Vegan Plate', 'Vegan');

INSERT INTO beverage
VALUES (1, 'Burgundie Bot', 'Burgundie', 'Bottle', 5),
       (2, 'Chablis Bottl', 'Chablis', 'Bottle', 5),
       (3, 'Nestle Filter', 'Coffee', 'Serving', 5),
       (4, 'Coca Cola Bot', 'Coke', 'Bottle', 2),
       (5, 'Water boiled', 'HotWasser', 'Serving', 1),
       (6, 'Jägermeister ', 'Jagermeister', 'Bottle', 1),
       (7, 'Jamesson Wisk', 'Jamesson', 'Bottle', 40),
       (8, 'Jus d\'Orange ', 'OrangeJuce', 'Pack', 10),
       (9, 'Bayriche Schn', 'Schnaps', 'Bottle', 20),
       (10, 'Lipton Tea Ba', 'Tea', 'Serving', 1),
       (11, 'Salted Tomato', 'TomatoJuce', 'Pack', 10),
       (12, 'Water Bevian ', 'Wasser', 'Bottle', 1);

INSERT INTO loaded_beverages
VALUES ('Chablis', 29, 1, 5),
       ('HotWasser', 29, 2, 1),
       ('Jagermeister', 29, 2, 1),
       ('Jamesson', 29, 4, 40),
       ('Tea', 29, 3, 1);

INSERT INTO loaded_meals
VALUES ('Chicken', 29, 6),
       ('Salmon', 29, 1),
       ('Vegan', 29, 2);

INSERT INTO passenger
VALUES ('LH-320001A', 29, 'Emmanuel Macron', 'F', 1, 'A', '1234567812340002'),
       ('LH-320002F', 29, 'Boris Johnson', 'F', 2, 'F', '1234567812340008'),
       ('LH-320005D', 29, 'Elon Musk', 'F', 5, 'D', '1234567812340007'),
       ('LH-320005F', 29, 'J D Vans', 'F', 5, 'F', '1234567812340003'),
       ('LH-320007C', 29, 'Boris Pistorius', 'B', 7, 'C', '1234567812340004'),
       ('LH-320011A', 29, 'Angela Merkel', 'E', 11, 'A', '1234567812340001'),
       ('LH-320011F', 29, 'Joe Biden', 'E', 11, 'F', '1234567812340006'),
       ('LH-320022A', 29, 'James Bond', 'E', 22, 'A', '1234567812340009'),
       ('LH-320022F', 29, 'Olaf Scholz', 'E', 22, 'F', '1234567812340005');

INSERT INTO passenger_beverage_preorder
VALUES ('LH-320005D', 'Chablis'),
       ('LH-320001A', 'HotWasser'),
       ('LH-320005D', 'HotWasser'),
       ('LH-320001A', 'Jagermeister'),
       ('LH-320002F', 'Jagermeister'),
       ('LH-320005F', 'Jamesson'),
       ('LH-320007C', 'Jamesson'),
       ('LH-320011A', 'Jamesson'),
       ('LH-320022A', 'Jamesson'),
       ('LH-320002F', 'Tea'),
       ('LH-320011F', 'Tea'),
       ('LH-320022F', 'Tea');

INSERT INTO passenger_beverage_served
VALUES ('LH-320001A', 'Coke'),
       ('LH-320002F', 'Jamesson'),
       ('LH-320005D', 'Jamesson'),
       ('LH-320005F', 'Jamesson'),
       ('LH-320007C', 'Jamesson'),
       ('LH-320011A', 'Jamesson'),
       ('LH-320011F', 'Jamesson'),
       ('LH-320022A', 'Jamesson'),
       ('LH-320022F', 'Jamesson');

INSERT INTO passenger_meal_preorder
VALUES ('LH-320002F', 'Chicken'),
       ('LH-320005D', 'Chicken'),
       ('LH-320005F', 'Chicken'),
       ('LH-320007C', 'Chicken'),
       ('LH-320011A', 'Chicken'),
       ('LH-320022A', 'Chicken'),
       ('LH-320022F', 'Salmon'),
       ('LH-320001A', 'Vegan'),
       ('LH-320011F', 'Vegan');

INSERT INTO passenger_meal_served
VALUES ('LH-320001A', 'Chicken'),
       ('LH-320002F', 'Chicken'),
       ('LH-320005D', 'Chicken'),
       ('LH-320005F', 'Chicken'),
       ('LH-320007C', 'Chicken'),
       ('LH-320011A', 'Chicken'),
       ('LH-320011F', 'Chicken'),
       ('LH-320022A', 'Chicken'),
       ('LH-320022F', 'Chicken');
