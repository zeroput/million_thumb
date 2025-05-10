

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



# 博客测试数据 由AI生成

INSERT INTO blog (id, userId, title, coverImg, content, thumbCount, createTime, updateTime)
VALUES (1, 101, '初识MySQL', 'https://example.com/img1.jpg', '本文介绍了MySQL的基本使用方法。', 5, '2025-05-01 10:00:00',
        '2025-05-01 10:00:00'),
       (2, 102, 'Spring Boot实战', 'https://example.com/img2.jpg', '分享一个基于Spring Boot的实战项目。', 12,
        '2025-05-02 09:30:00', '2025-05-02 09:30:00'),
       (3, 103, '前端性能优化', 'https://example.com/img3.jpg', '如何通过合理手段提升前端性能。', 8,
        '2025-05-03 15:20:00', '2025-05-03 15:20:00'),
       (4, 101, 'Nginx配置详解', 'https://example.com/img4.jpg', '深入了解Nginx的配置与优化。', 20,
        '2025-05-04 11:00:00', '2025-05-04 11:00:00'),
       (5, 104, '使用Redis实现缓存', 'https://example.com/img5.jpg', '讲解如何在项目中使用Redis缓存。', 3,
        '2025-05-05 16:45:00', '2025-05-05 16:45:00'),
       (6, 105, 'Linux常用命令速查', 'https://example.com/img6.jpg', '收录常用Linux命令及其示例。', 7,
        '2025-05-06 08:50:00', '2025-05-06 08:50:00'),
       (7, 106, '深入理解JVM内存模型', 'https://example.com/img7.jpg', '分析JVM的内存结构和GC机制。', 15,
        '2025-05-07 14:30:00', '2025-05-07 14:30:00'),
       (8, 107, '前端模块化开发指南', 'https://example.com/img8.jpg', '介绍ES模块和Webpack配置实践。', 6,
        '2025-05-08 10:10:00', '2025-05-08 10:10:00'),
       (9, 108, 'Docker基础入门', 'https://example.com/img9.jpg', '快速入门Docker容器技术。', 9, '2025-05-09 13:00:00',
        '2025-05-09 13:00:00'),
       (10, 109, '设计模式总结', 'https://example.com/img10.jpg', '总结常用的Java设计模式。', 18, '2025-05-10 12:00:00',
        '2025-05-10 12:00:00');
