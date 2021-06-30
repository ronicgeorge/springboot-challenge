create sequence hibernate_sequence start with 1 increment by 1;
create table location
(
    location_id int8 not null,
    city        varchar(255),
    street      varchar(255),
    zip_code    varchar(255),
    created_at  bigint,
    x           numeric(19, 8),
    y           numeric(19, 8),
    modified_at bigint,
    primary key (location_id)
)
