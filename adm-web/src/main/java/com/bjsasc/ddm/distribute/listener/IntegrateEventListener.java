package com.bjsasc.ddm.distribute.listener;

import com.cascc.platform.event.AbstractListener;
import com.cascc.platform.event.Event;
import com.cascc.platform.event.Listener;
import com.bjsasc.platform.lifecycle.event.stateEvent.PhaseEnteredInEvent;
import com.bjsasc.plm.Helper;
import com.bjsasc.ddm.distribute.helper.DistributeHelper;
import com.bjsasc.ddm.distribute.model.distributeorder.DistributeOrder;
import com.bjsasc.ddm.distribute.service.distributecommonconfig.DistributeCommonConfigService;
import com.bjsasc.ddm.distribute.service.integrate.IntegrateService;

/**
 * 生命周期状态变更事件监听
 * 
 * @author zhangguoqiang 2014-07-09
 */
public class IntegrateEventListener extends AbstractListener implements Listener {

	/** serialVersionUID */
	private static final long serialVersionUID = 8204490902187070031L;
	
	DistributeCommonConfigService distributeCommonConfigService = DistributeHelper.getDistributeCommonConfigService();
	
	public void raiseEvent(Event event) {

		if (event instanceof PhaseEnteredInEvent) {
			PhaseEnteredInEvent inEvent = (PhaseEnteredInEvent) event;
			String classId = inEvent.getObjectClassId();
			String innerId = inEvent.getObjectIId();

			com.bjsasc.plm.core.persist.model.Persistable obj = Helper.getPersistService().getObject(classId, innerId);
			
			if(obj instanceof DistributeOrder)
            {
            	DistributeOrder distributeOrder = (DistributeOrder) obj;
            	String is_AutoExportXml = distributeCommonConfigService.getConfigValueByConfigId("Is_AutoExportXml");
            	// 是否开启瀚海之星Xml文件导出功能
            	// (true：发放单状态变为分发中的时候自动导出分发数据，分发信息的Xml文件；false：不导出Xml文件)
				if ("true".equalsIgnoreCase(is_AutoExportXml)) {
					//发放单状态变为分发中的时候，自动导出分发数据，分发信息的Xml文件
					String export_StateName = distributeCommonConfigService.getConfigValueByConfigId("Export_StateName");
					if(export_StateName != null && export_StateName.equals(distributeOrder.getStateName())){
	                	IntegrateService integrateService = DistributeHelper.getIntegrateService();
	                	integrateService.exportXml(distributeOrder.getOid());
	            	}
				}
            }
		}
	}
}
