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
	 * 通过纸质任务和分发数据的oid获取DistributeObjectPrint。
	 * 
	 * @param paperTaskOid String
	 * @param objOid String
	 * @return List
	 */
	public List<DistributeObjectPrint> getDisObjPrintByDisPaperTaskAndObjOID(String paperTaskOid,String objOid);
	/**
	 * 通过纸质任务和分发数据的oid对DistributeObjectPrint的isprint字段进行插值“1”，表示已打印
	 * 
	 * @param paperTaskOid String
	 * @param classid String
	 * void
	 */
	public DistributeObjectPrint setDistributeObjectPrint(String paperTaskOid, String objOid);
	
	/**
	 * new DistributeObjectPrint对象
	 * @param classid DistributeObjectPrintclassid
	 * @return DistributeObjectPrint
	 */
	public DistributeObjectPrint newDistributeObjectPrint(String classId);
	
	/**
	 * 批量保存DistributeObjectPrint
	 * @param dopList
	 */
	public void saveDistributeObjectPrint(List<DistributeObjectPrint> dopList);

}
