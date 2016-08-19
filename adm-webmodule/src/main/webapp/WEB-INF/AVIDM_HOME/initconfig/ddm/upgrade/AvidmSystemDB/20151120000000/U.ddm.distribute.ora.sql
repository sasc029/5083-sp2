--增加DDM_RECDES_INFO表 字段distype(分发方式)
alter table DDM_RECDES_INFO add distype NUMBER
/
comment on column DDM_RECDES_INFO.distype is '分发方式（0为直接分发，1为补发，2为移除，3为转发，4为回收，5为销毁）'
