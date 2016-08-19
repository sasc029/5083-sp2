--追加人  ：		kangyanfei
--追加时间：		2014/08/13
--追加表名：		回收销毁发放信息
create table DDM_RECDES_INFO
(
  innerid                VARCHAR2(255) not null,
  classid                VARCHAR2(255) not null,
  disOrderObjectLinkId      VARCHAR2(255) not null,
  disOrderObjectLinkClassId VARCHAR2(255) not null,
  updatetime             NUMBER(19),
  updatecount            NUMBER(10),
  stateid                VARCHAR2(255) not null,
  statename              VARCHAR2(255) not null,
  lifecycletemplate      VARCHAR2(64) not null,
  sendtime               NUMBER(19),
  createbyid             VARCHAR2(255) not null,
  createbyclassid        VARCHAR2(255) not null,
  createtime             NUMBER(19) not null,
  modifybyid             VARCHAR2(255),
  modifybyclassid        VARCHAR2(255),
  modifytime             NUMBER(19),
  needRecoverNum         NUMBER,
  needDestroyNum         NUMBER,
  disinfonum             NUMBER,
  destroynum             NUMBER,
  recovernum             NUMBER,
  disinfoname            VARCHAR2(255),
  disinfoid              VARCHAR2(255),
  infoclassid            VARCHAR2(255),
  disinfotype            NUMBER,
  dismediatype           NUMBER,
  note                   VARCHAR2(1024)
)
/
comment on table DDM_RECDES_INFO is '回收销毁分发信息'
/
comment on column DDM_RECDES_INFO.innerid is '回收销毁分发信息配置内部标识'
/
comment on column DDM_RECDES_INFO.classid is '回收销毁分发信息配置类标识'
/
comment on column DDM_RECDES_INFO.disOrderObjectLinkId is '发放单与分发数据LINK内部标识'
/
comment on column DDM_RECDES_INFO.disOrderObjectLinkClassId is '发放单与分发数据LINK类标识'
/
comment on column DDM_RECDES_INFO.updatetime is '系统用来支持并发的字段'
/
comment on column DDM_RECDES_INFO.updatecount is '系统用来支持并发的字段'
/
comment on column DDM_RECDES_INFO.stateid is '回收销毁分发信息生命周期标识'
/
comment on column DDM_RECDES_INFO.statename is '回收销毁分发信息生命周期名称'
/
comment on column DDM_RECDES_INFO.lifecycletemplate is '回收销毁分发信息生命周期模板'
/
comment on column DDM_RECDES_INFO.sendtime is '回收销毁分发信息变为已分发状态的时间'
/
comment on column DDM_RECDES_INFO.createbyid is '回收销毁分发信息创建人标识'
/
comment on column DDM_RECDES_INFO.createbyclassid is '回收销毁分发信息创建人对象类名'
/
comment on column DDM_RECDES_INFO.createtime is '回收销毁分发信息创建时间'
/
comment on column DDM_RECDES_INFO.modifybyid is '回收销毁分发信息修改人标识'
/
comment on column DDM_RECDES_INFO.modifybyclassid is '回收销毁分发信息修改人对象类名'
/
comment on column DDM_RECDES_INFO.modifytime  is '回收销毁分发信息修改时间'
/
comment on column DDM_RECDES_INFO.needRecoverNum is '需要回收份数'
/
comment on column DDM_RECDES_INFO.needDestroyNum is '需要销毁份数'
/
comment on column DDM_RECDES_INFO.disinfonum is '分发份数'
/
comment on column DDM_RECDES_INFO.destroynum is '销毁份数'
/
comment on column DDM_RECDES_INFO.recovernum  is '回收分数'
/
comment on column DDM_RECDES_INFO.disinfoname is '回收销毁分发信息名称（单位/人员）'
/
comment on column DDM_RECDES_INFO.disinfoid is '回收销毁分发信息IID（人员或组织的内部标识）'
/
comment on column DDM_RECDES_INFO.infoclassid is '回收销毁分发信息的类标识（人员或者组织的类标识）'
/
comment on column DDM_RECDES_INFO.disinfotype is '回收销毁分发信息类型（0为单位，1为人员）'
/
comment on column DDM_RECDES_INFO.dismediatype is '分发介质类型（0为纸质，1为电子，2为跨域）'
/
comment on column DDM_RECDES_INFO.note is '备注'
/
alter table DDM_RECDES_INFO add constraint PK_DDM_RECDES_INFO_IID primary key (INNERID) using index tablespace plmindex
/
CREATE TABLE DDM_RECDES_ELECTASK (
  innerid               VARCHAR2(255) not null,
  classid               VARCHAR2(255) not null ,
  id VARCHAR2(255) null ,
  name VARCHAR2(255) null ,
  note VARCHAR2(1024) null ,
  stateid           VARCHAR2(255) not null,
  statename         VARCHAR2(255) not null,
  lifecycletemplate VARCHAR2(64) not null ,
  contextid VARCHAR2(255) null ,
  contextclassid VARCHAR2(255) null ,
  createbyid             VARCHAR2(255) not null,
  createbyclassid        VARCHAR2(255) not null,
  createtime             NUMBER(19) not null,
  modifybyid             VARCHAR2(255),
  modifybyclassid        VARCHAR2(255),
  modifytime             NUMBER(19),
  updatetime             NUMBER(19),
  updatecount            NUMBER(10),
  receivebyid            VARCHAR2(255) null ,
  receivebyclassid       VARCHAR2(255) null ,
  receivetime            NUMBER(19) null ,
  domainid               VARCHAR2(255) null ,
  domainclassid          VARCHAR2(255) null ,
  fromtaskid             VARCHAR2(255) null ,
  fromtaskclassid        VARCHAR2(255) null ,
  electasktype           NUMBER(1) null ,
  sourcesiteid           VARCHAR2(255) null ,
  sourcesiteclassid VARCHAR2(255) null ,
  sourcesitename VARCHAR2(255) null ,
  targetsiteid VARCHAR2(255) null ,
  targetsiteclassid VARCHAR2(255) null ,
  targetsitename VARCHAR2(255) null ,
  centersiteid VARCHAR2(255) null ,
  centersiteclassid VARCHAR2(255) null ,
  receivebyname VARCHAR2(255) null 
)
/
COMMENT ON TABLE DDM_RECDES_ELECTASK IS '回收销毁电子任务'
/
COMMENT ON COLUMN DDM_RECDES_ELECTASK.innerid IS '内部标识'
/
COMMENT ON COLUMN DDM_RECDES_ELECTASK.classid IS '类标识'
/
COMMENT ON COLUMN DDM_RECDES_ELECTASK.id IS '任务编号'
/
COMMENT ON COLUMN DDM_RECDES_ELECTASK.name IS '任务名称'
/
COMMENT ON COLUMN DDM_RECDES_ELECTASK.note IS '备注'
/
COMMENT ON COLUMN DDM_RECDES_ELECTASK.stateid IS '生命周期状态'
/
COMMENT ON COLUMN DDM_RECDES_ELECTASK.statename IS '生命周期名称'
/
COMMENT ON COLUMN DDM_RECDES_ELECTASK.lifecycletemplate IS '生命周期模板'
/
COMMENT ON COLUMN DDM_RECDES_ELECTASK.contextid IS '上下文内部标识'
/
COMMENT ON COLUMN DDM_RECDES_ELECTASK.contextclassid IS '上下文类名'
/
COMMENT ON COLUMN DDM_RECDES_ELECTASK.createbyid IS '任务创建人标识'
/
COMMENT ON COLUMN DDM_RECDES_ELECTASK.createbyclassid IS '任务创建人类名'
/
COMMENT ON COLUMN DDM_RECDES_ELECTASK.createtime IS '任务创建时间'
/
COMMENT ON COLUMN DDM_RECDES_ELECTASK.modifybyid IS '修改人标识'
/
COMMENT ON COLUMN DDM_RECDES_ELECTASK.modifybyclassid IS '修改人对象类名'
/
COMMENT ON COLUMN DDM_RECDES_ELECTASK.modifytime IS '修改时间'
/
COMMENT ON COLUMN DDM_RECDES_ELECTASK.updatetime IS '系统用来支持并发的字段'
/
COMMENT ON COLUMN DDM_RECDES_ELECTASK.updatecount IS '系统用来支持并发的字段'
/
COMMENT ON COLUMN DDM_RECDES_ELECTASK.receivebyid IS '接收人标识'
/
COMMENT ON COLUMN DDM_RECDES_ELECTASK.receivebyclassid IS '接收人类标识'
/
COMMENT ON COLUMN DDM_RECDES_ELECTASK.receivetime IS '接收时间'
/
COMMENT ON COLUMN DDM_RECDES_ELECTASK.domainid IS '域标识'
/
COMMENT ON COLUMN DDM_RECDES_ELECTASK.domainclassid IS '域对象类名'
/
COMMENT ON COLUMN DDM_RECDES_ELECTASK.electasktype IS '任务类型（0为域内，1为域外）'
/
COMMENT ON COLUMN DDM_RECDES_ELECTASK.sourcesiteid IS '源站点内部表示'
/
COMMENT ON COLUMN DDM_RECDES_ELECTASK.sourcesiteclassid IS '源站点类标识'
/
COMMENT ON COLUMN DDM_RECDES_ELECTASK.sourcesitename IS '源站点名称'
/
COMMENT ON COLUMN DDM_RECDES_ELECTASK.targetsiteid IS '目标站点内部标识'
/
COMMENT ON COLUMN DDM_RECDES_ELECTASK.targetsiteclassid IS '目标站点类标识'
/
COMMENT ON COLUMN DDM_RECDES_ELECTASK.targetsitename IS '目标站点名称'
/
COMMENT ON COLUMN DDM_RECDES_ELECTASK.centersiteid IS '中心站点内部标识'
/
COMMENT ON COLUMN DDM_RECDES_ELECTASK.centersiteclassid IS '中心站点类标识'
/
COMMENT ON COLUMN DDM_RECDES_ELECTASK.receivebyname IS '接收者名称'
/
alter table DDM_RECDES_ELECTASK add constraint PK_DDM_RECDES_ELECTASK_IID primary key (INNERID) using index tablespace plmindex
/


