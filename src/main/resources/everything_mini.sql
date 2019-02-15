drop table if exists file_index;
create table file_index(
  name varchar(256) not null comment '文件名称',
  path varchar(2048) not null comment '文件路径',
  depth int not null comment '文件路径深度',
  file_type varchar(32) not null comment '文件类型'
);

