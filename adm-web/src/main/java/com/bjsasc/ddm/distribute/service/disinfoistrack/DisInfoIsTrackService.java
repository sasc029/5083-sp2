package com.bjsasc.ddm.distribute.service.disinfoistrack;
import java.util.List;

import com.bjsasc.ddm.distribute.model.disinfoistrack.DisInfoIsTrack;
import com.bjsasc.ddm.distribute.model.distributeinfo.DistributeInfo;

/**
 * 是否跟踪服务接口。
 * 
 * @author xuhuiling 2013-9-12
 */
public interface DisInfoIsTrackService {

	/**
	 * 创建是否跟踪对象。
	 * 
	 * @param dis DisInfoIsTrack
	 */
	public void createDisInfoIsTrack(DisInfoIsTrack dis);

	/**
	 * 创建是否跟踪对象。
	 * 
	 * @param disInfoObj DistributeInfo, isTrack int
	 */
	public void createDisInfoIsTrack(DistributeInfo disInfoObj, int isTrack);

	/**
	 * 创建是否跟踪对象。
	 * 
	 * @return DisInfoIsTrack
	 */
	public DisInfoIsTrack newDisInfoIsTrack();

	/**
	 * 取得是否跟踪对象。
	 * 
	 * @return DisInfoIsTrack
	 */
	public List<DisInfoIsTrack> getDisInfoIsTrackByDisInfoOid(String disInfoOid);
	
	
}
