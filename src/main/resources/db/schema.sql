DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user` (
                          `id` int NOT NULL AUTO_INCREMENT,
                          `name` varchar(255) NOT NULL,
    `tenant_id` int NOT NULL,
    PRIMARY KEY (`id`)
    );

insert into `t_user` (id,`name`,`tenant_id`) values (1, 'laokou', 0);
