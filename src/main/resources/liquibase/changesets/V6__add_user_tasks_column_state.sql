alter table user_tasks
    add column state varchar(255) check (state in ('NOT_EVALUATED', 'REJECTED', 'ACCEPTED'));
