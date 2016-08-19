create table DDM_DIS_DUPLICATE
(
  innerid             VARCHAR2(255) not null,
  classid             VARCHAR2(255) not null,
  disobjectinnerid    VARCHAR2(255),
  disobjectclassid    VARCHAR2(255),
  dispapertaskinnerid VARCHAR2(255),
  dispapertaskclassid VARCHAR2(255),
  contractor          VARCHAR2(255),
  collator            VARCHAR2(255),
  finishtime          NUMBER(19),
  updatetime          NUMBER(19),
  updatecount         NUMBER(10)
)
/

comment on table DDM_DIS_DUPLICATE is '复制加工'
/
comment on column DDM_DIS_DUPLICATE.innerid is '内部标识'
/
comment on column DDM_DIS_DUPLICATE.classid is '内部类标识'
/
comment on column DDM_DIS_DUPLICATE.disobjectinnerid is '分发数据内部标识'
/
comment on column DDM_DIS_DUPLICATE.disobjectclassid is '分发数据内部类标识'
/
comment on column DDM_DIS_DUPLICATE.dispapertaskinnerid is '关联纸质分发任务内部标识'
/
comment on column DDM_DIS_DUPLICATE.dispapertaskclassid is '关联纸质分发任务内部类标识'
/
comment on column DDM_DIS_DUPLICATE.contractor is '复印人'
/
comment on column DDM_DIS_DUPLICATE.collator is '整理人'
/
comment on column DDM_DIS_DUPLICATE.finishtime is '完成时间'
/
comment on column DDM_DIS_DUPLICATE.updatetime is '系统用来支持并发的字段'
/
comment on column DDM_DIS_DUPLICATE.updatecount is '系统用来支持并发的字段'
/
  
alter table DDM_DIS_DUPLICATE  add constraint PK_DDM_DIS_DUPLICATE_IID primary key (INNERID) using index  tablespace plmindex
/
CREATE INDEX DDM_DIS_DUPLICATE_IDX1 ON DDM_DIS_DUPLICATE (disPaperTaskClassId || ':' || disPaperTaskInnerId)
/
CREATE INDEX DDM_DIS_DUPLICATE_IDX2 ON DDM_DIS_DUPLICATE (disObjectClassId || ':' || disObjectInnerId)
/

create table DDM_DIS_ELECTASK
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
  disInfoName		VARCHAR2(255),
  disInfoId 		VARCHAR2(255),
  infoClassId 		VARCHAR2(255)
  

)
/

comment on table DDM_DIS_ELECTASK is '电子任务'
/
comment on column DDM_DIS_ELECTASK.innerid is '内部标识'
/
comment on column DDM_DIS_ELECTASK.classid is '类标识'
/
comment on column DDM_DIS_ELECTASK.id is '任务编号'
/
comment on column DDM_DIS_ELECTASK.name is '任务名称'
/
comment on column DDM_DIS_ELECTASK.note is '备注'
/
comment on column DDM_DIS_ELECTASK.stateid is '生命周期状态'
/
comment on column DDM_DIS_ELECTASK.statename is '生命周期名称'
/
comment on column DDM_DIS_ELECTASK.lifecycletemplate is '生命周期模板'
/
comment on column DDM_DIS_ELECTASK.contextid is '上下文内部标识'
/
comment on column DDM_DIS_ELECTASK.contextclassid is '上下文类名'
/
comment on column DDM_DIS_ELECTASK.createbyid is '任务创建人标识'
/
comment on column DDM_DIS_ELECTASK.createbyclassid is '任务创建人类名'
/
comment on column DDM_DIS_ELECTASK.createtime is '任务创建时间'
/
comment on column DDM_DIS_ELECTASK.modifybyid is '修改人标识'
/
comment on column DDM_DIS_ELECTASK.modifybyclassid is '修改人对象类名'
/
comment on column DDM_DIS_ELECTASK.modifytime is '修改时间'
/
comment on column DDM_DIS_ELECTASK.updatetime is '系统用来支持并发的字段'
/
comment on column DDM_DIS_ELECTASK.updatecount is '系统用来支持并发的字段'
/
comment on column DDM_DIS_ELECTASK.receivebyid is '接收人标识'
/
comment on column DDM_DIS_ELECTASK.receivebyclassid is '接收人类标识'
/
comment on column DDM_DIS_ELECTASK.receivetime is '接收时间'
/
comment on column DDM_DIS_ELECTASK.domainid is '域标识'
/
comment on column DDM_DIS_ELECTASK.domainclassid is '域对象类名'
/
comment on column DDM_DIS_ELECTASK.electasktype is '任务类型（0为域内，1为域外）'
/
comment on column DDM_DIS_ELECTASK.sourcesiteid is '源站点内部表示'
/
comment on column DDM_DIS_ELECTASK.sourcesiteclassid is '源站点类标识'
/
comment on column DDM_DIS_ELECTASK.sourcesitename is '源站点名称'
/
comment on column DDM_DIS_ELECTASK.targetsiteid is '目标站点内部标识'
/
comment on column DDM_DIS_ELECTASK.targetsiteclassid is '目标站点类标识'
/
comment on column DDM_DIS_ELECTASK.targetsitename is '目标站点名称'
/
comment on column DDM_DIS_ELECTASK.centersiteid is '中心站点内部标识'
/
comment on column DDM_DIS_ELECTASK.centersiteclassid is '中心站点类标识'
/
comment on column DDM_DIS_ELECTASK.receivebyname is '接收者名称'
/
comment on column DDM_DIS_ELECTASK.infoClassId is '分发单位或者人员classid'
/
comment on column DDM_DIS_ELECTASK.disInfoId is '分发单位或者人员id'
/
comment on column DDM_DIS_ELECTASK.disInfoName is '分发单位或者人员名称'
/

