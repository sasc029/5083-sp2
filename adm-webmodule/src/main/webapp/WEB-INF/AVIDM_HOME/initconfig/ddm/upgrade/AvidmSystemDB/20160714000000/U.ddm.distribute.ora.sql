alter table DDM_DIS_ELECTASK add disInfoName VARCHAR2(255)
/
alter table DDM_DIS_ELECTASK add disInfoId VARCHAR2(255)
/
alter table DDM_DIS_ELECTASK add infoClassId VARCHAR2(255)
/

comment on column DDM_DIS_ELECTASK.disInfoName is '分发单位或者人员名称'
/
comment on column DDM_DIS_ELECTASK.disInfoId is '分发单位或者人员id'
/
comment on column DDM_DIS_ELECTASK.infoClassId is '分发单位或者人员classid'
/

