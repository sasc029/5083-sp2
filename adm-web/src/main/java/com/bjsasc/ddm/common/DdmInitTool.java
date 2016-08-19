package com.bjsasc.ddm.common;

import com.bjsasc.plm.core.system.init.Inited;
import com.cascc.avidm.util.UUIDService;
import com.cascc.platform.aa.AAProvider;
import com.cascc.platform.aa.api.data.AADomainData;
import com.cascc.platform.aa.api.data.AAPripType;
import com.cascc.platform.aa.api.data.AAGroupData;
/**
 * 
 * 由于发放管理模块的出厂设置需要在平台的行政角色组底下创建一个二级调度的组
 * 并给二级调度配置权限，所以需要在初始化的时候把二级调醋初始化到数据库
 * 
 * @author zhang guoqiang
 *
 */
public class DdmInitTool implements Inited{

	@Override
	public void init() {
		//取得行政角色组织类型
		AAPripType pripType_xzjs = AAProvider.getPripTypeService().getPripType(null, "XZJS");
		//取得二级调度组
		AAGroupData group_second_sche = AAProvider.getGroupService().getGroupByID(null, "second_sche");
		//存在行政角色组织类型并且二级调度组不存在的情况下，创建二级调度组
		if(group_second_sche == null && pripType_xzjs != null){
			AADomainData aaDomain = AAProvider.getDomainService().getRootDomain(null);
			AAGroupData group = new AAGroupData();
			group.setInnerID(UUIDService.getUUID());
			group.setId("second_sche");
			group.setName("二级调度");
			group.setWeight(10);
			group.setGroupCategory("XZJS");
			group.setDomainRef(aaDomain.getInnerID());
			group.setDescription("发放管理模块使用的二级调度员组");
			AAProvider.getGroupService().insertGroup(null, group);
	}
		
	}

}
