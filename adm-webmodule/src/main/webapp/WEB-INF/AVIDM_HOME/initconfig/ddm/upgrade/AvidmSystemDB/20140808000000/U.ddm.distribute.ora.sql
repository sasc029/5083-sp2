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

