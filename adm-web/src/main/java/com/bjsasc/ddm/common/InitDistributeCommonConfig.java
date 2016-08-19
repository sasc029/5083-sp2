package com.bjsasc.ddm.common;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.bjsasc.ddm.distribute.helper.DistributeHelper;
import com.bjsasc.ddm.distribute.model.distributecommonconfig.DistributeCommonConfig;
import com.bjsasc.ddm.distribute.service.distributecommonconfig.DistributeCommonConfigService;
import com.cascc.avidm.util.SysConfig;

/**
 * 发放管理通用配置初始化共通处理。
 * 
 * @author zhangguoqiang 2014-7-11
 */
public class InitDistributeCommonConfig {
	// 发放管理通用配置 service
	private static DistributeCommonConfigService disComCfgService = DistributeHelper.getDistributeCommonConfigService();
	
	private static final String STR_FALSE = "false";
	private static final String STR_TRUE = "true";
	
	/**
	 * 初始化配置
	 * 
	 * @return void
	 */
	public static void initDistributeCommonConfig() {
		List<DistributeCommonConfig> disComCfgList = getDistributeCommonConfigList();
		for (int i = 0; i < disComCfgList.size(); i++) {
			DistributeCommonConfig disComConfig = (DistributeCommonConfig) disComCfgList.get(i);
			DistributeCommonConfig tempdmcConfig = disComCfgService
					.findDistributeCommonConfigByConfigId(disComConfig.getConfigID());
			if (tempdmcConfig == null) {
				disComCfgService.addDistributeCommonConfig(disComConfig);
			}
		}
	}
	
	/**
	 * 获取发放管理通用配置
	 * 
	 * @return
	 */
	private static List<DistributeCommonConfig> getDistributeCommonConfigList() {
		
		List<DistributeCommonConfig> disComCfgObjList = new ArrayList<DistributeCommonConfig>();
		List<String[]> disComCfgList = initDefaultDistributeCommonConfigList();
		Iterator<String[]> it = disComCfgList.iterator();
		while (it.hasNext()) {
			String[] agr = (String[]) it.next();
			DistributeCommonConfig dcon = new DistributeCommonConfig();
			dcon.setConfigID(agr[0]);
			dcon.setConfigName(agr[1]);
			dcon.setConfigValue(agr[2]);
			dcon.setConfigDefaultValue(agr[3]);
			if (agr[4] != null && agr[4].equals(STR_FALSE)) { 
				// 1:不允许删除
				dcon.setIsPermitDelete(1);
			} else {
				// 0:允许删除
				dcon.setIsPermitDelete(0);
			}

			disComCfgObjList.add(dcon);
		}
		return disComCfgObjList;
	}

	/**
	 * 初始化发放管理通用配置
	 * 
	 * @return
	 */
	public static List<String[]> initDefaultDistributeCommonConfigList() {
		// 初始化内容
		List<String[]> disComCfgList = new ArrayList<String[]>();
		String[] agr10 = new String[5];
		//配置编号
		agr10[0] = "Is_AutoExportXml";
		//配置名称
		agr10[1] = "系统集成-是否开启瀚海之星Xml文件导出功能";
		//配置值
		agr10[2] = "";
		//配置默认值
		agr10[3] = STR_FALSE;
		//是否允许删除
		agr10[4] = STR_FALSE;
		disComCfgList.add(agr10);

		String[] agr11 = new String[5];
		agr11[0] = "Export_StateName";
		agr11[1] = "系统集成-导出时点发放单生命周期状态名称";
		agr11[2] = "";
		agr11[3] = ConstUtil.LC_DISTRIBUTING.getName();
		agr11[4] = STR_FALSE;
		disComCfgList.add(agr11);
		
		String[] agr12 = new String[5];
		agr12[0] = "Export_Company";
		agr12[1] = "系统集成-导出用公司标识";
		agr12[2] = "";
		agr12[3] = "神软";
		agr12[4] = STR_FALSE;
		disComCfgList.add(agr12);

		String[] agr13 = new String[5];
		agr13[0] = "Export_System";
		agr13[1] = "系统集成-导出用系统标识";
		agr13[2] = "";
		agr13[3] = "Avidm5";
		agr13[4] = STR_FALSE;
		disComCfgList.add(agr13);

		String[] agr14 = new String[5];
		agr14[0] = "Export_ServerTransferPath";
		agr14[1] = "系统集成-导出中转用Web服务器路径(默认值:Web服务器路径)";
		agr14[2] = "";
		agr14[3] = SysConfig.getTempDir();
		agr14[4] = STR_FALSE;
		disComCfgList.add(agr14);

		String[] agr15 = new String[5];
		agr15[0] = "Is_NeedZip";
		agr15[1] = "系统集成-导出文件夹是否需要压缩(true:是;false:否)";
		agr15[2] = "";
		agr15[3] = STR_TRUE;
		agr15[4] = STR_TRUE;
		disComCfgList.add(agr15);

		String[] agr16 = new String[5];
		agr16[0] = "TransferMethod";
		agr16[1] = "系统集成-传输方式（dir:导出到指定路径;webservice:调用WebService）";
		agr16[2] = "";
		agr16[3] = "webservice";
		agr16[4] = STR_FALSE;
		disComCfgList.add(agr16);

		String[] agr17 = new String[5];
		agr17[0] = "WebServiceUrl";
		agr17[1] = "系统集成-瀚海之星WebService的Url（EndpointReference）";
		agr17[2] = "";
		agr17[3] = "http://192.168.163.128/archivesfile/services/DataInterfaceService.asmx?wsdl";
		agr17[4] = STR_TRUE;
		disComCfgList.add(agr17);
		
		String[] agr18 = new String[5];
		agr18[0] = "TargetNamespace";
		agr18[1] = "系统集成-瀚海之星WebService的WSDL文件的命名空间";
		agr18[2] = "";
		agr18[3] = "http://www.shsasc.com/Archives";
		agr18[4] = STR_TRUE;
		disComCfgList.add(agr18);

		String[] agr19 = new String[5];
		agr19[0] = "MethodName";
		agr19[1] = "系统集成-瀚海之星WebService的上传文件方法名";
		agr19[2] = "";
		agr19[3] = "UploadDIMonitorFile";
		agr19[4] = STR_TRUE;
		disComCfgList.add(agr19);
		
		String[] agr20 = new String[5];
		agr20[0] = "Is_EncryptElcFile";
		agr20[1] = "系统集成-是否加密分发数据的电子文件(true:是;false:否)";
		agr20[2] = "";
		agr20[3] = STR_FALSE;
		agr20[4] = STR_FALSE;
		disComCfgList.add(agr20);
		
//		String[] agr21 = new String[5];
//		agr21[0] = "EncryptElcKeyFileName";
//		agr21[1] = "系统集成-密钥存储文件名(注：包含路径的文件名，分发数据的电子文件需要加密时设置,并将此文件发送给瀚海系统)";
//		agr21[2] = "";
//		agr21[3] = "plm/ddm/desKeyFile/hanhaiElecFileKey.dat";
//		agr21[4] = STR_FALSE;
//		disComCfgList.add(agr21);
		
		String[] agr22 = new String[5];
		agr22[0] = "Is_OpenPaperSignTask";
		agr22[1] = "纸质签收任务-是否启用纸质签收任务(true:是;false:否)";
		agr22[2] = "";
		agr22[3] = STR_FALSE;
		agr22[4] = STR_FALSE;
		disComCfgList.add(agr22);
		
		return disComCfgList;
	}
	
}
