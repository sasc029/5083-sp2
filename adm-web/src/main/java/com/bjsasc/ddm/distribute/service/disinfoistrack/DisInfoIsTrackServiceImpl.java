package com.bjsasc.ddm.distribute.service.disinfoistrack;
import java.util.List;

import com.bjsasc.ddm.distribute.model.disinfoistrack.DisInfoIsTrack;
import com.bjsasc.ddm.distribute.model.distributeinfo.DistributeInfo;
import com.bjsasc.platform.objectmodel.business.persist.PersistUtil;
import com.bjsasc.plm.Helper;
import com.bjsasc.plm.core.persist.PersistHelper;

/**
 * 是否跟踪服务实现类。
 * 
 * @author xuhuiling 2013-9-12
 */
@SuppressWarnings({ "deprecation", "unchecked", "static-access" })
public class DisInfoIsTrackServiceImpl implements DisInfoIsTrackService {

	/* （非 Javadoc）
	 * @see com.bjsasc.ddm.distribute.service.disinfoistrack.DisInfoIsTrackService#createDisInfoIsTrack(com.bjsasc.ddm.distribute.model.disinfoistrack.DisInfoIsTrack)
	 */
	@Override
	public void createDisInfoIsTrack(DisInfoIsTrack disObject) {
		Helper.getPersistService().save(disObject);

	}

	/* （非 Javadoc）
	 * @see com.bjsasc.ddm.distribute.service.disinfoistrack.DisInfoIsTrackService#createDisInfoIsTrack(com.bjsasc.ddm.distribute.model.disinfoistrack.DisInfoIsTrack)
	 */
	@Override
	public void createDisInfoIsTrack(DistributeInfo disInfoObj, int isTrack) {
		DisInfoIsTrack disObj = newDisInfoIsTrack();
		disObj.setDisInfoId(disInfoObj.getInnerId());
		disObj.setInfoClassId(disInfoObj.getClassId());
		disObj.setIstrack(isTrack);
		createDisInfoIsTrack(disObj);

	}

	/* （非 Javadoc）
	 * @see com.bjsasc.ddm.distribute.service.disinfoistrack.DisInfoIsTrackService#newDisInfoIsTrack()
	 */
	@Override
	public DisInfoIsTrack newDisInfoIsTrack() {
		DisInfoIsTrack dis = (DisInfoIsTrack) PersistUtil.createObject(DisInfoIsTrack.CLASSID);

		dis.setClassId(dis.CLASSID);
		return dis;
	}

	/* （非 Javadoc）
	 * @see com.bjsasc.ddm.distribute.service.disinfoistrack.DisInfoIsTrackService#getDisInfoIsTrackByDisInfoOid()
	 */
	@Override
	public List<DisInfoIsTrack> getDisInfoIsTrackByDisInfoOid(String disInfoOid){
		String hql = "from DisInfoIsTrack t where t.disInfoId=? and t.infoClassId=? ";
		String disInfoId = Helper.getInnerId(disInfoOid);
		String infoClassId = Helper.getClassId(disInfoOid);
		List<DisInfoIsTrack> disInfoIsTrackList = PersistHelper.getService().find(hql, disInfoId, infoClassId);
		return disInfoIsTrackList;
	}
}
