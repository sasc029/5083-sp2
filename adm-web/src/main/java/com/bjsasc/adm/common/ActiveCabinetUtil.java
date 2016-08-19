package com.bjsasc.adm.common;

import com.bjsasc.plm.core.Helper;
import com.bjsasc.plm.core.context.model.Context;
import com.bjsasc.plm.core.domain.Domain;
import com.bjsasc.plm.core.folder.Cabinet;

/**
 * �������ݹ����ļ��񴴽��ࡣ
 * 
 * @author gengancong 2013-07-01
 */
public abstract class ActiveCabinetUtil {
	
	public static final String CABINET_NAME_DEFAULT = "Default";

	public static final String CABINET_NAME_SYSTEM = "System";

	/**
	 * ����"����"�ļ���
	 * @param context
	 * @param domain
	 * @return
	 */
	public static Cabinet createCabinetDefault(Context context, Domain domain) {
		return createCabinet(CABINET_NAME_DEFAULT, Cabinet.CABINET_TYPE_DEFALUT, context, domain);
	}

	/**
	 * ����"ϵͳ"�ļ���
	 * @param context
	 * @param domain
	 * @return
	 */
	public static Cabinet createCabinetSystem(Context context, Domain domain) {
		return createCabinet(CABINET_NAME_SYSTEM, Cabinet.CABINET_TYPE_SYSTEM, context, domain);
	}

	/**
	 * �����ļ���
	 * @param cabinetName �ļ�������
	 * @param cabinetType �ļ�������
	 * @param context ����������
	 * @param domain ������
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
