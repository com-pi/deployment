USE board;

create table member
(
    member_id bigint auto_increment primary key,
    image_url varchar(255) null,
    nickname  varchar(255) null
);

create table article
(
    article_id   bigint auto_increment primary key,
    view_count   int          null,
    created_at   datetime(6)  not null,
    deleted_at   datetime(6)  null,
    member_id    bigint       null,
    updated_at   datetime(6)  null,
    article_type varchar(31)  not null,
    content      varchar(255) null,
    deletion_yn  varchar(255) null,
    image_urls   varchar(255) null,
    title        varchar(255) null
);

create table article_report
(
    report_id   bigint auto_increment primary key,
    article_id  bigint       null,
    created_at  datetime(6)  not null,
    reported_id bigint       null,
    reporter_id bigint       null,
    updated_at  datetime(6)  null,
    content     varchar(255) null
);

create table comments
(
    comment_id   bigint auto_increment primary key,
    created_date date         null,
    article_id   bigint       null,
    created_at   datetime(6)  not null,
    deleted_at   datetime(6)  null,
    member_id    bigint       null,
    parent_id    bigint       null,
    updated_at   datetime(6)  null,
    content      varchar(255) null,
    deletion_yn  varchar(255) null,

    foreign key (parent_id) references comments (comment_id),
    foreign key (article_id) references article (article_id)
);

create table diary_board
(
    article_id bigint not null primary key,

    foreign key (article_id) references article (article_id)
);

create table general_board
(
    article_id bigint not null primary key,

    foreign key (article_id) references article (article_id)
);

create table hashtag
(
    hashtag_id bigint auto_increment primary key,
    name       varchar(255) null
);

create table article_hashtag
(
    article_hashtag_id bigint auto_increment primary key,
    article_id         bigint null,
    hashtag_id         bigint null,

    foreign key (hashtag_id) references hashtag (hashtag_id)
);

create table likes
(
    like_id    bigint auto_increment primary key,
    is_liked   bit    null,
    article_id bigint null,
    member_id  bigint null,

    unique (article_id, member_id),
    foreign key (article_id) references article (article_id)
);

create table qna_board
(
    article_id bigint not null primary key,

    foreign key (article_id) references article (article_id)
);


-- indexes
create index idx_name on hashtag (name);
