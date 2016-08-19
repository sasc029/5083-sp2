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
 * ���Ź���ͨ�ò������� action
 * @author zhangguoqiang 2014-7-11
 */
public class DistributeCommonConfigAction extends BaseAction {
	/** serialVersionUID */
	private static final long serialVersionUID = 735860232873217196L;
	
	//���Ź���ͨ�ò������� service
	private DistributeCommonConfigService service = DistributeHelper.getDistributeCommonConfigService();
	
	private DistributeCommonConfig distributeCommonConfig = service.newDistributeCommonConfig();
	
	/**
	 * ��ӷ��Ź���ͨ�����ö���
	 */
	public void addDistributeCommonConfig() throws IOException{
		service.addDistributeCommonConfig(distributeCommonConfig);
		response.getWriter().print(SUCCESS);
	}
	
	/**
	 * �ָ�ϵͳĬ������
	 */
	public void recoverDistributeCommonConfig() throws IOException{
		InitDistributeCommonConfig.initDistributeCommonConfig();
		response.getWriter().print(SUCCESS);
	}
	
	/**
	 * ��ѯ���з��Ź���ͨ�ò�������
	 */
	public void findAllDistributeCommonConfig() throws IOException{
		List<DistributeCommonConfig> list = service.findAllDistributeCommonConfig();
		response.getWriter().print(DataUtil.listToJson(list));
	}
	
	/**
	 * ɾ�����Ź���ͨ�ò�������
	 */
	public void delDistributeCommonConfig() throws IOException{
		String id = request.getParameter("ids");
		String ids[] = id.split(",");
		service.deleteDistributeCommonConfig(ids);
		response.getWriter().print(SUCCESS);
	}
	
	/**
	 * �޸ķ��Ź���ͨ�ò������ö���
	 */
	public void updateDistributeCommonConfig() throws IOException{
		service.updateDistributeCommonConfig(distributeCommonConfig);
		response.getWriter().print(SUCCESS);
	}

	/**
	 * ��֤ ���ñ���Ƿ��Ѵ���
	 */
	public void checkConfigID() throws IOException{
		String configID = request.getParameter("configID");
		boolean bo = service.checkDistributeCommonConfigOfConfigID(configID);
		response.getWriter().print(bo);
	}
	
	/**
	 * 
	 * �޸�����ʱ����ֵ
	 * @throws IOException 
	 * 
	 */
	public void updateSingleEditValue() throws IOException{
		
		//��ȡҪ�޸ĵ��ֶ�
		String columnValue = request.getParameter("flag");
		//��ȡ�޸ĺ��ֵ
		String columnResult = request.getParameter("value");
		//��ȡҪ�޸�ĳһ�е�IDֵ��Ϊ�޸�����
		String columnInnerId = request.getParameter("columnInnerId");
		//ȷ���޸�ָ����Ϣ
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