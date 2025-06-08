#云原生课程案例子
##1. 项目信息
###项目名：microservice-demo-n
### 包含四个微服务
### 1.注册服务、网关、Feign的client product-client和业务服务product-service.

###by:guoy
##2.项目运行
###2.1 分为本地和k8s集群两种方式
####--在本地运行时注意修改每个项目中的配置文件,修改application.yaml，将 active: k8s改为dev。

使用mysql 8.0.26数据库，创建一个数据库和一个表，后面的微服务会用到它们，现在创建好或者在后面编写
##3.数据库
product-services微服务时再创建也可以。
数据库名：tb_product
表名：product
下面是创建数据库tb_product及product表的sql语句。
create
CREATE DATABASE `tb_product`;
use  `tb_product`;
CREATE TABLE `product` (
`id` int NOT NULL AUTO_INCREMENT,
`product_name` varchar(100) DEFAULT NULL COMMENT '商品名称',
`price` double(15,3) DEFAULT NULL COMMENT '商品价格',
PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

插入数据的语句：
INSERT INTO `tb_product`.`product` (`product_name`,`price`) VALUES ('上衣','100.00');
INSERT INTO `tb_product`.`product` (`product_name`,`price`) VALUES ('裤子','50.00');
INSERT INTO `tb_product`.`product` (`product_name`,`price`) VALUES ('毛衣','200.00');
INSERT INTO `tb_product`.`product` (`product_name`,`price`) VALUES ('帽子','30.00');
INSERT INTO `tb_product`.`product` (`product_name`,`price`) VALUES ('鞋','200.00');

