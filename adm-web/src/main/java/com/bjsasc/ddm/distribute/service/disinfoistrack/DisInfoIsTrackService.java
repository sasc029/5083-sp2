package com.bjsasc.ddm.distribute.service.disinfoistrack;
import java.util.List;

import com.bjsasc.ddm.distribute.model.disinfoistrack.DisInfoIsTrack;
import com.bjsasc.ddm.distribute.model.distributeinfo.DistributeInfo;

/**
 * �Ƿ���ٷ���ӿڡ�
 * 
 * @author xuhuiling 2013-9-12
 */
public interface DisInfoIsTrackService {

	/**
	 * �����Ƿ���ٶ���
	 * 
	 * @param dis DisInfoIsTrack
	 */
	public void createDisInfoIsTrack(DisInfoIsTrack dis);

	/**
	 * �����Ƿ���ٶ���
	 * 
	 * @param disInfoObj DistributeInfo, isTrack int
	 */
	public void createDisInfoIsTrack(DistributeInfo disInfoObj, int isTrack);

	/**
	 * �����Ƿ���ٶ���
	 * 
	 * @return DisInfoIsTrack
	 */
	public DisInfoIsTrack newDisInfoIsTrack();

	/**
	 * ȡ���Ƿ���ٶ���
	 * 
	 * @return DisInfoIsTrack
	 */
	public List<DisInfoIsTrack> getDisInfoIsTrackByDisInfoOid(String disInfoOid);
	
	
}