alter table DDM_DIS_ELECTASK add constraint PK_DDM_DIS_ELECTASK_IID primary key (INNERID) using index  tablespace plmindex
/
CREATE INDEX DDM_DIS_ELECTASK_IDX1 ON DDM_DIS_ELECTASK(CLASSID || ':' ||INNERID)
/
create table DDM_DIS_INFO
(
  innerid                VARCHAR2(255) not null,
  classid                VARCHAR2(255) not null,
  disorderobjlinkid      VARCHAR2(255),
  disorderobjlinkclassid VARCHAR2(255),
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
  disinfoname            VARCHAR2(255),
  disinfoid              VARCHAR2(255),
  infoclassid            VARCHAR2(255),
  disinfotype            NUMBER,
  disinfonum             NUMBER,
  dismediatype           NUMBER,
  distype                NUMBER,
  note                   VARCHAR2(1024),
  destroynum             NUMBER,
  recovernum             NUMBER,
  outsignid              VARCHAR2(255),
  outsignclassid         VARCHAR2(255),
  outsignname            VARCHAR2(255),
  sendtype				 NUMBER,
  sealinfo				 VARCHAR2(255)
)
/

comment on table DDM_DIS_INFO is '分发信息'
/
comment on column DDM_DIS_INFO.innerid is '分发信息内部标识'
/
comment on column DDM_DIS_INFO.classid is '分发信息类标识'
/
comment on column DDM_DIS_INFO.disorderobjlinkid is '发放单与分发数据LINK内部标识'
/
comment on column DDM_DIS_INFO.disorderobjlinkclassid is '发放单与分发数据LINK类标识'
/
comment on column DDM_DIS_INFO.updatetime is '系统用来支持并发的字段'
/
comment on column DDM_DIS_INFO.updatecount is '系统用来支持并发的字段'
/
comment on column DDM_DIS_INFO.stateid is '分发信息生命周期标识'
/
comment on column DDM_DIS_INFO.statename is '分发信息生命周期名称'
/
comment on column DDM_DIS_INFO.lifecycletemplate is '分发信息生命周期模板'
/
comment on column DDM_DIS_INFO.sendtime is '分发信息变为已分发状态的时间'
/
comment on column DDM_DIS_INFO.createbyid is '分发信息创建人标识'
/
comment on column DDM_DIS_INFO.createbyclassid is '分发信息创建人对象类名'
/
comment on column DDM_DIS_INFO.createtime is '分发信息创建时间'
/
comment on column DDM_DIS_INFO.modifybyid is '分发信息修改人标识'
/
comment on column DDM_DIS_INFO.modifybyclassid is '分发信息修改人对象类名'
/
comment on column DDM_DIS_INFO.modifytime  is '分发信息修改时间'
/
comment on column DDM_DIS_INFO.disinfoname is '分发信息名称（单位/人员）'
/
comment on column DDM_DIS_INFO.disinfoid is '分发信息IID（人员或组织的内部标识）'
/
comment on column DDM_DIS_INFO.infoclassid is '分发信息的类标识（人员或者组织的类标识）'
/
comment on column DDM_DIS_INFO.disinfotype is '分发信息类型（0为单位，1为人员）'
/
comment on column DDM_DIS_INFO.disinfonum is '分发份数'
/
comment on column DDM_DIS_INFO.dismediatype is '分发介质类型（0为纸质，1为电子，2为跨域）'
/
comment on column DDM_DIS_INFO.distype is '分发方式（0为直接分发，1为补发，2为移除，3为转发）'
/
comment on column DDM_DIS_INFO.note is '备注'
/
comment on column DDM_DIS_INFO.destroynum is '销毁份数'
/
comment on column DDM_DIS_INFO.recovernum  is '回收分数'
/
comment on column DDM_DIS_INFO.outsignid  is '外域接收人IID（人员内部标识）'
/
comment on column DDM_DIS_INFO.outsignclassid  is '外域接收人的类标识（人员类标识）'
/
comment on column DDM_DIS_INFO.outsignname  is '外域接收人名称（人员）'
/
comment on column DDM_DIS_INFO.sendtype  is '组织类型（0为内部,1为外部）'
/
comment on column DDM_DIS_INFO.sealinfo  is '盖章信息'
/
  
