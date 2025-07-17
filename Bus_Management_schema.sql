create database BusManagement;
use BusManagement;
create table bus(
bus_id int auto_increment primary key,
bus_no varchar(255) not null,
seats int not null,
ticket_price double not null,
depot varchar(255),
start varchar(255) not null,
destination varchar(255) not null
);
create table booking(
 name varchar(255) not null,
 cst_id int primary key,
 bus_id int not null,
 status int not null,
 price double not null);