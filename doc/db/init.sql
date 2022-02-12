-- 笔记
create table `Note` (
    `id` bigint not null comment 'id',
    `notebookid` bigint not null comment '笔记本id',
    `createtime` datetime not null default current_timestamp comment '创建时间',
    `title` varchar(200) not null comment '标题',
    `content` text comment '内容',
    primary key(`id`)
) engine=innodb default charset=utf8mb4 comment='笔记';