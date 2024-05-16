USE auth;

CREATE TABLE AUTH.MEMBER (
                        created_at timestamp(6) not null DEFAULT CURRENT_TIMESTAMP(6),
                        deleted_at timestamp(6),
                        id bigint auto_increment not null,
                        last_login timestamp(6),
                        updated_at timestamp(6),
                        deletion_yn varchar(255) not null default 'N',
                        email varchar(255) unique,
                        eupmyundong varchar(255),
                        image_url varchar(255),
                        introduction varchar(255),
                        kakao_id varchar(255) unique,
                        naver_id varchar(255) unique,
                        nickname varchar(255) not null unique,
                        password varchar(255),
                        phone_number varchar(255),
                        role varchar(255) check (role in ('ANONYMOUS','MEMBER','ADMIN')),
                        sido varchar(255),
                        sigungu varchar(255),
                        thumbnail_url varchar(255),
                        location geometry,
                        primary key (id));

INSERT INTO AUTH.MEMBER (nickname, role) VALUES ('관리자', 'ADMIN')
