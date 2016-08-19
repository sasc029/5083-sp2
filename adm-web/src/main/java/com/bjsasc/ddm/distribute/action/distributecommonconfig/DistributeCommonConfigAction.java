package com.bjsasc.ddm.distribute.action.distributecommonconfig;


import java.io.IOException;
import java.util.List;

import com.bjsasc.plm.collaboration.common.BaseAction;
import com.bjsasc.ui.json.DataUtil;
import com.bjsasc.ddm.distribute.helper.DistributeHelper;
import com.bjsasc.ddm.distribute.model.distributecommonconfig.DistributeCommonConfig;
import com.bjsasc.ddm.distribute.service.distributecommonconfig.DistributeCommonConfigService;
import com.bjsasc.ddm.common.InitDistributeCommonConfig;

/**
 * 发放管理通用参数配置 action
 * @author zhangguoqiang 2014-7-11
 */
public class DistributeCommonConfigAction extends BaseAction {
	/** serialVersionUID */
	private static final long serialVersionUID = 735860232873217196L;
	
	//发放管理通用参数配置 service
	private DistributeCommonConfigService service = DistributeHelper.getDistributeCommonConfigService();
	
	private DistributeCommonConfig distributeCommonConfig = service.newDistributeCommonConfig();
	
	/**
	 * 添加发放管理通用配置对象
	 */
	public void addDistributeCommonConfig() throws IOException{
		service.addDistributeCommonConfig(distributeCommonConfig);
		response.getWriter().print(SUCCESS);
	}
	
	/**
	 * 恢复系统默认配置
	 */
	public void recoverDistributeCommonConfig() throws IOException{
		InitDistributeCommonConfig.initDistributeCommonConfig();
		response.getWriter().print(SUCCESS);
	}
	
	/**
	 * 查询所有发放管理通用参数配置
	 */
	public void findAllDistributeCommonConfig() throws IOException{
		List<DistributeCommonConfig> list = service.findAllDistributeCommonConfig();
		response.getWriter().print(DataUtil.listToJson(list));
	}
	
	/**
	 * 删除发放管理通用参数配置
	 */
	public void delDistributeCommonConfig() throws IOException{
		String id = request.getParameter("ids");
		String ids[] = id.split(",");
		service.deleteDistributeCommonConfig(ids);
		response.getWriter().print(SUCCESS);
	}
	
	/**
	 * 修改发放管理通用参数配置对象
	 */
	public void updateDistributeCommonConfig() throws IOException{
		service.updateDistributeCommonConfig(distributeCommonConfig);
		response.getWriter().print(SUCCESS);
	}

	/**
	 * 验证 配置编号是否已存在
	 */
	public void checkConfigID() throws IOException{
		String configID = request.getParameter("configID");
		boolean bo = service.checkDistributeCommonConfigOfConfigID(configID);
		response.getWriter().print(bo);
	}
	
	/**
	 * 
	 * 修改运行时配置值
	 * @throws IOException 
	 * 
	 */
	public void updateSingleEditValue() throws IOException{
		
		//获取要修改的字段
		String columnValue = request.getParameter("flag");
		//获取修改后的值
		String columnResult = request.getParameter("value");
		//获取要修改某一列的ID值作为修改条件
		String columnInnerId = request.getParameter("columnInnerId");
		//确认修改指定信息
		service.updateDistributeCommonConfigByInnerId(columnValue, columnResult, columnInnerId);
		response.getWriter().print(SUCCESS);
	}
	
	public DistributeCommonConfig getDistributeCommonConfig() {
		return distributeCommonConfig;
	}

	public void setDistributeCommonConfig(DistributeCommonConfig distributeCommonConfig) {
		this.distributeCommonConfig = distributeCommonConfig;
	}
}