create table datas(
id number not null primary key, 
device varchar2(50) not null,
temp number not null,
humi number not null,
co number not null,
rdate varchar2(50) not null);

create table audios(
id number not null primary key, 
device varchar2(50) not null,
filename varchar2(100) not null,
rdate varchar2(50) not null,
kbyte number,
sec number);

create SEQUENCE datas_seq INCREMENT by 1 start with 1 MAXVALUE 99999;
create SEQUENCE audios_seq INCREMENT by 1 start with 1 MAXVALUE 99999;