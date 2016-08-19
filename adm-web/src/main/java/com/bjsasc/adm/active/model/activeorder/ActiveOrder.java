package com.bjsasc.adm.active.model.activeorder;

import org.apache.log4j.Logger;

import com.bjsasc.adm.active.model.ActiveBase;
import com.bjsasc.adm.active.model.ActiveObjOrder;
import com.bjsasc.adm.active.model.ActiveSeted;
import com.bjsasc.adm.active.model.AdmMaster;
import com.bjsasc.adm.active.model.Recycledable;
import com.bjsasc.plm.core.attachment.FileHolder;
import com.bjsasc.plm.core.context.model.Context;
import com.bjsasc.plm.core.folder.Folder;
import com.bjsasc.plm.core.folder.FolderHelper;
import com.bjsasc.plm.core.folder.FolderInfo;
import com.bjsasc.plm.core.type.TypeDefinition;
import com.bjsasc.plm.core.type.TypeHelper;
import com.bjsasc.plm.core.util.DateTimeUtil;

/**
 * 现行单据模型。
 * 
 * @author yanjia 2013-5-13
 */
public class ActiveOrder extends ActiveBase implements ActiveSeted,
		Recycledable, FileHolder, ActiveObjOrder {

	/** serialVersionUID */
	private static final long serialVersionUID = -1649908282603536516L;

	public static final String CLASSID = ActiveOrder.class.getSimpleName();
	
	private static final Logger LOG = Logger.getLogger(ActiveOrder.class);

	/** 数据来源 */
	private String dataSource;
	/** 作者名称 */
	private String authorName;
	/** 文件编制时间 */
	private long authorTime;
	/** 作者所属单位 */
	private String authorUnit;
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
	 * 
	 * @return dataSource
	 * */
	public String getDataSource() {
		return dataSource;
	}

	/**
	 * 设置数据源
	 * 
	 * @param dataSource
	 *            要设置的dataSource
	 * */
	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}

	/**
	 * 取得作者名称
	 * 
	 * @return authorName
	 */
	public String getAuthorName() {
		return authorName;
	}

	/**
	 * 设置作者名称
	 * 
	 * @param authorName
	 *            要设置的 authorName
	 */
	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}

	/**
	 * 取得文件编制时间
	 * 
	 * @return authorTime
	 */
	public long getAuthorTime() {
		return authorTime;
	}

	/**
	 * 设置文件编制时间
	 * 
	 * @param authorTime
	 *            要设置的 authorTime
	 */
	public void setAuthorTime(long authorTime) {
		this.authorTime = authorTime;
	}
	
	public String getDisAuthorTime() {
		return DateTimeUtil.dateDisplay(authorTime, DateTimeUtil.DATE_YYYYMMDD);
	}

	/**
	 * 取得作者所属单位
	 * 
	 * @return authorUnit
	 */
	public String getAuthorUnit() {
		return authorUnit;
	}

	/**
	 * 设置作者所属单位
	 * 
	 * @param authorUnit
	 *            要设置的 authorUnit
	 */
	public void setAuthorUnit(String authorUnit) {
		this.authorUnit = authorUnit;
	}

	/**
	 * 取得页数
	 * 
	 * @return pages
	 */
	public int getPages() {
		return pages;
	}

	/**
	 * 设置页数
	 * 
	 * @param pages
	 *            要设置的 pages
	 */
	public void setPages(int pages) {
		this.pages = pages;
	}

	/**
	 * 取得份数
	 * 
	 * @return count
	 */
	public int getCount() {
		return count;
	}

	/**
	 * 设置份数
	 * 
	 * @param count
	 *            要设置的 count
	 */
	public void setCount(int count) {
		this.count = count;
	}

	/**
	 * 取得备注
	 * 
	 * @return note
	 */
	public String getNote() {
		return note;
	}

	/**
	 * 设置备注
	 * 
	 * @param note
	 *            要设置的 note
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
		try {
			// 文件夹信息
			FolderInfo folderInfo = getFolderInfo();
			if (folderInfo != null) {
				Folder folder = folderInfo.getFolder();
				if (folder != null) {
					// 文件夹PATH
					return FolderHelper.getService().getFolderPathStr(folder);
				}
			}
		} catch (Exception ex) {
			LOG.debug("文件夹信息取得失败。");
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
