package com.bjsasc.ddm.distribute.service.distributesendorder;

import java.util.List;
import java.util.Map;

import com.bjsasc.ddm.common.CheckPermission;
import com.bjsasc.ddm.common.ConstUtil;
import com.bjsasc.ddm.distribute.helper.DistributeHelper;
import com.bjsasc.ddm.distribute.model.distributeorder.DistributeOrder;
import com.bjsasc.ddm.distribute.service.distributeorder.DistributeOrderService;
import com.bjsasc.plm.core.query.condition.Condition;
import com.bjsasc.plm.grid.data.GridDataService;
import com.bjsasc.plm.type.type.Type;

public class DistributeSendOrder_GridDataServiceImpl implements GridDataService {

	public List<Map<String, Object>> getRows(String spot, String spotInstance, 
			Map<Type, Condition> typeCondition, Map<String, Object> map) {
		DistributeOrderService service = DistributeHelper.getDistributeOrderService();

		String[] notInStateArray = new String[]{ConstUtil.LC_DISTRIBUTED.getName(),ConstUtil.LC_COMPLETED.getName()};
		List<DistributeOrder> listDis = service
				.listNotInStatesDistributeOrder(notInStateArray);

		//String spot = ConstUtil.SPOT_LISTDISTRIBUTEORDERS;
		List<Map<String, Object>> listDatas = CheckPermission.checkPermissionAF(listDis,
				spot, spot);

		return listDatas;
	}

}