alter table DDM_DIS_INFO add constraint PK_DDM_DIS_INFO_IID primary key (INNERID) using index tablespace plmindex
/
CREATE INDEX DDM_DIS_INFO_IDX1 ON DDM_DIS_INFO(CLASSID || ':' ||INNERID)
/
CREATE INDEX DDM_DIS_INFO_IDX2 ON DDM_DIS_INFO(INFOCLASSID || ':' || DISINFOID)
/
CREATE INDEX DDM_DIS_INFO_IDX3 ON DDM_DIS_INFO(disOrderObjLinkClassId || ':' || disOrderObjLinkId)
/
create table DDM_DIS_OBJECT
(
  innerid              VARCHAR2(255) not null,
  classid              VARCHAR2(255) not null,
  datainnerid          VARCHAR2(255) not null,
  dataclassid          VARCHAR2(255) not null,
  datafrom             VARCHAR2(255) not null,
  id                   VARCHAR2(255),
  name                 VARCHAR2(255),
  note                 VARCHAR2(1024),
  updatetime           NUMBER(19),
  updatecount          NUMBER(10),
  createtime           NUMBER(19),
  modifytime           NUMBER(19),
  createbyid           VARCHAR2(255),
  createbyclassid      VARCHAR2(255),
  modifybyid           VARCHAR2(255),
  modifybyclassid      VARCHAR2(255),
  contextid            VARCHAR2(255),
  contextclassid       VARCHAR2(255),
  domainid             VARCHAR2(255),
  domainclassid        VARCHAR2(255),
  versionno            VARCHAR2(255),
  iterationno          VARCHAR2(255),
  latestinbranch       VARCHAR2(1),
  latestinlevel        VARCHAR2(1),
  versionlevel         NUMBER(10),
  versionsortno        VARCHAR2(255),
  checkoutstate        VARCHAR2(255),
  datelock             NUMBER(19),
  checkinnote          VARCHAR2(1000),
  createstamp          NUMBER(19),
  modifystamp          NUMBER(19),
  updatestamp          NUMBER(19),
  predecessorid        VARCHAR2(255),
  predecessorclassid   VARCHAR2(255),
  controlbranchid      VARCHAR2(255),
  controlbranchclassid VARCHAR2(255),
  lockerid             VARCHAR2(255),
  lockerclassid        VARCHAR2(255),
  creatorid            VARCHAR2(255),
  creatorclassid       VARCHAR2(255),
  modifierid           VARCHAR2(255),
  modifierclassid      VARCHAR2(255),
  phaseid              VARCHAR2(255),
  phaseclassid         VARCHAR2(255),
  disciplineid         VARCHAR2(255),
  disciplineclassid    VARCHAR2(255),
  departmentid         VARCHAR2(255),
  departmentclassid    VARCHAR2(255),
  securitylevelid      VARCHAR2(255),
  securitylevelclassid VARCHAR2(255),
  type                 VARCHAR2(255),
  code                 VARCHAR2(255),
  linkurl              VARCHAR2(1024),
  accessurl            VARCHAR2(1024),
  pages				   VARCHAR2(255)
)
/
comment on column DDM_DIS_OBJECT.linkurl is '分发数据链接url'
/
comment on column DDM_DIS_OBJECT.accessurl is '接收方访问分发数据的链接'
/
alter table DDM_DIS_OBJECT add constraint PK_DDM_DIS_OBJECT_IID primary key (INNERID) using index tablespace plmindex
/
CREATE INDEX DDM_DIS_OBJECT_IDX1 ON DDM_DIS_OBJECT(CLASSID || ':' ||INNERID)
/
CREATE INDEX DDM_DIS_OBJECT_IDX2 ON DDM_DIS_OBJECT(DATACLASSID || ':' || DATAINNERID)
/
create table DDM_DIS_ORDER
(
  innerid           VARCHAR2(255) not null,
  classid           VARCHAR2(255) not null,
  id                VARCHAR2(255) not null,
  name              VARCHAR2(255) not null,
  createbyid        VARCHAR2(255) not null,
  createbyclassid   VARCHAR2(255) not null,
  createtime        NUMBER(19) not null,
  modifybyid        VARCHAR2(255) not null,
  modifybyclassid   VARCHAR2(255) not null,
  modifytime        NUMBER(19) not null,
  stateid           VARCHAR2(255),
  statename         VARCHAR2(255),
  lifecycletemplate VARCHAR2(64),
  contextid         VARCHAR2(255) not null,
  contextclassid    VARCHAR2(255) not null,
  ordertype         NUMBER,
  updatetime        NUMBER(19),
  updatecount       NUMBER(10),
  note              VARCHAR2(1024),
  domainid          VARCHAR2(255),
  domainclassid     VARCHAR2(255),
  submituserid      VARCHAR2(255),
  submituserclassid VARCHAR2(255),
  submitusername    VARCHAR2(255),
  siteid            VARCHAR2(255),
  siteclassid       VARCHAR2(255),
  sitename          VARCHAR2(255),
  MASTERDATACLASSID VARCHAR2(255)
)
/

comment on table DDM_DIS_ORDER is '发放单对象表'
/
comment on column DDM_DIS_ORDER.innerid is '内部标识'
/
comment on column DDM_DIS_ORDER.classid is '类名'
/
comment on column DDM_DIS_ORDER.id is '编号'
/
comment on column DDM_DIS_ORDER.name is '名称'
/
comment on column DDM_DIS_ORDER.createbyid is '创建人标识'
/
comment on column DDM_DIS_ORDER.createbyclassid  is '创建人对象类名'
/
comment on column DDM_DIS_ORDER.createtime is '创建时间'
/
comment on column DDM_DIS_ORDER.modifybyid is '修改人标识'
/
comment on column DDM_DIS_ORDER.modifybyclassid is '修改人对象类名'
/
comment on column DDM_DIS_ORDER.modifytime is '修改时间'
/
comment on column DDM_DIS_ORDER.stateid is '生命周期标识'
/
comment on column DDM_DIS_ORDER.statename is '生命周期名称'
/
comment on column DDM_DIS_ORDER.lifecycletemplate is '生命周期模板'
/
comment on column DDM_DIS_ORDER.contextid is '上下文标识'
/
comment on column DDM_DIS_ORDER.contextclassid is '上下文类名'
/
comment on column DDM_DIS_ORDER.ordertype is '单据类型(0发放单、1补发发放单)'
/
comment on column DDM_DIS_ORDER.updatetime is '系统用来支持并发的字段'
/
comment on column DDM_DIS_ORDER.updatecount is '系统用来支持并发的字段'
/
comment on column DDM_DIS_ORDER.note is '备注'
/
comment on column DDM_DIS_ORDER.domainid is '域标识'
/
comment on column DDM_DIS_ORDER.domainclassid is '域对象类名'
/
comment on column DDM_DIS_ORDER.submituserid is '发起人内部标识'
/
comment on column DDM_DIS_ORDER.submituserclassid is '发起人类标识'
/
comment on column DDM_DIS_ORDER.submitusername is '发起人名称'
/
comment on column DDM_DIS_ORDER.siteid is '站点内部标识'
/
comment on column DDM_DIS_ORDER.siteclassid is '站点类标识'
/
comment on column DDM_DIS_ORDER.sitename is '站点名称'
/
comment on column DDM_DIS_ORDER.MASTERDATACLASSID is '分发数据主对象类型'
/

alter table DDM_DIS_ORDER add constraint PK_DDM_DIS_ORDER_IID primary key (INNERID) using index tablespace plmindex
/
CREATE INDEX DDM_DIS_ORDER_IDX1 ON DDM_DIS_ORDER(CLASSID || ':' ||INNERID)
/
create table DDM_DIS_ORDEROBJLINK
(
  innerid           VARCHAR2(255) not null,
  classid           VARCHAR2(255) not null,
  fromobjectid      VARCHAR2(255),
  fromobjectclassid VARCHAR2(255),
  toobjectid        VARCHAR2(255),
  toobjectclassid   VARCHAR2(255),
  updatetime        NUMBER(19),
  updatecount       NUMBER(10),
  isparent          NUMBER,
  stateid           VARCHAR2(255),
  statename         VARCHAR2(255),
  lifecycletemplate VARCHAR2(64),
  ismaster          NUMBER,
  disdeadline       NUMBER,
  disurgent         NUMBER,
  disstyle          NUMBER
)
/

