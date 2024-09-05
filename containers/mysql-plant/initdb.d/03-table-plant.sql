USE plant;

create table diary
(
    diary_id        bigint auto_increment   primary key,
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
);

create table plant_character
(
    plant_character_id bigint auto_increment    primary key,
    character_name     varchar(255) null,
    image_url          varchar(255) null
);

create table my_plant
(
    my_plant_id               bigint       auto_increment primary key,
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
    plant_type_id             bigint       null,
    plant_type                varchar(255) null,
    pot_type                  varchar(255) null,
    foreign key (plant_character_id) references plant_character (plant_character_id)
);

create table schedule
(
    schedule_id     bigint auto_increment   primary key,
    frequency       int          null,
    is_completed    bit          null,
    is_recurring    bit          null,
    created_at      datetime(6)  not null,
    deleted_at      datetime(6)  null,
    end_date_time   datetime(6)  null,
    member_id       bigint       null,
    start_date_time datetime(6)  null,
    updated_at      datetime(6)  null,
    color_type      varchar(255) null,
    deletion_yn     varchar(255) null,
    title           varchar(255) null
);

create table completed_schedule
(
    completed_schedule_id bigint auto_increment     primary key,
    completed_date        date   null,
    schedule_id           bigint null,

    unique (schedule_id, completed_date),
    foreign key (schedule_id) references schedule (schedule_id)
);

-- indexes
create index plant_character_id on my_plant (plant_character_id);
