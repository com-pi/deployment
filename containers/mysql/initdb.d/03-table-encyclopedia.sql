USE ENCYCLOPEDIA;

CREATE TABLE PLANT_SPECIES (
                                    id BIGINT NOT NULL AUTO_INCREMENT,
                                    taxonomy_species VARCHAR(255),
                                    taxonomy_genus VARCHAR(255),
                                    taxonomy_family VARCHAR(255),
                                    common_name VARCHAR(255) UNIQUE,
                                    description TEXT,
                                    watering_summary VARCHAR(255),
                                    watering_description VARCHAR(255),
                                    environment_summary VARCHAR(255),
                                    environment_description VARCHAR(255),
                                    humidity_summary VARCHAR(255),
                                    humidity_description VARCHAR(255),
                                    temperature_min INT,
                                    temperature_max INT,
                                    humid_min INT,
                                    humid_max INT,
                                    image_urls TEXT,
                                    created_at timestamp(6) not null DEFAULT CURRENT_TIMESTAMP(6),
                                    updated_at timestamp(6),
                                    PRIMARY KEY (id)
);

CREATE TABLE MY_ENCYCLOPEDIA (
                                   id	bigint	NOT NULL AUTO_INCREMENT,
                                   member_id	bigint	NULL,
                                   title	varchar(255)	NULL,
                                   cover_image_url	varchar(255)	NULL,
                                   created_at timestamp(6) not null DEFAULT CURRENT_TIMESTAMP(6),
                                   updated_at timestamp(6),
                                   deletion_yn varchar(255) not null default 'N',
                                   deleted_at timestamp(6),
                                   primary key (id)
);

CREATE TABLE ENCYCLOPEDIA_PLANT (
                                    ID	bigint	NOT NULL AUTO_INCREMENT,
                                    PLANT_SPECIES_ID	bigint	NOT NULL,
                                    MY_ENCYCLOPEDIA_ID	bigint	NOT NULL,
                                    PRIMARY KEY (ID),
                                    FOREIGN KEY (plant_species_id) REFERENCES PLANT_SPECIES(id),
                                    FOREIGN KEY (my_encyclopedia_id) REFERENCES MY_ENCYCLOPEDIA(id),
                                    UNIQUE (PLANT_SPECIES_ID, MY_ENCYCLOPEDIA_ID)
);

CREATE TABLE PLANT_ADD_INQUIRY (
                                   id	bigint	NOT NULL AUTO_INCREMENT,
                                   common_name	varchar(255)	NOT NULL,
                                   scientific_name	varchar(255)	NOT NULL,
                                   requester_id    bigint	NOT NULL,
                                   result varchar(255),
                                   status varchar(255) check ( status in ('SUBMITTED', 'IN_PROGRESS', 'PROCESSED')),
                                   created_at timestamp(6) not null DEFAULT CURRENT_TIMESTAMP(6),
                                   updated_at timestamp(6),
                                   deletion_yn varchar(255) not null default 'N',
                                   deleted_at timestamp(6),
                                   primary key (id)
);
