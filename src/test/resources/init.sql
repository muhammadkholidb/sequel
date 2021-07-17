create table if not exists "table" (
    id bigint not null primary key,
    created_at timestamp not null default current_timestamp,
    updated_at timestamp not null default current_timestamp,
    deleted_at timestamp,
    number integer not null,
    code varchar not null
);
