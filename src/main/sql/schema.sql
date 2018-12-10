-- 数据库初始化脚本

-- 创建数据库
create database seckill;

-- 使用数据库
use seckill;
-- 创建秒杀库存表
create table seckill(
`seckill_id` bigint not null AUTO_INCREMENT COMMENT '商品库存id',
`name` varchar(120) not null comment '商品名称',
`number` int not null comment '库存数量',
`start_time` timestamp not null comment '秒杀开始时间',
`end_time` timestamp not  null comment '秒杀结束时间',
`create_time` timestamp not null default current_timestamp comment '创建时间',
primary key (seckill_id),
key idx_start_time(start_time),
key idx_end_time(end_time),
key idx_create_time(create_time)
)engine=InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8 comment='秒杀库存表';

--初始化数据
insert into
  seckill( name, number, start_time, end_time)
values
  ('1000元秒杀iphone6',100,'2018-11-24 00:00:00','2018-11-25 00:00:00'),
  ('500元秒杀ipd2',100,'2018-11-24 00:00:00','2018-11-25 00:00:00'),
  ('300元秒杀小米4',100,'2018-11-24 00:00:00','2018-11-25 00:00:00'),
  ('100元秒杀红米note',100,'2018-11-24 00:00:00','2018-11-25 00:00:00');

-- 秒杀成功明细表
-- 用户登录认证相关的信息
create table success_killed(
`seckill_id` bigint not null  COMMENT '秒杀商品id',
`user_phone` bigint  not null  COMMENT '用户手机号',
`state` tinyint not null DEFAULT -1  COMMENT '状态标识：-1：无效 0：成功 1：已付款 2：已发货',
`create_time` timestamp not null default current_timestamp comment '创建时间',
primary key (seckill_id,user_phone),/*联合主键*/
key idx_create_time(create_time)
)engine=InnoDB DEFAULT CHARSET=utf8 comment='秒杀成功明细表';

/*windows连接数据库 mysql -uroot -p891110 */

-- 为什么手写DDL
-- 记录每次上线的DDL修改

