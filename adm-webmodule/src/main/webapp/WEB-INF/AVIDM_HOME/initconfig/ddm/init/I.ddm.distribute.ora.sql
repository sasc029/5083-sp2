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

comment on table DDM_DIS_DUPLICATE is '���Ƽӹ�'
/
comment on column DDM_DIS_DUPLICATE.innerid is '�ڲ���ʶ'
/
comment on column DDM_DIS_DUPLICATE.classid is '�ڲ����ʶ'
/
comment on column DDM_DIS_DUPLICATE.disobjectinnerid is '�ַ������ڲ���ʶ'
/
comment on column DDM_DIS_DUPLICATE.disobjectclassid is '�ַ������ڲ����ʶ'
/
comment on column DDM_DIS_DUPLICATE.dispapertaskinnerid is '����ֽ�ʷַ������ڲ���ʶ'
/
comment on column DDM_DIS_DUPLICATE.dispapertaskclassid is '����ֽ�ʷַ������ڲ����ʶ'
/
comment on column DDM_DIS_DUPLICATE.contractor is '��ӡ��'
/
comment on column DDM_DIS_DUPLICATE.collator is '������'
/
comment on column DDM_DIS_DUPLICATE.finishtime is '���ʱ��'
/
comment on column DDM_DIS_DUPLICATE.updatetime is 'ϵͳ����֧�ֲ������ֶ�'
/
comment on column DDM_DIS_DUPLICATE.updatecount is 'ϵͳ����֧�ֲ������ֶ�'
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

comment on table DDM_DIS_ELECTASK is '��������'
/
comment on column DDM_DIS_ELECTASK.innerid is '�ڲ���ʶ'
/
comment on column DDM_DIS_ELECTASK.classid is '���ʶ'
/
comment on column DDM_DIS_ELECTASK.id is '������'
/
comment on column DDM_DIS_ELECTASK.name is '��������'
/
comment on column DDM_DIS_ELECTASK.note is '��ע'
/
comment on column DDM_DIS_ELECTASK.stateid is '��������״̬'
/
comment on column DDM_DIS_ELECTASK.statename is '������������'
/
comment on column DDM_DIS_ELECTASK.lifecycletemplate is '��������ģ��'
/
comment on column DDM_DIS_ELECTASK.contextid is '�������ڲ���ʶ'
/
comment on column DDM_DIS_ELECTASK.contextclassid is '����������'
/
comment on column DDM_DIS_ELECTASK.createbyid is '���񴴽��˱�ʶ'
/
comment on column DDM_DIS_ELECTASK.createbyclassid is '���񴴽�������'
/
comment on column DDM_DIS_ELECTASK.createtime is '���񴴽�ʱ��'
/
comment on column DDM_DIS_ELECTASK.modifybyid is '�޸��˱�ʶ'
/
comment on column DDM_DIS_ELECTASK.modifybyclassid is '�޸��˶�������'
/
comment on column DDM_DIS_ELECTASK.modifytime is '�޸�ʱ��'
/
comment on column DDM_DIS_ELECTASK.updatetime is 'ϵͳ����֧�ֲ������ֶ�'
/
comment on column DDM_DIS_ELECTASK.updatecount is 'ϵͳ����֧�ֲ������ֶ�'
/
comment on column DDM_DIS_ELECTASK.receivebyid is '�����˱�ʶ'
/
comment on column DDM_DIS_ELECTASK.receivebyclassid is '���������ʶ'
/
comment on column DDM_DIS_ELECTASK.receivetime is '����ʱ��'
/
comment on column DDM_DIS_ELECTASK.domainid is '���ʶ'
/
comment on column DDM_DIS_ELECTASK.domainclassid is '���������'
/
comment on column DDM_DIS_ELECTASK.electasktype is '�������ͣ�0Ϊ���ڣ�1Ϊ���⣩'
/
comment on column DDM_DIS_ELECTASK.sourcesiteid is 'Դվ���ڲ���ʾ'
/
comment on column DDM_DIS_ELECTASK.sourcesiteclassid is 'Դվ�����ʶ'
/
comment on column DDM_DIS_ELECTASK.sourcesitename is 'Դվ������'
/
comment on column DDM_DIS_ELECTASK.targetsiteid is 'Ŀ��վ���ڲ���ʶ'
/
comment on column DDM_DIS_ELECTASK.targetsiteclassid is 'Ŀ��վ�����ʶ'
/
comment on column DDM_DIS_ELECTASK.targetsitename is 'Ŀ��վ������'
/
comment on column DDM_DIS_ELECTASK.centersiteid is '����վ���ڲ���ʶ'
/
comment on column DDM_DIS_ELECTASK.centersiteclassid is '����վ�����ʶ'
/
comment on column DDM_DIS_ELECTASK.receivebyname is '����������'
/
comment on column DDM_DIS_ELECTASK.infoClassId is '�ַ���λ������Աclassid'
/
comment on column DDM_DIS_ELECTASK.disInfoId is '�ַ���λ������Աid'
/
comment on column DDM_DIS_ELECTASK.disInfoName is '�ַ���λ������Ա����'
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

