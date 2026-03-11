--liquibase formatted sql

--changeset rzakharov:1
create table if not exists products
(
    id             bigserial primary key,
    name           varchar(250) not null unique,
    description    text,
    price          decimal(19,2)   default 0.00,
    stock_quantity int          not null,
    created_at     timestamp default CURRENT_TIMESTAMP
);

--rollback drop table if exists products

--changeset rzakharov:2
create index if not exists idx_products_name on products (name);

-- rollback drop index if exists idx_products_name