comment on table DDM_DIS_ORDEROBJLINK is '发放单与分发数据link表'
/
comment on column DDM_DIS_ORDEROBJLINK.innerid is '内部标识'
/
comment on column DDM_DIS_ORDEROBJLINK.classid is '类名'
/
comment on column DDM_DIS_ORDEROBJLINK.fromobjectid is '发放单内部标识'
/
comment on column DDM_DIS_ORDEROBJLINK.fromobjectclassid is '发放单类名'
/
comment on column DDM_DIS_ORDEROBJLINK.toobjectid is '分发数据源内部标识'
/
comment on column DDM_DIS_ORDEROBJLINK.toobjectclassid is '分发数据源类名'
/
comment on column DDM_DIS_ORDEROBJLINK.updatetime is '系统用来支持并发的字段'
/
comment on column DDM_DIS_ORDEROBJLINK.updatecount is '系统用来支持并发的字段'
/
comment on column DDM_DIS_ORDEROBJLINK.isparent is '是否在这个发放单内是父节点（0：不是，1：是）'
/
comment on column DDM_DIS_ORDEROBJLINK.stateid is '生命周期标识'
/
comment on column DDM_DIS_ORDEROBJLINK.statename is '生命周期名称'
/
comment on column DDM_DIS_ORDEROBJLINK.lifecycletemplate is '生命周期模板'
/
comment on column DDM_DIS_ORDEROBJLINK.ismaster is '是否是主对象'
/
comment on column DDM_DIS_ORDEROBJLINK.disdeadline is '分发信息完工期限'
/
comment on column DDM_DIS_ORDEROBJLINK.disurgent is '0为普通，1为加急'
/
comment on column DDM_DIS_ORDEROBJLINK.disstyle is '0为正式分发，1一次性分发'
/  
alter table DDM_DIS_ORDEROBJLINK add constraint PK_DDM_DIS_ORDEROBJLINK_IID primary key (INNERID) using index tablespace plmindex
/
CREATE INDEX DDM_DIS_ORDEROBJLINK_IDX1 ON DDM_DIS_ORDEROBJLINK(CLASSID || ':' ||INNERID)
/
CREATE INDEX DDM_DIS_ORDEROBJLINK_IDX2 ON DDM_DIS_ORDEROBJLINK(FROMOBJECTCLASSID || ':' || FROMOBJECTID)
/
CREATE INDEX DDM_DIS_ORDEROBJLINK_IDX3 ON DDM_DIS_ORDEROBJLINK(TOOBJECTCLASSID || ':' || TOOBJECTID)
/
create table DDM_DIS_PAPERTASK
(
  innerid           VARCHAR2(255) not null,
  classid           VARCHAR2(255) not null,
  id                VARCHAR2(255),
  name              VARCHAR2(255),
  createbyid        VARCHAR2(255),
  createbyclassid   VARCHAR2(255),
  createtime        NUMBER(19),
  stateid           VARCHAR2(255) not null,
  statename         VARCHAR2(255) not null,
  lifecycletemplate VARCHAR2(64) not null,
  contextid         VARCHAR2(255),
  contextclassid    VARCHAR2(255),
  updatetime        NUMBER(19),
  updatecount       NUMBER(10),
  note              VARCHAR2(1024),
  modifybyid        VARCHAR2(255),
  modifybyclassid   VARCHAR2(255),
  modifytime        NUMBER(19),
  domainid          VARCHAR2(255),
  domainclassid     VARCHAR2(255),
  disurgent         NUMBER,
  disordercreator   VARCHAR2(255),
  isprint  			NUMBER(10)
)
/

comment on table DDM_DIS_PAPERTASK is '纸质任务'
/
comment on column DDM_DIS_PAPERTASK.innerid is '内部标识'
/
comment on column DDM_DIS_PAPERTASK.classid is '类标识'
/
comment on column DDM_DIS_PAPERTASK.id is '任务编号'
/
comment on column DDM_DIS_PAPERTASK.name is '任务名称'
/
comment on column DDM_DIS_PAPERTASK.createbyid is '任务创建人标识'
/
comment on column DDM_DIS_PAPERTASK.createbyclassid is '任务创建人类名'
/
comment on column DDM_DIS_PAPERTASK.createtime is '任务创建时间'
/
comment on column DDM_DIS_PAPERTASK.stateid is '生命周期状态'
/
comment on column DDM_DIS_PAPERTASK.statename is '生命周期名称'
/
comment on column DDM_DIS_PAPERTASK.lifecycletemplate is '生命周期模板'
/
comment on column DDM_DIS_PAPERTASK.contextid is '上下文内部标识'
/
comment on column DDM_DIS_PAPERTASK.contextclassid  is '上下文类名'
/
comment on column DDM_DIS_PAPERTASK.updatetime  is '系统用来支持并发的字段'
/
comment on column DDM_DIS_PAPERTASK.updatecount is '系统用来支持并发的字段'
/
comment on column DDM_DIS_PAPERTASK.note is '备注'
/
comment on column DDM_DIS_PAPERTASK.modifybyid is '修改人标识'
/
comment on column DDM_DIS_PAPERTASK.modifybyclassid is '修改人对象类名'
/
comment on column DDM_DIS_PAPERTASK.modifytime is '修改时间'
/
comment on column DDM_DIS_PAPERTASK.domainid is '域标识'
/
comment on column DDM_DIS_PAPERTASK.domainclassid is '域对象类名'
/
comment on column DDM_DIS_PAPERTASK.disurgent is '0为普通，1为加急'
/
comment on column DDM_DIS_PAPERTASK.disordercreator is '发放单创建者'
/
comment on column DDM_DIS_PAPERTASK.ISPRINT is '分发数据是否已打印（1为是，0为否）'
/
alter table DDM_DIS_PAPERTASK add constraint PK_DDM_DIS_PAPERTASK_IID primary key (INNERID) using index tablespace plmindex
/
CREATE INDEX DDM_DIS_PAPERTASK_IDX1 ON DDM_DIS_PAPERTASK(CLASSID || ':' ||INNERID)
/
create table DDM_DIS_RETURN
(
  innerid           VARCHAR2(255) not null,
  classid           VARCHAR2(255) not null,
  objectid          VARCHAR2(255),
  objectclassid     VARCHAR2(255),
  stateid           VARCHAR2(255) not null,
  statename         VARCHAR2(255) not null,
  lifecycletemplate VARCHAR2(64) not null,
  returncount       NUMBER(10),
  returnreason      VARCHAR2(1024),
  updatetime        NUMBER(19),
  updatecount       NUMBER(10),
  taskid            VARCHAR2(255),
  taskclassid       VARCHAR2(255),
  userid            VARCHAR2(255),
  userclassid       VARCHAR2(255),
  username          VARCHAR2(255)
)
/