comment on table DDM_DIS_INFO is '�ַ���Ϣ'
/
comment on column DDM_DIS_INFO.innerid is '�ַ���Ϣ�ڲ���ʶ'
/
comment on column DDM_DIS_INFO.classid is '�ַ���Ϣ���ʶ'
/
comment on column DDM_DIS_INFO.disorderobjlinkid is '���ŵ���ַ�����LINK�ڲ���ʶ'
/
comment on column DDM_DIS_INFO.disorderobjlinkclassid is '���ŵ���ַ�����LINK���ʶ'
/
comment on column DDM_DIS_INFO.updatetime is 'ϵͳ����֧�ֲ������ֶ�'
/
comment on column DDM_DIS_INFO.updatecount is 'ϵͳ����֧�ֲ������ֶ�'
/
comment on column DDM_DIS_INFO.stateid is '�ַ���Ϣ�������ڱ�ʶ'
/
comment on column DDM_DIS_INFO.statename is '�ַ���Ϣ������������'
/
comment on column DDM_DIS_INFO.lifecycletemplate is '�ַ���Ϣ��������ģ��'
/
comment on column DDM_DIS_INFO.sendtime is '�ַ���Ϣ��Ϊ�ѷַ�״̬��ʱ��'
/
comment on column DDM_DIS_INFO.createbyid is '�ַ���Ϣ�����˱�ʶ'
/
comment on column DDM_DIS_INFO.createbyclassid is '�ַ���Ϣ�����˶�������'
/
comment on column DDM_DIS_INFO.createtime is '�ַ���Ϣ����ʱ��'
/
comment on column DDM_DIS_INFO.modifybyid is '�ַ���Ϣ�޸��˱�ʶ'
/
comment on column DDM_DIS_INFO.modifybyclassid is '�ַ���Ϣ�޸��˶�������'
/
comment on column DDM_DIS_INFO.modifytime  is '�ַ���Ϣ�޸�ʱ��'
/
comment on column DDM_DIS_INFO.disinfoname is '�ַ���Ϣ���ƣ���λ/��Ա��'
/
comment on column DDM_DIS_INFO.disinfoid is '�ַ���ϢIID����Ա����֯���ڲ���ʶ��'
/
comment on column DDM_DIS_INFO.infoclassid is '�ַ���Ϣ�����ʶ����Ա������֯�����ʶ��'
/
comment on column DDM_DIS_INFO.disinfotype is '�ַ���Ϣ���ͣ�0Ϊ��λ��1Ϊ��Ա��'
/
comment on column DDM_DIS_INFO.disinfonum is '�ַ�����'
/
comment on column DDM_DIS_INFO.dismediatype is '�ַ��������ͣ�0Ϊֽ�ʣ�1Ϊ���ӣ�2Ϊ����'
/
comment on column DDM_DIS_INFO.distype is '�ַ���ʽ��0Ϊֱ�ӷַ���1Ϊ������2Ϊ�Ƴ���3Ϊת����'
/
comment on column DDM_DIS_INFO.note is '��ע'
/
comment on column DDM_DIS_INFO.destroynum is '���ٷ���'
/
comment on column DDM_DIS_INFO.recovernum  is '���շ���'
/
comment on column DDM_DIS_INFO.outsignid  is '���������IID����Ա�ڲ���ʶ��'
/
comment on column DDM_DIS_INFO.outsignclassid  is '��������˵����ʶ����Ա���ʶ��'
/
comment on column DDM_DIS_INFO.outsignname  is '������������ƣ���Ա��'
/
comment on column DDM_DIS_INFO.sendtype  is '��֯���ͣ�0Ϊ�ڲ�,1Ϊ�ⲿ��'
/
comment on column DDM_DIS_INFO.sealinfo  is '������Ϣ'
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
comment on column DDM_DIS_OBJECT.linkurl is '�ַ���������url'
/
comment on column DDM_DIS_OBJECT.accessurl is '���շ����ʷַ����ݵ�����'
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

comment on table DDM_DIS_ORDER is '���ŵ������'
/
comment on column DDM_DIS_ORDER.innerid is '�ڲ���ʶ'
/
comment on column DDM_DIS_ORDER.classid is '����'
/
comment on column DDM_DIS_ORDER.id is '���'
/
comment on column DDM_DIS_ORDER.name is '����'
/
comment on column DDM_DIS_ORDER.createbyid is '�����˱�ʶ'
/
comment on column DDM_DIS_ORDER.createbyclassid  is '�����˶�������'
/
comment on column DDM_DIS_ORDER.createtime is '����ʱ��'
/
comment on column DDM_DIS_ORDER.modifybyid is '�޸��˱�ʶ'
/
comment on column DDM_DIS_ORDER.modifybyclassid is '�޸��˶�������'
/
comment on column DDM_DIS_ORDER.modifytime is '�޸�ʱ��'
/
comment on column DDM_DIS_ORDER.stateid is '�������ڱ�ʶ'
/
comment on column DDM_DIS_ORDER.statename is '������������'
/
comment on column DDM_DIS_ORDER.lifecycletemplate is '��������ģ��'
/
comment on column DDM_DIS_ORDER.contextid is '�����ı�ʶ'
/
comment on column DDM_DIS_ORDER.contextclassid is '����������'
/
comment on column DDM_DIS_ORDER.ordertype is '��������(0���ŵ���1�������ŵ�)'
/
comment on column DDM_DIS_ORDER.updatetime is 'ϵͳ����֧�ֲ������ֶ�'
/
comment on column DDM_DIS_ORDER.updatecount is 'ϵͳ����֧�ֲ������ֶ�'
/
comment on column DDM_DIS_ORDER.note is '��ע'
/
comment on column DDM_DIS_ORDER.domainid is '���ʶ'
/
comment on column DDM_DIS_ORDER.domainclassid is '���������'
/
comment on column DDM_DIS_ORDER.submituserid is '�������ڲ���ʶ'
/
comment on column DDM_DIS_ORDER.submituserclassid is '���������ʶ'
/
comment on column DDM_DIS_ORDER.submitusername is '����������'
/
comment on column DDM_DIS_ORDER.siteid is 'վ���ڲ���ʶ'
/
comment on column DDM_DIS_ORDER.siteclassid is 'վ�����ʶ'
/
comment on column DDM_DIS_ORDER.sitename is 'վ������'
/
comment on column DDM_DIS_ORDER.MASTERDATACLASSID is '�ַ���������������'
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

