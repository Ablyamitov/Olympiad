--User Tasks
create table if not exists user_tasks
(
    id bigserial primary key,
    session bigint not null,
    user_id bigint not null,
    taskNumber bigint not null,
    fileContent text not null,
    points int,
    comment text,
    sent_time timestamp,
    constraint fk_tasks_contest foreign key (session) references contest (session) on delete cascade on update no action,
    constraint fk_tasks_users foreign key (user_id) references users (id) on delete cascade on update no action
    );
