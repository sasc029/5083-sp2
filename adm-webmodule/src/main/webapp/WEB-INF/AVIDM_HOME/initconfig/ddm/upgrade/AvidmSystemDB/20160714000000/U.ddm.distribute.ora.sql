alter table DDM_DIS_ELECTASK add disInfoName VARCHAR2(255)
/
alter table DDM_DIS_ELECTASK add disInfoId VARCHAR2(255)
/
alter table DDM_DIS_ELECTASK add infoClassId VARCHAR2(255)
/

comment on column DDM_DIS_ELECTASK.disInfoName is '�ַ���λ������Ա����'
/
comment on column DDM_DIS_ELECTASK.disInfoId is '�ַ���λ������Աid'
/
comment on column DDM_DIS_ELECTASK.infoClassId is '�ַ���λ������Աclassid'
/