comment on table DDM_DIS_ORDEROBJLINK is '���ŵ���ַ�����link��'
/
comment on column DDM_DIS_ORDEROBJLINK.innerid is '�ڲ���ʶ'
/
comment on column DDM_DIS_ORDEROBJLINK.classid is '����'
/
comment on column DDM_DIS_ORDEROBJLINK.fromobjectid is '���ŵ��ڲ���ʶ'
/
comment on column DDM_DIS_ORDEROBJLINK.fromobjectclassid is '���ŵ�����'
/
comment on column DDM_DIS_ORDEROBJLINK.toobjectid is '�ַ�����Դ�ڲ���ʶ'
/
comment on column DDM_DIS_ORDEROBJLINK.toobjectclassid is '�ַ�����Դ����'
/
comment on column DDM_DIS_ORDEROBJLINK.updatetime is 'ϵͳ����֧�ֲ������ֶ�'
/
comment on column DDM_DIS_ORDEROBJLINK.updatecount is 'ϵͳ����֧�ֲ������ֶ�'
/
comment on column DDM_DIS_ORDEROBJLINK.isparent is '�Ƿ���������ŵ����Ǹ��ڵ㣨0�����ǣ�1���ǣ�'
/
comment on column DDM_DIS_ORDEROBJLINK.stateid is '�������ڱ�ʶ'
/
comment on column DDM_DIS_ORDEROBJLINK.statename is '������������'
/
comment on column DDM_DIS_ORDEROBJLINK.lifecycletemplate is '��������ģ��'
/
comment on column DDM_DIS_ORDEROBJLINK.ismaster is '�Ƿ���������'
/
comment on column DDM_DIS_ORDEROBJLINK.disdeadline is '�ַ���Ϣ�깤����'
/
comment on column DDM_DIS_ORDEROBJLINK.disurgent is '0Ϊ��ͨ��1Ϊ�Ӽ�'
/
comment on column DDM_DIS_ORDEROBJLINK.disstyle is '0Ϊ��ʽ�ַ���1һ���Էַ�'
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

comment on table DDM_DIS_PAPERTASK is 'ֽ������'
/
comment on column DDM_DIS_PAPERTASK.innerid is '�ڲ���ʶ'
/
comment on column DDM_DIS_PAPERTASK.classid is '���ʶ'
/
comment on column DDM_DIS_PAPERTASK.id is '������'
/
comment on column DDM_DIS_PAPERTASK.name is '��������'
/
comment on column DDM_DIS_PAPERTASK.createbyid is '���񴴽��˱�ʶ'
/
comment on column DDM_DIS_PAPERTASK.createbyclassid is '���񴴽�������'
/
comment on column DDM_DIS_PAPERTASK.createtime is '���񴴽�ʱ��'
/
comment on column DDM_DIS_PAPERTASK.stateid is '��������״̬'
/
comment on column DDM_DIS_PAPERTASK.statename is '������������'
/
comment on column DDM_DIS_PAPERTASK.lifecycletemplate is '��������ģ��'
/
comment on column DDM_DIS_PAPERTASK.contextid is '�������ڲ���ʶ'
/
comment on column DDM_DIS_PAPERTASK.contextclassid  is '����������'
/
comment on column DDM_DIS_PAPERTASK.updatetime  is 'ϵͳ����֧�ֲ������ֶ�'
/
comment on column DDM_DIS_PAPERTASK.updatecount is 'ϵͳ����֧�ֲ������ֶ�'
/
comment on column DDM_DIS_PAPERTASK.note is '��ע'
/
comment on column DDM_DIS_PAPERTASK.modifybyid is '�޸��˱�ʶ'
/
comment on column DDM_DIS_PAPERTASK.modifybyclassid is '�޸��˶�������'
/
comment on column DDM_DIS_PAPERTASK.modifytime is '�޸�ʱ��'
/
comment on column DDM_DIS_PAPERTASK.domainid is '���ʶ'
/
comment on column DDM_DIS_PAPERTASK.domainclassid is '���������'
/
comment on column DDM_DIS_PAPERTASK.disurgent is '0Ϊ��ͨ��1Ϊ�Ӽ�'
/
comment on column DDM_DIS_PAPERTASK.disordercreator is '���ŵ�������'
/
comment on column DDM_DIS_PAPERTASK.ISPRINT is '�ַ������Ƿ��Ѵ�ӡ��1Ϊ�ǣ�0Ϊ��'
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

comment on table DDM_DIS_RETURN is '��������'
/
comment on column DDM_DIS_RETURN.innerid is '�ڲ���ʶ'
/
comment on column DDM_DIS_RETURN.classid is '���ʶ'
/
comment on column DDM_DIS_RETURN.objectid is '�����ڲ���ʶ'
/
comment on column DDM_DIS_RETURN.objectclassid is '�������ʶ'
/
comment on column DDM_DIS_RETURN.stateid is '��������״̬'
/
comment on column DDM_DIS_RETURN.statename is '������������'
/
comment on column DDM_DIS_RETURN.lifecycletemplate is '��������ģ��'
/
comment on column DDM_DIS_RETURN.returncount is '���˴���'
/
comment on column DDM_DIS_RETURN.returnreason is '����ԭ��'
/
comment on column DDM_DIS_RETURN.updatetime is 'ϵͳ����֧�ֲ������ֶ�'
/
comment on column DDM_DIS_RETURN.updatecount is 'ϵͳ����֧�ֲ������ֶ�'
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

