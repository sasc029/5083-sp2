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
comment on table DDM_DIS_OBJECT_PRINT is '�����µķַ������Ƿ��ӡ��'
/
comment on column DDM_DIS_OBJECT_PRINT.innerid is '�ڲ���ʶ'
/
comment on column DDM_DIS_OBJECT_PRINT.classid is '�ڲ����ʶ'
/
comment on column DDM_DIS_OBJECT_PRINT.OBJINNERID is '�ַ������ڲ���ʶ'
/
comment on column DDM_DIS_OBJECT_PRINT.OBJCLASSID is '�ַ������ڲ����ʶ'
/
comment on column DDM_DIS_OBJECT_PRINT.TASKINNERID is '�ַ������ڲ���ʶ'
/
comment on column DDM_DIS_OBJECT_PRINT.TASKCLASSID is '�ַ������ڲ����ʶ'
/
comment on column DDM_DIS_OBJECT_PRINT.ISPRINT is '�ַ������Ƿ��ӡ��1Ϊ�ǣ�0Ϊ��'
/
alter table DDM_DIS_OBJECT_PRINT add constraint PK_DDM_DIS_OBJECT_PRINT_IID primary key (INNERID) using index tablespace plmindex
/