package com.bjsasc.ddm.distribute.event;

import com.bjsasc.ddm.distribute.model.distributeorder.DistributeOrder;
import com.cascc.platform.event.AbstractEvent;

/**
 * 
 * @author kangyanfei
 * 2014-07-18
 *
 */
public class CreateDistributeOrderSucessEvent extends AbstractEvent {


	private static final long serialVersionUID = 3557306587712921766L;

	/*
	 * ·Å·Åµ¥
	 */
	private DistributeOrder disOrder;

	public DistributeOrder getDisOrder() {
		return disOrder;
	}

	public void setDisOrder(DistributeOrder disOrder) {
		this.disOrder = disOrder;
	}

	
	

}
