package com.bjsasc.adm.active.action.modelview;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.bjsasc.adm.active.action.AbstractAction;
import com.bjsasc.adm.active.helper.AdmHelper;
import com.bjsasc.adm.active.service.modelviewservice.ModelViewService;
import com.bjsasc.adm.common.AdmLifeCycleConstUtil;
import com.bjsasc.platform.objectmodel.business.persist.PTFactor;
import com.bjsasc.plm.Helper;
import com.bjsasc.plm.KeyS;
import com.bjsasc.plm.core.util.StringUtil;
import com.bjsasc.plm.grid.GridHelper;
import com.bjsasc.plm.grid.data.GridDataUtil;

/**
 * 模型视图Action实现类。
 * 
 * @author gengancong 2013-6-18
 */
public class ModelViewAction extends AbstractAction {

	/** serialVersionUID */
	private static final long serialVersionUID = 4947724950254337383L;
	
	private static final String LC_RECYCLE_NAME = AdmLifeCycleConstUtil.LC_RECYCLE.getName();

	/** Log处理 */
	private static final Logger LOG = Logger.getLogger(ModelViewAction.class);

	public String getActiveDocuments() {
		String spot = "ACTIVEDOCUMENT";
		List<String> fkeys = GridHelper.getService().getMyLatestGridViewColumnIds(spot, spot);

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
			String init = request.getParameter("init");
			if (!"true".equalsIgnoreCase(init)) {
				String keys = request.getParameter("keys");
				keys = StringUtil.encoding(keys, "GBK");
				ModelViewService service = AdmHelper.getModelViewService();

				List<Object> forCheckObj = service.getActiveDocuments(keys);
				for (Object obj : forCheckObj) {

					Map<String, Object> map = Helper.getTypeManager().format((PTFactor) obj, fkeys, false);
					// 过滤已删除的数据。
					if (LC_RECYCLE_NAME.equals(map.get("LIFECYCLE_STATE"))) {
						continue;
					}
					map.put(KeyS.ACCESS, true);
					list.add(map);
				}
				LOG.debug("取得现行文件数据: " + getDataSize(list) + " 条");
			}
		} catch (Exception ex) {
			error(ex);
		}
		// 输出结果
		GridDataUtil.prepareRowObjects(list, "ACTIVEDOCUMENT");
		return "content";
	}

}
