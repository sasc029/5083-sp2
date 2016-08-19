package com.bjsasc.ddm.distribute.service.distributeworkload;

import java.util.List;

import com.bjsasc.ddm.distribute.model.distributeworkload.DistributeWorkload;
import com.bjsasc.platform.objectmodel.business.persist.PersistUtil;
import com.bjsasc.plm.Helper;
import com.bjsasc.plm.core.identifier.Identified;
import com.bjsasc.plm.core.identifier.UniqueIdentified;
import com.bjsasc.plm.core.lifecycle.LifeCycleManaged;
import com.bjsasc.plm.core.persist.model.Persistable;

/**
 * 工作量统计服务实现类。
 * 
 * @author gengancong 2013-3-21
 */
public class DistributeWorkloadServiceImpl implements DistributeWorkloadService {

	@Override
	public List<DistributeWorkload> getDistributeWorkloads(String where) {
		String sql = "select * from DDM_DIS_WORKLOAD t ";
		if (where != null) {
			sql += " where " + where;
		}
		sql += " ORDER BY CREATETIME DESC ";
		List<DistributeWorkload> list = Helper.getPersistService().query(sql, DistributeWorkload.class);
		return list;
	}

	@Override
	public void createDistributeWorkload(DistributeWorkload dis, LifeCycleManaged object) {
		dis.setToLifeCycleInfo(object.getLifeCycleInfo());
		setValues(dis, object);
		if (dis.check()) {
			Helper.getPersistService().save(dis);
		}
	}

	@Override
	public DistributeWorkload newDistributeWorkload(LifeCycleManaged object) {
		DistributeWorkload disObject = (DistributeWorkload) PersistUtil.createObject(DistributeWorkload.CLASSID);
		disObject.setFromLifeCycleInfo(object.getLifeCycleInfo());
		return disObject;
	}

	/**
	 * 对象转换。
	 * @param disObject DistributeWorkload
	 * @param object Persistable
	 */
	private void setValues(DistributeWorkload disObject, Persistable object) {
		disObject.setObjectId(object.getInnerId());
		disObject.setObjectClassId(object.getClassId());
		if (object instanceof UniqueIdentified) {
			UniqueIdentified uniqueIdentified = (UniqueIdentified) object;
			disObject.setNumber(uniqueIdentified.getNumber());
			disObject.setName(uniqueIdentified.getName());
		} else if(object instanceof Identified){
			Identified identified = (Identified) object;
			disObject.setNumber(identified.getNumber());
			disObject.setName(identified.getName());
		}
	}
}