comment on table DDM_DIS_RETURN is '回退理由'
/
comment on column DDM_DIS_RETURN.innerid is '内部标识'
/
comment on column DDM_DIS_RETURN.classid is '类标识'
/
comment on column DDM_DIS_RETURN.objectid is '对象内部标识'
/
comment on column DDM_DIS_RETURN.objectclassid is '对象类标识'
/
comment on column DDM_DIS_RETURN.stateid is '生命周期状态'
/
comment on column DDM_DIS_RETURN.statename is '生命周期名称'
/
comment on column DDM_DIS_RETURN.lifecycletemplate is '生命周期模板'
/
comment on column DDM_DIS_RETURN.returncount is '回退次数'
/
comment on column DDM_DIS_RETURN.returnreason is '回退原因'
/
comment on column DDM_DIS_RETURN.updatetime is '系统用来支持并发的字段'
/
comment on column DDM_DIS_RETURN.updatecount is '系统用来支持并发的字段'
/

alter table DDM_DIS_RETURN add constraint PK_DDM_DIS_RETURN_IID primary key (INNERID) using index tablespace plmindex
/
CREATE INDEX DDM_DIS_RETURN_IDX1 ON DDM_DIS_RETURN(TASKCLASSID || ':' || TASKID)
/
CREATE INDEX DDM_DIS_RETURN_IDX2 ON DDM_DIS_RETURN(OBJECTCLASSID || ':' || OBJECTID)
/
CREATE INDEX DDM_DIS_RETURN_IDX3 ON DDM_DIS_RETURN(CLASSID || ':' || INNERID)
/
create table DDM_DIS_SERIANO
(
  seriano           NUMBER(6) not null,
  createserianotime NUMBER(19) not null,
  ordertype         NUMBER,
  updatetime        NUMBER(19),
  updatecount       NUMBER(10),
  innerid           VARCHAR2(255) not null,
  classid           VARCHAR2(255) not null
)
/

comment on table DDM_DIS_SERIANO is '流水号'
/
comment on column DDM_DIS_SERIANO.seriano is '流水号'
/
comment on column DDM_DIS_SERIANO.createserianotime is '创建时间'
/
comment on column DDM_DIS_SERIANO.ordertype is '单据类型(0发放单、1补发发放单)'
/
comment on column DDM_DIS_SERIANO.updatetime is '系统用来支持并发的字段'
/
comment on column DDM_DIS_SERIANO.updatecount is '系统用来支持并发的字段'
/
comment on column DDM_DIS_SERIANO.innerid is '内部标识'
/
comment on column DDM_DIS_SERIANO.classid  is '类名'
/

alter table DDM_DIS_SERIANO add constraint PK_DDM_DIS_SERIANO_IID primary key (INNERID) using index tablespace plmindex
/

create table DDM_DIS_TASKINFOLINK
(
  innerid           VARCHAR2(255) not null,
  classid           VARCHAR2(255) not null,
  fromobjectid      VARCHAR2(255),
  fromobjectclassid VARCHAR2(255),
  toobjectid        VARCHAR2(255),
  toobjectclassid   VARCHAR2(255),
  updatetime        NUMBER(19),
  updatecount       NUMBER(10),
  tasktype          NUMBER
)
/

comment on table DDM_DIS_TASKINFOLINK is '分发信息与纸质任务link'
/
comment on column DDM_DIS_TASKINFOLINK.innerid is '内部标识'
/
comment on column DDM_DIS_TASKINFOLINK.classid is '类名'
/
comment on column DDM_DIS_TASKINFOLINK.fromobjectid is '任务内部标识'
/
comment on column DDM_DIS_TASKINFOLINK.fromobjectclassid is '任务类名'
/
comment on column DDM_DIS_TASKINFOLINK.toobjectid is '分发信息内部标识'
/
comment on column DDM_DIS_TASKINFOLINK.toobjectclassid is '分发信息类名'
/
comment on column DDM_DIS_TASKINFOLINK.updatetime is '系统用来支持并发的字段'
/
comment on column DDM_DIS_TASKINFOLINK.updatecount is '系统用来支持并发的字段'
/
comment on column DDM_DIS_TASKINFOLINK.tasktype is '任务类型（0：纸质任务，1：电子任务）'
/ 

