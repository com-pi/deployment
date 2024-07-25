USE auth;

CREATE TABLE MEMBER (
                        id bigint auto_increment not null,
                        kakao_id varchar(255) unique,
                        naver_id varchar(255) unique,
                        email varchar(255) unique,
                        phone_number varchar(255) unique,
                        password varchar(255),
                        role varchar(255) check (role in ('ANONYMOUS','MEMBER','ADMIN')),
                        nickname varchar(36) not null unique,
                        introduction text,
                        location geometry,
                        sido varchar(255),
                        sigungu varchar(255),
                        eupmyundong varchar(255),
                        image_url varchar(255),
                        thumbnail_url varchar(255),
                        created_at timestamp(6) not null DEFAULT CURRENT_TIMESTAMP(6),
                        last_login timestamp(6),
                        updated_at timestamp(6),
                        deletion_yn varchar(255) not null default 'N',
                        deleted_at timestamp(6),
                        primary key (id));

CREATE TABLE EVENT_RECORD_ENTITY (
                        id bigint auto_increment not null,
                        transaction_id varchar(255),
                        event_type varchar(255) check ( event_type in ('CREATE', 'UPDATE', 'DELETE')),
                        entity_type varchar(255),
                        entity_id varchar(255),
                        json_data text,
                        status varchar(255) check ( status in ('NEW', 'PUBLISHED', 'FAILED', 'PENDING')),
                        created_at timestamp(6),
                        updated_at timestamp(6),
                        primary key (id));

CREATE TABLE FOLLOW (
                        id bigint auto_increment not null,
                        follower_id bigint not null,
                        following_id bigint not null,
                        primary key (id),
                        foreign key (follower_id) references MEMBER (id),
                        foreign key (following_id) references MEMBER (id),
                        UNIQUE (follower_id, following_id)
);

INSERT INTO member (nickname, role, email, phone_number) VALUES ('관리자', 'ADMIN', 'comppi.comppi@gmail.com', '01012345678');
