--׷����  ��		���
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

--����DDM_DIS_INFO���ֶ�SEALINFO(������Ϣ)
alter table DDM_DIS_INFO add SEALINFO VARCHAR2(255);
/
comment on column DDM_DIS_INFO.sealinfo  is '������Ϣ'
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


