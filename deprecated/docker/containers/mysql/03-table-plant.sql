USE plant;

create table plant_character
(
    plant_character_id bigint auto_increment primary key,
    character_name     varchar(255) null,
    image_url          varchar(255) null
) ENGINE = InnoDB;

create table my_plant
(
    my_plant_id               bigint auto_increment primary key,
    last_watering_date        date         null,
    plant_birthday            date         null,
    relationship_score        int          null,
    watering_interval_in_days int          null,
    created_at                datetime(6)  not null,
    deleted_at                datetime(6)  null,
    member_id                 bigint       null,
    plant_character_id        bigint       null,
    updated_at                datetime(6)  null,
    deletion_yn               varchar(255) null,
    plant_name                varchar(255) null,
    plant_spot                varchar(255) null,
    plant_type                varchar(255) null,
    pot_type                  varchar(255) null,
    foreign key (plant_character_id) references plant_character (plant_character_id)
) ENGINE = InnoDB;

create table schedule
(
    schedule_id   bigint auto_increment primary key,
    is_completed  bit          null,
    created_at    datetime(6)  not null,
    deleted_at    datetime(6)  null,
    end_date_time datetime(6)  null,
    member_id     bigint       null,
    updated_at    datetime(6)  null,
    deletion_yn   varchar(255) null,
    title         varchar(255) null
) ENGINE = InnoDB;

create table diary
(
    diary_id        bigint auto_increment primary key,
    created_date    date         null,
    is_public       bit          null,
    is_published    bit          null,
    created_at      datetime(6)  not null,
    deleted_at      datetime(6)  null,
    member_id       bigint       null,
    my_plant_id     bigint       null,
    updated_at      datetime(6)  null,
    content         varchar(255) null,
    deletion_yn     varchar(255) null,
    image_url_list  varchar(255) null,
    plant_care_list varchar(255) null,
    title           varchar(255) null
) ENGINE = InnoDB;





