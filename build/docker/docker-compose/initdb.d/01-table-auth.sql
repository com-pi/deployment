USE auth;

CREATE TABLE member (
                        id bigint auto_increment not null,
                        kakao_id varchar(255) unique,
                        naver_id varchar(255) unique,
                        email varchar(255) unique,
                        phone_number varchar(255) unique,
                        password varchar(255),
                        role varchar(255) check (role in ('ANONYMOUS','MEMBER','ADMIN')),
                        nickname varchar(255) not null unique,
                        introduction varchar(255),
                        location geometry,
                        sido varchar(255),
                        sigungu varchar(255),
                        eupmyundong varchar(255),
                        image_url varchar(255),
                        thumbnail_url varchar(255),
                        created_at timestamp(6) not null DEFAULT CURRENT_TIMESTAMP(6),
                        deleted_at timestamp(6),
                        last_login timestamp(6),
                        updated_at timestamp(6),
                        deletion_yn varchar(255) not null default 'N',
                        primary key (id));

INSERT INTO member (nickname, role) VALUES ('관리자', 'ADMIN');
