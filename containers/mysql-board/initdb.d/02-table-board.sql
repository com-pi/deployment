USE board;

create table article
(
    article_id    bigint auto_increment                                                primary key,
    article_type  varchar(31)                                                          not null,
    created_at    datetime(6)                                                          not null,
    updated_at    datetime(6)                                                          null,
    deleted_at    datetime(6)                                                          null,
    deletion_yn   varchar(255)                                                         null,
    comment_count int                                                                  null,
    content       varchar(255)                                                         null,
    image_urls    varchar(255)                                                         null,
    like_count    int                                                                  null,
    member_id     bigint                                                               null,
    title         varchar(255)                                                         null,
    type          enum ('COMMON_ARTICLE', 'GENERAL_BOARD', 'QNA_BOARD', 'DIARY_BOARD') null,
    view_count    int                                                                  null
);

create table article_likes
(
    like_id    bigint auto_increment    primary key,
    is_liked   bit    null,
    member_id  bigint null,
    article_id bigint null,

    unique (article_id, member_id),
    foreign key (article_id) references article (article_id)
);

create table article_report
(
    report_id   bigint auto_increment   primary key,
    article_id  bigint       null,
    created_at  datetime(6)  not null,
    reported_id bigint       null,
    reporter_id bigint       null,
    updated_at  datetime(6)  null,
    content     varchar(255) null
);

create table comments
(
    comment_id   bigint auto_increment  primary key,
    created_at   datetime(6)   not null,
    updated_at   datetime(6)   null,
    deleted_at   datetime(6)   null,
    deletion_yn  varchar(255)  null,
    content      varchar(255)  null,
    created_date date          null,
    member_id    bigint        null,
    article_id   bigint        null,
    parent_id    bigint        null,
    like_count   int default 0 null,

    foreign key (parent_id) references comments (comment_id),
    foreign key (article_id) references article (article_id)
);

create table comment_likes
(
    like_id    bigint auto_increment    primary key,
    is_liked   bit    null,
    comment_id bigint null,
    member_id  bigint null,

    unique (comment_id, member_id),
    foreign key (comment_id) references comments (comment_id)
);

create table diary_board
(
    article_id bigint not null  primary key,
    foreign key (article_id) references article (article_id)
);

create table general_board
(
    article_id bigint not null primary key,
    foreign key (article_id) references article (article_id)
);

create table hashtag
(
    hashtag_id bigint auto_increment    primary key,
    name       varchar(255) null
);

create table article_hashtag
(
    article_hashtag_id bigint auto_increment    primary key,
    article_id         bigint null,
    hashtag_id         bigint null,
    foreign key (hashtag_id) references hashtag (hashtag_id)
);


create table member
(
    member_id bigint auto_increment primary key,
    image_url varchar(255) null,
    nickname  varchar(255) null
);

create table qna_board
(
    article_id bigint not null  primary key,
    foreign key (article_id) references article (article_id)
);

-- indexes
create index hashtag_id on article_hashtag (hashtag_id);

create index idx_name on hashtag (name);

create index article_id on comments (article_id);

create index parent_id on comments (parent_id);