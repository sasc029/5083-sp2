package com.bjsasc.ddm.distribute.action.distributepapersigntask;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.bjsasc.ddm.common.ConstUtil;
import com.bjsasc.ddm.distribute.action.AbstractAction;
import com.bjsasc.ddm.distribute.helper.DistributeHelper;
import com.bjsasc.ddm.distribute.model.distributeobject.DistributeObject;
import com.bjsasc.ddm.distribute.service.distributepapersigntask.DistributePaperSignTaskService;
import com.bjsasc.plm.Helper;
import com.bjsasc.plm.KeyS;
import com.bjsasc.plm.grid.data.GridDataUtil;
import com.bjsasc.plm.type.TypeService;

/**
 * 纸质签收任务Action实现类
 * 
 * @author zhangguoqiang 2014-09-11
 */
public class DistributePaperSignTaskAction extends AbstractAction {

	/** serialVersionUID */
	private static final long serialVersionUID = 985407439320987763L;

	private static final Logger LOG = Logger.getLogger(DistributePaperSignTaskAction.class);

	
	private final DistributePaperSignTaskService service = DistributeHelper.getDistributePaperSignTaskService();

	/**
	 * 取得纸质签收任务的分发数据信息。
	 * 
	 * @return 画面ID
	 */
	public String getDistributePaperSignTaskObjects() {
		try {
			// 纸质签收任务OID
			String oid = (String) session.getAttribute(ConstUtil.DISTRIBUTE_PAPERSIGNTASK_OID);

			// 取得分发数据列表
			List<DistributeObject> listDisObj = service.getDistributeObjects(oid);

			// 格式化显示数据
			GridDataUtil.prepareRowObjects(listDisObj, ConstUtil.SOPT_LISTDISTRIBUTEPAPERSIGNOBJECTS);

		} catch (Exception ex) {
			error(ex);
		}
		return "paperSignTaskObjects";
	}

}
