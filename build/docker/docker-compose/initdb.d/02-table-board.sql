USE board;

CREATE TABLE article (
                         price integer,
                         id bigint auto_increment not null,
                         member_id bigint,
                         article_type varchar(255) check (article_type in ('BUY_AND_SELL','PLANT_SITTER','SHOW_OFF','QNA')),
                         content varchar(255),
                         eupmyundong varchar(255),
                         hashtags varchar(255),
                         image_urls varchar(255),
                         location Geometry,
                         sido varchar(255),
                         sigungu varchar(255),
                         title varchar(255),
                         primary key (id));

CREATE TABLE member (
                        id bigint not null,
                        member_id bigint,
                        image_url varchar(255),
                        nickname varchar(255),
                        primary key (id));

INSERT INTO member (member_id, image_url, nickname) VALUES (1, null, '관리자');
