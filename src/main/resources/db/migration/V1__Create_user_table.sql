CREATE TABLE `user`
(
    `id`           int(11) NOT NULL AUTO_INCREMENT,
    `account_id`   varchar(50) DEFAULT NULL,
    `name`         varchar(50) DEFAULT NULL,
    `token`        char(36)    DEFAULT NULL,
    `gmt_create`   bigint(50)  DEFAULT NULL,
    `gmt_modified` bigint(50)  DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 3
  DEFAULT CHARSET = utf8
