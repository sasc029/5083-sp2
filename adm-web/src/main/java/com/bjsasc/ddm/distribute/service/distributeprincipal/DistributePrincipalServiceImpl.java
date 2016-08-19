package com.bjsasc.ddm.distribute.service.distributeprincipal;

import java.util.ArrayList;
import java.util.List;

import com.bjsasc.plm.Helper;
import com.bjsasc.plm.core.context.model.ContextInfo;
import com.bjsasc.plm.core.domain.Domain;
import com.bjsasc.plm.core.domain.DomainHelper;
import com.bjsasc.plm.core.domain.Domained;
import com.bjsasc.plm.core.privilege.acl.ACLData;
import com.bjsasc.plm.core.privilege.acl.ACLDataHelper;
import com.bjsasc.plm.core.session.SessionHelper;
import com.bjsasc.plm.core.system.access.operate.Operate;
import com.bjsasc.plm.core.system.principal.User;
import com.cascc.platform.aa.AAProvider;
import com.cascc.platform.aa.api.ResRightsMgrService;
import com.cascc.platform.aa.api.data.AAOperationData;
import com.cascc.platform.aa.api.data.AAUserData;
import com.cascc.platform.aa.api.util.AADomainUtil;

/**
 * 发放授权服务实现类
 * 
 * @author guowei 2014-03-05
 * @modify Sun Zongqing 2014/7/9
 */
@SuppressWarnings({"rawtypes","unchecked"})
public class DistributePrincipalServiceImpl implements DistributePrincipalService{
	
	public void setPriviledge(Domained domained, User currentUser){
		List accList = new ArrayList();
		List rejList = new ArrayList();
		int objDomainRef = domained.getDomainInfo().getDomain().getAaDomainRef();
		Domain domain = DomainHelper.getService().getDomain(objDomainRef);
		ContextInfo contextInfo = domain.getContextInfo();
		String userId = currentUser.getAaUserInnerId();
		AAUserData user = AAProvider.getUserService().getUser(SessionHelper.getService().getSessionContext(),
				userId);
		String userInnerId = user.getInnerId();
		String userName = user.getName();
		
		String typeId = Helper.getTypeService().getTargetClassId(domained.getClassId());
		
		// 访问权限
		//accList.add(5);
		ResRightsMgrService resServ = AAProvider.getResRightsMgrService();
		
		//根据对象权限项中是否有“访问”和“下载”来确定是否需要进行授权
		String depTypeIID = AADomainUtil.getDependType(typeId);
		List<Integer> idxes = resServ.listIdx4DepType(null, depTypeIID);
		for(Integer idx:idxes){
			AAOperationData opt = resServ.getOptByNo(null, idx);
			if(opt.getOperationID().equals(Operate.ACCESS)){
				accList.add(resServ.getOptByID(null, Operate.ACCESS).getOperationNo());
			}else if(opt.getOperationID().equals(Operate.DOWNLOAD)){
				accList.add(resServ.getOptByID(null, Operate.DOWNLOAD).getOperationNo());
			}
		}
		
		String defaultPrivilege = AADomainUtil.getAclValue(typeId, accList, rejList);
		ACLData aclData = Helper.getAclService().prepareUserAclData(AADomainUtil.ACLCATEGORY_OBJECT,
				domained.getInnerId(), domained.getClassId(), objDomainRef, userInnerId, userName,
				defaultPrivilege);
		if(aclData!=null){
			aclData.setContextInfo(contextInfo);
			List<ACLData> operationPlyList = new ArrayList<ACLData>();
			operationPlyList.add(aclData);
			Helper.getPersistService().save(operationPlyList);
			ACLDataHelper.getService().afterUpdateAcls(operationPlyList);
		}else{
			System.out.println("发放管理设置权限过程中(DistributePrincipalServiceImpl.setPriviledge()方法)获取到的aclData对象为null");
		}
	}

	
	public List<ACLData> getPriviledgeData(Domained domained, User currentUser){
		List accList = new ArrayList();
		List rejList = new ArrayList();
		int objDomainRef = domained.getDomainInfo().getDomain().getAaDomainRef();
		String userId = currentUser.getAaUserInnerId();
		AAUserData user = AAProvider.getUserService().getUser(SessionHelper.getService().getSessionContext(),
				userId);
		String userInnerId = user.getInnerId();
		String userName = user.getName();
		// 访问权限
		//accList.add(5);
		ResRightsMgrService resServ = AAProvider.getResRightsMgrService();
		//access
		accList.add(resServ.getOptByID(null, Operate.ACCESS).getOperationNo());
		accList.add(resServ.getOptByID(null, Operate.DOWNLOAD).getOperationNo());
		String typeId = Helper.getTypeService().getTargetClassId(domained.getClassId());
		String defaultPrivilege = AADomainUtil.getAclValue(typeId, accList, rejList);
		ACLData aclData = Helper.getAclService().prepareUserAclData(AADomainUtil.ACLCATEGORY_OBJECT,
				domained.getInnerId(), domained.getClassId(), objDomainRef, userInnerId, userName,
				defaultPrivilege);
		Domain domain = DomainHelper.getService().getDomain(objDomainRef);
		ContextInfo contextInfo = domain.getContextInfo();
		aclData.setContextInfo(contextInfo);
		List<ACLData> operationPlyList = new ArrayList<ACLData>();
		operationPlyList.add(aclData);
		return operationPlyList;
	}
	
}
