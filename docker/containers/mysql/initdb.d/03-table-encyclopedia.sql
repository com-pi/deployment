USE ENCYCLOPEDIA;

CREATE TABLE PLANT_SPECIES (
                                    id BIGINT NOT NULL AUTO_INCREMENT,
                                    plant_taxonomy_ VARCHAR(255),
                                    plant_taxonomy_description VARCHAR(255),
                                    plant_taxonomy_description VARCHAR(255),
                                    common_name VARCHAR(255) UNIQUE,
                                    description TEXT,
                                    watering_summary VARCHAR(255),
                                    watering_description VARCHAR(255),
                                    environment_summary VARCHAR(255),
                                    environment_description VARCHAR(255),
                                    temperature_min INT,
                                    temperature_max INT,
                                    humid_min INT,
                                    humid_max INT,
                                    image_urls TEXT,
                                    created_at timestamp(6) not null DEFAULT CURRENT_TIMESTAMP(6),
                                    updated_at timestamp(6),
                                    PRIMARY KEY (id)
);