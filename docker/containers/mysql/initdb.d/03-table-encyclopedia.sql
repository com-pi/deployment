USE ENCYCLOPEDIA;

CREATE TABLE PLANT_SPECIES (
                                    id BIGINT NOT NULL AUTO_INCREMENT,
                                    plant_taxonomy_summary VARCHAR(255),
                                    plant_taxonomy_description VARCHAR(255),
                                    commonName VARCHAR(255) UNIQUE,
                                    description TEXT,
                                    watering_summary VARCHAR(255),
                                    watering_description VARCHAR(255),
                                    environment_summary VARCHAR(255),
                                    environment_description VARCHAR(255),
                                    temperature_min INT,
                                    temperature_max INT,
                                    humid_min INT,
                                    humid_max INT,
                                    imageUrls TEXT,
                                    PRIMARY KEY (id)
);