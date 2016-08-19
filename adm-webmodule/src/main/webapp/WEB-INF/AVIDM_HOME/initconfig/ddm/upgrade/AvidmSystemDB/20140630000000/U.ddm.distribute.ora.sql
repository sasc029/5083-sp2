--增加DDM_DIS_INFO表字段sendtype(内外部组织区分)
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
comment on table DDM_DIS_OBJECT_TYPE is '对象模型配置表'
/
comment on column DDM_DIS_OBJECT_TYPE.innerid is '内部标识'
/
comment on column DDM_DIS_OBJECT_TYPE.classid is '内部类标识'
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
comment on table DDM_DIS_CONFIG_PARAMETER is '发放管理参数配置表'
/
comment on column DDM_DIS_CONFIG_PARAMETER.innerid is '内部标识'
/
comment on column DDM_DIS_CONFIG_PARAMETER.classid is '内部类标识'
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
comment on table DDM_DIS_INFO_CONFIG is '分发信息配置'
/
comment on column DDM_DIS_INFO_CONFIG.innerid is '分发信息配置内部标识'
/
comment on column DDM_DIS_INFO_CONFIG.classid is '分发信息配置类标识'
/
comment on column DDM_DIS_INFO_CONFIG.updatetime is '系统用来支持并发的字段'
/
comment on column DDM_DIS_INFO_CONFIG.updatecount is '系统用来支持并发的字段'
/
comment on column DDM_DIS_INFO_CONFIG.disinfoname is '分发信息配置名称（单位/人员）'
/
comment on column DDM_DIS_INFO_CONFIG.disinfoid is '分发信息配置IID（人员或组织的内部标识）'
/
comment on column DDM_DIS_INFO_CONFIG.disinfotype is '分发信息配置类型（0为单位，1为人员）'
/
comment on column DDM_DIS_INFO_CONFIG.disinfonum is '分发份数'
/
comment on column DDM_DIS_INFO_CONFIG.dismediatype is '分发介质类型（0为纸质，1为电子，2为跨域）'
/
comment on column DDM_DIS_INFO_CONFIG.note is '备注'
/
alter table DDM_DIS_INFO_CONFIG add constraint PK_DDM_DIS_INFO_CONFIG_IID primary key (INNERID) using index tablespace plmindex
/












