package com.bjsasc.adm.active.model.activeset;

import com.bjsasc.adm.active.model.ActiveBase;
import com.bjsasc.adm.active.model.ActiveObjSet;
import com.bjsasc.adm.active.model.ActiveOrdered;
import com.bjsasc.adm.active.model.AdmMaster;
import com.bjsasc.adm.active.model.Recycledable;
import com.bjsasc.plm.core.context.model.Context;
import com.bjsasc.plm.core.folder.Folder;
import com.bjsasc.plm.core.folder.FolderHelper;
import com.bjsasc.plm.core.folder.FolderInfo;
import com.bjsasc.plm.core.type.TypeDefinition;
import com.bjsasc.plm.core.type.TypeHelper;

/**
 * 现行套模型。
 * 
 * @author yanjia 2013-5-13
 */
public class ActiveSet extends ActiveBase implements Recycledable, ActiveObjSet, ActiveOrdered {

	/** serialVersionUID */
	private static final long serialVersionUID = -6509148365523073600L;

	public static final String CLASSID = ActiveSet.class.getSimpleName();

	/** 数据来源 */
	private String dataSource;
	/** 页数 */
	private int pages;
	/** 份数 */
	private int count;
	/** 备注 */
	private String note;
	/** 现行文件编号 */
	private String activeDocumentNumber;
	/** 代号 */
	private String activeCode;

	@Override
	public String getPropertiesUrl() {
		return "/plm/common/visit.jsp";
	}

	/**
	 * 取得数据源
	 * @return dataSource
	 * */
	public String getDataSource() {
		return dataSource;
	}

	/**
	 * 设置数据源
	 * @param dataSource 要设置的dataSource
	 * */
	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}

	/**
	 * 取得页数
	 * @return pages
	 */
	public int getPages() {
		return pages;
	}

	/**
	 * 设置页数
	 * @param pages 要设置的 pages
	 */
	public void setPages(int pages) {
		this.pages = pages;
	}

	/**
	 * 取得份数
	 * @return count
	 */
	public int getCount() {
		return count;
	}

	/**
	 * 设置份数
	 * @param count 要设置的 count
	 */
	public void setCount(int count) {
		this.count = count;
	}

	/**
	 * 取得备注
	 * @return note
	 */
	public String getNote() {
		return note;
	}

	/**
	 * 设置备注
	 * @param note 要设置的 note
	 */
	public void setNote(String note) {
		this.note = note;
	}
	
	public String getActiveDocumentNumber() {
		return activeDocumentNumber;
	}

	public void setActiveDocumentNumber(String activeDocumentNumber) {
		this.activeDocumentNumber = activeDocumentNumber;
	}

	public String getActiveCode() {
		return activeCode;
	}

	public void setActiveCode(String activeCode) {
		this.activeCode = activeCode;
	}

	public String getFolderPath() {
		// 文件夹信息
		FolderInfo folderInfo = getFolderInfo();
		if (folderInfo != null) {
			Folder folder = folderInfo.getFolder();
			if (folder != null) {
				// 文件夹PATH
				return FolderHelper.getService().getFolderPathStr(folder);
			}
		}
		return "";
	}
	
	public String getModelName() {
		TypeDefinition sourceDef = TypeHelper.getService().getType(getClassId());
		return sourceDef.getName();
	}

	public String getContextName() {
		Context context = getContextInfo().getContext();
		// 上下文NAME
		return context.getName();
	}

	public String getSecLevelName() {
		try {
			AdmMaster master = (AdmMaster) getMaster();
			return master.getSecurityLevelInfo().getSecurityLevel().getName();
		} catch (Exception ex) {
			return "";
		}
	}

	public String getVersionName() {
		return getIterationInfo().getFullVersionNo();
	}
}
