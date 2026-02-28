--liquibase formatted sql

--changeset rzakharov:1
create table if not exists users (
    id bigserial primary key,
    email varchar(200) unique not null,
    name varchar(250) not null,
    created_at timestamp default current_timestamp
);
--rollback drop table if exists users;

--changeset rzakharov:2
create index if not exists idx_users_email on users(email);
--rollback drop index if exists idx_users_email