comment on table DDM_DIS_SERIANO is '��ˮ��'
/
comment on column DDM_DIS_SERIANO.seriano is '��ˮ��'
/
comment on column DDM_DIS_SERIANO.createserianotime is '����ʱ��'
/
comment on column DDM_DIS_SERIANO.ordertype is '��������(0���ŵ���1�������ŵ�)'
/
comment on column DDM_DIS_SERIANO.updatetime is 'ϵͳ����֧�ֲ������ֶ�'
/
comment on column DDM_DIS_SERIANO.updatecount is 'ϵͳ����֧�ֲ������ֶ�'
/
comment on column DDM_DIS_SERIANO.innerid is '�ڲ���ʶ'
/
comment on column DDM_DIS_SERIANO.classid  is '����'
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

comment on table DDM_DIS_TASKINFOLINK is '�ַ���Ϣ��ֽ������link'
/
comment on column DDM_DIS_TASKINFOLINK.innerid is '�ڲ���ʶ'
/
comment on column DDM_DIS_TASKINFOLINK.classid is '����'
/
comment on column DDM_DIS_TASKINFOLINK.fromobjectid is '�����ڲ���ʶ'
/
comment on column DDM_DIS_TASKINFOLINK.fromobjectclassid is '��������'
/
comment on column DDM_DIS_TASKINFOLINK.toobjectid is '�ַ���Ϣ�ڲ���ʶ'
/
comment on column DDM_DIS_TASKINFOLINK.toobjectclassid is '�ַ���Ϣ����'
/
comment on column DDM_DIS_TASKINFOLINK.updatetime is 'ϵͳ����֧�ֲ������ֶ�'
/
comment on column DDM_DIS_TASKINFOLINK.updatecount is 'ϵͳ����֧�ֲ������ֶ�'
/
comment on column DDM_DIS_TASKINFOLINK.tasktype is '�������ͣ�0��ֽ������1����������'
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

comment on table DDM_DIS_WORKLOAD is '������ͳ��'
/
comment on column DDM_DIS_WORKLOAD.innerid is '�ڲ���ʶ'
/
comment on column DDM_DIS_WORKLOAD.classid is '���ʶ'
/
comment on column DDM_DIS_WORKLOAD.objectid is '�����ڲ���ʶ'
/
comment on column DDM_DIS_WORKLOAD.objectclassid is '�����ڲ����ʶ'
/
comment on column DDM_DIS_WORKLOAD.fromstateid is 'ԭ��������״̬'
/
comment on column DDM_DIS_WORKLOAD.fromstatename is 'ԭ������������'
/
comment on column DDM_DIS_WORKLOAD.fromlifecycletemplate is 'ԭ��������ģ��'
/
comment on column DDM_DIS_WORKLOAD.tostateid is '����������״̬'
/
comment on column DDM_DIS_WORKLOAD.tostatename is '��������������'
/
comment on column DDM_DIS_WORKLOAD.tolifecycletemplate is '����������ģ��'
/
comment on column DDM_DIS_WORKLOAD.createbyid is '�����˱�ʶ'
/
comment on column DDM_DIS_WORKLOAD.createbyclassid is '����������'
/
comment on column DDM_DIS_WORKLOAD.createtime is '����ʱ��'
/
comment on column DDM_DIS_WORKLOAD.modifybyid is '�޸��˱�ʶ'
/
comment on column DDM_DIS_WORKLOAD.modifybyclassid is '�޸��˶�������'
/
comment on column DDM_DIS_WORKLOAD.modifytime is '�޸�ʱ��'
/
comment on column DDM_DIS_WORKLOAD.updatetime is 'ϵͳ����֧�ֲ������ֶ�'
/
comment on column DDM_DIS_WORKLOAD.updatecount is 'ϵͳ����֧�ֲ������ֶ�'
/
comment on column DDM_DIS_WORKLOAD.id is '���'
/
comment on column DDM_DIS_WORKLOAD.name is '����'
/

alter table DDM_DIS_WORKLOAD add constraint PK_DDM_DIS_WORKLOAD_IID primary key (INNERID) using index tablespace plmindex
/

--׷����  ��		�����
--׷��ʱ�䣺		2013/9/18
--׷��ԭ��		Ϊ�˼�¼���ĵ��ַ���Ϣ�е��Ƿ������Ϣ
--�Ƿ�Ӱ���������ݣ�	��Ӱ��
--�Ƿ����������ű���	�������������������г�

-- �����Ƿ���ٱ�
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

comment on table DDM_DIS_ISTRACK is '�Ƿ����'
/
comment on column DDM_DIS_ISTRACK.innerid is '�ڲ���ʶ'
/
comment on column DDM_DIS_ISTRACK.classid is '�ڲ����ʶ'
/
comment on column DDM_DIS_ISTRACK.istrack is '�Ƿ����'
/
comment on column DDM_DIS_ISTRACK.disinfoid is '�ַ���ϢID'
/
comment on column DDM_DIS_ISTRACK.infoclassid is '�ַ���Ϣ�����ʶ'
/
comment on column DDM_DIS_ISTRACK.updatetime  is 'ϵͳ����֧�ֲ������ֶ�'
/
comment on column DDM_DIS_ISTRACK.updatecount is 'ϵͳ����֧�ֲ������ֶ�'
/
comment on column DDM_DIS_ISTRACK.createtime is '����ʱ��'
/
comment on column DDM_DIS_ISTRACK.modifytime is '�޸�ʱ��'
/
comment on column DDM_DIS_ISTRACK.createbyid is '���񴴽��˱�ʶ'
/
comment on column DDM_DIS_ISTRACK.createbyclassid is '���񴴽�������'
/
comment on column DDM_DIS_ISTRACK.modifybyid is '�޸��˱�ʶ'
/
comment on column DDM_DIS_ISTRACK.modifybyclassid is '�޸��˶�������'
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

