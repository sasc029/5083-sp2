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