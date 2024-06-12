USE board;
create table member
(
    member_id bigint auto_increment primary key,
    image_url varchar(255) null,
    nickname  varchar(255) null
) ENGINE = InnoDB;


create table article
(
    article_id   bigint       not null  primary key,
    view_count   int          null,
    created_at   datetime(6)  not null,
    deleted_at   datetime(6)  null,
    member_id    bigint       null,
    updated_at   datetime(6)  null,
    article_type varchar(31)  not null,
    content      varchar(255) null,
    deletion_yn  varchar(255) null,
    image_urls   varchar(255) null,
    title        varchar(255) null,
    foreign key (member_id) references member (member_id)
) ENGINE = InnoDB;

create table article_seq
(
    next_val bigint null
) ENGINE = InnoDB;

create table buy_and_sell
(
    article_id  bigint       not null   primary key,
    is_free     bit          null,
    like_count  int          null,
    price       int          null,
    eupmyundong varchar(255) null,
    hashtags    varchar(255) null,
    sido        varchar(255) null,
    sigungu     varchar(255) null,
    location    geometry     null,
    foreign key (article_id) references article (article_id)
) ENGINE = InnoDB;

create table likes
(
    like_id      bigint auto_increment                             primary key,
    is_liked     bit                                                      null,
    article_id   bigint                                                   null,
    member_id    bigint                                                   null,
    article_type enum ('BUY_AND_SELL', 'PLANT_SITTER', 'SHOW_OFF', 'QNA') null,
    unique (article_id, member_id),
    foreign key (article_id) references article (article_id),
    foreign key (member_id) references member (member_id)
) ENGINE = InnoDB;