comment on table DDM_DIS_TASKDOMAINLINK is '�ַ���Ϣ�������������link'
/
comment on column DDM_DIS_TASKDOMAINLINK.innerid is '�ڲ���ʶ'
/
comment on column DDM_DIS_TASKDOMAINLINK.classid is '����'
/
comment on column DDM_DIS_TASKDOMAINLINK.fromobjectid is '�����ڲ���ʶ'
/
comment on column DDM_DIS_TASKDOMAINLINK.fromobjectclassid is '��������'
/
comment on column DDM_DIS_TASKDOMAINLINK.toobjectid is '�ַ���Ϣ�ڲ���ʶ'
/
comment on column DDM_DIS_TASKDOMAINLINK.toobjectclassid is '�ַ���Ϣ����'
/
comment on column DDM_DIS_TASKDOMAINLINK.updatetime is 'ϵͳ����֧�ֲ������ֶ�'
/
comment on column DDM_DIS_TASKDOMAINLINK.updatecount is 'ϵͳ����֧�ֲ������ֶ�'
/
comment on column DDM_DIS_TASKDOMAINLINK.tasktype is '�������ͣ�0��ֽ������1����������'
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

comment on table DDM_DIS_TASKSYN is '����ͬ����'
/
comment on column DDM_DIS_TASKSYN.innerid is '�ڲ���ʶ'
/
comment on column DDM_DIS_TASKSYN.classid is '����'
/
comment on column DDM_DIS_TASKSYN.taskid is '�����ڲ���ʶ'
/
comment on column DDM_DIS_TASKSYN.taskclassid is '��������'
/
comment on column DDM_DIS_TASKSYN.tasksynid is 'ͬ�������ڲ���ʶ'
/
comment on column DDM_DIS_TASKSYN.tasksynclassid is 'ͬ����������'
/
comment on column DDM_DIS_TASKSYN.updatetime is 'ϵͳ����֧�ֲ������ֶ�'
/
comment on column DDM_DIS_TASKSYN.updatecount is 'ϵͳ����֧�ֲ������ֶ�'
/
comment on column DDM_DIS_TASKSYN.sitetype is 'վ������(0�����ģ�1�����շ���'
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
--׷����  ��		�Ź�ǿ
--׷��ʱ�䣺		2014/08/08
--׷��ԭ��		���Ź���ͨ������
--�Ƿ�Ӱ���������ݣ�	��Ӱ��
--�Ƿ����������ű���	�������������������г�
create table DDM_DIS_COMMON_CONFIG		------���Ź���ͨ������
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

--�������
create index idx_dis_com_cfg_cfgid on DDM_DIS_COMMON_CONFIG(configid) tablespace plmindex
/

--׷��ԭ��		�������������ò��Զ�����
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
comment on table DDM_DIS_OBJECT_NOTAUTOCREATE is '����ģ�Ͳ��Զ��������ñ�'
/
comment on column DDM_DIS_OBJECT_NOTAUTOCREATE.innerid is '�ڲ���ʶ'
/
comment on column DDM_DIS_OBJECT_NOTAUTOCREATE.classid is '�ڲ����ʶ'
/
alter table DDM_DIS_OBJECT_NOTAUTOCREATE add constraint PK_DDM_DISOBJ_NAC_IID primary key (INNERID) using index tablespace plmindex
/

