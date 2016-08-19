package com.bjsasc.ddm.distribute.listener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.bjsasc.platform.objectmodel.business.transaction.HibernateUtil;
import com.bjsasc.plm.core.event.BeforeDeletePersistableEvent;
import com.bjsasc.plm.core.persist.model.Persistable;
import com.bjsasc.plm.core.util.LogUtil;
import com.bjsasc.plm.core.util.PlmException;
import com.cascc.platform.event.AbstractListener;
import com.cascc.platform.event.Event;
import com.cascc.platform.event.Listener;
/**

/**
 * 删除对象前的发放单检查监听
 * 
 * @author zhangguoqiang 2014-10-14
 */
public class DistributeOrderDeleteListener extends AbstractListener implements Listener {

	/** serialVersionUID */
	private static final long serialVersionUID = 3555568968721094675L;

	@Override
	public void raiseEvent(Event event) {
		LogUtil.beginMethod("com.bjsasc.ddm.distribute.listener.DistributeOrderDeleteListener.raiseEvent", event);
		//删除持久化对象前的事件
		if(event instanceof BeforeDeletePersistableEvent){
			//取得待删除的对象列表
			Collection<?> deleteObjs = ((BeforeDeletePersistableEvent) event).getDeleteObjs();
			
			List<String> innerIds = new ArrayList<String>();
			List<String> innerIds1 = new ArrayList<String>();
			List<String> innerIds2 = new ArrayList<String>();
			List<String> innerIds3 = new ArrayList<String>();
			
			if(deleteObjs != null && deleteObjs.size() > 0){
				Persistable target = null;
				for(Object obj : deleteObjs){
					if(obj instanceof Persistable){
						target = (Persistable)obj;
						if(innerIds.size()<1000){
							innerIds.add(target.getInnerId());
						}else if(innerIds1.size()<1000){
							innerIds1.add(target.getInnerId());
						}else if(innerIds2.size()<1000){
							innerIds2.add(target.getInnerId());
						}else if(innerIds3.size()<1000){
							innerIds3.add(target.getInnerId());
						}
					}
				}
				
				if(innerIds.size() == 0){
					return;
				}
				//判断分发数据当中是否包含待删除的对象
				StringBuffer hql = new StringBuffer("select count(innerId) as IdCount from  DistributeObject t "
						+ " where t.dataInnerId in(:idList) ");
				if(innerIds1.size()>0){
					hql.append(" or t.dataInnerId in(:idList1) ");
				}
				if(innerIds2.size()>0){
					hql.append(" or t.dataInnerId in(:idList2) ");
				}
				if(innerIds3.size()>0){
					hql.append(" or t.dataInnerId in(:idList3) ");
				}
				Session session = HibernateUtil.getSessionFactory("AvidmSystemDB").getCurrentSession();
				Query query=session.createQuery(hql.toString());
				query.setParameterList("idList", innerIds);
				if(innerIds1.size()>0){
					query.setParameterList("idList1", innerIds1);
				}
				if(innerIds2.size()>0){
					query.setParameterList("idList2", innerIds2);
				}
				if(innerIds3.size()>0){
					query.setParameterList("idList3", innerIds3);
				}
				
				Object value = query.uniqueResult();
				
				boolean delete = true;
				if(value instanceof Long){
					if(((Long)value).longValue() > 0){
						delete = false;
					}
				}else if(value instanceof Integer){
					if(((Integer)value).intValue() > 0){
						delete = false;
					}
				}
				
				if(!delete){
					throw new PlmException("ddm.listener.objNotAllowDel");
				}
			}
			
		}
	}
}
