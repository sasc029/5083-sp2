--增加DDM_DIS_PAPERTASK表 ISPRINT(分发数据主对象类型)
alter table DDM_DIS_PAPERTASK add ISPRINT NUMBER(10)
/
comment on column DDM_DIS_PAPERTASK.ISPRINT is '分发数据是否已打印（1为是，0为否）'
/