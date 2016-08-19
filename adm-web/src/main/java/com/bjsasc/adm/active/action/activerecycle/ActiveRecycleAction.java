package com.bjsasc.adm.active.action.activerecycle;

import java.util.List;

import org.apache.log4j.Logger;

import com.bjsasc.adm.active.action.AbstractAction;
import com.bjsasc.adm.active.helper.AdmHelper;
import com.bjsasc.adm.active.model.activerecycle.ActiveRecycle;
import com.bjsasc.adm.active.service.activerecycleservice.ActiveRecycleService;
import com.bjsasc.adm.active.service.admmodelservice.AdmModelService;
import com.bjsasc.adm.common.AdmLifeCycleConstUtil;
import com.bjsasc.plm.core.Helper;
import com.bjsasc.plm.core.folder.SubFolder;
import com.bjsasc.plm.core.lifecycle.LifeCycleManaged;
import com.bjsasc.plm.core.persist.model.Persistable;
import com.bjsasc.plm.grid.data.GridDataUtil;
import com.bjsasc.plm.util.Ajax;
import com.cascc.avidm.util.SplitString;

/**
 * 回收站Action实现类。
 * 
 * @author gengancong 2013-6-3
 */
public class ActiveRecycleAction extends AbstractAction {

	/** serialVersionUID */
	private static final long serialVersionUID = 6224514110020121703L;

	/** 回收站服务 */
	ActiveRecycleService service = AdmHelper.getActiveRecycleService();

	/** Log处理 */
	private static final Logger LOG = Logger.getLogger(ActiveRecycleAction.class);

	/**
	 * 取得所有回收站对象
	 * 
	 * @return INITPAGE
	 */
	public String getAllRecycle() {
		try {
			List<ActiveRecycle> arList = service.listItems();
			LOG.debug("取得回收数据: " + getDataSize(arList) + " 条");
			// 输出结果
			GridDataUtil.prepareRowObjects(arList, "ListActiveRecycles");
		} catch (Exception ex) {
			error(ex);
		}
		return INITPAGE;
	}

	/**
	 * 添加对象回收站。
	 */
	public String addRecycle() {
		try {
			String oids = request.getParameter("OIDS");

			boolean hasModelFolder = false, deleteFlag = true;
			AdmModelService modelService = AdmHelper.getAdmModelService();
			List<String> oidList = SplitString.string2List(oids, ",");
			for (String oid : oidList) {
				Persistable object = Helper.getPersistService().getObject(oid);
				if (modelService.isModelFolder(oid)) {
					SubFolder sf = (SubFolder) object;
					hasModelFolder = true;
					result.put(Ajax.SUCCESS, Ajax.FASLE);
					result.put(Ajax.MESSAGE, "模型对应的文件夹 [" + sf.getName()
							+ "] 不可以被删除。");
					break;
				}
				if (object instanceof LifeCycleManaged && object != null) {
					LifeCycleManaged lcm = (LifeCycleManaged) object;
					boolean flag = false;
					String stateName = lcm.getLifeCycleInfo().getStateName();
					/** 现行数据管理-生命周期-审批中 */
					if (AdmLifeCycleConstUtil.LC_APPROVING.getName().equals(stateName)) {
						flag = true;
					} else
					/** 现行数据管理-生命周期-受控中 */
					if (AdmLifeCycleConstUtil.LC_CONTROLLING.getName().equals(stateName)) {
						flag = true;
					} else
					/** 现行数据管理-生命周期-发放中 */
					if (AdmLifeCycleConstUtil.LC_PROVIDING.getName().equals(stateName)) {
						flag = true;
					} else
					/** 现行数据管理-生命周期-已删除 */
					if (AdmLifeCycleConstUtil.LC_RECYCLE.getName().equals(stateName)) {
						flag = true;
					}
					if (flag) {
						deleteFlag = false;
						result.put(Ajax.SUCCESS, Ajax.FASLE);
						result.put(Ajax.MESSAGE, "生命周期状态为 ["+stateName+"] 的数据不可以被删除。");
						break;
					}
				}
			}
			if (!hasModelFolder && deleteFlag) {
				service.addltem(oids);
				success();
			}
		} catch (Exception ex) {
			jsonError(ex);
		}
		mapToSimpleJson();
		return OUTPUTDATA;
	}

	/**
	 * 删除回收站。
	 */
	public String deleteRecycle() {
		try {
			String oids = request.getParameter("OIDS");
			service.deleteItems(oids);
			// 重新显示数据
			getAllRecycle();

			success();
		} catch (Exception ex) {
			jsonError(ex);
		}
		mapToSimpleJson();
		return OUTPUTDATA;
	}

	/**
	 * 清空回收站。
	 */
	public String clearRecycle() {
		try {
			service.clearItems();
			// 重新显示数据
			getAllRecycle();

			success();
		} catch (Exception ex) {
			jsonError(ex);
		}
		mapToSimpleJson();
		return OUTPUTDATA;
	}

	/**
	 * 还原回收站。
	 */
	public String reductionRecycle() {
		try {
			String oids = request.getParameter("OIDS");
			service.reductionItems(oids);
			// 重新显示数据
			getAllRecycle();

			success();
		} catch (Exception ex) {
			jsonError(ex);
		}
		mapToSimpleJson();
		return OUTPUTDATA;
	}
}