alter table DDM_DIS_TASKINFOLINK add constraint PK_DDM_DIS_TASKINFOLINK_IID primary key (INNERID) using index tablespace plmindex
/
CREATE INDEX DDM_DIS_TASKINFOLINK_IDX1 ON DDM_DIS_TASKINFOLINK(fromObjectClassId || ':' || fromObjectId)
/
CREATE INDEX DDM_DIS_TASKINFOLINK_IDX2 ON DDM_DIS_TASKINFOLINK(toObjectClassId || ':' || toObjectId)
/
create table DDM_DIS_WORKLOAD
(
  innerid               VARCHAR2(255) not null,
  classid               VARCHAR2(255) not null,
  objectid              VARCHAR2(255),
  objectclassid         VARCHAR2(255),
  fromstateid           VARCHAR2(255),
  fromstatename         VARCHAR2(255),
  fromlifecycletemplate VARCHAR2(64),
  tostateid             VARCHAR2(255),
  tostatename           VARCHAR2(255),
  tolifecycletemplate   VARCHAR2(64),
  createbyid            VARCHAR2(255),
  createbyclassid       VARCHAR2(255),
  createtime            NUMBER(19),
  modifybyid            VARCHAR2(255),
  modifybyclassid       VARCHAR2(255),
  modifytime            NUMBER(19),
  updatetime            NUMBER(19),
  updatecount           NUMBER(10),
  id                    VARCHAR2(255),
  name                  VARCHAR2(255)
)
/

comment on table DDM_DIS_WORKLOAD is '工作量统计'
/
comment on column DDM_DIS_WORKLOAD.innerid is '内部标识'
/
comment on column DDM_DIS_WORKLOAD.classid is '类标识'
/
comment on column DDM_DIS_WORKLOAD.objectid is '对象内部标识'
/
comment on column DDM_DIS_WORKLOAD.objectclassid is '对象内部类标识'
/
comment on column DDM_DIS_WORKLOAD.fromstateid is '原生命周期状态'
/
comment on column DDM_DIS_WORKLOAD.fromstatename is '原生命周期名称'
/
comment on column DDM_DIS_WORKLOAD.fromlifecycletemplate is '原生命周期模板'
/
comment on column DDM_DIS_WORKLOAD.tostateid is '现生命周期状态'
/
comment on column DDM_DIS_WORKLOAD.tostatename is '现生命周期名称'
/
comment on column DDM_DIS_WORKLOAD.tolifecycletemplate is '现生命周期模板'
/
comment on column DDM_DIS_WORKLOAD.createbyid is '创建人标识'
/
comment on column DDM_DIS_WORKLOAD.createbyclassid is '创建人类名'
/
comment on column DDM_DIS_WORKLOAD.createtime is '创建时间'
/
comment on column DDM_DIS_WORKLOAD.modifybyid is '修改人标识'
/
comment on column DDM_DIS_WORKLOAD.modifybyclassid is '修改人对象类名'
/
comment on column DDM_DIS_WORKLOAD.modifytime is '修改时间'
/
comment on column DDM_DIS_WORKLOAD.updatetime is '系统用来支持并发的字段'
/
comment on column DDM_DIS_WORKLOAD.updatecount is '系统用来支持并发的字段'
/
comment on column DDM_DIS_WORKLOAD.id is '编号'
/
comment on column DDM_DIS_WORKLOAD.name is '名称'
/

alter table DDM_DIS_WORKLOAD add constraint PK_DDM_DIS_WORKLOAD_IID primary key (INNERID) using index tablespace plmindex
/

--追加人  ：		许慧玲
--追加时间：		2013/9/18
--追加原因：		为了记录更改单分发信息中的是否跟踪信息
--是否影响已有数据：	不影响
--是否依赖其他脚本：	不依赖，若有依赖请列出

-- 创建是否跟踪表
create table DDM_DIS_ISTRACK
(
  innerid             VARCHAR2(255) not null,
  classid             VARCHAR2(255) not null,
  istrack             NUMBER(1),
  disinfoid           VARCHAR2(255),
  infoclassid         VARCHAR2(255),
  updatetime          NUMBER(19),
  updatecount         NUMBER(10),
  createtime          NUMBER(19),
  modifytime          NUMBER(19),
  createbyid          VARCHAR2(255),
  createbyclassid     VARCHAR2(255),
  modifybyid          VARCHAR2(255),
  modifybyclassid     VARCHAR2(255)
)
/

comment on table DDM_DIS_ISTRACK is '是否跟踪'
/
comment on column DDM_DIS_ISTRACK.innerid is '内部标识'
/
comment on column DDM_DIS_ISTRACK.classid is '内部类标识'
/
comment on column DDM_DIS_ISTRACK.istrack is '是否跟踪'
/
comment on column DDM_DIS_ISTRACK.disinfoid is '分发信息ID'
/
comment on column DDM_DIS_ISTRACK.infoclassid is '分发信息的类标识'
/
comment on column DDM_DIS_ISTRACK.updatetime  is '系统用来支持并发的字段'
/
comment on column DDM_DIS_ISTRACK.updatecount is '系统用来支持并发的字段'
/
comment on column DDM_DIS_ISTRACK.createtime is '创建时间'
/
comment on column DDM_DIS_ISTRACK.modifytime is '修改时间'
/
comment on column DDM_DIS_ISTRACK.createbyid is '任务创建人标识'
/
comment on column DDM_DIS_ISTRACK.createbyclassid is '任务创建人类名'
/
comment on column DDM_DIS_ISTRACK.modifybyid is '修改人标识'
/
comment on column DDM_DIS_ISTRACK.modifybyclassid is '修改人对象类名'
/
  
alter table DDM_DIS_ISTRACK  add constraint PK_DDM_DIS_ISTRACK_IID primary key (INNERID) using index  tablespace plmindex
/
CREATE INDEX DDM_DIS_ISTRACK_IDX1 ON DDM_DIS_ISTRACK(infoClassId || ':' || disInfoId)
/
create table DDM_DIS_TASKDOMAINLINK
(
  innerid           VARCHAR2(255) not null,
  classid           VARCHAR2(255) not null,
  fromobjectid      VARCHAR2(255),
  fromobjectclassid VARCHAR2(255),
  toobjectid        VARCHAR2(255),
  toobjectclassid   VARCHAR2(255),
  updatetime        NUMBER(19),
  updatecount       NUMBER(10),
  tasktype          NUMBER
)
/

