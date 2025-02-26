LOCK TABLES `admin` WRITE;
INSERT IGNORE INTO `admin` (`id`,`user_name`, `password`, `first_name`, `last_name`, `email`) 
VALUES (999999,'admin', 'admin', 'admin', 'admin', 'admin@gmail.com');
UNLOCK TABLES;

