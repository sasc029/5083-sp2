--����DDM_DIS_INFO���ֶ�sendtype(���ⲿ��֯����)
alter table DDM_DIS_INFO add SENDTYPE NUMBER
/

alter table DDM_DIS_OBJECT add pages VARCHAR2(255)
/

create table DDM_DIS_OBJECT_TYPE
(
  innerid              VARCHAR2(255) not null,
  classid              VARCHAR2(255) not null,
  dataId               VARCHAR2(255),
  dataClassId          VARCHAR2(255),
  typeId               VARCHAR2(255),
  typeName             VARCHAR2(255),
  state                VARCHAR2(255),
  contextid            VARCHAR2(255),
  contextclassid       VARCHAR2(255),
  domainid             VARCHAR2(255),
  domainclassid        VARCHAR2(255),
  createtime           NUMBER(19),
  modifytime           NUMBER(19),
  createbyid           VARCHAR2(255),
  createbyclassid      VARCHAR2(255),
  modifybyid           VARCHAR2(255),
  modifybyclassid      VARCHAR2(255),
  updatetime           NUMBER(19),
  updatecount          NUMBER(10)
)
/
comment on table DDM_DIS_OBJECT_TYPE is '����ģ�����ñ�'
/
comment on column DDM_DIS_OBJECT_TYPE.innerid is '�ڲ���ʶ'
/
comment on column DDM_DIS_OBJECT_TYPE.classid is '�ڲ����ʶ'
/
alter table DDM_DIS_OBJECT_TYPE add constraint PK_DDM_DIS_OBJECT_TYPE_IID primary key (INNERID) using index tablespace plmindex
/

create table DDM_DIS_CONFIG_PARAMETER
(
  innerid              VARCHAR2(255) not null,
  classid              VARCHAR2(255) not null,
  paramId              VARCHAR2(255),
  paramName            VARCHAR2(255),
  defaultValue         VARCHAR2(255),
  currentValue         VARCHAR2(255),
  state                VARCHAR2(255),
  description          VARCHAR2(255),
  contextid            VARCHAR2(255),
  contextclassid       VARCHAR2(255),
  domainid             VARCHAR2(255),
  domainclassid        VARCHAR2(255),
  createtime           NUMBER(19),
  modifytime           NUMBER(19),
  createbyid           VARCHAR2(255),
  createbyclassid      VARCHAR2(255),
  modifybyid           VARCHAR2(255),
  modifybyclassid      VARCHAR2(255),
  updatetime           NUMBER(19),
  updatecount          NUMBER(10)
)
/
comment on table DDM_DIS_CONFIG_PARAMETER is '���Ź���������ñ�'
/
comment on column DDM_DIS_CONFIG_PARAMETER.innerid is '�ڲ���ʶ'
/
comment on column DDM_DIS_CONFIG_PARAMETER.classid is '�ڲ����ʶ'
/
alter table DDM_DIS_CONFIG_PARAMETER add constraint PK_DDM_DIS_CONFIG_PARAM_IID primary key (INNERID) using index tablespace plmindex
/

create table DDM_DIS_INFO_CONFIG
(
  innerid                VARCHAR2(255) not null,
  classid                VARCHAR2(255) not null,
  updatetime             NUMBER(19),
  updatecount            NUMBER(10),
  disinfoname            VARCHAR2(255),
  disinfoid              VARCHAR2(255),
  disinfotype            NUMBER,
  disinfonum             NUMBER,
  dismediatype           NUMBER,
  note                   VARCHAR2(1024)
)
/
comment on table DDM_DIS_INFO_CONFIG is '�ַ���Ϣ����'
/
comment on column DDM_DIS_INFO_CONFIG.innerid is '�ַ���Ϣ�����ڲ���ʶ'
/
comment on column DDM_DIS_INFO_CONFIG.classid is '�ַ���Ϣ�������ʶ'
/
comment on column DDM_DIS_INFO_CONFIG.updatetime is 'ϵͳ����֧�ֲ������ֶ�'
/
comment on column DDM_DIS_INFO_CONFIG.updatecount is 'ϵͳ����֧�ֲ������ֶ�'
/
comment on column DDM_DIS_INFO_CONFIG.disinfoname is '�ַ���Ϣ�������ƣ���λ/��Ա��'
/
comment on column DDM_DIS_INFO_CONFIG.disinfoid is '�ַ���Ϣ����IID����Ա����֯���ڲ���ʶ��'
/
comment on column DDM_DIS_INFO_CONFIG.disinfotype is '�ַ���Ϣ�������ͣ�0Ϊ��λ��1Ϊ��Ա��'
/
comment on column DDM_DIS_INFO_CONFIG.disinfonum is '�ַ�����'
/
comment on column DDM_DIS_INFO_CONFIG.dismediatype is '�ַ��������ͣ�0Ϊֽ�ʣ�1Ϊ���ӣ�2Ϊ����'
/
comment on column DDM_DIS_INFO_CONFIG.note is '��ע'
/
alter table DDM_DIS_INFO_CONFIG add constraint PK_DDM_DIS_INFO_CONFIG_IID primary key (INNERID) using index tablespace plmindex
/