--׷����  ��		kangyanfei
--׷��ʱ�䣺		2014/08/13
--׷�ӱ�����		�������ٷ�����Ϣ
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
comment on table DDM_RECDES_INFO is '�������ٷַ���Ϣ'
/
comment on column DDM_RECDES_INFO.innerid is '�������ٷַ���Ϣ�����ڲ���ʶ'
/
comment on column DDM_RECDES_INFO.classid is '�������ٷַ���Ϣ�������ʶ'
/
comment on column DDM_RECDES_INFO.disOrderObjectLinkId is '���ŵ���ַ�����LINK�ڲ���ʶ'
/
comment on column DDM_RECDES_INFO.disOrderObjectLinkClassId is '���ŵ���ַ�����LINK���ʶ'
/
comment on column DDM_RECDES_INFO.updatetime is 'ϵͳ����֧�ֲ������ֶ�'
/
comment on column DDM_RECDES_INFO.updatecount is 'ϵͳ����֧�ֲ������ֶ�'
/
comment on column DDM_RECDES_INFO.stateid is '�������ٷַ���Ϣ�������ڱ�ʶ'
/
comment on column DDM_RECDES_INFO.statename is '�������ٷַ���Ϣ������������'
/
comment on column DDM_RECDES_INFO.lifecycletemplate is '�������ٷַ���Ϣ��������ģ��'
/
comment on column DDM_RECDES_INFO.sendtime is '�������ٷַ���Ϣ��Ϊ�ѷַ�״̬��ʱ��'
/
comment on column DDM_RECDES_INFO.createbyid is '�������ٷַ���Ϣ�����˱�ʶ'
/
comment on column DDM_RECDES_INFO.createbyclassid is '�������ٷַ���Ϣ�����˶�������'
/
comment on column DDM_RECDES_INFO.createtime is '�������ٷַ���Ϣ����ʱ��'
/
comment on column DDM_RECDES_INFO.modifybyid is '�������ٷַ���Ϣ�޸��˱�ʶ'
/
comment on column DDM_RECDES_INFO.modifybyclassid is '�������ٷַ���Ϣ�޸��˶�������'
/
comment on column DDM_RECDES_INFO.modifytime  is '�������ٷַ���Ϣ�޸�ʱ��'
/
comment on column DDM_RECDES_INFO.needRecoverNum is '��Ҫ���շ���'
/
comment on column DDM_RECDES_INFO.needDestroyNum is '��Ҫ���ٷ���'
/
comment on column DDM_RECDES_INFO.disinfonum is '�ַ�����'
/
comment on column DDM_RECDES_INFO.destroynum is '���ٷ���'
/
comment on column DDM_RECDES_INFO.recovernum  is '���շ���'
/
comment on column DDM_RECDES_INFO.disinfoname is '�������ٷַ���Ϣ���ƣ���λ/��Ա��'
/
comment on column DDM_RECDES_INFO.disinfoid is '�������ٷַ���ϢIID����Ա����֯���ڲ���ʶ��'
/
comment on column DDM_RECDES_INFO.infoclassid is '�������ٷַ���Ϣ�����ʶ����Ա������֯�����ʶ��'
/
comment on column DDM_RECDES_INFO.disinfotype is '�������ٷַ���Ϣ���ͣ�0Ϊ��λ��1Ϊ��Ա��'
/
comment on column DDM_RECDES_INFO.dismediatype is '�ַ��������ͣ�0Ϊֽ�ʣ�1Ϊ���ӣ�2Ϊ����'
/
comment on column DDM_RECDES_INFO.distype is '�ַ���ʽ��0Ϊֱ�ӷַ���1Ϊ������2Ϊ�Ƴ���3Ϊת����4Ϊ���գ�5Ϊ���٣�'
/
comment on column DDM_RECDES_INFO.note is '��ע'
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
COMMENT ON TABLE DDM_RECDES_ELECTASK IS '�������ٵ�������'
/
COMMENT ON COLUMN DDM_RECDES_ELECTASK.innerid IS '�ڲ���ʶ'
/
COMMENT ON COLUMN DDM_RECDES_ELECTASK.classid IS '���ʶ'
/
COMMENT ON COLUMN DDM_RECDES_ELECTASK.id IS '������'
/
COMMENT ON COLUMN DDM_RECDES_ELECTASK.name IS '��������'
/
COMMENT ON COLUMN DDM_RECDES_ELECTASK.note IS '��ע'
/
COMMENT ON COLUMN DDM_RECDES_ELECTASK.stateid IS '��������״̬'
/
COMMENT ON COLUMN DDM_RECDES_ELECTASK.statename IS '������������'
/
COMMENT ON COLUMN DDM_RECDES_ELECTASK.lifecycletemplate IS '��������ģ��'
/
COMMENT ON COLUMN DDM_RECDES_ELECTASK.contextid IS '�������ڲ���ʶ'
/
COMMENT ON COLUMN DDM_RECDES_ELECTASK.contextclassid IS '����������'
/
COMMENT ON COLUMN DDM_RECDES_ELECTASK.createbyid IS '���񴴽��˱�ʶ'
/
COMMENT ON COLUMN DDM_RECDES_ELECTASK.createbyclassid IS '���񴴽�������'
/
COMMENT ON COLUMN DDM_RECDES_ELECTASK.createtime IS '���񴴽�ʱ��'
/
COMMENT ON COLUMN DDM_RECDES_ELECTASK.modifybyid IS '�޸��˱�ʶ'
/
COMMENT ON COLUMN DDM_RECDES_ELECTASK.modifybyclassid IS '�޸��˶�������'
/
COMMENT ON COLUMN DDM_RECDES_ELECTASK.modifytime IS '�޸�ʱ��'
/
COMMENT ON COLUMN DDM_RECDES_ELECTASK.updatetime IS 'ϵͳ����֧�ֲ������ֶ�'
/
COMMENT ON COLUMN DDM_RECDES_ELECTASK.updatecount IS 'ϵͳ����֧�ֲ������ֶ�'
/
COMMENT ON COLUMN DDM_RECDES_ELECTASK.receivebyid IS '�����˱�ʶ'
/
COMMENT ON COLUMN DDM_RECDES_ELECTASK.receivebyclassid IS '���������ʶ'
/
COMMENT ON COLUMN DDM_RECDES_ELECTASK.receivetime IS '����ʱ��'
/
COMMENT ON COLUMN DDM_RECDES_ELECTASK.domainid IS '���ʶ'
/
COMMENT ON COLUMN DDM_RECDES_ELECTASK.domainclassid IS '���������'
/
COMMENT ON COLUMN DDM_RECDES_ELECTASK.electasktype IS '�������ͣ�0Ϊ���ڣ�1Ϊ���⣩'
/
COMMENT ON COLUMN DDM_RECDES_ELECTASK.sourcesiteid IS 'Դվ���ڲ���ʾ'
/
COMMENT ON COLUMN DDM_RECDES_ELECTASK.sourcesiteclassid IS 'Դվ�����ʶ'
/
COMMENT ON COLUMN DDM_RECDES_ELECTASK.sourcesitename IS 'Դվ������'
/
COMMENT ON COLUMN DDM_RECDES_ELECTASK.targetsiteid IS 'Ŀ��վ���ڲ���ʶ'
/
COMMENT ON COLUMN DDM_RECDES_ELECTASK.targetsiteclassid IS 'Ŀ��վ�����ʶ'
/
COMMENT ON COLUMN DDM_RECDES_ELECTASK.targetsitename IS 'Ŀ��վ������'
/
COMMENT ON COLUMN DDM_RECDES_ELECTASK.centersiteid IS '����վ���ڲ���ʶ'
/
COMMENT ON COLUMN DDM_RECDES_ELECTASK.centersiteclassid IS '����վ�����ʶ'
/
COMMENT ON COLUMN DDM_RECDES_ELECTASK.receivebyname IS '����������'
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
COMMENT ON TABLE DDM_RECDES_PAPERTASK IS '��������ֽ������'
/
COMMENT ON COLUMN DDM_RECDES_PAPERTASK.innerid IS '�ڲ���ʶ'
/
COMMENT ON COLUMN DDM_RECDES_PAPERTASK.classid IS '���ʶ'
/
COMMENT ON COLUMN DDM_RECDES_PAPERTASK.id IS '������'
/
COMMENT ON COLUMN DDM_RECDES_PAPERTASK.name IS '��������'
/
COMMENT ON COLUMN DDM_RECDES_PAPERTASK.createbyid IS '���񴴽��˱�ʶ'
/
COMMENT ON COLUMN DDM_RECDES_PAPERTASK.createbyclassid IS '���񴴽�������'
/
COMMENT ON COLUMN DDM_RECDES_PAPERTASK.createtime IS '���񴴽�ʱ��'
/
COMMENT ON COLUMN DDM_RECDES_PAPERTASK.stateid IS '��������״̬'
/
COMMENT ON COLUMN DDM_RECDES_PAPERTASK.statename IS '������������'
/
COMMENT ON COLUMN DDM_RECDES_PAPERTASK.lifecycletemplate IS '��������ģ��'
/
COMMENT ON COLUMN DDM_RECDES_PAPERTASK.contextid IS '�������ڲ���ʶ'
/
COMMENT ON COLUMN DDM_RECDES_PAPERTASK.contextclassid IS '����������'
/
COMMENT ON COLUMN DDM_RECDES_PAPERTASK.updatetime IS 'ϵͳ����֧�ֲ������ֶ�'
/
COMMENT ON COLUMN DDM_RECDES_PAPERTASK.updatecount IS 'ϵͳ����֧�ֲ������ֶ�'
/
COMMENT ON COLUMN DDM_RECDES_PAPERTASK.note IS '��ע'
/
COMMENT ON COLUMN DDM_RECDES_PAPERTASK.modifybyid IS '�޸��˱�ʶ'
/
COMMENT ON COLUMN DDM_RECDES_PAPERTASK.modifybyclassid IS '�޸��˶�������'
/
COMMENT ON COLUMN DDM_RECDES_PAPERTASK.modifytime IS '�޸�ʱ��'
/
COMMENT ON COLUMN DDM_RECDES_PAPERTASK.domainid IS '���ʶ'
/
COMMENT ON COLUMN DDM_RECDES_PAPERTASK.domainclassid IS '���������'
/
COMMENT ON COLUMN DDM_RECDES_PAPERTASK.disurgent IS '0Ϊ��ͨ��1Ϊ�Ӽ�'
/
alter table DDM_RECDES_PAPERTASK add constraint PK_DDM_RECDES_PAPERTASK_IID primary key (INNERID) using index tablespace plmindex
/
--׷����  ��		�Ź�ǿ
--׷��ʱ�䣺		2014-09-11
--׷��ԭ��		ֽ��ǩ������
--�Ƿ�Ӱ���������ݣ�	��Ӱ��
--�Ƿ����������ű���	�������������������г�

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

