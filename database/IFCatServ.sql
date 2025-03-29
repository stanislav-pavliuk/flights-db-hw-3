DROP DATABASE IF EXISTS inflight_catering_service;

CREATE DATABASE IF NOT EXISTS inflight_catering_service CHARACTER SET 'utf8mb4' COLLATE 'utf8mb4_unicode_ci';

USE inflight_catering_service;

DROP TABLE IF EXISTS airport;
CREATE TABLE airport
(
    airport_code CHAR(3)      NOT NULL PRIMARY KEY COMMENT 'Airport Code: primary key inline declaration.',
    airport_name VARCHAR(255) NOT NULL COMMENT 'Full name of the Airport'
) ENGINE = innodb COMMENT = 'Airport entity type.';


DROP TABLE IF EXISTS airline;
CREATE TABLE airline
(
    code CHAR(2) COMMENT 'Airline Code.',
    name VARCHAR(100) NOT NULL COMMENT 'Full name of the Airline.',

    CONSTRAINT pk_airline PRIMARY KEY (code)
);

DROP TABLE IF EXISTS aircraft_type;
CREATE TABLE aircraft_type
(
    type_designator CHAR(4) COMMENT 'The ICAO (International Civil Aviation Organization) assigns 4-character codes to identify aircraft types.',
    model           VARCHAR(100) NOT NULL COMMENT 'Model of the aircraft.',

    CONSTRAINT pk_aircraft_code PRIMARY KEY (type_designator)
);

DROP TABLE IF EXISTS aircraft;
CREATE TABLE aircraft
(
    airline_code  CHAR(2),
    number        VARCHAR(10),
    aircraft_type CHAR(4),

    CONSTRAINT pk_aircraft
        PRIMARY KEY (airline_code, number) COMMENT 'Airline Code and Aircraft Number uniquely identifies an aircraft.',

    CONSTRAINT fk_aircraft_airline_code
        FOREIGN KEY (airline_code) REFERENCES airline (code) ON DELETE CASCADE ON UPDATE CASCADE,

    CONSTRAINT fk_aircraft_aircraft_code
        FOREIGN KEY (aircraft_type) REFERENCES aircraft_type (type_designator) ON DELETE RESTRICT ON UPDATE CASCADE

);

DROP TABLE IF EXISTS meal_type;
CREATE TABLE meal_type
(
    type VARCHAR(40) NOT NULL COMMENT 'Type of the meal type',

    CONSTRAINT pk_meal_type PRIMARY KEY (type)
) COMMENT 'Meal Type - reference table.';

DROP TABLE IF EXISTS meal;
CREATE TABLE meal
(
    id   BIGINT AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL COMMENT 'Name of the meal type',
    type VARCHAR(13) NOT NULL COMMENT 'Type of the meal type',

    CONSTRAINT pk_meal PRIMARY KEY (id),

    CONSTRAINT fk_meal_type
        FOREIGN KEY (type) REFERENCES meal_type (type) ON DELETE RESTRICT ON UPDATE CASCADE
) COMMENT 'Reference table of all.';

DROP TABLE IF EXISTS beverage_type;
CREATE TABLE beverage_type
(
    type  VARCHAR(40)    NOT NULL COMMENT 'Beverage type.',
    h_o_c ENUM ('H','C') NOT NULL COMMENT 'Hot or Cold: H, C',

    CONSTRAINT pk_beverage_type PRIMARY KEY (type)
) COMMENT 'Beverage Type - reference table.';

DROP TABLE IF EXISTS beverage;
CREATE TABLE beverage
(
    id          BIGINT AUTO_INCREMENT,
    name        VARCHAR(13)                      NOT NULL COMMENT 'Name of the beverage type',
    type        VARCHAR(13)                      NOT NULL COMMENT 'Type of the beverage.',
    unit        ENUM ('Bottle','Pack','Serving') NOT NULL COMMENT 'Unit of the beverage',
    qty_in_unit SMALLINT                         NOT NULL COMMENT 'Quantity in one unit',

    CONSTRAINT pk_beverage PRIMARY KEY (id),

    CONSTRAINT fk_beverage_type
        FOREIGN KEY (type) REFERENCES beverage_type (type) ON DELETE RESTRICT ON UPDATE CASCADE
);

