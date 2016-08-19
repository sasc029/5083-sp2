package com.bjsasc.ddm.distribute.service.distributeobjectnotautocreate;

import java.util.List;
import java.util.Map;

import com.bjsasc.ddm.distribute.helper.DistributeHelper;
import com.bjsasc.ddm.distribute.service.distributeobjecttype.DistributeObjectTypeService;
import com.bjsasc.plm.core.query.condition.Condition;
import com.bjsasc.plm.grid.data.GridDataService;
import com.bjsasc.plm.type.type.Type;

public class DistributeObjectNotAutoCreate_GridDataServiceImpl implements
	GridDataService{

	@Override
	public List<Map<String, Object>> getRows(String spot, String spotInstance,
			Map<Type, Condition> typeCondition, Map<String, Object> params) {
		String contextOid = (String) params.get("contextOid");
		DistributeObjectNotAutoCreateService service = DistributeHelper.getDistributeObjectNotAutoCreateService();
		List<Map<String, Object>> resultList = service.findByContextOid(contextOid, spot, spotInstance);
		return resultList;
	}

}
