package com.bjsasc.adm.active.service.activerelated;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.bjsasc.adm.active.model.activeorder.ActiveOrder;
import com.bjsasc.plm.Helper;
import com.bjsasc.plm.core.query.condition.Condition;
import com.bjsasc.plm.grid.data.GridDataService;
import com.bjsasc.plm.type.type.Type;

/**
 * 相关对象（现行单据）查询服务实现类。
 * 
 * @author gengancong 2013-6-3
 */
public class RelatedActiveOrderImpl implements GridDataService {

	public List<Map<String, Object>> getRows(String spot, String spotInstance, 
			Map<Type, Condition> typeCondition, Map<String, Object> map) {

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		// 取得当前现行OID
		String activeOid = (String) map.get("OID");

		String sql = "SELECT DISTINCT O.* FROM ADM_ACTIVEORDER O, ADM_ACTIVEORDEREDLINK L "
				+ " WHERE O.CLASSID = L.FROMOBJECTCLASSID " + "   AND O.INNERID = L.FROMOBJECTID "
				+ "   AND (L.TOOBJECTCLASSID || ':' || L.TOOBJECTID = ? "
				+ "    or L.ORDEREDBEFORECLASSID || ':' || L.ORDEREDBEFOREID = ?)";

		List<ActiveOrder> resultList = Helper.getPersistService().query(sql, ActiveOrder.class, 
				activeOid, activeOid);

		for (ActiveOrder order : resultList) {
			Map<String, Object> orderMap = Helper.getTypeManager().format(order);
			list.add(orderMap);
		}

		return list;
	}

}
