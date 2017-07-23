create table cue_location (
    `groupname` varchar(255) not null,
    `page_index` integer not null,
    `page` integer not null,
    `active_claim_id` integer,
    `reserved` integer not null,
    primary key (`groupname`, `page_index`, `page`)
)
