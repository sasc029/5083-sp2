package com.bjsasc.ddm.common;

import com.bjsasc.plm.core.system.init.Inited;
import com.cascc.avidm.util.UUIDService;
import com.cascc.platform.aa.AAProvider;
import com.cascc.platform.aa.api.data.AADomainData;
import com.cascc.platform.aa.api.data.AAPripType;
import com.cascc.platform.aa.api.data.AAGroupData;
/**
 * 
 * ���ڷ��Ź���ģ��ĳ���������Ҫ��ƽ̨��������ɫ����´���һ���������ȵ���
 * ����������������Ȩ�ޣ�������Ҫ�ڳ�ʼ����ʱ��Ѷ������׳�ʼ�������ݿ�
 * 
 * @author zhang guoqiang
 *
 */
public class DdmInitTool implements Inited{

	@Override
	public void init() {
		//ȡ��������ɫ��֯����
		AAPripType pripType_xzjs = AAProvider.getPripTypeService().getPripType(null, "XZJS");
		//ȡ�ö���������
		AAGroupData group_second_sche = AAProvider.getGroupService().getGroupByID(null, "second_sche");
		//����������ɫ��֯���Ͳ��Ҷ��������鲻���ڵ�����£���������������
		if(group_second_sche == null && pripType_xzjs != null){
			AADomainData aaDomain = AAProvider.getDomainService().getRootDomain(null);
			AAGroupData group = new AAGroupData();
			group.setInnerID(UUIDService.getUUID());
			group.setId("second_sche");
			group.setName("��������");
			group.setWeight(10);
			group.setGroupCategory("XZJS");
			group.setDomainRef(aaDomain.getInnerID());
			group.setDescription("���Ź���ģ��ʹ�õĶ�������Ա��");
			AAProvider.getGroupService().insertGroup(null, group);
	}
		
	}

}
