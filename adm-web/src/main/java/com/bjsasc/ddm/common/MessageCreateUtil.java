package com.bjsasc.ddm.common;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import com.bjsasc.plm.collaboration.a4x.model.DmcConvertData;
import com.bjsasc.plm.collaboration.a4x.model.ObjectMessage;
import com.bjsasc.plm.collaboration.a4x.model.StoreFile;
import com.bjsasc.plm.collaboration.a4x.model.StoreObject;
import com.bjsasc.plm.collaboration.a4x.util.MessageConstant;
import com.cascc.platform.uuidservice.UUID;

public class MessageCreateUtil {

	public static DmcConvertData createMessage(ObjectMessage om, List listData, List paths) {
		DmcConvertData msgData = new DmcConvertData();
		List soList = new ArrayList();
		List sFList = new ArrayList();
		try {
			om.setMsgState(MessageConstant.MESSAGE_STATE_NEW);
			// 生成SO
			if (listData != null && !listData.isEmpty()) {
				
				int objNum = 200;
				objNum = getObjectNum(listData.size(), objNum);
				// 拆分存储
				Vector vec = new Vector();
				for (Iterator it = listData.iterator(); it.hasNext();) {
					vec.add(it.next());
					if (vec.size() == objNum || !it.hasNext()) {
						// 生成so对象
						StoreObject so = new StoreObject();
						// 创建SO
						so.setSoIID(UUID.getUID());
						so.setMsgIID(om.getMsgIID());
						so.setDataObject(vec);
						so.setState(MessageConstant.MESSAGE_STATE_NEW);
						soList.add(so);
						vec = new Vector();
					}
				}
			}
			if (paths != null) {
				Iterator it = paths.iterator();
				while (it.hasNext()) {
					String path = (String) it.next();
					File file = new File(path);
					StoreFile sf = new StoreFile();
					sf.setFileIID(UUID.getUID());
					sf.setFileName(file.getName());
					sf.setFilePath(path);
					sf.setFileSize(file.length()); // 文件长度
					sf.setMsgIID(om.getMsgIID());
					sFList.add(sf);
				}
			}
			msgData.setOm(om);
			msgData.setSoList(soList);
			msgData.setSoFileList(sFList);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return msgData;
	}
	
	
	/**
	 * 在进行存储时候消息对象附件内的个数
	 *@param objNum
	 *@param num
	 *@return
	 *@return int
	 */
	public static int getObjectNum(long objNum,int num){
		if(objNum>=500000){
			return 10000;
		}
		if(objNum<500000&&objNum>=100000){
			return 5000;
		}
		if(objNum>=1000&&objNum<100000){
			return 1000;
		}
		return num; 
	}
}
