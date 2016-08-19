package com.bjsasc.ddm.common;

import com.bjsasc.ddm.distribute.model.distributeobject.DistributeObject;
import com.bjsasc.plm.Helper;
import com.bjsasc.plm.core.cad.CADDocument;
import com.bjsasc.plm.core.cad.mapping.CADDocType;
import com.bjsasc.plm.core.cad.mapping.MappingHelper;
import com.bjsasc.plm.core.persist.model.Persistable;
import com.bjsasc.plm.core.type.TypeDefinition;
import com.bjsasc.plm.core.type.TypeHelper;
import com.bjsasc.plm.type.attr.Attr;
import com.bjsasc.plm.type.formater.Formater;
import com.bjsasc.plm.type.formater.impl.TypeFormatImpl;
import com.bjsasc.plm.url.Url;

/**
 * ��ʽ������
 * Grid ����ִ�� JavaScript ���� .
 * ����Ƿַ�������Ҫȡ�÷ַ��������ݵ�ͼ������ʾ������
 * �����CAD�ĵ���ͼ�������⴦��
 * 
 * @author zhangguoqiang 2014-12-16
 */
public class DisTypeFormatImpl extends TypeFormatImpl {

	/* (non-Javadoc)
	 * @see com.bjsasc.plm.type.formater.impl.TypeFormatImpl#format(java.lang.Object, com.bjsasc.plm.type.attr.Attr, com.bjsasc.plm.type.formater.Formater)
	 */
	public Object format(Object target, Attr attr, Formater formater) {
		Object result = super.format(target, attr, formater);
		
		if(target instanceof DistributeObject){
			DistributeObject disObj=(DistributeObject)target;
			
			Persistable persist = Helper.getPersistService().getObject(Helper.getOid(disObj.getDataClassId(), disObj.getDataInnerId()));
			//����ǿ���ĵ��ӷַ���ʱ������ȡ�����ַ���������,������Ҫ���Ӷ�persist��null�ж�
			if(persist != null){
				//CAD�ĵ����������ͣ���Ҫ��������������ʾͼ��
				if(persist instanceof CADDocument) {
					CADDocType docType = MappingHelper.getService().getDocType((CADDocument)persist);
					if(docType != null) {
						String typeName = docType.getModelTypeName();
						String url = Url.APP + docType.getModelIcon();
						result = Helper.getUrlManager().addTagImage(url, "", typeName);
					}
				}else{
					String typeName = Helper.getTypeService().getTypeName(persist);
					String url = Url.APP + Helper.getTypeService().getIcon(persist);
					result= Helper.getUrlManager().addTagImage(url, "", typeName);
				}
			}else{
				TypeDefinition type = TypeHelper.getService().getType(disObj.getDataClassId());
				String typeName = type.getName();
				String url = Url.APP + type.getIcon();
				result= Helper.getUrlManager().addTagImage(url, "", typeName);
			}
		}
		return result;
	}
}
