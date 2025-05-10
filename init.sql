

-- 用户表
create table if not exists user
(
    id       bigint auto_increment primary key,
    username varchar(128) not null
);


-- 博客表
create table if not exists blog
(
    id         bigint auto_increment primary key,
    userId     bigint                             not null,
    title      varchar(512)                       null comment '标题',
    coverImg   varchar(1024)                      null comment '封面',
    content    text                               not null comment '内容',
    thumbCount int      default 0                 not null comment '点赞数',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间'
);
create index idx_userId on blog (userId);


-- 点赞记录表
create table if not exists thumb
(
    id         bigint auto_increment primary key,
    userId     bigint                             not null,
    blogId     bigint                             not null,
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间'
);
create unique index idx_userId_blogId on thumb (userId, blogId);



# 我自己创建的测试表
# com.github.zeroput.million_thumb
# com.github.zeroput.million_thumb

CREATE TABLE MyTask
(
    TaskID      INT AUTO_INCREMENT PRIMARY KEY, -- 任务ID，主键，自增
    Title       VARCHAR(100) NOT NULL,          -- 任务标题
    IsCompleted BOOLEAN DEFAULT FALSE,          -- 是否完成
    DueDate     DATETIME,                       -- 截止时间
    Priority    ENUM ('Low', 'Medium', 'High')  -- 优先级
);

SELECT *
FROM MyTask;

INSERT INTO MyTask (Title, IsCompleted, DueDate, Priority)
VALUES ('完成月报', FALSE, '2025-05-15 17:00:00', 'High'),
       ('代码重构', TRUE, '2025-04-30 18:30:00', 'Medium'),
       ('预研新技术', FALSE, NULL, 'Low'),
       ('团队会议', TRUE, '2025-05-10 10:00:00', 'High'),
       ('文档整理', FALSE, '2025-05-12 14:00:00', 'Medium');


