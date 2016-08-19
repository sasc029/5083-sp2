package com.bjsasc.adm.common;

import com.bjsasc.plm.core.Helper;
import com.bjsasc.plm.core.context.model.Context;
import com.bjsasc.plm.core.domain.Domain;
import com.bjsasc.plm.core.folder.Cabinet;

/**
 * 现行数据管理，文件柜创建类。
 * 
 * @author gengancong 2013-07-01
 */
public abstract class ActiveCabinetUtil {
	
	public static final String CABINET_NAME_DEFAULT = "Default";

	public static final String CABINET_NAME_SYSTEM = "System";

	/**
	 * 创建"共享"文件柜
	 * @param context
	 * @param domain
	 * @return
	 */
	public static Cabinet createCabinetDefault(Context context, Domain domain) {
		return createCabinet(CABINET_NAME_DEFAULT, Cabinet.CABINET_TYPE_DEFALUT, context, domain);
	}

	/**
	 * 创建"系统"文件柜
	 * @param context
	 * @param domain
	 * @return
	 */
	public static Cabinet createCabinetSystem(Context context, Domain domain) {
		return createCabinet(CABINET_NAME_SYSTEM, Cabinet.CABINET_TYPE_SYSTEM, context, domain);
	}

	/**
	 * 创建文件柜
	 * @param cabinetName 文件柜名称
	 * @param cabinetType 文件柜类型
	 * @param context 所属上下文
	 * @param domain 所属域
	 * @return
	 */
	public static Cabinet createCabinet(String cabinetName, String cabinetType, Context context, Domain domain) {
		Cabinet cabinet = new Cabinet();
		cabinet.setClassId("ActiveCabinet");
		cabinet.setCabtype(Cabinet.CABINET_TYPE_DEFALUT);		
		cabinet.setName(cabinetName);
		cabinet.setCabtype(cabinetType);
		cabinet.setContextInfo(context.buildContextInfo());
		cabinet.setDomainInfo(domain.buildDomainInfo());

		Helper.getPersistService().save(cabinet);

		return cabinet;
	}
}
