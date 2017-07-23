CREATE TABLE `zone_coordinates` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT,
    `latitude` double not null,
    `longitude` double not null,
    `zone_ref` bigint(20),
    primary key (id),
    KEY `FK_zone_ref` (`zone_ref`),
    CONSTRAINT `FK_zone_ref` FOREIGN KEY (`zone_ref`) REFERENCES `zone_mapping` (`id`)
);