comment on table DDM_DIS_PAPERSIGNTASK is 'ֽ��ǩ������'
/
comment on column DDM_DIS_PAPERSIGNTASK.innerid is '�ڲ���ʶ'
/
comment on column DDM_DIS_PAPERSIGNTASK.classid is '���ʶ'
/
comment on column DDM_DIS_PAPERSIGNTASK.id is '������'
/
comment on column DDM_DIS_PAPERSIGNTASK.name is '��������'
/
comment on column DDM_DIS_PAPERSIGNTASK.note is '��ע'
/
comment on column DDM_DIS_PAPERSIGNTASK.stateid is '��������״̬'
/
comment on column DDM_DIS_PAPERSIGNTASK.statename is '������������'
/
comment on column DDM_DIS_PAPERSIGNTASK.lifecycletemplate is '��������ģ��'
/
comment on column DDM_DIS_PAPERSIGNTASK.contextid is '�������ڲ���ʶ'
/
comment on column DDM_DIS_PAPERSIGNTASK.contextclassid is '����������'
/
comment on column DDM_DIS_PAPERSIGNTASK.createbyid is '���񴴽��˱�ʶ'
/
comment on column DDM_DIS_PAPERSIGNTASK.createbyclassid is '���񴴽�������'
/
comment on column DDM_DIS_PAPERSIGNTASK.createtime is '���񴴽�ʱ��'
/
comment on column DDM_DIS_PAPERSIGNTASK.modifybyid is '�޸��˱�ʶ'
/
comment on column DDM_DIS_PAPERSIGNTASK.modifybyclassid is '�޸��˶�������'
/
comment on column DDM_DIS_PAPERSIGNTASK.modifytime is '�޸�ʱ��'
/
comment on column DDM_DIS_PAPERSIGNTASK.updatetime is 'ϵͳ����֧�ֲ������ֶ�'
/
comment on column DDM_DIS_PAPERSIGNTASK.updatecount is 'ϵͳ����֧�ֲ������ֶ�'
/
comment on column DDM_DIS_PAPERSIGNTASK.receivebyid is '�����˱�ʶ'
/
comment on column DDM_DIS_PAPERSIGNTASK.receivebyclassid is '���������ʶ'
/
comment on column DDM_DIS_PAPERSIGNTASK.receivetime is '����ʱ��'
/
comment on column DDM_DIS_PAPERSIGNTASK.domainid is '���ʶ'
/
comment on column DDM_DIS_PAPERSIGNTASK.domainclassid is '���������'
/
comment on column DDM_DIS_PAPERSIGNTASK.electasktype is '�������ͣ�0Ϊ���ڣ�1Ϊ���⣩'
/
comment on column DDM_DIS_PAPERSIGNTASK.sourcesiteid is 'Դվ���ڲ���ʾ'
/
comment on column DDM_DIS_PAPERSIGNTASK.sourcesiteclassid is 'Դվ�����ʶ'
/
comment on column DDM_DIS_PAPERSIGNTASK.sourcesitename is 'Դվ������'
/
comment on column DDM_DIS_PAPERSIGNTASK.targetsiteid is 'Ŀ��վ���ڲ���ʶ'
/
comment on column DDM_DIS_PAPERSIGNTASK.targetsiteclassid is 'Ŀ��վ�����ʶ'
/
comment on column DDM_DIS_PAPERSIGNTASK.targetsitename is 'Ŀ��վ������'
/
comment on column DDM_DIS_PAPERSIGNTASK.centersiteid is '����վ���ڲ���ʶ'
/
comment on column DDM_DIS_PAPERSIGNTASK.centersiteclassid is '����վ�����ʶ'
/
comment on column DDM_DIS_PAPERSIGNTASK.receivebyname is '����������'
/
comment on column DDM_DIS_PAPERSIGNTASK.disurgent is '������'
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

