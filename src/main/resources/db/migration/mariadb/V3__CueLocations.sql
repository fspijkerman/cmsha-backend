create table cue_location (
    `groupname` varchar(255) not null,
    `page_index` integer not null,
    `page` integer not null,
    `active_claim_id` bigint(20) DEFAULT NULL,
    `reserved` integer not null,
    primary key (`groupname`, `page_index`, `page`),
    KEY `FK_cue_location_active_claim_id` (`active_claim_id`),
    CONSTRAINT `FK_cue_location_active_claim_id` FOREIGN KEY (`active_claim_id`) REFERENCES `active_claim` (`id`) ON DELETE SET NULL
);
