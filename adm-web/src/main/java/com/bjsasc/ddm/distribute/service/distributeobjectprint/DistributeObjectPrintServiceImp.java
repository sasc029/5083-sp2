package com.bjsasc.ddm.distribute.service.distributeobjectprint;

import java.util.ArrayList;
import java.util.List;

import com.bjsasc.ddm.common.ConstUtil;
import com.bjsasc.ddm.distribute.model.distributeobjectprint.DistributeObjectPrint;
import com.bjsasc.platform.objectmodel.business.persist.PersistUtil;
import com.bjsasc.plm.Helper;
import com.bjsasc.plm.core.persist.PersistHelper;

/**
 * 任务下分发数据是否打印
 * @author yangzhenzhou
 */
public class DistributeObjectPrintServiceImp  implements DistributeObjectPrintService{

	@Override
	public List<DistributeObjectPrint> getDisObjPrintByDisPaperTaskAndObjOID(String paperTaskOid, String objOid) {
		List<DistributeObjectPrint>	list = new ArrayList<DistributeObjectPrint>();
		String sql = "SELECT * FROM DDM_DIS_OBJECT_PRINT A WHERE A.OBJCLASSID || ':' || A.OBJINNERID = ? AND A.TASKCLASSID || ':' || A.TASKINNERID = ? ";
		list = PersistHelper.getService().query(sql, DistributeObjectPrint.class, objOid,paperTaskOid);
		return list;
	}

	@Override
	public DistributeObjectPrint setDistributeObjectPrint(String paperTaskOid, String objOid) {
		
		DistributeObjectPrint dp = newDistributeObjectPrint(DistributeObjectPrint.CLASSID);
				
		String paperTaskinnerId = Helper.getInnerId(paperTaskOid);
		String paperTaskclassId = Helper.getClassId(paperTaskOid);
		String objinnerId = Helper.getInnerId(objOid);
		String objclassId = Helper.getClassId(objOid);
		
		dp.setObjClassid(objclassId);
		dp.setObjInnerid(objinnerId);
		dp.setTaskClassid(paperTaskclassId);
		dp.setTaskInnerid(paperTaskinnerId);
		dp.setIsprint(ConstUtil.OBJECT_PRINT_SIGN_YES);
		
		return dp;
	}

	@Override
	public DistributeObjectPrint newDistributeObjectPrint(String classId) {
		DistributeObjectPrint dop = (DistributeObjectPrint)PersistUtil.createObject(classId);
		dop.setClassId(classId);
		return dop;
	}

	@Override
	public void saveDistributeObjectPrint(List<DistributeObjectPrint> dopList) {
		Helper.getPersistService().saveBatch(dopList);
	}
}