CREATE TABLE DDM_RECDES_PAPERTASK (
  innerid               VARCHAR2(255) not null ,
  classid VARCHAR2(255) not null ,
  id VARCHAR2(255) null ,
  name VARCHAR2(255) null ,
  createbyid VARCHAR2(255) null ,
  createbyclassid VARCHAR2(255) null ,
  createtime NUMBER(19) null ,
  stateid VARCHAR2(255) not null ,
  statename VARCHAR2(255) not null ,
  lifecycletemplate VARCHAR2(64) not null ,
  contextid VARCHAR2(255) null ,
  contextclassid      VARCHAR2(255) null ,
  updatetime      NUMBER(19) null ,
  updatecount      NUMBER(10) null ,
  note      VARCHAR2(1024) null ,
  modifybyid      VARCHAR2(255) null ,
  modifybyclassid      VARCHAR2(255) null ,
  modifytime      NUMBER(19) null ,
  domainid      VARCHAR2(255) null ,
  domainclassid      VARCHAR2(255) null ,
  disurgent      NUMBER null 
)
/
COMMENT ON TABLE DDM_RECDES_PAPERTASK IS '回收销毁纸质任务'
/
COMMENT ON COLUMN DDM_RECDES_PAPERTASK.innerid IS '内部标识'
/
COMMENT ON COLUMN DDM_RECDES_PAPERTASK.classid IS '类标识'
/
COMMENT ON COLUMN DDM_RECDES_PAPERTASK.id IS '任务编号'
/
COMMENT ON COLUMN DDM_RECDES_PAPERTASK.name IS '任务名称'
/
COMMENT ON COLUMN DDM_RECDES_PAPERTASK.createbyid IS '任务创建人标识'
/
COMMENT ON COLUMN DDM_RECDES_PAPERTASK.createbyclassid IS '任务创建人类名'
/
COMMENT ON COLUMN DDM_RECDES_PAPERTASK.createtime IS '任务创建时间'
/
COMMENT ON COLUMN DDM_RECDES_PAPERTASK.stateid IS '生命周期状态'
/
COMMENT ON COLUMN DDM_RECDES_PAPERTASK.statename IS '生命周期名称'
/
COMMENT ON COLUMN DDM_RECDES_PAPERTASK.lifecycletemplate IS '生命周期模板'
/
COMMENT ON COLUMN DDM_RECDES_PAPERTASK.contextid IS '上下文内部标识'
/
COMMENT ON COLUMN DDM_RECDES_PAPERTASK.contextclassid IS '上下文类名'
/
COMMENT ON COLUMN DDM_RECDES_PAPERTASK.updatetime IS '系统用来支持并发的字段'
/
COMMENT ON COLUMN DDM_RECDES_PAPERTASK.updatecount IS '系统用来支持并发的字段'
/
COMMENT ON COLUMN DDM_RECDES_PAPERTASK.note IS '备注'
/
COMMENT ON COLUMN DDM_RECDES_PAPERTASK.modifybyid IS '修改人标识'
/
COMMENT ON COLUMN DDM_RECDES_PAPERTASK.modifybyclassid IS '修改人对象类名'
/
COMMENT ON COLUMN DDM_RECDES_PAPERTASK.modifytime IS '修改时间'
/
COMMENT ON COLUMN DDM_RECDES_PAPERTASK.domainid IS '域标识'
/
COMMENT ON COLUMN DDM_RECDES_PAPERTASK.domainclassid IS '域对象类名'
/
COMMENT ON COLUMN DDM_RECDES_PAPERTASK.disurgent IS '0为普通，1为加急'
/
alter table DDM_RECDES_PAPERTASK add constraint PK_DDM_RECDES_PAPERTASK_IID primary key (INNERID) using index tablespace plmindex
/