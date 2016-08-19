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

