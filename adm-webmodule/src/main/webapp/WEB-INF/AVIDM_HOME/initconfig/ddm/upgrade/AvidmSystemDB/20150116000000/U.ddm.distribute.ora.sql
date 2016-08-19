--追加人  ：		徐睿
--追加原因：		发放管理outlook url变更
update AATXDATA t set t.link='/ddm/distributeorder/distributeOrder_main.jsp' where t.txid = 'plm_distribute_manager' and t.appref='plm'
/