DROP TABLE IF EXISTS flight;
CREATE TABLE flight
(
    id                BIGINT AUTO_INCREMENT COMMENT 'Surrogate key for the flight entity.',
    no                VARCHAR(10) COMMENT 'Flight number (Part of PK), Example: AA335',

    departure_date    DATE        NOT NULL COMMENT 'Departure Date (Part of PK)',
    dep_time          TIME        NOT NULL COMMENT 'Departure time',

    check_in_time     TIME        NOT NULL COMMENT 'Check-in time',

    arrival_date      DATE        NOT NULL COMMENT 'Arrival Date',
    arrival_time      TIME        NOT NULL COMMENT 'Arrival time',

    from_airport_code CHAR(3)     NOT NULL COMMENT 'From Airport Code: N:1 (Many to one)',
    to_airport_code   CHAR(3)     NOT NULL COMMENT 'To Airport Code: N:1 (Many to one)',

    airline_code      CHAR(2)     NOT NULL COMMENT 'Airline Code: N:1 (Many to one)',
    aircraft_number   VARCHAR(10) NOT NULL COMMENT 'Aircraft Number: N:1 (Many to one)',

    # duration is not needed unless we want to create a trigger to update it automatically
    # should be derived from (arrival_date, arrival_time) - (dep_time

    CONSTRAINT pk_flight PRIMARY KEY (id) COMMENT 'Primary key for the flight entity.',

    CONSTRAINT fk_flight_from_airport_code
        FOREIGN KEY (from_airport_code) REFERENCES airport (airport_code) ON DELETE RESTRICT ON UPDATE CASCADE,

    CONSTRAINT fk_flight_to_airport_code
        FOREIGN KEY (to_airport_code) REFERENCES airport (airport_code) ON DELETE RESTRICT ON UPDATE CASCADE,

    CONSTRAINT fk_flight_airline_code
        FOREIGN KEY (airline_code) REFERENCES airline (code) ON DELETE RESTRICT ON UPDATE CASCADE,

    CONSTRAINT fk_flight_aircraft
        FOREIGN KEY (airline_code, aircraft_number) REFERENCES aircraft (airline_code, number) ON DELETE RESTRICT ON UPDATE CASCADE,

    CONSTRAINT uq_flight_no_dep_date
        UNIQUE (no, departure_date) COMMENT 'Unique constraint to ensure no duplicate flight records.'
) ENGINE = innodb COMMENT = 'Flight entity type.';