COMMENT ON TABLE "DDM_DC_A3_TASK_SIGNATURE" IS 'A3ǩ����Ϣ'
/
COMMENT ON COLUMN "DDM_DC_A3_TASK_SIGNATURE"."INNERID" IS '�ڲ���ʶ'
/
COMMENT ON COLUMN "DDM_DC_A3_TASK_SIGNATURE"."CLASSID" IS '����'
/
COMMENT ON COLUMN "DDM_DC_A3_TASK_SIGNATURE"."DEALUSERIID" IS '�����˱�ʶ'
/
COMMENT ON COLUMN "DDM_DC_A3_TASK_SIGNATURE"."DEALUSERID" IS '�����˱��'
/
COMMENT ON COLUMN "DDM_DC_A3_TASK_SIGNATURE"."DEALUSERNAME" IS '����������'
/
COMMENT ON COLUMN "DDM_DC_A3_TASK_SIGNATURE"."DEALDEVISIONIID" IS '�����˲��ű�ʶ'
/
COMMENT ON COLUMN "DDM_DC_A3_TASK_SIGNATURE"."DEALDEVISIONID" IS '�����˲��ű��'
/
COMMENT ON COLUMN "DDM_DC_A3_TASK_SIGNATURE"."DEALDEVISIONNAME" IS '�����˲�������'
/
COMMENT ON COLUMN "DDM_DC_A3_TASK_SIGNATURE"."DEALROLE" IS '�����˽�ɫ'
/
COMMENT ON COLUMN "DDM_DC_A3_TASK_SIGNATURE"."ISAGREE" IS '�Ƿ�ͬ��'
/
COMMENT ON COLUMN "DDM_DC_A3_TASK_SIGNATURE"."DEALMIND" IS '�������'
/
COMMENT ON COLUMN "DDM_DC_A3_TASK_SIGNATURE"."MINDTYPE" IS '�������'
/
COMMENT ON COLUMN "DDM_DC_A3_TASK_SIGNATURE"."ISNEEDDEVISIONMIND" IS '�Ƿ���Ҫ��λ���'
/
COMMENT ON COLUMN "DDM_DC_A3_TASK_SIGNATURE"."STARTTIME" IS '����ʼʱ��'
/
COMMENT ON COLUMN "DDM_DC_A3_TASK_SIGNATURE"."SIGNATURETIME" IS 'ǩ��ʱ��'
/
COMMENT ON COLUMN "DDM_DC_A3_TASK_SIGNATURE"."DOMAINIID" IS '���ʶ'
/
COMMENT ON COLUMN "DDM_DC_A3_TASK_SIGNATURE"."ORDERIID" IS '���ݱ�ʶ'
/
COMMENT ON COLUMN "DDM_DC_A3_TASK_SIGNATURE"."TASKIID" IS '�����ʶ'
/
COMMENT ON COLUMN "DDM_DC_A3_TASK_SIGNATURE"."UPDATETIME" IS 'ϵͳ����֧�ֲ������ֶ�'
/
COMMENT ON COLUMN "DDM_DC_A3_TASK_SIGNATURE"."UPDATECOUNT" IS 'ϵͳ����֧�ֲ������ֶ�'
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
COMMENT ON TABLE "DDM_DC_A3_SIGOBJLINK" IS '���ŵ���ַ�����link��'
/
COMMENT ON COLUMN "DDM_DC_A3_SIGOBJLINK"."INNERID" IS '�ڲ���ʶ'
/
COMMENT ON COLUMN "DDM_DC_A3_SIGOBJLINK"."CLASSID" IS '����'
/
COMMENT ON COLUMN "DDM_DC_A3_SIGOBJLINK"."FROMOBJECTID" IS '���ŵ��ڲ���ʶ'
/
COMMENT ON COLUMN "DDM_DC_A3_SIGOBJLINK"."FROMOBJECTCLASSID" IS '���ŵ�����'
/
COMMENT ON COLUMN "DDM_DC_A3_SIGOBJLINK"."TOOBJECTID" IS '�ַ�����Դ�ڲ���ʶ'
/
COMMENT ON COLUMN "DDM_DC_A3_SIGOBJLINK"."TOOBJECTCLASSID" IS '�ַ�����Դ����'
/
COMMENT ON COLUMN "DDM_DC_A3_SIGOBJLINK"."UPDATETIME" IS 'ϵͳ����֧�ֲ������ֶ�'
/
COMMENT ON COLUMN "DDM_DC_A3_SIGOBJLINK"."UPDATECOUNT" IS 'ϵͳ����֧�ֲ������ֶ�'
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
