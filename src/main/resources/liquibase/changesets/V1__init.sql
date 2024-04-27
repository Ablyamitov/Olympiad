create table if not exists users
(
    id bigserial primary key,
    name varchar(255),
    surname varchar(255),
    username varchar(255) not null unique,
    email varchar(255) unique,
    password varchar(255) not null,
    session bigint not null

    );

create table if not exists users_roles
(
    user_id bigint not null,
    role varchar(255) not null,
    primary key (user_id,role),
    constraint fk_users_roles_users foreign key (user_id) references users (id) on delete cascade on update no action
    );


--Contest
create table if not exists contest
(
    id bigserial primary key,
    session bigint unique not null,
    name varchar(255) not null,
    participant_count int not null,
    judge_count int not null,
    username_prefix varchar(255) not null,
    duration varchar(255) not null,
    start_time timestamp,
    end_time timestamp
    );

--Tasks
create table if not exists tasks
(
    id bigserial primary key,
    session bigint not null,
    name varchar(255) not null,
    task text not null,
    points int not null,
    constraint fk_tasks_contest foreign key (session) references contest (session) on delete cascade on update no action
);

-- --User Tasks
-- create table if not exists user_tasks
-- (
--     id bigserial primary key,
--     session bigint not null,
--     user_id bigint not null,
--     taskNumber bigint not null,
--     fileContent text not null,
--     points int not null,
--     comment text not null,
--     sent_time timestamp,
--     constraint fk_tasks_contest foreign key (session) references contest (session) on delete cascade on update no action,
--     constraint fk_tasks_users foreign key (user_id) references users (id) on delete cascade on update no action
--     );





