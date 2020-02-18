CREATE TABLE `majiang`.`article`(
  `id` INT(50) NOT NULL AUTO_INCREMENT,
  `title` VARCHAR(50) NOT NULL,
  `desc` TEXT NOT NULL,
  `gmt_create` BIGINT,
  `gmt_modified` BIGINT,
  `creator` INT,
  `comment_count` INT DEFAULT 0,
  `view_count` INT DEFAULT 0,
  `like_count` INT DEFAULT 0,
  `tags` VARCHAR(256),
  PRIMARY KEY (`id`)
) ENGINE=INNODB CHARSET=utf8;