comment on table DDM_DIS_TASKDOMAINLINK is '分发信息与外域电子任务link'
/
comment on column DDM_DIS_TASKDOMAINLINK.innerid is '内部标识'
/
comment on column DDM_DIS_TASKDOMAINLINK.classid is '类名'
/
comment on column DDM_DIS_TASKDOMAINLINK.fromobjectid is '任务内部标识'
/
comment on column DDM_DIS_TASKDOMAINLINK.fromobjectclassid is '任务类名'
/
comment on column DDM_DIS_TASKDOMAINLINK.toobjectid is '分发信息内部标识'
/
comment on column DDM_DIS_TASKDOMAINLINK.toobjectclassid is '分发信息类名'
/
comment on column DDM_DIS_TASKDOMAINLINK.updatetime is '系统用来支持并发的字段'
/
comment on column DDM_DIS_TASKDOMAINLINK.updatecount is '系统用来支持并发的字段'
/
comment on column DDM_DIS_TASKDOMAINLINK.tasktype is '任务类型（0：纸质任务，1：电子任务）'
/ 

alter table DDM_DIS_TASKDOMAINLINK add constraint PK_DDM_DIS_TASKDOMAINLINK_IID primary key (INNERID) using index tablespace plmindex
/
CREATE INDEX DDM_DIS_TASKDOMAINLINK_IDX1 ON DDM_DIS_TASKDOMAINLINK(fromObjectClassId || ':' || fromObjectId)
/
CREATE INDEX DDM_DIS_TASKDOMAINLINK_IDX2 ON DDM_DIS_TASKDOMAINLINK(toObjectClassId || ':' || toObjectId)
/
create table DDM_DIS_TASKSYN
(
  innerid           VARCHAR2(255) not null,
  classid           VARCHAR2(255) not null,
  taskid            VARCHAR2(255),
  taskclassid       VARCHAR2(255),
  tasksynid         VARCHAR2(255),
  tasksynclassid    VARCHAR2(255),
  updatetime        NUMBER(19),
  updatecount       NUMBER(10),
  sitetype          NUMBER
)
/

comment on table DDM_DIS_TASKSYN is '任务同步表'
/
comment on column DDM_DIS_TASKSYN.innerid is '内部标识'
/
comment on column DDM_DIS_TASKSYN.classid is '类名'
/
comment on column DDM_DIS_TASKSYN.taskid is '任务内部标识'
/
comment on column DDM_DIS_TASKSYN.taskclassid is '任务类名'
/
comment on column DDM_DIS_TASKSYN.tasksynid is '同步任务内部标识'
/
comment on column DDM_DIS_TASKSYN.tasksynclassid is '同步任务类名'
/
comment on column DDM_DIS_TASKSYN.updatetime is '系统用来支持并发的字段'
/
comment on column DDM_DIS_TASKSYN.updatecount is '系统用来支持并发的字段'
/
comment on column DDM_DIS_TASKSYN.sitetype is '站点类型(0：中心，1：接收方）'
/ 

alter table DDM_DIS_TASKSYN add constraint PK_DDM_DIS_TASKSYN_IID primary key (INNERID) using index tablespace plmindex
/
CREATE INDEX DDM_DIS_TASKSYN_IDX1 ON DDM_DIS_TASKSYN(TASKCLASSID || ':' || TASKID)
/
CREATE INDEX DDM_DIS_TASKSYN_IDX2 ON DDM_DIS_TASKSYN(taskSynClassId || ':' || taskSynId)
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
--追加人  ：		张国强
--追加时间：		2014/08/08
--追加原因：		发放管理通用配置
--是否影响已有数据：	不影响
--是否依赖其他脚本：	不依赖，若有依赖请列出
create table DDM_DIS_COMMON_CONFIG		------发放管理通用配置
(
  innerid            VARCHAR2(255) not null,
  classid            VARCHAR2(255),
  configid           VARCHAR2(510),
  configname         VARCHAR2(510),
  configvalue        VARCHAR2(510),
  ispermitdelete     NUMBER(10),
  configdefaultvalue VARCHAR2(510),
  updatetime         NUMBER(19),
  updatecount        NUMBER(10),
  createtime         NUMBER(19),
  modifytime         NUMBER(19),
  createbyid         VARCHAR2(255),
  createbyclassid    VARCHAR2(255),
  modifybyid         VARCHAR2(255),
  modifybyclassid    VARCHAR2(255)
)
/
alter table DDM_DIS_COMMON_CONFIG add constraint PK_DIS_COMMON_CFG_IID primary key (innerid) using index tablespace plmindex
/

--添加索引
create index idx_dis_com_cfg_cfgid on DDM_DIS_COMMON_CONFIG(configid) tablespace plmindex
/

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
  distype                NUMBER,
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
comment on column DDM_RECDES_INFO.distype is '分发方式（0为直接分发，1为补发，2为移除，3为转发，4为回收，5为销毁）'
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

CREATE TABLE "DDM_DC_A3_TASK_SIGNATURE" (
"INNERID" VARCHAR2(255) NOT NULL ,
"CLASSID" VARCHAR2(255) NOT NULL ,
"DEALUSERIID" VARCHAR2(64) NULL ,
"DEALUSERID" VARCHAR2(64) NULL ,
"DEALUSERNAME" VARCHAR2(128) NULL ,
"DEALDEVISIONIID" VARCHAR2(64) NULL ,
"DEALDEVISIONID" VARCHAR2(64) NULL ,
"DEALDEVISIONNAME" VARCHAR2(128) NULL ,
"DEALROLE" VARCHAR2(64) NULL ,
"ISAGREE" NUMBER(1) NULL ,
"DEALMIND" VARCHAR2(512) NULL ,
"MINDTYPE" VARCHAR2(16) NULL ,
"ISNEEDDEVISIONMIND" NUMBER(1) NULL ,
"STARTTIME" NUMBER NULL ,
"SIGNATURETIME" NUMBER NULL ,
"SIGNATURESTATE" NUMBER NULL ,
"DOMAINIID" VARCHAR2(64) NULL ,
"ORDERIID" VARCHAR2(64) NULL ,
"TASKIID" VARCHAR2(64) NULL ,
"UPDATETIME" NUMBER(19) NULL ,
"UPDATECOUNT" NUMBER(10) NULL 
)
/

