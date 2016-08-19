create table DDM_DIS_OBJECT_PRINT
(
  innerid     VARCHAR2(255) not null,
  classid     VARCHAR2(255) not null,
  objinnerid  VARCHAR2(255),
  objclassid  VARCHAR2(255),
  taskinnerid VARCHAR2(255),
  taskclassid VARCHAR2(255),
  isprint     NUMBER(10),
  updatetime  NUMBER(19),
  updatecount NUMBER(10)
)
/
comment on table DDM_DIS_OBJECT_PRINT is '任务下的分发数据是否打印表'
/
comment on column DDM_DIS_OBJECT_PRINT.innerid is '内部标识'
/
comment on column DDM_DIS_OBJECT_PRINT.classid is '内部类标识'
/
comment on column DDM_DIS_OBJECT_PRINT.OBJINNERID is '分发数据内部标识'
/
comment on column DDM_DIS_OBJECT_PRINT.OBJCLASSID is '分发数据内部类标识'
/
comment on column DDM_DIS_OBJECT_PRINT.TASKINNERID is '分发任务内部标识'
/
comment on column DDM_DIS_OBJECT_PRINT.TASKCLASSID is '分发任务内部类标识'
/
comment on column DDM_DIS_OBJECT_PRINT.ISPRINT is '分发数据是否打印（1为是，0为否）'
/
alter table DDM_DIS_OBJECT_PRINT add constraint PK_DDM_DIS_OBJECT_PRINT_IID primary key (INNERID) using index tablespace plmindex
/