--追加人  ：		徐睿
--追加原因：		按对象类型配置不自动发放
create table DDM_DIS_OBJECT_NOTAUTOCREATE
(
  innerid              VARCHAR2(255) not null,
  classid              VARCHAR2(255) not null,
  typeId               VARCHAR2(255),
  typeName             VARCHAR2(255),
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
comment on table DDM_DIS_OBJECT_NOTAUTOCREATE is '对象模型不自动发放配置表'
/
comment on column DDM_DIS_OBJECT_NOTAUTOCREATE.innerid is '内部标识'
/
comment on column DDM_DIS_OBJECT_NOTAUTOCREATE.classid is '内部类标识'
/
alter table DDM_DIS_OBJECT_NOTAUTOCREATE add constraint PK_DDM_DISOBJ_NAC_IID primary key (INNERID) using index tablespace plmindex
/

--增加DDM_DIS_INFO表字段SEALINFO(盖章信息)
alter table DDM_DIS_INFO add SEALINFO VARCHAR2(255);
/
comment on column DDM_DIS_INFO.sealinfo  is '盖章信息'
/

--追加人  ：		张国强
--追加时间：		2014-09-11
--追加原因：		纸质签收任务
--是否影响已有数据：	不影响
--是否依赖其他脚本：	不依赖，若有依赖请列出

create table DDM_DIS_PAPERSIGNTASK
(
  innerid           VARCHAR2(255) not null,
  classid           VARCHAR2(255) not null,
  id                VARCHAR2(255),
  name              VARCHAR2(255),
  note              VARCHAR2(1024),
  stateid           VARCHAR2(255) not null,
  statename         VARCHAR2(255) not null,
  lifecycletemplate VARCHAR2(64) not null,
  contextid         VARCHAR2(255),
  contextclassid    VARCHAR2(255),
  createbyid        VARCHAR2(255),
  createbyclassid   VARCHAR2(255),
  createtime        NUMBER(19),
  modifybyid        VARCHAR2(255),
  modifybyclassid   VARCHAR2(255),
  modifytime        NUMBER(19),
  updatetime        NUMBER(19),
  updatecount       NUMBER(10),
  receivebyid       VARCHAR2(255),
  receivebyclassid  VARCHAR2(255),
  receivetime       NUMBER(19),
  domainid          VARCHAR2(255),
  domainclassid     VARCHAR2(255),
  fromtaskid        VARCHAR2(255),
  fromtaskclassid   VARCHAR2(255),
  electasktype      NUMBER(1),
  sourcesiteid      VARCHAR2(255),
  sourcesiteclassid VARCHAR2(255),
  sourcesitename    VARCHAR2(255),
  targetsiteid      VARCHAR2(255),
  targetsiteclassid VARCHAR2(255),
  targetsitename    VARCHAR2(255),
  centersiteid      VARCHAR2(255),
  centersiteclassid VARCHAR2(255),
  receivebyname     VARCHAR2(255),
  disurgent         NUMBER
)
/

comment on table DDM_DIS_PAPERSIGNTASK is '纸质签收任务'
/
comment on column DDM_DIS_PAPERSIGNTASK.innerid is '内部标识'
/
comment on column DDM_DIS_PAPERSIGNTASK.classid is '类标识'
/
comment on column DDM_DIS_PAPERSIGNTASK.id is '任务编号'
/
comment on column DDM_DIS_PAPERSIGNTASK.name is '任务名称'
/
comment on column DDM_DIS_PAPERSIGNTASK.note is '备注'
/
comment on column DDM_DIS_PAPERSIGNTASK.stateid is '生命周期状态'
/
comment on column DDM_DIS_PAPERSIGNTASK.statename is '生命周期名称'
/
comment on column DDM_DIS_PAPERSIGNTASK.lifecycletemplate is '生命周期模板'
/
comment on column DDM_DIS_PAPERSIGNTASK.contextid is '上下文内部标识'
/
comment on column DDM_DIS_PAPERSIGNTASK.contextclassid is '上下文类名'
/
comment on column DDM_DIS_PAPERSIGNTASK.createbyid is '任务创建人标识'
/
comment on column DDM_DIS_PAPERSIGNTASK.createbyclassid is '任务创建人类名'
/
comment on column DDM_DIS_PAPERSIGNTASK.createtime is '任务创建时间'
/
comment on column DDM_DIS_PAPERSIGNTASK.modifybyid is '修改人标识'
/
comment on column DDM_DIS_PAPERSIGNTASK.modifybyclassid is '修改人对象类名'
/
comment on column DDM_DIS_PAPERSIGNTASK.modifytime is '修改时间'
/
comment on column DDM_DIS_PAPERSIGNTASK.updatetime is '系统用来支持并发的字段'
/
comment on column DDM_DIS_PAPERSIGNTASK.updatecount is '系统用来支持并发的字段'
/
comment on column DDM_DIS_PAPERSIGNTASK.receivebyid is '接收人标识'
/
comment on column DDM_DIS_PAPERSIGNTASK.receivebyclassid is '接收人类标识'
/
comment on column DDM_DIS_PAPERSIGNTASK.receivetime is '接收时间'
/
comment on column DDM_DIS_PAPERSIGNTASK.domainid is '域标识'
/
comment on column DDM_DIS_PAPERSIGNTASK.domainclassid is '域对象类名'
/
comment on column DDM_DIS_PAPERSIGNTASK.electasktype is '任务类型（0为域内，1为域外）'
/
comment on column DDM_DIS_PAPERSIGNTASK.sourcesiteid is '源站点内部表示'
/
comment on column DDM_DIS_PAPERSIGNTASK.sourcesiteclassid is '源站点类标识'
/
comment on column DDM_DIS_PAPERSIGNTASK.sourcesitename is '源站点名称'
/
comment on column DDM_DIS_PAPERSIGNTASK.targetsiteid is '目标站点内部标识'
/
comment on column DDM_DIS_PAPERSIGNTASK.targetsiteclassid is '目标站点类标识'
/
comment on column DDM_DIS_PAPERSIGNTASK.targetsitename is '目标站点名称'
/
comment on column DDM_DIS_PAPERSIGNTASK.centersiteid is '中心站点内部标识'
/
comment on column DDM_DIS_PAPERSIGNTASK.centersiteclassid is '中心站点类标识'
/
comment on column DDM_DIS_PAPERSIGNTASK.receivebyname is '接收者名称'
/
comment on column DDM_DIS_PAPERSIGNTASK.disurgent is '紧急度'
/
alter table DDM_DIS_PAPERSIGNTASK add constraint PK_DDM_DIS_PAPERSIGNTASK_IID primary key (INNERID) using index  tablespace plmindex
/
CREATE INDEX DDM_DIS_PAPERSIGNTASK_IDX1 ON DDM_DIS_PAPERSIGNTASK(CLASSID || ':' ||INNERID)
/