COMMENT ON TABLE "DDM_DC_A3_TASK_SIGNATURE" IS 'A3签署信息'
/
COMMENT ON COLUMN "DDM_DC_A3_TASK_SIGNATURE"."INNERID" IS '内部标识'
/
COMMENT ON COLUMN "DDM_DC_A3_TASK_SIGNATURE"."CLASSID" IS '类名'
/
COMMENT ON COLUMN "DDM_DC_A3_TASK_SIGNATURE"."DEALUSERIID" IS '处理人标识'
/
COMMENT ON COLUMN "DDM_DC_A3_TASK_SIGNATURE"."DEALUSERID" IS '处理人编号'
/
COMMENT ON COLUMN "DDM_DC_A3_TASK_SIGNATURE"."DEALUSERNAME" IS '处理人名称'
/
COMMENT ON COLUMN "DDM_DC_A3_TASK_SIGNATURE"."DEALDEVISIONIID" IS '处理人部门标识'
/
COMMENT ON COLUMN "DDM_DC_A3_TASK_SIGNATURE"."DEALDEVISIONID" IS '处理人部门编号'
/
COMMENT ON COLUMN "DDM_DC_A3_TASK_SIGNATURE"."DEALDEVISIONNAME" IS '处理人部门名称'
/
COMMENT ON COLUMN "DDM_DC_A3_TASK_SIGNATURE"."DEALROLE" IS '处理人角色'
/
COMMENT ON COLUMN "DDM_DC_A3_TASK_SIGNATURE"."ISAGREE" IS '是否同意'
/
COMMENT ON COLUMN "DDM_DC_A3_TASK_SIGNATURE"."DEALMIND" IS '处理意见'
/
COMMENT ON COLUMN "DDM_DC_A3_TASK_SIGNATURE"."MINDTYPE" IS '意见类型'
/
COMMENT ON COLUMN "DDM_DC_A3_TASK_SIGNATURE"."ISNEEDDEVISIONMIND" IS '是否需要单位意见'
/
COMMENT ON COLUMN "DDM_DC_A3_TASK_SIGNATURE"."STARTTIME" IS '任务开始时间'
/
COMMENT ON COLUMN "DDM_DC_A3_TASK_SIGNATURE"."SIGNATURETIME" IS '签署时间'
/
COMMENT ON COLUMN "DDM_DC_A3_TASK_SIGNATURE"."DOMAINIID" IS '域标识'
/
COMMENT ON COLUMN "DDM_DC_A3_TASK_SIGNATURE"."ORDERIID" IS '单据标识'
/
COMMENT ON COLUMN "DDM_DC_A3_TASK_SIGNATURE"."TASKIID" IS '任务标识'
/
COMMENT ON COLUMN "DDM_DC_A3_TASK_SIGNATURE"."UPDATETIME" IS '系统用来支持并发的字段'
/
COMMENT ON COLUMN "DDM_DC_A3_TASK_SIGNATURE"."UPDATECOUNT" IS '系统用来支持并发的字段'
/

CREATE INDEX "INDEX_SIGNATURE_ORDERIID"
ON "DDM_DC_A3_TASK_SIGNATURE" ("ORDERIID" ASC)
/
CREATE INDEX "INDEX_SIGNATURE_TASKIID"
ON "DDM_DC_A3_TASK_SIGNATURE" ("TASKIID" ASC)
/
ALTER TABLE "DDM_DC_A3_TASK_SIGNATURE" ADD PRIMARY KEY ("INNERID")
/


CREATE TABLE "DDM_DC_A3_SIGOBJLINK" (
"INNERID" VARCHAR2(255) NOT NULL ,
"CLASSID" VARCHAR2(255) NOT NULL ,
"FROMOBJECTID" VARCHAR2(255) NULL ,
"FROMOBJECTCLASSID" VARCHAR2(255) NULL ,
"TOOBJECTID" VARCHAR2(255) NULL ,
"TOOBJECTCLASSID" VARCHAR2(255) NULL ,
"UPDATETIME" NUMBER(19) NULL ,
"UPDATECOUNT" NUMBER(10) NULL 
)
/
COMMENT ON TABLE "DDM_DC_A3_SIGOBJLINK" IS '发放单与分发数据link表'
/
COMMENT ON COLUMN "DDM_DC_A3_SIGOBJLINK"."INNERID" IS '内部标识'
/
COMMENT ON COLUMN "DDM_DC_A3_SIGOBJLINK"."CLASSID" IS '类名'
/
COMMENT ON COLUMN "DDM_DC_A3_SIGOBJLINK"."FROMOBJECTID" IS '发放单内部标识'
/
COMMENT ON COLUMN "DDM_DC_A3_SIGOBJLINK"."FROMOBJECTCLASSID" IS '发放单类名'
/
COMMENT ON COLUMN "DDM_DC_A3_SIGOBJLINK"."TOOBJECTID" IS '分发数据源内部标识'
/
COMMENT ON COLUMN "DDM_DC_A3_SIGOBJLINK"."TOOBJECTCLASSID" IS '分发数据源类名'
/
COMMENT ON COLUMN "DDM_DC_A3_SIGOBJLINK"."UPDATETIME" IS '系统用来支持并发的字段'
/
COMMENT ON COLUMN "DDM_DC_A3_SIGOBJLINK"."UPDATECOUNT" IS '系统用来支持并发的字段'
/


CREATE INDEX "DDM_DC_A3_SIGOBJLINK_IDX1"
ON "DDM_DC_A3_SIGOBJLINK" ("INNERID" ASC)
/
CREATE INDEX "DDM_DC_A3_SIGOBJLINK_IDX2"
ON "DDM_DC_A3_SIGOBJLINK" ("FROMOBJECTID" ASC)
/
CREATE INDEX "DDM_DC_A3_SIGOBJLINK_IDX3"
ON "DDM_DC_A3_SIGOBJLINK" ("TOOBJECTID" ASC)
/
ALTER TABLE "DDM_DC_A3_SIGOBJLINK" ADD PRIMARY KEY ("INNERID")
/

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
