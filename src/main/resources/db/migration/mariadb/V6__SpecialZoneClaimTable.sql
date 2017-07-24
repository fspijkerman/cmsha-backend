create table special_zone_claim (
    id bigint(20) NOT NULL AUTO_INCREMENT,
    claim_expiration bigint,
    claim_tag varchar(255),
    claimed datetime,
    opt_lock bigint not null,
    zone_name varchar(255) not null,
    primary key (id)
);

alter table special_zone_claim
    add constraint UK_3cs7o2w1xfylm35p381c67453 unique (zone_name);