CREATE TABLE `active_claim` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created` datetime NOT NULL,
  `expiration` bigint(20) NOT NULL,
  `playback_titan_id` int(11) NOT NULL,
  PRIMARY KEY (`id`)
);

CREATE TABLE `zone_mapping` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `titan_group_id` int(11) DEFAULT NULL,
  `titan_group_name` varchar(255) NOT NULL,
  `zone_name` varchar(255) NOT NULL,
  `active_claim_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_zoneName` (`zone_name`),
  KEY `FK_active_claim_id` (`active_claim_id`),
  CONSTRAINT `FK_active_claim_id` FOREIGN KEY (`active_claim_id`) REFERENCES `active_claim` (`id`)
);