DROP TABLE IF EXISTS loaded_meals;
CREATE TABLE loaded_meals
/* LoadedMeals ET. Source: Catering & Galley Loading Plan */
(
    meal_type         VARCHAR(40) NOT NULL,
    flight_id         BIGINT      NOT NULL,

    quantity          SMALLINT    NOT NULL DEFAULT 0,

    CONSTRAINT pk_loaded_meals PRIMARY KEY (meal_type, flight_id),

    CONSTRAINT fk_loaded_meals_flight_id
        FOREIGN KEY (flight_id) REFERENCES flight (id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_loaded_meals_meal_type
        FOREIGN KEY (meal_type) REFERENCES meal_type (type) ON DELETE CASCADE ON UPDATE CASCADE
) COMMENT 'LoadedMeals ET. Source: Catering & Galley Loading Plan';

DROP TABLE IF EXISTS loaded_beverages;
CREATE TABLE loaded_beverages
(
    beverage_type     VARCHAR(40) NOT NULL,
    flight_id         BIGINT      NOT NULL,

    quantity          SMALLINT    NOT NULL DEFAULT 0,
    servings_quantity SMALLINT    NOT NULL DEFAULT 0 COMMENT 'Quantity served to passengers. Why this column is here?',

    CONSTRAINT pk_loaded_beverages PRIMARY KEY (flight_id, beverage_type),

    CONSTRAINT fk_loaded_beverages_flight_id
        FOREIGN KEY (flight_id) REFERENCES flight (id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_loaded_beverages_beverage_type
        FOREIGN KEY (beverage_type) REFERENCES beverage_type (type) ON DELETE CASCADE ON UPDATE CASCADE
) COMMENT 'LoadedBeverages ET. Source: Catering & Galley Loading Plan';


DROP TABLE IF EXISTS passenger;
CREATE TABLE passenger
/* Passenger ET. Source: Passenger & Cargo Manifest */
(
    # ticket no can be split into 2 parts: 3-char airline code and 10-char ticket number
    ticket_no      CHAR(13)                       NOT NULL PRIMARY KEY COMMENT 'Ticket Number (IATA format): PK',
    flight_id      BIGINT                         NOT NULL COMMENT 'Flight ID: FK',

    full_name      VARCHAR(255)                   NOT NULL COMMENT 'Full name of the passenger',
    flight_class   ENUM ('F','B','E')             NOT NULL COMMENT 'Flight Class: F, B, E; flightClass: rows: 1-5 - First;6-10 - Business; 11-inf - Economy',
    seat_row       SMALLINT                       NOT NULL,
    seat_letter    ENUM ('A','B','C','D','E','F') NOT NULL,
    credit_card_no CHAR(16),

    CONSTRAINT fk_flight
        FOREIGN KEY (flight_id) REFERENCES flight (id)
            ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE passenger_meal_preorder
(
    ticket_no CHAR(13) PRIMARY KEY,
    meal_type VARCHAR(13) NOT NULL,

    CONSTRAINT fk_passenger_meal_preference_ticket_no
        FOREIGN KEY (ticket_no) REFERENCES passenger (ticket_no) ON DELETE CASCADE ON UPDATE CASCADE,

    CONSTRAINT fk_passenger_meal_preference_meal_type
        FOREIGN KEY (meal_type) REFERENCES meal_type (type) ON DELETE RESTRICT ON UPDATE CASCADE
);

CREATE TABLE passenger_beverage_preorder
(
    ticket_no     CHAR(13),
    beverage_type VARCHAR(13) NOT NULL,

    CONSTRAINT pk_passenger_beverage_preorder PRIMARY KEY (ticket_no, beverage_type),

    CONSTRAINT fk_passenger_beverage_preference_ticket_no
        FOREIGN KEY (ticket_no) REFERENCES passenger (ticket_no) ON DELETE CASCADE ON UPDATE CASCADE,

    CONSTRAINT fk_passenger_beverage_preference_beverage_type
        FOREIGN KEY (beverage_type) REFERENCES beverage_type (type) ON DELETE RESTRICT ON UPDATE CASCADE
);

CREATE TABLE passenger_meal_served
(
    ticket_no CHAR(13) PRIMARY KEY,
    meal_type VARCHAR(13) NOT NULL,

    CONSTRAINT fk_passenger_meal_served_ticket_no
        FOREIGN KEY (ticket_no) REFERENCES passenger (ticket_no) ON DELETE CASCADE ON UPDATE CASCADE,

    CONSTRAINT fk_passenger_meal_served_meal_type
        FOREIGN KEY (meal_type) REFERENCES meal_type (type) ON DELETE RESTRICT ON UPDATE CASCADE
);

CREATE TABLE passenger_beverage_served
(
    ticket_no     CHAR(13),
    beverage_type VARCHAR(13) NOT NULL,

    CONSTRAINT pk_passenger_beverage_served PRIMARY KEY (ticket_no, beverage_type),

    CONSTRAINT fk_passenger_beverage_served_ticket_no
        FOREIGN KEY (ticket_no) REFERENCES passenger (ticket_no) ON DELETE CASCADE ON UPDATE CASCADE,

    CONSTRAINT fk_passenger_beverage_served_beverage_type
        FOREIGN KEY (beverage_type) REFERENCES beverage_type (type) ON DELETE RESTRICT ON UPDATE CASCADE
);
