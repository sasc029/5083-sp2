package com.bjsasc.ddm.distribute.service.distributeinfoconfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.bjsasc.ddm.distribute.helper.DistributeHelper;
import com.bjsasc.ddm.distribute.model.distributeinfoconfig.DistributeInfoConfig;
import com.bjsasc.platform.objectmodel.business.persist.PTFactor;
import com.bjsasc.platform.objectmodel.business.persist.PersistUtil;
import com.bjsasc.plm.Helper;
import com.bjsasc.plm.KeyS;
import com.bjsasc.plm.core.persist.PersistHelper;
import com.bjsasc.plm.core.persist.model.Persistable;
import com.bjsasc.plm.core.system.principal.Organization;
import com.bjsasc.plm.core.system.principal.OrganizationHelper;
import com.bjsasc.plm.core.system.principal.Principal;
import com.bjsasc.plm.core.system.principal.User;
import com.bjsasc.plm.core.system.principal.UserHelper;
import com.bjsasc.plm.grid.GridHelper;
import com.cascc.avidm.util.SplitString;

/**
 * 默认分发信息配置服务实现类
 * 
 * @author yangqun  2013-05-09
 *
 */
public class DistributeInfoConfigServiceImpl implements
		DistributeInfoConfigService {

	public List<DistributeInfoConfig> getAllDistributeInfoConfig() {

		String hql = "from DistributeInfoConfig";
		List<DistributeInfoConfig> disInfoConfigList = new ArrayList();
		disInfoConfigList = PersistHelper.getService().find(hql);
		return disInfoConfigList;
	}

	/* (non-Javadoc)
	 * @see com.bjsasc.ddm.distribute.service.distributeinfoconfig.DistributeInfoConfigService#listDistributeInfoConfig(java.lang.String, java.lang.String)
	 */
	public List<Map<String, Object>> listDistributeInfoConfig(String spot,
			String spotInstance) {

		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		List<DistributeInfoConfig> disInfoConfigList = getAllDistributeInfoConfig();
		List<String> keys = GridHelper.getService()
				.getMyLatestGridViewColumnIds(spot, spotInstance);

		for (Object obj : disInfoConfigList) {
			Map<String, Object> map = Helper.getTypeManager().format(
					(PTFactor) obj, keys, false);
			map.put(KeyS.ACCESS, true);
			resultList.add(map);
		}
		return resultList;
	}
	//过滤重复的用户或者组织
	public boolean filtSameUserOrOrgnization(DistributeInfoConfig distributeInfoConfig){
		boolean flag=false;
		String hql = "from DistributeInfoConfig d where d.disInfoId=?";
		@SuppressWarnings("unchecked")
		List<DistributeInfoConfig> disInfoConfigList = PersistHelper.getService().find(hql,distributeInfoConfig.getDisInfoId());
		if (disInfoConfigList!=null&&disInfoConfigList.size()!=0) {
			for (int i = 0; i < disInfoConfigList.size(); i++) {
				DistributeInfoConfig config=disInfoConfigList.get(i);
				if (config.getDisMediaType().equals(distributeInfoConfig.getDisMediaType())) {
					flag=true;
					break;
				}
			}
			
		};
		return flag;
		
	}
	
	public boolean addDistributeInfoConfig(String type,String iids,String disMediaTypes,String disInfoNums,String notes){
		
		 List<DistributeInfoConfig> distributeInfoConfigList=new ArrayList<DistributeInfoConfig>();
		
		 List<String> disInfoIdsList = SplitString.string2List(iids, ",");
		 List<String> disMediaTypesList = SplitString.string2List(disMediaTypes, ",");
		 List<String> disInfoNumsList = SplitString.string2List(disInfoNums, ",");
		 List<String> notesList = SplitString.string2List(notes, ",");
		
		 boolean flag=false;
		 DistributeInfoConfigService infoConfigservice = DistributeHelper.getDistributeInfoConfigService();
		 for(int i=0;i<disInfoIdsList.size();i++){
			
			 DistributeInfoConfig configInfo= (DistributeInfoConfig) PersistUtil.createObject(DistributeInfoConfig.CLASSID);
			 
			 if(type.equals("ORG")){
				 
				 Principal princi = OrganizationHelper.getService().getOrganization(disInfoIdsList.get(i));
				 Persistable p=Helper.getPersistService().getObject(princi.getClassId()+":"+princi.getInnerId());
				 Organization organiz=(Organization)p;
				 configInfo.setDisInfoName(organiz.getName());
				 configInfo.setDisInfoType("0");
				 
			 }if(type.equals("USER")){
				 Principal princi = UserHelper.getService().getUser(disInfoIdsList.get(i));
				 Persistable p=Helper.getPersistService().getObject(princi.getClassId()+":"+princi.getInnerId());
				 User userinfo=(User)p;
				 configInfo.setDisInfoName(userinfo.getName());
				 configInfo.setDisInfoType("1");
			 }
			 
			 configInfo.setDisInfoId(disInfoIdsList.get(i));
			 configInfo.setDisInfoNum(Long.parseLong(disInfoNumsList.get(i)));
			 configInfo.setDisMediaType(disMediaTypesList.get(i));
			 configInfo.setNote(String.valueOf(notesList.get(i)));
			 if (notesList.get(i).equals("&nbsp;")) {
				 configInfo.setNote("");
			}
			 
			 configInfo.setClassId(configInfo.CLASSID);
			 
			 flag=infoConfigservice.filtSameUserOrOrgnization(configInfo);
		     if (flag){
			  break;
		     }
		     distributeInfoConfigList.add(configInfo);
		 }
		 
	    if (!flag) {
			 addDistributeInfoConfigList(distributeInfoConfigList);
		}
		 
		 return flag;
		 
		 
	}
	@Override
	public void addDistributeInfoConfig(
			DistributeInfoConfig distributeInfoConfig) {
		PersistHelper.getService().save(distributeInfoConfig);
	}
	
	public void addDistributeInfoConfigList(
			List<DistributeInfoConfig> distributeInfoConfigList) {
		PersistHelper.getService().save(distributeInfoConfigList);
	}

	@Override
	public void delDistributeInfoConfig(
			DistributeInfoConfig distributeInfoConfig) {
		PersistHelper.getService().delete(distributeInfoConfig);
	}

	@Override
	public void updateDistributeInfoConfig(
			DistributeInfoConfig distributeInfoConfig) {
		PersistHelper.getService().update(distributeInfoConfig);
	}
}
