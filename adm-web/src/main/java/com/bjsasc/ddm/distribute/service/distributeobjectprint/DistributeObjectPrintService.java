package com.bjsasc.ddm.distribute.service.distributeobjectprint;

import java.util.List;
import com.bjsasc.ddm.distribute.model.distributeobjectprint.DistributeObjectPrint;

/**
 * 
 * @author yangzhenzhou
 *
 */
public interface DistributeObjectPrintService {
	
	/**
	 * ͨ��ֽ������ͷַ����ݵ�oid��ȡDistributeObjectPrint��
	 * 
	 * @param paperTaskOid String
	 * @param objOid String
	 * @return List
	 */
	public List<DistributeObjectPrint> getDisObjPrintByDisPaperTaskAndObjOID(String paperTaskOid,String objOid);
	/**
	 * ͨ��ֽ������ͷַ����ݵ�oid��DistributeObjectPrint��isprint�ֶν��в�ֵ��1������ʾ�Ѵ�ӡ
	 * 
	 * @param paperTaskOid String
	 * @param classid String
	 * void
	 */
	public DistributeObjectPrint setDistributeObjectPrint(String paperTaskOid, String objOid);
	
	/**
	 * new DistributeObjectPrint����
	 * @param classid DistributeObjectPrintclassid
	 * @return DistributeObjectPrint
	 */
	public DistributeObjectPrint newDistributeObjectPrint(String classId);
	
	/**
	 * ��������DistributeObjectPrint
	 * @param dopList
	 */
	public void saveDistributeObjectPrint(List<DistributeObjectPrint> dopList);

}
