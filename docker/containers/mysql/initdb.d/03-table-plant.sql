USE plant;

CREATE TABLE plant (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       member_id BIGINT NOT NULL,
                       plant_name VARCHAR(255) NOT NULL,
                       plant_type VARCHAR(255) NOT NULL,
                       plant_age INT NOT NULL,
                       plant_birthday DATE NOT NULL,
                       last_water_day DATE,
                       plant_image_url VARCHAR(255),
                       interval_in_weeks INT,
                       frequency INT,
                       plant_description VARCHAR(3000),
                       plant_status VARCHAR(255),
                       plant_location VARCHAR(255),
                       intimacy INT,
                       pot_type VARCHAR(255),
                       repotting_date DATE,
                       fertilizing_date DATE,
                       pruning_date DATE,
                       character_id BIGINT,
                       FOREIGN KEY (character_id) REFERENCES character(id)
);

CREATE TABLE character (
                           id BIGINT AUTO_INCREMENT PRIMARY KEY,
                           character_no BIGINT NOT NULL,
                           character_name VARCHAR(255) NOT NULL
);

CREATE TABLE character_illustration (
                                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                        name VARCHAR(255) NOT NULL,
                                        image_url VARCHAR(255) NOT NULL,
                                        required_level INT
);

CREATE TABLE plant_character (
                                 id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                 plant_id BIGINT NOT NULL,
                                 character_id BIGINT NOT NULL,
                                 FOREIGN KEY (plant_id) REFERENCES plant(id),
                                 FOREIGN KEY (character_id) REFERENCES character(id)
);

CREATE TABLE calendar (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          plant_id BIGINT NOT NULL,
                          title VARCHAR(255) NOT NULL,
                          description VARCHAR(255),
                          FOREIGN KEY (plant_id) REFERENCES plant(id)
);

CREATE TABLE plant_diary (
                             id BIGINT AUTO_INCREMENT PRIMARY KEY,
                             plant_id BIGINT NOT NULL,
                             calendar_id BIGINT NOT NULL,
                             diary_id BIGINT NOT NULL,
                             intimacy_score INT,
                             FOREIGN KEY (plant_id) REFERENCES plant(id),
                             FOREIGN KEY (calendar_id) REFERENCES calendar(id),
                             FOREIGN KEY (diary_id) REFERENCES diary(id)
);

CREATE TABLE diary (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       title VARCHAR(255) NOT NULL,
                       content VARCHAR(255) NOT NULL,
                       image_url VARCHAR(255),
                       is_public BOOLEAN
);